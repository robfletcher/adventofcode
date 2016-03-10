package day05

val input = ClassLoader.getSystemResource("day5.txt").readText()

fun main(vararg args: String) {
  val words = input.lines()
  println("${words.count(::nice1)} of ${words.size} words are nice (original rules)")
  println("${words.count(::nice2)} of ${words.size} words are nice (revised rules)")
}

val vowels = "aeiou"
val doubles = Regex("""([a-z])\1""")
val banned = Regex("(ab)|(cd)|(pq)|(xy)")

fun nice1(s: String) =
  s.count { it in vowels } > 2
    && doubles.containsMatchIn(s)
    && !banned.containsMatchIn(s)

val twoPair = Regex("""([a-z][a-z])[a-z]*\1""")
val sandwich = Regex("""([a-z])[a-z]\1""")

fun nice2(s: String) =
  twoPair.containsMatchIn(s)
    && sandwich.containsMatchIn(s)