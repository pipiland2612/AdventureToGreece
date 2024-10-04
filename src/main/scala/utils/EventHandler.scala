package utils

import entities.Direction
import game.GameState.DialogueState
import game.{GamePanel, GameState}

class EventHandler (var gp: GamePanel) :
  val eventRect: Array[Array[EventRect]] = Array.ofDim[EventRect](gp.maxWorldRow, gp.maxWorldCol)
  var previousEventX, previousEventY : Int = _
  var canCauseEvent: Boolean = true
  var counter = 60

  def loadEventRect (): Unit =
    for row <- 0 until gp.maxWorldRow
        col <- 0 until gp.maxWorldCol
    do
      eventRect(row)(col) = new EventRect()
      val thisEventRect = eventRect(row)(col)
      thisEventRect.x = 23
      thisEventRect.y = 23
      thisEventRect.width = 2
      thisEventRect.height = 2
      thisEventRect.eventRectDefaultX = thisEventRect.x
      thisEventRect.eventRectDefaultY = thisEventRect.y
  loadEventRect()

  def hasHit (eventRow: Int, eventCol : Int, direction: Direction): Boolean =
    var hasHit = false
    val thisEventRect = eventRect(eventRow)(eventCol)
    Tools.updateSolidArea(gp.player)
    thisEventRect.x = eventCol * gp.tileSize + thisEventRect.x
    thisEventRect.y = eventRow * gp.tileSize + thisEventRect.y

    if gp.player.solidArea.intersects(thisEventRect) && !thisEventRect.hasHappened then
      if gp.player.direction == direction || direction == Direction.ANY then
        hasHit = true

        previousEventX = gp.player.getPosition._1
        previousEventY = gp.player.getPosition._2
        counter = 0

    Tools.resetSolidArea(gp.player)
    thisEventRect.x = thisEventRect.eventRectDefaultX
    thisEventRect.y = thisEventRect.eventRectDefaultY

    hasHit
  // Event heal, damage, etc..
  def damagePit (row: Int, col: Int, gameState : GameState) =
    gp.gameState = gameState
    gp.gui.currentDialogue = "You have been trapped"
    gp.player.takeDamage(20)
    canCauseEvent = false
    counter = 0

  def heal (row: Int, col: Int, gameState : GameState): Unit =
    gp.gameState = gameState
    gp.gui.currentDialogue = "Heal"
    gp.player.health = Math.min(gp.player.health + 10, gp.player.maxHealth)


  // Call by the game loop
  def checkEvent (): Unit =
    val xDistance = Math.abs(gp.player.getPosition._1 - previousEventX)
    val yDistance = Math.abs(gp.player.getPosition._2 - previousEventY)
    val distance = Math.max(xDistance, yDistance)
    if counter < 60 then counter += 1

    if distance > gp.tileSize || counter >= 60 then
      canCauseEvent = true

    if canCauseEvent then
      if hasHit(48, 48, Direction.ANY) then damagePit(48, 48,DialogueState)
      if hasHit(10 ,20, Direction.ANY) then heal(10, 20, DialogueState)

end EventHandler


