package Behavior

class Node(
  var row: Int,
  var col: Int
):
  var parent: Node = _
  var gCost: Int = 0
  var hCost: Int = 0
  var fCost: Int = 0
  var solid: Boolean = false
  var open : Boolean = false
  var checked: Boolean = false

