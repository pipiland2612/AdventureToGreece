package Enemy

import `object`.ObjectType.{OBJ_BronzeCoin, OBJ_GoldCoin, OBJ_ManaFlask, OBJ_NormalHealFlask}
import entities.Direction
import game.GamePanel
import items.Item
import utils.{Animation, Tools}

import java.awt.Rectangle
import java.awt.image.BufferedImage

abstract class EN_Orc (gp: GamePanel) extends Enemy(gp):

  attackArea.width = (gp.tileSize / 1.5).toInt
  attackArea.height = (gp.tileSize / 1.5).toInt
  var verticalScanRange: Int = gp.tileSize
  var horizontalScanRange: Int = gp.tileSize

  val spriteFrames: Array[Array[BufferedImage]]
  val attackFrames: Array[Array[BufferedImage]]

  var frameSize = 64
  var scale: Int = (gp.tileSize * 1.5).toInt

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

  override def attackHitBox: Rectangle = direction match
    case Direction.UP => Rectangle(pos._1 + areaHitBox.x , pos._2 + areaHitBox.y - attackArea.height, attackArea.width, attackArea.height)
    case Direction.DOWN => Rectangle(pos._1 + areaHitBox.x, pos._2 + areaHitBox.y + (attackArea.height * 1.5).toInt, attackArea.width, attackArea.height)
    case Direction.LEFT => Rectangle(pos._1 + areaHitBox.x - attackArea.width, pos._2 + (areaHitBox.y * 3/2), attackArea.width, attackArea.height)
    case Direction.RIGHT => Rectangle(pos._1 + areaHitBox.x + attackArea.width, pos._2 + (areaHitBox.y * 3/2), attackArea.width, attackArea.height)
end EN_Orc


class EN_Orc_Light(gp: GamePanel) extends EN_Orc(gp) :

  var pos = (0,0)
  var name = "Light Orc"
  speed = 2
  maxHealth = 25
  health = maxHealth
  damagePower = 10
  var defense = 5
  maxInvincDuration = 15
  var expGet: Int = 5
  var itemDropped: Vector[Item] = Vector(new OBJ_BronzeCoin(gp), new OBJ_NormalHealFlask(gp), new OBJ_ManaFlask(gp))

  var attackRate = 20
  var changeDirectionInterval = 60
  attackTimeAnimation = 30

  val spriteFrames = Tools.loadFrames("Enemy/Orc/light_orc", frameSize, frameSize, scale, scale, 12)
  val attackFrames = Tools.loadFrames("Enemy/Orc/light_orc_attack", frameSize, frameSize, scale, scale, 4)

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

end EN_Orc_Light

class EN_Orc_Heavy(gp: GamePanel) extends EN_Orc(gp) :

  var pos = (0,0)
  var name = "Heavy Orc"
  speed = 2
  maxHealth = 30
  health = maxHealth
  damagePower = 15
  var defense = 7
  maxInvincDuration = 15
  var expGet: Int = 7
  var itemDropped: Vector[Item] = Vector(new OBJ_GoldCoin(gp))

  var attackRate = 20
  var changeDirectionInterval = 60
  attackTimeAnimation = 30

  val spriteFrames = Tools.loadFrames("Enemy/Orc/heavy_orc", frameSize, frameSize, scale, scale, 12)
  val attackFrames = Tools.loadFrames("Enemy/Orc/heavy_orc_attack", frameSize, frameSize, scale, scale, 4)

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

end EN_Orc_Heavy
