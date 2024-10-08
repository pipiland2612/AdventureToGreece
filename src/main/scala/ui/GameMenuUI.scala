package ui

import java.awt.{BasicStroke, Graphics2D}
import game.{GamePanel, GameState}
import ui.PlayerUI.tileSize
import utils.Tools

object GameMenuUI:

  var g2: Graphics2D = _
  var gp: GamePanel = _

  def setGraphics(g: Graphics2D, gamePanel: GamePanel): Unit =
    g2 = g
    gp = gamePanel
    tileSize = gp.tileSize

  def gameMenuTop(frameX: Int, frameY: Int):Unit =
    val tileSize = gp.tileSize
    val text = "Options"
    var textX = Tools.getCenterX(g2, gp, text) + 20
    var textY = frameY + tileSize
    g2.drawString(text, textX, textY)

    // MUSIC
    textX = frameX + tileSize - 10
    textY += tileSize + 25
    g2.drawString("Music", textX, textY)
    if gp.gui.commandNum == 0 then g2.drawString(">", textX - 25, textY)
    // SE
    textY += tileSize
    g2.drawString("SE", textX, textY)
    if gp.gui.commandNum == 1 then g2.drawString(">", textX - 25, textY)
    // CONTROL
    textY += tileSize
    g2.drawString("Control", textX, textY)
    if gp.gui.commandNum == 2 then
      g2.drawString(">", textX - 25, textY)
      if gp.keyH.enterPressed then
        gp.gui.subState = 1
        gp.gui.commandNum = 0
    // END GAME
    textY += tileSize
    g2.drawString("End Game", textX, textY)
    if gp.gui.commandNum == 3 then
      g2.drawString(">", textX - 25, textY)
      if gp.keyH.enterPressed then
        gp.gui.subState = 2
        gp.gui.commandNum = 0
    // BACK
    textY += tileSize * 2
    g2.drawString("Back", textX, textY)
    if gp.gui.commandNum == 4 then
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

    gp.config.saveConfig()

  // CONTROL
  def controlDisplay(frameX: Int, frameY: Int): Unit =
    val tileSize = gp.tileSize
    val text = "Control"
    var textX = Tools.getCenterX(g2, gp, text) + 20
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
    if gp.gui.commandNum == 0 then
      g2.drawString(">", textX - 25, textY)
      if gp.keyH.enterPressed then
        gp.gui.subState = 0
        gp.gui.commandNum = 2

  def endGameConfirmation(frameX: Int, frameY: Int): Unit =
    val tileSize = gp.tileSize
    var textX = frameX + tileSize - 10
    var textY = frameY + tileSize
    gp.gui.currentDialogue = "Quit the game and \nreturn to the title screen ?"

    for line <- gp.gui.currentDialogue.split("\n") do
      g2.drawString(line, textX, textY)
      textY += 40

    val text = "Yes"
    textX = frameX + tileSize * 4 - 20
    textY += tileSize * 3
    g2.drawString(text, textX, textY)
    if gp.gui.commandNum == 0 then
      g2.drawString(">", textX - 25, textY)
      if gp.keyH.enterPressed then
        gp.gui.subState = 0
        gp.gui.commandNum = 0
        gp.gameState = GameState.TitleState

    val text2 = "No"
    textX = frameX + tileSize * 4 - 20
    textY += tileSize * 2
    g2.drawString(text2, textX, textY)
    if gp.gui.commandNum == 1 then
      g2.drawString(">", textX - 25, textY)
      if gp.keyH.enterPressed then
        gp.gui.subState = 0
        gp.gui.commandNum = 3

