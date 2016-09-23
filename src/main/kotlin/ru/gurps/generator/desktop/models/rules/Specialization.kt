package ru.gurps.generator.desktop.models.rules

import ru.gurps.generator.desktop.config.Model
import tornadofx.FX.Companion.messages
import tornadofx.get
import java.sql.ResultSet

class Specialization : Model {
    override var id: Int = -1
    var skillId: Int = -1
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
    private var _modifiers: String = ""
    var modifiers: String = ""
        get() = if (_modifiers.isNullOrBlank()) "" else "${messages["modifiers"]}: $_modifiers"
    var description: String = ""
    var parry: Boolean = false
    var parryBonus: Int = 0
    var cost: Int = 0
    var level: Int = 1
    var add: Boolean = false
    val skill: Skill by lazy {
        Skill().find(skillId) as Skill
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
        skillId = result.getInt("skillId")
        name = result.getString("name") ?: skill.name
        nameEn = result.getString("nameEn") ?: skill.nameEn
        _skillType = result.getInt("skillType")
        if(_skillType == 0) _skillType = skill._skillType
        _complexity = result.getInt("complexity")
        if(_complexity == 0) _complexity = skill._complexity
        defaultUse = result.getString("defaultUse") ?: skill.defaultUse
        _demands = result.getString("demands") ?: skill._demands
        description = result.getString("description") ?: skill.description
        _modifiers = result.getString("modifiers") ?: skill._modifiers
        parry = result.getBoolean("parry")
        parryBonus = result.getInt("parry")
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
}
