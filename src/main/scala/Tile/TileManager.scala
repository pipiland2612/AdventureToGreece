package Tile

import game.GamePanel

import java.awt.Graphics2D
import java.io.{BufferedReader, File, InputStreamReader}
import utils.Tools

class TileManager(var gp: GamePanel) :
  var tile: Array[Tile] = new Array[Tile](10)

  var mapTileNum: Array[Array[Int]] = Array.ofDim[Int](gp.maxWorldRow, gp.maxWorldCol)
  def loadMap(path: String) =
    try
      val inputStream = new File(path).toURI.toURL.openStream()
      val br = BufferedReader(new InputStreamReader(inputStream))

      for row <- 0 until gp.maxWorldRow do
        val line = br.readLine()
        for col <- 0 until gp.maxWorldCol do
          val numbers: Array[String] = line.split(" ")
          val num: Int = numbers(col).toInt
          mapTileNum(row)(col) = num

      br.close()
    catch
      case exception: Exception => println("Map source not found")

  loadMap("/Users/batman/Desktop/Adventure to Greece/src/main/resources/images/Maps/map.txt")

  def loadTileImage (): Unit =
    setUp(0, "grass", false)
    setUp(1, "grass_flower1", false)
    setUp(2, "grass_flower2", false)
    setUp(3, "stone_path", false)
    setUp(4, "ruinedstone_path", false)
    setUp(5, "ruinedstone_path2", false)
    setUp(6, "horizontal_wall", true)
    setUp(7, "vertical_wall", true)
    setUp(8, "wall", true)
  loadTileImage()

  def setUp(index: Int, imageName: String, collision: Boolean): Unit =
    val uTools = Tools
    try
      tile(index) = new Tile()
      tile(index).image = uTools.loadImage("/Users/batman/Desktop/Adventure to Greece/src/main/resources/images/Tiles/" + imageName + ".png")

      // scale beforehand to optimize time to draw
      tile(index).image = uTools.scaleImage(tile(index).image, gp.tileSize, gp.tileSize)
      tile(index).collision = collision
    catch
      case exception: Exception =>
        exception.printStackTrace()


  def drawTiles(g: Graphics2D): Unit =
    val tileSize = gp.tileSize
    val playerX = gp.player.getPosition._1
    val playerY = gp.player.getPosition._2
    val screenX = gp.player.screenX
    val screenY = gp.player.screenY

    for
      worldRow <- 0 until gp.maxWorldRow
      worldCol <- 0 until gp.maxWorldCol
    do
      val tileNum: Int = mapTileNum(worldRow)(worldCol)
      val worldX = worldCol * tileSize
      val worldY = worldRow * tileSize
      val screenTileX = worldX - playerX + screenX
      val screenTileY = worldY - playerY + screenY

      // Only draw tiles that are within the visible screen boundaries
      if (
        worldX + tileSize > playerX - screenX &&
        worldX - tileSize < playerX + screenX &&
        worldY + tileSize > playerY - screenY &&
        worldY - tileSize < playerY + screenY
      ) then
        g.drawImage(tile(tileNum).image, screenTileX, screenTileY, null)


end TileManager




