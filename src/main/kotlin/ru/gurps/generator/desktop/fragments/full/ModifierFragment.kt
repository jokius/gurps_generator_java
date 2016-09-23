package ru.gurps.generator.desktop.fragments.full

import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.scene.text.Text
import ru.gurps.generator.desktop.views.tables.ModifiersView
import tornadofx.*

class ModifierFragment : Fragment() {
    override val root: AnchorPane by fxml("/templates/full/ModifierFragment.fxml")
    private val modifiersView: ModifiersView by inject()
    private val modifier = modifiersView.currentModifier

    private val name: Label by fxid()
    private val cost: Label by fxid()
    private val combat: Label by fxid()
    private val improving: Label by fxid()
    private val fullDescription: Text by fxid()

    init {
        name.text = "${modifier.name}(${modifier.nameEn})"
        cost.text = "${messages["cost"]}: ${modifier.cost}"
        improving.text = "${messages["improving"]}: ${modifier.getImproving()}"
        combat.text = "${messages["combat"]}: ${modifier.getCombat()}"
        fullDescription.text = modifier.description
    }
}
