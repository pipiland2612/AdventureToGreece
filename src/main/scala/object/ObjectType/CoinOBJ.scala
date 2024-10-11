package `object`.ObjectType

import game.GamePanel
import items.Coin
import utils.Tools

class OBJ_BronzeCoin (gp: GamePanel) extends Coin(1, gp):
  var name = "Bronze Coin"
  var pos = (0,0)
  var imageDisplayed = Tools.scaleImage(Tools.loadImage("Objects/coin.png"), 32, 32)
  isStackable = true
  image = Tools.scaleImage(Tools.loadImage("Objects/coin.png"), 20, 20)

