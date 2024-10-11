package game

import Behavior.PathFinder
import Enemy.Enemy
import Environment.EnvironmentManager

import java.awt.image.BufferedImage
import Tile.{TileManager, Map}
import `object`.ObjectManager

import java.awt.{Color, Dimension, Font, Graphics, Graphics2D}
import javax.swing.JPanel
import entities.{Entity, Npc, Player}
import items.Projectile
import ui.UI
import utils.{CollisionChecker, Config, EventHandler, KeyHandler, Sound, Tools}

import scala.collection.mutable.ListBuffer


class GamePanel extends JPanel with Runnable:
  // ----------------------------------------------------------------------------------------------
  //Screen settings
  val originTileSize = 16
  val scale = 3
  val tileSize = originTileSize * scale
  val maxScreenColumn = 20
  val maxScreenRow = 12
  val screenWidth = maxScreenColumn * tileSize
  val screenHeight = maxScreenRow * tileSize

  // worlds settings
  val maxWorldCol = 50
  val maxWorldRow = 50
  val maxMap = 2
  var currentMap = 0
  val FPS = 60

  //----------------------------------------------------------------------------------------------
  //INITIALIZATION
  var backGroundImage: BufferedImage = Tools.loadImage("Maps/backgroundImage.png")
  this.setPreferredSize(new Dimension(screenWidth, screenHeight))
  this.setBackground(Color.BLACK)

  // SYSTEM
  val tileManager = TileManager(this)
  val keyH = KeyHandler(this)
  val sound: Sound = new Sound()
  val se: Sound = new Sound()
  val cCheck = CollisionChecker(this)
  val oManager = ObjectManager(this)
  val gui: UI = UI(this)
  val eHandler: EventHandler =  EventHandler(this)
  val config: Config = Config(this)
  var pFinder: PathFinder = PathFinder(this)
  var environmentManager = EnvironmentManager(this)
  var miniMap: Map = new Map(this)
  var gameThread: Thread = _

  //ENTITY, OBJECT, NPCS< ENEMIES
  val player = Player((tileSize * 23, tileSize * 21), this)
  val obj: Array[Array[Entity]] = Array.ofDim[Entity](maxMap, 20)
  var enemyList: Array[Array[Enemy]] = Array.ofDim[Enemy](maxMap, 10)
  var npcList : Array[Array[Npc]] = Array.ofDim[Npc](maxMap, 1)
  var projectileList: ListBuffer[Projectile] = ListBuffer[Projectile]()
  var entityList: ListBuffer[Entity] = ListBuffer[Entity]()

  //GAME STATE
  var gameState: GameState = GameState.TitleState

  //----------------------------------------------------------------------------------------------
  // SET UP METHODS
  def startGameThread(): Unit =
    gameThread = new Thread(this)
    gameThread.start()

  def setUpGame (): Unit =
    oManager.setObject()
    oManager.setEnemy()
    oManager.setNpc()
    environmentManager.setup()
    playMusic(0)

  def retry(): Unit =
    player.isInvinc = false
    player.reset()
    setUpGame()

  def restart(): Unit =
    player.isInvinc = false
    player.reset()
    player.setItems()
    setUpGame()

  //----------------------------------------------------------------------------------------------
  // MUSICS SOUNDS methods
  def playMusic (int : Int) = this.sound.setFile(int); this.sound.play(); this.sound.loop()
  def stopMusic (): Unit = this.sound.stop()
  def playSE (int : Int) = this.se.setFile(int); this.se.play()

  // ----------------------------------------------------------------------------------------------
  // Game Loop Update
  def update(): Unit =
    if gameState == GameState.PlayState then
      this.player.update()

      for i <- npcList(1).indices do
        if npcList(currentMap)(i) != null then
          npcList(currentMap)(i).update()

      for i <- enemyList(1).indices do
        val currentEnemy = enemyList(currentMap)(i)
        if currentEnemy != null then
          currentEnemy.update()
          if currentEnemy.dying then
            currentEnemy.checkDrop()
            enemyList(currentMap)(i) = null

      for projectile <- projectileList do
        if projectile != null then
          projectile.update()

      environmentManager.update()

    else if gameState == GameState.PauseState then {}

  // RENDERING METHODS
  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)
    val g2d = g.asInstanceOf[Graphics2D]

    var startTime: Long = 0
    if(keyH.showDebugText) then
      startTime = System.nanoTime()

    //BACKGROUND
    if backGroundImage != null then
      g2d.drawImage(backGroundImage, -10, -10, 1792, 1024, null)

    if gameState == GameState.TitleState then
      gui.drawUI(g2d)
    else if gameState == GameState.MapState then
      miniMap.drawFullMapScreen(g2d)
    else
      //TILE
      tileManager.drawTiles(g2d)

      // ADDING ENTITIES
      entityList += player

      // ADDING OBJs, enemy, projectile, NPCS
      for i <- npcList(1).indices do
        if npcList(currentMap)(i) != null then
          entityList += npcList(currentMap)(i)

      for i <- obj(1).indices do
        if obj(currentMap)(i) != null then
          entityList += obj(currentMap)(i)

      for i <- enemyList(1).indices do
        if enemyList(currentMap)(i) != null then
          entityList += enemyList(currentMap)(i)

      for (projectile <- projectileList) do
        if projectile != null then
          entityList += projectile
      // SORT LIST
      entityList = entityList.sortBy(entity => entity.getPosition._2 + entity.solidAreaDefaultY)
      //DRAW
      for entity <- entityList do
        entity.draw(g2d)
      //EMPTY LIST
      entityList.clear()
      Tools.renderDebugInfo (g2d, player, obj, enemyList, this)
      // ENVIRONMENT
//      environmentManager.draw(g2d)
      miniMap.drawMiniMap(g2d)
      //UI
      gui.drawUI(g2d)

//      Debug
      if(keyH.showDebugText) then
        val endTime = System.nanoTime()
        val passTime = endTime - startTime
        g2d.setFont(new Font("Arial",Font.PLAIN, 20))
        g2d.setColor(Color.WHITE)
        val x = 10
        var y = 400
        var lineHeight = 20

        g2d.drawString("Position x: " + player.getPosition._1, x,y);y += lineHeight
        g2d.drawString("Position y: " + player.getPosition._2, x,y);y += lineHeight
        g2d.drawString("Row: " + (player.getPosition._1 + player.solidArea.x) / tileSize, x,y);y += lineHeight
        g2d.drawString("Col: " + (player.getPosition._2 + player.solidArea.y) / tileSize, x,y);y += lineHeight
        g2d.drawString("Draw time: " + passTime, x,y)

    g2d.dispose()

  // ----------------------------------------------------------------------------------------------
  // GAME LOOP
  override def run(): Unit =
    val drawInterval: Double = 1e9 / FPS
    var delta: Double = 0
    var lastTime: Long = System.nanoTime()
    var currentTime: Long = 0
    var timer: Long = 0
    var drawCount: Int = 0

    while (gameThread != null) do
      currentTime = System.nanoTime()
      delta += (currentTime - lastTime) / drawInterval
      timer += (currentTime - lastTime)
      lastTime = currentTime

      if delta >= 1 then
        update()
        repaint()
        delta -= 1
        drawCount += 1

//      if timer >= 1e9 then
//        drawCount = 0
//        timer = 0