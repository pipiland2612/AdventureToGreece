package Enemy

import entities.{Direction, Player, State}
import game.GamePanel
import utils.Tools
import utils.Animation

import java.awt.Rectangle
import java.awt.image.BufferedImage

class EN_Necromancer(gp : GamePanel, var pos: (Int, Int)) extends Enemy(gp) :
  name = "Necromancer"
  speed = 1
  maxHealth = 20
  health = maxHealth
  var attackPower: Int = 10
  maxInvincDuration = 30

  var solidAreaX = 70
  var solidAreaY = 100
  solidAreaDefaultX = solidAreaX
  solidAreaDefaultY = solidAreaY
  var solidAreaWidth = (12)
  var solidAreaHeight = (12)
  var solidArea = Rectangle(solidAreaX + gp.tileSize * 10 , solidAreaY + gp.tileSize * 15, (solidAreaWidth * 2).toInt, (solidAreaHeight * 2).toInt)

  private val frameSize = 64
  private val walkSpriteFrames = Tools.loadFrames("Enemy/Necromancer_walk", frameSize, gp.tileSize * 3, 1)
  private val idleSpriteFrames = Tools.loadFrames("Enemy/Necromancer_Idle", frameSize, gp.tileSize * 3, 1)
  private val dieSpriteFrames = Tools.loadFrames("Enemy/Necromancer_die", 64, gp.tileSize * 3, 1)

  val commonFrames = Tools.getFrames(walkSpriteFrames, 0, 9)
  val commonDeadFrames = Tools.getFrames(dieSpriteFrames, 0, 9)

  var idleAnimations = Map(
    Direction.UP -> Animation(Vector(idleSpriteFrames(0)(0)), 1),
    Direction.RIGHT -> Animation(Vector(idleSpriteFrames(0)(0)), 1),
    Direction.LEFT -> Animation(Vector(Tools.flipImageHorizontally(idleSpriteFrames(0)(0))), 1),
    Direction.DOWN -> Animation(Vector(Tools.flipImageHorizontally(idleSpriteFrames(0)(0))), 1)
  )

  var runAnimations = Map(
    Direction.UP -> Animation(commonFrames, 10),
    Direction.RIGHT -> Animation(commonFrames, 10),
    Direction.LEFT -> Animation(flipFrames(commonFrames), 10),
    Direction.DOWN -> Animation(flipFrames(commonFrames), 10)
  )
  var attackAnimations =  Map(
    Direction.UP -> Animation(commonFrames, 10),
    Direction.RIGHT -> Animation(commonFrames, 10),
    Direction.LEFT -> Animation(flipFrames(commonFrames), 10),
    Direction.DOWN -> Animation(flipFrames(commonFrames), 10)
  )
  var deadAnimations = Map(
    Direction.UP -> Animation(commonDeadFrames, 15),
    Direction.RIGHT -> Animation(commonDeadFrames, 15),
    Direction.LEFT -> Animation(flipFrames(commonDeadFrames), 15),
    Direction.DOWN -> Animation(flipFrames(commonDeadFrames), 15)
  )

  currentAnimation = idleAnimations(this.direction)

  def flipFrames(frames: Vector[BufferedImage]): Vector[BufferedImage] =
   frames.map(Tools.flipImageHorizontally)

  def attackPlayer(player: Player): Unit ={}
  def moveTowardsPlayer(player: Player): Unit = {}
end EN_Necromancer


