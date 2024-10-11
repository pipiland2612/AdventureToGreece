package `object`

import Enemy.EN_Necromancer
import `object`.ObjectType.{OBJ_BronzeCoin, OBJ_Chest, OBJ_NormalAxe, OBJ_NormalHealFlask, OBJ_NormalSword}
import entities.Merchant
import game.GamePanel


class ObjectManager(var gp : GamePanel) :
  val tileSize = gp.tileSize

  private def calcPos(col: Int, row: Int): (Int, Int) = (col * tileSize, row * tileSize)

  def setObject(): Unit =
    var mapNum = 0
    var currentObjList = gp.obj(mapNum)

    val objectData = List(
      // OBJ                                      INDEX   POSITION
      (new OBJ_Chest(32, gp, OBJ_BronzeCoin(gp)),  0,     Some(calcPos(35, 25))),
      (new OBJ_Rock(48, gp),                        1,     Some(calcPos(10, 15))),
      (new OBJ_Tree(256, gp),                        2,     Some(calcPos(10, 20))),
      (new OBJ_NormalHealFlask(gp),                  3,     Some(calcPos(9, 9))),
      (new OBJ_NormalSword(gp),                      4,     Some(calcPos(10, 9))),
      (new OBJ_NormalAxe(gp),                        5,     Some(calcPos(11, 9))),
      (new OBJ_BronzeCoin(gp),                       6,     Some(calcPos(11, 10))),
      (new OBJ_NormalHealFlask(gp),                  7,     Some(calcPos(10, 9))),
      (new OBJ_NormalHealFlask(gp),                  8,     Some(calcPos(11, 9))),
      (new OBJ_NormalHealFlask(gp),                  9,     Some(calcPos(12, 9))),
      (new OBJ_Candle(gp),                          10,     Some(calcPos(24, 21)))
    )

    objectData.foreach { case (obj, index, positionOpt) =>
      currentObjList(index) = obj
      positionOpt.foreach(pos => currentObjList(index).pos = pos)
    }

  def setEnemy (): Unit =
    var mapNum = 0
    var currentEnemyList = gp.enemyList(mapNum)
    val enemyData = List(
      (new EN_Necromancer(gp, calcPos(23, 24)), 0),
      (new EN_Necromancer(gp, calcPos(13, 14)), 1)
    )

    enemyData.foreach { case (enemy, index) =>
      currentEnemyList(index) = enemy
    }

  def setNpc(): Unit =
    var mapNum = 0
    var currentNpcList = gp.npcList(mapNum)
    val npcData = List(
      (new Merchant(gp, calcPos(13, 14)), 0)
    )

    npcData.foreach { case (npc, index) =>
      currentNpcList(index) = npc
    }

end ObjectManager


