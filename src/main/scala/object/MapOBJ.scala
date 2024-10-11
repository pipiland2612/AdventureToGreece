package `object`

import entities.{Creatures, Direction, Entity}
import game.{GamePanel, GameState}
import items.{Coin, Item, Potion, Projectile, Shield, Weapon}
import levels.Obstacle
import utils.{Animation, Tools}

import java.awt.Rectangle
import java.awt.image.BufferedImage


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
