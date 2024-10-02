package `object`

import Enemy.EN_Necromancer
import game.GamePanel


class ObjectManager(var gp : GamePanel) :

  def setObject(): Unit =
    val tileSize = gp.tileSize

    gp.obj(0) = new OBJ_Chest(48, (25 * tileSize, 25 * tileSize), gp)
    gp.obj(1) = new OBJ_Rock(48, (10 * tileSize, 15 * tileSize), gp)
    gp.obj(2) = new OBJ_HealFlask(32, (10 * tileSize, 20 * tileSize), gp)
    gp.obj(3) = new OBJ_Tree(256, (10 * tileSize, 20 * tileSize), gp)

  def setEnemy (): Unit =
    gp.enemyList(0) = new EN_Necromancer(gp, (23 * gp.tileSize, 24 * gp.tileSize))

end ObjectManager


