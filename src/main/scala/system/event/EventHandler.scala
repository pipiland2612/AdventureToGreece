package system.event

import entities.Entity
import entities.creatures.{Direction, Npc}
import game.{GamePanel, GameProgress, GameState}
import system.environment.Area
import utils.Tools

import java.awt.Rectangle

class EventMaster(gp: GamePanel) extends Entity(gp):
  var name = ""
  var pos = (0, 0)
  var solidArea: Rectangle = Rectangle(0, 0, 0, 0)

class EventHandler(var gp: GamePanel):
  val eventRect: Array[Array[Array[EventRect]]] = Array.ofDim[EventRect](gp.maxMap, gp.maxWorldRow, gp.maxWorldCol)
  var previousEventX, previousEventY: Int = _
  var canCauseEvent: Boolean = true
  var counter = 60
  var tempMap, tempCol, tempRow: Int = _
  var eventMaster = new EventMaster(gp)
  var lastTriggeredEvent: Option[EventRect] = None

  // Initialize dialogues
  def setDialogue(): Unit =
    eventMaster.dialogues(0)(0) = "You have been trapped"
    eventMaster.dialogues(1)(0) = "Heal"
    eventMaster.dialogues(2)(0) = "Save Game"
  setDialogue()

  // Load event rectangles
  def loadEventRect(): Unit =
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

  // Check if player has triggered an event
  def hasHit(map: Int, eventRow: Int, eventCol: Int, direction: Direction): Boolean =
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

  // Event actions
  def damagePit(): Unit =
    eventMaster.startDialogue(eventMaster, 0)
    gp.player.takeDamage(20)
    canCauseEvent = false
    counter = 0

  def heal(): Unit =
    eventMaster.startDialogue(eventMaster, 1)
    gp.player.health = Math.min(gp.player.health + 10, gp.player.maxHealth)

  def teleport(map: Int, col: Int, row: Int, area: Area): Unit =
    gp.gameState = GameState.TransitionState
    gp.nextArea = area
    tempMap = map
    tempCol = col
    tempRow = row
    canCauseEvent = false
    counter = 0

  def saveGame(): Unit =
    if gp.keyH.enterPressed then
      eventMaster.startDialogue(eventMaster, 2)
      gp.saveLoad.save()

  // Game loop event check
  def checkEvent(): Unit =
    val xDistance = Math.abs(gp.player.getPosition._1 - previousEventX)
    val yDistance = Math.abs(gp.player.getPosition._2 - previousEventY)
    val distance = Math.max(xDistance, yDistance)
    if counter < 60 then counter += 1

    if distance > gp.tileSize || counter >= 60 then
      canCauseEvent = true
      lastTriggeredEvent.foreach(event => event.hasHappened = false)

    if canCauseEvent then
      if hasHit(0, 6, 1, Direction.ANY) then teleport(1, 4, 3, Area.Dungeon) // To Dungeon 1
      else if hasHit(1, 4, 3, Direction.ANY) then teleport(0, 6, 1, Area.OverWorld) // Back to OverWorld
      else if hasHit(1, 47, 33, Direction.ANY) then teleport(2, 4, 44, Area.Dungeon) // TO FINAL BOSS (DUNGEON 2)
      else if hasHit(2, 4, 44, Direction.ANY) then teleport(1, 47, 33, Area.Dungeon) // TO DUNGEON 1
      else if hasHit(2, 23, 32, Direction.ANY) then kingOfDeath() // CutScene
      else if hasHit(2, 24, 32, Direction.ANY) then kingOfDeath() // CutScene
      else if hasHit(2, 25, 32, Direction.ANY) then kingOfDeath() // CutScene
      else if hasHit(2, 23, 31, Direction.ANY) then kingOfDeath() // CutScene
      else if hasHit(2, 24, 31, Direction.ANY) then kingOfDeath() // CutScene
      else if hasHit(2, 25, 31, Direction.ANY) then kingOfDeath() // CutScene
      else if hasHit(2, 23, 30, Direction.ANY) then kingOfDeath() // CutScene
      else if hasHit(2, 24, 30, Direction.ANY) then kingOfDeath() // CutScene
      else if hasHit(2, 25, 30, Direction.ANY) then kingOfDeath() // CutScene

  // NPC interaction
  def speak(npc: Npc): Unit =
    if gp.keyH.enterPressed then
      gp.gameState = GameState.DialogueState
      npc.speak()

  def kingOfDeath(): Unit =
    if !gp.onBossBattle && !GameProgress.KingOfDeathDefeated then
      gp.gameState = GameState.CutSceneState
      gp.cutSceneManager.sceneNum = gp.cutSceneManager.kingOfDeath

end EventHandler
