package utils

import `object`.ObjectType.{OBJ_BronzeCoin, OBJ_Chest, OBJ_Fireball, OBJ_GoldCoin, OBJ_GoldenChest, OBJ_GoldenKatana, OBJ_ManaFlask, OBJ_MoonSaber, OBJ_NormalAxe, OBJ_NormalHealFlask, OBJ_NormalShield, OBJ_NormalSword, OBJ_SilverChest, OBJ_SilverKey, OBJ_GoldenRelic}
import `object`.{OBJ_BossPillar, OBJ_CheckPoint, OBJ_DungeonFence, OBJ_DungeonGate, OBJ_Heart, OBJ_HolyGrail, OBJ_LightCandle, OBJ_Mana, OBJ_Pillar, OBJ_PlayerDummy, OBJ_Rock, OBJ_Skeleton, OBJ_Tree}
import entities.Entity
import game.GamePanel

class EntityGenerator(gp: GamePanel) :

  def getObject(name: String): Entity =
    val entity: Entity =
      name match
        case OBJ_SilverKey.Name =>        new OBJ_SilverKey(gp)
        case OBJ_BronzeCoin.Name =>       new OBJ_BronzeCoin(gp)
        case OBJ_GoldCoin.Name =>         new OBJ_GoldCoin(gp)
        case OBJ_LightCandle.Name =>      new OBJ_LightCandle(gp)
        case OBJ_NormalSword.Name =>      new OBJ_NormalSword(gp)
        case OBJ_MoonSaber.Name =>        new OBJ_MoonSaber(gp)
        case OBJ_GoldenKatana.Name =>     new OBJ_GoldenKatana(gp)
        case OBJ_NormalAxe.Name =>        new OBJ_NormalAxe(gp)
        case OBJ_NormalShield.Name =>     new OBJ_NormalShield(gp)
        case OBJ_SilverChest.Name =>      new OBJ_SilverChest(gp)
        case OBJ_GoldenRelic.Name =>      new OBJ_GoldenRelic(gp)
        case OBJ_GoldenChest.Name =>      new OBJ_GoldenChest(gp)
        case OBJ_Fireball.Name =>         new OBJ_Fireball(gp)
        case OBJ_NormalHealFlask.Name =>  new OBJ_NormalHealFlask(gp)
        case OBJ_ManaFlask.Name =>        new OBJ_ManaFlask(gp)
        case OBJ_Chest.Name =>            new OBJ_Chest(gp)
        case OBJ_Rock.Name =>             new OBJ_Rock(gp)
        case OBJ_Tree.Name =>             new OBJ_Tree(gp)
        case OBJ_CheckPoint.Name =>       new OBJ_CheckPoint(gp)
        case OBJ_Heart.Name =>            new OBJ_Heart(gp)
        case OBJ_Mana.Name =>             new OBJ_Mana(gp)
        case OBJ_DungeonGate.Name =>      new OBJ_DungeonGate(gp)
        case OBJ_Pillar.Name =>           new OBJ_Pillar(gp)
        case OBJ_BossPillar.Name =>       new OBJ_BossPillar(gp)
        case OBJ_DungeonFence.Name =>     new OBJ_DungeonFence(gp)
        case OBJ_Skeleton.Name =>         new OBJ_Skeleton(gp)
        case OBJ_PlayerDummy.Name =>      new OBJ_PlayerDummy(gp)
        case OBJ_HolyGrail.Name =>        new OBJ_HolyGrail(gp)
        case _ =>                         new OBJ_BronzeCoin(gp);

    entity

