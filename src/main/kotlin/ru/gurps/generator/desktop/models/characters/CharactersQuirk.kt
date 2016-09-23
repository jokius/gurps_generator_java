package ru.gurps.generator.desktop.models.characters

import ru.gurps.generator.desktop.config.Model
import java.sql.ResultSet

class CharactersQuirk : Model {
    override var id: Int = -1
    var characterId: Int = -1
    var itemId: Int = -1
    var cost: Int = 0

    constructor()
    constructor(characterId: Int, itemId: Int, cost: Int) {
        this.characterId = characterId
        this.itemId = itemId
        this.cost = cost
    }

    constructor(result: ResultSet) {
        id = result.getInt("id")
        characterId = result.getInt("characterId")
        itemId = result.getInt("itemId")
        cost = result.getInt("cost")
    }
}
