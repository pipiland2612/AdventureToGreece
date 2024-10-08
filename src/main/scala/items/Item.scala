package items

import entities.Entity
import game.GamePanel

import java.awt.image.BufferedImage

abstract class Item(gp : GamePanel) extends Entity(gp):

  var size = gp.tileSize
  var price = 0

  var imageDisplayed: BufferedImage
  var name: String
  def getName = this.name
  def getDescription: String
  
end Item
