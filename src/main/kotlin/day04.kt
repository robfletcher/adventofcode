import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest

object day04 {
  @JvmStatic
  fun main(vararg args: String) {
    println(mine("iwrupvqb", 5))
    println(mine("iwrupvqb", 6))
  }
}

fun mine(secret: String, len: Int): Int {
  val prefix = "".padEnd(len, '0')
  var i = 0
  do {
    val hash = md5("$secret${++i}")
  } while (!hash.startsWith(prefix))
  return i
}

val digest = MessageDigest.getInstance("MD5")
val charset = Charset.forName("UTF-8")
fun md5(s: String): String = digest.digest(s.toByteArray(charset)).toHex()

fun ByteArray.toHex() = BigInteger(1, this).toString(16).padStart(32, '0')