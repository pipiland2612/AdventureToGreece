package Behavior

import game.GamePanel

import scala.collection.mutable.PriorityQueue
import scala.collection.mutable.ListBuffer

class PathFinder(gp: GamePanel):
  // USING A* ALGORITHM TO FIND PATH
  var nodeList: Array[Array[Node]] = Array.ofDim(gp.maxWorldRow, gp.maxWorldCol)

  var pathList: ListBuffer[Node] = new ListBuffer[Node]
  var startNode, goalNode, currentNode: Node = _
  var goalReached: Boolean = false
  var step = 0

  // Min-Heap by fCost, tie by hCost
  given Ordering[Node] = Ordering.by(node => (-node.fCost, -node.hCost))
  var openList: PriorityQueue[Node] = PriorityQueue.empty

  def initialiseNode(): Unit =
    for
      row <- 0 until gp.maxWorldRow
      col <- 0 until gp.maxWorldCol
    do
      nodeList(row)(col) = new Node(row, col)

  initialiseNode()

  def resetNodes(): Unit =
    for
      row <- 0 until gp.maxWorldRow
      col <- 0 until gp.maxWorldCol
    do
      nodeList(row)(col).open = false
      nodeList(row)(col).checked = false
      nodeList(row)(col).solid = false

    openList.clear()
    pathList.clear()
    goalReached = false
    step = 0

  def setNodes(startRow: Int, startCol: Int, goalRow: Int, goalCol: Int): Unit =
    resetNodes()

    startNode = nodeList(startRow)(startCol)
    currentNode = startNode
    goalNode = nodeList(goalRow)(goalCol)
    openList += currentNode

    for
      row <- 0 until gp.maxWorldRow
      col <- 0 until gp.maxWorldCol
    do
      
      val tileNum = gp.tileManager.mapTileNum(gp.currentMap)(row)(col)
      // initialize solid nodes
      if gp.tileManager.tile(tileNum).collision then
        nodeList(row)(col).solid = true
      // SET COST
      setCost(nodeList(row)(col))

      //Check for objs and npcs
//      for i <- gp.obj(1).indices do
//        if gp.obj(gp.currentMap)(i) != null && gp.obj(gp.currentMap)(i).collision then
//          val objCol = (gp.obj(gp.currentMap)(i).getPosition._1 + gp.obj(gp.currentMap)(i).solidAreaDefaultX) / gp.tileSize
//          val objRow = (gp.obj(gp.currentMap)(i).getPosition._2 + gp.obj(gp.currentMap)(i).solidAreaDefaultY) / gp.tileSize
//          if objRow >= 0 && objRow < gp.maxWorldRow && objCol >= 0 && objCol < gp.maxWorldCol then
//            gp.pFinder.nodeList(objRow)(objCol).solid = true


//    for i <- gp.npcList(1).indices do
//      if gp.npcList(gp.currentMap)(i) != null && gp.npcList(gp.currentMap)(i).notMoving then
//        val npcListCol = gp.npcList(gp.currentMap)(i).pos._1 / gp.tileSize
//        val npcListRow = gp.npcList(gp.currentMap)(i).pos._2 / gp.tileSize
//        if npcListRow >= 0 && npcListRow < gp.maxWorldRow && npcListCol >= 0 && npcListCol < gp.maxWorldCol then
//          nodeList(npcListRow)(npcListCol).solid = true

  def setCost(node: Node): Unit =
    var xDistance = Math.abs(node.col - startNode.col)
    var yDistance = Math.abs(node.row - startNode.row)
    node.gCost = xDistance + yDistance

    xDistance = Math.abs(node.col - goalNode.col)
    yDistance = Math.abs(node.row - goalNode.row)
    node.hCost = xDistance + yDistance

    node.fCost = node.gCost + node.hCost

  def search(): Boolean =
    while !goalReached && step < 300 && openList.nonEmpty do
      if openList.isEmpty then return false 

      currentNode = openList.dequeue()
      currentNode.checked = true

      if currentNode == goalNode then
        //When reached, Track the path and follow
        goalReached = true
        trackPath()
        return true

      val row = currentNode.row
      val col = currentNode.col
      // Open neighbor nodes
      if row - 1 >= 0 then openNode(nodeList(row - 1)(col))
      if col - 1 >= 0 then openNode(nodeList(row)(col - 1))
      if row + 1 < gp.maxWorldRow then openNode(nodeList(row + 1)(col))
      if col + 1 < gp.maxWorldCol then openNode(nodeList(row)(col + 1))
      step += 1

    goalReached

  def openNode(node: Node): Unit =
    if !node.open && !node.checked && !node.solid then
      node.open = true
      node.parent = currentNode
      openList += node

  def trackPath(): Unit =
    var current: Node = goalNode
    while current != startNode && current != null do
      pathList.prepend(current)
      current = current.parent
