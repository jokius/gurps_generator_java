package ru.gurps.generator.desktop.models.rules

import javafx.collections.ObservableList
import javafx.scene.control.TableRow
import ru.gurps.generator.desktop.interfaces.models.Inventory
import ru.gurps.generator.desktop.config.Model
import ru.gurps.generator.desktop.singletons.Money
import ru.gurps.generator.desktop.singletons.TechnicalLevels
import java.sql.ResultSet
import java.util.*

class Shield : Model, Inventory {
    override var id: Int = -1
    var skills: String = ""
    override var tl: Int = -1
    var name: String = ""
    var defenseBonus: Int = 0
    var cents: Long = 0
    var cost: String = ""
        get() = Money.toDollars(cents)
    var weight: String = ""
    var drHp: String = ""
    var legalityClass: Int = 0
    override var count: Int = 1
    override var add: Boolean = false
    var row: TableRow<Shield>? = null
    override val notes: ObservableList<Note> by lazy {
        val params = HashMap<String, Any>()
        params["itemType"] = "Rules::Shield"
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
        defenseBonus = result.getInt("defenseBonus")
        cents = result.getLong("costCent")
        val weightRes = result.getString("weight")
        if (weightRes != null) weight = weightRes
        val drHpRes = result.getString("drHp")
        if (drHpRes != null) drHp = drHpRes
        legalityClass = result.getInt("legalityClass")
    }

    override fun getTl(): String = TechnicalLevels.list[tl]!!

    override fun getFinalConstCents(): Long = cents * count

    override fun getFinalCost(): String = Money.toDollars(getFinalConstCents())
}
