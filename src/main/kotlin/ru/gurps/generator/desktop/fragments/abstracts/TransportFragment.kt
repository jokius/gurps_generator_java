package ru.gurps.generator.desktop.fragments.abstracts

import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import ru.gurps.generator.desktop.interfaces.models.Transport
import ru.gurps.generator.desktop.views.tables.TransportsView
import tornadofx.*

abstract class TransportFragment : Fragment() {
    override val root: AnchorPane by fxml("/templates/full/InventoryFragment.fxml")
    abstract val transport: Transport
    val transportsView: TransportsView by inject()
    val item = transportsView.currentTransport

    private val name: Label by fxid()
    val fullDescription: TextFlow by fxid()
    private val finalCost: Label by fxid()
    private val countTextField: TextField by fxid()
    private val countLabel: Label by fxid()
    private val addButton: Button by fxid()
    private val removeButton: Button by fxid()

    protected fun setItemInfo(){
        name.text = item.name
        countTextField.text = item.count.toString()
        countLabel.text = item.count.toString()
        setCount()
        if (item.add) countLabel.isVisible = true
        else countTextField.isVisible = true
    }
    
    protected open fun setChildren(){
        fullDescription.children.addAll(tl(), skills(), stHp(), handling(), stabilityRating(), ht(), move(),
                loadedWeight(), load(), sizeModifier(), occupant(), damageResist(),
                locations())
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
            transportsView.callAdd(transportsView.currentCell)
            item.add = true
        }
        removeButton.setOnAction {
            removeButton.isVisible = false
            addButton.isVisible = true
            countTextField.isVisible = true
            countLabel.isVisible = false
            transportsView.callRemove(transportsView.currentCell)
            item.add = false
        }
    }

    private fun setCount() {
        countTextField.textProperty().addListener { _, old, value ->
            if (old == value || !Regex("-?\\d+").matches(value)) return@addListener
            item.count = value.toInt()
            countLabel.text = value
            transportsView.currentCell.countTextField.text = value
            setFinalConst()
        }
    }

    private fun setFinalConst() {
        finalCost.text = "${messages["final_cost"]}: ${transport.getFinalCost()}"
    }

    protected fun skills(): Text {
        val skills = Text("${messages["skills"]}: ${transport.skills}\r\n")
        skills.addClass("skills")
        return skills
    }

    private fun stHp(): Text {
        val stHp = Text("${messages["st_hp_full"]}: ${transport.stHp}\r\n")
        stHp.addClass("stHp")
        return stHp
    }

    protected fun tl(): Text {
        val tl = Text("${messages["tl_full"]}: ${transport.getTl()}\r\n")
        tl.addClass("tl")
        return tl
    }

    protected fun handling(): Text {
        val handling = Text("${messages["handling"]}: ${transport.handling} \r\n")
        handling.addClass("handling")
        return handling
    }

    private fun stabilityRating(): Text {
        val stabilityRating = Text("${messages["stability_rating"]}: ${transport.stabilityRating} \r\n")
        stabilityRating.addClass("stabilityRating")
        return stabilityRating
    }

    protected fun ht(): Text {
        val ht = Text("${messages["health"]}: ${transport.ht} \r\n")
        ht.addClass("ht")
        return ht
    }

    protected fun move(): Text {
        val move = Text("${messages["move_full"]}: ${transport.move} \r\n")
        move.addClass("move")
        return move
    }

    private fun loadedWeight(): Text {
        val loadedWeight = Text("${messages["loaded_weight_full"]}: ${transport.loadedWeight} \r\n")
        loadedWeight.addClass("loadedWeight")
        return loadedWeight
    }

    protected fun load(): Text {
        val load = Text("${messages["load"]}: ${transport.load} \r\n")
        load.addClass("load")
        return load
    }

    private fun sizeModifier(): Text {
        val sizeModifier = Text("${messages["size_modifier"]}: ${transport.sizeModifier} \r\n")
        sizeModifier.addClass("sizeModifier")
        return sizeModifier
    }

    protected fun occupant(): Text {
        val occupant = Text("${messages["occupant"]}: ${transport.occupant} \r\n")
        occupant.addClass("occupant")
        return occupant
    }

    protected fun damageResist(): Text {
        val damageResist = Text("${messages["damage_resistance"]}: ${transport.occupant} \r\n")
        damageResist.addClass("damageResist")
        return damageResist
    }

    protected fun range(): Text {
        val range = Text("${messages["range"]}: ${transport.occupant} \r\n")
        range.addClass("range")
        return range
    }

    protected fun locations(): Text {
        val locations = Text("${messages["locations"]}: ${transport.locationsFull} \r\n\r\n")
        locations.addClass("locations")
        return locations
    }

    protected fun notes() {
        transport.notes.forEachIndexed { index, note ->
            val noteText = Text("[${index + 1}]: ${note.description} \r\n")
            noteText.addClass("notes")
            fullDescription.add(noteText)
        }
    }
}
