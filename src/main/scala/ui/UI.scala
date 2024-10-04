package ui

import `object`.OBJ_Heart
import game.{GamePanel, GameState}

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
  var commandNum = 1
  val tileSize = gp.tileSize
  var heart: OBJ_Heart = new OBJ_Heart(25 ,(0,0), gp)

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
        drawMessage()
      case GameState.PauseState =>
        drawPlayerLife()
        drawPauseScreen()
      case GameState.DialogueState =>
        drawPlayerLife()
        drawDialogueScreen()
      case GameState.TitleState =>
        drawTitleScreen()
      case GameState.CharacterState =>
        drawCharacterState()
        drawInventory()

  def drawPlayerLife(): Unit =
    val spacing = tileSize / 8
    val xStart = spacing
    val y = tileSize / 2 - 20

    val totalHearts = gp.player.maxHealth / 20
    var currentHealth = gp.player.health

    for (i <- 0 until totalHearts) do
      val x = xStart + i * (4 * spacing)

      if (currentHealth >= 20) then
        // Full heart
        g2.drawImage(heart.image, x, y, null)
        currentHealth -= 20
      else
        // Determine the heart state based on remaining health
        val heartImage = currentHealth match
          case h if h >= 14 => heart.image2
          case h if h >= 10 => heart.image3
          case h if h >= 7  => heart.image4
          case _             => heart.image5

        g2.drawImage(heartImage, x, y, null)
        currentHealth = 0

  def drawMessage(): Unit =
    var messageX = tileSize / 2
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
    if commandNum == 1 then g2.drawString(">", x - tileSize, y)

    text = "LOAD GAME"
    x = getCenterX(text)
    y += tileSize
    g2.drawString(text, x, y)
    if commandNum == 2 then g2.drawString(">", x - tileSize, y)

    text = "QUIT GAME"
    x = getCenterX(text)
    y += tileSize
    g2.drawString(text, x, y)
    if commandNum == 3 then g2.drawString(">", x - tileSize, y)

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
    val frameX = tileSize * 9
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
      if gp.player.inventory(index) == gp.player.currentWeapon || gp.player.inventory(index) == gp.player.currentShield then
        g2.setColor(new Color (240, 190, 90))
        g2.fillRoundRect(slotX, slotY, tileSize, tileSize, 10, 10 )

      g2.drawImage(gp.player.inventory(index).image, slotX + 5, slotY + 5, null)
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
        "Strength" -> (() => gp.player.strength.toString),
        "Dexterity" -> (() => gp.player.dexterity.toString),
        "Attack" -> (() => gp.player.attackDamage.toString),
        "Defense" -> (() => gp.player.defense.toString),
        "Next Level" -> (() =>s"${gp.player.exp}/${gp.player.nextLevelExp.toString}"),
        "Weapon" -> (() => ""),
        "Shield" -> (() => ""),
    )

    val initialTextX = frameX + 20
    var textY = frameY + tileSize
    val lineHeight = 36
    val tailX = (frameX + frameWidth) - 30

    for ((label, _) <- labels) {
        g2.drawString(label, initialTextX, textY)
        textY += lineHeight
    }

    textY = frameY + tileSize

    for (((_, getValue), index) <- labels.zipWithIndex) {
        if (index < labels.length - 1) {
            val value = getValue()
            val textX = getRightX(value, tailX)
            g2.drawString(value, textX, textY)
            textY += lineHeight
        }
    }

    val weapon = gp.player.getCurrentWeapon
    if (weapon != null && weapon.image != null) {
        g2.drawImage(weapon.image, (tailX - tileSize / 1.5).toInt, textY - 65, null)
    }
    textY += lineHeight
    val shield = gp.player.getCurrentShield
    if (shield != null && shield.image != null) {
        g2.drawImage(shield.image, (tailX - tileSize / 1.5).toInt, textY - 55, null)
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

end UI
