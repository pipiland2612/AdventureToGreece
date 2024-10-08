package Enemy

import `object`.{OBJ_BronzeCoin, OBJ_NormalHealFlask}
import entities.State.IDLE
import entities.{Creatures, Player, State}
import game.GamePanel
import items.Item

import java.awt.{AlphaComposite, Color, Graphics2D}
import scala.util.Random

abstract class Enemy(gp: GamePanel) extends Creatures(gp):
  var counter: Int = 0
  var spawnCounter: Int = 0
  var hasSpawn: Boolean = false
  val thisIndex = gp.enemyList(gp.currentMap).indexOf(this)
  //
  var pos: (Int, Int)
  var attackPower: Int
  var defense: Int
  var expGet: Int

  var hpBarOn: Boolean = false
  var hpBarCounter: Int = 0

  var itemDropped: Vector[Item]

  // ENEMY METHODS
  def attackPlayer(damagePower: Int): Unit =
    var damage = damagePower - gp.player.defense
    if damage < 0 then damage = 0
    gp.player.takeDamage(damage)
    gp.gui.addMessage(s"Monster attack! -$damage HP")
    gp.player.isInvinc = true

  def moveTowardsPlayer(player: Player): Unit
  def damageReaction(): Unit =
    counter = 0
//    direction = gp.player.direction

  def dropItem (item: Item): Unit =
    for index <- gp.obj(1).indices do
      if gp.obj(gp.currentMap)(index) == null then
        gp.obj(gp.currentMap)(index) = item
        gp.obj(gp.currentMap)(index).pos = (this.pos._1 + this.solidArea.x, this.pos._2 + this.solidArea.y)
        return
        
  def checkDrop (): Unit =
    val randomInt = new Random().nextInt(100) + 1

    if randomInt < 50 then
      dropItem(new OBJ_BronzeCoin(gp))
    if randomInt >= 50 then
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

  // Call by the game loop
  override def update(): Unit =
      if (!hasSpawn) then
        spawn()
        return

      if (isDead) then
        die()
        return

      if (isAlive) then
        performAliveActions()

      continueMove()

  override def setAction(): Unit =
    counter += 1
    if counter >= 120 then
      val random = new Random()
      this.direction = directions(random.nextInt(directions.length))
      currentAnimation = runAnimations(this.direction)
      counter = 0

  //    var i = new Random().nextInt(100) + 1
  //    if i == 100 && !projective.alive && shotCounter == 60 then
  //      this.projectile.set (this.pos, direction, true, this)
  //      gp.projectile += this.projectile
  //          shotCounter = 0to

  override def draw(g: Graphics2D): Unit =
    val (screenX, screenY) = calculateScreenCoordinates()

    if hpBarOn then
      val oneScale: Double = gp.tileSize / this.maxHealth
      val hpBarVlue = oneScale * this.health

      g.setColor(new Color(35, 35, 35))
      g.fillRect(screenX + gp.tileSize - 1, screenY - 6, hpBarVlue.toInt, 8)

      g.setColor(new Color(255, 0, 30))
      g.fillRect(screenX + gp.tileSize, screenY - 5, hpBarVlue.toInt, 6)

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

    // Handle collisions
    gp.cCheck.checkTileCollision(this)
    gp.cCheck.checkObjectCollision(this, false)
    gp.cCheck.checkCollisionWithTargets(this, gp.enemyList)

    // handle player collision
    handlePlayerCollision()

    // Update animations
    checkAnimationUpdate()

  private def handlePlayerCollision(): Unit =
    val hasTouchedPlayer = gp.cCheck.checkPlayer(this)

    if (hasTouchedPlayer && !gp.player.isInvinc) then
      this.attackPlayer(this.attackPower)
