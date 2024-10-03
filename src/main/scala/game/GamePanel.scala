package game

import Enemy.Enemy

import java.awt.image.BufferedImage
import Tile.TileManager
import `object`.ObjectManager

import java.awt.{Color, Dimension, Graphics, Graphics2D}
import javax.swing.JPanel
import entities.{Creatures, Entity, Player}
import ui.UI
import utils.{CollisionChecker, EventHandler, KeyHandler, Sound, Tools}

import scala.collection.mutable.ListBuffer


class GamePanel extends JPanel with Runnable:
  //screen settings
  val originTileSize = 16
  val scale = 3
  val tileSize = originTileSize * scale
  val maxScreenColumn = 16
  val maxScreenRow = 12
  val screenWidth = maxScreenColumn * tileSize
  val screenHeight = maxScreenRow * tileSize


  // worlds settings
  val maxWorldCol = 50
  val maxWorldRow = 50

  val FPS = 60
  //initialize
  var backGroundImage: BufferedImage = Tools.loadImage("/Users/batman/Desktop/Adventure to Greece/src/main/resources/images/Maps/backgroundImage.png")
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
  var gameThread: Thread = _

  //ENTITY, OBJECT
  val player = Player((tileSize * 23, tileSize * 21), this)
  val obj = new Array[Entity](10)
  //  var npc
  var entityList: ListBuffer[Entity] = ListBuffer[Entity]()
  var enemyList: Array[Enemy] = new Array[Enemy](10)

  //GAME STate
  var gameState: GameState = GameState.TitleState

  def startGameThread(): Unit =
    gameThread = new Thread(this)
    gameThread.start()

  def renderDebugInfo(g: Graphics2D, entity: Entity, objects: Array[Entity], creatures: Array[Enemy]): Unit =
    g.setColor(Color.RED)
    g.drawRect(entity.solidArea.x + player.screenX , entity.solidArea.y + player.screenY, entity.solidArea.width, entity.solidArea.height)

    objects.foreach ( obj =>
      if (obj != null) then
        g.setColor(Color.BLUE)
        g.drawRect(
          obj.solidArea.x + obj.pos._1 - entity.getPosition._1 + player.screenX,
          obj.solidArea.y + obj.pos._2 - entity.getPosition._2 + player.screenY,
          obj.solidArea.width, obj.solidArea.height)
    )

    creatures.foreach (creature =>
      if creature != null then
        g.setColor(Color.YELLOW)
        g.drawRect(
          creature.solidArea.x + creature.pos._1 - entity.getPosition._1 + player.screenX,
          creature.solidArea.y + creature.pos._2 - entity.getPosition._2 + player.screenY,
          creature.solidArea.width, creature.solidArea.height)
    )



  def setUpGame (): Unit =
    oManager.setObject()
    oManager.setEnemy()
    playMusic(0)

  // call by the game loop
  def update(): Unit =
    if gameState == GameState.PlayState then
      this.player.update()

      for enemy <- enemyList do
        if enemy != null then
          enemy.update()
//          if enemy.dying then


    else if gameState == GameState.PauseState then {}

  def playMusic (int : Int) = this.sound.setFile(int); this.sound.play(); this.sound.loop()
  def stopMusic (): Unit = this.sound.stop()
  def playSE (int : Int) = this.se.setFile(int); this.se.play()

  // call by the game loop
  override def paintComponent(g: Graphics): Unit =
    super.paintComponent(g)
    val g2d = g.asInstanceOf[Graphics2D]

    var startTime: Long = 0
    if(keyH.toggle) then
      startTime = System.nanoTime()

    //BACKGROUND
    if backGroundImage != null then
      g2d.drawImage(backGroundImage, -10, -10, 1792, 1024, null)

    if gameState == GameState.TitleState then
      gui.drawUI(g2d)
    else
      //TILE
      tileManager.drawTiles(g2d)


      // ADDING ENTITIES
      entityList += player

      //add npc using for loop ...
      for (ob <- obj) do
        if ob != null then
          entityList += ob

      for (enemy <- enemyList) do
        if enemy != null then
          entityList += enemy

      entityList = entityList.sortBy(entity => entity.getPosition._2 + entity.solidAreaDefaultY)

      for entity <- entityList do
        entity.draw(g2d)

      //EMPTY LIST
      entityList.clear()
      renderDebugInfo (g2d, player, obj, enemyList)

      //UI
      gui.drawUI(g2d)

//      Debug
      if(keyH.toggle) then
        val endTime = System.nanoTime()
        val passTime = endTime - startTime
        g2d.setColor(Color.WHITE)
        g2d.drawString("Draw time: " + passTime, 10,100)
        println(passTime)

    g2d.dispose()

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