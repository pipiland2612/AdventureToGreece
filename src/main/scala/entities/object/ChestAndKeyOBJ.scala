package entities.`object`

import entities.creatures.Creatures
import entities.items.{InteractiveObjects, Item}
import game.{GamePanel, GameState}
import utils.Tools

import java.awt.Rectangle
import java.awt.image.BufferedImage

class OBJ_Chest(gp: GamePanel) extends InteractiveObjects(gp):
  var name      = OBJ_Chest.Name
  var pos       = (0, 0)
  var size      = 48
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size * 2/3)
  image         = getImage
  collision     = true

  setDialogue()
  def setDialogue(): Unit =
    dialogues(0)(0) = "You need a key to open this"
    dialogues(1)(0) = "Empty chest!"

  def getImage: BufferedImage =
    if !opened then Tools.scaleImage(Tools.loadImage("objects/chest_closed.png"), size, size)
    else Tools.scaleImage(Tools.loadImage("objects/chest_opened.png"), size, size)

  override def setLoot(loot: Item): Unit = this.loot = loot

  override def interact(): Unit =
    gp.gameState = GameState.DialogueState
    if !opened then
      startDialogue(this, 0)
    else
      startDialogue(this, 1)
      
  def openChest(): Unit =
    gp.gameState = GameState.DialogueState
    val sb = new StringBuilder()
    if !opened && loot != null then
      sb.append(s"Open chest and find a ${loot.name}!")
      if !gp.player.obtainItem(loot) then
        sb.append(s"\nBut, you can not carry more items")
      else
        sb.append(s"\nLoot ${loot.name}")
        opened = true
      dialogues(2)(0) = sb.toString()
      startDialogue(this, 2)
    else
      dialogues(3)(0) = s"Empty"
      startDialogue(this, 3)
    image         = getImage
end OBJ_Chest

class OBJ_SilverKey(gp: GamePanel) extends Item(gp):
  var name          = OBJ_SilverKey.Name
  var pos           = (0, 0)
  var solidArea     = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)
  var imageDisplayed = Tools.scaleImage(Tools.loadImage("objects/silver_key.png"), 32, 32)
  image             = Tools.scaleImage(Tools.loadImage("objects/silver_key.png"), 32, 32)

  setDialogue()

  def setDialogue(): Unit =
    dialogues(0)(0) = s"Use $name to open chest"
    dialogues(1)(0) = "The chest is already open"
    dialogues(2)(0) = s"Cannot use $name here"

  def getDescription =
    s"$name can be used \nto open chest"
  override def canOpenChest: Boolean = true

  override def use(entity: Creatures): Boolean =
    gp.gameState = GameState.DialogueState
    val objIndex = getDetected(entity, gp.obj, OBJ_Chest.Name)
    if objIndex != -1 then
      val chest = gp.obj(gp.currentMap)(objIndex)
      chest match
        case chest1: OBJ_Chest if !chest1.opened =>
          startDialogue(this, 0)
          chest1.openChest()
          true
        case _ =>
          startDialogue(this, 1)
          false
    else
      startDialogue(this, 2)
      false

end OBJ_SilverKey

object OBJ_Chest:
  val Name: String = "Chest"

object OBJ_SilverKey:
  val Name: String = "Silver Key"


class OBJ_GoldenRelic(gp: GamePanel) extends InteractiveObjects(gp):
  var name      = OBJ_GoldenRelic.Name
  var pos       = (0, 0)
  var size      = 48
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size , size)
  image         = Tools.scaleImage(Tools.loadImage("objects/gold_chest.png"), size, size)
  collision     = true

  setDialogue()
  def setDialogue(): Unit =
    dialogues(0)(0) = "You need a grail to open this"

  override def interact(): Unit =
    gp.gameState = GameState.DialogueState
    startDialogue(this, 0)

  def openRelic(): Unit =
    gp.gameState = GameState.CutSceneState  
    gp.cutSceneManager.sceneNum = gp.cutSceneManager.credit

end OBJ_GoldenRelic
object OBJ_GoldenRelic:
  val Name: String = "Golden Relic"