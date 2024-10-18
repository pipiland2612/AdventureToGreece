package utils

import Enemy.Enemy
import entities.Direction.ANY
import entities.{Creatures, Direction, Entity}
import game.GamePanel

import java.awt.Rectangle

class CollisionChecker(var gp: GamePanel):

  // Adjust solid area based on direction
  private def adjustSolidArea(entity: Entity): Unit =
    entity.direction match
      case Direction.UP    => entity.solidArea.y -= entity.speed
      case Direction.DOWN  => entity.solidArea.y += entity.speed
      case Direction.LEFT  => entity.solidArea.x -= entity.speed
      case Direction.RIGHT => entity.solidArea.x += entity.speed
      case ANY             =>

  // Adjust hitbox area based on direction
  private def adjustAreaHitBox(entity: Entity): Unit =
    entity.direction match
      case Direction.UP    => entity.areaHitBox.y -= entity.speed
      case Direction.DOWN  => entity.areaHitBox.y += entity.speed
      case Direction.LEFT  => entity.areaHitBox.x -= entity.speed
      case Direction.RIGHT => entity.areaHitBox.x += entity.speed
      case ANY             =>

  // Check object collision for creatures
  def checkObjectCollision(entity: Creatures, isPlayer: Boolean): Int =
    var index = -1
    for (i <- gp.obj(1).indices) do
      if gp.obj(gp.currentMap)(i) != null then
        val currentObj = gp.obj(gp.currentMap)(i)
        Tools.updateSolidArea(entity)
        currentObj.solidArea.x = currentObj.pos._1 + currentObj.solidArea.x
        currentObj.solidArea.y = currentObj.pos._2 + currentObj.solidArea.y

        adjustSolidArea(entity)
        if entity.solidArea.intersects(currentObj.solidArea) then
          if currentObj.collision then
            entity.isCollided = true
          if isPlayer then
            index = i

        Tools.resetSolidArea(entity)
        Tools.resetSolidArea(currentObj)
    index

  // Check tile collision for creatures
  def checkTileCollision(entity: Creatures): Unit =
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
      case Direction.UP    =>
        entityTopRow = (entityTopWorldY - speed) / tileSize
        tile1 = tileManager.mapTileNum(gp.currentMap)(entityTopRow)(entityLeftCol)
        tile2 = tileManager.mapTileNum(gp.currentMap)(entityTopRow)(entityRightCol)

      case Direction.DOWN  =>
        entityBottomRow = (entityBottomWorldY + speed) / tileSize
        tile1 = tileManager.mapTileNum(gp.currentMap)(entityBottomRow)(entityLeftCol)
        tile2 = tileManager.mapTileNum(gp.currentMap)(entityBottomRow)(entityRightCol)

      case Direction.LEFT  =>
        entityLeftCol = (entityLeftWorldX - speed) / tileSize
        tile1 = tileManager.mapTileNum(gp.currentMap)(entityBottomRow)(entityLeftCol)
        tile2 = tileManager.mapTileNum(gp.currentMap)(entityTopRow)(entityLeftCol)

      case Direction.RIGHT =>
        entityRightCol = (entityRightWorldX + speed) / tileSize
        tile1 = tileManager.mapTileNum(gp.currentMap)(entityBottomRow)(entityRightCol)
        tile2 = tileManager.mapTileNum(gp.currentMap)(entityTopRow)(entityRightCol)
      case ANY             =>

    if tileManager.tile(tile1).collision || tileManager.tile(tile2).collision then
      entity.isCollided = true

  // General collision check for any target
  def checkCollision[T <: Creatures](entity: Entity, target: Array[Array[T]], updateArea: Entity => Unit, resetArea: Entity => Unit, adjustArea: Entity => Unit, getArea: Entity => Rectangle): Int =
    var index = -1
    for i <- target(1).indices do
      if target(gp.currentMap)(i) != null then
        val currentTarget = target(gp.currentMap)(i)
        updateArea(entity)
        updateArea(currentTarget)

        adjustArea(entity)

        if getArea(entity).intersects(getArea(currentTarget)) then
          if currentTarget != entity then
            index = i
            entity.isCollided = true

        resetArea(entity)
        resetArea(currentTarget)
    index

  // Collision check with NPCs or monsters
  def checkCollisionWithTargets[T <: Creatures](entity: Entity, target: Array[Array[T]]): Int =
    checkCollision(entity, target, Tools.updateSolidArea, Tools.resetSolidArea, adjustSolidArea, _.solidArea)

  def checkCollisionWithTargetsHitBox(entity: Entity, target: Array[Array[Enemy]]): Int =
    checkCollision(entity, target, Tools.updateAreaHitBox, Tools.resetAreaHitBox, adjustAreaHitBox, _.areaHitBox)

  // Collision check with player
  def checkCollisionWithPlayer(entity: Entity, updateArea: Entity => Unit, resetArea: Entity => Unit, adjustArea: Entity => Unit, getArea: Entity => Rectangle): Boolean =
    var touchedPlayer = false
    updateArea(entity)
    updateArea(gp.player)

    adjustArea(entity)
    if getArea(entity).intersects(getArea(gp.player)) then
      entity.isCollided = true
      touchedPlayer = true

    resetArea(entity)
    resetArea(gp.player)
    touchedPlayer

  def checkPlayer(entity: Entity): Boolean =
    checkCollisionWithPlayer(entity, Tools.updateSolidArea, Tools.resetSolidArea, adjustSolidArea, _.solidArea)

  def checkPlayerTargetHitBox(entity: Entity): Boolean =
    checkCollisionWithPlayer(entity, Tools.updateAreaHitBox, Tools.resetAreaHitBox, adjustAreaHitBox, _.areaHitBox)

  // Collision check with other hitbox
  def checkCollisionWithOtherHitBox[T <: Creatures](entity: Entity, target: Array[Array[T]], hitBox: Rectangle): Int =
    var index = -1
    for i <- target(1).indices do
      if target(gp.currentMap)(i) != null then
        val currentTarget = target(gp.currentMap)(i)
        Tools.updateAreaHitBox(currentTarget)

        if hitBox.intersects(currentTarget.areaHitBox) then
          if currentTarget != entity then
            index = i
            entity.isCollided = true

        Tools.resetAreaHitBox(currentTarget)
    index

  // Collision check between player and other hitbox
  def checkPlayerWithOtherHitBox(entity: Entity, hitBox: Rectangle): Boolean =
    var touchedPlayer = false
    Tools.updateAreaHitBox(gp.player)

    if hitBox.intersects(gp.player.areaHitBox) then
      entity.isCollided = true
      touchedPlayer = true

    Tools.resetAreaHitBox(gp.player)
    touchedPlayer

end CollisionChecker
