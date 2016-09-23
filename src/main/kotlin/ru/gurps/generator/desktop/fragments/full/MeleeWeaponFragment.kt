package ru.gurps.generator.desktop.fragments.full

import javafx.scene.text.Text
import ru.gurps.generator.desktop.fragments.abstracts.MeleeWeaponFragmentAbstract
import ru.gurps.generator.desktop.models.rules.MeleeWeapon
import tornadofx.*

class MeleeWeaponFragment : MeleeWeaponFragmentAbstract() {
    override val item: MeleeWeapon = meleeWeaponsView.currentWeapon as MeleeWeapon

    init {
        fullDescription.children.addAll(skills(), tl(), damage(), reach(), parry(), weight(), st(), twoHands(),
                training())
        setItemInfo()
        setChildren()
    }

    private fun reach(): Text {
        val reach = Text("${messages["reach"]}: ${item.reach} \r\n")
        reach.id = "reach"
        return reach
    }

    private fun parry(): Text {
        val parry = Text("${messages["parry"]}: ${item.parry} \r\n")
        parry.id = "parry"
        return parry
    }


    private fun training(): Text {
        val training = Text("${messages["training_weapon"]}: ${item.getTraining()} \r\n\r\n")
        training.id = "training"
        return training
    }
}
