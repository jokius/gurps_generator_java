package ru.gurps.generator.desktop.singletons

import tornadofx.FX.Companion.messages
import tornadofx.get

object SpellType {
    init { println("This Spell Types is load") }
    val list: Array<String> = setList()
    private fun setList(): Array<String> {
        return arrayOf(messages["physical"], messages["social"], messages["mental"], messages["exotic"],
                messages["supernatural"])
    }
}
