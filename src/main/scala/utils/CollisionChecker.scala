package utils

import Enemy.Enemy
import entities.Direction.ANY
import entities.{Creatures, Direction, Entity}
import game.GamePanel

import java.awt.Rectangle

class CollisionChecker (var gp: GamePanel) :

  private def adjustSolidArea (entity: Entity): Unit =
    entity.direction match
      case Direction.UP =>
        entity.solidArea.y -= entity.speed
      case Direction.DOWN =>
        entity.solidArea.y += entity.speed
      case Direction.LEFT =>
        entity.solidArea.x -= entity.speed
      case Direction.RIGHT =>
        entity.solidArea.x += entity.speed
      case ANY => 

  def checkObjectCollision (entity: Creatures, isPlayer: Boolean): Int =
    var index = -1

    for (i <- gp.obj(1).indices) do
      if gp.obj(gp.currentMap)(i) != null then
        val currentObj =  gp.obj(gp.currentMap)(i)
        Tools.updateSolidArea(entity)
        currentObj.solidArea.x = currentObj.pos._1 + currentObj.solidArea.x
        currentObj.solidArea.y = currentObj.pos._2 + currentObj.solidArea.y

        adjustSolidArea(entity)
        if (entity.solidArea.intersects(currentObj.solidArea)) then
          if(currentObj.collision) then
            entity.isCollided = true

          if(isPlayer) then
            index = i

        Tools.resetSolidArea(entity)
        Tools.resetSolidArea(currentObj)

    index

  def checkTileCollision (entity: Creatures): Unit =
    val (x, y) = entity.getPosition
    val solidArea = entity.solidArea
    val tileSize = gp.tileSize
    val speed = entity.speed
    val tileManager = gp.tileManager

    val entityLeftWorldX = x + solidArea.x
    val entityRightWorldX = x + solidArea.x + solidArea.width
    val entityTopWorldY = y + solidArea.y
    val entityBottomWorldY = y + solidArea.y + solidArea.height

    var entityLeftCol = entityLeftWorldX / tileSize
    var entityRightCol = entityRightWorldX / tileSize
    var entityTopRow = entityTopWorldY / tileSize
    var entityBottomRow = entityBottomWorldY / tileSize

    var tile1, tile2: Int = 0

    entity.direction match
      case Direction.UP =>
        entityTopRow = (entityTopWorldY - speed) / tileSize
        tile1 = tileManager.mapTileNum(gp.currentMap)(entityTopRow)(entityLeftCol)
        tile2 = tileManager.mapTileNum(gp.currentMap)(entityTopRow)(entityRightCol)

      case Direction.DOWN =>
        entityBottomRow = (entityBottomWorldY + speed) / tileSize
        tile1 = tileManager.mapTileNum(gp.currentMap)(entityBottomRow)(entityLeftCol)
        tile2 = tileManager.mapTileNum(gp.currentMap)(entityBottomRow)(entityRightCol)

      case Direction.LEFT =>
        entityLeftCol = (entityLeftWorldX - speed) / tileSize
        tile1 = tileManager.mapTileNum(gp.currentMap)(entityBottomRow)(entityLeftCol)
        tile2 = tileManager.mapTileNum(gp.currentMap)(entityTopRow)(entityLeftCol)

      case Direction.RIGHT =>
        entityRightCol = (entityRightWorldX + speed) / tileSize
        tile1 = tileManager.mapTileNum(gp.currentMap)(entityBottomRow)(entityRightCol)
        tile2 = tileManager.mapTileNum(gp.currentMap)(entityTopRow)(entityRightCol)
      case ANY =>
      case null =>

    if tileManager.tile(tile1).collision || tileManager.tile(tile2).collision then
      entity.isCollided = true


  // For npc and monster
  def checkCollisionWithTargets[T <: Creatures](entity: Entity, target: Array[Array[T]]) =
    var index = -1
    for (i <- target(1).indices) do
      if target(gp.currentMap)(i) != null then
        val currentTarget = target(gp.currentMap)(i)
        Tools.updateSolidArea(entity)
        Tools.updateSolidArea(currentTarget)

        adjustSolidArea(entity)

        if (entity.solidArea.intersects(currentTarget.solidArea)) then
          if currentTarget != entity then
            index = i
            entity.isCollided = true

        Tools.resetSolidArea(entity)
        Tools.resetSolidArea(currentTarget)

    index

  def checkCollisionWithTargetsHitBox (entity: Entity, target: Array[Array[Enemy]]) =
    var index = -1
    for (i <- target(1).indices) do
      if target(gp.currentMap)(i) != null then
        val currentTarget = target(gp.currentMap)(i)
        Tools.updateSolidArea(entity)
        Tools.updateAreaHitBox(currentTarget)

        adjustSolidArea(entity)

        if (entity.solidArea.intersects(currentTarget.areaHitBox)) then
          if currentTarget != entity then
            index = i

        Tools.resetSolidArea(entity)
        Tools.resetAreaHitBox(currentTarget)

    index

  def checkPlayer(entity: Entity): Boolean =
    var touchedPlayer: Boolean = false

    Tools.updateSolidArea(entity)
    Tools.updateSolidArea(gp.player)

    adjustSolidArea(entity)

    if (entity.solidArea.intersects(gp.player.solidArea)) then
        entity.isCollided = true
        touchedPlayer = true

    Tools.resetSolidArea(entity)
    Tools.resetSolidArea(gp.player)
    touchedPlayer
    
end CollisionChecker

