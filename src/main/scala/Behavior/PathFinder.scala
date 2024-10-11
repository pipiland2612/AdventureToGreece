package Behavior

import game.GamePanel

import scala.collection.mutable.ListBuffer

class PathFinder(gp: GamePanel):
  // USING A* ALGORITHM TO FIND PATH
  var nodeList: Array[Array[Node]] = Array.ofDim(gp.maxWorldRow, gp.maxWorldCol)
  var openList: ListBuffer[Node] = new ListBuffer[Node]
  var pathList: ListBuffer[Node] = new ListBuffer[Node]
  var startNode, goalNode, currentNode: Node = _
  var goalReached: Boolean = false
  var step = 0

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
      // CHECK TILES
      val tileNum = gp.tileManager.mapTileNum(gp.currentMap)(row)(col)
      if gp.tileManager.tile(tileNum).collision then
        nodeList(row)(col).solid = true

      // SET COST
      setCost(nodeList(row)(col))

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
      val row = currentNode.row
      val col = currentNode.col
      currentNode.checked = true
      openList -= currentNode

      // Open neighboring nodes
      if row - 1 >= 0 then
        openNode(nodeList(row - 1)(col))
      if col - 1 >= 0 then
        openNode(nodeList(row)(col - 1))
      if row + 1 < gp.maxWorldRow then
        openNode(nodeList(row + 1)(col))
      if col + 1 < gp.maxWorldCol then
        openNode(nodeList(row)(col + 1))

      var bestNodeIndex = 0
      var bestNodeFCost = Int.MaxValue

      for i <- openList.indices do
        if openList(i).fCost < bestNodeFCost then
          bestNodeIndex = i
          bestNodeFCost = openList(i).fCost
        else if openList(i).fCost == bestNodeFCost then
          if openList(i).hCost < openList(bestNodeIndex).hCost then
            bestNodeIndex = i

      currentNode = openList(bestNodeIndex)
      if (currentNode == goalNode) then
        goalReached = true
        trackPath()

      step += 1

    goalReached

  def openNode(node: Node): Unit =
    if !node.open && !node.checked && !node.solid then
      node.open = true
      node.parent = currentNode
      openList += node

  def trackPath(): Unit =
    var current: Node = goalNode
    while current != startNode do
      pathList.prepend(current)
      current = current.parent
