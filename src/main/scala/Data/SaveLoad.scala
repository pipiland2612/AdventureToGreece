package Data

import `object`.OBJ_LightCandle
import `object`.ObjectType.{OBJ_BronzeCoin, OBJ_Fireball, OBJ_NormalAxe, OBJ_NormalHealFlask, OBJ_NormalShield, OBJ_NormalSword, OBJ_SilverKey}
import game.GamePanel
import items.{Item, Projectile, Shield, Weapon}

import java.io.{File, FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

class SaveLoad (gp: GamePanel):

  def getObject(name : String): Item =
    val item : Item =
      name match
        case "Silver Key" => new OBJ_SilverKey(gp)
        case "Bronze Coin" => new OBJ_BronzeCoin(gp)
        case "Light Candle" => new OBJ_LightCandle(gp)
        case "Normal Sword" => new OBJ_NormalSword(gp)
        case "Normal Axe" => new OBJ_NormalAxe(gp)
        case "Normal Shield" => new OBJ_NormalShield(gp)
        case "Fire Ball" => new OBJ_Fireball(gp)
        case "Normal Heal Flask" => new OBJ_NormalHealFlask(gp)
    item

  def save(): Unit =
    try
      val oos = new ObjectOutputStream(new FileOutputStream(new File("src/main/resources/save.dat")))
      val ds = new DataStorage()
      ds.level = gp.player.level
      ds.maxHealth = gp.player.maxHealth
      ds.health = gp.player.health
      ds.maxMana = gp.player.maxMana
      ds.mana = gp.player.mana
      ds.strength = gp.player.strength
      ds.dexterity = gp.player.dexterity
      ds.exp = gp.player.exp
      ds.nextLevelExp = gp.player.nextLevelExp
      ds.coin = gp.player.coin

      for item <- gp.player.inventory do
        ds.itemNames += item.name
        ds.itemAmount += item.amount

      ds.currentWeaponSlot = gp.player.getCurrentWeaponIndex
      ds.currentShieldSlot = gp.player.getCurrentShieldIndex
      ds.currentProjectileSlot = gp.player.getCurrentProjectileIndex

      // OBJS
      ds.mapObjNames = Array.ofDim(gp.maxMap, gp.obj(1).length)
      ds.mapObjX = Array.ofDim(gp.maxMap, gp.obj(1).length)
      ds.mapObjY = Array.ofDim(gp.maxMap, gp.obj(1).length)
      ds.mapObjLootNames = Array.ofDim(gp.maxMap, gp.obj(1).length)
      ds.chestOpened = Array.ofDim(gp.maxMap, gp.obj(1).length)

      for mapNum <- 0 until gp.maxMap do
        for i <- gp.obj(1).indices do 
          if gp.obj(mapNum)(i) == null then 
            ds.mapObjNames(mapNum)(i) = "N/A"
          else 
            ds.mapObjNames(mapNum)(i) = gp.obj(mapNum)(i).name
            ds.mapObjX(mapNum)(i) = gp.obj(mapNum)(i).pos._1
            ds.mapObjY(mapNum)(i) = gp.obj(mapNum)(i).pos._2
            
          
      oos.writeObject(ds)

    catch
      case e: Exception => e.printStackTrace()

  def load(): Unit =
    try
      val ois = new ObjectInputStream(new FileInputStream(new File("src/main/resources/save.dat")))
      val ds: DataStorage = ois.readObject().asInstanceOf[DataStorage]

      gp.player.level = ds.level
      gp.player.maxHealth = ds.maxHealth
      gp.player.health = ds.health
      gp.player.maxMana = ds.maxMana
      gp.player.mana = ds.mana
      gp.player.strength = ds.strength
      gp.player.dexterity = ds.dexterity
      gp.player.exp = ds.exp
      gp.player.nextLevelExp = ds.nextLevelExp
      gp.player.coin = ds.coin

      gp.player.inventory.clear()
      for index <- ds.itemNames.indices do
        if getObject(ds.itemNames(index)) != null then
          gp.player.inventory += getObject(ds.itemNames(index))
          gp.player.inventory(index).amount = ds.itemAmount(index)

      if ds.currentWeaponSlot >= 0 && ds.currentWeaponSlot < gp.player.inventory.size then
        gp.player.currentWeapon = gp.player.inventory(ds.currentWeaponSlot).asInstanceOf[Weapon]

      if ds.currentShieldSlot >= 0 && ds.currentShieldSlot < gp.player.inventory.size then
        gp.player.currentShield = gp.player.inventory(ds.currentShieldSlot).asInstanceOf[Shield]

      if ds.currentProjectileSlot >= 0 && ds.currentProjectileSlot < gp.player.inventory.size then
        gp.player.currentProjectile = gp.player.inventory(ds.currentProjectileSlot).asInstanceOf[Projectile]

      gp.player.getAttackDamage
      gp.player.getDefense

    catch
      case e: Exception => e.printStackTrace()