package adventofcode2015

fun main() {
    Day24(test=false).showResult()
}

class Day24(test: Boolean) : PuzzleSolverAbstract(test) {
    private val packageList = inputLines.map{ it.toLong() }.sortedDescending()

    override fun resultPartOne(): Any {
        val allGroups = determineGroups(packageList, packageList.sum()/3).groupBy { it.size }
        return allGroups.minBy { it.key }.value.minOf{it.reduceRight { i, acc -> i*acc }}
    }

    override fun resultPartTwo(): Any {
        val allGroups = determineGroups(packageList, packageList.sum()/4).groupBy { it.size }
        return allGroups.minBy { it.key }.value.minOf{it.reduceRight { i, acc -> i*acc }}
    }

    private fun determineGroups(packages: List<Long>, groupWeight: Long, group:List<Long> = emptyList() ): List<List<Long>> {
        return if (group.sum() == groupWeight) {
            if ((packageList-group.toSet()).canBeBalanced(groupWeight) ) listOf(group) else emptyList()
        } else if(group.sum() > groupWeight) {
            emptyList()
        } else {
            packages
                .flatMapIndexed { index, pack ->
                    determineGroups(packages.drop(index + 1), groupWeight, group + pack)
                }
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


