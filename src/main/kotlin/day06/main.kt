package day06

import java.lang.Math.max

val input = ClassLoader.getSystemResource("day6.txt").readText()

fun main(vararg args: String) {
  val commands = input.lines()

  val grid1 = Grid1()
  commands.forEach { grid1.execute(it) }
  println("${grid1.luminance()} lights are lit!")

  val grid2 = Grid2()
  commands.forEach { grid2.execute(it) }
  println("grid luminance is ${grid2.luminance()}")
}

abstract class Grid {
  private val parser = Regex("(turn on|turn off|toggle) (\\d+,\\d+) through (\\d+,\\d+)")

  fun execute(command: String) {
    val (cmd, from, to) = parser.matchEntire(command)!!.destructured
    Coordinate.of(from).through(Coordinate.of(to)) {
      when (cmd) {
        "turn on" -> `turn on`(it)
        "turn off" -> `turn off`(it)
        "toggle" -> toggle(it)
      }
    }
  }

  abstract fun luminance(): Int
  abstract fun `turn on`(pos: Coordinate): Unit
  abstract fun `turn off`(pos: Coordinate): Unit
  abstract fun toggle(pos: Coordinate): Unit
}

class Grid1 : Grid() {
  private val lights = mutableMapOf<Coordinate, Boolean>()

  override fun luminance() = lights.count { it.value }

  override fun `turn on`(pos: Coordinate) {
    lights[pos] = true
  }

  override fun `turn off`(pos: Coordinate) {
    lights[pos] = false
  }

  override fun toggle(pos: Coordinate) {
    lights[pos] = !lights[pos]
  }

  operator fun Boolean?.not() = if (this == true) false else true
}

class Grid2 : Grid() {
  private val lights = mutableMapOf<Coordinate, Int>()

  override fun luminance() = lights.values.sum()

  override fun `turn on`(pos: Coordinate) {
    lights[pos] = lights[pos] + 1
  }

  override fun `turn off`(pos: Coordinate) {
    lights[pos] = max(lights[pos] - 1, 0)
  }

  override fun toggle(pos: Coordinate) {
    lights[pos] = lights[pos] + 2
  }

  operator fun Int?.plus(other: Int) = if (this == null) other else this + other
  operator fun Int?.minus(other: Int) = if (this == null) -other else this - other
}

data class Coordinate(val x: Int, val y: Int) {
  companion object {
    fun of(s: String): Coordinate =
      Coordinate(s.substringBefore(',').toInt(), s.substringAfter(',').toInt())
  }

  fun through(other: Coordinate, callback: (Coordinate) -> Unit) =
    (x..other.x).forEach { x ->
      (y..other.y).forEach { y ->
        callback.invoke(Coordinate(x, y))
      }
    }

  override fun toString() = "$x,$y"
}