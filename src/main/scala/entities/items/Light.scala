package entities.items

import game.GamePanel

abstract class Light(gp: GamePanel) extends  Item(gp) :
  var lightRadius: Int

  def getDescription: String = s"$name can light\nup to $lightRadius range!"