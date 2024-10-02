package entities

import Enemy.Enemy
import entities.Direction.ANY
import game.GamePanel
import utils.Animation

abstract class Creatures(gp: GamePanel) extends Entity(gp) :
  var speed: Int = 0
  var state: State = State.IDLE
  var direction: Direction = Direction.DOWN
  var isCollided: Boolean = false
  var dialoques = new Array[String](10)
  var dialoqueIndex = 0
  var needsAnimationUpdate = false
  var health: Int = 0
  var maxHealth: Int = 0
  var isInvinc : Boolean = false
  var invincibleDuration = 0

  var idleAnimations: Map[Direction, Animation]
  var runAnimations: Map[Direction, Animation]

  def images: Map[(Direction, State), Animation] =
    if idleAnimations != null || runAnimations != null then
      Map(
        (Direction.RIGHT, State.IDLE) -> idleAnimations(Direction.RIGHT),
        (Direction.RIGHT, State.RUN) -> runAnimations(Direction.RIGHT),
        (Direction.DOWN, State.IDLE) -> idleAnimations(Direction.DOWN),
        (Direction.DOWN, State.RUN) -> runAnimations(Direction.DOWN),
        (Direction.LEFT, State.IDLE) -> idleAnimations(Direction.LEFT),
        (Direction.LEFT, State.RUN) -> runAnimations(Direction.LEFT),
        (Direction.UP, State.IDLE) -> idleAnimations(Direction.UP),
        (Direction.UP, State.RUN) -> runAnimations(Direction.UP)
      )
    else null

  def speak (): Unit =
    if dialoques(dialoqueIndex) == null then
      dialoqueIndex = 0
    gp.gui.currentDialogue = dialoques(dialoqueIndex)
    dialoqueIndex += 1

    this.direction = this.direction match
      case Direction.UP => Direction.DOWN
      case Direction.DOWN => Direction.UP
      case Direction.LEFT => Direction.RIGHT
      case Direction.RIGHT => Direction.LEFT
      case Direction.ANY => null

  def takeDamage(amount: Int): Unit =
    this.health -= amount

  def isAlive: Boolean = health > 0

  def setAction(): Unit = {}

  def move(dx: Int, dy: Int): Unit =
    state = State.RUN
    this.pos = (pos._1 + dx, pos._2 + dy)
    needsAnimationUpdate = true

  def checkAnimationUpdate (): Unit =
    if(needsAnimationUpdate) then
      currentAnimation = images.getOrElse((direction, state), images((direction, State.IDLE)))
      needsAnimationUpdate = false
    currentAnimation.update()

  def continueMove (): Unit =
    if !isCollided then
      direction match
        case Direction.UP => this.move(0, -this.speed)
        case Direction.DOWN => this.move(0, this.speed)
        case Direction.LEFT => this.move(-this.speed, 0)
        case Direction.RIGHT => this.move(this.speed, 0)
        case ANY =>
    else
      currentAnimation = images.getOrElse((direction, state), images((direction, State.IDLE)))

  def update(): Unit =
    setAction()
    isCollided = false
    gp.cCheck.checkTileCollision(this)
    gp.cCheck.checkObjectCollision(this, false)
//    gp.cCheck.checkCollisionWithTargets(this, gp.npc)
    gp.cCheck.checkCollisionWithTargets(this, gp.enemyList)
    val hasTouchedPlayer = gp.cCheck.checkPlayer(this)

    if hasTouchedPlayer && this.isInstanceOf[Enemy] then
      if !gp.player.isInvinc then
        gp.player.health -= 10
        gp.player.isInvinc = true
    checkAnimationUpdate()
    continueMove()


