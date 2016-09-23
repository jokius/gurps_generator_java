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
import ru.gurps.generator.desktop.fragments.full.TechniquesFragment
import ru.gurps.generator.desktop.lib.LocalSearch
import ru.gurps.generator.desktop.models.characters.CharactersTechnique
import ru.gurps.generator.desktop.models.rules.Technique
import ru.gurps.generator.desktop.singletons.CharacterParams
import ru.gurps.generator.desktop.singletons.Characters
import ru.gurps.generator.desktop.singletons.ComplexityTypes
import ru.gurps.generator.desktop.singletons.NodeAttributes
import ru.gurps.generator.desktop.views.TabsView
import tornadofx.*
import java.util.*

class TechniquesView : View() {
    override val root: AnchorPane by fxml("/templates/tables/TechniquesView.fxml")
    val character = Characters.current!!
    val tabsView: TabsView by inject()

    private val tableView: TableView<Any> by fxid()
    private val nameColumn: TableColumn<Technique, String> by fxid()
    private val nameEnColumn: TableColumn<Technique, String> by fxid()
    private val complexityColumn: TableColumn<Technique, String> by fxid()
    private val defaultUseColumn: TableColumn<Technique, String> by fxid()
    private val levelColumn: TableColumn<Technique, Boolean> by fxid()
    private val costColumn: TableColumn<Technique, Boolean> by fxid()
    private val actionsColumn: TableColumn<Technique, Boolean> by fxid()

    private val searchButton: MenuButton by fxid()
    private val searchAll: MenuItem by fxid()
    private val searchName: MenuItem by fxid()
    private val searchNameEn: MenuItem by fxid()
    private val searchDescription: MenuItem by fxid()
    private val reset: MenuItem by fxid()
    private val searchText: TextField by fxid()
    private val complexitiesMb: MenuButton by fxid()
    private val complexitiesH: HashMap<String, Int> = HashMap()

    var currentTechnique = Technique()
    var currentCell: ActionsCell = ActionsCell()
    private val techniqueModel = Technique()

    val lazyLoad by lazy {
        setItems()
        setTechniques()
        localSearch()
        setCheckBox()
    }

    private fun setItems() {
        val collection = techniqueModel.all()
        val params: HashMap<String, Any> = HashMap()
        params["characterId"] = character.id
        (collection as ObservableList<Technique>).forEach {
            params.put("itemId", it.id)
            val charactersTechnique = CharactersTechnique().find_by(params) as CharactersTechnique
            if (charactersTechnique.id != -1) {
                it.cost = charactersTechnique.cost
                it.level = charactersTechnique.level
                it.add = true
            }
        }

        tableView.placeholder = Label(messages["techniques_not_found"])
        tableView.items = collection
    }

    private fun setTechniques() {
        nameColumn.cellValueFactory = PropertyValueFactory<Technique, String>("name")
        nameEnColumn.cellValueFactory = PropertyValueFactory<Technique, String>("nameEn")
        complexityColumn.cellValueFactory = PropertyValueFactory<Technique, String>("complexity")
        defaultUseColumn.cellValueFactory = PropertyValueFactory<Technique, String>("defaultUse")
        levelColumn.setCellValueFactory { SimpleBooleanProperty(true) }
        costColumn.setCellValueFactory { SimpleBooleanProperty(true) }
        actionsColumn.setCellValueFactory { SimpleBooleanProperty(true) }
        actionsColumn.setCellFactory { ActionsCell() }
        levelColumn.setCellFactory { LevelCell() }
        costColumn.setCellFactory { CostCell() }
    }

    private fun localSearch() {
        val ls = LocalSearch(techniqueModel, searchText)
        val all = techniqueModel.all()
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
        searchDescription.setOnAction { tableView.setItems(ls.searchDescription()) }

        reset.setOnAction {
            searchText.text = ""
            searchButton.isDisable = true
            tableView.setItems(all)
        }
    }

    private fun setCheckBox(){
        ComplexityTypes.list.forEachIndexed { i, name ->
            val cm = CheckMenuItem(name)
            cm.isSelected = true
            cm.selectedProperty().addListener { _, _, value ->
                if (value) complexitiesH[name] = i
                else complexitiesH.remove(name)

                var query = "complexity like "
                complexitiesH.forEach {
                    query += if (query == "complexity like ") "'%${it.value}%'"
                    else " or complexity like '%${it.value}%'"
                }

                if (query == "complexity like ") query = "complexity='-1'"
                tableView.setItems(Technique().where(query))
            }

            complexitiesH[name] = i
            complexitiesMb.items.add(cm)
        }
    }

    private fun openFullInfo(cell: ActionsCell, technique: Technique) {
        currentTechnique = technique
        currentCell = cell
        find(TechniquesFragment::class).openModal(stageStyle = StageStyle.UTILITY)
    }

    fun callAdd(cell: ActionsCell) {
        val technique = cell.rowItem
        CharactersTechnique(characterId = character.id, itemId = technique.id, cost = technique.cost,
                level = technique.level).create()
        tabsView.setCurrentPoints(character.currentPoints + technique.cost)
        @Suppress("UNCHECKED_CAST")
        val col: TableColumn<Technique, Boolean> = tableView.columns.find { it == costColumn } as
                TableColumn<Technique, Boolean>
        col.isVisible = false
        col.isVisible = true
        technique.add = true
        cell.addButton.isVisible = false
        cell.removeButton.isVisible = true
        cell.tableRow.styleClass.add("isAdd")
    }

    fun callRemove(cell: ActionsCell) {
        val technique = cell.rowItem
        tabsView.setCurrentPoints(character.currentPoints - technique.cost)
        val params = HashMap<String, Any>()
        params.put("characterId", character.id)
        params.put("itemId", technique.id)
        CharactersTechnique().find_by(params).destroy()
        @Suppress("UNCHECKED_CAST")
        val col: TableColumn<Technique, Boolean> = tableView.columns.find { it == costColumn } as
                TableColumn<Technique, Boolean>
        col.isVisible = false
        col.isVisible = true
        technique.add = false
        cell.addButton.isVisible = true
        cell.removeButton.isVisible = false
        cell.tableRow.styleClass.remove("isAdd")
    }

    inner class LevelCell : TableCell<Technique, Boolean>() {
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

            if (rowItem.add) {
                label.text = rowItem.level.toString()
                graphic = label
                return
            }

            textField.text = rowItem.level.toString()
            textField.textProperty().addListener { _, _, value ->
                if (!Regex("\\d+").matches(value) || rowItem.level.toString() == value ||
                        value.isNullOrBlank()) return@addListener
                rowItem.level = value.toInt()
                rowItem.cost = CharacterParams.techniqueCost(rowItem)
                @Suppress("UNCHECKED_CAST")
                val col: TableColumn<Technique, Boolean> = tableView.columns.find { it == costColumn } as
                        TableColumn<Technique, Boolean>
                col.isVisible = false
                col.isVisible = true
                textField.positionCaret(value.length)
            }

            graphic = textField
        }
    }

    inner class CostCell : TableCell<Technique, Boolean>() {
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

            label.text = if(rowItem.add) rowItem.cost.toString()
            else CharacterParams.techniqueCost(rowItem).toString()

            graphic = label
        }
    }

    inner class ActionsCell : TableCell<Technique, Boolean>() {
        val addButton = button(messages["add"]) {
            isVisible = false
            minWidth = NodeAttributes.minWidth
        }
        val removeButton = button(messages["remove"]) {
            isVisible = false
            minWidth = NodeAttributes.minWidth
        }
        val fullButton = button(messages["full"]) {
            minWidth = NodeAttributes.minWidth
        }

        private val paddedButton = stackpane {
            children.addAll(addButton, removeButton)
        }

        private val hBox = hbox {
            alignment = Pos.CENTER
            spacing = 10.0
            children.addAll(paddedButton, fullButton)
        }

        private fun setRow() {
            if (rowItem.add) {
                removeButton.isVisible = true
                tableRow.styleClass.add("isAdd")
            } else {
                addButton.isVisible = true
                tableRow.styleClass.remove("isAdd")
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
            graphic = hBox
        }
    }
}
