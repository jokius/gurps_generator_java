package ru.gurps.generator.desktop.fragments.abstracts

import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ScrollPane
import javafx.scene.control.TextField
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import javafx.scene.text.TextFlow
import ru.gurps.generator.desktop.interfaces.models.Armor
import ru.gurps.generator.desktop.models.rules.ArmorsAddon
import ru.gurps.generator.desktop.models.rules.ManArmor
import ru.gurps.generator.desktop.models.rules.NotManArmor
import ru.gurps.generator.desktop.views.tables.ArmorsView
import tornadofx.*

abstract class ArmorFragment : Fragment() {
    override val root: AnchorPane by fxml("/templates/full/ArmorFragment.fxml")
    val armorsView: ArmorsView by inject()
    val cell = armorsView.currentCell
    abstract val armor: Armor

    private val name: Label by fxid()
    private val addonsVBox: VBox by fxid()
    private val scroll: ScrollPane by fxid()
    val fullDescription: TextFlow by fxid()
    private val finalCost: Label by fxid()
    private val countTextField: TextField by fxid()
    private val countLabel: Label by fxid()
    private val addButton: Button by fxid()
    private val removeButton: Button by fxid()
    private val addonsLabel: Label by lazy {
        val addonsLabel = label(messages["addons"].capitalize()){
            minHeight = name.prefHeight + 20.0
            minWidth = name.prefWidth
            alignment = name.alignment
            addClass("addonsTitle")
        }
        addonsLabel
    }

    private val addonsText: Text by lazy {
        text("\r\n${addonsLabel.text}\r\n\r\n"){
            addClass("addonsTitle")
        }
    }

    private val heightAddonsVBox: Double by lazy {
        addonsVBox.isVisible = true
        addonsVBox.add(addonsLabel)
        var height = addonsLabel.minHeight + addonsVBox.spacing
        armor.addons.forEach { height += addAddon(addon = it, height = height) }
        height
    }

    protected fun setItemInfo() {
        name.text = armor.name
        countTextField.text = armor.count.toString()
        countLabel.text = armor.count.toString()
        setCount(item = armor, textField = countTextField, defaultValue = armor.count)
        if (armor.add) countLabel.isVisible = true
        else countTextField.isVisible = true
    }

    protected open fun setChildren() {
        fullDescription.children.addAll(slots(armor), tl(armor), protection(armor), damageResist(armor),
                weight(armor), legalityClass(armor))
        setFinalConst()
        notes(armor)
        addAddons()
        setButtons()
    }

    protected fun setButtons() {
        if (armor.add) removeButton.isVisible = true
        else addButton.isVisible = true
        addButton.setOnAction {
            addButton.isVisible = false
            removeButton.isVisible = true
            countLabel.isVisible = true
            countTextField.isVisible = false
            armorsView.callAdd(armorsView.currentCell)
            armor.add = true
            reloadAddons()
        }
        removeButton.setOnAction {
            removeButton.isVisible = false
            addButton.isVisible = true
            countTextField.isVisible = true
            countLabel.isVisible = false
            armorsView.callRemove(armorsView.currentCell)
            armor.add = false
            reloadAddons()
        }
    }

    private fun setCount(item: Armor, textField: TextField, defaultValue: Int) {
        textField.text = defaultValue.toString()
        textField.textProperty().addListener { _, old, value ->
            if (old == value || !Regex("-?\\d+").matches(value)) return@addListener
            if (textField != countTextField && armor.count == 0) countTextField.text = "1"
            when (item::class) {
                ManArmor::class, NotManArmor::class -> {
                    item.count = value.toInt()
                    countLabel.text = value
                    cell.countTextField.text = value
                }
                ArmorsAddon::class -> {
                    item.count = value.toInt()
                    cell.finalCostLabel.text = cell.rowItem.getFinalCost()
                }
            }
            setFinalConst()
        }
    }

    private fun addAddons() {
        if (armor.addons.isEmpty()) return
        addAddonsDescription()
        addonsVBox.isVisible = true
        addonsVBox.prefHeight = heightAddonsVBox
        fullDescription.prefHeight = fullDescription.prefHeight - addonsVBox.prefHeight
        AnchorPane.setTopAnchor(scroll, AnchorPane.getTopAnchor(scroll) + addonsVBox.prefHeight)
    }

    private fun reloadAddons() {
        addonsVBox.clear()
        addonsVBox.add(addonsLabel)
        armor.addons.forEach { addAddon(addon = it) }
    }

    private fun addAddonsDescription() {
        fullDescription.children.add(addonsText)
        armor.addons.forEach {
            fullDescription.children.addAll(name(it), protection(it), damageResist(it), weight(it))
            notes(it)
        }
    }

    private fun addAddon(addon: ArmorsAddon, height: Double = 0.0): Double {
        val addonHBox = addonBox()
        addonHBox.add(addonName(addon))
        if (armor.add) addonHBox.add(addonCostLabel(addon))
        else addonHBox.add(addonCostTextField(addon))
        addonsVBox.add(addonHBox)
        return height + addonHBox.prefHeight + addonsVBox.spacing
    }

    private fun addonBox(): HBox {
        return hbox(10.0) {
            prefHeight = countTextField.prefHeight
            alignment = Pos.CENTER_LEFT
        }
    }

    private fun addonName(addon: ArmorsAddon): Label {
        return label(addon.name) {
            maxWidth = name.prefWidth
            alignment = name.alignment
            addClass("itemName")
        }
    }

    private fun addonCostTextField(addon: ArmorsAddon): TextField {
        val addonCost = textfield(addon.count.toString()) {
            maxWidth = countTextField.prefWidth
            minWidth = countTextField.prefWidth
            alignment = countTextField.alignment
        }

        setCount(item = addon, textField = addonCost, defaultValue = addon.count)
        return addonCost
    }

    private fun addonCostLabel(addon: ArmorsAddon): Label {
        return label(addon.count.toString()) {
            maxWidth = countTextField.prefWidth
            minWidth = countTextField.prefWidth
            alignment = countTextField.alignment
        }
    }

    private fun setFinalConst() {
        finalCost.text = "${messages["final_cost"]}: ${armor.getFinalCost()}"
    }

    private fun name(item: Armor): Text {
        val name = Text("${messages["name"]}: ${item.name}\r\n")
        name.addClass("name")
        return name
    }

    private fun slots(item: Armor): Text {
        val slots = Text("${messages["slots"]}: ${item.slot}\r\n")
        slots.addClass("slots")
        return slots
    }

    private fun tl(item: Armor): Text {
        val tl = Text("${messages["tl_full"]}: ${item.getTl()}\r\n")
        tl.addClass("tl")
        return tl
    }

    private fun damageResist(item: Armor): Text {
        val damageResist = Text("${messages["damage_resistance"]}: ${item.damageResist} \r\n")
        damageResist.addClass("damageResist")
        return damageResist
    }

    private fun protection(item: Armor): Text {
        val protection = Text("${messages["protection"]}: ${item.protection} \r\n")
        protection.addClass("protection")
        return protection
    }


    private fun weight(item: Armor): Text {
        val weight = Text("${messages["weight"]}: ${item.weight} \r\n")
        weight.addClass("weight")
        return weight
    }


    private fun legalityClass(item: Armor): Text {
        val legalityClass = Text("${messages["legality_class"]}: ${item.legalityClass} \r\n\r\n")
        legalityClass.addClass("parry")
        return legalityClass
    }

    private fun notes(item: Armor) {
        item.notes.forEachIndexed { index, note ->
            val noteText = Text("[${index + 1}]: ${note.description} \r\n")
            noteText.addClass("notes")
            fullDescription.add(noteText)
        }
    }
}
