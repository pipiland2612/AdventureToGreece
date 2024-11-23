package entities.`object`

import entities.creatures.Creatures
import entities.items.Potion
import game.GamePanel
import utils.Tools

import java.awt.Rectangle

class OBJ_NormalHealFlask(gp: GamePanel) extends Potion(10, gp):
  name = OBJ_NormalHealFlask.Name
  effectName = OBJ_NormalHealFlask.EffectName
  price = 3
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


class OBJ_ManaFlask(gp: GamePanel) extends Potion(10, gp):
  name = OBJ_ManaFlask.Name
  effectName = OBJ_ManaFlask.EffectName
  price = 4
  isStackable = true
  var scale = 32
  var imageDisplayed = Tools.scaleImage(Tools.loadImage("Objects/mana_flask.png"), scale, scale)
  image = Tools.scaleImage(Tools.loadImage("Objects/mana_flask.png"), scale, scale)
  solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, scale, scale)

  override def applyEffect(effect: Int, creatures: Creatures): Unit =
    creatures.mana += effect
    if creatures.mana > creatures.maxMana then creatures.mana = creatures.maxMana

object OBJ_ManaFlask:
  val Name: String = "Mana Flask"
  val EffectName: String = "Mana regen"

end OBJ_ManaFlask

