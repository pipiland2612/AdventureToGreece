package entities

import `object`.ObjectType.{OBJ_GoldenChest, OBJ_GoldenKatana, OBJ_ManaFlask, OBJ_MoonSaber, OBJ_NormalAxe, OBJ_NormalHealFlask, OBJ_NormalShield, OBJ_NormalSword, OBJ_SilverChest}
import game.{GamePanel, GameState}
import utils.{Animation, Tools}

import java.awt.Rectangle

abstract class Npc(gp: GamePanel) extends Creatures(gp):

  var idleAnimations: Map[Direction, Animation] = _
  var runAnimations: Map[Direction, Animation] = _
  var attackAnimations: Map[Direction, Animation] = _
  var deadAnimations: Map[Direction, Animation] = _

  var notMoving: Boolean = false
  areaHitBox = Rectangle(0, 0, 0, 0)

  def speak (): Unit =
    facePlayer()
    startDialogue(this, dialogueSet)

  def facePlayer (): Unit =
    this.direction = gp.player.direction match
      case Direction.UP => Direction.DOWN
      case Direction.DOWN => Direction.UP
      case Direction.LEFT => Direction.RIGHT
      case Direction.RIGHT => Direction.LEFT

end Npc

class Merchant(gp : GamePanel) extends Npc(gp):
  var pos: (Int, Int) = (0,0)
  var name = "Merchant"
  speed = 2
  notMoving = true
  solidAreaDefaultX = 10
  solidAreaDefaultY = 22
  var solidArea: Rectangle = new Rectangle(solidAreaDefaultX, solidAreaDefaultY, 24, 24)
  image = Tools.scaleImage(Tools.loadImage("npc/merchant_npc.png"), gp.tileSize, gp.tileSize)

  def setDialogue(): Unit =
    dialogues(0)(0) = "Welcome, traveler! I have wares if you have coin.\n"
    dialogues(3)(0) = "Come again !"
    dialogues(4)(0) = "You do not have enough coin"
    dialogues(5)(0) = "You do not have enough spaces"
    dialogues(6)(0) = "You can not sell an equipped item"

  setDialogue()

  def setItem(): Unit =
    inventory += new OBJ_NormalHealFlask(gp)
    inventory += new OBJ_ManaFlask(gp)
    inventory += new OBJ_NormalSword(gp)
    inventory += new OBJ_NormalAxe(gp)
    inventory += new OBJ_NormalShield(gp)
    inventory += new OBJ_SilverChest(gp)
    inventory += new OBJ_MoonSaber(gp)
    inventory += new OBJ_GoldenKatana(gp)
    inventory += new OBJ_GoldenChest(gp)
  setItem()

  override def speak(): Unit =
    super.speak()
    gp.gameState = GameState.TradeState
    gp.gui.npc = this

  override def update(): Unit = {}
//    super.setAction()

end Merchant

class Socerer (gp: GamePanel) extends Npc(gp):
  var pos: (Int, Int) = (0,0)
  var name = "Socerer"
  speed = 2
  notMoving = true
  solidAreaDefaultX = 10
  solidAreaDefaultY = 22
  var solidArea: Rectangle = new Rectangle(solidAreaDefaultX, solidAreaDefaultY, 24, 24)
  image = Tools.scaleImage(Tools.loadImage("npc/socerer.png"), gp.tileSize * 3 / 2, gp.tileSize * 3 / 2)
  dialogueSet = -1

  def setDialogue(): Unit =
    dialogues(0)(0) = "Greetings, adventurer.\nThe magic of this world is deeper than you think.\n"
    dialogues(0)(1) = "Ahead stands a gate leads to dungeon\n"
    dialogues(0)(2) = "Do not underestimate the shadows.\nThey hold secrets not meant for the faint-hearted.\n"
    dialogues(0)(3) = "You must be well-armed\nAnd REMEMBER! Bring your light candle, turn it on!\n"
    dialogues(0)(4) = "There are forces in this realm\nthat even I dare not trifle with.\nTread carefully.\n"

  setDialogue()

  override def speak(): Unit =
    super.speak()

    dialogueSet += 1
    if dialogues(dialogueSet)(0) == null then dialogueSet = 0

  override def update(): Unit = {}
//    super.setAction()

end Socerer