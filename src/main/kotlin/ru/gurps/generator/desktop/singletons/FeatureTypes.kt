package ru.gurps.generator.desktop.singletons

import tornadofx.FX.Companion.messages
import tornadofx.get

object FeatureTypes {
    init { println("This Features Types is load") }
    val list: Array<String> = setList()
    private fun setList(): Array<String> {
        return arrayOf(messages["mental_multi"], messages["physical_multi"], messages["social_multi"],
                messages["exotic_multi"], messages["supernatural_multi"], messages["mundane_multi"])
    }
}
