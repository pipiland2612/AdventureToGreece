package Enemy

import entities.{Direction, Player, State}
import game.GamePanel
import utils.Tools
import utils.Animation

import java.awt.Rectangle
import java.awt.image.BufferedImage

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

  // world stats
  var solidAreaX = 70
  var solidAreaY = 100
  solidAreaDefaultX = solidAreaX
  solidAreaDefaultY = solidAreaY
  var solidAreaWidth = (12)
  var solidAreaHeight = (12)
  var solidArea = Rectangle(solidAreaX + gp.tileSize * 10 , solidAreaY + gp.tileSize * 15, (solidAreaWidth * 2).toInt, (solidAreaHeight * 2).toInt)

  private val frameSize = 64
  private val walkSpriteFrames = Tools.loadFrames("Enemy/Necromancer/Necromancer_walk", frameSize, gp.tileSize * 3, 1)
  private val idleSpriteFrames = Tools.loadFrames("Enemy/Necromancer/Necromancer_Idle", frameSize, gp.tileSize * 3, 1)
  private val dieSpriteFrames = Tools.loadFrames("Enemy/Necromancer/Necromancer_die", frameSize, gp.tileSize * 3, 1)
  private val spawnSpriteFrames = Tools.loadFrames("Enemy/Necromancer/Necromancer_spawn", frameSize, gp.tileSize * 3, 1 )

  val commonRunFrames = Tools.getFrames(walkSpriteFrames, 0, 9)
  val commonDeadFrames = Tools.getFrames(dieSpriteFrames, 0, 9)
  val commonSpawnFrames = Tools.getFrames(spawnSpriteFrames, 0, 100)
  val commonIdleFrames = Tools.getFrames(idleSpriteFrames, 0, 1)

  var idleAnimations = setupCommonAnimations(commonIdleFrames, 1)
  var runAnimations = setupCommonAnimations(commonRunFrames, 10)
  var attackAnimations = setupCommonAnimations(commonIdleFrames, 1)
  var deadAnimations = setupCommonAnimations(commonDeadFrames, 15)
  var spawnAnimations = setupCommonAnimations(commonSpawnFrames, 15)

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

  // Helper methods
  def flipFrames(frames: Vector[BufferedImage]): Vector[BufferedImage] =
   frames.map(Tools.flipImageHorizontally)

  def setupCommonAnimations (commonFrames :Vector[BufferedImage], framesRate: Int): Map[Direction, Animation] = Map (
    Direction.UP -> Animation(commonFrames, framesRate),
    Direction.RIGHT -> Animation(commonFrames, framesRate),
    Direction.LEFT -> Animation(flipFrames(commonFrames), framesRate),
    Direction.DOWN -> Animation(flipFrames(commonFrames), framesRate)
  )

