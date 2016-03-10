package day10

val input = "1113222113"

fun main(vararg args: String) {
  val result = (1..40).fold(input) { s, i -> lookAndSay(s) }
  println(result.length)
}

fun lookAndSay(s: String) =
  Regex("""((\d)\2*)""")
    .findAll(s)
    .map { it.value }
    .fold("") { acc, next ->
      acc + next.length + next.first()
    }