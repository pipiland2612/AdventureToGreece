package entities.`object`

import entities.items.Coin
import game.GamePanel
import utils.Tools

class OBJ_BronzeCoin(gp: GamePanel) extends Coin(2, gp):
  var name = OBJ_BronzeCoin.Name
  var pos = (0, 0)
  var imageDisplayed = Tools.scaleImage(Tools.loadImage("objects/coin.png"), 32, 32)
  isStackable = true
  image = Tools.scaleImage(Tools.loadImage("objects/coin.png"), 20, 20)

object OBJ_BronzeCoin:
  val Name: String = "Bronze Coin"


class OBJ_GoldCoin(gp: GamePanel) extends Coin(10, gp):
  var name = OBJ_GoldCoin.Name
  var pos = (0, 0)
  var imageDisplayed = Tools.scaleImage(Tools.loadImage("objects/gold_coin.png"), 32, 32)
  isStackable = true
  image = Tools.scaleImage(Tools.loadImage("objects/gold_coin.png"), 20, 20)

object OBJ_GoldCoin:
  val Name: String = "Gold Coin"