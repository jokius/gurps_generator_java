package ru.gurps.generator.desktop.fragments.full

import ru.gurps.generator.desktop.fragments.abstracts.ArmorFragment
import ru.gurps.generator.desktop.models.rules.ManArmor

class ManArmorFragment : ArmorFragment() {
    override val armor = armorsView.currentArmor as ManArmor

    init {
        setItemInfo()
        setChildren()
    }
}
