package ru.gurps.generator.desktop.models.rules

import javafx.collections.ObservableList
import ru.gurps.generator.desktop.interfaces.models.Armor
import ru.gurps.generator.desktop.config.Model
import ru.gurps.generator.desktop.models.Character
import ru.gurps.generator.desktop.models.characters.CharactersArmorsAddon
import ru.gurps.generator.desktop.models.characters.CharactersManArmor
import ru.gurps.generator.desktop.singletons.Money
import ru.gurps.generator.desktop.singletons.TechnicalLevels
import java.sql.ResultSet
import java.util.*

class ManArmor : Model, Armor {
    override var id: Int = -1
    override var slot: String = ""
    override var tl: Int = -1
    override var name: String = ""
    override var protection: String = ""
    override var damageResist: String = ""
    override var cents: Long = 0
    override var cost: String = ""
        get() = Money.toDollars(cents)
    override var weight: String = ""
    override var legalityClass: Int = 0
    override var count: Int = 1
    override var add: Boolean = false
    override val addons: ObservableList<ArmorsAddon> by lazy {
        val params = HashMap<String, Any>()
        params["armorType"] = "Rules::ManArmor"
        params["armorId"] = id
        ArmorsAddon().where(params) as ObservableList<ArmorsAddon>
    }
    override val notes: ObservableList<Note> by lazy {
        val params = HashMap<String, Any>()
        params["itemType"] = "Rules::ManArmor"
        params["itemId"] = id
        val notesIds = "id in (${NoteItem().pluck("noteId", params).joinToString { it }})"
        Note().where(notesIds) as ObservableList<Note>
    }

    constructor()
    constructor(result: ResultSet) {
        id = result.getInt("id")
        val slotRes = result.getString("slot")
        if (slotRes != null) slot = slotRes
        tl = result.getInt("tl")
        val nameRes = result.getString("name")
        if (nameRes != null) name = nameRes
        val protectionRes = result.getString("protection")
        if (protectionRes != null) protection = protectionRes
        val damageResistRes = result.getString("damageResist")
        if (damageResistRes != null) damageResist = damageResistRes
        cents = result.getLong("costCent")
        val weightRes = result.getString("weight")
        if (weightRes != null) weight = weightRes
        legalityClass = result.getInt("legalityClass")
    }

    fun getNameAndAddons(): String = "$name${addons.joinToString { "\r\n+ ${it.name}" }}"

    fun getProtectionAndAddons(): String = "$protection${addons.joinToString { "\r\n ${it.protection}" }}"

    override fun getTl(): String = TechnicalLevels.list[tl]!!

    fun getDamageResistAndAddons(): String = "$damageResist${addons.joinToString { "\r\n${it.damageResist}" }}"

    fun getCostAndAddons(): String = "$cost${addons.joinToString { "\r\n${it.cost}" }}"

    override fun getFinalConstCents(): Long {
        var addonsCost: Long = 0
        addons.forEach { addonsCost += it.cents * it.count }
        return cents * count + addonsCost
    }

    override fun getFinalCost(): String = Money.toDollars(getFinalConstCents())

    override fun addToCharacter(character: Character) {
        CharactersManArmor(character.id, id, count).create()
        addons.filter { it.count > 0 }.forEach { CharactersArmorsAddon(character.id, it.id, it.count).create() }
    }

    override fun removeToCharacter(character: Character) {
        val params = HashMap<String, Any>()
        params.put("characterId", character.id)
        params.put("itemId", id)

        if (addons.any()) CharactersArmorsAddon().destroy_all(CharactersArmorsAddon().where(params))
        CharactersManArmor().find_by(params).destroy()
    }
}
