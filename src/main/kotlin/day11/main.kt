package day11

val input = "cqjxjnds"

fun main(vararg args: String) {
  print(input)
  (1..2).fold(input) { s: String, i: Int ->
    print(" -> ")
    s.next().apply(::print)
  }
}

fun String.next(): String {
  var s = this
  do {
    s = s.incr()
  } while (!s.valid())
  return s
}

val rules = listOf(
  { s: String -> s.contains(Regex("abc|bcd|cde|def|efg|fgh|ghi|hij|ijk|jkl|klm|lmn|mno|nop|opr|prs|rst|stu|tuv|uvw|vwx|wxy|xyz)")) },
  { s: String -> !s.contains(Regex("[iol]")) },
  { s: String -> s.contains(Regex("""(?:([a-z])\1).*(?:(?!\1\1)(?:([a-z])\2))""")) }
)

fun String.valid(): Boolean =
  rules.all { let(it) }

fun String.incr(tail: String = ""): String =
  when {
    isEmpty() -> 'a' + tail
    last() == 'z' -> slice(0 until lastIndex).incr('a' + tail)
    else -> slice(0 until lastIndex) + (last() + 1) + tail
  }