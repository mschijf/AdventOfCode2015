package adventofcode2015

import kotlin.math.min

fun main() {
    Day19(test=false).showResult()
}

class Day19(test: Boolean) : PuzzleSolverAbstract(test) {
    private val replacementList = inputLines.dropLast(2).map { it.split(" => ") }.map { it[0] to it[1].toAtomList() }
    private val medicineMolecule = inputLines.last().toAtomList()

    override fun resultPartOne(): Any {
        return replacementList.flatMap{medicineMolecule.replaceBy(it).map{it.joinToString("")}}.distinct().size
    }

    override fun resultPartTwo(): Any {
        return shrinkRecursive(medicineMolecule)
    }

    private fun String.toAtomList() = this.windowed(size=2, partialWindows = true)
        .filter{it[0].isUpperCase()}
        .map {if (it.length == 1 || it[1].isLowerCase()) it else it[0].toString()}

    private fun List<String>.replaceBy(replacement: Pair<String, List<String>>): List<List<String>> {
        val result = mutableListOf<List<String>>()
        this.forEachIndexed { index, atom ->
            if (atom == replacement.first) {
                result.add((this.subList(0, index) + replacement.second + this.subList(index + 1, this.size)))
            }
        }
        return result
    }

    private fun List<String>.indexOf(atom: List<String>, startIndex: Int = 0): Int {
        for (i in startIndex.. this.size-atom.size) {
            var found = true
            for (j in atom.indices) {
                if (this[i+j] != atom[j]) {
                    found = false
                    break
                }
            }
            if (found)
                return i
        }
        return -1
    }

    /**
     * Very greedy algorithm
     * the replacement list is ordered descending by the replacement part. In the 'shrink', we therefore choose the biggest shrink first
     * 'the assumption' is that by doing this, teh first solution found is the best
     *
     * note: I run a full bruteforce (DFS), for a while. It found three times a 212 steps solution, and nothing else after running for an hour
     *       (but it didn't end as well, so ... still an assumption (?)
     *
     *       Read: https://www.happycoders.eu/algorithms/advent-of-code-2015/ for a very nice solution (needs analysis of the input)
     */
    private fun shrinkRecursive(current: List<String>): Int {

        if (current == listOf("e")) {
            return 0
        }

        replacementList.sortedByDescending { it.second.size }.forEach { (from, to) ->
            var ii = current.indexOf(to)
            while (ii != -1) {
                val shrinkedMolecule = current.reverse(ii, to.size, from)
                if (shrinkedMolecule.size == 1 || shrinkedMolecule.size < current.size) {
                    val tmp = 1 + shrinkRecursive(shrinkedMolecule)
                    if (tmp < Int.MAX_VALUE)
                        return tmp
                }
                ii = current.indexOf(to, ii + to.size)
            }
        }
        return Int.MAX_VALUE
    }


    private fun shrink(): Int {
        var xx = -1
        val queue = ArrayDeque<Pair<List<String>, Int>>().apply { this.add( Pair(medicineMolecule, 0)) }
        val visited = mutableSetOf<String>()

        while (queue.isNotEmpty()) {
            val (current, stepsDone) = queue.removeFirst()
            if (stepsDone != xx) {
                println("New stepsDone ($stepsDone) -> queueSize = ${queue.size}")
                xx = stepsDone
            }

            if (current == listOf("e")) {
                return stepsDone
            }

            if (current.size >= 1) {
                replacementList.forEach { (from, to) ->
                    var ii = current.indexOf(to)
                    while (ii != -1) {
                        val shrinkedMolecule = current.reverse(ii, to.size, from)
                        val shrinkedMoleculeString = shrinkedMolecule.joinToString("")
                        if (shrinkedMoleculeString !in visited) {
                            visited += shrinkedMoleculeString
                            queue.add(Pair(shrinkedMolecule, stepsDone + 1))
                        }
                        ii = current.indexOf(to, ii + to.size)
                    }
                }
            }
        }
        return -1
    }

    private fun List<String>.reverse(fromIndex: Int, length: Int, replaceBy: String) : List<String> {
        return this.subList(0, fromIndex) + replaceBy + this.subList(fromIndex+length, this.size)
    }

    private fun grow(): Int {
        var xx = -1
        val visited = mutableSetOf<String>()
        val queue = ArrayDeque<Pair<List<String>, Int>>().apply { this.add( Pair(listOf("e"), 0) ) }
        while (queue.isNotEmpty()) {
            val (currentMedicine, stepsDone)  = queue.removeFirst()
            if (currentMedicine == medicineMolecule)
                return stepsDone

            if (stepsDone != xx) {
                println("New stepsDone ($stepsDone) -> queueSize = ${queue.size}")
                xx = stepsDone
            }

            replacementList.flatMap{currentMedicine.replaceBy(it)}.forEach { newMedicine ->
                val newMedicineString = newMedicine.joinToString("")
                if ((newMedicine.size <= medicineMolecule.size) && (newMedicineString !in visited)) {
                    visited += newMedicineString
                    queue.add(Pair(newMedicine, stepsDone + 1))
                }
            }
        }
        return -1
    }
}


