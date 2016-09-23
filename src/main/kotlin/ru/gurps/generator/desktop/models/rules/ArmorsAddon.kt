package ru.gurps.generator.desktop.models.rules

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.gurps.generator.desktop.interfaces.models.Armor
import ru.gurps.generator.desktop.config.Model
import ru.gurps.generator.desktop.models.Character
import ru.gurps.generator.desktop.singletons.Money
import java.sql.ResultSet
import java.util.*

class ArmorsAddon : Model, Armor {
    override var id: Int = -1
    var armorType: String = ""
    var armorId: Int = -1
    override var name: String = ""
    override var protection: String = ""
    override var damageResist: String = ""
    override var cents: Long = 0
    override var cost: String = ""
        get() = Money.toDollars(cents)
    override var weight: String = ""
    override var count: Int = 0
    override val notes: ObservableList<Note> by lazy {
        val params = HashMap<String, Any>()
        params["itemType"] = "Rules::Armors::Addon"
        params["itemId"] = id
        val notesIds = "id in (${NoteItem().pluck("noteId", params).joinToString { it }})"
        Note().where(notesIds) as ObservableList<Note>
    }

    constructor()
    constructor(result: ResultSet) {
        id = result.getInt("id")
        val armorTypeRes = result.getString("armorType")
        if (armorTypeRes != null) armorType = armorTypeRes
        armorId = result.getInt("armorId")
        val nameRes = result.getString("name")
        if (nameRes != null) name = nameRes
        val protectionRes = result.getString("protection")
        if (protectionRes != null) protection = protectionRes
        val damageResistRes = result.getString("damageResist")
        if (damageResistRes != null) damageResist = damageResistRes
        cents = result.getLong("costCent")
        val weightRes = result.getString("weight")
        if (weightRes != null) weight = weightRes
    }

    // Don't use it
    override var slot: String = ""
    override var tl: Int = -3
    override var legalityClass: Int = -1
    override var add: Boolean = false
    override val addons: ObservableList<ArmorsAddon> = FXCollections.observableArrayList<ArmorsAddon>()
    override fun getFinalCost(): String = ""
    override fun getTl(): String = ""
    override fun addToCharacter(character: Character) {}
    override fun removeToCharacter(character: Character) {}
    override fun getFinalConstCents(): Long = 0
}
