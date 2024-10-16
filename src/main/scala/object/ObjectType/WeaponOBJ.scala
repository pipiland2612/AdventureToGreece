package `object`.ObjectType

import game.GamePanel
import items.{Shield, Weapon}
import utils.Tools

import java.awt.Rectangle

class OBJ_NormalSword(gp: GamePanel) extends Weapon (15, gp):
  var scale = 48
  price = 5
  name = "Normal Sword"
  var imageDisplayed = Tools.scaleImage(Tools.loadImage("Objects/sword_normal.png"), scale * 4/5 , scale * 4/5)
  image = Tools.scaleImage(Tools.loadImage("Objects/sword_normal.png"), scale, scale)
  attackArea.width = 32
  attackArea.height = 16
  solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, scale, scale)
end OBJ_NormalSword

class OBJ_NormalAxe(gp: GamePanel) extends Weapon (6, gp) :
  name = "Normal Axe"
  price = 20
  var imageDisplayed = Tools.scaleImage(Tools.loadImage("Objects/axe_normal.png"), size - size / 5, size - size / 5)
  image = Tools.scaleImage(Tools.loadImage("Objects/axe_normal.png"), size - size / 5, size - size / 5)
  attackArea.width = 32
  attackArea.height = 20
  solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)
end OBJ_NormalAxe

class OBJ_NormalShield(gp: GamePanel) extends Shield (3, gp) :
  name = "Normal Shield"
  price = 15
  var imageDisplayed = Tools.scaleImage(Tools.loadImage("Objects/shield_normal.png"), size - size / 5, size - size / 5 )
  image = Tools.scaleImage(Tools.loadImage("Objects/shield_normal.png"), size - size / 5, size - size / 5 )
  attackArea.width = 10
  attackArea.height = 10
  solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size , size )
end OBJ_NormalShield