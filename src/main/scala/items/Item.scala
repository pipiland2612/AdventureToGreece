package items

import entities.Entity
import game.GamePanel

import java.awt.Rectangle

abstract class Item(gp : GamePanel) extends Entity(gp):

  var name: String
  def getName = this.name
  def getDescription: String
  
end Item
