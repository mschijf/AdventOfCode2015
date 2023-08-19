package adventofcode2015

import tool.mylambdas.hasOnlyDigits
import java.lang.Exception

fun main() {
    Day07(test = false).showResult()
}

class Day07(test: Boolean) : PuzzleSolverAbstract(test) {

    override fun resultPartOne(): Any {
        return Program(inputLines).valueOf("a")
    }

    override fun resultPartTwo(): Any {
        val overrideValueB = resultPartOne().toString()
        return Program(inputLines, "$overrideValueB -> b").valueOf("a")
    }
}

class Program(private val statementList: List<String>, override: String? = null) {
    private val statementMap =
        statementList.map { it.split("\\s".toRegex()) }.associateBy { it.last() } +
                if (override != null) {
                    listOf(override.split("\\s".toRegex())).associateBy { it.last() }
                } else {
                    emptyMap()
                }

    private val register = mutableMapOf<String, UShort>()

    //
    // recursive function. parameter 'word' can be a registerId or a number
    // to find a value for given registerId, you have to execute the corresponding statement (can be found in the statement Map)
    // the value of the operands of the statement, can recursively be found by calling this valueOf function.
    // finally, store the result in the registerMap.
    //

    fun valueOf(word: String): UShort {
        if (word.hasOnlyDigits()) {
            return word.toUShort()
        }
        if (register.contains(word)) {
            return register[word]!!
        }

        val statement = statementMap[word]!!
        val result = when (statement.getOperator()) {
            "SET" -> valueOf(statement[0])
            "NOT" -> valueOf(statement[1]).inv()
            "OR" -> valueOf(statement[0]) or valueOf(statement[2])
            "AND" -> valueOf(statement[0]) and valueOf(statement[2])
            "LSHIFT" -> (valueOf(statement[0]).toUInt() shl valueOf(statement[2]).toInt()).toUShort()
            "RSHIFT" -> (valueOf(statement[0]).toUInt() shr valueOf(statement[2]).toInt()).toUShort()
            else -> throw Exception("Unknown statement: $statement")
        }
        register[statement.getOutputRegister()] = result
        return result
    }

    private fun List<String>.getOperator() =
        when (this.size) {
            3 -> "SET"
            4 -> "NOT"
            else -> this[1]
        }

    private fun List<String>.getOutputRegister() =
        this.last().trim()
}

class Statement(operator: String, operand1: String, operand2: String?) {
    companion object {
        fun of(raw: String): Statement {
            val wordList = raw.split("\\s".toRegex())
            val (operator, operand1, operand2) = when (wordList.size) {
                3 -> Triple("SET", wordList[0], null)
                4 -> Triple("NOT", wordList[1], null)
                else -> Triple(wordList[1], wordList[0], wordList[2])
            }
            return Statement(operator, operand1, operand2)
        }
    }

}