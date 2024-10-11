package entities
import `object`.ObjectType.{OBJ_BronzeCoin, OBJ_Chest, OBJ_Fireball, OBJ_NormalAxe, OBJ_NormalHealFlask, OBJ_NormalShield, OBJ_NormalSword, OBJ_SilverKey}
import entities.Direction.{ANY, DOWN, LEFT, RIGHT, UP}

import java.awt.image.BufferedImage
import game.{GamePanel, GameState}
import items.{Coin, Item, Potion, Projectile, Shield, Weapon}
import levels.Obstacle
import ui.PlayerUI
import utils.{Animation, Tools}

import java.awt.{AlphaComposite, Graphics2D, Rectangle}

class Player(var pos: (Int, Int), gp: GamePanel) extends Creatures(gp):
  direction = Direction.RIGHT
  currentWeapon = OBJ_NormalSword(gp)
  currentShield = OBJ_NormalShield(gp)
  currentProjectile = OBJ_Fireball(gp)

  setItems()

  state = State.IDLE
  private var counter = 0

  // Player stats
  var name = "Warrior"
  speed = 5
  maxHealth = 110
  health = maxHealth
  maxMana = 100
  mana = maxMana
  var strength = 1
  var dexterity = 1
  var exp = 0
  var level = 1
  var nextLevelExp = 5
  var attackDamage = getAttackDamage
  var defense = getDefense
  var coin = 20

  // other stats
  private var attackCooldown: Int = 0
  private val maxAttackCooldown: Int = 45
  maxInvincDuration = 60

  var solidAreaX = 22
  var solidAreaY = 40
  solidAreaDefaultX = solidAreaX
  solidAreaDefaultY = solidAreaY
  var solidAreaWidth = (12)
  var solidAreaHeight = (12)

  var solidArea = Rectangle(solidAreaX , solidAreaY , (solidAreaWidth * 1.25).toInt, (solidAreaHeight * 1.25).toInt)

  // Screen, animations stats
  val screenX: Int = gp.screenWidth / 2 - (gp.tileSize / 2)
  val screenY: Int = gp.screenHeight / 2 - (gp.tileSize / 2)
  private val playerScale = (gp.tileSize * 1.25).toInt
  private var isMoving = false

  private val frameSize = 64
  private val spriteFrames = Tools.loadFrames("Players/Player_spritesheet", frameSize, frameSize, playerScale, 21)
  private val attackFrames = Tools.loadFrames("Players/attack_spritesheet", frameSize, frameSize,playerScale, 4)
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

  needsAnimationUpdate = false
  var hasCallDie = false
  currentAnimation = images((direction, state))

  // Getter methods
  def getState = this.state
  def getHealth = this.health
  override def getPosition = this.pos
  def getSpeed = this.speed
  def getCurrentAnimation = this.currentAnimation
  def getDefense: Int = currentShield.defense * dexterity
  def getCurrentWeapon: Weapon = currentWeapon
  def getCurrentShield: Shield = currentShield
  def getCurrentProjectile: Projectile = currentProjectile
  def getAttackDamage: Int =
    attackArea = currentWeapon.attackArea
    currentWeapon.damage * strength

  // Needs update animations
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
      gp.keyH.attackPressed = false
      updateDirection()
      // save current world x,y
      val currentWorldX = pos._1
      val currentWorldy = pos._2
      val solidAreaWidth = this.solidArea.width
      val solidAreaHeight = this.solidArea.height
      // adjust player's worlds x,y
      direction match
        case UP => val newPosY = pos._2 - attackArea.height;  this.pos = (pos._1, newPosY)
        case DOWN => val newPosY = pos._2 + attackArea.height;  this.pos = (pos._1, newPosY)
        case LEFT => val newPosX = pos._1 - attackArea.width;  this.pos = (newPosX, pos._2)
        case RIGHT => val newPosX = pos._1 + attackArea.width;  this.pos = (newPosX, pos._2)
        case ANY =>
      // attackAreaBecome solid Area
      solidArea.width = attackArea.width
      solidArea.height = attackArea.height
      val enemyIndex = gp.cCheck.checkCollisionWithTargetsHitBox(this, gp.enemyList)
      attackEnemy(enemyIndex, attackDamage)

      this.pos = (currentWorldX, currentWorldy)
      solidArea.width = solidAreaWidth
      solidArea.height = solidAreaHeight

      state = State.ATTACK
      needsAnimationUpdate = true
      counter = 0
      attackCooldown = maxAttackCooldown


  def setItems(): Unit =
    this.inventory.clear()
    this.inventory += currentWeapon
    this.inventory += currentShield
    this.inventory += currentProjectile
    this.inventory += OBJ_NormalHealFlask(gp)
    this.inventory += OBJ_SilverKey(gp)

  def selectItem(): Unit =
    val itemIndex = PlayerUI.getItemIndexBySlot(PlayerUI.playerSlotCol, PlayerUI.playerSlotRow)
    if itemIndex < this.inventory.size then
      val selectedItem = this.inventory(itemIndex)
      selectedItem match
        case weapon: Weapon =>
          currentWeapon = weapon
          attackDamage = getAttackDamage
        case shield: Shield =>
          currentShield = shield
          defense = getDefense
        case coin: Coin =>
          coin.pickupCoin(this)
          inventory.remove(itemIndex)
        case potion: Potion =>
          if potion.use(this) then
            if potion.amount > 1 then potion.amount -= 1 else inventory.remove(itemIndex)
          else
            inventory.remove(itemIndex)
        case item: Item =>
          if item.use(this) then
            inventory.remove(itemIndex)

  def searchItemInInventory(itemName: String): Int =
    var itemIndex = -1
    for i <- inventory.indices do
      if inventory(i).name.equals(itemName) then
        itemIndex = i

    itemIndex

  def obtainItem(item: Item): Boolean =
    var canObtain : Boolean = false
    if item.isStackable then
      val index = searchItemInInventory(item.name)

      if index != -1 then
        inventory(index).amount += 1
        canObtain = true
      else // New ITEM
        if inventory.size != maxInventorySize then
          inventory += item
          canObtain = true
    else // New ITEM
        if inventory.size != maxInventorySize then
          inventory += item
          canObtain = true
    canObtain

  override def die(): Unit =
    if !hasCallDie then
      super.die()
      dyingCounter += 1
      if dyingCounter > 120 then
        gp.gui.isFinished = true
      hasCallDie = true

  // -----------------------------------------------
  // Rendering methods

  // call by the game loop
  override def update(): Unit =
    handleInvincibility()
    handleGameOver()
    if attackCooldown > 0 then
      attackCooldown -= 1

    state match
      case State.ATTACK => handleAttackState()
      case _ => handleInput()

    if shootCounter < 60 then
      shootCounter += 1

  override def draw(g: Graphics2D): Unit =
    if isInvinc then
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.3f))
    if isDead then if !hasCallDie then die()
    currentAnimation.getCurrentFrame match
      case frame =>
          g.drawImage(frame,
            (this.screenX),
            (this.screenY),
            null)
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))
  // ------------------------------

  // Helper methods. To call in update
  def pickUpObject(index : Int): Unit =
    if(index != -1) then
      var text: String = ""
      val obj = gp.obj(gp.currentMap)(index)
      obj match
        case coin: Coin =>
          coin.pickupCoin(this)
          gp.obj(gp.currentMap)(index) = null
        case item: Item =>
          if obtainItem(item) then
            gp.obj(gp.currentMap)(index) = null
            text = s"Picked up: ${item.name}"
          else text = "Your inventory is full"
        case obstacle :Obstacle =>
          if gp.keyH.enterPressed then
            obstacle.interact()
        case _ =>
      gp.gui.addMessage(text)

  def interactNPC(index : Int): Unit =
    if(index != -1) then
      if gp.keyH.enterPressed then
        gp.gameState = GameState.DialogueState
        gp.npcList(gp.currentMap)(index).speak()
    gp.keyH.enterPressed = false

  def attackEnemy(enemyIndex: Int, damageDealt: Int): Unit =
    if enemyIndex != -1 then
      val currentEnemy = gp.enemyList(gp.currentMap)(enemyIndex)
      if !currentEnemy.isInvinc then
        var damage = damageDealt - currentEnemy.defense
        if damage < 0 then damage = 0
        currentEnemy.takeDamage(damage)
        currentEnemy.damageReaction()
        gp.gui.addMessage(damage + " damage!")
        currentEnemy.isInvinc = true

        if !currentEnemy.isAlive then
          gp.gui.addMessage("Kill " + currentEnemy.name)
          exp += currentEnemy.expGet
          gp.gui.addMessage("Exp + " + exp)
          checkLevelUp()

  def checkLevelUp(): Unit =
    if exp >= nextLevelExp then
      exp -= nextLevelExp
      level += 1
      nextLevelExp = nextLevelExp * 2
      maxHealth += 10
      strength += 1
      dexterity += 1
      attackDamage = getAttackDamage
      defense = getDefense
      // play level up sound/////
      gp.gameState = GameState.DialogueState
      gp.gui.currentDialogue = "You are at level " + level + " now!"

// Helper function to handle movements, direction
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
    counter += 1
    if (counter >= 30) then
      counter = 0
      state = State.IDLE
    needsAnimationUpdate = true
    checkAnimationUpdate()

  private def handleInput(): Unit =

    if (gp.keyH.attackPressed && attackCooldown == 0) then
      attack()
    else if gp.keyH.upPressed || gp.keyH.downPressed || gp.keyH.leftPressed || gp.keyH.rightPressed || gp.keyH.enterPressed then
      updateDirection()
      checkAnimationUpdate()

      isCollided = false

      // CHECK TILE
      gp.cCheck.checkTileCollision(this)

      // CHECK OBJECT
      val objectIndex = gp.cCheck.checkObjectCollision(this, true)
      pickUpObject(objectIndex)

      val npcIndex = gp.cCheck.checkCollisionWithTargets(this, gp.npcList)
      interactNPC(npcIndex)

      // CHECK EVENT
      val enemyIndex = gp.cCheck.checkCollisionWithTargets(this, gp.enemyList)
//      if enemyIndex != -1 then
//        println(enemyIndex)
//        gp.enemyList(enemyIndex).attackPlayer(gp.enemyList(enemyIndex).attackPower)

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
      gp.keyH.enterPressed = false
    else
      if state != State.IDLE then
        state = State.IDLE
        isMoving = false
        needsAnimationUpdate = true

    if gp.keyH.shootKeyPressed && !currentProjectile.alive
        && shootCounter == 60 && this.currentProjectile.haveEnoughMana(this) then
      currentProjectile.set(this.pos, this.direction, true, this)
      currentProjectile.useMana(this)

      gp.projectileList += currentProjectile

      shootCounter = 0

  private def handleGameOver(): Unit =
    if isDead then
      gp.gameState = GameState.GameOver
      gp.gui.commandNum = -1

  def reset(): Unit =
    this.pos = (gp.tileSize * 23, gp.tileSize * 21)
    health = maxHealth
    mana = maxMana
    this.level = 1
