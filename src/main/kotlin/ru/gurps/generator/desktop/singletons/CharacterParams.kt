package ru.gurps.generator.desktop.singletons

import javafx.collections.ObservableList
import ru.gurps.generator.desktop.models.rules.*
import tornadofx.FX.Companion.messages
import tornadofx.get

object CharacterParams {
    init { println("This Character params is load") }
    var character = Characters.current!!

    fun stCost(): Int {
        var cost = (character.st - 10) * 10
        if (cost == 0) return cost
        if (character.noFineManipulators) cost = (cost - cost * 0.4).toInt()
        return costThroughSm(cost)
    }

    fun dxCost(): Int {
        var cost = (character.dx - 10) * 20
        if (character.noFineManipulators && cost != 0) cost = (cost - cost * 0.4).toInt()
        return cost
    }

    fun iqCost(): Int {
        return (character.iq - 10) * 20
    }

    fun htCost(): Int {
        return (character.ht - 10) * 10
    }

    fun hpCost(): Int {
        val cost = (character.hp - character.st) * 2
        if (cost == 0) return cost
        return costThroughSm(cost)
    }

    fun willCost(): Int {
        return (character.will - character.iq) * 5
    }

    fun perCost(): Int {
        return (character.per - character.iq) * 5
    }

    fun fpCost(): Int {
        return (character.fp - character.ht) * 3
    }

    fun defaultBs(): Double {
        return (character.dx + character.ht) / 4.0
    }

    fun bsCost(): Int {
        var cost = 0
        var periods: Double
        periods = character.bs - defaultBs()

        if (periods >= 0.25) {
            do {
                periods -= 0.25
                cost += 5
            } while (periods >= 0.25)
        } else if (periods <= -0.25) {
            do {
                periods += 0.25
                cost -= 5
            } while (periods <= -0.25)
        }
        return cost
    }

    fun moveCost(): Int {
        return (character.move - character.bs).toInt()
    }

    fun bg(): Int {
        return character.st * character.st / 5
    }

    fun doge(): Int {
        return (character.bs + 3).toInt()
    }

    fun parry(): String {
        var resultSkill = character.dx - 4
        character.skills().filter { it.parry && it.level > resultSkill }
                .forEach { if(it.level > resultSkill) resultSkill = it.level }
        return (3 + resultSkill / 2).toString()
    }

    fun block(): String {
        var shield = character.dx - 4
        var cloak = 0
        character.skills().forEach {
            when (it.nameEn) {
                "Shield" -> {
                    if(it.level > shield) shield = it.level
                    if(it.level > cloak) cloak = it.level
                }
                "Cloak" -> {
                    if(it.level > cloak) cloak = it.level
                    if (cloak == 0) cloak = it.level - 4
                }
                "Net" -> if ((it.level - 4) > cloak) cloak = it.level - 4
            }
        }

        if (cloak == 0) cloak = character.dx - 5
        return if (shield >= cloak)  (3 + shield / 2).toString()
        else (3 + cloak / 2).toString()
    }

    fun skillLevel(skill: Specialization): Int {
        return skillLevelResult(skill._skillType, skill._complexity)
    }

    fun skillLevel(skill: Skill): Int {
        return skillLevelResult(skill._skillType, skill._complexity)
    }

    fun skillCost(skill: Skill): Int {
        return skillCostResult(skill._skillType, skill._complexity, skill.level)
    }

    fun skillCost(skill: Specialization): Int {
        return skillCostResult(skill._skillType, skill._complexity, skill.level)
    }

    fun techniqueCost(technique: Technique): Int {
        if (technique.level == 0) return 0
        return if (technique._complexity == 1)
            technique.level
        else
            technique.level + 1
    }

    fun spellLevelResult(complexity: Int): Int {
        return when (complexity) {
            0 -> character.iq
            1 -> character.iq - 1
            2 -> character.iq - 2
            3 -> character.iq - 3
            else -> 0
        }
    }

    fun spellCost(spell: Spell): Int {
        if (spell._complexity == 2) {
            when {
                spell.level <= character.iq - 2 -> return 1
                spell.level == character.iq - 1 -> return 2
                spell.level == character.iq -> return 4
                spell.level == character.iq + 1 -> return 8
                spell.level == character.iq + 2 -> return 12
                spell.level == character.iq + 3 -> return 16
                else -> {
                    var i = spell.level
                    var cost = 16
                    while (i > character.iq + 3) {
                        cost += 4
                        i--
                    }
                    return cost
                }
            }

        } else {
            when {
                spell.level <= character.iq - 3 -> return 1
                spell.level == character.iq - 2 -> return 2
                spell.level == character.iq - 1 -> return 4
                spell.level == character.iq -> return 8
                spell.level == character.iq + 1 -> return 12
                spell.level == character.iq + 2 -> return 16
                spell.level == character.iq + 3 -> return 20
                else -> {
                    var i = spell.level
                    var cost = 20
                    while (i == character.iq + 3) {
                        cost += 4
                        i--
                    }
                    return cost
                }
            }
        }
    }

    fun featureFullName(feature: Feature): String {
        val modifiers = feature.modifiers(character.id)
        if (modifiers.size == 0) return feature.name

        var fullName = feature.name + " ("
        for (modifier in modifiers) {
            fullName += "${modifier.name}, ${modifier.cost}%, ${messages["level_colon"]}${modifier.level}; "
        }

        fullName = fullName.substring(0, fullName.length - 2) + ")"
        return fullName
    }

    private fun skillLevelResult(type: Int, complexity: Int): Int {
        val parameter = when(type) {
            0 ->  character.st
            1 -> character.dx
            2 -> character.iq
            3 -> character.ht
            4 -> character.hp
            5 -> character.will
            6 -> character.per
            7 -> character.fp
            else -> 0
        }

        return when (complexity) {
            0 -> parameter
            1 -> parameter - 1
            2 -> parameter - 2
            3 -> parameter - 3
            else -> 0
        }
    }

    private fun skillCostResult(type: Int, complexity: Int, level: Int): Int {
        val parameter = when (type) {
            0 -> character.st
            1 -> character.dx
            2 -> character.iq
            3 -> character.ht
            4 -> character.hp
            5 -> character.will
            6 -> character.per
            7 -> character.fp
            else -> 0
        }

        var i = 0
        var cost = 0

        when (complexity) {
            0 -> {
                when {
                    level <= parameter -> return 1
                    level <= parameter + 1 -> return 2
                    level <= parameter + 2 -> return 4
                    level <= parameter + 3 -> return 8
                    else -> {
                        i = level
                        cost = 8
                    }
                }
            }

            1 -> {
                when {
                    level <= parameter - 1 -> return 1
                    level <= parameter -> return 2
                    level <= parameter + 1 -> return 4
                    level <= parameter + 2 -> return 8
                    level <= parameter + 3 -> return 12
                    else -> {
                        i = level
                        cost = 12
                    }
                }
            }

            2 -> {
                when {
                    level <= parameter - 2 -> return 1
                    level <= parameter - 1 -> return 2
                    level <= parameter -> return 4
                    level <= parameter + 1 -> return 8
                    level <= parameter + 2 -> return 12
                    level <= parameter + 3 -> return 16
                    else -> {
                        i = level
                        cost = 16
                    }
                }
            }

            3 -> {
                when {
                    level <= parameter - 3 -> return 1
                    level <= parameter - 2 -> return 2
                    level <= parameter - 1 -> return 4
                    level <= parameter -> return 8
                    level <= parameter + 1 -> return 12
                    level <= parameter + 2 -> return 16
                    level <= parameter + 3 -> return 20
                    else -> {
                        i = level
                        cost = 20
                    }
                }
            }
        }

        while (i > parameter + 3) {
            cost += 4
            i--
        }

        return cost
    }

    private fun costThroughSm(startCost: Int): Int {
        var cost = startCost
        if (character.sm == 0) return cost
        cost = if (character.sm in 1..7 || character.sm in -7..-1)
            (cost - cost * (0.1 * character.sm)).toInt()
        else if (character.sm <= -8)
            (cost - cost * -0.8).toInt()
        else
            (cost - cost * 0.8).toInt()
        return cost
    }
}
