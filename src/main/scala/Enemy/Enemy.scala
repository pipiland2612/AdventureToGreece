package Enemy

import entities.{Creatures, Player}
import game.GamePanel

abstract class Enemy( gp: GamePanel) extends Creatures(gp):
  var pos: (Int, Int)
  var attackPower: Int
  def attackPlayer(player: Player): Unit
  def moveTowardsPlayer(player: Player): Unit