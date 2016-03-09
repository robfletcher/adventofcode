object day01 {
  @JvmStatic
  fun main(vararg args: String) {
    val input =
      javaClass.classLoader.getResource("day01/input")
        .readText()
    println("Final floor: ${input.calculateFloor()}")
    println("Hit basement on step ${input.findBasement()}")
  }
}

fun String.calculateFloor() =
  fold(0) { floor: Int, c: Char ->
    if (c == '(') floor + 1 else floor - 1
  }

fun String.findBasement(): Int {
  foldIndexed(0) { index: Int, floor: Int, c: Char ->
    if (floor < 0) return index
    if (c == '(') floor + 1 else floor - 1
  }
  return -1
}
