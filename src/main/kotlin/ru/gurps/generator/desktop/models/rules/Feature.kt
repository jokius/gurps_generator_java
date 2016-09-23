package ru.gurps.generator.desktop.models.rules

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.gurps.generator.desktop.interfaces.models.Ability
import ru.gurps.generator.desktop.config.Model
import ru.gurps.generator.desktop.models.characters.CharactersModifier
import tornadofx.FX.Companion.messages
import tornadofx.get
import java.sql.ResultSet

import java.util.HashMap

class Feature : Model, Ability {
    override var id: Int = -1
    var advantage: Boolean = false
    override var name: String = ""
    override var nameEn: String = ""
    private var _featureType: String = ""
    var featureType: String = ""
        get() = _featureType
                .replace("[", "")
                .replace("]", "")
                .replace(",", "/ ")
                .replace("0", "${messages["mental_single"]} ")
                .replace("1", "${messages["physical_single"]} ")
                .replace("2", "${messages["social_single"]} ")
                .replace("3", "${messages["exotic_single"]} ")
                .replace("4", "${messages["supernatural_single"]} ")
                .replace("5", "${messages["mundane_single"]} ")
    override var cost: Int = 0
    override var currentCost: Int = 0
    override var description: String = ""
    var maxLevel: Int = 1
    var psi: Boolean = false
    var cybernetic: Boolean = false
    var level: Int = 1
    override var add: Boolean = false
    var modifier: Boolean = false
    val addons: ObservableList<Addon> by lazy {
        hasMany(Addon()) as ObservableList<Addon>
    }

    constructor()
    constructor(advantage: Boolean) : super() {
        this.advantage = advantage
    }

    constructor(result: ResultSet) {
        id = result.getInt("id")
        advantage = result.getBoolean("advantage")
        val nameRes = result.getString("name")
        if (nameRes != null) name = nameRes
        val nameEnRes = result.getString("nameEn")
        if (nameEnRes != null) nameEn = nameEnRes
        val featureTypeRes = result.getString("featureType")
        if (featureTypeRes != null) _featureType = featureTypeRes
        cost = result.getInt("cost")
        currentCost = result.getInt("cost")
        val descriptionRes = result.getString("description")
        if (descriptionRes != null) description = descriptionRes
        maxLevel = result.getInt("maxLevel")
        psi = result.getBoolean("psi")
        cybernetic = result.getBoolean("cybernetic")
    }

    fun getCost(): String {
        return cost.toString()
    }

    fun modifiers(characterId: Int): ObservableList<Modifier> {
        val params = HashMap<String, Any>()
        params.put("characterId", characterId)
        params.put("featureId", id)
        val charactersModifiers = CharactersModifier().where(params) as ObservableList<CharactersModifier>
        val modifiers = FXCollections.observableArrayList<Modifier>()
        charactersModifiers.forEach {
            val modifier = Modifier().find(it.modifierId) as Modifier
            modifier.level = it.level
            modifier.cost = it.cost
            modifiers.add(modifier)
        }
        return modifiers
    }

    fun modifierCost(modifier: Modifier): Int {
        return Math.ceil(cost * level * (modifier.currentCost * modifier.level / 100.0)).toInt()
    }

    fun getTypeShort(): String {
        return _featureType
                .replace("[", "")
                .replace("]", "")
                .replace(",", "/ ")
                .replace("0", "${messages["mental_short"]} ")
                .replace("1", "${messages["physical_short"]} ")
                .replace("2", "${messages["social_short"]} ")
                .replace("3", "${messages["exotic_short"]} ")
                .replace("4", "${messages["supernatural_short"]} ")
                .replace("5", "${messages["mundane_short"]} ")
    }

    override fun toString(): String {
        return "$name/$nameEn"
    }
}
