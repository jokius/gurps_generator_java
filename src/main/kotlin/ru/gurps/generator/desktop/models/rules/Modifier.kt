package ru.gurps.generator.desktop.models.rules

import ru.gurps.generator.desktop.interfaces.models.Ability
import ru.gurps.generator.desktop.config.Model
import tornadofx.*
import java.sql.ResultSet

class Modifier : Model, Ability {
    override var id: Int = -1
    override var name: String = ""
    override var nameEn: String = ""
    override var cost: Int = 0
    override var description: String = ""
    var maxLevel: Int = 1
    var combat: Boolean = false
    var improving: Boolean = false
    var level: Int = 1
    override var currentCost: Int = 0
    override var add: Boolean = false
    var feature: Feature = Feature()

    constructor()
    constructor(result: ResultSet) {
        id = result.getInt("id")
        val nameRes = result.getString("name")
        if (nameRes != null) name = nameRes
        val nameEnRes = result.getString("nameEn")
        if (nameEnRes != null) nameEn = nameEnRes
        cost = result.getInt("cost")
        currentCost = cost
        val descriptionRes = result.getString("description")
        if (descriptionRes != null) description = descriptionRes
        maxLevel = result.getInt("maxLevel")
        combat = result.getBoolean("combat")
        improving = result.getBoolean("improving")
    }

    fun getCost(): String {
        return cost.toString()
    }

    fun getCombat(): String {
        return if (combat) FX.messages["yes"] else FX.messages["no"]
    }

    fun getImproving(): String {
        return if (improving) FX.messages["yes"] else FX.messages["no"]
    }

    fun featureCost(): Int {
        return Math.ceil(feature.cost * feature.level * (currentCost / 100.0)).toInt()
    }
}
