package ru.gurps.generator.desktop.fragments.full

import javafx.scene.text.Text
import ru.gurps.generator.desktop.fragments.abstracts.ArmorFragment
import ru.gurps.generator.desktop.models.rules.NotManArmor
import tornadofx.*

class NotManArmorFragment : ArmorFragment() {
    override val armor = armorsView.currentArmor as NotManArmor

    init {
        setItemInfo()
        setChildren()
    }

    override fun setChildren() {
        fullDescription.children.add(race())
        super.setChildren()
    }

    private fun race(): Text {
        val race = Text("${messages["race"]}: ${armor.race}\r\n")
        race.id = "race"
        return race
    }
}
