package ru.gurps.generator.desktop.models.characters

import ru.gurps.generator.desktop.config.Model
import java.sql.ResultSet

class CharactersLanguage : Model {
    override var id: Int = -1
    var characterId: Int = -1
    var itemId: Int = -1
    var spoken: Int = 0
    var written: Int = 0
    var cost: Int = 0

    constructor()
    constructor(characterId: Int, itemId: Int, spoken: Int, written: Int, cost: Int) {
        this.characterId = characterId
        this.itemId = itemId
        this.spoken = spoken
        this.written = written
        this.cost = cost
    }

    constructor(result: ResultSet) {
        id = result.getInt("id")
        characterId = result.getInt("characterId")
        itemId = result.getInt("itemId")
        spoken = result.getInt("spoken")
        written = result.getInt("written")
        cost = result.getInt("cost")
    }
}
