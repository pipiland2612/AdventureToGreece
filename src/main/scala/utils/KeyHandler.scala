package utils
import game.{GamePanel, GameState}

import java.awt.event.{KeyEvent, KeyListener}

class KeyHandler(var gp: GamePanel) extends KeyListener :
  var upPressed, downPressed, leftPressed, rightPressed, attackPressed: Boolean = _
  var toggle: Boolean = _

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
        
      case _ =>
        println("Unknown game state")

  override def keyReleased(e: KeyEvent): Unit =
    e.getKeyCode match
      case KeyEvent.VK_W => upPressed = false
      case KeyEvent.VK_S => downPressed = false
      case KeyEvent.VK_A => leftPressed = false
      case KeyEvent.VK_D => rightPressed = false
      case KeyEvent.VK_G => attackPressed = false
      case _ => println("Not valid keys")

  override def keyTyped(e: KeyEvent): Unit = {}

  private def handleDialogueState (code : Int) : Unit =
    code match
      case KeyEvent.VK_ENTER => gp.gameState = GameState.PlayState
      case _ => println("Not valid keys")

  private def handlePauseState(code : Int) : Unit =
    code match
      case KeyEvent.VK_P => gp.gameState = GameState.PlayState
      case _ => println("Not valid keys")

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
      case _ => println("")

  private def handlePlayState(code : Int): Unit =
    code match
      case KeyEvent.VK_W => upPressed = true
      case KeyEvent.VK_S => downPressed = true
      case KeyEvent.VK_A => leftPressed = true
      case KeyEvent.VK_D => rightPressed = true
      case KeyEvent.VK_G => attackPressed = true
      case KeyEvent.VK_T =>
        if(!toggle) then toggle = true else if(toggle) then toggle = false
      case KeyEvent.VK_P =>
        gp.gameState = GameState.PauseState
      case _ => println("Not valid keys")