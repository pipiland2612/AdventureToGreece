package Tile

import game.GamePanel

import java.awt.{Color, Graphics2D}
import java.io.{BufferedReader, File, InputStreamReader}
import utils.Tools

class TileManager(var gp: GamePanel) :
  var tile: Array[Tile] = new Array[Tile](20)
  var drawPath: Boolean = true

  var mapTileNum: Array[Array[Array[Int]]] = Array.ofDim[Int](gp.maxMap, gp.maxWorldRow, gp.maxWorldCol)
  def loadMap(path: String, mapIndex: Int) =
    try
      val inputStream = new File(path).toURI.toURL.openStream()
      val br = BufferedReader(new InputStreamReader(inputStream))

      for row <- 0 until gp.maxWorldRow do
        val line = br.readLine()
        for col <- 0 until gp.maxWorldCol do
          val numbers: Array[String] = line.split(" ")
          val num: Int = numbers(col).toInt
          mapTileNum(mapIndex)(row)(col) = num

      br.close()
    catch
      case exception: Exception => println(s"Map source not found $mapIndex")

  loadMap("/Users/batman/Desktop/Adventure to Greece/src/main/resources/images/Maps/map.txt", 0)
  loadMap("/Users/batman/Desktop/Adventure to Greece/src/main/resources/images/Maps/dungeon_map.txt", 1)

  def loadTileImage (): Unit =
    setUp(0, "grass", false)
    setUp(1, "grass_flower1", false)
    setUp(2, "grass_flower2", false)
    setUp(3, "stone_path", false)
    setUp(4, "ruinedstone_path", false)
    setUp(5, "ruinedstone_path2", false)
    setUp(6, "wall", true)
    setUp(7, "wall", true)
    setUp(8, "wall", true)
    setUp(9, "water", true)

    setUp(10, "dungeon_path", false)
    setUp(11, "dungeon_path2", false)
    setUp(12, "dungeon_wall", true)
  loadTileImage()

  def setUp(index: Int, imageName: String, collision: Boolean): Unit =
    val uTools = Tools
    try
      tile(index) = new Tile()
      tile(index).image = uTools.loadImage("Tiles/" + imageName + ".png")

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
      val tileNum: Int = mapTileNum(gp.currentMap)(worldRow)(worldCol)
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

    if drawPath then
      g.setColor(new Color (255, 0, 0, 70))
      for i <- gp.pFinder.pathList.indices do
        val worldX = gp.pFinder.pathList(i).col * tileSize
        val worldY = gp.pFinder.pathList(i).row * tileSize
        val screenTileX = worldX - playerX + screenX
        val screenTileY = worldY - playerY + screenY
        g.fillRect(screenTileX, screenTileY, tileSize, tileSize)

end TileManager




