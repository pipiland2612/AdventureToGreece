package entities.creatures.enemy

import entities.`object`.{OBJ_Fireball, OBJ_NormalHealFlask, OBJ_NormalShield, OBJ_NormalSword, OBJ_SilverKey, OBJ_BronzeCoin}
import entities.creatures.Direction
import entities.items.Item
import game.GamePanel
import utils.Tools
import utils.Animation

import java.awt.Rectangle
class Beetle(gp : GamePanel) extends Enemy(gp) :
  // stats
  var pos = (0,0)
  var name = "Beetle"
  speed = 3
  maxHealth = 10
  health = maxHealth
  damagePower = 5
  var defense = 2
  maxInvincDuration = 15
  var expGet: Int = 4
  var itemDropped: Vector[Item] = Vector(new OBJ_BronzeCoin(gp), new OBJ_NormalHealFlask(gp), new OBJ_SilverKey(gp))

  var changeDirectionInterval = 30
  var attackRate = 15 // Chance to perform attack
  var verticalScanRange: Int = gp.tileSize
  var horizontalScanRange: Int = gp.tileSize
  attackTimeAnimation = 30
  attackArea.width = gp.tileSize / 2
  attackArea.height = gp.tileSize / 2

  // world stats
  var solidAreaWidth = 16
  var solidAreaHeight = 12
  var solidAreaX = 20
  var solidAreaY = 23
  solidAreaDefaultX = solidAreaX
  solidAreaDefaultY = solidAreaY
  var solidArea = Rectangle(solidAreaX , solidAreaY, solidAreaWidth , solidAreaHeight)

  areaDefaultX = 17
  areaDefaultY = 17
  var areaHitBoxWidth = (solidAreaWidth * 1.5).toInt
  var areaHitBoxHeight = solidAreaHeight * 2

  areaHitBox = Rectangle(areaDefaultX , areaDefaultY, areaHitBoxWidth, areaHitBoxHeight)

  private val frameSize = 32
  private val scale = (48 * 1.25).toInt
  private val idleSpriteFrames = Tools.loadFrames("enemy/Beetle/BeetleIdle", frameSize, frameSize, scale, scale, 4)
  private val walkSpriteFrames = Tools.loadFrames("enemy/Beetle/BeetleMove", frameSize, frameSize, scale, scale, 4)
  private val attackSpriteFrames = Tools.loadFrames("enemy/Beetle/BeetleAttack", frameSize, frameSize, scale, scale, 4)

  var idleAnimations = Map(
    Direction.DOWN -> Animation(Vector(idleSpriteFrames(0)(0)), 5),
    Direction.LEFT -> Animation(Vector(idleSpriteFrames(1)(0)), 5),
    Direction.RIGHT -> Animation(Vector(idleSpriteFrames(2)(0)), 5),
    Direction.UP -> Animation(Vector(idleSpriteFrames(3)(0)), 5),
  )

  var runAnimations = Map(
    Direction.UP -> Animation(
      Tools.getFrames(walkSpriteFrames, 3, 4), 5),
    Direction.LEFT -> Animation(
      Tools.getFrames(walkSpriteFrames, 1, 4), 5),
    Direction.DOWN -> Animation(
      Tools.getFrames(walkSpriteFrames, 0, 4), 5),
    Direction.RIGHT -> Animation(
      Tools.getFrames(walkSpriteFrames, 2, 4), 5),
  )

  var attackAnimations = Map(
    Direction.UP -> Animation(
      Tools.getFrames(attackSpriteFrames, 3, 6), 5, 1, 5),
    Direction.LEFT -> Animation(
      Tools.getFrames(attackSpriteFrames, 2, 6), 5, 1, 5),
    Direction.DOWN -> Animation(
      Tools.getFrames(attackSpriteFrames, 0, 6), 5, 1, 5),
    Direction.RIGHT -> Animation(
      Tools.getFrames(attackSpriteFrames, 1, 6), 5, 1, 5),
  )

//
  override def attackHitBox: Rectangle = direction match
    case Direction.UP => Rectangle(pos._1 + (areaHitBox.x) , pos._2 + areaHitBox.y - attackArea.height, attackArea.width, attackArea.height)
    case Direction.DOWN => Rectangle(pos._1 + (areaHitBox.x), pos._2 + areaHitBox.y + attackArea.height, attackArea.width, attackArea.height)
    case Direction.LEFT => Rectangle(pos._1 + areaHitBox.x - attackArea.width, pos._2 + areaHitBox.y , attackArea.width, attackArea.height)
    case Direction.RIGHT => Rectangle(pos._1 + (areaHitBox.width / 2) + attackArea.width, pos._2 + areaHitBox.y, attackArea.width, attackArea.height)
    case Direction.ANY => Rectangle(0,0,0,0)

  currentAnimation = idleAnimations(this.direction)


