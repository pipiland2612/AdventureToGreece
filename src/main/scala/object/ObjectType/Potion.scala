package `object`.ObjectType

import entities.Creatures
import game.GamePanel
import items.Potion
import utils.Tools

import java.awt.Rectangle

class OBJ_NormalHealFlask(gp: GamePanel) extends Potion(10, gp):
  name = OBJ_NormalHealFlask.Name
  effectName = OBJ_NormalHealFlask.EffectName
  price = 10
  isStackable = true
  var scale = 32
  var imageDisplayed = Tools.scaleImage(Tools.loadImage("Objects/flask_medium.png"), scale, scale)
  image = Tools.scaleImage(Tools.loadImage("Objects/flask_medium.png"), scale, scale)
  solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, scale, scale)

  override def applyEffect(effect: Int, creatures: Creatures): Unit =
    creatures.health += effect
    if creatures.health > creatures.maxHealth then creatures.health = creatures.maxHealth

object OBJ_NormalHealFlask:
  val Name: String = "Normal Heal Flask"
  val EffectName: String = "Heal"

end OBJ_NormalHealFlask
