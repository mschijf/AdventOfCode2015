//package adventofcode2015.Day22
//
//import adventofcode2015.PuzzleSolverAbstract
//import java.util.PriorityQueue
//import kotlin.math.max
//
//fun main() {
//    Day22(test=false).showResult()
//}
//
//
////Puzzle Input (boss):
////  Hit Points: 51
////  Damage: 9
//
//class Day22(test: Boolean) : PuzzleSolverAbstract(test) {
//
//    private val spellList = listOf (
//        Spell("Magic Missile", 53, 4, 0, 0, 0,1),
//        Spell("Drain        ", 73, 2, 0, 2, 0, 1),
//        Spell("Shield       ", 113, 0, 7, 0, 0,6),
//        Spell("Poison       ", 173, 3, 0, 0, 0,6),
//        Spell("Recharge     ", 229, 0, 0, 0, 101,5),
//    )
//
//    override fun resultPartOne(): Any {
//        return ""
//    }
//
//
//
//    private fun run() {
//        val priorityQueue = PriorityQueue<Pair<Spell, Int>>()
//        priorityQueue.addAll(spellList.map { spell -> Pair(spell, spell.cost) })
//        val player = Player(50, 0, 0, 500)
//        val boss = Player(51, 9, 0)
//        while (boss.hitPoints > 0) {
//
//        }
//    }
//
//
//    private fun playerPlaysWithSpell(aSpell: Spell) {
//        add Spell to spell List
//        all spells are activated --> damages boss
//        if boss is dead -> game ends
//    }
//
//    private fun bossPlays() {
//        all spells are activated
//        if boss is dead -> game ends
//        boss does damage
//        if player is dead -> game ends
//    }
//
//    private fun Player.damages(other: Player) =
//        max(1, this.damageScore - other.armorScore)
//
//}
//
//data class Spell(val name: String, val cost: Int, val damage: Int, val armor: Int, val heals: Int, val moreMana: Int, val last: Int)
//
//data class Player(val hitPoints:Int, val damageScore: Int, val armorScore: Int, val mana: Int = 0) {
//
//}
//
//
