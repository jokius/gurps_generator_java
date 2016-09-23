package ru.gurps.generator.desktop.fragments.full

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.layout.AnchorPane
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import ru.gurps.generator.desktop.views.tables.AlchemiesView
import tornadofx.*

class AlchemyFragment : Fragment() {
    override val root: AnchorPane by fxml("/templates/full/AlchemyFragment.fxml")
    private val alchemyView: AlchemiesView by inject()
    private val alchemy = alchemyView.currentAlchemy

    private val name: Label by fxid()
    private val fullDescription: TextFlow by fxid()
    private val addButton: Button by fxid()
    private val removeButton: Button by fxid()

    init {
        setButtons()
        name.text = "${alchemy.name} (${alchemy.nameEn})"
        fullDescription.children.addAll(alternativeNames(), duration(), form(), recipe(), recipeCost(), cost(),
                description())
    }

    private fun alternativeNames(): Text {
        val alternativeNames = Text(alchemy.alternativeNamesSingle + "\r\n\r\n")
        alternativeNames.id = "alternativeNames"
        return alternativeNames
    }

    private fun duration(): Text {
        val duration = Text(alchemy.durationSingle + "\r\n")
        duration.id = "duration"
        return duration
    }

    private fun form(): Text {
        val form = Text(alchemy.formSingle + "\r\n")
        form.id = "form"
        return form
    }

    private fun recipe(): Text {
        val complexity = Text(alchemy.recipeSingle + "\r\n")
        complexity.id = "recipe"
        return complexity
    }

    private fun recipeCost(): Text {
        val complexity = Text(alchemy.recipeCostSingle + "\r\n")
        complexity.id = "recipeCost"
        return complexity
    }

    private fun cost(): Text {
        val cost = Text(alchemy.costSingle + "\r\n\r\n")
        cost.id = "cost"
        return cost
    }

    private fun description(): Text {
        val description = Text(alchemy.description + "\r\n")
        description.id = "description"
        return description
    }

    private fun setButtons() {
        setVisibleButtons()

        addButton.setOnAction {
            alchemyView.callAdd(alchemyView.currentCell)
            alchemy.add = true
            setVisibleButtons()
        }

        removeButton.setOnAction {
            alchemyView.callRemove(alchemyView.currentCell)
            alchemy.add = false
            setVisibleButtons()
        }
    }

    private fun setVisibleButtons() {
        if (alchemy.add) {
            addButton.isVisible = false
            removeButton.isVisible = true
        } else {
            addButton.isVisible = true
            removeButton.isVisible = false
        }
    }
}
