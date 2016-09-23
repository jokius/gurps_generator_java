package ru.gurps.generator.desktop.models.characters

import ru.gurps.generator.desktop.config.Model
import java.sql.ResultSet

class CharactersShield : Model {
    override var id: Int = -1
    var characterId: Int = -1
    var itemId: Int = -1
    var count: Int = 0

    constructor()
    constructor(characterId: Int, itemId: Int, count: Int) {
        this.characterId = characterId
        this.itemId = itemId
        this.count = count
    }

    constructor(result: ResultSet) {
        id = result.getInt("id")
        characterId = result.getInt("characterId")
        itemId = result.getInt("itemId")
        count = result.getInt("count")
    }
}