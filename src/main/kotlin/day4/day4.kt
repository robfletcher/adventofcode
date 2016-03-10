package day4

import java.math.BigInteger
import java.nio.charset.Charset
import java.security.MessageDigest

val input = ClassLoader.getSystemResource("day4.txt").readText()

fun main(vararg args: String) {
  println(mine(input, 5))
  println(mine(input, 6))
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