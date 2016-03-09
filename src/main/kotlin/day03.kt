object day03 {

  @JvmStatic
  fun main(vararg args: String) {
    val script = javaClass.classLoader.getResource("day03/input")
      .readText()

    val numHouses = script
      .fold(Santa()) { santa, direction -> santa.move(direction) }
      .visited
      .size
    println("With 1 santa, visited $numHouses houses")

    val numHousesWithRobo = script
      .fold(Pair(Santa(), Santa())) { santas, direction ->
        Pair(santas.second, santas.first.move(direction))
      }
      .toList()
      .fold(setOf<Coordinate>()) { visited, santa -> visited + santa.visited }
      .size
    println("With 2 santas, visited $numHousesWithRobo houses")
  }
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