package entities
import entities.Direction.{ANY, DOWN, LEFT, RIGHT, UP}
import entities.State.DEAD

import java.awt.image.BufferedImage
import game.{GamePanel, GameState}
import items.Weapon
import utils.{Animation, Tools}

import java.awt.{AlphaComposite, Graphics2D, Rectangle}

class Player(var pos: (Int, Int), gp: GamePanel) extends Creatures(gp):

  private var currentWeapon: Option[Weapon] = None
  state = State.IDLE
  private var counter = 0
  direction = Direction.RIGHT
  private var attackCooldown: Int = 0
  private val maxAttackCooldown: Int = 45
  maxInvincDuration = 60

  val screenX: Int = gp.screenWidth / 2 - (gp.tileSize / 2)
  val screenY: Int = gp.screenHeight / 2 - (gp.tileSize / 2)
  private val playerScale = (gp.tileSize * 1.25).toInt
  private var isMoving = false

  private val frameSize = 64
  private val spriteFrames = Tools.loadFrames("Players/Player_spritesheet", frameSize, playerScale, 21)
  private val attackFrames = Tools.loadFrames("Players/attack_spritesheet", frameSize, playerScale, 4)
  var idleAnimations = Map(
    Direction.UP -> Animation(Vector(spriteFrames(0)(0)), 1),
    Direction.LEFT -> Animation(Vector(spriteFrames(1)(0)), 1),
    Direction.DOWN -> Animation(Vector(spriteFrames(2)(0)), 1),
    Direction.RIGHT -> Animation(Vector(spriteFrames(3)(0)), 5),
  )

  var runAnimations = Map(
    Direction.UP -> Animation(
      Tools.getFrames(spriteFrames, 8, 8), 10),
    Direction.LEFT -> Animation(
      Tools.getFrames(spriteFrames, 9, 8), 10),
    Direction.DOWN -> Animation(
      Tools.getFrames(spriteFrames, 10, 8), 10),
    Direction.RIGHT -> Animation(
      Tools.getFrames(spriteFrames, 11, 8), 10),
  )

  var attackAnimations = Map(
    Direction.UP -> Animation(
      Tools.getFrames(attackFrames, 3, 6), 5),
    Direction.LEFT -> Animation(
      Tools.getFrames(attackFrames, 1, 6), 5),
    Direction.DOWN -> Animation(
      Tools.getFrames(attackFrames, 0, 6), 5),
    Direction.RIGHT -> Animation(
      Tools.getFrames(attackFrames, 2, 6), 5),
  )

  var deadAnimations = Map (
    Direction.UP -> Animation(
      Tools.getFrames(spriteFrames, 20, 6), 5),
    Direction.LEFT -> Animation(
      Tools.getFrames(spriteFrames, 20, 6), 5),
    Direction.DOWN -> Animation(
      Tools.getFrames(spriteFrames, 20, 6), 5),
    Direction.RIGHT -> Animation(
      Tools.getFrames(spriteFrames, 20, 6), 5),
  )

  def backGroundImage: BufferedImage = idleAnimations(Direction.DOWN).getCurrentFrame

  needsAnimationUpdate = false
  currentAnimation = images((direction, state))

  var solidAreaX = 22
  var solidAreaY = 40
  solidAreaDefaultX = solidAreaX
  solidAreaDefaultY = solidAreaY
  var solidAreaWidth = (12)
  var solidAreaHeight = (12)

  attackArea.width = 32
  attackArea.height = 16

  var solidArea = Rectangle(solidAreaX , solidAreaY , (solidAreaWidth * 1.25).toInt, (solidAreaHeight * 1.25).toInt)
  speed = 5
  maxHealth = 100
  health = maxHealth

  def getState = this.state
  def getHealth = this.health
  override def getPosition = this.pos
  def getSpeed = this.speed
  def getCurrentAnimation = this.currentAnimation

  def stop(): Unit =
    state = State.IDLE
    needsAnimationUpdate = true

  def jump(): Unit =
    state = State.JUMP
    val newY = pos._1 + -3
    this.pos = (pos._1, newY)
    needsAnimationUpdate = true

  def attack(): Unit =
    if state != State.ATTACK && attackCooldown == 0 then
      updateDirection()
      // save current world x,y
      var currentWorldX = pos._1
      var currentWorldy = pos._2
      var solidAreaWidth = this.solidArea.width
      var solidAreaHeight = this.solidArea.height
      // adjust player's worlds x,y
      direction match
        case UP => var newPosY = pos._2 - attackArea.height;  this.pos = (pos._1, newPosY)
        case DOWN => var newPosY = pos._2 + attackArea.height;  this.pos = (pos._1, newPosY)
        case LEFT => var newPosX = pos._1 - attackArea.width;  this.pos = (newPosX, pos._2)
        case RIGHT => var newPosX = pos._1 + attackArea.height;  this.pos = (newPosX, pos._2)
        case ANY =>
      // attackAreaBecome solid Area
      solidArea.width = attackArea.width
      solidArea.height = attackArea.height
      var enemyIndex = gp.cCheck.checkCollisionWithTargets(this, gp.enemyList)
      attackEnemy(enemyIndex)

      this.pos = (currentWorldX, currentWorldy)
      solidArea.width = solidAreaWidth
      solidArea.height = solidAreaHeight

      state = State.ATTACK
      needsAnimationUpdate = true
      counter = 0
      attackCooldown = maxAttackCooldown

  def equipWeapon(weapon: Weapon): Unit =
    currentWeapon = Some(weapon)

  override def die(): Unit =
    super.die()
    dyingCounter += 1
    if dyingCounter > 120 then
      println("Death animation completed. Triggering game over.")
      gp.gui.isFinished = true

  // -----------------------------------------------
  // Rendering methods

  // call by the game loop
  override def update(): Unit =
    handleInvincibility()
    if attackCooldown > 0 then
      attackCooldown -= 1

//    if (!isDead) then
//      state = State.DEAD

    state match
      case State.ATTACK => handleAttackState()
//      case State.DEAD => die()
      case _ => handleInput()

  override def draw(g: Graphics2D): Unit =
    if isInvinc then
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f))
    currentAnimation.getCurrentFrame match
      case frame =>
          g.drawImage(frame,
            (this.screenX),
            (this.screenY),
            null)
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))
  // ------------------------------

  def pickUpObject(index : Int): Unit =
    if(index != -1) then
      {}
  def interactNPC(index : Int): Unit =
    if(index != -1) then
      gp.gameState = GameState.DialogueState
  def enemyAttack(enemyIndex: Int): Unit =
    if enemyIndex != -1 then
      if !isInvinc then
        this.takeDamage(gp.enemyList(enemyIndex).attackPower)
        isInvinc = true
  def attackEnemy(enemyIndex: Int): Unit =
    if enemyIndex != -1 then
      var currentEnemy = gp.enemyList(enemyIndex)

      if !currentEnemy.isInvinc then
        currentEnemy.takeDamage(10)
        currentEnemy.isInvinc = true

        if !currentEnemy.isAlive then
          currentEnemy.dying = true

    else println("Miss")
// Helper function
  private def updateDirection(): Unit =
    if (gp.keyH.upPressed) then
      this.direction = Direction.UP
    else if (gp.keyH.downPressed) then
      this.direction = Direction.DOWN
    else if (gp.keyH.leftPressed) then
      this.direction = Direction.LEFT
    else if (gp.keyH.rightPressed) then
      this.direction = Direction.RIGHT

  private def handleAttackState(): Unit =
    checkAnimationUpdate()
    currentAnimation.update()
    counter += 1
    if (counter >= 30) then
      state = State.IDLE
      needsAnimationUpdate = true

  private def handleInput(): Unit =
    if (gp.keyH.attackPressed && attackCooldown == 0) then
      attack()
    else if gp.keyH.upPressed || gp.keyH.downPressed || gp.keyH.leftPressed || gp.keyH.rightPressed then
      updateDirection()
      checkAnimationUpdate()

      isCollided = false

      // CHECK TILE
      gp.cCheck.checkTileCollision(this)

      // CHECK OBJECT
      val objectIndex = gp.cCheck.checkObjectCollision(this, true)
//      pickUpObject(index)
//      val npcIndex = gp.cCheck.checkCollisionWithPlayer(this, gp.npc)
//      interact ???

      // CHECK EVENT
      val enemyIndex = gp.cCheck.checkCollisionWithTargets(this, gp.enemyList)
      enemyAttack(enemyIndex)

      gp.eHandler.checkEvent()

      if !isCollided then
        direction match
          case Direction.UP => this.move(0, -this.speed); isMoving = true
          case Direction.DOWN => this.move(0, this.speed); isMoving = true
          case Direction.LEFT => this.move(-this.speed, 0); isMoving = true
          case Direction.RIGHT => this.move(this.speed, 0); isMoving = true
          case Direction.ANY =>
      else
        currentAnimation = images.getOrElse((direction, state), images((direction, State.IDLE)))
      gp.keyH.attackPressed = false
    else
      if state != State.IDLE then
        state = State.IDLE
        isMoving = false
        needsAnimationUpdate = true