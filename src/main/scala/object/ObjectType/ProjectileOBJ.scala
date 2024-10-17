package `object`.ObjectType

import entities.Direction
import game.GamePanel
import items.Projectile
import utils.Tools

import java.awt.Rectangle

class OBJ_Fireball(gp: GamePanel) extends Projectile(gp):
  var name = OBJ_Fireball.Name // Use the static name from the companion object
  speed = 5
  var damage = 2
  var costMana = 20
  val maxHealth = 80
  attackArea.width = 32
  attackArea.height = 20

  var scale = 48
  // Images
  def flyAnimation = Map(
    Direction.UP -> Tools.scaleImage(Tools.loadImage("Objects/Fireball/fireball_up.png"), scale, scale),
    Direction.LEFT -> Tools.scaleImage(Tools.loadImage("Objects/Fireball/fireball_left.png"), scale, scale),
    Direction.DOWN -> Tools.scaleImage(Tools.loadImage("Objects/Fireball/fireball_down.png"), scale, scale),
    Direction.RIGHT -> Tools.scaleImage(Tools.loadImage("Objects/Fireball/fireball_right.png"), scale, scale)
  )

  var imageDisplayed = Tools.scaleImage(Tools.loadImage("Objects/Fireball/fireball_left.png"), size, size)
  image = Tools.scaleImage(Tools.loadImage("Objects/Fireball/fireball_left.png"), size, size)
  areaHitBox = Rectangle(solidAreaDefaultX, solidAreaDefaultY, scale, scale)
  solidArea = areaHitBox

object OBJ_Fireball:
  val Name: String = "Fire Ball"

end OBJ_Fireball
