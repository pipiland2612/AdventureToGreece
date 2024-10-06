package ui

import `object`.{OBJ_Heart, OBJ_Mana}
import game.{GamePanel, GameState}

import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color, Font, Graphics2D}
import scala.collection.mutable.ListBuffer


class UI (var gp: GamePanel):
  var g2 : Graphics2D = _
  var font_40: Font = new Font("Arial", Font.PLAIN ,40)
  var messageOn : Boolean = false
  var message: ListBuffer[String] = ListBuffer()
  var messageCounter: ListBuffer[Int] = ListBuffer()
  var isFinished = false
  var currentDialogue: String = ""
  var commandNum = 0
  val tileSize = gp.tileSize
  var heart: OBJ_Heart = new OBJ_Heart(25 , gp)
  var mana : OBJ_Mana = new OBJ_Mana(25, gp)
  var subState = 0

  var slotCol = 0
  var slotRow = 0


  def addMessage(text : String) =
    message += text
    messageCounter += 0

  def drawUI (g : Graphics2D): Unit =
    this.g2 = g
    g.setFont(font_40)
    g.setColor(Color.WHITE)

    gp.gameState match
      case GameState.PlayState =>
        drawPlayerLife()
        drawPlayerMana()
        drawMessage()
      case GameState.PauseState =>
        drawPlayerLife()
        drawPlayerMana()
        drawPauseScreen()
      case GameState.DialogueState =>
        drawPlayerLife()
        drawPlayerMana()
        drawDialogueScreen()
      case GameState.TitleState =>
        drawTitleScreen()
      case GameState.CharacterState =>
        drawCharacterState()
        drawInventory()
      case GameState.GameMenu =>
        drawGameMenu()

  def drawPlayerLife(): Unit =
    val spacing = tileSize / 8
    val y = tileSize / 2 - 20
    val totalHearts = gp.player.maxHealth / 20
    val currentHealth = gp.player.health

    drawPlayerStats(spacing, y, totalHearts, currentHealth, heart.image, heart.image2, heart.image3, heart.image4, heart.image5)

  def drawPlayerMana(): Unit =
    val spacing = tileSize / 8
    val y = tileSize / 2 + 5
    val totalMana = gp.player.maxMana / 20
    val currentMana = gp.player.mana

    drawPlayerStats(spacing, y, totalMana, currentMana, mana.image, mana.image2, mana.image3, mana.image4, mana.image5)

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
    var x = getCenterX(text)
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
    x = getCenterX(text)
    y += tileSize * 4
    g2.drawString(text, x, y)
    if commandNum == 0 then g2.drawString(">", x - tileSize, y)

    text = "LOAD GAME"
    x = getCenterX(text)
    y += tileSize
    g2.drawString(text, x, y)
    if commandNum == 1 then g2.drawString(">", x - tileSize, y)

    text = "QUIT GAME"
    x = getCenterX(text)
    y += tileSize
    g2.drawString(text, x, y)
    if commandNum == 2 then g2.drawString(">", x - tileSize, y)

  def drawDialogueScreen(): Unit =
    var (x, y , width, height) = (
      tileSize * 2,
      tileSize / 2,
      gp.screenWidth - (tileSize * 5),
      tileSize * 3
    )
    drawSubWindow(x, y, width, height)

    g2.setFont(g2.getFont.deriveFont(Font.PLAIN, 25F))
    x += tileSize; y += tileSize
    currentDialogue.split("\n").foreach( line =>
      g2.drawString(line, x, y )
      y += 40
    )

  // Handle character state
  def drawInventory(): Unit =
    // Frame
    val frameX = tileSize * 18
    val frameY = tileSize
    val frameWidth = tileSize * 6
    val frameHeight = tileSize * 5
    drawSubWindow(frameX, frameY, frameWidth, frameHeight)

    // SLOT
    val slotXstart = frameX + 20
    val slotYstart = frameY + 20
    var slotX = slotXstart
    var slotY = slotYstart
    var slotSize = tileSize + 3

    // Player's Item
    for index <- gp.player.inventory.indices do
      val currentItem = gp.player.inventory(index)
      if currentItem == gp.player.currentWeapon || currentItem == gp.player.currentShield
        || currentItem == gp.player.currentProjectile then
        g2.setColor(new Color (240, 190, 90))
        g2.fillRoundRect(slotX, slotY, tileSize, tileSize, 10, 10 )

      g2.drawImage(currentItem.imageDisplayed, slotX + 5, slotY + 5, null)
      slotX += slotSize
      if index % 5 == 4 then
        slotX = slotXstart
        slotY += slotSize

    // CURSOR
    val cursorX = slotXstart + (slotSize * slotCol)
    val cursorY = slotYstart + (slotSize * slotRow)
    val cursorWidth = tileSize
    val cursorHeight = tileSize

    // DRAW
    g2.setColor(Color.WHITE)
    g2.setStroke(new BasicStroke(3))
    g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10)

    // Descriptions FRAME, and Draw
    val dframeX = frameX
    val dframeY = frameY + frameHeight
    val dframeWidth = frameWidth
    val dframeHeight = tileSize * 3

    val textX = dframeX + 20
    var textY = dframeY + tileSize * 0.8
    g2.setFont(g2.getFont.deriveFont(20F))
    if getItemIndexBySlot < gp.player.inventory.size then
      drawSubWindow(dframeX, dframeY, dframeWidth, dframeHeight)
      val currentItem = gp.player.inventory(getItemIndexBySlot)
      for (line <- currentItem.getDescription.split("\n")) do
        g2.drawString(line, textX, textY.toInt)
        textY += 32

  def drawCharacterState(): Unit =
    val frameX = (tileSize * 1.5).toInt
    val frameY = tileSize
    val frameWidth = tileSize * 5
    val frameHeight = tileSize * 10
    drawSubWindow(frameX, frameY, frameWidth, frameHeight)
    g2.setColor(Color.WHITE)
    g2.setFont(g2.getFont.deriveFont(20F))

    val labels = List(
        "Level" -> (() => gp.player.level.toString),
        "Health" -> (() => s"${gp.player.health}/${gp.player.maxHealth}"),
        "Mana" -> (() => s"${gp.player.mana}/${gp.player.maxMana}"),
        "Strength" -> (() => gp.player.strength.toString),
        "Dexterity" -> (() => gp.player.dexterity.toString),
        "Attack" -> (() => gp.player.attackDamage.toString),
        "Defense" -> (() => gp.player.defense.toString),
        "Next Level" -> (() =>s"${gp.player.exp}/${gp.player.nextLevelExp.toString}"),
        "Coin" -> (() =>s"${gp.player.coin}"),
        "Weapon" -> (() => ""),
        "Shield" -> (() => ""),
        "Projectile" -> (() => ""),
    )

    val initialTextX = frameX + 20
    var textY = frameY + tileSize - 10
    val lineHeight = 38
    val tailX = (frameX + frameWidth) - 30

    for ((label, _) <- labels) {
        g2.drawString(label, initialTextX, textY)
        textY += lineHeight
    }

    textY = frameY + tileSize - 10

    for (((_, getValue), index) <- labels.zipWithIndex) {
        if (index < labels.length - 1) {
            val value = getValue()
            val textX = getRightX(value, tailX)
            g2.drawString(value, textX, textY)
            textY += lineHeight
        }
    }

    val weapon = gp.player.getCurrentWeapon
    if (weapon != null && weapon.imageDisplayed != null) {
        g2.drawImage(weapon.imageDisplayed, (tailX - tileSize / 1.5).toInt, textY - 105, null)
    }
    textY += lineHeight
    val shield = gp.player.getCurrentShield
    if (shield != null && shield.imageDisplayed != null) {
        g2.drawImage(shield.imageDisplayed, (tailX - tileSize / 1.5).toInt, textY - 100, null)
    }
    textY += lineHeight
    val projectile = gp.player.getCurrentProjectile
    if (projectile != null && projectile.imageDisplayed != null) {
        g2.drawImage(projectile.imageDisplayed, (tailX - tileSize / 1.5).toInt, textY - 100, null)
    }

  def drawPauseScreen(): Unit =
    val text: String = "PAUSE"
    val x: Int = getCenterX(text)
    val y: Int = gp.screenHeight / 2
    g2.drawString(text, x ,y)

  // HelperMethod
  def getItemIndexBySlot: Int = slotCol + (slotRow * 5)

  def drawSubWindow(x: Int, y: Int, width: Int, height: Int): Unit =
    var c: Color = Color(0,0,0, 210)
    g2.setColor(c)
    g2.fillRoundRect(x, y, width, height, 35 ,35)

    c = Color(255, 255, 255)
    g2.setColor(c)
    g2.setStroke(new BasicStroke(5))
    g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25)

  def getCenterX (text: String): Int =
    val length = g2.getFontMetrics.getStringBounds(text, g2).getWidth
    (gp.screenWidth / 2 - length /2).toInt

  def getRightX (text: String, tailX : Int): Int =
    val length = g2.getFontMetrics.getStringBounds(text, g2).getWidth
    (tailX - length).toInt

  def drawPlayerStats(
      spacing: Int,
      yPosition: Int,
      totalUnits: Int,
      currentUnits: Int,
      fullImage: BufferedImage,
      image2: BufferedImage,
      image3: BufferedImage,
      image4: BufferedImage,
      image5: BufferedImage
  ): Unit =
    val xStart = spacing
    var remainingUnits = currentUnits
    for (i <- 0 until totalUnits) do
      val x = xStart + i * (4 * spacing)

      if (remainingUnits >= 20) then

        g2.drawImage(fullImage, x, yPosition, null)
        remainingUnits -= 20
      else

        val unitImage = remainingUnits match
          case u if u >= 14 => image2
          case u if u >= 10 => image3
          case u if u >= 7  => image4
          case _            => image5

        g2.drawImage(unitImage, x, yPosition, null)
        remainingUnits = 0

  def drawGameMenu(): Unit =
    g2.setColor(Color.WHITE)
    g2.setFont(g2.getFont.deriveFont(28F))
    val frameX = (tileSize * 9)
    val frameY = tileSize
    val frameWidth = tileSize * 8
    val frameHeight = tileSize * 10
    drawSubWindow(frameX, frameY, frameWidth, frameHeight)

    subState match
      case 0 => gameMenuTop(frameX, frameY)
      case 1 => controlDisplay(frameX, frameY)
      case 2 => endGameConfimation(frameX, frameY)
      case _ =>
    gp.keyH.enterPressed = false

  def gameMenuTop(frameX: Int, frameY: Int):Unit =
    val text = "Options"
    var textX = getCenterX(text) + 20
    var textY = frameY + tileSize
    g2.drawString(text, textX, textY)

    // MUSIC
    textX = frameX + tileSize - 10
    textY += tileSize + 25
    g2.drawString("Music", textX, textY)
    if commandNum == 0 then g2.drawString(">", textX - 25, textY)
    // SE
    textY += tileSize
    g2.drawString("SE", textX, textY)
    if commandNum == 1 then g2.drawString(">", textX - 25, textY)
    // CONTROL
    textY += tileSize
    g2.drawString("Control", textX, textY)
    if commandNum == 2 then
      g2.drawString(">", textX - 25, textY)
      if gp.keyH.enterPressed then
        subState = 1
        commandNum = 0
    // END GAME
    textY += tileSize
    g2.drawString("End Game", textX, textY)
    if commandNum == 3 then
      g2.drawString(">", textX - 25, textY)
      if gp.keyH.enterPressed then
        subState = 2
        commandNum = 0
    // BACK
    textY += tileSize * 2
    g2.drawString("Back", textX, textY)
    if commandNum == 4 then
      g2.drawString(">", textX - 25, textY)
      if gp.keyH.enterPressed then
        gp.gameState = GameState.PlayState

    // MUSIC CHECKBOX
    textX = frameX + (tileSize * 4.5).toInt
    textY = frameY + tileSize * 2 + 3
    g2.setStroke(new BasicStroke(3))
    g2.drawRect(textX, textY, 120, 24) // 120/5
    val volumeWidth = 24 * gp.sound.volumeScale
    g2.fillRect(textX, textY, volumeWidth, 24)

    textY += tileSize
    g2.drawRect(textX, textY, 120, 24)
    val seWidth = 24 * gp.se.volumeScale
    g2.fillRect(textX, textY, seWidth, 24)
    // CONTROL

  def controlDisplay(frameX: Int, frameY: Int): Unit =
    val text = "Control"
    var textX = getCenterX(text) + 20
    var textY = frameY + tileSize
    g2.drawString("Control", textX, textY)

    textX = frameX + tileSize - 15
    textY += tileSize + 25

    g2.drawString("Move", textX, textY); textY += tileSize
    g2.drawString("Confirm/Attack", textX, textY); textY += tileSize
    g2.drawString("Shoot/Cast", textX, textY); textY += tileSize
    g2.drawString("Character Screen", textX, textY); textY += tileSize
    g2.drawString("Pause", textX, textY); textY += tileSize
    g2.drawString("Game Menu", textX, textY); textY += tileSize

    textX = frameX + tileSize * 6 - 5
    textY = frameY + tileSize * 2 + 25

    g2.drawString("WASD", textX, textY); textY += tileSize
    g2.drawString("J", textX, textY); textY += tileSize
    g2.drawString("U", textX, textY); textY += tileSize
    g2.drawString("C", textX, textY); textY += tileSize
    g2.drawString("P", textX, textY); textY += tileSize
    g2.drawString("ESC", textX, textY); textY += tileSize

    textX = frameX + tileSize
    textY = frameY + tileSize * 9
    g2.drawString("Back", textX, textY)
    if commandNum == 0 then
      g2.drawString(">", textX - 25, textY)
      if gp.keyH.enterPressed then
        subState = 0
        commandNum = 2

  def endGameConfimation(frameX: Int, frameY: Int): Unit =
    var textX = frameX + tileSize - 10
    var textY = frameY + tileSize
    currentDialogue = "Quit the game and \nreturn to the title screen ?"

    for line <- currentDialogue.split("\n") do
      g2.drawString(line, textX, textY)
      textY += 40

    val text = "Yes"
    textX = frameX + tileSize * 4 - 20
    textY += tileSize * 3
    g2.drawString(text, textX, textY)
    if commandNum == 0 then
      g2.drawString(">", textX - 25, textY)
      if gp.keyH.enterPressed then
        subState = 0
        commandNum = 0
        gp.gameState = GameState.TitleState

    val text2 = "No"
    textX = frameX + tileSize * 4 - 20
    textY += tileSize * 2
    g2.drawString(text2, textX, textY)
    if commandNum == 1 then
      g2.drawString(">", textX - 25, textY)
      if gp.keyH.enterPressed then
        subState = 0
        commandNum = 3

end UI
