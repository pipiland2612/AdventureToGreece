package Environment

import game.GamePanel
import java.awt.Graphics2D

class EnvironmentManager (gp: GamePanel) :
  var lighting: Lighting = _

  def setup(): Unit =
    lighting = new Lighting(gp)

  def update(): Unit =
    lighting.update()

  def draw(g2: Graphics2D) :Unit =
    lighting.draw(g2)

end EnvironmentManager
