package items

import entities.Entity
import game.GamePanel

abstract class InteractiveObjects (gp: GamePanel) extends Entity(gp):
  def interact(): Unit = {}
  def setDialogue(): Unit


