package day01

val input = ClassLoader.getSystemResource("day1.txt").readText()

fun main(vararg args: String) {
  println("Final floor: ${input.calculateFloor()}")
  println("Hit basement on step ${input.findBasement()}")
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
