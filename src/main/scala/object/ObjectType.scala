package `object`

import entities.{Creatures, Direction, Entity}
import game.GamePanel
import items.{Coin, Item, Potion, Projectile, Shield, Weapon}
import utils.{Animation, Tools}

import java.awt.Rectangle
import java.awt.image.BufferedImage

class OBJ_Chest(size : Int, var pos: (Int, Int), gp: GamePanel) extends Entity (gp):
  var name = "Chest"
  image = Tools.scaleImage(Tools.loadImage("Objects/chest.png"), size, size)
  collision = false
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)

end OBJ_Chest


class OBJ_Rock(size : Int, var pos: (Int, Int), gp: GamePanel) extends Entity (gp):
  var name = "Rock"
  image = Tools.scaleImage(Tools.loadImage("Objects/rock.png"), size, size)
  collision = true
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)

end OBJ_Rock


class OBJ_Tree(size : Int, var pos: (Int, Int), gp: GamePanel) extends Entity (gp):
  var name = "Tree"
  image = Tools.scaleImage(Tools.loadImage("Objects/tree.png"), size, size)
  collision = true
  solidAreaDefaultX = size / 2 - size / 18
  solidAreaDefaultY = size - size / 8
  // change the position to the roots
  override def getPosition: (Int, Int) = (solidAreaDefaultX + this.pos._1, solidAreaDefaultY / 4 + this.pos._2 - gp.tileSize)
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size / 10, size / 11)
end OBJ_Tree

class OBJ_Heart(size : Int, gp: GamePanel) extends Entity (gp):
  var name = "Heart"
  var pos: (Int, Int) = (0,0)
  image = Tools.scaleImage(Tools.loadImage("Objects/Heart/heart_full.png"), size, size)
  collision = true
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)
  var image2 = Tools.scaleImage(Tools.loadImage("Objects/Heart/heart_thirds.png"), size, size)
  var image3 = Tools.scaleImage(Tools.loadImage("Objects/Heart/heart_half.png"), size, size)
  var image4 = Tools.scaleImage(Tools.loadImage("Objects/Heart/heart_nearly.png"), size, size)
  var image5 = Tools.scaleImage(Tools.loadImage("Objects/Heart/heart_empty.png"), size, size)
end OBJ_Heart

class OBJ_Mana (size : Int, gp: GamePanel) extends Entity (gp):
  var name = "Mana"
  var pos: (Int, Int) = (0,0)
  image = Tools.scaleImage(Tools.loadImage("Objects/Mana/mana_full.png"), size, size)
  collision = true
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)
  var image2 = Tools.scaleImage(Tools.loadImage("Objects/Mana/mana_thirds.png"), size, size)
  var image3 = Tools.scaleImage(Tools.loadImage("Objects/Mana/mana_half.png"), size, size)
  var image4 = Tools.scaleImage(Tools.loadImage("Objects/Mana/mana_nearly.png"), size, size)
  var image5 = Tools.scaleImage(Tools.loadImage("Objects/Mana/mana_empty.png"), size, size)
end OBJ_Mana

class OBJ_NormalHealFlask(gp: GamePanel) extends Potion (10, gp) :
  name = "Normal Heal Flask"
  effectName = "Heal"
  var scale = 32
  var imageDisplayed = Tools.scaleImage(Tools.loadImage("Objects/flask_medium.png"), scale, scale)
  image = Tools.scaleImage(Tools.loadImage("Objects/flask_medium.png"), scale, scale)
  solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, scale, scale)

  override def applyEffect(effect: Int, creatures: Creatures): Unit =
    creatures.health += effect
    if creatures.health > creatures.maxHealth then creatures.health = creatures.maxHealth

end OBJ_NormalHealFlask

class OBJ_NormalSword(gp: GamePanel) extends Weapon (15, gp):
  var scale = 48
  name = "Normal Sword"
  var imageDisplayed = Tools.scaleImage(Tools.loadImage("Objects/sword_normal.png"), scale * 4/5 , scale * 4/5)
  image = Tools.scaleImage(Tools.loadImage("Objects/sword_normal.png"), scale, scale)
  attackArea.width = 32
  attackArea.height = 16
  solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, scale, scale)
end OBJ_NormalSword

class OBJ_NormalAxe(gp: GamePanel) extends Weapon (6, gp) :
  name = "Normal Axe"
  var imageDisplayed = Tools.scaleImage(Tools.loadImage("Objects/axe_normal.png"), size - size / 5, size - size / 5)
  image = Tools.scaleImage(Tools.loadImage("Objects/axe_normal.png"), size - size / 5, size - size / 5)
  attackArea.width = 32
  attackArea.height = 20
  solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)
end OBJ_NormalAxe

class OBJ_NormalShield(gp: GamePanel) extends Shield (3, gp) :
  name = "Normal shield"
  var imageDisplayed = Tools.scaleImage(Tools.loadImage("Objects/shield_normal.png"), size - size / 5, size - size / 5 )
  image = Tools.scaleImage(Tools.loadImage("Objects/shield_normal.png"), size - size / 5, size - size / 5 )
  attackArea.width = 10
  attackArea.height = 10
  solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size , size )
end OBJ_NormalShield

class OBJ_Fireball(gp : GamePanel) extends Projectile(gp) :
  var name = "Fire Ball"
  speed = 5
  var damage = 2
  var costMana = 20
  val maxHealth = 80
  attackArea.width = 32
  attackArea.height = 20

  var scale = 48
  // Images
  def flyAnimation = Map (
    Direction.UP -> Tools.scaleImage(Tools.loadImage("Objects/Fireball/fireball_up.png"), scale, scale),
    Direction.LEFT -> Tools.scaleImage(Tools.loadImage("Objects/Fireball/fireball_left.png"), scale, scale),
    Direction.DOWN -> Tools.scaleImage(Tools.loadImage("Objects/Fireball/fireball_down.png"), scale, scale),
    Direction.RIGHT -> Tools.scaleImage(Tools.loadImage("Objects/Fireball/fireball_right.png"), scale, scale)
  )

  var imageDisplayed = Tools.scaleImage(Tools.loadImage("Objects/Fireball/fireball_left.png"), size, size)
  image = Tools.scaleImage(Tools.loadImage("Objects/Fireball/fireball_left.png"), size, size)
  solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, scale, scale)

end OBJ_Fireball

class OBJ_BronzeCoin (gp: GamePanel) extends Coin(1, gp):
  var name = "Bronze Coin"
  var pos = (0,0)
  var imageDisplayed = Tools.loadImage("Objects/coin.png")
  image = Tools.scaleImage(Tools.loadImage("Objects/coin.png"), 20, 20)
