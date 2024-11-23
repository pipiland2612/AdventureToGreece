package system.data

import system.environment.Area.{Dungeon, OverWorld}
import entities.`object`.OBJ_Chest
import entities.items.{Item, Light, Projectile, Shield, Weapon}
import game.GamePanel

import java.io.{File, FileInputStream, FileOutputStream, ObjectInputStream, ObjectOutputStream}

class SaveLoad (gp: GamePanel):

  def save(): Unit =
    try
      val oos = new ObjectOutputStream(new FileOutputStream(new File("src/main/resources/save.dat")))
      val ds = new DataStorage()
      // Players stats
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
      ds.playerPositionX = gp.player.lastSavePoint._1
      ds.playerPositionY = gp.player.lastSavePoint._2
      ds.currentMap = gp.player.lastSavePointMap
      ds.currentArea = gp.player.lastSavePointMap match
        case 0 => "OverWorld"
        case 1 => "Dungeon"

      // Player inventory
      for item <- gp.player.inventory do
        ds.itemNames += item.name
        ds.itemAmount += item.amount

      ds.currentWeaponSlot = gp.player.getCurrentWeaponIndex
      ds.currentShieldSlot = gp.player.getCurrentShieldIndex
      ds.currentProjectileSlot = gp.player.getCurrentProjectileIndex
      ds.currentLightSlot = gp.player.getCurrentLightIndex

      // OBJS on map
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
            if gp.obj(mapNum)(i).loot != null then
              ds.mapObjLootNames(mapNum)(i) = gp.obj(mapNum)(i).loot.name
            ds.chestOpened(mapNum)(i) = gp.obj(mapNum)(i).opened

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
      gp.player.pos = (ds.playerPositionX, ds.playerPositionY)
      gp.currentMap = ds.currentMap
      gp.currentArea = ds.currentArea match
        case "OverWorld" => OverWorld
        case "Dungeon" => Dungeon

      gp.player.inventory.clear()
      for index <- ds.itemNames.indices do
        if gp.entityGen.getObject(ds.itemNames(index)) != null then
          gp.player.inventory += gp.entityGen.getObject(ds.itemNames(index)).asInstanceOf[Item]
          gp.player.inventory(index).amount = ds.itemAmount(index)

      if ds.currentWeaponSlot >= 0 && ds.currentWeaponSlot < gp.player.inventory.size then
        gp.player.currentWeapon = gp.player.inventory(ds.currentWeaponSlot).asInstanceOf[Weapon]

      if ds.currentShieldSlot >= 0 && ds.currentShieldSlot < gp.player.inventory.size then
        gp.player.currentShield = gp.player.inventory(ds.currentShieldSlot).asInstanceOf[Shield]

      if ds.currentProjectileSlot >= 0 && ds.currentProjectileSlot < gp.player.inventory.size then
        gp.player.currentProjectile = gp.player.inventory(ds.currentProjectileSlot).asInstanceOf[Projectile]
      if ds.currentLightSlot >= 0 && ds.currentLightSlot < gp.player.inventory.size then
        gp.player.currentLight = gp.player.inventory(ds.currentLightSlot).asInstanceOf[Light]

      gp.player.attackDamage = gp.player.getAttackDamage
      gp.player.defense = gp.player.getDefense

      for mapNum <- 0 until gp.maxMap do
        for i <- gp.obj(1).indices do
          if ds.mapObjNames(mapNum)(i).equals("N/A") then
            gp.obj(mapNum)(i) = null
          else
            gp.obj(mapNum)(i) = gp.entityGen.getObject(ds.mapObjNames(mapNum)(i))
            gp.obj(mapNum)(i).pos = (ds.mapObjX(mapNum)(i), ds.mapObjY(mapNum)(i))
            if ds.mapObjLootNames(mapNum)(i) != null  then
              gp.obj(mapNum)(i).loot = gp.entityGen.getObject(ds.mapObjLootNames(mapNum)(i)).asInstanceOf[Item]
            gp.obj(mapNum)(i).opened = ds.chestOpened(mapNum)(i)
            if gp.obj(mapNum)(i).opened then gp.obj(mapNum)(i).image = gp.obj(mapNum)(i).asInstanceOf[OBJ_Chest].getImage

    catch
      case e: Exception => {}