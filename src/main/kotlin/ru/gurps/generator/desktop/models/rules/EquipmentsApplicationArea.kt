package ru.gurps.generator.desktop.models.rules

import ru.gurps.generator.desktop.config.Model
import java.sql.ResultSet

class EquipmentsApplicationArea : Model {
    override var id: Int = -1
    var name: String = ""

    constructor()
    constructor(result: ResultSet) {
        id = result.getInt("id")
        val nameRes = result.getString("name")
        if (nameRes != null) name = nameRes
    }
}
