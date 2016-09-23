package ru.gurps.generator.desktop.models.rules

import javafx.collections.ObservableList
import javafx.scene.control.TableRow
import ru.gurps.generator.desktop.interfaces.models.Inventory
import ru.gurps.generator.desktop.config.Model
import ru.gurps.generator.desktop.singletons.Money
import ru.gurps.generator.desktop.singletons.TechnicalLevels
import java.sql.ResultSet
import java.util.*

class Equipment : Model, Inventory {
    override var id: Int = -1
    var applicationAreaId: Int = -1
    var name: String = ""
    override var tl: Int = -1
    var description: String = ""
    var cents: Long = 0
    var cost: String = ""
        get() = Money.toDollars(cents)
    var weight: String = ""
    var time: String = ""
    var legalityClass: Int = 0
    override var count: Int = 1
    override var add: Boolean = false
    var row: TableRow<Equipment>? = null
    private val applicationArea: EquipmentsApplicationArea by lazy {
        EquipmentsApplicationArea().find(applicationAreaId) as EquipmentsApplicationArea
    }
    val applicationAreaName: String by lazy {
        applicationArea.name
    }
    override val notes: ObservableList<Note> by lazy {
        val params = HashMap<String, Any>()
        params["itemType"] = "Rules::Equipment"
        params["itemId"] = id
        val notesIds = "id in (${NoteItem().pluck("noteId", params).joinToString { it }})"
        Note().where(notesIds) as ObservableList<Note>
    }


    constructor()
    constructor(result: ResultSet) {
        id = result.getInt("id")
        applicationAreaId = result.getInt("applicationAreaId")
        val nameRes = result.getString("name")
        if (nameRes != null) name = nameRes
        tl = result.getInt("tl")
        val descriptionRes = result.getString("description")
        if (descriptionRes != null) description = descriptionRes
        cents = result.getLong("costCent")
        val weightRes = result.getString("weight")
        if (weightRes != null) weight = weightRes
        val timeRes = result.getString("time")
        if (timeRes != null) time = timeRes
        legalityClass = result.getInt("legalityClass")
    }


    override fun getTl(): String = TechnicalLevels.list[tl]!!

    override fun getFinalConstCents(): Long = cents * count

    override fun getFinalCost(): String = Money.toDollars(getFinalConstCents())
}
