package Tile

import game.GamePanel

import java.awt.{Color, Graphics2D}
import java.io.{BufferedReader, InputStreamReader}
import scala.util.Using
import utils.Tools

class TileManager(var gp: GamePanel) :
  var tile: Array[Tile] = new Array[Tile](35)
  var drawPath: Boolean = false;

  var mapTileNum: Array[Array[Array[Int]]] = Array.ofDim[Int](gp.maxMap, gp.maxWorldRow, gp.maxWorldCol)
  def loadMap(path: String, mapIndex: Int) =
    val resourcePath = s"/images/Maps/$path"
    val streamOption = Option(getClass.getResourceAsStream(resourcePath))

    streamOption match
      case Some(inputStream) =>
        Using.resource(new BufferedReader(new InputStreamReader(inputStream))) ( br =>
          for row <- 0 until gp.maxWorldRow do
            val line = br.readLine()
            for col <- 0 until gp.maxWorldCol do
              val numbers: Array[String] = line.split(" ")
              val num: Int = numbers(col).toInt
              mapTileNum(mapIndex)(row)(col) = num
        )
      case None => println(s"Map source not found for mapIndex: $mapIndex")

  loadMap("map.txt", 0)
  loadMap("dungeon_map.txt", 1)
  loadMap("boss_map.txt", 2)

  def loadTileImage (): Unit =
    // OVERWORLD TILE
    setUp(0, "OverWorld/grass", false)
    setUp(1, "OverWorld/grass_flower1", false)
    setUp(2, "OverWorld/grass_flower2", false)
    setUp(3, "OverWorld/stone_path", false)
    setUp(4, "OverWorld/ruinedstone_path", false)
    setUp(5, "OverWorld/ruinedstone_path2", false)
    setUp(6, "OverWorld/wall", true)
    setUp(7, "OverWorld/wall", true)
    setUp(8, "OverWorld/wall", true)
    setUp(9, "OverWorld/water", true)

    // DUNGEON TILE
    setUp(10, "Dungeon/black", true)
    setUp(11, "Dungeon/floor_tile", false)
    setUp(12, "Dungeon/wall_1", true)
    setUp(13, "Dungeon/wall_2", true)
    setUp(14, "Dungeon/wall_3", true)
    setUp(15, "Dungeon/wall_0", true)
    setUp(16, "Dungeon/wall_3", true)
    setUp(17, "Dungeon/wall_5", true)
    setUp(18, "Dungeon/stair", false)
    setUp(19, "Dungeon/wall_4", true)
    setUp(20, "Dungeon/dark_tile", true)
    setUp(21, "Dungeon/wall_7", true)
    setUp(22, "Dungeon/wall_6", true)
    setUp(23, "Dungeon/down_stair", false)
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




