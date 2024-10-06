package Enemy

import `object`.OBJ_BronzeCoin
import entities.{Direction, Player, State}
import game.GamePanel
import items.Item
import utils.Tools
import utils.Animation

import java.awt.Rectangle
class EN_Necromancer(gp : GamePanel, var pos: (Int, Int)) extends Enemy(gp) :
  // stats
  var name = "Necromancer"
  speed = 1
  maxHealth = 40
  health = maxHealth
  var attackPower: Int = 10
  var defense = 5
  maxInvincDuration = 30
  var expGet: Int = 5
  var itemDropped: Vector[Item] = Vector(new OBJ_BronzeCoin(gp))

  // world stats
  var solidAreaX = 70
  var solidAreaY = 100
  solidAreaDefaultX = solidAreaX
  solidAreaDefaultY = solidAreaY
  var solidAreaWidth = (12)
  var solidAreaHeight = (12)
  var solidArea = Rectangle(solidAreaX + gp.tileSize * 10 , solidAreaY + gp.tileSize * 15, (solidAreaWidth * 2), (solidAreaHeight * 2))

  areaDefaultX = 70
  areaDefaultY = 55
  areaHitBox = Rectangle(70 , 55, (solidAreaWidth * 2), (solidAreaHeight * 6))

  private val frameSize = 64
  private val walkSpriteFrames = Tools.loadFrames("Enemy/Necromancer/Necromancer_walk", frameSize, frameSize, gp.tileSize * 3, 1)
  private val idleSpriteFrames = Tools.loadFrames("Enemy/Necromancer/Necromancer_Idle", frameSize, frameSize, gp.tileSize * 3, 1)
  private val dieSpriteFrames = Tools.loadFrames("Enemy/Necromancer/Necromancer_die", frameSize, frameSize, gp.tileSize * 3, 1)
  private val spawnSpriteFrames = Tools.loadFrames("Enemy/Necromancer/Necromancer_spawn", frameSize, frameSize, gp.tileSize * 3, 1 )

  val commonRunFrames = Tools.getFrames(walkSpriteFrames, 0, 9)
  val commonDeadFrames = Tools.getFrames(dieSpriteFrames, 0, 9)
  val commonSpawnFrames = Tools.getFrames(spawnSpriteFrames, 0, 100)
  val commonIdleFrames = Tools.getFrames(idleSpriteFrames, 0, 1)

  var idleAnimations = Tools.setupCommonAnimations(Tools.flipImageHorizontally, commonIdleFrames, 1)
  var runAnimations = Tools.setupCommonAnimations(Tools.flipImageHorizontally, commonRunFrames, 10)
  var attackAnimations = Tools.setupCommonAnimations(Tools.flipImageHorizontally, commonIdleFrames, 1)
  var deadAnimations = Tools.setupCommonAnimations(Tools.flipImageHorizontally, commonDeadFrames, 15)
  var spawnAnimations = Tools.setupCommonAnimations(Tools.flipImageHorizontally, commonSpawnFrames, 15)

  override def images: Map[(Direction, State), Animation] =
    super.images ++ Map (
      (Direction.UP, State.SPAWN) -> spawnAnimations(Direction.UP),
      (Direction.DOWN, State.SPAWN) -> spawnAnimations(Direction.DOWN),
      (Direction.LEFT, State.SPAWN) -> spawnAnimations(Direction.LEFT),
      (Direction.RIGHT, State.SPAWN) -> spawnAnimations(Direction.RIGHT)
    )

  currentAnimation = idleAnimations(this.direction)

  def attackPlayer(player: Player): Unit = {}
  def moveTowardsPlayer(player: Player): Unit = {}

