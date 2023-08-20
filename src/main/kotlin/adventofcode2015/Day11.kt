package adventofcode2015

fun main() {
    Day11(test=false).showResult()
}

class Day11(test: Boolean) : PuzzleSolverAbstract(test) {

    private val start = if (test) "ghijklmn" else "hxbxwxba"

    private val passWordgenerator = generateSequence(start) { it.nextWord() }

    override fun resultPartOne(): Any {
        return passWordgenerator.filter{ it.isLegal() }.first()
    }

    override fun resultPartTwo(): Any {
        return passWordgenerator.filter{ it.isLegal() }.take(2).last()
    }

    private fun String.nextWord(): String =
        if (this.isEmpty())
            ""
        else if (this.last() == 'z')
            this.dropLast(1).nextWord() + 'a'
        else
            this.dropLast(1) + (this.last() + 1)

    private fun String.isLegal() =
        this.all { ch -> ch.isLegal() } && this.hasThreeInARow() && this.hasTwoTwinLetters()

    private fun Char.isLegal() =
        this in "abcdefghjkmnpqrstuvwxyz"

    private fun String.hasThreeInARow(): Boolean {
        for (i in 2..< this.length) {
            if (this[i-2] == this[i-1]-1 && this[i-1] == this[i]-1)
                return true
        }
        return false
    }

    private fun String.hasTwoTwinLetters(): Boolean {
        var count = 0
        var i=1
        while (i < this.length) {
            if (this[i-1] == this[i]) {
                i+=2
                count++
            } else {
                i++
            }
        }
        return count >= 2
    }

}


