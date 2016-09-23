package ru.gurps.generator.desktop.models.rules

import ru.gurps.generator.desktop.config.Model
import ru.gurps.generator.desktop.singletons.LanguagesValues
import tornadofx.FX
import tornadofx.get
import java.sql.ResultSet

class Language : Model {
    override var id: Int = -1
    var name: String = ""
    var spoken: Int = 0
    var written: Int = 0
    var cost: Int = 0
    var add: Boolean = false

    constructor()
    constructor(result: ResultSet) {
        id = result.getInt("id")
        val nameRes = result.getString("name")
        if (nameRes != null) name = nameRes
    }

    constructor(language: String) {
        this.name = language
    }

    fun getCost(): String {
        return cost.toString()
    }

    fun getSpoken(): String {
        return LanguagesValues.spoken[spoken]
    }

    fun setSpoken(spoken: String) {
        when(spoken) {
            FX.messages["not_have"] -> this.spoken = 0
            FX.messages["broken"] -> this.spoken = 1
            FX.messages["accent"] -> this.spoken = 2
            FX.messages["native"] -> this.spoken = 3
        }
    }

    fun getWritten(): String? {
        return LanguagesValues.spoken[written]
    }

    fun setWritten(written: String) {
        when (written) {
            FX.messages["illiteracy"] -> this.written = 0
            FX.messages["semi-literate"] -> this.written = 1
            FX.messages["literacy"] -> this.written = 2
        }
    }
}
