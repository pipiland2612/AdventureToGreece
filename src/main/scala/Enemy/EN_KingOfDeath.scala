package Enemy

import `object`.ObjectType.OBJ_BronzeCoin
import entities.{Direction, State}
import game.GamePanel
import items.Item
import utils.{Animation, Tools}

import java.awt.Rectangle
import java.awt.image.BufferedImage

class EN_KingOfDeath (gp: GamePanel) extends Enemy(gp) :

  var pos = (0,0)
  var name = "King Of Death"
  speed = 4
  maxHealth = 50
  health = maxHealth
  damagePower = 25
  var defense = 20
  maxInvincDuration = 15
  var expGet: Int = 30
  var itemDropped: Vector[Item] = Vector(new OBJ_BronzeCoin(gp))

  var attackRate = 10
  var changeDirectionInterval = 45
  attackTimeAnimation = 25

  attackArea.width = gp.tileSize * 2
  attackArea.height = gp.tileSize * 2
  var verticalScanRange: Int = gp.tileSize * 3
  var horizontalScanRange: Int = gp.tileSize * 3

  var frameSize = 64
  var scale: Int = 64 * 2

  // world stats
  var solidAreaWidth = scale / 4
  var solidAreaHeight = scale / 4
  var solidAreaX = 45
  var solidAreaY = scale - gp.tileSize
  solidAreaDefaultX = solidAreaX
  solidAreaDefaultY = solidAreaY
  var solidArea = Rectangle(solidAreaX , solidAreaY, solidAreaWidth , solidAreaHeight)

  areaDefaultX = 31
  areaDefaultY = 17
  var areaHitBoxWidth = scale / 2
  var areaHitBoxHeight = scale - (gp.tileSize / 2)

  areaHitBox = Rectangle(areaDefaultX , areaDefaultY, areaHitBoxWidth, areaHitBoxHeight)

  val spriteFrames = Tools.loadFrames("Enemy/KingOfDeath/king_death", frameSize, frameSize, scale, scale, 12)
  val attackFrames = Tools.loadFrames("Enemy/KingOfDeath/king_death_attack", frameSize * 2, (frameSize * 1.5).toInt, scale * 2, (scale * 1.5).toInt, 4)

  var idleAnimations = Map(
    Direction.UP -> Animation(Vector(spriteFrames(0)(0)), 1),
    Direction.LEFT -> Animation(Vector(spriteFrames(1)(0)), 1),
    Direction.DOWN -> Animation(Vector(spriteFrames(2)(0)), 1),
    Direction.RIGHT -> Animation(Vector(spriteFrames(3)(0)), 1),
  )

  var runAnimations = Map(
    Direction.UP -> Animation(
      Tools.getFrames(spriteFrames, 8, 9), 10),
    Direction.LEFT -> Animation(
      Tools.getFrames(spriteFrames, 9, 9), 10),
    Direction.DOWN -> Animation(
      Tools.getFrames(spriteFrames, 10, 9), 10),
    Direction.RIGHT -> Animation(
      Tools.getFrames(spriteFrames, 11, 9), 10),
  )

  var attackAnimations = Map(
    Direction.UP -> Animation(
      Tools.getFrames(attackFrames, 0, 6), 5, 0, 5),
    Direction.LEFT -> Animation(
      Tools.getFrames(attackFrames, 1, 6), 5, 0, 5),
    Direction.DOWN -> Animation(
      Tools.getFrames(attackFrames, 2, 6), 5, 0, 5),
    Direction.RIGHT -> Animation(
      Tools.getFrames(attackFrames, 3, 6), 5, 0, 5),
  )
  needsAnimationUpdate = false
  currentAnimation = images((direction, state))

  //For Finding the center of character
  imageWidthCenter = idleAnimations(Direction.UP).getCurrentFrame.getWidth / 4
  imageHeightCenter = idleAnimations(Direction.UP).getCurrentFrame.getHeight / 4

  //offset the difference between Attack and Other animations
  offsetX = idleAnimations(Direction.UP).getCurrentFrame.getWidth / 2
  offsetY = idleAnimations(Direction.UP).getCurrentFrame.getHeight / 2

  override def attackHitBox: Rectangle = direction match
    case Direction.UP => Rectangle(pos._1 + areaHitBox.x / 2 , pos._2 + (areaHitBox.y * 1.5).toInt - attackArea.height, attackArea.width, attackArea.height)
    case Direction.DOWN => Rectangle(pos._1 + areaHitBox.x / 2, pos._2 + (attackArea.height), attackArea.width, attackArea.height)
    case Direction.LEFT => Rectangle(pos._1 + areaHitBox.x - attackArea.width, pos._2 + (areaHitBox.y * 3/2), attackArea.width, attackArea.height)
    case Direction.RIGHT => Rectangle(pos._1 + areaHitBox.x + attackArea.width / 2, pos._2 + (areaHitBox.y / 1.5).toInt , attackArea.width, attackArea.height)

  override def setAction(): Unit =
    if getTileDistance(gp.player) < 10 then
      moveTowardPlayer(60)
    else
      setRandomDirection(changeDirectionInterval)

    if state != State.ATTACK then checkToAttack(attackRate, verticalScanRange, horizontalScanRange)

end EN_KingOfDeath



