package items

abstract class Item(val name: String):
  def use(): Unit
  def applyEffect(): Unit