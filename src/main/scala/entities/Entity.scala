package entities
import game.GamePanel
import utils.Animation

import java.awt.Rectangle
import java.awt.Graphics2D
import java.awt.image.BufferedImage

abstract class Entity(var gp: GamePanel) :

  var solidArea: Rectangle
  var solidAreaDefaultX, solidAreaDefaultY: Int = 0
  var pos : (Int, Int)
  val directions = Vector(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)
 
  var currentAnimation: Animation = _
  var image: BufferedImage = _
  var name: String = ""
  var collision: Boolean = false

  def getPosition: (Int, Int) = this.pos

  // draw both objects and creatures
  def draw (g : Graphics2D): Unit =
    val (playerX, playerY) = gp.player.getPosition
    val screenX = gp.player.screenX
    val screenY = gp.player.screenY
    val (worldX, worldY) = this.pos
    val tileSize = gp.tileSize

    val screenTileX = worldX - playerX + screenX
    val screenTileY = worldY - playerY + screenY

    val drawRange = tileSize * 2
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



