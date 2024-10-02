package levels


import entities.{Creatures, Entity}

class Obstacle(var pos: (Double, Double)):
  def activate(): Unit = {}
  
  def trigger(): Unit = {}
  
  def dealDamage(entity: Creatures): Unit =
    entity.takeDamage(1)
    
    