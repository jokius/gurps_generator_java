package ru.gurps.generator.desktop.fragments

import javafx.scene.layout.AnchorPane
import tornadofx.Fragment

class NotHaveUpdatesFragment : Fragment("") {
    override val root: AnchorPane by fxml("/templates/NotHaveUpdatesFragment.fxml")

    fun ok() {
        close()
    }
}
