package `object`

import Enemy.EN_Necromancer
import `object`.ObjectType.{OBJ_BronzeCoin, OBJ_Chest, OBJ_NormalAxe, OBJ_NormalHealFlask, OBJ_NormalSword}
import entities.Merchant
import game.GamePanel


class ObjectManager(var gp : GamePanel) :

  def setObject(): Unit =
    val tileSize = gp.tileSize
    var mapNum = 0
    var currentObjList = gp.obj(mapNum)

    currentObjList(0) = new OBJ_Chest(32, (35 * tileSize, 25 * tileSize), gp, OBJ_BronzeCoin(gp))
    currentObjList(1) = new OBJ_Rock(48, (10 * tileSize, 15 * tileSize), gp)
    currentObjList(2) = new OBJ_Tree(256, (10 * tileSize, 20 * tileSize), gp)

    currentObjList(3) = new OBJ_NormalHealFlask(gp)
    currentObjList(3).pos = (9 * tileSize, 9 * tileSize)

    currentObjList(4) = new OBJ_NormalSword(gp)
    currentObjList(4).pos = (10 * tileSize, 9 * tileSize)

    currentObjList(5) = new OBJ_NormalAxe(gp)
    currentObjList(5).pos = (11 * tileSize, 9 * tileSize)

    currentObjList(6) = new OBJ_BronzeCoin(gp)
    currentObjList(6).pos = (11 * tileSize, 10 * tileSize)

    currentObjList(7) = new OBJ_NormalHealFlask(gp)
    currentObjList(7).pos = (10 * tileSize, 9 * tileSize)

    currentObjList(8) = new OBJ_NormalHealFlask(gp)
    currentObjList(8).pos = (11 * tileSize, 9 * tileSize)

    currentObjList(9) = new OBJ_NormalHealFlask(gp)
    currentObjList(9).pos = (12 * tileSize, 9 * tileSize)


  def setEnemy (): Unit =
    var mapNum = 0
    var currentEnemyList = gp.enemyList(mapNum)
    currentEnemyList(0) = new EN_Necromancer(gp, (23 * gp.tileSize, 24 * gp.tileSize))
    currentEnemyList(1) = new EN_Necromancer(gp, (13 * gp.tileSize, 14 * gp.tileSize))

  def setNpc(): Unit =
    var mapNum = 0
    var currentNpcList = gp.npcList(mapNum)
    currentNpcList(0) = new Merchant(gp, (13 * gp.tileSize, 14 * gp.tileSize))

end ObjectManager


