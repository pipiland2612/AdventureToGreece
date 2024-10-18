package Enemy

import `object`.ObjectType.OBJ_BronzeCoin
import entities.{Direction, State}
import game.GamePanel
import items.Item
import utils.Tools
import utils.Animation

import java.awt.Rectangle
class EN_Necromancer(gp : GamePanel) extends Enemy(gp) :
  // stats
  var pos = (0,0)
  var name = "Necromancer"
  speed = 1
  maxHealth = 40
  health = maxHealth
  damagePower = 10
  var defense = 5
  maxInvincDuration = 30
  var expGet: Int = 5
  var itemDropped: Vector[Item] = Vector(new OBJ_BronzeCoin(gp))

  var attackRate = 30
  var verticalScanRange: Int = (gp.tileSize * 1.5).toInt
  var horizontalScanRange: Int = (gp.tileSize * 1.25).toInt
  attackTimeAnimation = 45
  attackArea.width = (gp.tileSize * 1.5).toInt
  attackArea.height = (gp.tileSize * 1.5).toInt

  // world stats
  var solidAreaWidth = 9 * 2
  var solidAreaHeight = 12 * 2
  var solidAreaX = 65
  var solidAreaY = 107
  solidAreaDefaultX = solidAreaX
  solidAreaDefaultY = solidAreaY
  var solidArea = Rectangle(solidAreaX , solidAreaY, solidAreaWidth , solidAreaHeight)

  areaDefaultX = 62
  areaDefaultY = 55
  var areaHitBoxWidth = solidAreaWidth + 5
  var areaHitBoxHeight = solidAreaHeight * 3

  areaHitBox = Rectangle(areaDefaultX , areaDefaultY, areaHitBoxWidth, areaHitBoxHeight)

  private val frameSize = 64
  private val walkSpriteFrames = Tools.loadFrames("Enemy/Necromancer/Necromancer_walk", frameSize, frameSize, gp.tileSize * 3,gp.tileSize * 3, 1)
  private val idleSpriteFrames = Tools.loadFrames("Enemy/Necromancer/Necromancer_Idle", frameSize, frameSize, gp.tileSize * 3, gp.tileSize * 3, 1)
  private val dieSpriteFrames = Tools.loadFrames("Enemy/Necromancer/Necromancer_die", frameSize, frameSize, gp.tileSize * 3,gp.tileSize * 3, 1)
  private val spawnSpriteFrames = Tools.loadFrames("Enemy/Necromancer/Necromancer_spawn", frameSize, frameSize, gp.tileSize * 3, gp.tileSize * 3, 1)
  private val attackSpriteFrames = Tools.loadFrames("Enemy/Necromancer/Necromancer_attack", frameSize, frameSize, gp.tileSize * 3, gp.tileSize * 3, 1)

  val commonIdleFrames = Tools.getFrames(idleSpriteFrames, 0, 1)
  val commonRunFrames = Tools.getFrames(walkSpriteFrames, 0, 9)
  val commonDeadFrames = Tools.getFrames(dieSpriteFrames, 0, 55)
  val commonSpawnFrames = Tools.getFrames(spawnSpriteFrames, 0, 25)
  val commonAttackFrames = Tools.getFrames(attackSpriteFrames, 0, 50)

  var idleAnimations = Tools.setupCommonAnimations(Tools.flipImageHorizontally, commonIdleFrames, 1)
  var runAnimations = Tools.setupCommonAnimations(Tools.flipImageHorizontally, commonRunFrames, 10)
  var deadAnimations = Tools.setupCommonAnimations(Tools.flipImageHorizontally, commonDeadFrames, 4)
  var spawnAnimations = Tools.setupCommonAnimations(Tools.flipImageHorizontally, commonSpawnFrames, 7)
  var attackAnimations = Tools.setupCommonAnimations(Tools.flipImageHorizontally, commonAttackFrames, 3, 25, 35)

  override def images: Map[(Direction, State), Animation] =
    super.images ++ Map (
      (Direction.UP, State.SPAWN) -> spawnAnimations(Direction.UP),
      (Direction.DOWN, State.SPAWN) -> spawnAnimations(Direction.DOWN),
      (Direction.LEFT, State.SPAWN) -> spawnAnimations(Direction.LEFT),
      (Direction.RIGHT, State.SPAWN) -> spawnAnimations(Direction.RIGHT)
    )

  override def attackHitBox: Rectangle = direction match
    case Direction.UP => Rectangle(pos._1 + (areaHitBox.x / 1.5).toInt , pos._2 + areaHitBox.y - attackArea.height, attackArea.width, attackArea.height)
    case Direction.DOWN => Rectangle(pos._1 + (areaHitBox.x / 1.5).toInt, pos._2 + areaHitBox.y + attackArea.height, attackArea.width, attackArea.height)
    case Direction.LEFT => Rectangle(pos._1 + areaHitBox.x - attackArea.width, pos._2 + areaHitBox.y , attackArea.width, attackArea.height)
    case Direction.RIGHT => Rectangle(pos._1 + (areaHitBox.width / 2) + attackArea.width, pos._2 + areaHitBox.y, attackArea.width, attackArea.height)

  currentAnimation = idleAnimations(this.direction)


