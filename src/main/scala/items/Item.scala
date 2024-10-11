package items

import entities.{Creatures, Direction, Entity}
import game.GamePanel

import java.awt.image.BufferedImage

abstract class Item(gp : GamePanel) extends Entity(gp):

  var size = gp.tileSize
  var price = 0

  var imageDisplayed: BufferedImage
  var name: String
  var isStackable: Boolean = false
  var amount: Int = 1

  def getName = this.name
  def getDescription: String
  def use(entity: Creatures): Boolean = false
  def canOpenChest: Boolean = false

  
  def getDetected(user: Creatures, target: Array[Array[Entity]], name:String): Int =
    var index = -1
    var (nextWorldX, nextWorldY) = (user.getLeftX, user.getTopY)

    user.direction match
      case Direction.UP => nextWorldY = user.getTopY - 1
      case Direction.DOWN => nextWorldY = user.getBottomY + 1
      case Direction.LEFT => nextWorldX = user.getLeftX - 1
      case Direction.RIGHT => nextWorldX = user.getRightX + 1
      case Direction.ANY =>

    val col = nextWorldX / gp.tileSize
    val row = nextWorldY / gp.tileSize

    for i <- target(1).indices do
      if target(gp.currentMap)(i) != null then
        if target(gp.currentMap)(i).getCol == col && target(gp.currentMap)(i).getRow == row && target(gp.currentMap)(i).name.equals(name) then
          index = i

    index
  
end Item
