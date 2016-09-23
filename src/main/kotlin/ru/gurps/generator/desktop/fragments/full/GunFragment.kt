package ru.gurps.generator.desktop.fragments.full

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import ru.gurps.generator.desktop.views.tables.GunsView
import tornadofx.*

class GunFragment : Fragment() {
    override val root: AnchorPane by fxml("/templates/full/InventoryFragment.fxml")
    private val gunsView: GunsView by inject()
    private val item = gunsView.currentGun

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
        fullDescription.children.addAll(skills(), tl(), damage(), accuracy(), range(), weight(), rateOfFire(), shots(),
                st(), bulk(), recoil(), legalityClass())
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
            gunsView.callAdd(gunsView.currentCell)
            item.add = true
        }
        removeButton.setOnAction {
            removeButton.isVisible = false
            addButton.isVisible = true
            countTextField.isVisible = true
            countLabel.isVisible = false
            gunsView.callRemove(gunsView.currentCell)
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
            gunsView.currentCell.countTextField.text = value
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

    private fun weight(): Text {
        val weight = Text("${messages["weight"]}: ${item.weight} \r\n")
        weight.id = "weight"
        return weight
    }

    private fun rateOfFire(): Text {
        val rateOfFire = Text("${messages["rate_of_fire"]}: ${item.rateOfFire} \r\n")
        rateOfFire.id = "rate_of_fire"
        return rateOfFire
    }

    private fun shots(): Text {
        val shots = Text("${messages["shots"]}: ${item.shots} \r\n")
        shots.id = "shots"
        return shots
    }

    private fun st(): Text {
        val st = Text("${messages["strength"]}: ${item.st} \r\n")
        st.id = "st"
        return st
    }

    private fun bulk(): Text {
        val bulk = Text("${messages["bulk"]}: ${item.bulk} \r\n")
        bulk.id = "bulk"
        return bulk
    }

    private fun recoil(): Text {
        val recoil = Text("${messages["recoil"]}: ${item.recoil} \r\n")
        recoil.id = "recoil"
        return recoil
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
