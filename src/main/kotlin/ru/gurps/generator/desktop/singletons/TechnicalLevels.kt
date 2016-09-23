package ru.gurps.generator.desktop.singletons

import tornadofx.FX.Companion.messages
import tornadofx.get
import java.util.*

object TechnicalLevels {
    init { println("This Technical Levels is load") }
    val list: HashMap<Int, String> = setList()
    private fun setList(): HashMap<Int, String> {
         val list: HashMap<Int, String> = HashMap()
         (0..12).forEach { list[it] = it.toString() }
         list[-1] = messages["outside_levels"]
         list[-2] = messages["various"]
         return list
    }
}
