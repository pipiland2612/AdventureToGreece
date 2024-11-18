package items

import entities.Creatures
import game.GamePanel
import game.GameState.DialogueState

import java.awt.Rectangle

abstract class Potion (var effect: Int, gp: GamePanel) extends Item (gp):
  var name = ""
  var effectName = ""
  var pos = (0,0)
  var solidArea: Rectangle = Rectangle()
  def getDescription: String = s"$name can\n$effectName $effect "

  def applyEffect(effect: Int, creature: Creatures): Unit

  override def use(creature: Creatures): Boolean =
    gp.gameState = DialogueState
    dialogues(0)(0) = s"You drink the $name!\nYou got ${effectName}ed!"
    startDialogue(this, 0)
    applyEffect(effect, creature)
    true

end Potion


