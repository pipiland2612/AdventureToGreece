package items

import entities.Entity
import game.GamePanel

import java.awt.Rectangle

abstract class InteractiveObjects (gp: GamePanel) extends Entity(gp):
  def interact(): Unit = {}
  def setDialogue(): Unit
  def setLoot(loot : Item): Unit = {}


