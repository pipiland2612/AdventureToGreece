package entities

import `object`.{OBJ_NormalAxe, OBJ_NormalHealFlask, OBJ_NormalShield, OBJ_NormalSword}
import game.{GamePanel, GameState}
import items.Item
import utils.{Animation, Tools}

import java.awt.Rectangle

abstract class Npc(gp: GamePanel) extends Creatures(gp):

  var idleAnimations: Map[Direction, Animation] = _
  var runAnimations: Map[Direction, Animation] = _
  var attackAnimations: Map[Direction, Animation] = _
  var deadAnimations: Map[Direction, Animation] = _

  var dialogues = new Array[String](20)
  var dialogueIndex = 0

  def speak (): Unit =
    if dialogues(dialogueIndex) == null then
      dialogueIndex = 0
    gp.gui.currentDialogue = dialogues(dialogueIndex)
    dialogueIndex += 1

    this.direction = this.direction match
      case Direction.UP => Direction.DOWN
      case Direction.DOWN => Direction.UP
      case Direction.LEFT => Direction.RIGHT
      case Direction.RIGHT => Direction.LEFT
      case Direction.ANY => null

end Npc


class Merchant(gp : GamePanel, var pos: (Int, Int)) extends Npc(gp):
  var name = "Merchant"

  solidAreaDefaultX = 10
  solidAreaDefaultY = 22
  var solidArea: Rectangle = new Rectangle(solidAreaDefaultX, solidAreaDefaultY, 24, 24)
  image = Tools.scaleImage(Tools.loadImage("npc/merchant_npc.png"), gp.tileSize, gp.tileSize)

  def setDialogue(): Unit =
    dialogues(0) = "Welcome, traveler! I have wares if you have coin."
    dialogues(1) = "Ah, I see you have a discerning eye!\nPerhaps you'd like to take a look at my finest potions."
    dialogues(2) = "No refunds!\nJust kidding, but be careful with that sword—it’s sharp!"
    dialogues(3) = "Business is slow these days...\nmaybe I should start selling enchanted carrots."
    dialogues(4) = "Looking for something special?\nPerhaps an amulet to ward off those pesky goblins?"
    dialogues(5) = "I once traveled far and wide,\nbut now, I let the adventurers come to me.\nMuch easier on the boots."
    dialogues(6) = "Oh, you’ve got a good eye for quality!\nThis one’s been enchanted by\nthe finest wizard in the kingdom."
    dialogues(7) = "Need supplies for your journey?\nI’ve got potions, charms,\nand a little bit of good luck for sale."
    dialogues(8) = "Watch out for bandits out there!\nAnd remember, \na well-equipped adventurer is a happy adventurer!"
    dialogues(9) = "I may look like an old man,\nbut I tell you, \nI could beat you in a haggling contest any day!"
  setDialogue()

  def setItem(): Unit =
    inventory += new OBJ_NormalHealFlask(gp)
    inventory += new OBJ_NormalSword(gp)
    inventory += new OBJ_NormalHealFlask(gp)
    inventory += new OBJ_NormalAxe(gp)
    inventory += new OBJ_NormalAxe(gp)
    inventory += new OBJ_NormalShield(gp)
  setItem()

  override def speak(): Unit =
    super.speak()
    gp.gameState = GameState.TradeState
    gp.gui.merchant = this


end Merchant

