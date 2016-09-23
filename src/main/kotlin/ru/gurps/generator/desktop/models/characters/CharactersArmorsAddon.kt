package ru.gurps.generator.desktop.models.characters

import ru.gurps.generator.desktop.interfaces.models.relationship.CharacterInventory
import ru.gurps.generator.desktop.config.Model
import java.sql.ResultSet

class CharactersArmorsAddon : Model, CharacterInventory {
    override var id: Int = -1
    override var characterId: Int = -1
    override var itemId: Int = -1
    override var count: Int = 0

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