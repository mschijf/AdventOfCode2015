package adventofcode2015

fun main() {
    Day20(test=false).showResult()
}

/**
 * Net als met priemgetallen bepalen, kan je hier beter een zeef constructie gebruiken, ipv voor een huisnummer uitrekenen
 * hoeveel pakjes het huis krijgt. Het laatste is wel mooier qua code (want in één a twee regels te vangen), maar wel veel langzamer
 *
 */

class Day20(test: Boolean) : PuzzleSolverAbstract(test) {
    private val puzzleInput = if (test) 150 else 34_000_000

    override fun resultPartOne(): Any {
        val houseList = deliverPresents(1_000_000)
        return houseList.indexOfFirst { it >= puzzleInput }
    }

    override fun resultPartTwo(): Any {
        val houseList = deliverPresentsPart2(1_000_000)
        return houseList.indexOfFirst { it >= puzzleInput }
    }

    private fun deliverPresents(numberOfHouses: Int): IntArray {
        val houseList = IntArray(numberOfHouses)
        for (elfNr in 1..< numberOfHouses) {
            for (houseNr in elfNr..< numberOfHouses step elfNr) {
                houseList[houseNr] += elfNr*10
            }
        }
        return houseList
    }

    private fun deliverPresentsPart2(numberOfHouses: Int): IntArray {
        val houseList = IntArray(numberOfHouses)
        for (elfNr in 1..< numberOfHouses) {
            var countPresents = 0
            for (houseNr in elfNr..< numberOfHouses step elfNr) {
                houseList[houseNr] += elfNr*11
                countPresents++
                if (countPresents == 50)
                    break
            }
        }
        return houseList
    }


    // als je het aantal pakjes per huis wilt berekenen zonder zeef-constructie
    //        return generateSequence(1){it+1}.first { it.houseToPresent() >= puzzleInput }
    private fun Int.houseToPresent(): Int {
        return (1..this).filter{this % it == 0}.sum()*10
    }

}


