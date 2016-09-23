package ru.gurps.generator.desktop.fragments.full

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import ru.gurps.generator.desktop.views.tables.GrenadesView
import tornadofx.*

class GrenadeFragment : Fragment() {
    override val root: AnchorPane by fxml("/templates/full/InventoryFragment.fxml")
    private val grenadesView: GrenadesView by inject()
    private val item = grenadesView.currentGrenade

    private val name: Label by fxid()
    private val fullDescription: TextFlow by fxid()
    private val finalCost: Label by fxid()
    private val countTextField: TextField by fxid()
    private val countLabel: Label by fxid()
    private val addButton: Button by fxid()
    private val removeButton: Button by fxid()

    init {
        setItemInfo()
        setChildren()
    }

    private fun setChildren() {
        fullDescription.children.addAll(skills(), tl(), damage(), weight(), fuse(), legalityClass())
        setFinalConst()
        notes()
        setButtons()
    }

    private fun setButtons() {
        if (item.add) removeButton.isVisible = true
        else addButton.isVisible = true
        addButton.setOnAction {
            addButton.isVisible = false
            removeButton.isVisible = true
            countLabel.isVisible = true
            countTextField.isVisible = false
            grenadesView.callAdd(grenadesView.currentCell)
            item.add = true
        }
        removeButton.setOnAction {
            removeButton.isVisible = false
            addButton.isVisible = true
            countTextField.isVisible = true
            countLabel.isVisible = false
            grenadesView.callRemove(grenadesView.currentCell)
            item.add = false
        }
    }

    private fun setItemInfo() {
        name.text = item.name
        countTextField.text = item.count.toString()
        countLabel.text = item.count.toString()
        setCount()
        if (item.add) countLabel.isVisible = true
        else countTextField.isVisible = true
    }

    private fun setCount() {
        countTextField.textProperty().addListener { _, old, value ->
            if (old == value || !Regex("-?\\d+").matches(value)) return@addListener
            item.count = value.toInt()
            countLabel.text = value
            grenadesView.currentCell.countTextField.text = value
            setFinalConst()
        }
    }

    private fun setFinalConst() {
        finalCost.text = "${messages["final_cost"]}: ${item.getFinalCost()}"
    }

    private fun skills(): Text {
        val skills = Text("${messages["skills"]}: ${item.skills}\r\n")
        skills.id = "skills"
        return skills
    }

    private fun tl(): Text {
        val tl = Text("${messages["tl_full"]}: ${item.getTl()}\r\n")
        tl.id = "tl"
        return tl
    }

    private fun damage(): Text {
        val damage = Text("${messages["damage"]}: ${item.damage} \r\n")
        damage.id = "damage"
        return damage
    }

    private fun fuse(): Text {
        val fuse = Text("${messages["fuse"]}: ${item.fuse} \r\n")
        fuse.id = "fuse"
        return fuse
    }

    private fun weight(): Text {
        val weight = Text("${messages["weight"]}: ${item.weight} \r\n")
        weight.id = "weight"
        return weight
    }

    private fun legalityClass(): Text {
        val legalityClass = Text("${messages["legality_class"]}: ${item.legalityClass} \r\n\r\n")
        legalityClass.id = "parry"
        return legalityClass
    }

    private fun notes() {
        item.notes.forEachIndexed { index, note ->
            val noteText = Text("[${index + 1}]: ${note.description} \r\n")
            noteText.addClass("notes")
            noteText.id = "note_$index"
            fullDescription.add(noteText)
        }
    }
}
