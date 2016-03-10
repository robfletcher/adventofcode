package day12

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.net.URL

val input = ClassLoader.getSystemResource("day12.json").readJSON()

fun main(vararg args: String) {
  input.sum().apply(::println)
  input.sumIgnoreRed().apply(::println)
}

fun JsonNode.sum(): Int =
  when {
    isInt -> intValue()
    isArray -> sumBy { it.sum() }
    isObject -> sumBy { it.sum() }
    else -> 0
  }

fun JsonNode.sumIgnoreRed(): Int =
  when {
    isInt -> intValue()
    isArray -> sumBy { it.sumIgnoreRed() }
    any { it.textValue() == "red" } -> 0
    isObject -> sumBy { it.sumIgnoreRed() }
    else -> 0
  }

fun URL.readJSON() = jacksonObjectMapper().readTree(this)
