package utils

import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import java.io.File
import scala.util.Try

object SpriteSheetTest extends App :

  val spriteSheetPath = "Objects/Fireball/fireball_right.png"

  val spriteSheet: BufferedImage = 
    try
      (Tools.loadImage(spriteSheetPath))
    catch
      case e: Exception =>
        println(s"Failed to load sheet at : $spriteSheetPath")
        sys.exit(1)

  val frameWidth = 64
  val frameHeight = 32

  val spriteFrames: Array[Array[BufferedImage]] = SpriteSheet.splitSpriteSheet(spriteSheet, frameWidth, frameHeight, 16,16, 4)

  println(s"Sprite Sheet Dimensions: ${spriteSheet.getWidth}x${spriteSheet.getHeight}")
  println(s"Frame Size: ${frameWidth}x${frameHeight}")
  println(s"Total Rows: ${spriteFrames.length}")
  println(s"Total Columns per Row: ${if (spriteFrames.nonEmpty) then spriteFrames(0).length else 0}")

  val outputDir = new File("/Users/batman/Desktop/Adventure to Greece/src/main/scala/utils/output_frames")
  if (!outputDir.exists()) then
    outputDir.mkdirs()

  for
    (row, rowIndex) <- spriteFrames.zipWithIndex
    (frame, colIndex) <- row.zipWithIndex
  do
    val outputFile = new File(outputDir, s"frame_row${rowIndex}_col${colIndex}.png")
    Try(ImageIO.write(frame, "png", outputFile)) match
      case scala.util.Success(_) =>
        println(s"Saved frame to ${outputFile.getPath}")
      case scala.util.Failure(ex) =>
        println(s"Failed to save frame at row $rowIndex, column $colIndex: ${ex.getMessage}")

  println("Sprite sheet splitting test completed.")

