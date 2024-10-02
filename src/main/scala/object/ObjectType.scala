package `object`

import entities.Entity
import game.GamePanel
import utils.Tools

import java.awt.Rectangle
import java.awt.image.BufferedImage


class OBJ_HealFlask(size : Int, var pos: (Int, Int), gp: GamePanel) extends Entity (gp) :
  name = "HealFlask"
  image = Tools.scaleImage(Tools.loadImage("/Users/batman/Desktop/Adventure to Greece/src/main/resources/images/Objects/flask_medium.png"), size, size)
  collision = false
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)


end OBJ_HealFlask


class OBJ_Chest(size : Int, var pos: (Int, Int), gp: GamePanel) extends Entity (gp):
  name = "Chest"
  image = Tools.scaleImage(Tools.loadImage("/Users/batman/Desktop/Adventure to Greece/src/main/resources/images/Objects/chest.png"), size, size)
  collision = false
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)

end OBJ_Chest


class OBJ_Rock(size : Int, var pos: (Int, Int), gp: GamePanel) extends Entity (gp):
  name = "Rock"
  image = Tools.scaleImage(Tools.loadImage("/Users/batman/Desktop/Adventure to Greece/src/main/resources/images/Objects/rock.png"), size, size)
  collision = true
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)

end OBJ_Rock


class OBJ_Tree(size : Int, var pos: (Int, Int), gp: GamePanel) extends Entity (gp):
  name = "Tree"
  image = Tools.scaleImage(Tools.loadImage("/Users/batman/Desktop/Adventure to Greece/src/main/resources/images/Objects/tree.png"), size, size)
  collision = true
  solidAreaDefaultX = size / 2 - size / 18
  solidAreaDefaultY = size - size / 8
  // change the position to the roots
  override def getPosition: (Int, Int) = (solidAreaDefaultX + this.pos._1, solidAreaDefaultY / 4 + this.pos._2 - gp.tileSize)
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size / 10, size / 11)
end OBJ_Tree

class OBJ_Heart(size : Int, var pos: (Int, Int), gp: GamePanel) extends Entity (gp):
  name = "Heart"
  image = Tools.scaleImage(Tools.loadImage("/Users/batman/Desktop/Adventure to Greece/src/main/resources/images/Objects/heart_full.png"), size, size)
  collision = true
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)
  var image2 = Tools.scaleImage(Tools.loadImage("/Users/batman/Desktop/Adventure to Greece/src/main/resources/images/Objects/heart_thirds.png"), size, size)
  var image3 = Tools.scaleImage(Tools.loadImage("/Users/batman/Desktop/Adventure to Greece/src/main/resources/images/Objects/heart_half.png"), size, size)
  var image4 = Tools.scaleImage(Tools.loadImage("/Users/batman/Desktop/Adventure to Greece/src/main/resources/images/Objects/heart_nearly.png"), size, size)
  var image5 = Tools.scaleImage(Tools.loadImage("/Users/batman/Desktop/Adventure to Greece/src/main/resources/images/Objects/heart_empty.png"), size, size)
end OBJ_Heart