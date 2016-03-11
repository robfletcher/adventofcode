package day09

val input = ClassLoader.getSystemResource("day9.txt").readText()

fun main(vararg args: String) {
  buildChart(input)
    .let { chart ->
      chart.map { Path(it.name, 0) }.findRoute(chart)
    }
    .apply(::println)
}

/**
 * A node in the search space with zero or more outbound paths.
 */
data class Node(val name: String, val paths: Collection<Path> = emptySet()) {
  fun withPath(destination: String, distance: Int) =
    copy(paths = paths + Path(destination, distance))
}

/**
 * A route through a list of nodes in the search space with a summed distance.
 */
data class Route(val nodes: List<String> = emptyList(), val distance: Int = 0) {
  override fun toString() = nodes.joinToString(" -> ") + " = " + distance
  operator fun plus(path: Path) =
    copy(nodes + path.destination, distance + path.distance)
}

/**
 * A path to a new node with a distance.
 */
data class Path(val destination: String, val distance: Int)

/**
 * Find the shortest route building on [route] that visits all nodes in
 * [remaining].
 */
fun Collection<Path>.findRoute(remaining: Set<Node>, route: Route = Route())
  : Route =
  mapNotNull { path ->
    remaining.find { it.name == path.destination }
      ?.let {
        it.findRoute(remaining - it, route + path)
      }
  }
    .minBy { it.distance }!! // change to `maxBy` for second solution

/**
 * Find the shortest route building on [route] that visits all nodes in
 * [remaining].
 */
fun Node.findRoute(remaining: Set<Node>, route: Route) =
  when {
    remaining.isEmpty() -> route
    else -> paths.findRoute(remaining, route)
  }

/**
 * Build the chart of all nodes and paths based on the input data.
 */
fun buildChart(input: String) =
  input
    .lines()
    .fold(mapOf<String, Node>()) { nodes, line ->
      val (start, end, distance) = parse(line)
      nodes.withPath(start, end, distance)
    }
    .values
    .toSet()

/**
 * Parse a line of input data into a [Triple] representing _from_, _to_ and
 * _distance_ values.
 */
fun parse(line: String) =
  Regex("""(\w+) to (\w+) = (\d+)""")
    .matchEntire(line)!!
    .groups
    .run { Triple(get(1)!!.value, get(2)!!.value, get(3)!!.value.toInt()) }

/**
 * Upserts [Node]s for both ends of a path with [Path]s connecting them.
 */
fun Map<String, Node>.withPath(start: String, end: String, distance: Int)
  : Map<String, Node> {
  val startNode = getOrElse(start) { Node(start) }.withPath(end, distance)
  val endNode = getOrElse(end) { Node(end) }.withPath(start, distance)
  return this + mapOf(start to startNode) + mapOf(end to endNode)
}
