package ui

import Enemy.Enemy
import entities.{Entity, Npc}
import game.{GamePanel, GameState}
import utils.Tools

import java.awt.{Color, Font, Graphics2D}
import scala.collection.mutable.ListBuffer

class UI(var gp: GamePanel):
  // Fonts and graphics
  var font_40: Font = new Font("Arial", Font.PLAIN, 40)
  var g2: Graphics2D = _

  // Messages
  var messageOn: Boolean = false
  var message: ListBuffer[String] = ListBuffer()
  var messageCounter: ListBuffer[Int] = ListBuffer()

  // Dialogue and NPC
  var npc: Entity = _
  var currentDialogue: String = ""
  var charIndex: Int = 0
  var combinedText: String = ""

  // UI State
  var isFinished = false
  var commandNum = 0
  var counter: Int = 0
  var subState = 0

  val tileSize = gp.tileSize

  // Set graphics
  def setGraphics(g: Graphics2D): Unit =
    g2 = g
    PlayerUI.setGraphics(g, gp)
    GameMenuUI.setGraphics(g, gp)
    TradeUI.setGraphics(g, gp)

  // MAIN draw function
  def drawUI(g: Graphics2D): Unit =
    setGraphics(g)
    g.setFont(font_40)
    g.setColor(Color.WHITE)

    gp.gameState match
      case GameState.PlayState       => drawPlayState()
      case GameState.PauseState      => drawPauseState()
      case GameState.DialogueState   => drawDialogueState()
      case GameState.TitleState      => drawTitleScreen()
      case GameState.CharacterState  => drawCharacterState()
      case GameState.GameMenu        => drawGameMenu()
      case GameState.GameOver        => drawGameOver()
      case GameState.TransitionState => drawTransitionState()
      case GameState.TradeState      => TradeUI.drawTradeState()
      case GameState.MapState        =>
      case GameState.CutSceneState   => 

  // Draw different UI components
  private def drawPlayState(): Unit =
    PlayerUI.drawPlayerLife()
    PlayerUI.drawPlayerMana()
    this.drawEnemyHealth()
    drawMessage()

  private def drawPauseState(): Unit =
    PlayerUI.drawPlayerLife()
    PlayerUI.drawPlayerMana()
    drawPauseScreen()

  private def drawDialogueState(): Unit =
    PlayerUI.drawPlayerLife()
    PlayerUI.drawPlayerMana()
    drawDialogueScreen()

  private def drawCharacterState(): Unit =
    PlayerUI.drawCharacterState()
    PlayerUI.drawInventory(gp.player, true)

  private def drawMessage(): Unit =
    val messageX = tileSize / 2
    var messageY = tileSize * 4
    g2.setFont(g2.getFont.deriveFont(Font.BOLD, 15F))

    for index <- message.indices.reverse do
      if message(index) != null then
        drawMessageText(message(index), messageX, messageY)
        val counter = messageCounter(index) + 1
        messageCounter(index) = counter
        messageY += 30

        if messageCounter(index) > 150 then
          message.remove(index)
          messageCounter.remove(index)

  private def drawMessageText(text: String, x: Int, y: Int): Unit =
    g2.setColor(Color.BLACK)
    g2.drawString(text, x + 1, y + 1)
    g2.setColor(Color.WHITE)
    g2.drawString(text, x, y)

  private def drawTitleScreen(): Unit =
    g2.setColor(new Color(208, 147, 62))
    g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight)

    g2.setFont(g2.getFont.deriveFont(Font.BOLD, 70F))
    drawTitleText("Adventure to Greece", tileSize * 3)

    // Menu options
    g2.setFont(g2.getFont.deriveFont(Font.BOLD, 40F))
    var y = tileSize * 4
    drawMenuOption("NEW GAME", 0, y); y += tileSize
    drawMenuOption("LOAD GAME", 1, y); y += tileSize
    drawMenuOption("QUIT GAME", 2, y)

  private def drawTitleText(text: String, y: Int): Unit =
    val x = Tools.getCenterX(g2, gp, text)
    g2.setColor(Color.BLACK)
    g2.drawString(text, x + 5, y + 5)
    g2.setColor(Color.WHITE)
    g2.drawString(text, x, y)

  private def drawMenuOption(text: String, commandIndex: Int, offsetY: Int): Unit =
    val x = Tools.getCenterX(g2, gp, text)
    val y = tileSize * 3 + offsetY
    g2.drawString(text, x, y)
    if commandNum == commandIndex then g2.drawString(">", x - tileSize, y)

  def drawDialogueScreen(): Unit =
    var (x, y, width, height) = (
      tileSize * 3,
      tileSize / 2,
      gp.screenWidth - (tileSize * 6),
      tileSize * 3
    )

    Tools.drawSubWindow(g2, x, y, width, height)

    g2.setFont(g2.getFont.deriveFont(Font.PLAIN, 25F))
    x += tileSize; y += tileSize

    handleDialogueText()

    currentDialogue.split("\n").foreach { line =>
      g2.drawString(line, x, y)
      y += 40
    }

  private def handleDialogueText(): Unit =
    if npc.dialogues(npc.dialogueSet)(npc.dialogueIndex) != null then
      type DialogueSource = Npc | Enemy
      npc match
        case npcOrEnemy: DialogueSource =>
          val charactersSet: Array[Char] = npcOrEnemy.dialogues(npcOrEnemy.dialogueSet)(npcOrEnemy.dialogueIndex).toCharArray
          if charIndex < charactersSet.length then
            combinedText += charactersSet(charIndex).toString
            currentDialogue = combinedText
            charIndex += 1
        case _ =>
          currentDialogue = npc.dialogues(npc.dialogueSet)(npc.dialogueIndex)

      if gp.keyH.enterPressed then
        charIndex = 0
        combinedText = ""
        if gp.gameState == GameState.DialogueState || gp.gameState == GameState.CutSceneState then
          npc.dialogueIndex += 1
          gp.keyH.enterPressed = false
    else
      npc.dialogueIndex = 0
      if gp.gameState == GameState.DialogueState then
        gp.gameState = GameState.PlayState
      if gp.gameState == GameState.CutSceneState then
        gp.cutSceneManager.scenePhase += 1

  private def drawPauseScreen(): Unit =
    val text: String = "PAUSE"
    val x: Int = Tools.getCenterX(g2, gp, text)
    val y: Int = gp.screenHeight / 2
    g2.drawString(text, x, y)

  private def drawGameMenu(): Unit =
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

  def addMessage(text: String): Unit =
    message += text
    messageCounter += 0

  private def drawGameOver(): Unit =
    g2.setColor(new Color(0, 0, 0, 150))
    g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight)

    g2.setFont(g2.getFont.deriveFont(90F))
    drawGameOverText("You Died! Game Over", tileSize * 4)
    g2.setFont(g2.getFont.deriveFont(50F))
    val y = tileSize * 3
    drawGameOverOption("Retry", 0, y)
    drawGameOverOption("Quit", 1, y + tileSize * 3)

  private def drawGameOverText(text: String, y: Int): Unit =
    val x = Tools.getCenterX(g2, gp, text)
    g2.setColor(Color.BLACK)
    g2.drawString(text, x, y)
    g2.setColor(Color.WHITE)
    g2.drawString(text, x - 4, y - 4)

  private def drawGameOverOption(text: String, commandIndex: Int, offsetY: Int): Unit =
    val x = Tools.getCenterX(g2, gp, text)
    val y = tileSize * 4 + offsetY
    g2.drawString(text, x, y)
    if commandNum == commandIndex then g2.drawString(">", x - 40, y)

  private def drawTransitionState(): Unit =
    counter += 1
    g2.setColor(new Color(0, 0, 0, counter * 5))
    g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight)

    if counter == 50 then
      counter = 0
      gp.gameState = GameState.PlayState
      gp.currentMap = gp.eHandler.tempMap
      gp.player.pos = (gp.eHandler.tempCol * gp.tileSize, gp.eHandler.tempRow * gp.tileSize)
      gp.eHandler.previousEventX = gp.player.pos._1
      gp.eHandler.previousEventY = gp.player.pos._2
      gp.changeArea()

  private def drawEnemyHealth(): Unit =

    for i <- gp.enemyList(1).indices do

      val currentEnemy = gp.enemyList(gp.currentMap)(i)
      if currentEnemy != null && currentEnemy.isInCamera then
        val (screenX, screenY) = currentEnemy.calculateScreenCoordinates()

        if currentEnemy.hpBarOn && !currentEnemy.isBoss then
          val oneScale: Double = gp.tileSize / currentEnemy.maxHealth
          val hpBarValue = oneScale * currentEnemy.health
          // Border
          g2.setColor(new Color(35, 35, 35))
          g2.fillRect(screenX + gp.tileSize - 1, screenY - 6, hpBarValue.toInt, 8)
          // Health Bar
          g2.setColor(new Color(255, 0, 30))
          g2.fillRect(screenX + gp.tileSize, screenY - 5, hpBarValue.toInt, 6)

          currentEnemy.hpBarCounter += 1
          if currentEnemy.hpBarCounter > 800 then
            currentEnemy.health = currentEnemy.maxHealth
            currentEnemy.hpBarCounter = 0
            currentEnemy.hpBarOn = false

        else if currentEnemy.isBoss then
          val oneScale: Double = gp.tileSize * 8 / currentEnemy.maxHealth
          val hpBarValue = oneScale * currentEnemy.health
          val x = gp.screenWidth / 2 - gp.tileSize * 4
          val y = gp.tileSize * 10
          // Border
          g2.setColor(new Color(35, 35, 35))
          g2.fillRect(x - 1 , y - 1, hpBarValue.toInt, 22)
          // Health Bar
          g2.setColor(new Color(255, 0, 30))
          g2.fillRect(x, y , hpBarValue.toInt, 20)

          g2.setFont(g2.getFont.deriveFont(Font.BOLD, 24F))
          g2.setColor(Color.WHITE)
          g2.drawString(currentEnemy.name, x + 4, y - 10)
end UI
