package day3

val input = ClassLoader.getSystemResource("day3.txt").readText()

fun main(vararg args: String) {
  input
    .fold(Santa()) { santa, direction -> santa.move(direction) }
    .visited
    .apply {
      println("With 1 santa, visited $size houses")
    }

  val numHousesWithRobo = input
    .fold(Pair(Santa(), Santa())) { santas, direction ->
      Pair(santas.second, santas.first.move(direction))
    }
    .toList()
    .fold(setOf<Coordinate>()) { visited, santa -> visited + santa.visited }
    .size
  println("With 2 santas, visited $numHousesWithRobo houses")
}

data class Santa(
  val current: Coordinate = Coordinate(0, 0),
  val visited: Set<Coordinate> = setOf(current)) {
  fun move(direction: Char): Santa {
    val next = when (direction) {
      '^' -> current.copy(x = current.x + 1)
      '>' -> current.copy(y = current.y + 1)
      'v' -> current.copy(x = current.x - 1)
      '<' -> current.copy(y = current.y - 1)
      else -> throw IllegalArgumentException()
    }
    return copy(current = next, visited = visited + next)
  }
}

data class Coordinate(val x: Int, val y: Int)