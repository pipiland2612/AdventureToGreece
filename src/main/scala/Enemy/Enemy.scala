package Enemy

import entities.{Creatures, Direction, Entity, State}
import game.GamePanel
import items.Item

import java.awt.{AlphaComposite, Graphics2D}
import scala.util.Random

abstract class Enemy(gp: GamePanel) extends Creatures(gp):

  // -----------------------------------------------
  // Enemy Specific Attributes
  val thisIndex = gp.enemyList(gp.currentMap).indexOf(this)
  val directions = Vector(Direction.UP, Direction.DOWN, Direction.LEFT, Direction.RIGHT)

  var pos: (Int, Int)
  var defense: Int
  var expGet: Int
  var attackRate: Int
  var verticalScanRange: Int
  var horizontalScanRange: Int

  var changeDirectionInterval: Int

  var hpBarOn: Boolean = false
  var hpBarCounter: Int = 0
  var isBoss: Boolean = false

  var itemDropped: Vector[Item]

  var hasOwnDyingAnimation: Boolean = false

  var isSleeping: Boolean = false

  // -----------------------------------------------
  // Distance Calculations
  def getXDistance(entity: Entity): Int = Math.abs((getCenterX(this)) - (getCenterX(entity)))
  def getYDistance(entity: Entity): Int = Math.abs((getCenterY(this)) - (getCenterY(entity)))
  def getTileDistance(entity: Entity): Int = (getXDistance(entity) + getYDistance(entity)) / gp.tileSize
  def getGoalCol(entity: Entity): Int = (entity.pos._1 + entity.solidArea.x) / gp.tileSize
  def getGoalRow(entity: Entity): Int = (entity.pos._2 + entity.solidArea.y) / gp.tileSize

  def getCenterX (entity: Entity): Int =
    if entity.imageWidthCenter != 0 then entity.pos._1 + entity.imageWidthCenter else entity.pos._1 + entity.areaHitBox.x
  def getCenterY (entity: Entity): Int =
    if entity.imageWidthCenter != 0 then entity.pos._2 + entity.imageHeightCenter else entity.pos._2 + entity.areaHitBox.y

  // -----------------------------------------------
  // Chasing, Attack Logic
  def checkStopChase(entity: Entity, distance: Int, rate: Int): Unit =
    val tileDistance = getTileDistance(entity)
    if tileDistance > distance then
      val i = new Random().nextInt(rate)
      if i == 0 then
        isOnPath = false

  def checkToChase(entity: Entity, distance: Int, rate: Int): Unit =
    val tileDistance = getTileDistance(entity)
    if tileDistance < distance then
      isOnPath = true

  def checkToAttack(rate: Int, vertical: Int, horizontal: Int): Unit =
    var targetInRange = false
    val xDis = getXDistance(gp.player)
    val yDis = getYDistance(gp.player)

    this.direction match
      case Direction.UP =>
        if getCenterY(gp.player) < getCenterY(this) && yDis < vertical && xDis < horizontal then
          targetInRange = true
      case Direction.DOWN =>
        if getCenterY(gp.player) > getCenterY(this) && yDis < vertical + (gp.tileSize / 3) && xDis < horizontal then
          targetInRange = true
      case Direction.LEFT =>
        if getCenterX(gp.player) < getCenterX(this) && yDis < vertical && xDis < horizontal then
          targetInRange = true
      case Direction.RIGHT =>
        if getCenterX(gp.player) > getCenterX(this) && yDis < vertical && xDis < horizontal then
          targetInRange = true
      case Direction.ANY =>

    if targetInRange then
      val i = new Random().nextInt(rate)
      if i == 0 then
        attack()
        shootCounter = 0

  // -----------------------------------------------
  // Shooting Logic
  def checkToShoot(rate: Int, interval: Int): Unit =
    if currentProjectile != null then
      val i = new Random().nextInt(rate) + 1
      if i >= rate - 2 && !currentProjectile.alive && shootCounter >= interval then
        this.currentProjectile.set(this.pos, direction, true, this)
        gp.projectileList += this.currentProjectile
        shootCounter = 0

  def setRandomDirection(interval: Int): Unit =
    counter += 1
    if counter >= interval then
      val random = new Random()
      this.direction = directions(random.nextInt(directions.length))
      currentAnimation = runAnimations(this.direction)
      counter = 0

  // -----------------------------------------------
  // Enemy Actions
  override def dealDamage(damagePower: Int): Unit =
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
    val randomItem = new Random().nextInt(itemDropped.size)
    if isBoss then
      dropItem(itemDropped(randomItem))
    else
      if randomInt < 70 then
        dropItem(itemDropped(randomItem))

  def moveTowardPlayer(interval : Int): Unit =
    counter += 1
    if counter >= interval then
      if getXDistance(gp.player) > getYDistance(gp.player) then
        if getCenterX(gp.player) < getCenterX(this) then
          this.direction = Direction.LEFT
        else
          this.direction = Direction.RIGHT
      else if getXDistance(gp.player) < getYDistance(gp.player) then
        if getCenterY(gp.player) < getCenterY(this) then
          this.direction = Direction.UP
        else
          this.direction = Direction.DOWN
      counter = 0

  // -----------------------------------------------
  // Game Loop Update
  override def update(): Unit =
    if !isSleeping then
      if isDead && hasOwnDyingAnimation then
        state = State.DEAD
        super.die()
        dyingCounter += 1
        if dyingCounter >= 180 then
          dying = true

      handleInvincibility()

      super.update()

  override def setAction(): Unit =
    val tileDistance = getTileDistance(gp.player)
    if isOnPath then
      checkStopChase(gp.player, 15, 100)
      findPath(getGoalRow(gp.player), getGoalCol(gp.player))
    else
      checkToChase(gp.player, 5, 100)
      setRandomDirection(this.changeDirectionInterval)

    if state != State.ATTACK then checkToAttack(attackRate, verticalScanRange, horizontalScanRange)

  // -----------------------------------------------
  // Rendering Methods
  override def draw(g: Graphics2D): Unit =
    val (screenX, screenY) = calculateScreenCoordinates()

    if isInvinc then
      hpBarOn = true
      hpBarCounter = 0
      g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.7f))

    if isDead && !hasOwnDyingAnimation then
      state = State.DEAD
      dyingAnimation(g)
    super.draw(g)
    g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))

  private def handlePlayerCollision(): Unit =
    val hasTouchedPlayer = gp.cCheck.checkPlayer(this)
    if hasTouchedPlayer && !gp.player.isInvinc then
      dealDamage(this.damagePower)

  private def dyingAnimation (g2: Graphics2D): Unit =
    dyingCounter += 1
    if dyingCounter <= 5 then g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))
    if dyingCounter > 5 && dyingCounter <= 10 then g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f))
    if dyingCounter > 10 && dyingCounter <= 15 then g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))
    if dyingCounter > 15 && dyingCounter <= 20 then g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f))
    if dyingCounter > 20 && dyingCounter <= 25 then g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))
    if dyingCounter > 25 && dyingCounter <= 30 then g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f))
    if dyingCounter > 30 && dyingCounter <= 35 then g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))
    if dyingCounter > 35 && dyingCounter <= 40 then g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f))
    if dyingCounter > 40 && dyingCounter <= 45 then g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))
    if dyingCounter > 45 && dyingCounter <= 50 then g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0f))
    if dyingCounter > 50 && dyingCounter <= 55 then g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))
    if dyingCounter > 55 && dyingCounter <= 60 then g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1f))
    if dyingCounter > 60 then dying = true