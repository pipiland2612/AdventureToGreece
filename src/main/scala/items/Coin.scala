package items

import entities.Player
import game.GamePanel


import java.awt.Rectangle

abstract class Coin (var value : Int, gp: GamePanel) extends Item(gp):
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, 20, 20)

  def pickupCoin(player: Player): Unit =
    gp.gui.addMessage(s"$name has value $value")
    gp.player.coin += value


  def getDescription: String = s"Coin $name \nhas $value value!"
end Coin


