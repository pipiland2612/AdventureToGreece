package utils

import Environment.Area
import entities.{Direction, Entity, Npc}
import game.GameState.DialogueState
import game.{GamePanel, GameState}

import java.awt.Rectangle

class EventMaster(gp: GamePanel) extends Entity(gp):
  var name = ""
  var pos = (0,0)
  var solidArea: Rectangle = Rectangle(0,0,0,0)

class EventHandler (var gp: GamePanel) :
  val eventRect: Array[Array[Array[EventRect]]] = Array.ofDim[EventRect](gp.maxMap, gp.maxWorldRow, gp.maxWorldCol)
  var previousEventX, previousEventY : Int = _
  var canCauseEvent: Boolean = true
  var counter = 60
  var tempMap, tempCol, tempRow : Int = _
  var eventMaster = new EventMaster(gp)
  var lastTriggeredEvent: Option[EventRect] = None

  def setDialogue(): Unit =
    eventMaster.dialogues(0)(0) = "You have been trapped"
    eventMaster.dialogues(1)(0) = "Heal"
    eventMaster.dialogues(2)(0) = "Save Game"

  def loadEventRect (): Unit =
    for
      map <- 0 until gp.maxMap
      row <- 0 until gp.maxWorldRow
      col <- 0 until gp.maxWorldCol
    do
      eventRect(map)(row)(col) = new EventRect()
      val thisEventRect = eventRect(map)(row)(col)
      thisEventRect.x = 23
      thisEventRect.y = 23
      thisEventRect.width = 2
      thisEventRect.height = 2
      thisEventRect.eventRectDefaultX = thisEventRect.x
      thisEventRect.eventRectDefaultY = thisEventRect.y

  loadEventRect()

  def hasHit (map: Int, eventRow: Int, eventCol : Int, direction: Direction): Boolean =
    var hasHit = false

    if map == gp.currentMap then
      val thisEventRect = eventRect(map)(eventRow)(eventCol)

      Tools.updateSolidArea(gp.player)
      thisEventRect.x = eventRow * gp.tileSize + thisEventRect.x
      thisEventRect.y = eventCol * gp.tileSize + thisEventRect.y


      if gp.player.solidArea.intersects(thisEventRect) && !thisEventRect.hasHappened then
        if gp.player.direction == direction || direction == Direction.ANY then
          hasHit = true

          previousEventX = gp.player.getPosition._1
          previousEventY = gp.player.getPosition._2
          counter = 0

          thisEventRect.hasHappened = true
          this.lastTriggeredEvent = Some(thisEventRect)

      Tools.resetSolidArea(gp.player)
      thisEventRect.x = thisEventRect.eventRectDefaultX
      thisEventRect.y = thisEventRect.eventRectDefaultY

    hasHit
  // Event heal, damage, etc..
  def damagePit (gameState : GameState) =
    gp.gameState = gameState
    eventMaster.startDialoque(eventMaster, 0)
    gp.player.takeDamage(20)
    canCauseEvent = false
    counter = 0

  def heal (gameState : GameState): Unit =
    gp.gameState = gameState
    eventMaster.startDialoque(eventMaster, 1)
    gp.player.health = Math.min(gp.player.health + 10, gp.player.maxHealth)

  def teleport(map: Int, row: Int, col: Int, area: Area): Unit =
    gp.gameState = GameState.TransitionState
    gp.nextArea = area
    tempMap = map
    tempRow = row
    tempCol = col

    canCauseEvent = false
    counter = 0


  def saveGame(gameState : GameState): Unit =
    if gp.keyH.enterPressed then
      gp.gameState = gameState
      eventMaster.startDialoque(eventMaster, 2)
      gp.saveLoad.save()

  // Call by the game loop
  def checkEvent (): Unit =
    val xDistance = Math.abs(gp.player.getPosition._1 - previousEventX)
    val yDistance = Math.abs(gp.player.getPosition._2 - previousEventY)
    val distance = Math.max(xDistance, yDistance)
    if counter < 60 then counter += 1

    if distance > gp.tileSize || counter >= 60 then
      canCauseEvent = true

      lastTriggeredEvent.foreach(event => event.hasHappened = false)

    if canCauseEvent then
      if hasHit(0, 48, 48, Direction.ANY) then damagePit(DialogueState)
      else if hasHit(0, 10 ,20, Direction.ANY) then heal(DialogueState)
      else if hasHit(0, 37, 25, Direction.ANY) then teleport(1, 4 ,3, Area.Dungeon) // To DUNGEON
      else if hasHit(1, 4, 3, Direction.ANY) then teleport(0, 37 ,25, Area.OverWorld) // Back to Over World

  def speak(npc : Npc): Unit =
    if gp.keyH.enterPressed then
      gp.gameState = GameState.DialogueState
      npc.speak()


end EventHandler


