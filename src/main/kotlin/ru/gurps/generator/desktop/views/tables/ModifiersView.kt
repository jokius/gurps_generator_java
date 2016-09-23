package ru.gurps.generator.desktop.views.tables

import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.KeyCode
import javafx.scene.layout.AnchorPane
import javafx.scene.text.TextAlignment
import javafx.stage.StageStyle
import ru.gurps.generator.desktop.fragments.full.ModifierFragment
import ru.gurps.generator.desktop.lib.LocalSearch
import ru.gurps.generator.desktop.models.characters.CharactersModifier
import ru.gurps.generator.desktop.models.rules.Feature
import ru.gurps.generator.desktop.models.rules.Modifier
import ru.gurps.generator.desktop.singletons.Characters
import ru.gurps.generator.desktop.singletons.NodeAttributes
import ru.gurps.generator.desktop.views.TabsView
import tornadofx.*
import java.util.*

class ModifiersView : View() {
    override val root: AnchorPane by fxml("/templates/tables/ModifiersView.fxml")
    val character = Characters.current!!
    val tabsView: TabsView by inject()

    val tableView: TableView<Any> by fxid()
    private val modifiersNameColumn: TableColumn<Modifier, String> by fxid()
    private val modifiersNameEnColumn: TableColumn<Modifier, String> by fxid()
    private val costColumn: TableColumn<Modifier, Int> by fxid()
    private val levelColumn: TableColumn<Modifier, Boolean> by fxid()
    private val combatColumn: TableColumn<Modifier, String> by fxid()
    private val actionsColumn: TableColumn<Modifier, Boolean> by fxid()

    private val searchButton: MenuButton by fxid()
    private val searchAll: MenuItem by fxid()
    private val searchName: MenuItem by fxid()
    private val searchNameEn: MenuItem by fxid()
    private val searchCost: MenuItem by fxid()
    private val searchDescription: MenuItem by fxid()
    private val reset: MenuItem by fxid()
    private val searchText: TextField by fxid()


    private val combatCheckBox: CheckMenuItem by fxid()
    private val improvingCheckBox: CheckMenuItem by fxid()
    private val limitationCheckBox: CheckMenuItem by fxid()

    val full: Button by fxid()
    val add: Button by fxid()
    val remove: Button by fxid()

    var modifierModel = Modifier()
    var currentModifier = modifierModel
    var currentCell: ActionsCell = ActionsCell()

    val lazyLoad by lazy {
        setModifiers()
        setItems()
        setCheckBox()
        localSearch()
    }

    private fun setModifiers() {
        modifiersNameColumn.cellValueFactory = PropertyValueFactory<Modifier, String>("name")
        modifiersNameEnColumn.cellValueFactory = PropertyValueFactory<Modifier, String>("nameEn")
        costColumn.cellValueFactory = PropertyValueFactory<Modifier, Int>("currentCost")
        levelColumn.setCellValueFactory { SimpleBooleanProperty(true) }
        combatColumn.cellValueFactory = PropertyValueFactory<Modifier, String>("combat")
        actionsColumn.setCellValueFactory { SimpleBooleanProperty(true) }
        actionsColumn.setCellFactory { ActionsCell() }
        levelColumn.setCellFactory { LevelCell() }
    }

    private fun setItems() {
        val collection = modifierModel.all()
        val params: HashMap<String, Any> = HashMap()
        params["characterId"] = character.id
        (collection as ObservableList<Modifier>).forEach {
            params.put("modifierId", it.id)
            val charactersModifier = CharactersModifier().find_by(params) as CharactersModifier
            if (charactersModifier.id > 0) {
                it.currentCost = charactersModifier.cost
                it.level = charactersModifier.level
                it.add = true
                it.feature = character.features().find { it.id == charactersModifier.featureId }!!
            }
        }

        tableView.placeholder = Label(messages["modifiers_not_found"])
        tableView.items = collection
    }

    private fun localSearch() {
        val ls = LocalSearch(modifierModel, searchText)
        val all = modifierModel.all()
        searchText.textProperty().addListener { _, _, newValue ->
            if (newValue == "") {
                searchButton.isDisable = true
                tableView.setItems(all)
            } else
                searchButton.isDisable = false
        }

        searchAll.setOnAction { tableView.setItems(ls.searchAll()) }
        searchName.setOnAction { tableView.setItems(ls.searchName()) }
        searchNameEn.setOnAction { tableView.setItems(ls.searchNameEn()) }
        searchCost.setOnAction { tableView.setItems(ls.searchCost()) }
        searchDescription.setOnAction { tableView.setItems(ls.searchDescription()) }

        reset.setOnAction {
            searchText.text = ""
            searchButton.isDisable = true
            tableView.setItems(all)
        }
    }

    private fun setCheckBox() {
        combatCheckBox.isSelected = true
        improvingCheckBox.isSelected = true
        limitationCheckBox.isSelected = true

        combatCheckBox.selectedProperty().addListener { _, _, value ->
            var query = ""
            query += if (value) "combat=true" else "combat=false"
            if (improvingCheckBox.isSelected && !limitationCheckBox.isSelected)
                query += " and improving=true"
            else if (!improvingCheckBox.isSelected && limitationCheckBox.isSelected)
                query += " and improving=true"
            else if (!improvingCheckBox.isSelected) query += " and id='-1'"
            tableView.items = modifierModel.where(query)
        }

        improvingCheckBox.selectedProperty().addListener { _, _, value ->
            var query = ""

            if (value!! && !limitationCheckBox.isSelected)
                query += " and improving=true"
            else if (!value && limitationCheckBox.isSelected)
                query += " and improving=true"
            else if (!value) query += " and id='-1'"

            query += if (improvingCheckBox.isSelected) " and combat=true" else " and combat=false"
            tableView.items = modifierModel.where(query)
        }

        limitationCheckBox.selectedProperty().addListener { _, _, value ->
            var query = ""

            if (improvingCheckBox.isSelected && !value)
                query += " and improving=true"
            else if (!improvingCheckBox.isSelected && value!!)
                query += " and improving=true"
            else if (!improvingCheckBox.isSelected) query += " and id='-1'"

            query += if (improvingCheckBox.isSelected) " and combat=true" else " and combat=false"
            tableView.items = modifierModel.where(query)
        }
    }

    private fun openFullInfo(cell: ActionsCell, modifier: Modifier) {
        currentCell = cell
        currentModifier = modifier
        find(ModifierFragment::class).openModal(stageStyle = StageStyle.UTILITY)
    }

    fun callAdd(cell: ActionsCell) {
        val modifier = cell.rowItem
        CharactersModifier(character.id, modifier.id, modifier.feature.id, modifier.cost, modifier.level).create()
        tabsView.setCurrentPoints(character.currentPoints + modifier.featureCost())
        modifier.add = true
        cell.setFeatureNode()
        cell.addButton.isVisible = false
        cell.removeButton.isVisible = true
        cell.tableRow.styleClass.add("isAdd")
    }

    fun callRemove(cell: ActionsCell) {
        val modifier = cell.rowItem
        val params = HashMap<String, Any>()
        params["characterId"] = character.id
        params["modifierId"] = modifier.id
        params["featureId"] = modifier.feature.id

        CharactersModifier().find_by(params).destroy()
        tabsView.setCurrentPoints(character.currentPoints - modifier.featureCost())
        modifier.add = false
        cell.setFeatureNode()
        cell.addButton.isVisible = true
        cell.removeButton.isVisible = false
        cell.addButton.isDisable = modifier.feature.id == -1
        cell.tableRow.styleClass.remove("isAdd")
    }

    inner class LevelCell : TableCell<Modifier, Boolean>() {
        private val textField = textfield {
            minWidth = NodeAttributes.minWidth
            alignment = Pos.CENTER
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

            if (rowItem.add || rowItem.maxLevel > 0) {
                label.text = rowItem.level.toString()
                graphic = label
                return
            }

            textField.text = rowItem.level.toString()
            textField.textProperty().addListener { _, _, value ->
                if (!Regex("\\d+").matches(value) || rowItem.level.toString() == value ||
                        value.isNullOrBlank()) return@addListener
                rowItem.level = value.toInt()
                rowItem.currentCost = rowItem.cost * rowItem.level
                @Suppress("UNCHECKED_CAST")
                val col: TableColumn<Modifier, Boolean> = tableView.columns.find { it == costColumn } as
                        TableColumn<Modifier, Boolean>
                col.isVisible = false
                col.isVisible = true
                textField.positionCaret(value.length)
            }

            graphic = textField
        }
    }

    inner class ActionsCell : TableCell<Modifier, Boolean>() {
        val addButton = button(messages["add"]) {
            isVisible = false
            minWidth = NodeAttributes.minWidth
        }

        val removeButton = button(messages["remove"]) {
            isVisible = false
            minWidth = NodeAttributes.minWidth
        }

        private val fullButton = button(messages["full"]) {
            minWidth = NodeAttributes.minWidth
        }

        private val featureComboBox = combobox<Feature> {
            isVisible = false
            minWidth = NodeAttributes.minWidth
            valueProperty().addListener { _, _, value ->
                if (value == null) return@addListener
                rowItem.feature = value
                addButton.isDisable = false
            }
        }

        private val featureLabel = label {
            isVisible = false
        }

        private val featureStack = stackpane {
            children.addAll(featureComboBox, featureLabel)
        }

        private val paddedButton = stackpane {
            children.addAll(addButton, removeButton)
        }

        private val hBox = hbox {
            alignment = Pos.CENTER_LEFT
            spacing = 5.0
            children.addAll(featureStack, paddedButton, fullButton)
        }

        private fun setRow() {
            if (rowItem.add) {
                removeButton.isVisible = true
                tableRow.styleClass.add("isAdd")
            } else {
                addButton.isVisible = true
                addButton.isDisable = rowItem.feature.id == -1
                tableRow.styleClass.remove("isAdd")
            }
        }

        fun setFeatureNode() {
            if (character.features().isEmpty()) {
                featureLabel.isVisible = true
                featureLabel.text = messages["features_not_found"]
                return
            }

            if (rowItem.add) {
                featureLabel.text = rowItem.feature.toString()
                featureLabel.isVisible = true
                featureComboBox.isVisible = false
            } else {
                featureComboBox.items = character.features()
                featureComboBox.isVisible = true
                featureLabel.isVisible = false
            }
        }

        private fun setCallBacks() {
            tableView.setOnKeyPressed { if (it.code == KeyCode.ENTER) openFullInfo(this, rowItem) }
            tableRow.setOnMouseClicked { if (it.clickCount == 2) openFullInfo(this, rowItem) }
            addButton.setOnAction { callAdd(this) }
            removeButton.setOnAction { callRemove(this) }
            fullButton.setOnAction { openFullInfo(this, rowItem) }
        }

        override fun updateItem(t: Boolean?, empty: Boolean) {
            super.updateItem(t, empty)
            if (empty) {
                graphic = null
                return
            }

            setRow()
            setCallBacks()
            setFeatureNode()
            graphic = hBox
        }
    }
}
