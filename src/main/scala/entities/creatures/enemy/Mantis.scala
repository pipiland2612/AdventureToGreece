package entities.creatures.enemy

import entities.`object`.{OBJ_ManaFlask, OBJ_SilverKey}
import entities.creatures.Direction
import entities.items.Item
import game.GamePanel
import utils.Tools
import utils.Animation

import java.awt.Rectangle
class Mantis(gp : GamePanel) extends Enemy(gp) :
  // stats
  var pos = (0,0)
  var name = "Mantis"
  speed = 2
  maxHealth = 15
  health = maxHealth
  damagePower = 7
  var defense = 3
  maxInvincDuration = 15
  var expGet: Int = 7
  var itemDropped: Vector[Item] = Vector(new OBJ_ManaFlask(gp), new OBJ_SilverKey(gp))

  var attackRate = 40
  var changeDirectionInterval = 60
  var verticalScanRange: Int = gp.tileSize
  var horizontalScanRange: Int = gp.tileSize
  attackTimeAnimation = 30
  attackArea.width = gp.tileSize
  attackArea.height = gp.tileSize

  // world stats
  var solidAreaWidth = 17
  var solidAreaHeight = 23
  var solidAreaX = 25
  var solidAreaY = 40
  solidAreaDefaultX = solidAreaX
  solidAreaDefaultY = solidAreaY
  var solidArea = Rectangle(solidAreaX , solidAreaY, solidAreaWidth , solidAreaHeight)

  areaDefaultX = 17
  areaDefaultY = 17
  var areaHitBoxWidth = (solidAreaWidth * 2)
  var areaHitBoxHeight = solidAreaHeight * 2

  areaHitBox = Rectangle(areaDefaultX , areaDefaultY, areaHitBoxWidth, areaHitBoxHeight)

  private val frameSize = 32
  private val scale = (48 * 1.5).toInt
  private val idleSpriteFrames = Tools.loadFrames("enemy/Mantis/MantisIdle", frameSize, frameSize, scale, scale, 2)
  private val walkSpriteFrames = Tools.loadFrames("enemy/Mantis/MantisMove", frameSize, frameSize, scale, scale, 4)
  private val attackSpriteFrames = Tools.loadFrames("enemy/Mantis/MantisAttack", frameSize, frameSize, scale, scale, 4)

  var idleAnimations = Map(
    Direction.DOWN -> Animation(Vector(idleSpriteFrames(0)(0)), 5),
    Direction.LEFT -> Animation(Vector(idleSpriteFrames(0)(1)), 5),
    Direction.RIGHT -> Animation(Vector(idleSpriteFrames(1)(0)), 5),
    Direction.UP -> Animation(Vector(idleSpriteFrames(1)(1)), 5),
  )

  var runAnimations = Map(
    Direction.UP -> Animation(
      Tools.getFrames(walkSpriteFrames, 3, 4), 5),
    Direction.LEFT -> Animation(
      Tools.getFrames(walkSpriteFrames, 2, 4), 5),
    Direction.DOWN -> Animation(
      Tools.getFrames(walkSpriteFrames, 0, 4), 5),
    Direction.RIGHT -> Animation(
      Tools.getFrames(walkSpriteFrames, 1, 4), 5),
  )

  var attackAnimations = Map(
    Direction.UP -> Animation(
      Tools.getFrames(attackSpriteFrames, 3, 7), 5, 1, 5),
    Direction.LEFT -> Animation(
      Tools.getFrames(attackSpriteFrames, 2, 7), 5, 1, 5),
    Direction.DOWN -> Animation(
      Tools.getFrames(attackSpriteFrames, 0, 7), 5, 1, 5),
    Direction.RIGHT -> Animation(
      Tools.getFrames(attackSpriteFrames, 1, 7), 5, 1, 5),
  )

//
  override def attackHitBox: Rectangle = direction match
    case Direction.UP => Rectangle(pos._1 + (areaHitBox.x ) , pos._2 + areaHitBox.y - attackArea.height, attackArea.width, attackArea.height)
    case Direction.DOWN => Rectangle(pos._1 + (areaHitBox.x ), pos._2 + areaHitBox.y + attackArea.height, attackArea.width, attackArea.height)
    case Direction.LEFT => Rectangle(pos._1 + areaHitBox.x - attackArea.width, pos._2 + areaHitBox.y , attackArea.width, attackArea.height)
    case Direction.RIGHT => Rectangle(pos._1 + (areaHitBox.width / 4) + attackArea.width, pos._2 + areaHitBox.y, attackArea.width, attackArea.height)
    case Direction.ANY => Rectangle(0,0,0,0)

  currentAnimation = idleAnimations(this.direction)


