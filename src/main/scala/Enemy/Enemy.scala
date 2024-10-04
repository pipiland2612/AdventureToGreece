package Enemy

import entities.State.IDLE
import entities.{Creatures, Player, State}
import game.GamePanel

import java.awt.{AlphaComposite, Color, Graphics2D}
import scala.util.Random

abstract class Enemy(gp: GamePanel) extends Creatures(gp):
  var counter: Int = 0
  var spawnCounter: Int = 0
  var hasSpawn: Boolean = false
  //
  var pos: (Int, Int)
  var attackPower: Int
  var defense: Int
  var expGet: Int

  var hpBarOn: Boolean = false
  var hpBarCounter: Int = 0

  // ENEMY METHODS
  def attackPlayer(player: Player): Unit
  def moveTowardsPlayer(player: Player): Unit
  def damageReaction(): Unit =
    counter = 0
//    direction = gp.player.direction

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
      gp.enemyList = gp.enemyList.filterNot(_ == this)

  // Call by the game loop
  def update(): Unit =
    if (!hasSpawn) then
      spawn()
      return

    if (dying) then
      die()
      return

    if (isAlive) then
      performAliveActions()

    continueMove()

  override def setAction(): Unit =
    counter += 1
    if counter >= 120 then
      var random = new Random()
      this.direction = directions(random.nextInt(directions.length))
      currentAnimation = runAnimations(this.direction)
      counter = 0

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

    if (hasTouchedPlayer && this.isInstanceOf[Enemy] && !gp.player.isInvinc) then
      gp.player.health -= 10
      gp.player.isInvinc = true
