package ru.gurps.generator.desktop.models.rules

import ru.gurps.generator.desktop.interfaces.models.Ability
import ru.gurps.generator.desktop.config.Model
import java.sql.ResultSet

class Addon : Model, Ability {
    override var id: Int = -1
    var featureId: Int = -1
    override var name: String = ""
    override var nameEn: String = ""
    override var cost: Int = 0
    override var currentCost: Int = 0
    override var description: String = ""
    var maxLevel: Int = 0
    var level = 1
    override var add: Boolean = false

    constructor()
    constructor(result: ResultSet) {
        id = result.getInt("id")
        featureId = result.getInt("featureId")
        val nameRes = result.getString("name")
        if (nameRes != null) name = nameRes
        val nameEnRes = result.getString("nameEn")
        if (nameEnRes != null) nameEn = nameEnRes
        cost = result.getInt("cost")
        currentCost = result.getInt("cost")
        val descriptionRes = result.getString("description")
        if (descriptionRes != null) description = descriptionRes
        maxLevel = result.getInt("maxLevel")
    }

    fun getLevel(): String {
        return level.toString()
    }

    fun getCost(): String {
        return "$currentCost%"
    }
}
