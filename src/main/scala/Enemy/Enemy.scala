package Enemy

import `object`.ObjectType.{OBJ_BronzeCoin, OBJ_NormalHealFlask}
import entities.State.IDLE
import entities.{Creatures, Entity, Player, State}
import game.GamePanel
import items.Item

import java.awt.{AlphaComposite, Color, Graphics2D}
import scala.util.Random

abstract class Enemy(gp: GamePanel) extends Creatures(gp):

  // -----------------------------------------------
  // Enemy Specific Attributes
  var counter: Int = 0
  var spawnCounter: Int = 0
  var hasSpawn: Boolean = false
  val thisIndex = gp.enemyList(gp.currentMap).indexOf(this)

  var pos: (Int, Int)
  var attackPower: Int
  var defense: Int
  var expGet: Int

  var hpBarOn: Boolean = false
  var hpBarCounter: Int = 0
  var shotCounter: Int = 0

  var itemDropped: Vector[Item]

  // -----------------------------------------------
  // Distance Calculations
  def getXDistance(entity: Entity): Int = Math.abs(this.pos._1 - entity.pos._1)
  def getYDistance(entity: Entity): Int = Math.abs(this.pos._2 - entity.pos._2)
  def getTileDistance(entity: Entity): Int = getXDistance(entity) + getYDistance(entity) / gp.tileSize
  def getGoalCol(entity: Entity): Int = (entity.pos._1 + entity.solidArea.x) / gp.tileSize
  def getGoalRow(entity: Entity): Int = (entity.pos._2 + entity.solidArea.y) / gp.tileSize

  // -----------------------------------------------
  // Chasing Logic
  def stopChasing(entity: Entity, distance: Int, rate: Int): Unit =
    if getTileDistance(entity) > distance then
      val i = new Random().nextInt(rate)
      if i == 0 then
        isOnPath = false

  def startChasing(entity: Entity, distance: Int, rate: Int): Unit =
    if getTileDistance(entity) < distance then
      val i = new Random().nextInt(rate)
      if i == 0 then
        isOnPath = false

  // -----------------------------------------------
  // Shooting Logic
  def checkToShoot(rate: Int, interval: Int): Unit =
    if currentProjectile != null then
      val i = new Random().nextInt(rate) + 1
      if i >= rate - 2 && !currentProjectile.alive && shotCounter >= interval then
        this.currentProjectile.set(this.pos, direction, true, this)
        gp.projectileList += this.currentProjectile
        shotCounter = 0

  def setRandomDirection(): Unit =
    counter += 1
    if counter >= 120 then
      val random = new Random()
      this.direction = directions(random.nextInt(directions.length))
      currentAnimation = runAnimations(this.direction)
      counter = 0

  // -----------------------------------------------
  // Enemy Actions
  def attackPlayer(damagePower: Int): Unit =
    var damage = damagePower - gp.player.defense
    if damage < 0 then damage = 0
    gp.player.takeDamage(damage)
    gp.gui.addMessage(s"Monster attack! -$damage HP")
    gp.player.isInvinc = true

  def damageReaction(): Unit =
    counter = 0
    isOnPath = true

  def dropItem(item: Item): Unit =
    for index <- gp.obj(1).indices do
      if gp.obj(gp.currentMap)(index) == null then
        gp.obj(gp.currentMap)(index) = item
        gp.obj(gp.currentMap)(index).pos = (this.pos._1 + this.solidArea.x, this.pos._2 + this.solidArea.y)
        return

  def checkDrop(): Unit =
    val randomInt = new Random().nextInt(100) + 1
    if randomInt < 50 then
      dropItem(new OBJ_BronzeCoin(gp))
    else if randomInt >= 50 then
      dropItem(new OBJ_NormalHealFlask(gp))

  def spawn(): Unit =
    if !hasSpawn then
      spawnCounter += 1
      state = State.SPAWN
      if spawnCounter > 150 then
        hasSpawn = true
        this.state = IDLE
    needsAnimationUpdate = true
    checkAnimationUpdate()

  override def die(): Unit =
    super.die()
    dyingCounter += 1
    if dyingCounter >= 150 then
      dying = true

  // -----------------------------------------------
  // Game Loop Update
  override def update(): Unit =
    if !hasSpawn then
      spawn()
      return

    if isDead then
      die()
      return

    if isAlive then
      performAliveActions()

    continueMove()

  override def setAction(): Unit =
    val tileDistance = getTileDistance(gp.player)
    if isOnPath then
//      stopChasing(gp.player, 15, 100)
      findPath(getGoalRow(gp.player), getGoalCol(gp.player))
    else
      startChasing(gp.player, 5, 100)
      setRandomDirection()

  // -----------------------------------------------
  // Rendering Methods
  override def draw(g: Graphics2D): Unit =
    val (screenX, screenY) = calculateScreenCoordinates()
    if hpBarOn then
      val oneScale: Double = gp.tileSize / this.maxHealth
      val hpBarValue = oneScale * this.health
      g.setColor(new Color(35, 35, 35))
      g.fillRect(screenX + gp.tileSize - 1, screenY - 6, hpBarValue.toInt, 8)
      g.setColor(new Color(255, 0, 30))
      g.fillRect(screenX + gp.tileSize, screenY - 5, hpBarValue.toInt, 6)
      hpBarCounter += 1
      if hpBarCounter > 800 then
        this.health = maxHealth
        hpBarCounter = 0
        hpBarOn = false
    if isInvinc then
      hpBarOn = true
      hpBarCounter = 0
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f))
    super.draw(g)
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))

  private def performAliveActions(): Unit =
    setAction()
    isCollided = false
    handleInvincibility()
    gp.cCheck.checkTileCollision(this)
    gp.cCheck.checkObjectCollision(this, false)
    gp.cCheck.checkCollisionWithTargets(this, gp.enemyList)
    handlePlayerCollision()
    checkAnimationUpdate()

  private def handlePlayerCollision(): Unit =
    val hasTouchedPlayer = gp.cCheck.checkPlayer(this)
    if hasTouchedPlayer && !gp.player.isInvinc then
      attackPlayer(this.attackPower)