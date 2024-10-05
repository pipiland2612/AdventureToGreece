package `object`

import Enemy.EN_Necromancer
import game.GamePanel


class ObjectManager(var gp : GamePanel) :

  def setObject(): Unit =
    val tileSize = gp.tileSize

    gp.obj(0) = new OBJ_Chest(48, (25 * tileSize, 25 * tileSize), gp)
    gp.obj(1) = new OBJ_Rock(48, (10 * tileSize, 15 * tileSize), gp)
    gp.obj(2) = new OBJ_Tree(256, (10 * tileSize, 20 * tileSize), gp)

    gp.obj(3) = new OBJ_NormalHealFlask(gp)
    gp.obj(3).pos = (9 * tileSize, 9 * tileSize)

    gp.obj(4) = new OBJ_NormalSword(gp)
    gp.obj(4).pos = (10 * tileSize, 9 * tileSize)

    gp.obj(5) = new OBJ_NormalAxe(gp)
    gp.obj(5).pos = (11 * tileSize, 9 * tileSize)

    gp.obj(6) = new OBJ_BronzeCoin(gp)
    gp.obj(6).pos = (11 * tileSize, 10 * tileSize)


  def setEnemy (): Unit =
    gp.enemyList(0) = new EN_Necromancer(gp, (23 * gp.tileSize, 24 * gp.tileSize))
    gp.enemyList(1) = new EN_Necromancer(gp, (13 * gp.tileSize, 14 * gp.tileSize))

end ObjectManager


