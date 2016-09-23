package ru.gurps.generator.desktop.models.rules

import ru.gurps.generator.desktop.config.Model
import java.sql.ResultSet

class Cultura : Model {
    override var id: Int = -1
    var name: String = ""
    var cost: Int = 0
    var add: Boolean = false

    constructor()
    constructor(result: ResultSet) {
        id = result.getInt("id")
        val nameRes = result.getString("name")
        if (nameRes != null) name = nameRes
    }

    constructor(name: String) {
        this.name = name
    }

    fun getCost(): String {
        return cost.toString()
    }
}
