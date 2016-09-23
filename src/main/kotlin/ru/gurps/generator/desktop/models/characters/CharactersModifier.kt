package ru.gurps.generator.desktop.models.characters

import ru.gurps.generator.desktop.config.Model
import java.sql.ResultSet

class CharactersModifier : Model {
    override var id: Int = -1
    var characterId: Int = -1
    var modifierId: Int = -1
    var featureId: Int = -1
    var cost: Int = 0
    var level: Int = 0

    constructor()

    constructor(characterId: Int, modifierId: Int, featureId: Int, cost: Int, level: Int) {
        this.characterId = characterId
        this.modifierId = modifierId
        this.featureId = featureId
        this.cost = cost
        this.level = level
    }

    constructor(result: ResultSet) {
        id = result.getInt("id")
        characterId = result.getInt("characterId")
        modifierId = result.getInt("modifierId")
        featureId = result.getInt("featureId")
        cost = result.getInt("cost")
        level = result.getInt("level")
    }
}
