package com.purdynet

import java.util.Date

import com.purdynet.data.SDownloader
import com.purdynet.graph.{PFGRenderer, PointFigureGraph}
import com.purdynet.prices.PriceRecord
import com.purdynet.scaling.Scaling
import com.purdynet.scaling.impl.TraditionalScaling
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.{CommandLineRunner, SpringApplication}

import scala.collection.JavaConverters._

@SpringBootApplication
@Autowired
class TodaySpringScores(val downloader: SDownloader, val renderer: PFGRenderer) extends CommandLineRunner {

  override def run(args: String*): Unit = {
    val filename = parseArgs(args.toArray)

    for (i <- 1 to 100) {
      val prices: Seq[PriceRecord] = downloader.getSPrices("GNCA", new Date(1405564800000L), new Date(1528243200000L))

      val s: Scaling = new TraditionalScaling
      val pfg: PointFigureGraph = new PointFigureGraph(prices.asJava, s)
      renderer.render(pfg)
    }
  }

  def parseArgs(args: Array[String]) {
    if (args.length == 0 || args.length > 1) System.exit(1)
    args(0)
  }
}

object TodaySpringScores extends App {
  SpringApplication.run(classOf[TodaySpringScores], args:_*)
}