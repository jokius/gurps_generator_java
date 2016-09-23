package ru.gurps.generator.desktop.models.rules

import ru.gurps.generator.desktop.config.Model
import tornadofx.*
import tornadofx.FX.Companion.messages
import java.sql.ResultSet

class Spell : Model {
    override var id: Int = -1
    var schoolId: Int = -1
    var name: String = ""
    var nameEn: String = ""
    private var _spellType: String = ""
    var spellType: String = ""
        get() = _spellType
                .replace(",", "; ")
                .replace(":", "${messages["or"]}  ")
                .replace("0", "${messages["usual"]}  ")
                .replace("1", "${messages["area"]}  ")
                .replace("2", "${messages["contact"]}  ")
                .replace("3", "${messages["throw"]}  ")
                .replace("4", "${messages["block_spell"]}  ")
                .replace("5", "${messages["resistance"]}  ")
                .replace("6", "${messages["information"]}  ")
                .replace("7", "${messages["charm"]}  ")
                .replace("8", "${messages["special"]}  ")
    var description: String = ""
    var _complexity: Int = -1
    val complexity: String by lazy {
        when (_complexity) {
            0 -> messages["easy"]
            1 -> messages["medium"]
            2 -> messages["hard"]
            3 -> messages["very_hard"]
            else -> ""
        }
    }
    var cost: String = ""
    var needTime: String = ""
    var duration: String = ""
    var maintainingCost: String = ""
    var thing: String = ""
    var createCost: String = ""
    var demands: String = ""
    var resistance: String = ""
    var modifiers: String = ""
    var level: Int = 1
    var finalCost: Int = 0
    var add: Boolean = false
    val school: School by lazy { School().find(schoolId) as School }
    val schoolName: String by lazy { school.name }
    val schoolSingle: String by lazy { "${messages["school"]}: ${school.name}" }
    val resistanceSingle: String by lazy {
        val res = if (resistance.isBlank()) messages["no"] else resistance
        "${messages["resistance"]}: $res"
    }
    val typeSingle: String by lazy { "${messages["type"]}: $spellType" }
    val complexitySingle: String by lazy { "${messages["complexity"]}: $complexity" }
    val demandsSingle: String by lazy {
        val dem = if (demands.isBlank()) messages["no"] else demands
        "${messages["demands"]}: $dem"
    }
    val modifiersSingle: String by lazy { if (modifiers.isBlank()) "" else "${messages["modifiers"]}: $modifiers" }
    val needTimeSingle: String by lazy { "${messages["need_time"]}: $needTime" }
    val costSingle: String by lazy { "${messages["cost"]}: $cost" }
    val maintainingCostSingle: String by lazy { "${messages["maintaining_cost"]}: $maintainingCost" }
    val durationSingle: String by lazy { "${messages["duration"]}: $duration" }
    val thingSingle: String by lazy {
        val thingS = if (thing.isBlank()) messages["no"] else thing
        "${messages["duration"]}: $thingS"
    }
    val createCostSingle: String by lazy {
        val create = if (createCost.isBlank()) messages["no"] else createCost
        "${messages["duration"]}: $create"
    }

    constructor()
    constructor(result: ResultSet) {
        id = result.getInt("id")
        schoolId = result.getInt("schoolId")
        val nameRes = result.getString("name")
        if (nameRes != null) name = nameRes
        val nameEnRes = result.getString("nameEn")
        if (nameEnRes != null) nameEn = nameEnRes
        val spellTypeRes = result.getString("spellType")
        if (spellTypeRes != null) _spellType = spellTypeRes
        val descriptionRes = result.getString("description")
        if (descriptionRes != null) description = descriptionRes
        _complexity = result.getInt("complexity")
        val costRes = result.getString("cost")
        if (costRes != null) cost = costRes
        val needTimeRes = result.getString("needTime")
        if (needTimeRes != null) needTime = needTimeRes
        val durationRes = result.getString("duration")
        if (durationRes != null) duration = durationRes
        val maintainingCostRes = result.getString("maintainingCost")
        if (maintainingCostRes != null) maintainingCost = maintainingCostRes
        val thingRes = result.getString("thing")
        if (thingRes != null) thing = thingRes
        val createCostRes = result.getString("createCost")
        if (createCostRes != null) createCost = createCostRes
        val demandsRes = result.getString("demands")
        if (demandsRes != null) demands = demandsRes
        val resistanceRes = result.getString("resistance")
        if (resistanceRes != null) resistance = resistanceRes
        val modifiersRes = result.getString("modifiers")
        if (modifiersRes != null) modifiers = modifiersRes
    }

    fun setComplexity(complexity: String) {
        when (complexity) {
            messages["easy"] -> this._complexity = 0
            messages["medium"] -> this._complexity = 1
            messages["hard"] -> this._complexity = 2
            messages["very_hard"] -> this._complexity = 3
        }
    }
}
