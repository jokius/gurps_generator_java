package ru.gurps.generator.desktop.fragments.abstracts

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import ru.gurps.generator.desktop.interfaces.models.MeleeWeaponInterface
import ru.gurps.generator.desktop.views.tables.MeleeWeaponsView
import tornadofx.*

abstract class MeleeWeaponFragmentAbstract : Fragment() {
    override val root: AnchorPane by fxml("/templates/full/InventoryFragment.fxml")
    val meleeWeaponsView: MeleeWeaponsView by inject()
    val cell = meleeWeaponsView.currentCell
    abstract val item: MeleeWeaponInterface

    private val name: Label by fxid()
    val fullDescription: TextFlow by fxid()
    private val finalCost: Label by fxid()
    private val countTextField: TextField by fxid()
    private val countLabel: Label by fxid()
    private val addButton: Button by fxid()
    private val removeButton: Button by fxid()

    protected fun setItemInfo() {
        name.text = item.name
        countTextField.text = item.count.toString()
        countLabel.text = item.count.toString()
        setCount()
        if (item.add) countLabel.isVisible = true
        else countTextField.isVisible = true
    }

    protected fun setChildren() {
        notes()
        setButtons()
    }

    protected fun setButtons() {
        if (item.add) removeButton.isVisible = true
        else addButton.isVisible = true
        addButton.setOnAction {
            addButton.isVisible = false
            removeButton.isVisible = true
            countLabel.isVisible = true
            countTextField.isVisible = false
            meleeWeaponsView.callAdd(meleeWeaponsView.currentCell)
            item.add = true
        }
        removeButton.setOnAction {
            removeButton.isVisible = false
            addButton.isVisible = true
            countTextField.isVisible = true
            countLabel.isVisible = false
            meleeWeaponsView.callRemove(meleeWeaponsView.currentCell)
            item.add = false
        }
    }

    private fun setCount() {
        countTextField.text = item.count.toString()
        countTextField.textProperty().addListener { _, old, value ->
            if (old == value || !Regex("-?\\d+").matches(value)) return@addListener
            item.count = value.toInt()
            countLabel.text = value
            cell.countTextField.text = value
            setFinalConst()
        }
    }

    private fun setFinalConst() {
        finalCost.text = "${messages["final_cost"]}: ${item.getFinalCost()}"
    }

    protected fun skills(): Text {
        val skills = Text("${messages["skills"]}: ${item.skills}\r\n")
        skills.id = "skills"
        return skills
    }

    protected fun tl(): Text {
        val tl = Text("${messages["tl_full"]}: ${item.getTl()}\r\n")
        tl.id = "tl"
        return tl
    }

    protected fun damage(): Text {
        val damage = Text("${messages["damage"]}: ${item.damage} \r\n")
        damage.id = "damage"
        return damage
    }
    

    protected fun weight(): Text {
        val weight = Text("${messages["weight"]}: ${item.weight} \r\n")
        weight.id = "weight"
        return weight
    }

    protected fun st(): Text {
        val st = Text("${messages["strength"]}: ${item.st} \r\n")
        st.id = "st"
        return st
    }

    protected fun twoHands(): Text {
        val twoHands = Text("${messages["two_hands"]}: ${item.getTwoHands()} \r\n")
        twoHands.id = "twoHands"
        return twoHands
    }

    protected fun notes() {
        item.notes.forEachIndexed { index, note ->
            val noteText = Text("[${index + 1}]: ${note.description} \r\n")
            noteText.addClass("notes")
            noteText.id = "note_$index"
            fullDescription.add(noteText)
        }
    }
}
