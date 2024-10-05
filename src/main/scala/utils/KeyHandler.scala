package utils
import game.GameState.{CharacterState, PlayState}
import game.{GamePanel, GameState}

import java.awt.event.{KeyEvent, KeyListener}

class KeyHandler(var gp: GamePanel) extends KeyListener :
  var upPressed, downPressed, leftPressed, rightPressed, attackPressed, shootKeyPressed: Boolean = _
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
      case _ =>
        println("Unknown game state")

  override def keyReleased(e: KeyEvent): Unit =
    e.getKeyCode match
      case KeyEvent.VK_W => upPressed = false
      case KeyEvent.VK_S => downPressed = false
      case KeyEvent.VK_A => leftPressed = false
      case KeyEvent.VK_D => rightPressed = false
      case KeyEvent.VK_G => attackPressed = false
      case KeyEvent.VK_U => shootKeyPressed = false
      case _ =>

  override def keyTyped(e: KeyEvent): Unit = {}


  // Handle different game state
  private def handleDialogueState (code : Int) : Unit =
    code match
      case KeyEvent.VK_ENTER => gp.gameState = GameState.PlayState
      case _ =>

  private def handlePauseState(code : Int) : Unit =
    code match
      case KeyEvent.VK_P => gp.gameState = GameState.PlayState
      case _ =>

  private def handleTitleState(code: Int): Unit =
    code match
      case KeyEvent.VK_UP =>
        gp.gui.commandNum = (gp.gui.commandNum - 1)
        if gp.gui.commandNum < 1 then gp.gui.commandNum = 3
      case KeyEvent.VK_DOWN =>
        gp.gui.commandNum = (gp.gui.commandNum + 1)
        if gp.gui.commandNum > 3 then gp.gui.commandNum = 1
      case KeyEvent.VK_ENTER =>
        gp.gui.commandNum match
          case 1 => gp.gameState = GameState.PlayState
          case 2 => {}
          case 3 => System.exit(0)
      case _ =>

  private def handlePlayState(code : Int): Unit =
    code match
      case KeyEvent.VK_W => upPressed = true
      case KeyEvent.VK_S => downPressed = true
      case KeyEvent.VK_A => leftPressed = true
      case KeyEvent.VK_D => rightPressed = true
      case KeyEvent.VK_J => attackPressed = true
      case KeyEvent.VK_U => shootKeyPressed = true
      case KeyEvent.VK_C => gp.gameState = CharacterState
      case KeyEvent.VK_T =>
        if(!showDebugText) then showDebugText = true else if(showDebugText) then showDebugText = false
      case KeyEvent.VK_R =>
        gp.tileManager.loadMap("/Users/batman/Desktop/Adventure to Greece/src/main/resources/images/Maps/map.txt")
      case KeyEvent.VK_P =>
        gp.gameState = GameState.PauseState
      case _ =>

  private def handleCharacterState (code : Int): Unit =
    code match
      case KeyEvent.VK_W =>
        if gp.gui.slotRow != 0 then
          gp.gui.slotRow -= 1
      case KeyEvent.VK_S =>
        if gp.gui.slotRow != 3 then
          gp.gui.slotRow += 1
      case KeyEvent.VK_A =>
        if gp.gui.slotCol != 0 then
          gp.gui.slotCol -= 1
      case KeyEvent.VK_D =>
        if gp.gui.slotCol != 4 then
          gp.gui.slotCol += 1
      case KeyEvent.VK_C => gp.gameState = PlayState
      case KeyEvent.VK_ENTER => gp.player.selectItem()
      case _ =>