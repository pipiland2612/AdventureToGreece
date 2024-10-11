package Environment

import game.GamePanel

import java.awt.{Color, Graphics2D, RadialGradientPaint}
import java.awt.image.BufferedImage

class Lighting (gp: GamePanel) :
  var darkNessFilter: BufferedImage =_
  var g2: Graphics2D = _

  setLightSource()
  def setLightSource(): Unit =
    darkNessFilter = new BufferedImage(gp.screenWidth, gp.screenHeight, BufferedImage.TYPE_INT_ARGB)
    g2 = darkNessFilter.createGraphics

    if gp.player.currentLight == null then
      g2.setColor(new Color(0, 0, 0.07f, 0.98f))
    else
      val centerX = gp.player.screenX + gp.tileSize / 2
      val centerY = gp.player.screenY + gp.tileSize / 2

      val color: Array[Color] = new Array[Color](12)
      val fraction: Array[Float] = new Array[Float](12)

      color(0) = Color(0,0,0.07f,0.1f)
      color(1) = Color(0,0,0.07f,0.42f)
      color(2) = Color(0,0,0.07f,0.52f)
      color(3) = Color(0,0,0.07f,0.61f)
      color(4) = Color(0,0,0.07f,0.69f)
      color(5) = Color(0,0,0.07f,0.76f)
      color(6) = Color(0,0,0.07f,0.82f)
      color(7) = Color(0,0,0.07f,0.87f)
      color(8) = Color(0,0,0.07f,0.91f)
      color(9) = Color(0,0,0.07f,0.94f)
      color(10) = Color(0,0,0.07f,0.96f)
      color(11) = Color(0,0,0.07f,0.98f)

      fraction(0) = 0f
      fraction(1) = 0.4f
      fraction(2) = 0.5f
      fraction(3) = 0.6f
      fraction(4) = 0.65f
      fraction(5) = 0.7f
      fraction(6) = 0.75f
      fraction(7) = 0.8f
      fraction(8) = 0.85f
      fraction(9) = 0.9f
      fraction(10) = 0.95f
      fraction(11) = 1f

      val gPaint :RadialGradientPaint = RadialGradientPaint(centerX, centerY, gp.player.currentLight.lightRadius , fraction, color)
      g2.setPaint(gPaint)

    g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight)
    g2.dispose()

  def update(): Unit =
    if gp.player.lightUpdated then
      setLightSource()
      gp.player.lightUpdated = false

  def draw (g2: Graphics2D): Unit =
    g2.drawImage(darkNessFilter, 0, 0, null)

end Lighting

