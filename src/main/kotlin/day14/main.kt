package day14

val input = ClassLoader.getSystemResource("day14.txt").readText()

data class Reindeer(
  val name: String,
  val speed: Int,
  val runDuration: Int,
  val restDuration: Int
) {
  fun distanceAfter(seconds: Int): Int {
    var distance = 0
    var flipIn = runDuration
    var running = true
    (1..seconds).forEach {
      if (running) {
        distance += speed
      }

      if (--flipIn == 0) {
        running = !running
        flipIn = if (running) runDuration else restDuration
      }
    }
    return distance
  }
}

fun main(vararg args: String) {
  load(input.lines()).apply(::println)
    .map { Pair(it.name, it.distanceAfter(2503)) }
    .maxBy { it.second }!!
    .apply { println("$first wins after running $second km.") }
}

val parser = Regex("""(\w+) can fly (\d+) km/s for (\d+) seconds, but then must rest for (\d+) seconds\.""")

fun load(input: Iterable<String>): List<Reindeer> =
  input
    .map {
      parser.matchEntire(it)!!.groupValues.run {
        Reindeer(get(1), get(2).toInt(), get(3).toInt(), get(4).toInt())
      }
    }