package ru.gurps.generator.desktop.models.rules

import ru.gurps.generator.desktop.config.Model
import tornadofx.FX.Companion.messages
import tornadofx.get
import java.sql.ResultSet

class Technique : Model {
    override var id: Int = -1
    var name: String = ""
    var nameEn: String = ""
    var _complexity: Int = -1
    val complexity: String by lazy {
        when (_complexity) {
            0 -> messages["easy_short"]
            1 -> messages["medium_short"]
            2 -> messages["hard_short"]
            3 -> messages["very_hard_short"]
            else -> ""
        }
    }
    val complexityFull: String by lazy {
        when (_complexity) {
            0 -> messages["easy"]
            1 -> messages["medium"]
            2 -> messages["hard"]
            3 -> messages["very_hard"]
            else -> ""
        }
    }
    var _defaultUse: String = ""
    val defaultUse: String by lazy { if (_defaultUse.isBlank()) messages["no"] else _defaultUse }
    var _demands: String = ""
    val demands: String by lazy { if (_demands == "") messages["no"] else _demands }
    var description: String = ""
    var cost: Int = 1
    var level: Int = 1
    var add: Boolean = false

    constructor()
    constructor(result: ResultSet) {
        id = result.getInt("id")
        val nameRes = result.getString("name")
        if (nameRes != null) name = nameRes
        val nameEnRes = result.getString("nameEn")
        if (nameEnRes != null) nameEn = nameEnRes
        _complexity = result.getInt("complexity")
        val defaultUseRes = result.getString("defaultUse")
        if (defaultUseRes != null) _defaultUse = defaultUseRes
        val demandsRes = result.getString("demands")
        if (demandsRes != null) _demands = demandsRes
        val descriptionRes = result.getString("description")
        if (descriptionRes != null) description = descriptionRes
    }

    fun setComplexity(complexity: String) {
        when (complexity) {
            messages["easy"] -> this._complexity = 0
            messages["medium"] -> this._complexity = 2
            messages["hard"] -> this._complexity = 3
            messages["very_hard"] -> this._complexity = 4
        }
    }
}
