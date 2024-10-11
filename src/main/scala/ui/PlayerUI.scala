package ui

import `object`.{OBJ_Heart, OBJ_Mana}
import entities.Creatures
import game.GamePanel
import utils.Tools

import java.awt.image.BufferedImage
import java.awt.{BasicStroke, Color, Graphics2D}

object PlayerUI:
  var g2: Graphics2D = _
  var gp: GamePanel = _

  var tileSize: Int = _
  var heart: OBJ_Heart = new OBJ_Heart(25 , gp)
  var mana : OBJ_Mana = new OBJ_Mana(25, gp)

  var playerSlotCol = 0
  var playerSlotRow = 0
  var npcSlotRow = 0
  var npcSlotCol = 0

  def setGraphics(g: Graphics2D, gamePanel: GamePanel): Unit =
    g2 = g
    gp = gamePanel
    tileSize = gp.tileSize

  def drawPlayerLife(): Unit =
    val spacing = tileSize / 8
    val y = tileSize / 2 - 20
    val otherHealth: Int = if gp.player.maxHealth % 20 >= 0 then 1 else 0
    val totalHearts = if gp.player.maxHealth % 20 == 0 then gp.player.maxHealth / 20 else gp.player.maxHealth / 20 + otherHealth
    val currentHealth = gp.player.health

    drawPlayerStats(spacing, y, totalHearts, currentHealth, heart.image, heart.image2, heart.image3, heart.image4, heart.image5)

  def drawPlayerMana(): Unit =
    val spacing = tileSize / 8
    val y = tileSize / 2 + 5
    val totalMana = gp.player.maxMana / 20
    val currentMana = gp.player.mana

    drawPlayerStats(spacing, y, totalMana, currentMana, mana.image, mana.image2, mana.image3, mana.image4, mana.image5)

  def drawInventory(creature: Creatures, cursor : Boolean): Unit =
    // Frame
    var slotRow: Int = 0
    var slotCol: Int = 0
    var frameX: Int = 0
    var frameY: Int = 0
    var frameWidth: Int = 0
    var frameHeight: Int = 0

    if creature == gp.player then
      frameX = gp.screenWidth - tileSize * 7
      frameY = tileSize
      frameWidth = tileSize * 6
      frameHeight = tileSize * 5
      slotRow = playerSlotRow
      slotCol = playerSlotCol
    else
      frameX = tileSize * 2
      frameY = tileSize
      frameWidth = tileSize * 6
      frameHeight = tileSize * 5
      slotRow = npcSlotRow
      slotCol = npcSlotCol

    Tools.drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight)

    // SLOT
    val slotXstart = frameX + 20
    val slotYstart = frameY + 20
    var slotX = slotXstart
    var slotY = slotYstart
    var slotSize = tileSize + 3

    // Player's Item
    for index <- creature.inventory.indices do
      val currentItem = creature.inventory(index)
      if currentItem == creature.currentWeapon || currentItem == creature.currentShield
        || currentItem == creature.currentProjectile then
        g2.setColor(new Color (240, 190, 90))
        g2.fillRoundRect(slotX, slotY, tileSize, tileSize, 10, 10 )

      g2.drawImage(currentItem.imageDisplayed, slotX + 5, slotY + 5, null)

      // DISPLAY AMOUBNT
      if creature == gp.player && currentItem.amount > 1 then
        g2.setFont(g2.getFont.deriveFont(20f))
        val string = s"${currentItem.amount}"
        val amountX = Tools.getRightX(g2, string, slotX + 44)
        val amountY =  slotY + tileSize
        // SHADOW
        g2.setColor(new Color(60, 60, 60))
        g2.drawString(string, amountX, amountY)
        g2.setColor(Color.WHITE)
        g2.drawString(string, amountX - 3, amountY - 3)

      slotX += slotSize
      if index % 5 == 4 then
        slotX = slotXstart
        slotY += slotSize

    // CURSOR
    if cursor then
      val cursorX = slotXstart + (slotSize * slotCol)
      val cursorY = slotYstart + (slotSize * slotRow)
      val cursorWidth = tileSize
      val cursorHeight = tileSize

      // DRAW
      g2.setColor(Color.WHITE)
      g2.setStroke(new BasicStroke(3))
      g2.drawRoundRect(cursorX, cursorY, cursorWidth, cursorHeight, 10, 10)

      // Descriptions FRAME, and Draw
      val dframeX = frameX
      val dframeY = frameY + frameHeight
      val dframeWidth = frameWidth
      val dframeHeight = tileSize * 3

      val textX = dframeX + 20
      var textY = dframeY + tileSize * 0.8
      g2.setFont(g2.getFont.deriveFont(20F))
      val itemIndex = getItemIndexBySlot(slotCol, slotRow)
      if itemIndex < creature.inventory.size then
        Tools.drawSubWindow(g2, dframeX, dframeY, dframeWidth, dframeHeight)
        val currentItem = creature.inventory(itemIndex)
        for (line <- currentItem.getDescription.split("\n")) do
          g2.drawString(line, textX, textY.toInt)
          textY += 32

  def drawCharacterState(): Unit =
    val frameX = (tileSize * 1.5).toInt
    val frameY = tileSize
    val frameWidth = tileSize * 5
    val frameHeight = tileSize * 10
    Tools.drawSubWindow(g2, frameX, frameY, frameWidth, frameHeight)
    g2.setColor(Color.WHITE)
    g2.setFont(g2.getFont.deriveFont(20F))

    val labels = List(
        "Level" -> (() => gp.player.level.toString),
        "Health" -> (() => s"${gp.player.health}/${gp.player.maxHealth}"),
        "Mana" -> (() => s"${gp.player.mana}/${gp.player.maxMana}"),
        "Strength" -> (() => gp.player.strength.toString),
        "Dexterity" -> (() => gp.player.dexterity.toString),
        "Attack" -> (() => gp.player.attackDamage.toString),
        "Defense" -> (() => gp.player.defense.toString),
        "Next Level" -> (() =>s"${gp.player.exp}/${gp.player.nextLevelExp.toString}"),
        "Coin" -> (() =>s"${gp.player.coin}"),
        "Weapon" -> (() => ""),
        "Shield" -> (() => ""),
        "Projectile" -> (() => ""),
    )

    val initialTextX = frameX + 20
    var textY = frameY + tileSize - 10
    val lineHeight = 38
    val tailX = (frameX + frameWidth) - 30

    for ((label, _) <- labels) do
        g2.drawString(label, initialTextX, textY)
        textY += lineHeight

    textY = frameY + tileSize - 10

    for (((_, getValue), index) <- labels.zipWithIndex) do
        if (index < labels.length - 1) then
            val value = getValue()
            val textX = Tools.getRightX(g2, value, tailX)
            g2.drawString(value, textX, textY)
            textY += lineHeight

    val weapon = gp.player.getCurrentWeapon
    if (weapon != null && weapon.imageDisplayed != null) then
        g2.drawImage(weapon.imageDisplayed, (tailX - tileSize / 1.5).toInt, textY - 105, null)
    textY += lineHeight

    val shield = gp.player.getCurrentShield
    if (shield != null && shield.imageDisplayed != null) then
        g2.drawImage(shield.imageDisplayed, (tailX - tileSize / 1.5).toInt, textY - 100, null)
    textY += lineHeight

    val projectile = gp.player.getCurrentProjectile
    if (projectile != null && projectile.imageDisplayed != null) then
        g2.drawImage(projectile.imageDisplayed, (tailX - tileSize / 1.5).toInt, textY - 100, null)

  def getItemIndexBySlot(slotCol: Int, slotRow : Int ): Int = slotCol + (slotRow * 5)

  private def drawPlayerStats(
      spacing: Int,
      yPosition: Int,
      totalUnits: Int,
      currentUnits: Int,
      fullImage: BufferedImage,
      image2: BufferedImage,
      image3: BufferedImage,
      image4: BufferedImage,
      image5: BufferedImage
  ): Unit =
    val xStart = spacing
    var remainingUnits = currentUnits
    for (i <- 0 until totalUnits) do
      val x = xStart + i * (4 * spacing)

      if (remainingUnits >= 20) then

        g2.drawImage(fullImage, x, yPosition, null)
        remainingUnits -= 20
      else

        val unitImage = remainingUnits match
          case u if u >= 14 => image2
          case u if u >= 10 => image3
          case u if u >= 7  => image4
          case _            => image5

        g2.drawImage(unitImage, x, yPosition, null)
        remainingUnits = 0