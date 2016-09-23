package ru.gurps.generator.desktop.models.rules

import ru.gurps.generator.desktop.config.Model
import java.sql.ResultSet

class NoteItem : Model {
    override var id: Int = -1
    var itemType: String = ""
    var itemId: Int = -1
    var noteId: Int = -1
    
    constructor()
    constructor(result: ResultSet) {
        id = result.getInt("id")
        val itemTypeRes = result.getString("itemType")
        if (itemTypeRes != null) itemType = itemTypeRes
        itemId = result.getInt("itemId")
        noteId = result.getInt("noteId")
    }
}
