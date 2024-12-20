package entities.items

import game.GamePanel

import java.awt.Rectangle

abstract class Weapon(var damage: Int, gp: GamePanel) extends Item (gp):
  var name = ""
  var pos = (0,0)
  var solidArea: Rectangle = Rectangle()
  def getDescription: String = s"$name has $damage \nbase damages! \nCurrent damage : ${gp.player.strength * damage}"