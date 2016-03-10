package day10

val input = "1113222113"

fun main(vararg args: String) {
  (1..50)
    .generate(input, ::lookAndSay)
    .apply { println(length) }
}

fun lookAndSay(s: String) = s
  .fold(Triple(StringBuilder(), 0, s[0])) { acc, c ->
    val (out, count, ch) = acc
    when (ch) {
      c -> Triple(out, count + 1, c)
      else -> Triple(out.append(count).append(ch), 1, c)
    }
  }
  .run { first.append(second).append(third).toString() }

inline fun <T> IntRange.generate(initial: T, operation: (T) -> T) =
  fold(initial) { acc, ignored -> operation(acc) }
