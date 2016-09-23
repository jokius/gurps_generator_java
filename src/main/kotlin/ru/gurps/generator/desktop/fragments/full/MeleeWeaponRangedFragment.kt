package ru.gurps.generator.desktop.fragments.full

import javafx.scene.text.Text
import ru.gurps.generator.desktop.fragments.abstracts.MeleeWeaponFragmentAbstract
import ru.gurps.generator.desktop.models.rules.MeleeWeaponRanged
import tornadofx.*

class MeleeWeaponRangedFragment : MeleeWeaponFragmentAbstract() {
    override val item: MeleeWeaponRanged = meleeWeaponsView.currentWeapon as MeleeWeaponRanged

    init {
        fullDescription.children.addAll(skills(), tl(), damage(), accuracy(), range(), weight(), rateOfFire(), shots(),
                st(), bulk(), twoHands())
        setItemInfo()
        setChildren()
    }
    

    private fun accuracy(): Text {
        val accuracy = Text("${messages["accuracy"]}: ${item.accuracy} \r\n")
        accuracy.id = "accuracy"
        return accuracy
    }

    private fun range(): Text {
        val range = Text("${messages["range"]}: ${item.range} \r\n")
        range.id = "range"
        return range
    }

    private fun rateOfFire(): Text {
        val rateOfFire = Text("${messages["rate_of_fire"]}: ${item.rateOfFire} \r\n")
        rateOfFire.id = "rateOfFire"
        return rateOfFire
    }

    private fun shots(): Text {
        val shots = Text("${messages["shots"]}: ${item.shots} \r\n")
        shots.id = "shots"
        return shots
    }


    private fun bulk(): Text {
        val bulk = Text("${messages["bulk"]}: ${item.bulk} \r\n")
        bulk.id = "bulk"
        return bulk
    }
}
