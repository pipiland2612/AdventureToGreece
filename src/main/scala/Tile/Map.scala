package Tile

import game.GamePanel

import java.awt.{AlphaComposite, BasicStroke, Color, Graphics2D}
import java.awt.image.BufferedImage

class Map(gp : GamePanel) extends TileManager(gp):
  var miniMapOn = false
  var worldMap: Array[BufferedImage] = _

  def createWorldMap (): Unit =
    worldMap = new Array[BufferedImage](gp.maxMap)
    val worldMapWidth = gp.tileSize * gp.maxWorldCol
    val worldMapHeight = gp.tileSize * gp.maxWorldRow

    for i <- 0 until gp.maxMap do
      worldMap(i) = BufferedImage(worldMapWidth, worldMapHeight, BufferedImage.TYPE_INT_ARGB)
      val g2: Graphics2D = worldMap(i).createGraphics

      for
        row <- 0 until gp.maxWorldRow
        col <- 0 until gp.maxWorldCol
      do
        val tileNum = mapTileNum(i)(row)(col)
        val x = gp.tileSize * col
        val y = gp.tileSize * row
        g2.drawImage(tile(tileNum).image, x, y, null)
        g2.dispose()
  createWorldMap()

  def drawFullMapScreen(g2: Graphics2D) : Unit =
    // BACKGROUND COLOR
    g2.setColor(Color.BLACK)
    g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight)
    // DRAW MAP
    val width = 500
    val height = 500
    val x = gp.screenWidth / 2 - width / 2
    val y = gp.screenHeight / 2 - height / 2
    g2.drawImage(worldMap(gp.currentMap), x, y, width, height, null)
    // DRAW PLAYER
    val scale: Double = (gp.tileSize * gp.maxWorldCol).toDouble / width
    val playerX: Int = (gp.player.pos._1 / scale).toInt + x
    val playerY: Int = (gp.player.pos._2 / scale).toInt + y
    val playerSize: Int = (gp.player.playerScale / scale).toInt
    g2.drawImage(gp.player.displayedImage, playerX, playerY, playerSize, playerSize, null)

    // HINT
    g2.setFont(gp.gui.g2.getFont.deriveFont(25F))
    g2.setColor(Color.WHITE)
    g2.drawString("Press M to close", 750, 550)

  def drawMiniMap(g2: Graphics2D): Unit =
    if miniMapOn then
      val width = 200
      val height = 200
      val x = gp.screenWidth - width
      val y = 0
      g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.8f))
      g2.drawImage(worldMap(gp.currentMap), x, y, width, height, null)

      // DRAW PLAYER
      val scale: Double = (gp.tileSize * gp.maxWorldCol).toDouble / width
      val playerX: Int = (gp.player.pos._1 / scale).toInt + x
      val playerY: Int = (gp.player.pos._2 / scale).toInt + y
      val playerSize: Int = (gp.player.playerScale * 3 / scale).toInt
      g2.drawImage(gp.player.displayedImage, playerX - 4, playerY - 4, playerSize, playerSize, null)

      // Draw a frame around the minimap
      g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))
      g2.setColor(Color.WHITE)
      g2.setStroke(new BasicStroke(3))
      g2.drawRect(x - 1, y - 1, width + 2, height + 2)

      g2.setStroke(new BasicStroke(1))
