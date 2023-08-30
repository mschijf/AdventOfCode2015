package adventofcode2015

import tool.mylambdas.collectioncombination.mapCombinedItems
import kotlin.math.cos
import kotlin.math.max

fun main() {
    Day21(test=true).showResult()
}

//Puzzle Input:
//  Hit Points: 100
//  Damage: 8
//  Armor: 2

class Day21(test: Boolean) : PuzzleSolverAbstract(test) {

    private val weaponList = listOf(
        Item("Dagger", 8, 4, 0),
        Item("Shortsword", 10, 5, 0),
        Item("Warhammer", 25, 6, 0),
        Item("Longsword", 40, 7, 0),
        Item("Greataxe", 74, 8, 0))

    private val armorList = listOf(
        Item("Leather", 13, 0, 1),
        Item("Chainmail", 31, 0, 2),
        Item("Splintmail", 53, 0, 3),
        Item("Bandedmail", 75, 0, 4),
        Item("Platemail", 102, 0, 5))

    private val ringList = listOf(
        Item("Damage +1", 25, 1, 0),
        Item("Damage +2", 50, 2, 0),
        Item("Damage +3", 100, 3, 0),
        Item("Defense +1", 20, 0, 1),
        Item("Defense +2", 40, 0, 2),
        Item("Defense +3", 80, 0, 3))

    private val emptyItem = Item("No Item", 0,0,0)

    private val boss = Player(100, 8, 2)

    override fun resultPartOne(): Any {
        return makePlayerList().filter{player -> player.beats(boss)}.minOf { player -> player.cost }
    }

    override fun resultPartTwo(): Any {
        return makePlayerList().filter{player -> !player.beats(boss)}.maxOf { player -> player.cost }
    }

    private fun Player.beats(other: Player): Boolean {
        var playerHitPoints = this.hitPoints
        var opponentHitPoints= other.hitPoints
        var playerToMove = true
        while (playerHitPoints > 0 && opponentHitPoints > 0) {
            if (playerToMove) {
                opponentHitPoints -= this.damages(other)
            } else {
                playerHitPoints -= other.damages(this)
            }
            playerToMove = !playerToMove
        }
        return (playerHitPoints > 0)
    }

    private fun Player.damages(other: Player) =
        max(1, this.damageScore - other.armorScore)

    private fun makePlayerList(): List<Player> {
        return weaponList.flatMap {weapon ->
            (armorList+emptyItem).flatMap { armor ->
                (ringList+emptyItem+emptyItem).mapCombinedItems { ring1, ring2 ->
                    Player(
                        hitPoints = 100,
                        damageScore = weapon.damage+ring1.damage+ring2.damage,
                        armorScore = armor.armor+ring1.armor+ring2.armor,
                        cost =  weapon.cost+armor.cost+ring1.cost+ring2.cost,
                        itemList = listOf(weapon, armor, ring1, ring2)
                    )
                }
            }
        }
    }
}

data class Item(val name: String, val cost: Int, val damage: Int, val armor: Int)

data class Player(val hitPoints:Int, val damageScore: Int, val armorScore: Int, val cost: Int = 0, val itemList : List<Item> = emptyList())


