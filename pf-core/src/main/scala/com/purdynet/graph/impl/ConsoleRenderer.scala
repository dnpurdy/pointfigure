package com.purdynet.graph.impl

import com.purdynet.graph._
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component

@Component
@Profile(Array("console"))
class ConsoleRenderer extends PFGRenderer {
  override def render(pfg: PointFigureGraph): Unit = pfg.render();
}
