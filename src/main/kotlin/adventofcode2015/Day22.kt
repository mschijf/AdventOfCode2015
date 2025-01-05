package adventofcode2015.Day22

import adventofcode2015.PuzzleSolverAbstract
import java.util.Comparator
import java.util.PriorityQueue
import kotlin.math.max

fun main() {
    Day22(test=false).showResult()
}


//Puzzle Input (boss):
//  Hit Points: 51
//  Damage: 9


class Day22(test: Boolean) : PuzzleSolverAbstract(test) {

    //900 is correct
    override fun resultPartOne(): Any {
        val startGameStatus =
        if (test) {
            GameStatus(
                shieldTimer = 0,
                poisonTimer = 0,
                rechargeTimer = 0,

                manaSpend = 0,
                hitPoints = 10,
                manaPoints = 250,

                bossHitPoints = 14,
                damage = 8
            )
        } else {
            GameStatus(
                shieldTimer = 0,
                poisonTimer = 0,
                rechargeTimer = 0,

                hitPoints = 50,
                manaSpend = 0,
                manaPoints = 500,

                bossHitPoints = 51,
                damage = 9
            )
        }
        return run(startGameStatus)
    }

    override fun resultPartTwo(): Any {
        val startGameStatus =
                GameStatus(
                    shieldTimer = 0,
                    poisonTimer = 0,
                    rechargeTimer = 0,

                    hitPoints = 50,
                    manaSpend = 0,
                    manaPoints = 500,

                    bossHitPoints = 51,
                    damage = 9
                )
        return run(startGameStatus)
    }


    private fun run(startGameStatus: GameStatus): Int {
        val compareByCost: Comparator<GameStatus> = compareBy { it.manaSpend }
        val priorityQueue = PriorityQueue(compareByCost)

        priorityQueue.addAll(startGameStatus.nextStatuses())
        while (priorityQueue.isNotEmpty()) {
            val current = priorityQueue.remove()
            if (!current.bossStillAlive()) {
                //found!!
                return current.manaSpend
            }

            val statusAfterBossMove = current.doBossMove()
            if (!statusAfterBossMove.bossStillAlive()) {
                //found!!
                return current.manaSpend
            }

            if (statusAfterBossMove.playerStillAlive()) {
                statusAfterBossMove.nextStatuses().forEach { status ->
                    priorityQueue.add(status)
                }
            }
        }
        return -1
    }
}


data class GameStatus(
    val shieldTimer: Int,
    val poisonTimer: Int,
    val rechargeTimer: Int,

    val hitPoints: Int,
    val manaSpend: Int,
    val manaPoints: Int,

    val bossHitPoints: Int,
    val damage: Int,
) {

    override fun toString(): String {
        return "Player has $hitPoints hit points, 0 armor, $manaPoints mana\n" +
                "Boss has $bossHitPoints hit points"
    }

    fun doBossMove(): GameStatus {
        val newBossHitPoints = bossHitPoints - if (poisonTimer > 0) 3 else 0
        return GameStatus(
            shieldTimer = max(shieldTimer-1, 0),
            poisonTimer = max(poisonTimer-1, 0),
            rechargeTimer = max(rechargeTimer-1, 0),

            hitPoints = if (newBossHitPoints > 0)
                hitPoints - if (shieldTimer > 0) damage-7 else damage
            else
                hitPoints,
            manaSpend = manaSpend,
            manaPoints = manaPoints + if (rechargeTimer > 0) 101 else 0,

            bossHitPoints = newBossHitPoints,
            damage = damage
        )
    }

    fun bossStillAlive(): Boolean {
        return bossHitPoints > 0
    }

    fun playerStillAlive(): Boolean {
        return hitPoints > 0
    }

    fun nextStatuses(): List<GameStatus> {
        return listOfNotNull(
            magicMissile(), drain(), shield(), poison(), recharge()
        )
    }

    fun magicMissile(): GameStatus? {
        val cost = 53
        if (manaPoints >= cost) {
            return GameStatus(
                shieldTimer = max(shieldTimer-1, 0),
                poisonTimer = max(poisonTimer-1, 0),
                rechargeTimer = max(rechargeTimer-1, 0),

                hitPoints = hitPoints,
                manaSpend = manaSpend + cost,
                manaPoints = manaPoints  - cost + if (rechargeTimer > 0) 101 else 0,

                bossHitPoints = bossHitPoints - 4 - if (poisonTimer > 0) 3 else 0,
                damage = damage
            )
        }
        return null
    }

    fun drain(): GameStatus? {
        val cost = 73
        if (manaPoints >= cost) {
            return GameStatus(
                shieldTimer = max(shieldTimer-1, 0),
                poisonTimer = max(poisonTimer-1, 0),
                rechargeTimer = max(rechargeTimer-1, 0),

                hitPoints = hitPoints + 2,
                manaSpend = manaSpend + cost,
                manaPoints = manaPoints - cost + if (rechargeTimer > 0) 101 else 0,

                bossHitPoints = bossHitPoints - 2 - if (poisonTimer > 0) 3 else 0,
                damage = damage
            )
        }
        return null
    }


    fun shield(): GameStatus? {
        val cost = 113
        if (manaPoints >= cost && shieldTimer <= 1) {
            return GameStatus(
                shieldTimer = 6,
                poisonTimer = max(poisonTimer-1, 0),
                rechargeTimer = max(rechargeTimer-1, 0),

                hitPoints = hitPoints,
                manaSpend = manaSpend + cost,
                manaPoints = manaPoints - cost + if (rechargeTimer > 0) 101 else 0,

                bossHitPoints = bossHitPoints - if (poisonTimer > 0) 3 else 0,
                damage = damage
            )
        }
        return null
    }

    fun poison(): GameStatus? {
        val cost = 173
        if (manaPoints >= cost && poisonTimer <= 1) {
            return GameStatus(
                shieldTimer = max(shieldTimer-1, 0),
                poisonTimer = 6,
                rechargeTimer = max(rechargeTimer-1, 0),

                hitPoints = hitPoints,
                manaSpend = manaSpend + cost,
                manaPoints = manaPoints - cost + if (rechargeTimer > 0) 101 else 0,

                bossHitPoints = bossHitPoints - if (poisonTimer > 0) 3 else 0,
                damage = damage
            )
        }
        return null
    }


    fun recharge(): GameStatus? {
        val cost = 229
        if (manaPoints >= cost && rechargeTimer <= 1) {
            return GameStatus(
                shieldTimer = max(shieldTimer-1, 0),
                poisonTimer = max(poisonTimer-1, 0),
                rechargeTimer = 5,

                hitPoints = hitPoints,
                manaSpend = manaSpend + cost,
                manaPoints = manaPoints - cost + if (rechargeTimer > 0) 101 else 0,

                bossHitPoints = bossHitPoints - if (poisonTimer > 0) 3 else 0,
                damage = damage
            )
        }
        return null
    }

}


//
// data class Spell(val name: String, val cost: Int, val damage: Int, val armor: Int, val heals: Int, val moreMana: Int, val last: Int)
//
//
//private val spellList = listOf (
//    Spell("Magic Missile", 53, 4, 0, 0, 0,1),
//    Spell("Drain        ", 73, 2, 0, 2, 0, 1),
//    Spell("Shield       ", 113, 0, 7, 0, 0,6),
//    Spell("Poison       ", 173, 3, 0, 0, 0,6),
//    Spell("Recharge     ", 229, 0, 0, 0, 101,5),
//)
//
