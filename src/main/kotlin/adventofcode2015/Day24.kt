package adventofcode2015

import java.time.DayOfWeek

fun main() {
    Day24(test=false).showResult()
}

class Day24(test: Boolean) : PuzzleSolverAbstract(test) {
    private val packageList = inputLines.map{ it.toLong() }.sortedDescending()

    override fun resultPartOne(): Any {
        val allGroups = determineGroups(packageList, packageList.sum()/3).groupBy { it.size }
        return allGroups.minBy { it.key }.value.map{it.reduceRight { i, acc -> i*acc }}.min()
    }

    override fun resultPartTwo(): Any {
        val allGroups = determineGroups(packageList, packageList.sum()/4).groupBy { it.size }
        return allGroups.minBy { it.key }.value.map{it.reduceRight { i, acc -> i*acc }}.min()
    }

    private fun determineGroups(packages: List<Long>, groupWeight: Long, group:List<Long> = emptyList() ): List<List<Long>> {
        if (group.sum() == groupWeight) {
            return if ((packageList-group).canBeBalanced(groupWeight) ) listOf(group) else emptyList()
        } else if(group.sum() > groupWeight) {
            return emptyList()
        } else {
            val result = mutableListOf<List<Long>>()
            packages.forEachIndexed { index, pack ->
                val tmp = determineGroups(packages.drop(index+1), groupWeight,group+pack)
                result.addAll(tmp)
            }
            return result
        }
    }


    private fun List<Long>.canBeBalanced(groupWeight: Long, sum: Long = 0): Boolean {
        if (sum == groupWeight) {
            return true
        } else if(sum > groupWeight) {
            return false
        } else {
            this.forEachIndexed { index, pack ->
                val found = this.drop(index+1).canBeBalanced(groupWeight, sum+pack)
                if (found)
                    return true
            }
            return false
        }
    }
}


