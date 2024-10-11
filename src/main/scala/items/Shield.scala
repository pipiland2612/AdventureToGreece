package items
import game.GamePanel

import java.awt.Rectangle


abstract class Shield(var defense: Int, gp: GamePanel ) extends Item (gp):
  var name = ""
  var pos = (0,0)
  var solidArea: Rectangle = Rectangle()
  def getDescription: String = s"Shield $name \nhas $defense defense value!"



