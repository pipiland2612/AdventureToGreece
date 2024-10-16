package utils

import java.awt.image.BufferedImage

object SpriteSheet :

  def isBlank(image: BufferedImage): Boolean =
    var result = true
    for (x <- 0 until image.getWidth; y <- 0 until image.getHeight)
      if ((image.getRGB(x, y) & 0xFF000000) != 0x00000000) then
        result = false
    result

  //split and also scale the image
  def splitSpriteSheet(spriteSheet: BufferedImage, frameWidth: Int, frameHeight: Int, scaleX: Int, scaleY: Int, numsRowToPrint: Int): Array[Array[BufferedImage]] =
    val cols = spriteSheet.getWidth / frameWidth
    val rows = spriteSheet.getHeight / frameHeight
    val effectiveRows = Math.min(rows, numsRowToPrint)
    Array.tabulate(effectiveRows)(row =>
      Array.tabulate(cols)(col =>
        val frame = spriteSheet.getSubimage(col * frameWidth, row * frameHeight, frameWidth, frameHeight)
        if(isBlank(frame)) then null
        else Tools.scaleImage(frame, scaleX, scaleY)
      ).filter(_ != null)
    )