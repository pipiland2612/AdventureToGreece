package Enemy

import entities.{Creatures, Player}
import game.GamePanel

import java.awt.{AlphaComposite, Color, Graphics2D}
import scala.util.Random

abstract class Enemy(gp: GamePanel) extends Creatures(gp):
  var counter: Int = 0

  var pos: (Int, Int)
  var attackPower: Int

  var hpBarOn: Boolean = false
  var hpBarCounter: Int = 0

  def damageReaction(): Unit =
    counter = 0
//    direction = gp.player.direction


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

  def attackPlayer(player: Player): Unit
  def moveTowardsPlayer(player: Player): Unit