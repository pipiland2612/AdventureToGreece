package utils

import `object`.ObjectType.{OBJ_BronzeCoin, OBJ_Chest, OBJ_Fireball, OBJ_NormalAxe, OBJ_NormalHealFlask, OBJ_NormalShield, OBJ_NormalSword, OBJ_SilverKey}
import `object`.{OBJ_CheckPoint, OBJ_DungeonGate, OBJ_Heart, OBJ_LightCandle, OBJ_Mana, OBJ_Rock, OBJ_Tree}
import entities.Entity
import game.GamePanel

class EntityGenerator(gp: GamePanel) :

  def getObject(name: String): Entity =
    val entity: Entity =
      name match
        case OBJ_SilverKey.Name =>        new OBJ_SilverKey(gp)
        case OBJ_BronzeCoin.Name =>       new OBJ_BronzeCoin(gp)
        case OBJ_LightCandle.Name =>      new OBJ_LightCandle(gp)
        case OBJ_NormalSword.Name =>      new OBJ_NormalSword(gp)
        case OBJ_NormalAxe.Name =>        new OBJ_NormalAxe(gp)
        case OBJ_NormalShield.Name =>     new OBJ_NormalShield(gp)
        case OBJ_Fireball.Name =>         new OBJ_Fireball(gp)
        case OBJ_NormalHealFlask.Name =>  new OBJ_NormalHealFlask(gp)
        case OBJ_Chest.Name =>            new OBJ_Chest(gp)
        case OBJ_Rock.Name =>             new OBJ_Rock(gp)
        case OBJ_Tree.Name =>             new OBJ_Tree(gp)
        case OBJ_CheckPoint.Name =>       new OBJ_CheckPoint(gp)
        case OBJ_Heart.Name =>            new OBJ_Heart(gp)
        case OBJ_Mana.Name =>             new OBJ_Mana(gp)
        case OBJ_DungeonGate.Name =>      new OBJ_DungeonGate(gp)

    entity

