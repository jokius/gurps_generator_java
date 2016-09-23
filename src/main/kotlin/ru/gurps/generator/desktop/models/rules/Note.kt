package ru.gurps.generator.desktop.models.rules

import ru.gurps.generator.desktop.config.Model
import java.sql.ResultSet

class Note : Model {
    override var id: Int = -1
    var itemType: String = ""
    var description: String = ""
    constructor()
    constructor(result: ResultSet) {
        id = result.getInt("id")
        val itemTypeRes = result.getString("itemType")
        if (itemTypeRes != null) itemType = itemTypeRes
        val descriptionRes = result.getString("description")
        if (descriptionRes != null) description = descriptionRes
    }
}
