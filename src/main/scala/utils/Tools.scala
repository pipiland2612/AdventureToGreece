package utils

import Enemy.Enemy
import entities.{Creatures, Direction, Entity, State}
import game.GamePanel

import java.awt.geom.AffineTransform
import java.awt.{BasicStroke, Color, Graphics2D, Image}
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

object Tools:
  // Images helper methods
  def flipImageHorizontally(image: BufferedImage): BufferedImage =
    val flippedImage = new BufferedImage(image.getWidth, image.getHeight, image.getType)
    val g2d: Graphics2D = flippedImage.createGraphics()
    val at = AffineTransform.getScaleInstance(-1, 1)
    at.translate(-image.getWidth, 0)
    g2d.drawImage(image, at, null)
    g2d.dispose()
    flippedImage

  def flipImageVertically(image: BufferedImage): BufferedImage =
    val flippedImage = new BufferedImage(image.getWidth, image.getHeight, image.getType)
    val g2d: Graphics2D = flippedImage.createGraphics()
    val at = AffineTransform.getScaleInstance(1, -1)
    at.translate(0, -image.getHeight)
    g2d.drawImage(image, at, null)
    g2d.dispose()
    flippedImage

  def loadImage(path: String): BufferedImage =
    try
      ImageIO.read(getClass.getResourceAsStream(s"/images/$path"))
    catch
      case e: Exception =>
        throw new RuntimeException(s"Failed to load image at path: $path")

  def scaleImage(origin: BufferedImage, width: Int, height: Int): BufferedImage =
    val resized = origin.getScaledInstance(width, height, Image.SCALE_DEFAULT)
    val scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val g: Graphics2D = scaledImage.createGraphics()
    g.drawImage(resized, 0, 0, width, height, null)
    g.dispose()
    scaledImage

  def flipFrames(frames: Vector[BufferedImage], flipFunction: BufferedImage => BufferedImage): Vector[BufferedImage] = frames.map(flipFunction)

  def setupCommonAnimations(flipFunction: BufferedImage => BufferedImage, commonFrames: Vector[BufferedImage], framesRate: Int, attackStartFrame: Int = -1, attackEndFrame: Int = -1): Map[Direction, Animation] =
    Map(
      Direction.UP    -> Animation(commonFrames, framesRate, attackStartFrame, attackEndFrame),
      Direction.RIGHT -> Animation(commonFrames, framesRate, attackStartFrame, attackEndFrame),
      Direction.LEFT  -> Animation(flipFrames(commonFrames, flipFunction), framesRate, attackStartFrame, attackEndFrame),
      Direction.DOWN  -> Animation(flipFrames(commonFrames, flipFunction), framesRate, attackStartFrame, attackEndFrame)
    )

  // Position helper methods
  def resetSolidArea[T <: Entity](entity: T): Unit =
    entity.solidArea.x = entity.solidAreaDefaultX
    entity.solidArea.y = entity.solidAreaDefaultY

  def updateSolidArea[T <: Entity](entity: T): Unit =
    val (x, y) = entity.getPosition
    entity.solidArea.x = x + entity.solidArea.x
    entity.solidArea.y = y + entity.solidArea.y

  def updateAreaHitBox[T <: Entity](creatures: T): Unit =
    val (x, y) = creatures.getPosition
    creatures.areaHitBox.x = x + creatures.areaHitBox.x
    creatures.areaHitBox.y = y + creatures.areaHitBox.y

  def resetAreaHitBox[T <: Entity](creatures: T): Unit =
    creatures.areaHitBox.x = creatures.areaDefaultX
    creatures.areaHitBox.y = creatures.areaDefaultY

  // Frames helper methods
  def loadFrames(path: String, frameSizeX: Int, frameSizeY: Int, scaleX: Int, scaleY: Int, numsRow: Int): Array[Array[BufferedImage]] =
    val image: BufferedImage = Tools.loadImage(path + ".png")
    SpriteSheet.splitSpriteSheet(image, frameSizeX, frameSizeY, scaleX, scaleY, numsRow)

  def getFrames(framesName: Array[Array[BufferedImage]], rowsNum: Int, colLimit: Int): Vector[BufferedImage] =
    if rowsNum >= framesName.length then Vector.empty
    else
      val selectedRow = framesName(rowsNum)
      val effectiveColLimit = Math.min(colLimit, selectedRow.length - 1)
      selectedRow.take(effectiveColLimit + 1).toVector

  // Debug method
  def renderDebugInfo(g: Graphics2D, entity: Creatures, objects: Array[Array[Entity]], creatures: Array[Array[Enemy]], gp: GamePanel): Unit =
    // Draw entity solid area
    g.setColor(Color.RED)
    g.drawRect(entity.solidArea.x + gp.player.screenX, entity.solidArea.y + gp.player.screenY, entity.solidArea.width, entity.solidArea.height)
    g.drawRect(entity.areaHitBox.x + gp.player.screenX, entity.areaHitBox.y + gp.player.screenY, entity.areaHitBox.width, entity.areaHitBox.height)

    // Draw attack hitbox
    g.setColor(Color.GREEN)
    g.drawRect(
      entity.attackHitBox.x - entity.getPosition._1 + gp.player.screenX,
      entity.attackHitBox.y - entity.getPosition._2 + gp.player.screenY,
      entity.attackHitBox.width,
      entity.attackHitBox.height
    )

    // Draw object hitboxes
    for i <- objects(1).indices do
      if objects(gp.currentMap)(i) != null then
        g.setColor(Color.BLUE)
        g.drawRect(
          objects(gp.currentMap)(i).solidArea.x + objects(gp.currentMap)(i).getPosition._1 - entity.getPosition._1 + gp.player.screenX,
          objects(gp.currentMap)(i).solidArea.y + objects(gp.currentMap)(i).getPosition._2 - entity.getPosition._2 + gp.player.screenY,
          objects(gp.currentMap)(i).solidArea.width,
          objects(gp.currentMap)(i).solidArea.height
        )

    // Draw NPC hitboxes
    for i <- gp.npcList(1).indices do
      if gp.npcList(gp.currentMap)(i) != null then
        g.setColor(Color.BLUE)
        g.drawRect(
          gp.npcList(gp.currentMap)(i).solidArea.x + gp.npcList(gp.currentMap)(i).pos._1 - entity.getPosition._1 + gp.player.screenX,
          gp.npcList(gp.currentMap)(i).solidArea.y + gp.npcList(gp.currentMap)(i).pos._2 - entity.getPosition._2 + gp.player.screenY,
          gp.npcList(gp.currentMap)(i).solidArea.width,
          gp.npcList(gp.currentMap)(i).solidArea.height
        )

    // Draw creatures hitboxes
    for i <- creatures(1).indices do
      if creatures(gp.currentMap)(i) != null then
        g.setColor(Color.YELLOW)
        g.drawRect(
          creatures(gp.currentMap)(i).solidArea.x + creatures(gp.currentMap)(i).pos._1 - entity.getPosition._1 + gp.player.screenX,
          creatures(gp.currentMap)(i).solidArea.y + creatures(gp.currentMap)(i).pos._2 - entity.getPosition._2 + gp.player.screenY,
          creatures(gp.currentMap)(i).solidArea.width,
          creatures(gp.currentMap)(i).solidArea.height
        )

        g.drawRect(
          creatures(gp.currentMap)(i).areaHitBox.x + creatures(gp.currentMap)(i).pos._1 - entity.getPosition._1 + gp.player.screenX,
          creatures(gp.currentMap)(i).areaHitBox.y + creatures(gp.currentMap)(i).pos._2 - entity.getPosition._2 + gp.player.screenY,
          creatures(gp.currentMap)(i).areaHitBox.width,
          creatures(gp.currentMap)(i).areaHitBox.height
        )

        if creatures(gp.currentMap)(i).state == State.ATTACK then
          g.setColor(Color.GREEN)
          g.drawRect(
            creatures(gp.currentMap)(i).attackHitBox.x - entity.getPosition._1 + gp.player.screenX,
            creatures(gp.currentMap)(i).attackHitBox.y - entity.getPosition._2 + gp.player.screenY,
            creatures(gp.currentMap)(i).attackHitBox.width,
            creatures(gp.currentMap)(i).attackHitBox.height
          )

  // UI drawing helper methods
  def drawSubWindow(g2: Graphics2D, x: Int, y: Int, width: Int, height: Int): Unit =
    g2.setColor(Color(0, 0, 0, 210))
    g2.fillRoundRect(x, y, width, height, 35, 35)
    g2.setColor(Color(255, 255, 255))
    g2.setStroke(new BasicStroke(5))
    g2.drawRoundRect(x + 5, y + 5, width - 10, height - 10, 25, 25)

  def getCenterX(g2: Graphics2D, gp: GamePanel, text: String): Int =
    val length = g2.getFontMetrics.getStringBounds(text, g2).getWidth
    (gp.screenWidth / 2 - length / 2).toInt

  def getRightX(g2: Graphics2D, text: String, tailX: Int): Int =
    val length = g2.getFontMetrics.getStringBounds(text, g2).getWidth
    (tailX - length).toInt
