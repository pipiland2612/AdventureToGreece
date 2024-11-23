package utils

import Enemy.EN_KingOfDeath
import `object`.{OBJ_DungeonFence, OBJ_PlayerDummy}
import game.GamePanel
import game.GameState.PlayState

import java.awt.{Color, Font, GradientPaint, Graphics2D}

class CutsceneManager (var gp: GamePanel):

  var g2: Graphics2D = _
  var sceneNum: Int = 0
  var scenePhase: Int = 0

  var phaseStartTime: Long = 0
  val NA = 0
  val kingOfDeath = 1
  val credit = 2

  var creditY: Int = 0
  var fadeAlpha: Float = 0f
  var counter: Int = 0

  def draw(graphics2D: Graphics2D): Unit =
    this.g2 = graphics2D
    sceneNum match
      case this.kingOfDeath => kingOfDeathCutScene()
      case this.credit =>      creditCutScene()
      case _ =>

  def kingOfDeathCutScene(): Unit =
    var found = false
    if scenePhase == 0 then
      gp.onBossBattle = true

      for i <- gp.obj(1).indices if !found do
        if gp.obj(gp.currentMap)(i) == null then
          gp.obj(gp.currentMap)(i) = new OBJ_DungeonFence(gp)
          gp.obj(gp.currentMap)(i).pos = (24 * gp.tileSize, 12 * gp.tileSize)
          gp.obj(gp.currentMap)(i).isTemp = true
          found = true

      found = false
      for i <- gp.obj(1).indices if !found do
        if gp.obj(gp.currentMap)(i) == null then
          gp.obj(gp.currentMap)(i) = new OBJ_PlayerDummy(gp)
          gp.obj(gp.currentMap)(i).pos = gp.player.pos
          found = true

      gp.player.drawing = false
      scenePhase += 1
    if scenePhase == 1 then
      gp.player.move(0, -2)
      if gp.player.pos._2 < 18 * gp.tileSize then
        scenePhase += 1

    if scenePhase == 2 then
      found = false
      for i <- gp.enemyList(1).indices if !found do
        if gp.enemyList(gp.currentMap)(i) != null then
          if gp.enemyList(gp.currentMap)(i).name.equals(EN_KingOfDeath.Name) then
            gp.enemyList(gp.currentMap)(i).isSleeping = false
            gp.gui.npc = gp.enemyList(gp.currentMap)(i)
            scenePhase += 1
            found = true

    if scenePhase == 3 then
      gp.gui.drawDialogueScreen()

    if scenePhase == 4 then
      found = false
      for i <- gp.obj(1).indices if !found do
        if gp.obj(gp.currentMap)(i) != null && gp.obj(gp.currentMap)(i).name.equals(OBJ_PlayerDummy.Name) then
          gp.player.pos = gp.obj(gp.currentMap)(i).pos
          gp.obj(gp.currentMap)(i) = null
          gp.player.drawing = true
          found = true
      sceneNum = NA
      scenePhase = 0
      gp.gameState = PlayState
      

  def creditCutScene(): Unit =
    val scrollSpeed = 1
    val textSpacing = 50
    val centerX = gp.screenWidth / 2
    val fadeSpeed = 0.02f

    // Helper function to center text
    def drawCenteredText(text: String, y: Int): Unit =
      val metrics = g2.getFontMetrics()
      val x = centerX - metrics.stringWidth(text) / 2
      g2.drawString(text, x, y)

    if scenePhase == 0 then
      gp.stopMusic()
      phaseStartTime = System.nanoTime()
      creditY = gp.screenHeight
      fadeAlpha = 0f
      scenePhase = 1

    // Fade in phase
    if scenePhase == 1 then
      g2.setColor(new Color(0, 0, 0, fadeAlpha))
      g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight)

      fadeAlpha += fadeSpeed

      // When fully dark, move to credit scroll
      if fadeAlpha >= 0.95f then
        fadeAlpha = 0.95f
        counter += 1
        if(counter >= 60) then
          scenePhase = 2
          creditY = gp.screenHeight

    // Main credit scroll phase
    if scenePhase == 2 then
      g2.setColor(new Color(0, 0, 0, 0.9f))
      g2.fillRect(0, 0, gp.screenWidth, gp.screenHeight)

      g2.setColor(Color.WHITE)
      g2.setFont(g2.getFont.deriveFont(Font.BOLD, 48f))

      drawCenteredText("DUNGEON ADVENTURE", creditY)

      g2.setFont(g2.getFont.deriveFont(Font.PLAIN, 28f))

      val credits = Array(
        "",
        "==== PROJECT HORIZON ====",
        "A Journey of Innovation and Passion",
        "",
        "==== CREDITS ====",
        "",
        "Developed By:",
        "Tran Dang Minh Nguyen",
        "Independent Game Developer",
        "",
        "- Creative Leadership -",
        "",
        "Game Director & Lead Designer",
        "Tran Dang Minh Nguyen",
        "Transforming creative vision into interactive experiences",
        "",
        "",
        "Art Direction & Visual Design",
        "Beautiful Assets from Itch.io",
        "Carefully selected to create a unique visual narrative",
        "",
        "",
        "- Educational Influences -",
        "",
        "Mentorship & Guidance",
        "Dedicated Teaching Assistants and Head TAs",
        "Who supported and challenged my growth as a developer",
        "",
        "Learning Resources",
        "Inspirational Tutorials on YouTube",
        "",
        "- Collaborative Inspirations -",
        "",
        "Ideation & Conceptualization",
        "Collaborative brainstorming with AI assistants",
        "",
        "",
        "- Community Acknowledgments -",
        "",
        "Asset Creators on Itch.io",
        "Providing high-quality, creative resources",
        "",
        "Global Gaming Community",
        "Your passion, feedback, and support fuel creative innovation",
        "",
        "- Professional Journey -",
        "",
        "",
        "Check out the game progress on my GitHub:",
        "github.com/pipiland2612",
        "",
        "==== THANK YOU ====",
        "",
        "Every line of code is a step in my continuous learning journey.",
        "",
        "To those who inspire, challenge, and support creative technology",
        "Thank You.",
        "",
        "© " + java.time.Year.now() + " Tran Dang Minh Nguyen. All Rights Reserved.",
        ""
      )

      for i <- credits.indices do
        val y = creditY + (i * textSpacing)
        if y >= 0 && y <= gp.screenHeight then
          drawCenteredText(credits(i), y)

      // Scroll the credits upward
      creditY -= scrollSpeed

      // Check if credits have finished scrolling
      val lastTextY = creditY + (credits.length * textSpacing)
      if lastTextY < 0 then
        scenePhase = 3

      val gradientHeight = 100
      val topGradient = new GradientPaint(
        0, 0, new Color(0, 0, 0, 255),
        0, gradientHeight.toFloat, new Color(0, 0, 0, 0)
      )
      val bottomGradient = new GradientPaint(
        0, gp.screenHeight.toFloat - gradientHeight, new Color(0, 0, 0, 0),
        0, gp.screenHeight.toFloat, new Color(0, 0, 0, 255)
      )

      g2.setPaint(topGradient)
      g2.fillRect(0, 0, gp.screenWidth, gradientHeight)
      g2.setPaint(bottomGradient)
      g2.fillRect(0, gp.screenHeight - gradientHeight, gp.screenWidth, gradientHeight)

    // Cleanup phase
    if scenePhase == 3 then
      gp.stopMusic()
      gp.playMusic(0)
      sceneNum = NA
      scenePhase = 0
      gp.gameState = PlayState