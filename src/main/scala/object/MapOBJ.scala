package `object`

import entities.Entity
import game.{GamePanel, GameState}
import items.{InteractiveObjects, Light}
import utils.Tools

import java.awt.Rectangle
import java.awt.image.BufferedImage

class OBJ_Rock(gp: GamePanel) extends Entity(gp):
  var name      = OBJ_Rock.Name
  var pos       = (0, 0)
  var size      = 48
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)
  image         = Tools.scaleImage(Tools.loadImage("Objects/rock.png"), size, size)
  collision     = true

object OBJ_Rock:
  val Name: String = "Rock"

end OBJ_Rock

class OBJ_Tree(gp: GamePanel) extends Entity(gp):
  var name      = OBJ_Tree.Name
  var pos       = (0, 0)
  var size      = 256
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size / 10, (size / 12.5).toInt)
  image         = Tools.scaleImage(Tools.loadImage("Objects/tree.png"), size, size)
  collision     = true
  solidAreaDefaultX = size / 2 - size / 18
  solidAreaDefaultY = size - size / 8

object OBJ_Tree:
  val Name: String = "Tree"

end OBJ_Tree

class OBJ_Heart(gp: GamePanel) extends Entity(gp):
  var name      = OBJ_Heart.Name
  var pos       = (0, 0)
  var size      = 25
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)
  image         = Tools.scaleImage(Tools.loadImage("Objects/Heart/heart_full.png"), size, size)
  var image2    = Tools.scaleImage(Tools.loadImage("Objects/Heart/heart_thirds.png"), size, size)
  var image3    = Tools.scaleImage(Tools.loadImage("Objects/Heart/heart_half.png"), size, size)
  var image4    = Tools.scaleImage(Tools.loadImage("Objects/Heart/heart_nearly.png"), size, size)
  var image5    = Tools.scaleImage(Tools.loadImage("Objects/Heart/heart_empty.png"), size, size)

object OBJ_Heart:
  val Name: String = "Heart"

end OBJ_Heart

class OBJ_Mana(gp: GamePanel) extends Entity(gp):
  var name      = OBJ_Mana.Name
  var pos       = (0, 0)
  var size      = 25
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)
  image         = Tools.scaleImage(Tools.loadImage("Objects/Mana/mana_full.png"), size, size)
  var image2    = Tools.scaleImage(Tools.loadImage("Objects/Mana/mana_thirds.png"), size, size)
  var image3    = Tools.scaleImage(Tools.loadImage("Objects/Mana/mana_half.png"), size, size)
  var image4    = Tools.scaleImage(Tools.loadImage("Objects/Mana/mana_nearly.png"), size, size)
  var image5    = Tools.scaleImage(Tools.loadImage("Objects/Mana/mana_empty.png"), size, size)

object OBJ_Mana:
  val Name: String = "Mana"

end OBJ_Mana

class OBJ_LightCandle(gp: GamePanel) extends Light(gp):
  var name         = OBJ_LightCandle.Name
  var pos          = (0, 0)
  var solidArea    = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)
  var imageDisplayed = Tools.scaleImage(Tools.loadImage("Objects/candle_light.png"), 32, 32)
  image            = Tools.scaleImage(Tools.loadImage("Objects/candle_light.png"), 32, 32)
  price            = 100
  var lightRadius  = 250

object OBJ_LightCandle:
  val Name: String = "Light Candle"

end OBJ_LightCandle

class OBJ_CheckPoint(gp: GamePanel) extends InteractiveObjects(gp):
  var name      = OBJ_CheckPoint.Name
  var pos       = (0, 0)
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, 20, 32)
  solidAreaDefaultX = 16
  solidAreaDefaultY = 10
  image         = Tools.scaleImage(Tools.loadImage("Objects/check_point.png"), 48, 48)
  collision     = true
  setDialogue()

  def setDialogue(): Unit =
    dialogues(0)(0) = s"Your progress has been saved.\n"

  override def interact(): Unit =
    gp.player.lastSavePoint = this.pos
    gp.player.lastSavePointMap = gp.currentMap
    gp.gameState = GameState.DialogueState
    startDialogue(this, 0)
    gp.saveLoad.save()

object OBJ_CheckPoint:
  val Name: String = "Check Point"
end OBJ_CheckPoint

class OBJ_DungeonGate(gp: GamePanel) extends Entity(gp):
  var name      = OBJ_DungeonGate.Name
  var pos       = (0, 0)
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, 32, 32)
  solidAreaDefaultX = 0
  solidAreaDefaultY = 0
  image         = Tools.scaleImage(Tools.loadImage("Objects/dungeon_gate2.png"), gp.tileSize * 2, gp.tileSize * 2)

object OBJ_DungeonGate:
  val Name: String = "Dungeon Gate"

end OBJ_DungeonGate
