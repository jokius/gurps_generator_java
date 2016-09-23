package ru.gurps.generator.desktop.models.rules

import javafx.collections.ObservableList
import ru.gurps.generator.desktop.config.Model
import tornadofx.FX.Companion.messages
import tornadofx.get
import java.sql.ResultSet

class Skill : Model {
    override var id: Int = -1
    var name: String = ""
    var nameEn: String = ""
    var _skillType: Int = -1
    val skillType: String by lazy {
        when (_skillType) {
            0 -> messages["strength_short"]
            1 -> messages["dexterity_short"]
            2 -> messages["intellect_short"]
            3 -> messages["health_short"]
            4 -> messages["health_points_short"]
            5 -> messages["will_short"]
            6 -> messages["perception_short"]
            7 -> messages["fatigue_points_short"]
            else -> ""
        }
    }
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
    var defaultUse: String = ""
    var _demands: String = ""
    val demands: String by lazy {
        if (_demands.isNullOrEmpty()) messages["no"] else _demands
    }
    var _modifiers: String = ""
    val modifiers: String by lazy {
        if (_modifiers.isNullOrEmpty()) "" else "${messages["modifiers"]}: $_modifiers"
    }
    var description: String = ""
    var twoHands: Boolean = false
    var parry: Boolean = false
    var parryBonus: Int = 0
    var cost: Int = 0
    var level: Int = 1
    var add: Boolean = false
    val specializations: ObservableList<Specialization> by lazy {
        hasMany(Specialization()) as ObservableList<Specialization>
    }
    val typeFull: String by lazy {
        when (_skillType) {
            0 -> messages["strength"]
            1 -> messages["dexterity"]
            2 -> messages["intellect"]
            3 -> messages["health"]
            4 -> messages["health_points"]
            5 -> messages["will"]
            6 -> messages["perception"]
            7 -> messages["fatigue_points"]
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
    val typeAndComplexity: String by lazy {
        "$skillType | $complexity"
    }

    constructor()
    constructor(result: ResultSet) {
        id = result.getInt("id")
        val nameRes = result.getString("name")
        if (nameRes != null) name = nameRes
        val nameEnRes = result.getString("nameEn")
        if (nameEnRes != null) nameEn = nameEnRes
        _skillType = result.getInt("skillType")
        _complexity = result.getInt("complexity")
        val defaultUseRes = result.getString("defaultUse")
        if (defaultUseRes != null) defaultUse = defaultUseRes
        val demandsRes = result.getString("demands")
        if (demandsRes != null) _demands = demandsRes
        val descriptionRes = result.getString("description")
        if (descriptionRes != null) description = descriptionRes
        val modifiersRes = result.getString("modifiers")
        if (modifiersRes != null) _modifiers = modifiersRes
        twoHands = result.getBoolean("twoHands")
        parry = result.getBoolean("parry")
        parryBonus = result.getInt("parryBonus")
    }

    fun setSkillType(skillType: String) {
        when (skillType) {
            messages["strength_short"] -> this._skillType = 0
            messages["dexterity_short"] -> this._skillType = 1
            messages["intellect_short"] -> this._skillType = 2
            messages["health_short"] -> this._skillType = 3
            messages["health_points_short"] -> this._skillType = 4
            messages["will_short"] -> this._skillType = 5
            messages["perception_short"] -> this._skillType = 6
            messages["fatigue_points_short"] -> this._skillType = 7
        }
    }

    fun setComplexity(complexity: String) {
        when (complexity) {
            messages["easy_short"] -> this._complexity = 0
            messages["medium_short"] -> this._complexity = 1
            messages["hard_short"] -> this._complexity = 2
            messages["very_hard_short"] -> this._complexity = 3
        }
    }

    fun getTwoHands(): String {
        return if (twoHands) messages["yes"] else messages["no"]
    }

    fun getParry(): String {
        return if (parry) messages["yes"] else messages["no"]
    }
}
