package ru.gurps.generator.desktop.models.rules

import javafx.collections.ObservableList
import ru.gurps.generator.desktop.interfaces.models.Weapon
import ru.gurps.generator.desktop.config.Model
import ru.gurps.generator.desktop.singletons.Money
import ru.gurps.generator.desktop.singletons.TechnicalLevels
import java.sql.ResultSet
import java.util.*

class Gun : Model, Weapon {
    override var id: Int = -1
    override var skills: String = ""
    override var tl: Int = -1
    override var name: String = ""
    override var damage: String = ""
    var accuracy: String = ""
    var range: String = ""
    override var weight: String = ""
    var rateOfFire: Int = 0
    var shots: String = ""
    var st: Int = 0
    var bulk: Int = 0
    var recoil: Int = 0
    override var cents: Long = 0
    var cost: String = ""
        get() = Money.toDollars(cents)
    override var legalityClass: Int = 0
    override var count: Int = 1
    override var add: Boolean = false
    override val notes: ObservableList<Note> by lazy {
        val params = HashMap<String, Any>()
        params["itemType"] = "Rules::Gun"
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
        val accuracyRes = result.getString("accuracy")
        if (accuracyRes != null) accuracy = accuracyRes
        val rangeRes = result.getString("range")
        if (rangeRes != null) range = rangeRes
        val weightRes = result.getString("weight")
        if (weightRes != null) weight = weightRes
        rateOfFire = result.getInt("rateOfFire")
        val shotsRes = result.getString("shots")
        if (shotsRes != null) shots = shotsRes
        st = result.getInt("st")
        bulk = result.getInt("bulk")
        recoil = result.getInt("recoil")
        cents = result.getLong("costCent")
        legalityClass = result.getInt("legalityClass")
    }

    fun getLegalityClass(): String = legalityClass.toString()

    override fun getTl(): String = TechnicalLevels.list[tl]!!

    override fun getFinalConstCents(): Long = cents * count

    override fun getFinalCost(): String = Money.toDollars(getFinalConstCents())
}
