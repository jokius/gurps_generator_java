package ru.gurps.generator.desktop.models.characters

import ru.gurps.generator.desktop.config.Model
import java.sql.ResultSet

class CharactersAlchemy : Model {
    override var id: Int = -1
    var characterId: Int = -1
    var itemId: Int = -1

    constructor()
    constructor(characterId: Int, itemId: Int) {
        this.characterId = characterId
        this.itemId = itemId
    }

    constructor(result: ResultSet) {
        id = result.getInt("id")
        characterId = result.getInt("characterId")
        itemId = result.getInt("itemId")
    }
}
