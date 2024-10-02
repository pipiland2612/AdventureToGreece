package ui

import `object`.OBJ_Heart
import game.{GamePanel, GameState}

import java.awt.{BasicStroke, Color, Font, Graphics2D}

class UI (var gp: GamePanel):
  var g2 : Graphics2D = _
  var font_40: Font = new Font("Arial", Font.PLAIN ,40)
  var messageOn : Boolean = false
  var message = ""
  var messageCounter = 0
  var isFinished = false
  var currentDialogue: String = ""
  var commandNum = 1

  var heart: OBJ_Heart = new OBJ_Heart(25 ,(0,0), gp)

  def showMessage(text : String) =
    message = text
    messageOn = true

  def drawUI (g : Graphics2D): Unit =
    this.g2 = g
    g.setFont(font_40)
    g.setColor(Color.WHITE)

    gp.gameState match
      case GameState.PlayState =>
        drawPlayerLife()
      case GameState.PauseState =>
        drawPlayerLife()
        drawPauseScreen()
      case GameState.DialogueState =>
        drawPlayerLife()
        drawDialogueScreen()
      case GameState.TitleState =>
        drawTitleScreen()

  def drawPlayerLife(): Unit =
    val spacing = gp.tileSize / 8
    val xStart = spacing
    val y = gp.tileSize / 2 - 20

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


  def drawTitleScreen(): Unit =
    g2.setColor(new Color(208, 147, 62))
    g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight)

    g2.setFont(g2.getFont.deriveFont(Font.BOLD, 70F))
    var text = "Adventure to Greece"
    var x = getCenterX(text)
    var y = gp.tileSize * 3
    //Shadow
    g2.setColor(Color.BLACK)
    g2.drawString(text, x + 5, y + 5)
    //
    g2.setColor(Color.WHITE)
    g2.drawString(text, x ,y)

    //Player image
    x = gp.screenWidth / 2
    y += gp.tileSize * 2
//    g2.drawImage( x, y, gp.tileSize, gp.tileSize, null)
    //MENU
    g2.setFont(g2.getFont.deriveFont(Font.BOLD, 40F))
    text = "NEW GAME"
    x = getCenterX(text)
    y += gp.tileSize * 4
    g2.drawString(text, x, y)
    if commandNum == 1 then g2.drawString(">", x - gp.tileSize, y)

    text = "LOAD GAME"
    x = getCenterX(text)
    y += gp.tileSize
    g2.drawString(text, x, y)
    if commandNum == 2 then g2.drawString(">", x - gp.tileSize, y)

    text = "QUIT GAME"
    x = getCenterX(text)
    y += gp.tileSize
    g2.drawString(text, x, y)
    if commandNum == 3 then g2.drawString(">", x - gp.tileSize, y)

  def drawDialogueScreen(): Unit =
    val tileSize = gp.tileSize
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

  def drawSubWindow(x: Int, y: Int, width: Int, height: Int): Unit =
    var c: Color = Color(0,0,0, 210)
    g2.setColor(c)
    g2.fillRoundRect(x, y, width, height, 35 ,35)

    c = Color(255, 255, 255)
    g2.setColor(c)
    g2.setStroke(new BasicStroke(5))
    g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25)

  def drawPauseScreen(): Unit =
    val text: String = "PAUSE"
    val x: Int = getCenterX(text)
    val y: Int = gp.screenHeight / 2
    g2.drawString(text, x ,y)

  def getCenterX (text: String): Int =
    val length = g2.getFontMetrics.getStringBounds(text, g2).getWidth
    (gp.screenWidth / 2 - length /2).toInt

end UI
