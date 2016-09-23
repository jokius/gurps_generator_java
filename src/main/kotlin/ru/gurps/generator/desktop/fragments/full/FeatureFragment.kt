package ru.gurps.generator.desktop.fragments.full

import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.scene.text.Text
import javafx.scene.text.TextAlignment
import javafx.scene.text.TextFlow
import ru.gurps.generator.desktop.models.characters.CharactersAddon
import ru.gurps.generator.desktop.models.rules.Addon
import ru.gurps.generator.desktop.singletons.Characters
import ru.gurps.generator.desktop.singletons.NodeAttributes
import ru.gurps.generator.desktop.views.TabsView
import ru.gurps.generator.desktop.views.tables.FeaturesView
import tornadofx.*
import java.util.*

class FeatureFragment : Fragment() {
    override val root: AnchorPane by fxml("/templates/full/FeatureFragment.fxml")
    private val featuresView: FeaturesView by inject()
    private val character = Characters.current!!
    private var feature = featuresView.currentFeature
    private val name: Label by fxid()
    private val hBox: HBox by fxid()
    private val hBoxButton: HBox by fxid()
    private val scroll: ScrollPane by fxid()
    private val fullDescription: TextFlow by fxid()
    private val finalCostLabel = label {
        isVisible = false
        minWidth = NodeAttributes.minWidth
        textAlignment = TextAlignment.CENTER
    }

    private val finalCostText = textfield(feature.currentCost.toString()) {
        isVisible = false
        minWidth = NodeAttributes.minWidth
        alignment = Pos.CENTER
        textProperty().addListener { _, old, value ->
            if (!isVisible || !Regex("\\d+").matches(value) || old == value || value.isNullOrBlank()) return@addListener
            val iValue = value.toInt()
            removeButton.fire()
            feature.cost = iValue
            feature.currentCost = iValue
            newCost(value.toInt())
        }
    }

    private val addButton: Button = Button(messages["add"])
    val removeButton: Button = Button(messages["remove"])

    private val addonsTableView: TableView<Addon> by fxid()
    private val activate: TableColumn<Addon, Boolean> by fxid()
    private val addonName: TableColumn<Addon, String> by fxid()
    private val addonNameEn: TableColumn<Addon, String> by fxid()
    private val addonLevel: TableColumn<Addon, Boolean> by fxid()
    private val addonCost: TableColumn<Addon, Boolean> by fxid()

    init {
        name.text = "${feature.name} (${feature.nameEn})"
        fullDescription.children.addAll(type(), description())
        intHBox()
        setButtons()
        addAddons()
    }

    private fun intHBox() {
        val params = HashMap<String, Any>()
        params.put("characterId", character.id)
        params.put("featureId", feature.id)
        hBox.children.clear()
        hBoxButton.children.clear()
        addLevel()
        addFinalCost()
        addButton()
    }

    private fun addLevel() {
        if (feature.add) {
            hBox.children.add(lvlLabel(feature.level.toString()))
            return
        }
        when {
            feature.maxLevel == 0 -> hBox.children.add(lvlText(feature.level.toString()))
            feature.maxLevel > 1 -> hBox.children.add(lvlComboBox(feature.level))
            else -> hBox.children.add(lvlLabel(feature.level.toString()))
        }
    }

    private fun addFinalCost() {
        if (feature.add || feature.cost != 0) {
            finalCostLabel.isVisible = true
            finalCostLabel.text = feature.currentCost.toString()
            hBox.children.add(finalCostLabel)
        } else {
            finalCostText.isVisible = true
            hBox.children.add(finalCostText)
        }
    }

    private fun addButton() {
        if (feature.add) hBoxButton.children.add(removeButton)
        else hBoxButton.children.add(addButton)
    }

    private fun setButtons() {
        addButton.setOnAction {
            featuresView.callAdd(featuresView.currentCell)
            feature.add = true
            addonsTableView.refresh()
            intHBox()
        }

        removeButton.setOnAction {
            featuresView.callRemove(featuresView.currentCell)
            feature.add = false
            addonsTableView.refresh()
            finalCost()
            intHBox()
        }
    }

    private fun lvlText(level: String): TextField {
        return textfield(level) {
            minWidth = NodeAttributes.minWidth
            alignment = Pos.CENTER

            textProperty().addListener { _, old, value ->
                if (!Regex("\\d+").matches(value) || old == value || value.isNullOrBlank()) return@addListener
                feature.level = value.toInt()
                feature.currentCost = feature.cost * feature.level
                finalCost()
            }
        }
    }

    private fun lvlComboBox(level: Int): ComboBox<Int> {
        return combobox {
            minWidth = NodeAttributes.minWidth
            value = level
            (1..feature.maxLevel).forEach { items.add(it) }
            valueProperty().addListener { _, _, value ->
                feature.level = value
                feature.currentCost = feature.cost * feature.level
                finalCost()
            }
        }
    }

    private fun lvlLabel(level: String): Label {
        return label(level) {
            minWidth = NodeAttributes.minWidth
            textAlignment = TextAlignment.CENTER
        }
    }

    private fun finalCost() {
        val finalCost = feature.currentCost * feature.level
        newCost(finalCost)
    }

    private fun newCost(changeCost: Int) {
        val result: String = changeCost.toString()
        finalCostLabel.text = result
        finalCostText.text = result
    }

    private fun addAddons() {
        if (feature.addons.isEmpty()) return
        setAddons()
        setItems()
        showTable()
    }

    private fun showTable() {
        addonsTableView.isVisible = true
        fullDescription.prefHeight = fullDescription.prefHeight - addonsTableView.prefHeight
        AnchorPane.setTopAnchor(scroll, AnchorPane.getTopAnchor(scroll) + addonsTableView.prefHeight)
    }

    private fun setItems() {
        val params = HashMap<String, Any>()
        params["characterId"] = character.id
        params["featureId"] = feature.id
        (CharactersAddon().where(params) as ObservableList<CharactersAddon>).forEach {
            val addon = feature.addons.find { addon -> addon.id == it.itemId }!!
            addon.add = true
            addon.cost = it.cost
            addon.level = it.level
        }

        addonsTableView.items = feature.addons
    }

    private fun setAddons() {
        addonName.cellValueFactory = PropertyValueFactory("name")
        addonNameEn.cellValueFactory = PropertyValueFactory("nameEn")
        addonLevel.setCellValueFactory { SimpleBooleanProperty(true) }
        addonCost.setCellValueFactory { SimpleBooleanProperty(true) }
        activate.setCellValueFactory { SimpleBooleanProperty(true) }
        activate.setCellFactory { ButtonCell() }
        addonLevel.setCellFactory { LevelCell() }
        addonCost.setCellFactory { CostCell() }
    }

    internal inner class LevelCell : TableCell<Addon, Boolean>() {
        private val textField = textfield {
            minWidth = NodeAttributes.minWidth
            alignment = Pos.CENTER
        }

        private val comboBox = combobox<Int> {
            minWidth = NodeAttributes.minWidth
        }

        private val label = label {
            minWidth = NodeAttributes.minWidth
            textAlignment = TextAlignment.CENTER
        }

        override fun updateItem(item: Boolean?, empty: Boolean) {
            super.updateItem(item, empty)
            if (empty) {
                graphic = null
                return
            }

            if (rowItem.add || feature.add) {
                label.text = rowItem.level.toString()
                graphic = label
                return
            }

            when {
                rowItem.maxLevel == 0 -> {
                    textField.text = rowItem.level.toString()
                    textField.textProperty().addListener { _, _, value ->
                        if (!Regex("\\d+").matches(value) || rowItem.level.toString() == value ||
                                value.isNullOrBlank()) return@addListener
                        rowItem.level = value.toInt()
                        rowItem.currentCost = rowItem.cost * rowItem.level
                        @Suppress("UNCHECKED_CAST")
                        val col: TableColumn<Addon, Boolean> = tableView.columns.find { it == addonCost } as
                                TableColumn<Addon, Boolean>
                        col.isVisible = false
                        col.isVisible = true
                        textField.positionCaret(value.length)
                    }

                    graphic = textField
                }
                rowItem.maxLevel > 1 -> {
                    comboBox.value = rowItem.level
                    (1..rowItem.maxLevel).forEach { comboBox.items.add(it) }
                    comboBox.valueProperty().addListener { _, _, value ->
                        rowItem.level = value
                        rowItem.currentCost = rowItem.cost * rowItem.level
                        @Suppress("UNCHECKED_CAST")
                        val col: TableColumn<Addon, Boolean> = tableView.columns.find { it == addonCost } as
                                TableColumn<Addon, Boolean>
                        col.isVisible = false
                        col.isVisible = true
                    }
                    graphic = comboBox
                }
                else -> {
                    label.text = rowItem.level.toString()
                    graphic = label
                }
            }
        }
    }

    internal inner class CostCell : TableCell<Addon, Boolean>() {
        private val textField = textfield {
            minWidth = NodeAttributes.minWidth
            alignment = Pos.CENTER
            textProperty().addListener { _, old, value ->
                if (!Regex("\\d+").matches(value) || old == value || value.isNullOrBlank()) return@addListener
                rowItem.currentCost = value.toInt()
            }
        }

        private val label = label {
            minWidth = NodeAttributes.minWidth
            textAlignment = TextAlignment.CENTER
        }

        override fun updateItem(item: Boolean?, empty: Boolean) {
            super.updateItem(item, empty)
            if (empty) {
                graphic = null
                return
            }
            if (!rowItem.add && rowItem.cost == 0 && !feature.add) {
                textField.text = rowItem.currentCost.toString()
                graphic = textField
            } else {
                label.text = rowItem.getCost()
                graphic = label
            }
        }
    }

    internal inner class ButtonCell : TableCell<Addon, Boolean>() {
        private var addButtonAddon = button(messages["add"]) {
            isVisible = false
            minWidth = 84.0
            setOnAction { addAddonCall() }
        }
        private var removeButtonAddon = button(messages["remove"]) {
            isVisible = false
            minWidth = 84.0
            setOnAction { removeAddonCall() }
        }

        private val paddedButtonAddon = stackpane {
            children.addAll(addButtonAddon, removeButtonAddon)
        }
        private val hBoxAddon = hbox {
            alignment = Pos.CENTER
            children.add(paddedButtonAddon)
        }

        private fun initButtons() {
            if (rowItem.add) {
                removeButtonAddon.isVisible = true
                tableRow.styleClass.add("isAdd")
            } else {
                addButtonAddon.isVisible = true
                tableRow.styleClass.remove("isAdd")
            }
        }

        private fun addAddonCall() {
            val addon = rowItem
            addon.add = true
            addonCost(addon)
            CharactersAddon(characterId = character.id, featureId = feature.id, itemId = addon.id, level = addon.level,
                            cost = addon.cost).create()
            addonsTableView.refresh()
        }

        private fun removeAddonCall() {
            val addon = rowItem
            addon.add = false
            val params = HashMap<String, Any>()
            params.put("characterId", character.id)
            params.put("itemId", addon.id)
            val charactersAddon = CharactersAddon().find_by(params) as CharactersAddon
            charactersAddon.destroy()
            addonCost(addon)
            addonsTableView.refresh()
        }

        private fun addonCost(addon: Addon) {
            if (addon.add) {
                feature.currentCost += pointsCostAddon(addon)
            } else {
                feature.currentCost -= pointsCostAddon(addon)
            }

            newCost(feature.currentCost)
        }

        private fun pointsCostAddon(addon: Addon): Int {
            return Math.ceil(feature.cost * feature.level * currentAddonCost(addon)).toInt()
        }

        private fun currentAddonCost(addon: Addon): Double {
            return (addon.currentCost.toDouble() * addon.level.toDouble() / 100.0)
        }

        override fun updateItem(t: Boolean?, empty: Boolean) {
            super.updateItem(t, empty)
            if (empty) {
                graphic = null
                return
            }

            initButtons()
            graphic = if (feature.add) null else hBoxAddon
        }
    }

    private fun type(): Text {
        val type = Text(feature.featureType + "\r\n\r\n")
        type.addClass("type")
        return type
    }

    private fun description(): Text {
        val description = Text(feature.description + "\r\n")
        description.addClass("description")
        return description
    }
}
