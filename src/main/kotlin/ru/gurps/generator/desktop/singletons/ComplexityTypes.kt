package ru.gurps.generator.desktop.singletons

import tornadofx.FX.Companion.messages
import tornadofx.get

object ComplexityTypes {
    init { println("This Complexity Types is load") }
    val list: Array<String> = setList()
    private fun setList(): Array<String> {
        return arrayOf(messages["easy"], messages["medium"], messages["hard"], messages["very_hard"])
    }
}
