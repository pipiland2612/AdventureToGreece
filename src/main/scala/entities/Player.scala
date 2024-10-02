package entities

import java.awt.image.BufferedImage
import game.{GamePanel, GameState}
import items.Weapon
import utils.{Animation, SpriteSheet, Tools}

import java.awt.{Graphics2D, Rectangle}

class Player(var pos: (Int, Int), gp: GamePanel) extends Creatures(gp):

  private var currentWeapon: Option[Weapon] = None
  state = State.IDLE
  private var counter = 0
  direction = Direction.RIGHT

  val screenX: Int = gp.screenWidth / 2 - (gp.tileSize / 2)
  val screenY: Int = gp.screenHeight / 2 - (gp.tileSize / 2)

  private val frameSize = 64
  private val spriteFrames = Tools.loadFrames("Players/Player_spritesheet", frameSize, (gp.tileSize * 1.25).toInt, 100)
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

  def backGroundImage: BufferedImage = idleAnimations(Direction.DOWN).getCurrentFrame

  needsAnimationUpdate = false
  currentAnimation = images((direction, state))

  var solidAreaX = 22
  var solidAreaY = 40
  solidAreaDefaultX = solidAreaX
  solidAreaDefaultY = solidAreaY
  var solidAreaWidth = (12)
  var solidAreaHeight = (12)

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
    state = State.ATTACK
    currentWeapon.foreach(_.swing())
    needsAnimationUpdate = true

  def equipWeapon(weapon: Weapon): Unit =
    currentWeapon = Some(weapon)

//  override def die(): Unit =
//    state = State.DEAD
//    needsAnimationUpdate = true


  // -----------------------------------------------
  // Rendering methods

  // call by the game loop
  override def update(): Unit =
    if(gp.keyH.upPressed || gp.keyH.downPressed || gp.keyH.leftPressed || gp.keyH.rightPressed ) then
      if(gp.keyH.upPressed) then
        this.direction = Direction.UP
      if(gp.keyH.downPressed) then
        this.direction = Direction.DOWN
      if(gp.keyH.leftPressed) then
        this.direction = Direction.LEFT
      if(gp.keyH.rightPressed) then
        this.direction = Direction.RIGHT

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
      continueMove()

    // Outside if statement
    if isInvinc then
      invincibleDuration += 1
      if invincibleDuration > 60 then
        invincibleDuration = 0
        isInvinc = false

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

  // call by the game loop
  override def draw(g: Graphics2D): Unit =
    currentAnimation.getCurrentFrame match
      case frame =>
          g.drawImage(frame,
            (this.screenX),
            (this.screenY),
            null)
      case _ =>

