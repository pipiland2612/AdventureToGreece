package entities
import game.GamePanel
import utils.Animation

import java.awt.Rectangle
import java.awt.Graphics2D
import java.awt.image.BufferedImage
import java.lang.annotation.Target

abstract class Entity(var gp: GamePanel) :

  var isCollided: Boolean = false
  var speed: Int = 0
  var direction: Direction = Direction.DOWN
  var solidArea: Rectangle
  var solidAreaDefaultX, solidAreaDefaultY: Int = 0
  var pos : (Int, Int)
  val directions = Vector(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)
 
  
  var currentAnimation: Animation = _
  var image: BufferedImage = _
  var name: String
  var collision: Boolean = false

  var attackArea: Rectangle = new Rectangle(0, 0, 0, 0)
  def getPosition: (Int, Int) = this.pos
  def getLeftX: Int = this.pos._1 + this.solidArea.x
  def getRightX: Int = this.pos._1 + this.solidArea.x + this.solidArea.width
  def getTopY: Int = this.pos._2 + this.solidArea.y
  def getBottomY: Int = this.pos._2 + this.solidArea.y + solidArea.height

  def getCol = (this.pos._1 + this.solidArea.x) / gp.tileSize
  def getRow = (this.pos._2 + this.solidArea.y) / gp.tileSize

  protected def calculateScreenCoordinates(): (Int, Int) =
    val screenX = gp.player.screenX
    val screenY = gp.player.screenY

    val tileSize = gp.tileSize
    val (worldX, worldY) = this.pos
    val (playerX, playerY) = gp.player.getPosition
    val screenTileX = worldX - playerX + screenX
    val screenTileY = worldY - playerY + screenY

    (screenTileX, screenTileY)

  // draw both objects and creatures
  def draw (g : Graphics2D): Unit =
    val (screenTileX, screenTileY) = calculateScreenCoordinates()
    val drawRange = gp.tileSize * 2
    val (worldX, worldY) = this.pos
    val (playerX, playerY) = gp.player.getPosition
    val screenX = gp.player.screenX
    val screenY = gp.player.screenY
    if (
      worldX + drawRange > playerX - screenX &&
      worldX - drawRange < playerX + screenX &&
      worldY + drawRange > playerY - screenY &&
      worldY - drawRange < playerY + screenY
    ) then
      val imageToDraw =
        if currentAnimation != null then
          currentAnimation.getCurrentFrame
        else image
      g.drawImage(imageToDraw,
        (screenTileX),
        (screenTileY),
        null)



