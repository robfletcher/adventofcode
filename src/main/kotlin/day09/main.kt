package day09

val input = ClassLoader.getSystemResource("day9.txt").readText()

data class Node(val name: String, val paths: Collection<Path> = emptySet())
data class Route(val nodes: List<String> = emptyList(), val distance: Int = 0) {
  override fun toString() = nodes.joinToString(" -> ") + " = " + distance
}

data class Path(val destination: String, val distance: Int)

fun main(vararg args: String) {
  buildChart(input)
    .let { chart ->
      chart.map { Path(it.name, 0) }.findRoute(chart)
    }
    .apply(::println)
}

fun Collection<Path>.findRoute(remaining: Set<Node>, route: Route = Route())
  : Route =
  mapNotNull { path ->
    remaining.find { it.name == path.destination }
      ?.let {
        it.findRoute(remaining - it, route + path)
      }
  }
    .minBy { it.distance }!! // change to `maxBy` for second solution

fun Node.findRoute(remaining: Set<Node>, route: Route) =
  when {
    remaining.isEmpty() -> route
    else -> paths.findRoute(remaining, route)
  }

operator fun Route.plus(path: Path) =
  copy(nodes + path.destination, distance + path.distance)

fun buildChart(input: String): Set<Node> =
  input
    .lines()
    .fold(mapOf<String, Node>()) { nodes, line ->
      val (start, end, distance) = parse(line)
      nodes.withPath(start, end, distance)
    }
    .values
    .toSet()

fun parse(line: String) =
  Regex("""(\w+) to (\w+) = (\d+)""")
    .matchEntire(line)!!
    .groups
    .run { Triple(get(1)!!.value, get(2)!!.value, get(3)!!.value.toInt()) }

fun Map<String, Node>.withPath(start: String, end: String, distance: Int)
  : Map<String, Node> {
  val startNode = getOrElse(start) { Node(start) }.withPath(end, distance)
  val endNode = getOrElse(end) { Node(end) }.withPath(start, distance)
  return this + mapOf(start to startNode) + mapOf(end to endNode)
}

fun Node.withPath(destination: String, distance: Int) =
  run { copy(paths = paths + Path(destination, distance)) }
