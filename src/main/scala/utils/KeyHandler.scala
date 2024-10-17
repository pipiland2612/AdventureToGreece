package utils
import game.GameState.{CharacterState, MapState, PlayState}
import game.{GamePanel, GameState}
import ui.PlayerUI

import java.awt.event.{KeyEvent, KeyListener}

class KeyHandler(var gp: GamePanel) extends KeyListener :
  var upPressed, downPressed, leftPressed, rightPressed, enterPressed, attackPressed, shootKeyPressed: Boolean = _
  var showDebugText: Boolean = _

  override def keyPressed(e: KeyEvent): Unit =
    val code = e.getKeyCode
    gp.gameState match
      case GameState.PlayState =>
        handlePlayState(code)
      case GameState.TitleState =>
        handleTitleState(code)
      case GameState.PauseState =>
        handlePauseState(code)
      case GameState.DialogueState =>
        handleDialogueState(code)
      case GameState.CharacterState =>
        handleCharacterState(code)
      case GameState.GameMenu =>
        handleGameMenuState(code)
      case GameState.GameOver =>
        handleGameOverState(code)
      case GameState.TradeState =>
        handleTradeState(code)
      case GameState.MapState =>
        handleMapState(code)
      case _ =>
        println("Unknown game state")

  override def keyReleased(e: KeyEvent): Unit =
    e.getKeyCode match
      case KeyEvent.VK_W | KeyEvent.VK_UP => upPressed = false
      case KeyEvent.VK_S | KeyEvent.VK_DOWN => downPressed = false
      case KeyEvent.VK_A | KeyEvent.VK_LEFT => leftPressed = false
      case KeyEvent.VK_D | KeyEvent.VK_RIGHT => rightPressed = false
      case KeyEvent.VK_G => attackPressed = false
      case KeyEvent.VK_U => shootKeyPressed = false
      case KeyEvent.VK_ENTER => enterPressed = false
      case _ =>

  override def keyTyped(e: KeyEvent): Unit = {}

  // Handle different game state
  private def handleDialogueState (code : Int) : Unit =
    code match
      case KeyEvent.VK_ENTER => enterPressed = true
      case _ =>

  private def handlePauseState(code : Int) : Unit =
    code match
      case KeyEvent.VK_P => gp.gameState = GameState.PlayState
      case _ =>

  private def handleTitleState(code: Int): Unit =
    code match
      case KeyEvent.VK_UP | KeyEvent.VK_W =>
        gp.gui.commandNum = (gp.gui.commandNum - 1)
        if gp.gui.commandNum < 0 then gp.gui.commandNum = 2
      case KeyEvent.VK_DOWN | KeyEvent.VK_S =>
        gp.gui.commandNum = (gp.gui.commandNum + 1)
        if gp.gui.commandNum > 2 then gp.gui.commandNum = 0
      case KeyEvent.VK_ENTER =>
        gp.gui.commandNum match
          case 0 => gp.gameState = GameState.PlayState; gp.playMusic(0)
          case 1 =>
            gp.saveLoad.load()
            gp.gameState = GameState.PlayState
          case 2 => System.exit(0)
      case _ =>

  private def handlePlayState(code : Int): Unit =
    code match
      case KeyEvent.VK_W | KeyEvent.VK_UP => upPressed = true
      case KeyEvent.VK_S | KeyEvent.VK_DOWN => downPressed = true
      case KeyEvent.VK_A | KeyEvent.VK_LEFT => leftPressed = true
      case KeyEvent.VK_D | KeyEvent.VK_RIGHT => rightPressed = true
      case KeyEvent.VK_J => attackPressed = true
      case KeyEvent.VK_U => shootKeyPressed = true
      case KeyEvent.VK_C => gp.gameState = CharacterState
      case KeyEvent.VK_M => gp.gameState = MapState
      case KeyEvent.VK_N => if gp.miniMap.miniMapOn then gp.miniMap.miniMapOn = false else gp.miniMap.miniMapOn = true
      case KeyEvent.VK_T =>
        if(!showDebugText) then showDebugText = true else if(showDebugText) then showDebugText = false
      case KeyEvent.VK_R =>
        gp.currentMap match
          case 0 => gp.tileManager.loadMap("/Users/batman/Desktop/Adventure to Greece/src/main/resources/images/Maps/map.txt", 0)
          case 1 => gp.tileManager.loadMap("/Users/batman/Desktop/Adventure to Greece/src/main/resources/images/Maps/dungeon_map.txt", 1)
      case KeyEvent.VK_P =>
        gp.gameState = GameState.PauseState
      case KeyEvent.VK_ESCAPE =>
        gp.gameState = GameState.GameMenu
      case KeyEvent.VK_ENTER =>
        enterPressed = true
      case _ =>

  private def handleCharacterState (code : Int): Unit =
    code match
      case KeyEvent.VK_C => gp.gameState = PlayState
      case KeyEvent.VK_ENTER => gp.player.selectItem()
      case _ =>
    playerInventory(code)

  private def handleGameMenuState(code : Int): Unit =
    val maxCommanNum = gp.gui.subState match
        case 0 => 4
        case 1 => 0
        case 2 => 1
    code match
      case KeyEvent.VK_ESCAPE =>
        gp.gameState = GameState.PlayState
      case KeyEvent.VK_ENTER =>
        enterPressed = true
      case KeyEvent.VK_UP | KeyEvent.VK_W =>
          gp.gui.commandNum -= 1
          if gp.gui.commandNum < 0 then gp.gui.commandNum = maxCommanNum
      case KeyEvent.VK_DOWN | KeyEvent.VK_S =>
          gp.gui.commandNum += 1
          if gp.gui.commandNum > maxCommanNum then gp.gui.commandNum = 0
      case KeyEvent.VK_LEFT | KeyEvent.VK_A =>
        if gp.gui.subState == 0 then
          if gp.gui.commandNum == 0 && gp.sound.volumeScale > 0 then
            gp.sound.volumeScale -= 1; gp.sound.checkVolume()
          if gp.gui.commandNum == 1 && gp.se.volumeScale > 0 then
            gp.se.volumeScale -= 1
      case KeyEvent.VK_RIGHT | KeyEvent.VK_D =>
        if gp.gui.subState == 0 then
          if gp.gui.commandNum == 0 && gp.sound.volumeScale < 5 then
            gp.sound.volumeScale += 1; gp.sound.checkVolume()
          if gp.gui.commandNum == 1 && gp.se.volumeScale < 5 then
            gp.se.volumeScale += 1
      case _ =>

  private def handleGameOverState(code: Int): Unit  =
    code match
      case KeyEvent.VK_UP | KeyEvent.VK_W =>
        gp.gui.commandNum -= 1
        if gp.gui.commandNum < 0 then gp.gui.commandNum = 1
      case KeyEvent.VK_DOWN | KeyEvent.VK_S =>
        gp.gui.commandNum += 1
        if gp.gui.commandNum > 1 then gp.gui.commandNum = 0
      case KeyEvent.VK_ENTER =>
        if gp.gui.commandNum == 0 then
          gp.gameState = GameState.PlayState
          gp.resetGame(false)
        if gp.gui.commandNum == 1 then
          gp.gameState = GameState.TitleState
          gp.resetGame(true)
      case _ =>
  private def handleMapState(code: Int): Unit =
    code match
      case KeyEvent.VK_M => gp.gameState = GameState.PlayState
      case _ =>

  private def handleTradeState(code : Int): Unit =

      if code == KeyEvent.VK_ENTER then enterPressed = true
      if gp.gui.subState == 0 then
        if code == KeyEvent.VK_W || code == KeyEvent.VK_UP then
            gp.gui.commandNum -= 1
            if gp.gui.commandNum < 0 then gp.gui.commandNum = 2
        if code == KeyEvent.VK_S || code == KeyEvent.VK_DOWN then
            gp.gui.commandNum += 1
            if gp.gui.commandNum > 2 then gp.gui.commandNum = 0
      if gp.gui.subState == 1 then
        npcInventory(code)
        if code == KeyEvent.VK_ESCAPE then
          gp.gui.subState = 0
      if gp.gui.subState == 2 then
        playerInventory(code)
        if code == KeyEvent.VK_ESCAPE then
          gp.gui.subState = 0

  def playerInventory (code : Int): Unit =
    code match
      case KeyEvent.VK_W | KeyEvent.VK_UP =>
        if PlayerUI.playerSlotRow != 0 then
          PlayerUI.playerSlotRow -= 1
      case KeyEvent.VK_S | KeyEvent.VK_DOWN =>
        if PlayerUI.playerSlotRow != 3 then
          PlayerUI.playerSlotRow += 1
      case KeyEvent.VK_A | KeyEvent.VK_LEFT =>
        if PlayerUI.playerSlotCol != 0 then
          PlayerUI.playerSlotCol -= 1
      case KeyEvent.VK_D | KeyEvent.VK_RIGHT =>
        if PlayerUI.playerSlotCol != 4 then
          PlayerUI.playerSlotCol += 1
      case _ =>

  def npcInventory (code : Int): Unit =
    code match
      case KeyEvent.VK_W | KeyEvent.VK_UP =>
        if PlayerUI.npcSlotRow != 0 then
          PlayerUI.npcSlotRow -= 1
      case KeyEvent.VK_S | KeyEvent.VK_DOWN =>
        if PlayerUI.npcSlotRow != 3 then
          PlayerUI.npcSlotRow += 1
      case KeyEvent.VK_A | KeyEvent.VK_LEFT =>
        if PlayerUI.npcSlotCol != 0 then
          PlayerUI.npcSlotCol -= 1
      case KeyEvent.VK_D | KeyEvent.VK_RIGHT =>
        if PlayerUI.npcSlotCol != 4 then
          PlayerUI.npcSlotCol += 1
      case _ =>