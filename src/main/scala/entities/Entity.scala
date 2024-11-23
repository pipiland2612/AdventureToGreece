package entities

import entities.creatures.{Direction, State}
import entities.items.*
import game.{GamePanel, GameState}
import utils.Animation

import java.awt.image.BufferedImage
import java.awt.{Graphics2D, Rectangle}
import scala.collection.mutable.ListBuffer

abstract class Entity(var gp: GamePanel):

  // ----------------------------------------------------------------------------------------------
  // Attributes

  var state: State = State.IDLE
  var pos: (Int, Int)                                
  var speed: Int = 0                                 
  var direction: Direction = Direction.DOWN          
  var isCollided: Boolean = false                    

  // ----------------------------------------------------------------------------------------------
  // Inventory, Equipment, and Interaction

  var currentWeapon: Weapon = _
  var currentShield: Shield = _
  var currentProjectile: Projectile = _
  var currentLight: Light = _

  val maxInventorySize = 20
  var inventory: ListBuffer[Item] = ListBuffer()     
  var loot: Item = _                                 
  var opened: Boolean = false                        

  // ----------------------------------------------------------------------------------------------
  // Collision Areas

  var solidArea: Rectangle                           
  var solidAreaDefaultX, solidAreaDefaultY: Int = 0  

  var areaHitBox: Rectangle = Rectangle(0, 0, 0, 0)  
  var areaDefaultX, areaDefaultY: Int = 0            

  var attackArea: Rectangle = new Rectangle(0, 0, 0, 0) 

  // ----------------------------------------------------------------------------------------------
  // Animation and Rendering

  var currentAnimation: Animation = _
  var image: BufferedImage = _
  var offsetX: Int = 0
  var offsetY: Int = 0
  // For more precision
  var imageWidthCenter: Int = 0
  var imageHeightCenter: Int = 0

  // ----------------------------------------------------------------------------------------------
  // Dialogue System

  var name: String                                  
  var collision: Boolean = false                    

  var dialogues: Array[Array[String]] = Array.ofDim(20, 20) // Dialogue contents
  var dialogueSet: Int = 0
  var dialogueIndex: Int = 0

  var isTemp = false

  // ----------------------------------------------------------------------------------------------
  // Helper Methods

  def getPosition: (Int, Int) = this.pos

  def getLeftX: Int = this.pos._1 + this.solidArea.x
  def getRightX: Int = this.pos._1 + this.solidArea.x + this.solidArea.width
  def getTopY: Int = this.pos._2 + this.solidArea.y
  def getBottomY: Int = this.pos._2 + this.solidArea.y + solidArea.height

  def getCol: Int = (this.pos._1 + this.solidAreaDefaultX) / gp.tileSize
  def getRow: Int = (this.pos._2 + this.solidAreaDefaultY) / gp.tileSize

  // ----------------------------------------------------------------------------------------------
  // Methods for Gameplay Actions

  def startDialogue(entity: Entity, setNum: Int): Unit =
    gp.gameState = GameState.DialogueState
    gp.gui.npc = entity
    dialogueSet = setNum

  def calculateScreenCoordinates(): (Int, Int) =
    val screenX = gp.player.screenX
    val screenY = gp.player.screenY

    val (worldX, worldY) = this.pos
    val (playerX, playerY) = gp.player.getPosition
    val screenTileX = worldX - playerX + screenX
    val screenTileY = worldY - playerY + screenY

    (screenTileX, screenTileY)


  // ----------------------------------------------------------------------------------------------
  // Rendering Method
  def isInCamera: Boolean =
    val drawRange = gp.tileSize * 3
    val (worldX, worldY) = this.pos
    val (playerX, playerY) = gp.player.getPosition
    val screenX = gp.player.screenX
    val screenY = gp.player.screenY
    // Condition
    worldX + drawRange > playerX - screenX &&
    worldX - drawRange < playerX + screenX &&
    worldY + drawRange > playerY - screenY &&
    worldY - drawRange < playerY + screenY


  def draw(g: Graphics2D): Unit =
    val (screenTileX, screenTileY) = calculateScreenCoordinates()

    val adjustedScreenTileX = if this.state == State.ATTACK then screenTileX - offsetX else screenTileX
    val adjustedScreenTileY = if this.state == State.ATTACK then screenTileY - offsetY else screenTileY

    if isInCamera then
      val imageToDraw =
        if currentAnimation != null then currentAnimation.getCurrentFrame
        else image

      g.drawImage(imageToDraw, adjustedScreenTileX, adjustedScreenTileY, null)
