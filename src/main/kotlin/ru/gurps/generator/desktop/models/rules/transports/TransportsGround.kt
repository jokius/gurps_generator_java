package ru.gurps.generator.desktop.models.rules.transports

import javafx.collections.ObservableList
import ru.gurps.generator.desktop.interfaces.models.Transport
import ru.gurps.generator.desktop.config.Model
import ru.gurps.generator.desktop.models.Character
import ru.gurps.generator.desktop.models.characters.transports.CharactersTransportsGround
import ru.gurps.generator.desktop.models.rules.Note
import ru.gurps.generator.desktop.models.rules.NoteItem
import ru.gurps.generator.desktop.singletons.Money
import ru.gurps.generator.desktop.singletons.TechnicalLevels
import ru.gurps.generator.desktop.singletons.TransportLocations
import tornadofx.*
import tornadofx.FX.Companion.messages
import java.sql.ResultSet
import java.util.*

class TransportsGround : Model, Transport {
    override var id: Int = -1
    override var skills: String = ""
    override var tl: Int = -1
    override var name: String = ""
    override var stHp: Int = 0
    override var handling: Int = 0
    override var stabilityRating: String = ""
    override var ht: String = ""
    override var move: String = ""
    override var loadedWeight: Double = 0.0
    override var load: Double = 0.0
    override var sizeModifier: String = ""
    override var occupant: String = ""
    override var damageResist: String = ""
    override var range: String = ""
    override var cents: Long = 0
    var cost: String = ""
        get() = Money.toDollars(cents)
    override var locations: String = ""
    override val locationsFull: String by lazy { TransportLocations.replace(locations) }
    var unpowered: Boolean = false
    var road: Boolean = false
    var rails: Boolean = false
    override var count: Int = 1
    override var add: Boolean = false
    override val notes: ObservableList<Note> by lazy {
        val params = HashMap<String, Any>()
        params["itemType"] = "Rules::Transports::Ground"
        params["itemId"] = id
        val notesIds = "id in (${NoteItem().pluck("noteId", params).joinToString { it }})"
        Note().where(notesIds) as ObservableList<Note>
    }

    constructor()
    constructor(result: ResultSet) {
        id = result.getInt("id")
        val skillsRes = result.getString("skills")
        if (skillsRes != null) skills = skillsRes
        tl = result.getInt("tl")
        val nameRes = result.getString("name")
        if (nameRes != null) name = nameRes
        stHp = result.getInt("stHp")
        handling = result.getInt("handling")
        val stabilityRatingRes = result.getString("stabilityRating")
        if (stabilityRatingRes != null) stabilityRating = stabilityRatingRes
        val htRes = result.getString("ht")
        if (htRes != null) ht = htRes
        val moveRes = result.getString("move")
        if (moveRes != null) move = moveRes
        loadedWeight = result.getDouble("loadedWeight")
        load = result.getDouble("load")
        val sizeModifierRes = result.getString("sizeModifier")
        if (sizeModifierRes != null) sizeModifier = sizeModifierRes
        val occupantRes = result.getString("occupant")
        if (occupantRes != null) occupant = occupantRes
        val damageResistRes = result.getString("damageResist")
        if (damageResistRes != null) damageResist = damageResistRes
        val rangeRes = result.getString("range")
        if (rangeRes != null) range = rangeRes
        cents = result.getLong("costCent")
        val locationsRes = result.getString("locations")
        if (locationsRes != null) locations = locationsRes
        unpowered = result.getBoolean("unpowered")
        road = result.getBoolean("road")
        rails = result.getBoolean("rails")
    }

    override fun getTl(): String = TechnicalLevels.list[tl]!!

    override fun getFinalConstCents(): Long = cents * count

    override fun getFinalCost(): String = Money.toDollars(getFinalConstCents())

    fun getUnpowered(): String = if (unpowered) messages["yes"] else messages["no"]

    fun getRoad(): String = if (road) messages["yes"] else messages["no"]

    fun getRails(): String = if (rails) messages["yes"] else messages["no"]

    override fun addToCharacter(character: Character) {
        CharactersTransportsGround(character.id, id, count).create()
    }

    override fun removeToCharacter(character: Character) {
        val params = HashMap<String, Any>()
        params.put("characterId", character.id)
        params.put("itemId", id)
        CharactersTransportsGround().find_by(params).destroy()
    }
}
