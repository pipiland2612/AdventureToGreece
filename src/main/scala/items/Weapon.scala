package items


class Weapon(name: String, var damage: Int) extends Item (name):
  def swing(): Unit = {
    println(s"Swinging $name, dealing $damage damage!")
  }

  def throwWeapon(): Unit = {
    println(s"Throwing $name!")
  }

  def dealDamage(): Int = {
    damage
  }

  override def use(): Unit = swing()
  override def applyEffect(): Unit = println(s"Weapon $name applied an effect")
