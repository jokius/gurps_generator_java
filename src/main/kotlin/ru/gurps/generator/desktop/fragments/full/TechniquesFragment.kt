package ru.gurps.generator.desktop.fragments.full

import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import javafx.scene.text.TextFlow
import ru.gurps.generator.desktop.singletons.CharacterParams
import ru.gurps.generator.desktop.singletons.NodeAttributes
import ru.gurps.generator.desktop.views.tables.TechniquesView
import tornadofx.*

class TechniquesFragment : Fragment() {
    override val root: AnchorPane by fxml("/templates/full/TechniquesFragment.fxml")
    private val techniquesView: TechniquesView by inject()
    private val technique = techniquesView.currentTechnique
    private val name: Label by fxid()
    private val hBox: HBox by fxid()
    private val hBoxButton: HBox by fxid()
    private val fullDescription: TextFlow by fxid()
    private val finalCostLabel = label {
        minWidth = NodeAttributes.minWidth
        textAlignment = TextAlignment.CENTER
    }

    private val addButton: Button = Button(messages["add"])
    private val removeButton: Button = Button(messages["remove"])

    init {
        name.text = "${technique.name} (${technique.nameEn})"
        fullDescription.children.addAll(complexity(), defaultUse(), demands(), description())
        intHBox()
        setButtons()
    }

    private fun intHBox() {
        hBox.children.clear()
        hBoxButton.children.clear()
        addLevel()
        addFinalCost()
        addButton()
    }

    private fun addLevel() {
        if (technique.add) {
            hBox.children.add(lvlLabel(technique.level.toString()))
            return
        }

        hBox.children.add(lvlText(technique.level.toString()))
    }

    private fun addFinalCost() {
        finalCostLabel.text = if (technique.add) technique.cost.toString()
        else CharacterParams.techniqueCost(technique).toString()
        hBox.children.add(finalCostLabel)
    }

    private fun addButton() {
        if (technique.add) hBoxButton.children.add(removeButton)
        else hBoxButton.children.add(addButton)
    }

    fun setButtons() {
        addButton.setOnAction {
            techniquesView.callAdd(techniquesView.currentCell)
            technique.add = true
            intHBox()
        }

        removeButton.setOnAction {
            techniquesView.callRemove(techniquesView.currentCell)
            technique.add = false
            newCost(0)
            intHBox()
        }
    }

    private fun lvlText(level: String): TextField {
        return textfield(level) {
            minWidth = NodeAttributes.minWidth
            alignment = Pos.CENTER

            textProperty().addListener { _, old, value ->
                if (!Regex("\\d+").matches(value) || old == value || value.isNullOrBlank()) return@addListener
                technique.level = value.toInt()
                technique.cost = CharacterParams.techniqueCost(technique)
                newCost(technique.cost)
            }
        }
    }

    private fun lvlLabel(level: String): Label {
        return label(level) {
            minWidth = NodeAttributes.minWidth
            textAlignment = TextAlignment.CENTER
        }
    }

    private fun newCost(changeCost: Int) {
        val result: String = changeCost.toString()
        finalCostLabel.text = result
    }

    private fun complexity(): Text {
        val complexity = Text(technique.complexityFull + "\r\n")
        complexity.addClass("complexity")
        return complexity
    }

    private fun defaultUse(): Text {
        val defaultUse = Text("${messages["default"]}: ${technique.defaultUse}" + "\r\n")
        defaultUse.addClass("defaultUse")
        return defaultUse
    }

    private fun demands(): Text {
        val demands = Text("${messages["demands"]}: ${technique.demands}" + "\r\n")
        demands.addClass("demands")
        return demands
    }

    private fun description(): Text {
        val description = Text(technique.description + "\r\n")
        description.id = "description"
        return description
    }
}
