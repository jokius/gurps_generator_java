package ru.gurps.generator.desktop.models.characters

import ru.gurps.generator.desktop.config.Model
import java.sql.ResultSet

class CharactersAddon : Model {
    override var id: Int = -1
    var characterId: Int = -1
    var featureId: Int = -1
    var itemId: Int = -1
    var cost: Int = 0
    var level: Int = 0

    constructor()
    constructor(result: ResultSet) {
        id = result.getInt("id")
        characterId = result.getInt("characterId")
        featureId = result.getInt("featureId")
        itemId = result.getInt("itemId")
        cost = result.getInt("cost")
        level = result.getInt("level")
    }

    constructor(characterId: Int, featureId: Int, itemId: Int, cost: Int, level: Int) {
        this.characterId = characterId
        this.featureId = featureId
        this.itemId = itemId
        this.cost = cost
        this.level = level
    }
}
