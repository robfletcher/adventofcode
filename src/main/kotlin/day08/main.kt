package day08

val input = ClassLoader.getSystemResource("day8.txt").readText()

fun main(vararg args: String) {
  val diff1 = input.lines().map(::diffUnescaped).sum()
  println("diff in lengths when unescaping = $diff1")

  val diff2 = input.lines().map(::diffEscaped).sum()
  println("diff in lengths when escaping = $diff2")
}

fun diffUnescaped(s: String) =
  s.length - s.unescape().length

fun diffEscaped(s: String) =
  s.escape().length - s.length

fun String.unescape() =
  removePrefix("\"")
    .removeSuffix("\"")
    .replace(Regex("""(\\"|\\\\|\\x[0-9a-f]{2})"""), ".")

fun String.escape(): String =
  "\"${replace("\\", "\\\\").replace("\"", "\\\"")}\""