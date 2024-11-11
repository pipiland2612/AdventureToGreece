package `object`

import Enemy.{EN_Beetle, EN_KingOfDeath, EN_Mantis, EN_Necromancer, EN_Orc_Heavy, EN_Orc_Light}
import `object`.ObjectType.{OBJ_BronzeCoin, OBJ_Chest, OBJ_NormalAxe, OBJ_NormalHealFlask, OBJ_NormalSword}
import entities.{Entity, Merchant, Socerer}
import game.{GamePanel, GameProgress}
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
      (new OBJ_CheckPoint(gp),                  1,     Some(calcPos (6, 3))),
      (new OBJ_Pillar(gp),                      2,     Some(calcPos (21, 26))),
      (new OBJ_Pillar(gp),                      3,     Some(calcPos (25, 26))),
      (new OBJ_Pillar(gp),                      4,     Some(calcPos (21, 30))),
      (new OBJ_Pillar(gp),                      5,     Some(calcPos (25, 30))),
      (new OBJ_Pillar(gp),                      6,     Some(calcPos (46, 36))),
      (new OBJ_Pillar(gp),                      7,     Some(calcPos (48, 36))),

    )
    this.loadEntity(objectData1, gp.obj(1))

    val objectData2 = List (
      (new OBJ_Skeleton(gp),                    0,     Some(calcPos (25, 45))),
      (new OBJ_DungeonFence(gp),                1,     Some(calcPos (11, 31))),
      (new OBJ_DungeonFence(gp),                2,     Some(calcPos (12, 31))),
      (new OBJ_DungeonFence(gp),                3,     Some(calcPos (14, 31))),
      (new OBJ_DungeonFence(gp),                4,     Some(calcPos (15, 31))),
      (new OBJ_DungeonFence(gp),                5,     Some(calcPos (17, 31))),
      (new OBJ_DungeonFence(gp),                6,     Some(calcPos (18, 31))),
      (new OBJ_DungeonFence(gp),                7,     Some(calcPos (20, 31))),
      (new OBJ_DungeonFence(gp),                8,     Some(calcPos (21, 31))),
      (new OBJ_DungeonFence(gp),                9,     Some(calcPos (27, 31))),
      (new OBJ_DungeonFence(gp),                10,    Some(calcPos (28, 31))),
      (new OBJ_DungeonFence(gp),                11,    Some(calcPos (30, 31))),
      (new OBJ_DungeonFence(gp),                12,    Some(calcPos (31, 31))),
      (new OBJ_DungeonFence(gp),                13,    Some(calcPos (33, 31))),
      (new OBJ_DungeonFence(gp),                14,    Some(calcPos (34, 31))),
      (new OBJ_DungeonFence(gp),                15,    Some(calcPos (36, 31))),
      (new OBJ_DungeonFence(gp),                16,    Some(calcPos (37, 31))),
      (new OBJ_DungeonFence(gp),                17,    Some(calcPos (39, 31))),

      (new OBJ_BossPillar(gp),                  18,     Some(calcPos (10, 30))),
      (new OBJ_BossPillar(gp),                  19,     Some(calcPos (13, 30))),
      (new OBJ_BossPillar(gp),                  20,     Some(calcPos (16, 30))),
      (new OBJ_BossPillar(gp),                  21,     Some(calcPos (19, 30))),
      (new OBJ_BossPillar(gp),                  22,     Some(calcPos (22, 30))),
      (new OBJ_BossPillar(gp),                  23,     Some(calcPos (23, 30))),
      (new OBJ_BossPillar(gp),                  24,     Some(calcPos (25, 30))),
      (new OBJ_BossPillar(gp),                  25,     Some(calcPos (26, 30))),
      (new OBJ_BossPillar(gp),                  26,     Some(calcPos (29, 30))),
      (new OBJ_BossPillar(gp),                  27,     Some(calcPos (32, 30))),
      (new OBJ_BossPillar(gp),                  28,     Some(calcPos (35, 30))),
      (new OBJ_BossPillar(gp),                  29,     Some(calcPos (38, 30))),

      (new OBJ_Pillar(gp),                      30,     Some(calcPos (22, 33))),
      (new OBJ_Pillar(gp),                      31,     Some(calcPos (26, 33))),

      (new OBJ_DungeonFence(gp),                32,     Some(calcPos (9, 31))),
      (new OBJ_DungeonFence(gp),                33,     Some(calcPos (40, 31))),

      (new OBJ_BossPillar(gp),                  34,     Some(calcPos (17, 15))),
      (new OBJ_BossPillar(gp),                  35,     Some(calcPos (17, 25))),
      (new OBJ_BossPillar(gp),                  36,     Some(calcPos (35, 15))),
      (new OBJ_BossPillar(gp),                  37,     Some(calcPos (35, 25))),
      (new OBJ_BossPillar(gp),                  38,     Some(calcPos (23, 21))),

    )

    this.loadEntity(objectData2, gp.obj(2))

  def setEnemy (): Unit =
    // OBJ                                      INDEX  POSITION    (COL, ROW)
    val enemyData0 = List(
      (new EN_Beetle(gp),                       1,     Some(calcPos (23, 24))),
      (new EN_Mantis(gp),                       2,     Some(calcPos (15, 17))),
    )

    this.loadEntity(enemyData0, gp.enemyList(0))

    val enemyData1 = List(
      (new EN_Necromancer(gp),                  0,     Some(calcPos (23, 24))),
      (new EN_Orc_Light(gp),                    1,     Some(calcPos (3, 3))),
      (new EN_Necromancer(gp),                  2,     Some(calcPos (13, 14))),
      (new EN_Orc_Heavy(gp),                    3,     Some(calcPos (5, 5))),
    )
    this.loadEntity(enemyData1, gp.enemyList(1))

    val enemyData2 = List(
        (new EN_KingOfDeath(gp),                  0,     Some(calcPos (23, 18))),
    )

    if !GameProgress.KingOfDeathDefeated then
      this.loadEntity(enemyData2, gp.enemyList(2))

  def setNpc(): Unit =
    // OBJ                                      INDEX  POSITION    (COL, ROW)
    val npcData0 = List(
      (new Merchant(gp),                        0,     Some(calcPos (13, 14))),
      (new Socerer(gp),                         1,     Some(calcPos (15, 14)))
    )

    this.loadEntity(npcData0, gp.npcList(0))

end ObjectManager


