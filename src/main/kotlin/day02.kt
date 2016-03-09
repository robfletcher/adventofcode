object day02 {

  @JvmStatic
  fun main(vararg args: String) {
    val boxes = javaClass.classLoader.getResource("day02/input")
      .readText()
      .lines()
      .map { Box.parse(it) }
    val area = boxes
      .map { it.area() + it.overflow() }
      .sum()
    println("Require $area sq feet of paper")
    val length = boxes
      .map { it.circumference() + it.volume() }
      .sum()
    println("Require $length feet of ribbon")
  }

  data class Box(val w: Int, val h: Int, val l: Int) {
    constructor(vararg args: Int) : this(args[0], args[1], args[2])

    companion object {
      fun parse(s: String): Box =
        Box(*s.split('x').map { it.toInt() }.toIntArray())
    }

    private val sides = sequenceOf(l, w, h)
    private val surfaces = sequenceOf(l * w, w * h, h * l)

    fun area() = surfaces.sum() * 2

    fun overflow() = surfaces.min()!!

    fun circumference() = (sides.sum() - sides.max()!!) * 2

    fun volume() = l * w * h
  }

}