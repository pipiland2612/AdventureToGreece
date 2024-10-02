package utils

import Enemy.Enemy
import entities.Direction.ANY
import entities.{Creatures, Direction}
import game.GamePanel

class CollisionChecker (var gp: GamePanel) :

  private def adjustSolidArea (entity: Creatures): Unit =
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

    for (i <- gp.obj.indices) do
      if gp.obj(i) != null then
        Tools.updateSolidArea(entity)
        gp.obj(i).solidArea.x = gp.obj(i).pos._1 + gp.obj(i).solidAreaDefaultX
        gp.obj(i).solidArea.y = gp.obj(i).pos._2 + gp.obj(i).solidAreaDefaultY

        adjustSolidArea(entity)
        if (entity.solidArea.intersects(gp.obj(i).solidArea)) then
          if(gp.obj(i).collision) then
            entity.isCollided = true

          if(isPlayer) then
            index = i

        Tools.resetSolidArea(entity)
        Tools.resetSolidArea(gp.obj(i))

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
        tile1 = tileManager.mapTileNum(entityTopRow)(entityLeftCol)
        tile2 = tileManager.mapTileNum(entityTopRow)(entityRightCol)

      case Direction.DOWN =>
        entityBottomRow = (entityBottomWorldY + speed) / tileSize
        tile1 = tileManager.mapTileNum(entityBottomRow)(entityLeftCol)
        tile2 = tileManager.mapTileNum(entityBottomRow)(entityRightCol)

      case Direction.LEFT =>
        entityLeftCol = (entityLeftWorldX - speed) / tileSize
        tile1 = tileManager.mapTileNum(entityBottomRow)(entityLeftCol)
        tile2 = tileManager.mapTileNum(entityTopRow)(entityLeftCol)

      case Direction.RIGHT =>
        entityRightCol = (entityRightWorldX + speed) / tileSize
        tile1 = tileManager.mapTileNum(entityBottomRow)(entityRightCol)
        tile2 = tileManager.mapTileNum(entityTopRow)(entityRightCol)
      case ANY =>
      case null =>

    if tileManager.tile(tile1).collision || tileManager.tile(tile2).collision then
      entity.isCollided = true


  // For npc and monster
  def checkCollisionWithTargets (entity: Creatures, target: Array[Enemy]) =
    var index = -1
    for (i <- target.indices) do
      if target(i) != null then

        Tools.updateSolidArea(entity)
        Tools.updateSolidArea(target(i))

        adjustSolidArea(entity)

        if (entity.solidArea.intersects(target(i).solidArea)) then
          if target(i) != entity then
            index = i
            entity.isCollided = true

        entity.solidArea.x = entity.solidAreaDefaultX
        entity.solidArea.y = entity.solidAreaDefaultY
        target(i).solidArea.x = target(i).solidAreaDefaultX
        target(i).solidArea.y = target(i).solidAreaDefaultY

    index

  def checkPlayer(entity: Creatures): Boolean =
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

