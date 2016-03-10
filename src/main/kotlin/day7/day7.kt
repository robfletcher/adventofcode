package day7

val input = ClassLoader.getSystemResource("day7.txt").readText()

fun main(vararg args: String) {
  val circuit = Circuit(input.lines())
  println("The signal on wire a is ${circuit.signalOn("a")}")
}

class Circuit private constructor() {
  companion object {
    private val parser = Regex("""(?:(?:([a-z\d]+) )?(?:(AND|OR|[LR]SHIFT|NOT) )?([a-z\d]+)) -> ([a-z]+)""")
  }

  constructor(lines: Iterable<String>) : this() {
    lines.forEach {
      addConnection(it)
    }
  }

  private val signals: MutableMap<String, () -> Int> =
    mutableMapOf()

  fun signalOn(wire: String) =
    signals[wire]?.invoke() ?: 0

  private fun addConnection(line: String) {
    val match = parser.matchEntire(line)
    if (match == null) {
      throw IllegalStateException("Failed to parse $line")
    } else {
      val a = operand(match.groups[1]?.value)
      val op = operator(match.groups[2]?.value)
      val b = operand(match.groups[3]?.value)
      val wire = match.groups[4]!!.value
      signals[wire] = op(a, b)
    }
  }

  private fun operator(s: String?): ((() -> Int)?, (() -> Int)?) -> () -> Int =
    when (s) {
      null -> { a, b -> b!! }
      "NOT" -> { a, b -> { -> b!!.invoke().inv() } }
      "AND" -> { a, b -> { -> a!!.invoke() and b!!.invoke() } }
      "OR" -> { a, b -> { -> a!!.invoke() or b!!.invoke() } }
      "LSHIFT" -> { a, b -> { -> a!!.invoke() shl b!!.invoke() } }
      "RSHIFT" -> { a, b -> { -> a!!.invoke() shr b!!.invoke() } }
      else -> throw IllegalArgumentException("Unknown operator $s")
    }

  private fun operand(s: String?): (() -> Int)? =
    when {
      s == null -> null
      s.isNumeric() -> Memoized { -> s.toInt() }
      else -> Memoized { -> signals[s]?.invoke() ?: 0 }
    }

  fun String.isNumeric() = Regex("""\d+""").matches(this)
}

data class Memoized<T>(private val fn: () -> T) : () -> T {
  private val result: T by lazy { fn.invoke() }

  override fun invoke() = result
}

