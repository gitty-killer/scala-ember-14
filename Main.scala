import scala.io.Source

object TextStats {
  def tokenize(text: String): Seq[String] =
    "[A-Za-z0-9]+".r.findAllIn(text.toLowerCase).toSeq

  def main(args: Array[String]): Unit = {
    if (args.isEmpty) {
      Console.err.println("usage: scala Main.scala <path> [--top N]")
      System.exit(1)
    }
    var top = 10
    var path: String = ""
    var i = 0
    while (i < args.length) {
      if (args(i) == "--top" && i + 1 < args.length) {
        top = args(i + 1).toInt
        i += 2
      } else if (path.isEmpty) {
        path = args(i)
        i += 1
      } else {
        i += 1
      }
    }

    val text = Source.fromFile(path).mkString
    val lines = if (text.isEmpty) 0 else text.count(_ == '\n') + 1
    val words = tokenize(text)
    val counts = words.groupBy(identity).view.mapValues(_.size).toMap
    val sorted = counts.toSeq.sortBy { case (w, c) => (-c, w) }.take(top)

    println(s"lines: $lines")
    println(s"words: ${words.size}")
    println(s"chars: ${text.length}")
    println("top words:")
    sorted.foreach { case (w, c) => println(s"  $w: $c") }
  }
}
