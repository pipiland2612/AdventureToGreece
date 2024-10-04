package `object`

import entities.{Creatures, Entity}
import game.GamePanel
import items.{Item, Potion, Shield, Weapon}
import utils.Tools

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

class OBJ_Heart(size : Int, var pos: (Int, Int), gp: GamePanel) extends Entity (gp):
  var name = "Heart"
  image = Tools.scaleImage(Tools.loadImage("Objects/heart_full.png"), size, size)
  collision = true
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)
  var image2 = Tools.scaleImage(Tools.loadImage("Objects/heart_thirds.png"), size, size)
  var image3 = Tools.scaleImage(Tools.loadImage("Objects/heart_half.png"), size, size)
  var image4 = Tools.scaleImage(Tools.loadImage("Objects/heart_nearly.png"), size, size)
  var image5 = Tools.scaleImage(Tools.loadImage("Objects/heart_empty.png"), size, size)
end OBJ_Heart



class OBJ_HealFlask(size : Int, effect : Int, gp: GamePanel) extends Potion (effect, gp) :
  name = "Heal Flask"
  effectName = "Heal"
  image = Tools.scaleImage(Tools.loadImage("Objects/flask_medium.png"), size, size)
  solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)

  override def applyEffect(effect: Int, creatures: Creatures): Unit =
    creatures.health += effect
    if creatures.health > creatures.maxHealth then creatures.health = creatures.maxHealth

end OBJ_HealFlask

class OBJ_NormalSword(size : Int, gp: GamePanel) extends Weapon (5, gp):
  name = "Normal Sword"
  image = Tools.scaleImage(Tools.loadImage("Objects/sword_normal.png"), size - size / 5, size - size / 5)
  attackArea.width = 32
  attackArea.height = 16
  solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)
end OBJ_NormalSword

class OBJ_NormalAxe(size : Int, gp: GamePanel) extends Weapon (6, gp) :
  name = "Normal Axe"
  image = Tools.scaleImage(Tools.loadImage("Objects/axe_normal.png"), size - size / 5, size - size /5)
  attackArea.width = 32
  attackArea.height = 20
  solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)
end OBJ_NormalAxe

class OBJ_NormalShield(size : Int, gp: GamePanel) extends Shield (3, gp) :
  name = "Normal shield"
  image = Tools.scaleImage(Tools.loadImage("Objects/shield_normal.png"), size - size / 5, size - size /5 )
  attackArea.width = 10
  attackArea.height = 10
  solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size , size )
end OBJ_NormalShield

