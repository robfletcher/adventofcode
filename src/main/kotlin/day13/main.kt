package day13

import java.text.DecimalFormat

val input = ClassLoader.getSystemResource("day13.txt").readText()

data class Person(val name: String, private val relationships: Map<String, Int> = emptyMap()) {
  fun happinessWith(other: Person) = relationships[other.name] ?: 0

  fun Person.withRelationship(name: String, score: Int) =
    copy(relationships = relationships + mapOf(name to score))
}

data class Layout(val seating: List<Person> = emptyList()) {
  val happiness by lazy {
    seating.sumBy {
      neighbors(it).run {
        it.happinessWith(first) + it.happinessWith(second)
      }
    }
  }

  operator fun plus(guest: Person) = copy(seating + guest)

  override fun toString(): String =
    seating
      .mapIndexed { i, p ->
        neighbors(p).run {
          "<(${format.format(p.happinessWith(first))}) " +
            p.name +
            " (${format.format(p.happinessWith(second))})>"
        }
      }
      .joinToString(" ") + " : " + format.format(happiness)

  private fun neighbors(person: Person): Pair<Person, Person> {
    val i = seating.indexOf(person)
    val left = (i - 1).let { i -> if (i < 0) seating.lastIndex else i }
    val right = (i + 1).let { i -> if (i > seating.lastIndex) 0 else i }
    return Pair(seating[left], seating[right])
  }
}

fun Collection<Person>.optimalLayout(layout: Layout = Layout()): Layout =
  when {
    isEmpty() -> layout
    else ->
      map { guest -> (this - guest).optimalLayout(layout + guest) }
        .maxBy { it.happiness }!!
  }

fun main(vararg args: String) {
  loadGuests(input)
    .apply { optimalLayout().apply(::println) }
    .let { it + Person("Me") }
    .optimalLayout().apply(::println)
}

val format = DecimalFormat("+#;-#")

val parser = Regex("""(\w+) would (gain|lose) (\d+) happiness units by sitting next to (\w+).""")

fun loadGuests(input: String): Set<Person> {
  val people = mutableMapOf<String, Person>()
  input
    .lines()
    .forEach { line ->
      parser.matchEntire(line)!!.groupValues.run {
        val name = get(1)
        val score = get(3).toInt() * if (get(2) == "gain") 1 else -1
        val other = get(4)
        val person = people.getOrPut(name) { Person(name) }.run {
          withRelationship(other, score)
        }
        people[name] = person
      }
    }
  return people.values.toSet()
}
