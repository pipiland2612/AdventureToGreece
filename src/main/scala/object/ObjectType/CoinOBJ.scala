package `object`.ObjectType

import game.GamePanel
import items.Coin
import utils.Tools

class OBJ_BronzeCoin(gp: GamePanel) extends Coin(1, gp):
  var name = OBJ_BronzeCoin.Name
  var pos = (0, 0)
  var imageDisplayed = Tools.scaleImage(Tools.loadImage("Objects/coin.png"), 32, 32)
  isStackable = true
  image = Tools.scaleImage(Tools.loadImage("Objects/coin.png"), 20, 20)

object OBJ_BronzeCoin:
  val Name: String = "Bronze Coin"


class OBJ_GoldCoin(gp: GamePanel) extends Coin(10, gp):
  var name = OBJ_GoldCoin.Name
  var pos = (0, 0)
  var imageDisplayed = Tools.scaleImage(Tools.loadImage("Objects/gold_coin.png"), 32, 32)
  isStackable = true
  image = Tools.scaleImage(Tools.loadImage("Objects/gold_coin.png"), 20, 20)

object OBJ_GoldCoin:
  val Name: String = "Gold Coin"