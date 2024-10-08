//package Tile
//
//import game.GamePanel
//
//object TestTileManager {
//  def main(args: Array[String]): Unit = {
//    val gamePanel = new GamePanel()
//
//    val tileManager = new TileManager(gamePanel)
//
//    val testMapPath = "/Users/batman/Desktop/Adventure to Greece/src/main/resources/images/Maps/map.txt"
//
//    tileManager.loadMap(testMapPath, 0)
//
//    val expectedMap = Array(
//      Array(4, 4, 4, 4, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1),
//      Array(4, 4, 4, 0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 1, 1),
//      Array(4, 4, 0, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2),
//      Array(4, 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 4),
//      Array(4, 0, 0, 0, 1, 1, 1, 0, 0, 1, 2, 2, 2, 2, 4, 4),
//      Array(4, 0, 1, 1, 1, 0, 0, 0, 0, 1, 1, 1, 2, 4, 4, 4),
//      Array(0, 0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 2, 4, 4, 4, 4),
//      Array(0, 0, 0, 1, 1, 1, 1, 1, 1, 1, 2, 4, 4, 4, 4, 4),
//      Array(0, 0, 1, 1, 1, 1, 1, 1, 1, 2, 4, 4, 4, 4, 4, 4),
//      Array(0, 1, 1, 1, 1, 1, 1, 1, 2, 2, 4, 4, 4, 4, 4, 4),
//      Array(0, 1, 1, 1, 1, 2, 2, 4, 4, 4, 4, 4, 4, 4, 4, 4),
//      Array(1, 1, 1, 1, 2, 2, 4, 4, 4, 4, 4, 4, 4, 4, 4, 4)
//    )
//
//    var testPassed = true
//    for (row <- 0 until gamePanel.maxScreenRow) {
//      for (col <- 0 until gamePanel.maxScreenColumn) {
//        val expectedValue = expectedMap(0)(row)(col)
//        val actualValue = tileManager.mapTileNum(row)(col)
//        if (actualValue != expectedValue) then
//          println(s"Mismatch at ($row, $col): expected $expectedValue, found $actualValue")
//          testPassed = false
//      }
//    }
//
//    if (testPassed) then
//      println("All map tiles loaded correctly.")
//    else {
//      println("Map tiles did not load as expected.")
//    }
//  }
//}
