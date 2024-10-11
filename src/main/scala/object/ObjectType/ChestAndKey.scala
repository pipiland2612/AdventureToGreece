package `object`.ObjectType

import entities.Creatures
import game.{GamePanel, GameState}
import items.Item
import levels.Obstacle
import utils.Tools

import java.awt.Rectangle

class OBJ_Chest(size : Int, gp: GamePanel, var loot: Item) extends Obstacle (gp):
  var name = "Chest"
  var pos: (Int, Int) = (0,0)
  var opened: Boolean = false

  image = Tools.scaleImage(Tools.loadImage("Objects/chest.png"), size, size)
  collision = true
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)

  override def interact(): Unit =
    gp.gameState = GameState.DialogueState
    if !opened then
      gp.gui.currentDialogue = s"You need a silver key to open this"
    else
      gp.gui.currentDialogue = s"Empty chest!"

  def openChest(): Unit =
    gp.gameState = GameState.DialogueState
    if !opened then
      val sb: StringBuilder = new StringBuilder()
      sb.append(s"Open chest and find !" )
      if !gp.player.obtainItem(loot) then
        sb.append(s"\nYou can not carry more items")
      else
        sb.append(s"\nLoot ${loot.name}")
        opened = true
      gp.gui.currentDialogue = sb.toString()
    else
      gp.gui.currentDialogue = s"Empty!"
end OBJ_Chest


class OBJ_SilverKey( gp: GamePanel) extends Item (gp):
  var name = "Silver Key"
  var pos: (Int, Int) = (0, 0)
  image = Tools.scaleImage(Tools.loadImage("Objects/silver_key.png"), size, size)
  var imageDisplayed = Tools.scaleImage(Tools.loadImage("Objects/silver_key.png"), 32, 32)
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, size, size)
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
          gp.gui.currentDialogue = s"Use $name to open chest"
          chest1.openChest()
          true
        case _ =>
          gp.gui.currentDialogue = s"The chest is already open"
          false
    else
      gp.gui.currentDialogue = s"Cannot use $name here"
      false

end OBJ_SilverKey
