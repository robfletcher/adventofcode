package day09

val input = ClassLoader.getSystemResource("day9.txt").readText()

data class Node(val name: String, val paths: Map<String, Int> = mapOf())

fun main(vararg args: String) {
  buildChart(input)
    .let { chart ->
      chart.keys.map { Pair(it, 0) }.toMap().findRoute(chart)
    }
    .apply { println(first.joinToString(" -> ") + " = " + second) }
}

fun Map<String, Int>.findRoute(remaining: Map<String, Node>,
                               route: List<String> = emptyList(),
                               distance: Int = 0)
  : Pair<List<String>, Int> =
  filter { it.key in remaining.keys }
    .map {
      remaining[it.key]!!.run {
        findRoute(remaining - name, route + name, distance + it.value)
      }
    }
    .minBy { it.second }!! // change to `maxBy` for second solution

fun Node.findRoute(remaining: Map<String, Node>,
                   route: List<String>,
                   distance: Int)
  : Pair<List<String>, Int> =
  when {
    remaining.isEmpty() -> Pair(route, distance)
    else -> paths.findRoute(remaining, route, distance)
  }

fun buildChart(input: String): Map<String, Node> =
  input
    .lines()
    .fold(mapOf<String, Node>()) { nodes, line ->
      val (start, end, distance) = parse(line)
      nodes.withRoute(start, end, distance)
    }

fun parse(line: String) =
  Regex("""(\w+) to (\w+) = (\d+)""")
    .matchEntire(line)!!
    .groups
    .run { Triple(get(1)!!.value, get(2)!!.value, get(3)!!.value.toInt()) }

fun Map<String, Node>.withRoute(start: String, end: String, distance: Int)
  : Map<String, Node> {
  val startNode = getOrElse(start) { Node(start) }.withPath(end, distance)
  val endNode = getOrElse(end) { Node(end) }.withPath(start, distance)
  return this + mapOf(start to startNode) + mapOf(end to endNode)
}

fun Node.withPath(destination: String, distance: Int) =
  run { copy(paths = paths + mapOf(destination to distance)) }

operator fun <K, V> Map<K, V>.minus(key: K) = filterNot { it.key == key }
