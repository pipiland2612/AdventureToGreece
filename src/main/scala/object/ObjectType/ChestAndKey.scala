package `object`.ObjectType

import entities.Creatures
import game.{GamePanel, GameState}
import items.{InteractiveObjects, Item}
import utils.Tools

import java.awt.Rectangle

class OBJ_Chest(gp: GamePanel) extends InteractiveObjects (gp):
  var name = OBJ_Chest.Name
  var pos: (Int, Int) = (0,0)

  var size = 48
  image = Tools.scaleImage(Tools.loadImage("Objects/chest.png"), size, size)
  collision = true
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)

  setDialogue()
  def setDialogue(): Unit =
    dialogues(0)(0) = "You need a key to open this"
    dialogues(1)(0) = "Empty chest!"

  override def interact(): Unit =
    gp.gameState = GameState.DialogueState
    if !opened then
      startDialoque(this, 0)
    else
      startDialoque(this, 1)

  def openChest(): Unit =
    gp.gameState = GameState.DialogueState
    var sb = new StringBuilder()
    if !opened then
      sb.append(s"Open chest and find a ${loot.name}!")
      if !gp.player.obtainItem(loot) then
        sb.append(s"\nBut, you can not carry more items")
      else
        sb.append(s"\nLoot ${loot.name}")
        opened = true
      dialogues(2)(0) = sb.toString()
      startDialoque(this, 2)
    else
      dialogues(3)(0) = s"Empty"
      startDialoque(this, 3)
end OBJ_Chest


class OBJ_SilverKey(gp: GamePanel) extends Item (gp):
  var name = OBJ_SilverKey.Name
  var pos: (Int, Int) = (0, 0)
  image = Tools.scaleImage(Tools.loadImage("Objects/silver_key.png"), size, size)
  var imageDisplayed = Tools.scaleImage(Tools.loadImage("Objects/silver_key.png"), 32, 32)
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)
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
    val objIndex = getDetected(entity, gp.obj, "Chest")
    if objIndex != -1 then
      val chest = gp.obj(gp.currentMap)(objIndex)
      chest match
        case chest1: OBJ_Chest if !chest1.opened =>
          startDialoque(this, 0)
          chest1.openChest()
          true
        case _ =>
          startDialoque(this, 1)
          false
    else
      startDialoque(this, 2)
      false

end OBJ_SilverKey

object OBJ_Chest:
  val Name: String = "Chest"

object OBJ_SilverKey:
  val Name: String = "Silver Key"
