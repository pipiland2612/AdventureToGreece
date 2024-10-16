package `object`

import entities.Entity
import game.{GamePanel, GameState}
import items.Light
import levels.Obstacle
import utils.Tools

import java.awt.Rectangle
import java.awt.image.BufferedImage


class OBJ_Rock(size : Int, gp: GamePanel) extends Entity (gp):
  var name = "Rock"
  var pos: (Int, Int)= (0,0)
  image = Tools.scaleImage(Tools.loadImage("Objects/rock.png"), size, size)
  collision = true
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)

end OBJ_Rock

class OBJ_Tree(size : Int, gp: GamePanel) extends Entity (gp):
  var pos: (Int, Int)= (0,0)
  var name = "Tree"
  image = Tools.scaleImage(Tools.loadImage("Objects/tree.png"), size, size)
  collision = true
  solidAreaDefaultX = size / 2 - size / 18
  solidAreaDefaultY = size - size / 8

  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size / 10, (size / 12.5).toInt)
end OBJ_Tree

class OBJ_Heart(size : Int, gp: GamePanel) extends Entity (gp):
  var name = "Heart"
  var pos: (Int, Int) = (0,0)
  image = Tools.scaleImage(Tools.loadImage("Objects/Heart/heart_full.png"), size, size)
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
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)
  var image2 = Tools.scaleImage(Tools.loadImage("Objects/Mana/mana_thirds.png"), size, size)
  var image3 = Tools.scaleImage(Tools.loadImage("Objects/Mana/mana_half.png"), size, size)
  var image4 = Tools.scaleImage(Tools.loadImage("Objects/Mana/mana_nearly.png"), size, size)
  var image5 = Tools.scaleImage(Tools.loadImage("Objects/Mana/mana_empty.png"), size, size)
end OBJ_Mana

class OBJ_LightCandle (gp: GamePanel) extends Light(gp):
  var name = "Light Candle"
  var pos = (0,0)
  var imageDisplayed = Tools.scaleImage(Tools.loadImage("Objects/candle_light.png"), 32, 32)
  image = Tools.scaleImage(Tools.loadImage("Objects/candle_light.png"), 32, 32)
  price = 100
  var lightRadius = 250
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)
end OBJ_LightCandle

class OBJ_CheckPoint (gp: GamePanel) extends Obstacle(gp):
  var name = "Check Point"
  var pos: (Int, Int) = (0,0)
  solidAreaDefaultX = 16
  solidAreaDefaultY = 10
  image = Tools.scaleImage(Tools.loadImage("Objects/check_point.png"), 48, 48)
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, 20, 32)
  collision = true

  override def interact(): Unit =
    gp.gameState = GameState.DialogueState
    gp.gui.currentDialogue = s"Game Save"
    gp.saveLoad.save()

end OBJ_CheckPoint




