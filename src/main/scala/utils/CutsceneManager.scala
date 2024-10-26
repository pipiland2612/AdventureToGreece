package utils

import Enemy.EN_KingOfDeath
import `object`.{OBJ_DungeonFence, OBJ_PlayerDummy}
import game.GamePanel
import game.GameState.PlayState

import java.awt.Graphics2D

class CutsceneManager (var gp: GamePanel):

  var g2: Graphics2D = _
  var sceneNum: Int = 0
  var scenePhase: Int = 0

  val NA = 0
  val kingOfDeath = 1

  def draw(graphics2D: Graphics2D): Unit =
    this.g2 = graphics2D
    sceneNum match
      case this.kingOfDeath => kingOfDeathCutScene()
      case _ =>

  def kingOfDeathCutScene(): Unit =
    var found = false
    if scenePhase == 0 then
      gp.onBossBattle = true

      for i <- gp.obj(1).indices if !found do
        if gp.obj(gp.currentMap)(i) == null then
          gp.obj(gp.currentMap)(i) = new OBJ_DungeonFence(gp)
          gp.obj(gp.currentMap)(i).pos = (24 * gp.tileSize, 12 * gp.tileSize)
          gp.obj(gp.currentMap)(i).isTemp = true
          found = true

      found = false
      for i <- gp.obj(1).indices if !found do
        if gp.obj(gp.currentMap)(i) == null then
          gp.obj(gp.currentMap)(i) = new OBJ_PlayerDummy(gp)
          gp.obj(gp.currentMap)(i).pos = gp.player.pos
          found = true

      gp.player.drawing = false
      scenePhase += 1
    if scenePhase == 1 then
      gp.player.move(0, -2)
      if gp.player.pos._2 < 18 * gp.tileSize then
        scenePhase += 1

    if scenePhase == 2 then
      found = false
      for i <- gp.enemyList(1).indices if !found do
        if gp.enemyList(gp.currentMap)(i) != null then
          if gp.enemyList(gp.currentMap)(i).name.equals(EN_KingOfDeath.Name) then
            gp.enemyList(gp.currentMap)(i).isSleeping = false
            gp.gui.npc = gp.enemyList(gp.currentMap)(i)
            scenePhase += 1
            found = true

    if scenePhase == 3 then
      gp.gui.drawDialogueScreen()

    if scenePhase == 4 then
      found = false
      for i <- gp.obj(1).indices if !found do
        if gp.obj(gp.currentMap)(i) != null && gp.obj(gp.currentMap)(i).name.equals(OBJ_PlayerDummy.Name) then
          gp.player.pos = gp.obj(gp.currentMap)(i).pos
          gp.obj(gp.currentMap)(i) = null
          gp.player.drawing = true
          found = true
      sceneNum = NA
      scenePhase = 0
      gp.gameState = PlayState
