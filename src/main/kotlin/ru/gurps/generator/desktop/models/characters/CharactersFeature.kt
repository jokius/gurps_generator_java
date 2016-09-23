package ru.gurps.generator.desktop.models.characters

import javafx.collections.ObservableList
import ru.gurps.generator.desktop.config.Model
import java.sql.ResultSet
import java.util.*

class CharactersFeature : Model {
    override var id: Int = -1
    var characterId: Int = -1
    var itemId: Int = -1
    var cost: Int = -1
    var level: Int = -1
    var advantage: Boolean = true

    constructor()
    constructor(characterId: Int, itemId: Int, cost: Int, level: Int, advantage: Boolean) {
        this.characterId = characterId
        this.itemId = itemId
        this.cost = cost
        this.level = level
        this.advantage = advantage
    }

    constructor(result: ResultSet) {
        id = result.getInt("id")
        characterId = result.getInt("characterId")
        itemId = result.getInt("itemId")
        cost = result.getInt("cost")
        level = result.getInt("level")
        advantage = result.getBoolean("advantage")
    }

    private fun destroyChildren() {
        val params = HashMap<String, Any>()
        params["characterId"] = characterId
        val charactersAddonModel = CharactersAddon()
        val charactersModifierModel = CharactersModifier()
        charactersAddonModel.destroy_all(charactersAddonModel.where(params))
        params["featureId"] = itemId
        charactersModifierModel.destroy_all(charactersModifierModel.where(params))
        super.destroy()
    }

    override fun destroy_all(models: ObservableList<Any>) {
        if (models.isEmpty()) return
        val params = HashMap<String, Any>()
        params.put("characterId", characterId)
        (models as ObservableList<CharactersFeature>).map { it.destroyChildren() }
        super.destroy_all(models)
    }
}
