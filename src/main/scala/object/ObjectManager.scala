package `object`

import Enemy.EN_Necromancer
import `object`.ObjectType.{OBJ_BronzeCoin, OBJ_Chest, OBJ_NormalAxe, OBJ_NormalHealFlask, OBJ_NormalSword}
import entities.{Entity, Merchant, Socerer}
import game.GamePanel
import items.InteractiveObjects


class ObjectManager(var gp : GamePanel) :
  val tileSize = gp.tileSize

  private def calcPos(col: Int, row: Int): (Int, Int) = (col * tileSize, row * tileSize)
  private def loadEntity[T <: Entity](list: List[(T, Int, Some[(Int, Int)])], currentList: Array[T]): Unit =
    list.foreach { case (entity, index, positionOpt) =>
      currentList(index) = entity
      positionOpt.foreach(pos => currentList(index).pos = pos)
    }

  def setObject(): Unit =

    val objectData0 = List(
      // OBJ                                   INDEX   POSITION     (COL, ROW)
      (new OBJ_Chest(gp),                       0,     Some(calcPos (35, 25))),
      (new OBJ_Rock(gp),                        1,     Some(calcPos (10, 15))),
      (new OBJ_Tree(gp),                        2,     Some(calcPos (10, 20))),
      (new OBJ_NormalHealFlask(gp),             3,     Some(calcPos (9,  9))),
      (new OBJ_NormalSword(gp),                 4,     Some(calcPos (10, 9))),
      (new OBJ_NormalAxe(gp),                   5,     Some(calcPos (11, 9))),
      (new OBJ_BronzeCoin(gp),                  6,     Some(calcPos (11, 10))),
      (new OBJ_NormalHealFlask(gp),             7,     Some(calcPos (10, 9))),
      (new OBJ_NormalHealFlask(gp),             8,     Some(calcPos (11, 9))),
      (new OBJ_NormalHealFlask(gp),             9,     Some(calcPos (12, 9))),
      (new OBJ_LightCandle(gp),                 10,    Some(calcPos (24, 21))),
      (new OBJ_CheckPoint(gp),                  11,    Some(calcPos (25, 21))),
      (new OBJ_DungeonGate(gp),                 12,    Some(calcPos (37, 25)))
    )
    this.loadEntity(objectData0, gp.obj(0))
    objectData0.head._1.asInstanceOf[InteractiveObjects].setLoot(new OBJ_BronzeCoin(gp))

    val objectData1 = List (
      (new OBJ_DungeonGate(gp),                 0,     Some(calcPos (4, 3))),
      (new OBJ_CheckPoint(gp),                  1,     Some(calcPos (6, 3)))
    )
    this.loadEntity(objectData1, gp.obj(1))

  def setEnemy (): Unit =
    val enemyData0 = List(
      (new EN_Necromancer(gp),                  0,     Some(calcPos (23, 24))),
      (new EN_Necromancer(gp),                  1,     Some(calcPos (13, 14)))
    )

    this.loadEntity(enemyData0, gp.enemyList(0))

  def setNpc(): Unit =
    val npcData0 = List(
      (new Merchant(gp),                        0,     Some(calcPos (13, 14))),
      (new Socerer(gp),                         1,     Some(calcPos (15, 14)))
    )

    this.loadEntity(npcData0, gp.npcList(0))

end ObjectManager


