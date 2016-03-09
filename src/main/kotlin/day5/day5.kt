package day5

val input = ClassLoader.getSystemResource("day5.txt").readText()

fun main(vararg args: String) {
  val words = input.lines()
  println("${words.count { it.nice1() }} of ${words.size} words are nice (original rules)")
  println("${words.count { it.nice2() }} of ${words.size} words are nice (revised rules)")
}

val vowels = "aeiou".toCharArray()
val doubleLetters = Regex("([a-z])\\1")
val bannedPhrases = Regex("(ab)|(cd)|(pq)|(xy)")

fun String.nice1() =
  count { it in vowels } > 2
    && doubleLetters.containsMatchIn(this)
    && !bannedPhrases.containsMatchIn(this)

val twoPairs = Regex("([a-z][a-z]).*\\1")
val palindrome = Regex("([a-z]).\\1")

fun String.nice2() =
  twoPairs.containsMatchIn(this)
    && palindrome.containsMatchIn(this)