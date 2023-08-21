package adventofcode2015

import java.lang.StringBuilder

fun main() {
    Day12(test=false).showResult()
}

class Day12(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        val t = Thing.of(inputLines.first())
        return t.sumOfNum()
    }
}



abstract open class Thing {
    abstract fun sumOfNum(): Int
    abstract fun hasRed(): Boolean

    companion object {
        fun of(rawInput: String): Thing {
            return when (rawInput[0]) {
                '[' -> MyArray.of(rawInput)
                '{' -> MyObject.of(rawInput)
                '"' -> MyString.of(rawInput)
                else -> MyNumber.of(rawInput)
            }
        }
    }
}

class MyString(val value: String): Thing() {
    override fun sumOfNum() = 0
    override fun hasRed() = (value == "red")

    companion object {
        fun of(rawInput: String): MyString {
            return MyString(rawInput.removeSurrounding("\""))
        }
    }
}

class MyNumber(val value: Int): Thing() {
    override fun hasRed() = false
    override fun sumOfNum() = value

    companion object {
        fun of(rawInput: String): MyNumber {
            return MyNumber(rawInput.toInt())
        }
    }
}

class MyArray(val list: List<Thing>): Thing() {
    override fun hasRed() = false
    override fun sumOfNum() = list.sumOf { it.sumOfNum() }

    companion object {
        fun of(rawInput: String): MyArray {
            val list = rawInput.splitLevel('[', ']')
            return MyArray(list.map{Thing.of(it)})
        }
    }
}

class MyObject(val map: Map<String, Thing>): Thing() {
    override fun hasRed() = false
    override fun sumOfNum() = if (map.values.none { it.hasRed() }) map.values.sumOf { it.sumOfNum() } else 0

    companion object {
        fun of(rawInput: String): MyObject {
            val list = rawInput.splitLevel('{', '}')
            return MyObject(list.associate{ it.substringBefore(":").removeSurrounding("\"") to Thing.of(it.substringAfter(":") ) })
        }
    }
}

private fun String.splitLevel(symbolOpen: Char, symbolClose: Char): List<String> {
    var level = 0
    val result = mutableListOf<String>()
    val item = StringBuilder()
    this.forEach { char ->
        when (char) {
            '{', '[' -> {
                if (level > 0) {
                    item.append(char)
                }
                level++
            }
            '}', ']' -> {
                level--
                if (level == 0) {
                    result.add(item.toString())
                    return result
                } else {
                    item.append(char)
                }
            }
            ',' -> {
                if (level == 1) {
                    result.add(item.toString())
                    item.clear()
                } else {
                    item.append(char)
                }
            }
            else -> {
                item.append(char)
            }
        }
    }
    throw Exception("Unexpected end of String parsing")
}




