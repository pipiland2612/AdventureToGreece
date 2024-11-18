package utils

import java.awt.Rectangle

class EventRect extends Rectangle:
  var eventRectDefaultX, eventRectDefaultY: Int = 0
  var hasHappened: Boolean = false
end EventRect