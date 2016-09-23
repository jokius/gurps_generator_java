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
import ru.gurps.generator.desktop.views.tables.SpellsView
import tornadofx.*

class SpellFragment : Fragment() {
    override val root: AnchorPane by fxml("/templates/full/SpellFragment.fxml")
    private val spellsView: SpellsView by inject()
    private val spell = spellsView.currentSpell
    private val name: Label by fxid()
    private val hBox: HBox by fxid()
    private val hBoxButton: HBox by fxid()
    private val fullDescription: TextFlow by fxid()
    private val finalCostLabel = label {
        minWidth = NodeAttributes.minWidth
        textAlignment = TextAlignment.CENTER
    }

    val addButton: Button = Button(messages["add"])
    val removeButton: Button = Button(messages["remove"])

    init {
        name.text = "${spell.name} (${spell.nameEn})"
        fullDescription.children.addAll(resistance(), school(), type(), complexity(), demands(), modifiers(),
                needTime(), cost(), maintainingCost(), duration(), thing(), createCost(), description())
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
        if (spell.add) {
            hBox.children.add(lvlLabel(CharacterParams.spellLevelResult(spell._complexity).toString()))
            return
        }

        hBox.children.add(lvlText(CharacterParams.spellLevelResult(spell._complexity).toString()))
    }

    private fun addFinalCost() {
        finalCostLabel.text = if (spell.add) spell.finalCost.toString()
        else CharacterParams.spellCost(spell).toString()
        hBox.children.add(finalCostLabel)
    }

    private fun addButton() {
        if (spell.add) hBoxButton.children.add(removeButton)
        else hBoxButton.children.add(addButton)
    }

    private fun setButtons() {
        addButton.setOnAction {
            spellsView.callAdd(spellsView.currentCell)
            spell.add = true
            intHBox()
        }

        removeButton.setOnAction {
            spellsView.callRemove(spellsView.currentCell)
            spell.add = false
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
                spell.level = value.toInt()
                spell.finalCost = CharacterParams.spellCost(spell)
                newCost(spell.finalCost)
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

    private fun resistance(): Text {
        val resistance = Text(spell.resistanceSingle + "\r\n")
        resistance.id = "resistance"
        return resistance
    }

    private fun school(): Text {
        val school = Text(spell.schoolSingle + "\r\n")
        school.id = "school"
        return school
    }

    private fun type(): Text {
        val type = Text(spell.typeSingle + "\r\n")
        type.id = "type"
        return type
    }

    private fun complexity(): Text {
        val complexity = Text(spell.complexitySingle + "\r\n")
        complexity.id = "complexity"
        return complexity
    }

    private fun demands(): Text {
        val demands = Text(spell.demandsSingle + "\r\n")
        demands.id = "demands"
        return demands
    }

    private fun modifiers(): Text {
        val modifiers = Text(spell.modifiersSingle + "\r\n")
        modifiers.id = "modifiers"
        return modifiers
    }

    private fun needTime(): Text {
        val needTime = Text(spell.needTimeSingle + "\r\n")
        needTime.id = "needTime"
        return needTime
    }

    private fun cost(): Text {
        val cost = Text(spell.costSingle + "\r\n")
        cost.id = "costSpell"
        return cost
    }

    private fun maintainingCost(): Text {
        val maintainingCost = Text(spell.maintainingCostSingle + "\r\n")
        maintainingCost.id = "maintainingCost"
        return maintainingCost
    }

    private fun duration(): Text {
        val duration = Text(spell.durationSingle + "\r\n")
        duration.id = "duration"
        return duration
    }

    private fun thing(): Text {
        val thing = Text(spell.thingSingle + "\r\n")
        thing.id = "thing"
        return thing
    }

    private fun createCost(): Text {
        val createCost = Text(spell.createCostSingle + "\r\n\r\n")
        createCost.id = "createCost"
        return createCost
    }

    private fun description(): Text {
        val description = Text(spell.description + "\r\n")
        description.id = "description"
        return description
    }
}
