package ui

import entities.Entity
import game.{GamePanel, GameState}
import utils.Tools

import java.awt.{Color, Font, Graphics2D}
import scala.collection.mutable.ListBuffer


class UI (var gp: GamePanel):
  var font_40: Font = new Font("Arial", Font.PLAIN ,40)
  var messageOn : Boolean = false
  var message: ListBuffer[String] = ListBuffer()
  var messageCounter: ListBuffer[Int] = ListBuffer()

  var npc: Entity = _
  var isFinished = false
  var currentDialogue: String = ""
  var commandNum = 0
  var counter: Int = 0

  var charIndex: Int = 0
  var combinedText: String = ""

  var g2 : Graphics2D = _
  val tileSize = gp.tileSize

  var subState = 0

  def setGraphics(g: Graphics2D): Unit =
    g2 = g
    PlayerUI.setGraphics(g, gp)
    GameMenuUI.setGraphics(g, gp)
    TradeUI.setGraphics(g, gp)

  def drawUI (g : Graphics2D): Unit =
    setGraphics(g)
    g.setFont(font_40)
    g.setColor(Color.WHITE)

    gp.gameState match
      case GameState.PlayState =>
        PlayerUI.drawPlayerLife()
        PlayerUI.drawPlayerMana()
        drawMessage()
      case GameState.PauseState =>
        PlayerUI.drawPlayerLife()
        PlayerUI.drawPlayerMana()
        drawPauseScreen()
      case GameState.DialogueState =>
        PlayerUI.drawPlayerLife()
        PlayerUI.drawPlayerMana()
        drawDialogueScreen()
      case GameState.TitleState =>
        drawTitleScreen()
      case GameState.CharacterState =>
        PlayerUI.drawCharacterState()
        PlayerUI.drawInventory(gp.player, true)
      case GameState.GameMenu =>
        drawGameMenu()
      case GameState.GameOver =>
        drawGameOver()
      case GameState.TransitionState =>
        drawTransitionState()
      case GameState.TradeState =>
        TradeUI.drawTradeState()
      case GameState.MapState => 

  def drawMessage(): Unit =
    val messageX = tileSize / 2
    var messageY = tileSize * 4
    g2.setFont(g2.getFont.deriveFont(Font.BOLD, 15F))

    for index <- message.indices.reverse do
      if message(index) != null then
        g2.setColor(Color.BLACK)
        g2.drawString(message(index), messageX + 1, messageY + 1)
        g2.setColor(Color.WHITE)
        g2.drawString(message(index), messageX, messageY)

        val counter = messageCounter(index) + 1
        messageCounter(index) = counter
        messageY += 30

        if messageCounter(index) > 150 then
          message.remove(index)
          messageCounter.remove(index)

  def drawTitleScreen(): Unit =
    g2.setColor(new Color(208, 147, 62))
    g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight)

    g2.setFont(g2.getFont.deriveFont(Font.BOLD, 70F))
    var text = "Adventure to Greece"
    var x = Tools.getCenterX(g2, gp, text)
    var y = tileSize * 3
    //Shadow
    g2.setColor(Color.BLACK)
    g2.drawString(text, x + 5, y + 5)
    //
    g2.setColor(Color.WHITE)
    g2.drawString(text, x ,y)

    //Player image
    x = gp.screenWidth / 2
    y += tileSize * 2
//    g2.drawImage( x, y, tileSize, tileSize, null)
    //MENU
    g2.setFont(g2.getFont.deriveFont(Font.BOLD, 40F))
    text = "NEW GAME"
    x = Tools.getCenterX(g2, gp, text)
    y += tileSize * 4
    g2.drawString(text, x, y)
    if commandNum == 0 then g2.drawString(">", x - tileSize, y)

    text = "LOAD GAME"
    x = Tools.getCenterX(g2, gp, text)
    y += tileSize
    g2.drawString(text, x, y)
    if commandNum == 1 then g2.drawString(">", x - tileSize, y)

    text = "QUIT GAME"
    x = Tools.getCenterX(g2, gp, text)
    y += tileSize
    g2.drawString(text, x, y)
    if commandNum == 2 then g2.drawString(">", x - tileSize, y)

  def drawDialogueScreen(): Unit =
    var (x, y , width, height) = (
      tileSize * 3,
      tileSize / 2,
      gp.screenWidth - (tileSize * 6),
      tileSize * 3
    )
    Tools.drawSubWindow(g2, x, y, width, height)

    g2.setFont(g2.getFont.deriveFont(Font.PLAIN, 25F))
    x += tileSize; y += tileSize

    if npc.dialogues(npc.dialogueSet)(npc.dialogueIndex) != null then
//      currentDialogue = npc.dialogues(npc.dialogueSet)(npc.dialogueIndex)
      val charactersSet: Array[Char] = npc.dialogues(npc.dialogueSet)(npc.dialogueIndex).toCharArray
      if charIndex < charactersSet.length then
        //play se
        //
        combinedText += charactersSet(charIndex).toString
        currentDialogue = combinedText
        charIndex += 1

      if gp.keyH.enterPressed then
        charIndex = 0
        combinedText = ""
        if gp.gameState == GameState.DialogueState then
          npc.dialogueIndex += 1
          gp.keyH.enterPressed = false
    else
      npc.dialogueIndex = 0
      if gp.gameState == GameState.DialogueState then
        gp.gameState = GameState.PlayState

    currentDialogue.split("\n").foreach( line =>
      g2.drawString(line, x, y)
      y += 40
    )

  def drawPauseScreen(): Unit =
    val text: String = "PAUSE"
    val x: Int = Tools.getCenterX(g2, gp, text)
    val y: Int = gp.screenHeight / 2
    g2.drawString(text, x ,y)

  def drawGameMenu(): Unit =
    g2.setColor(Color.WHITE)
    g2.setFont(g2.getFont.deriveFont(28F))
    val frameX = (tileSize * 7)
    val frameY = tileSize
    val frameWidth = tileSize * 8
    val frameHeight = tileSize * 10
    Tools.drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight)

    subState match
      case 0 => GameMenuUI.gameMenuTop(frameX, frameY)
      case 1 => GameMenuUI.controlDisplay(frameX, frameY)
      case 2 => GameMenuUI.endGameConfirmation(frameX, frameY)
      case _ =>
    gp.keyH.enterPressed = false

  def addMessage(text : String) =
    message += text
    messageCounter += 0

  def drawGameOver(): Unit =
    g2.setColor(new Color(0, 0, 0, 150))
    g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight)

    g2.setFont(g2.getFont.deriveFont(100F))
    var text = "You Died! Game Over"
    var x = Tools.getCenterX(g2, gp, text)
    var y = tileSize * 4
    // SHADOW
    g2.setColor(Color.BLACK)
    g2.drawString(text, x, y)
    // MAIN
    g2.setColor(Color.WHITE)
    g2.drawString(text, x - 4, y - 4)
    // RETRY
    g2.setFont(g2.getFont.deriveFont(50F))
    text = "Retry"
    x = Tools.getCenterX(g2, gp, text)
    y += tileSize * 4
    g2.drawString(text, x, y)
    if commandNum == 0 then
      g2.drawString(">", x-40, y)

    // QUIT
    text = "Quit"
    x = Tools.getCenterX(g2, gp, text)
    y += 55
    g2.drawString(text, x, y)
    if commandNum == 1 then
      g2.drawString(">", x-40, y)

  def drawTransitionState(): Unit =
    counter += 1
    g2.setColor(new Color(0, 0, 0, counter * 5))
    g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight)

    if counter == 50 then
      counter = 0
      gp.gameState = GameState.PlayState
      gp.currentMap = gp.eHandler.tempMap
      gp.player.pos = (gp.eHandler.tempRow * gp.tileSize, gp.eHandler.tempCol * gp.tileSize)
      gp.eHandler.previousEventX = gp.player.pos._1
      gp.eHandler.previousEventY = gp.player.pos._2
      gp.changeArea()

end UI
