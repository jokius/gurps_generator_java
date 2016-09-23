package ru.gurps.generator.desktop.singletons

import tornadofx.FX.Companion.messages
import tornadofx.get

object CharacteristicTypes {
    init { println("This Characteristic Types is load") }
    val list: Array<String> = setList()
    private fun setList(): Array<String> {
        return arrayOf(messages["strength"], messages["dexterity"], messages["intellect"], messages["health"],
                messages["health_points"], messages["will"], messages["perception"], messages["fatigue_points"])
    }
}
