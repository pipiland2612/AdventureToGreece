package items

import entities.Player
import game.GamePanel
import utils.Tools

import java.awt.Rectangle

abstract class Coin (var value : Int, gp: GamePanel) extends Item(gp):
  image = Tools.loadImage("Objects/coin.png")
  var solidArea = Rectangle(solidAreaDefaultX, solidAreaDefaultY, 32, 32)

  def pickupCoin(player: Player): Unit =
    gp.gui.addMessage(s"$name has value $value")
    gp.player.coin += value

  def getDescription: String = s"Coin $name \nhas $value value!"
end Coin


