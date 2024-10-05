package entities

import Enemy.Enemy
import entities.Direction.ANY
import game.GamePanel
import utils.Animation

import java.awt.{Graphics2D, Rectangle}

abstract class Creatures(gp: GamePanel) extends Entity(gp) :
  var state: State = State.IDLE

  var dialoques = new Array[String](10)
  var dialoqueIndex = 0

  var needsAnimationUpdate = false

  var health: Int = 0
  var maxHealth: Int = 0
  var isAttacking = false
  var maxMana: Int = 0
  var mana: Int = 0

  var isInvinc : Boolean = false
  var invincibleDuration = 0
  var maxInvincDuration: Int = 0

  var dying = false
  var dyingCounter = 0
  var shootCounter = 0

  var idleAnimations: Map[Direction, Animation]
  var runAnimations: Map[Direction, Animation]
  var attackAnimations: Map[Direction, Animation]
  var deadAnimations: Map[Direction, Animation]

  def images: Map[(Direction, State), Animation] =
    if idleAnimations.nonEmpty || runAnimations.nonEmpty || attackAnimations.nonEmpty then
      Map(
        (Direction.RIGHT, State.IDLE) -> idleAnimations(Direction.RIGHT),
        (Direction.DOWN, State.IDLE) -> idleAnimations(Direction.DOWN),
        (Direction.LEFT, State.IDLE) -> idleAnimations(Direction.LEFT),
        (Direction.UP, State.IDLE) -> idleAnimations(Direction.UP),

        (Direction.RIGHT, State.RUN) -> runAnimations(Direction.RIGHT),
        (Direction.DOWN, State.RUN) -> runAnimations(Direction.DOWN),
        (Direction.LEFT, State.RUN) -> runAnimations(Direction.LEFT),
        (Direction.UP, State.RUN) -> runAnimations(Direction.UP),

        (Direction.UP, State.ATTACK) -> attackAnimations(Direction.UP),
        (Direction.DOWN, State.ATTACK) -> attackAnimations(Direction.DOWN),
        (Direction.LEFT, State.ATTACK) -> attackAnimations(Direction.LEFT),
        (Direction.RIGHT, State.ATTACK) -> attackAnimations(Direction.RIGHT),

        (Direction.UP, State.DEAD) -> deadAnimations(Direction.UP),
        (Direction.DOWN, State.DEAD) -> deadAnimations(Direction.DOWN),
        (Direction.LEFT, State.DEAD) -> deadAnimations(Direction.LEFT),
        (Direction.RIGHT, State.DEAD) -> deadAnimations(Direction.RIGHT),
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
    if this.health < 0 then
      die()

  def isAlive: Boolean = health > 0
  def isDead: Boolean = !isAlive
  
  def usePotion(creatures: Creatures): Unit = {}
  def setAction(): Unit = {}

  def move(dx: Int, dy: Int): Unit =
    state = State.RUN
    this.pos = (pos._1 + dx, pos._2 + dy)
    needsAnimationUpdate = true

  def handleInvincibility(): Unit =
    if isInvinc then
      invincibleDuration += 1
      if invincibleDuration > maxInvincDuration then
        invincibleDuration = 0
        isInvinc = false

  def die(): Unit =
    if (state != State.DEAD) then
      state = State.DEAD

    needsAnimationUpdate = true
    checkAnimationUpdate()

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

  override def draw(g: Graphics2D): Unit = super.draw(g)
