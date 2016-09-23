package ru.gurps.generator.desktop.models.rules

import javafx.collections.ObservableList
import ru.gurps.generator.desktop.interfaces.models.Weapon
import ru.gurps.generator.desktop.config.Model
import ru.gurps.generator.desktop.singletons.Money
import ru.gurps.generator.desktop.singletons.TechnicalLevels
import java.sql.ResultSet
import java.util.*

class Grenade : Model, Weapon {
    override var id: Int = -1
    override var skills: String = ""
    override var tl: Int = -1
    override var name: String = ""
    override var damage: String = ""
    override var weight: String = ""
    var fuse: String = ""
    override var cents: Long = 0
    var cost: String = ""
        get() = Money.toDollars(cents)
    override var legalityClass: Int = 0
    override var count: Int = 1
    override var add: Boolean = false
    override val notes: ObservableList<Note> by lazy {
        val params = HashMap<String, Any>()
        params["itemType"] = "Rules::Grenade"
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
        val damageRes = result.getString("damage")
        if (damageRes != null) damage = damageRes
        val weightRes = result.getString("weight")
        if (weightRes != null) weight = weightRes
        val fuseRes = result.getString("fuse")
        if (fuseRes != null) fuse = fuseRes
        cents = result.getLong("costCent")
        legalityClass = result.getInt("legalityClass")
    }


    fun getLegalityClass(): String = legalityClass.toString()

    override fun getTl(): String = TechnicalLevels.list[tl]!!

    override fun getFinalConstCents(): Long = cents * count

    override fun getFinalCost(): String = Money.toDollars(getFinalConstCents())
}
