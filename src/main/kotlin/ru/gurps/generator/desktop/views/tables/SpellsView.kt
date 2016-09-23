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
import ru.gurps.generator.desktop.fragments.full.SpellFragment
import ru.gurps.generator.desktop.lib.LocalSearch
import ru.gurps.generator.desktop.models.characters.CharactersSpell
import ru.gurps.generator.desktop.models.rules.School
import ru.gurps.generator.desktop.models.rules.Spell
import ru.gurps.generator.desktop.singletons.CharacterParams
import ru.gurps.generator.desktop.singletons.Characters
import ru.gurps.generator.desktop.singletons.NodeAttributes
import ru.gurps.generator.desktop.singletons.SpellType
import ru.gurps.generator.desktop.views.TabsView
import tornadofx.*
import java.util.*

class SpellsView : View() {
    override val root: AnchorPane by fxml("/templates/tables/SpellsView.fxml")
    val character = Characters.current!!
    val tabsView: TabsView by inject()

    private val tableView: TableView<Any> by fxid()
    private val nameColumn: TableColumn<Spell, String> by fxid()
    private val nameEnColumn: TableColumn<Spell, String> by fxid()
    private val schoolColumn: TableColumn<Spell, String> by fxid()
    private val complexityColumn: TableColumn<Spell, String> by fxid()
    private val levelColumn: TableColumn<Spell, Boolean> by fxid()
    private val costColumn: TableColumn<Spell, Boolean> by fxid()
    private val actionsColumn: TableColumn<Spell, Boolean> by fxid()

    private val searchButton: MenuButton by fxid()
    private val searchAll: MenuItem by fxid()
    private val searchName: MenuItem by fxid()
    private val searchNameEn: MenuItem by fxid()
    private val searchDescription: MenuItem by fxid()
    private val reset: MenuItem by fxid()
    private val searchText: TextField by fxid()

    private val typesMb: MenuButton by fxid()
    private val schoolsMb: MenuButton by fxid()

    private val typesH: HashMap<String, Int> = HashMap()
    private val schoolsH: HashMap<String, Int> = HashMap()

    var currentSpell = Spell()
    var currentCell: ActionsCell = ActionsCell()
    private val spellModel = Spell()

    val lazyLoad by lazy {
        setItems()
        setSpells()
        localSearch()
        setCheckBox()
    }

    private fun setItems() {
        val collection = spellModel.all()
        val params: HashMap<String, Any> = HashMap()
        params["characterId"] = character.id
        (collection as ObservableList<Spell>).forEach {
            params.put("itemId", it.id)
            it.level = CharacterParams.spellLevelResult(it._complexity)
            val charactersSpell = CharactersSpell().find_by(params) as CharactersSpell
            if (charactersSpell.id != -1) {
                it.finalCost = charactersSpell.cost
                it.level = charactersSpell.level
                it.add = true
            }
        }

        tableView.placeholder = Label(messages["spells_not_found"])
        tableView.items = collection
    }

    private fun setSpells() {
        nameColumn.cellValueFactory = PropertyValueFactory<Spell, String>("name")
        nameEnColumn.cellValueFactory = PropertyValueFactory<Spell, String>("nameEn")
        complexityColumn.cellValueFactory = PropertyValueFactory<Spell, String>("complexity")
        schoolColumn.cellValueFactory = PropertyValueFactory<Spell, String>("schoolName")
        levelColumn.setCellValueFactory { SimpleBooleanProperty(true) }
        costColumn.setCellValueFactory { SimpleBooleanProperty(true) }
        actionsColumn.setCellValueFactory { SimpleBooleanProperty(true) }
        actionsColumn.setCellFactory { ActionsCell() }
        levelColumn.setCellFactory { LevelCell() }
        costColumn.setCellFactory { CostCell() }
    }

    private fun localSearch() {
        val ls = LocalSearch(spellModel, searchText)
        val all = spellModel.all()
        searchText.textProperty().addListener { _, _, value ->
            if (value == "") {
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

    private fun setCheckBox() {
        (School().all() as ObservableList<School>).forEach {
            val cm = CheckMenuItem(it.name)
            cm.isSelected = true
            cm.selectedProperty().addListener { _, _, value ->
                var spellsQ = "spellType like "
                var schoolsQ = " and schoolId="
                if (value) schoolsH[it.name] = it.id
                else schoolsH.remove(it.name)

                typesH.forEach {
                    spellsQ += if (spellsQ == "spellType like ") "'%${it.value}%'"
                    else " or spellType like '%${it.value}%'"
                }

                schoolsH.forEach {
                    schoolsQ += if (schoolsQ == " and schoolId = ") "${it.value}"
                    else " or schoolId = ${it.value}"
                }

                if (spellsQ == "spellType like ") spellsQ = "skillType='-1'"
                if (schoolsQ == " and schoolId=") schoolsQ = " and schoolId='-1'"
                tableView.setItems(Spell().where(spellsQ + schoolsQ))
            }

            schoolsH[it.name] = it.id
            schoolsMb.items.add(cm)
        }

        SpellType.list.forEachIndexed { i, name ->
            val cm = CheckMenuItem(name)
            cm.isSelected = true
            cm.selectedProperty().addListener { _, _, value ->
                var spellsQ = "spellType like "
                var schoolsQ = " and schoolId="
                if (value) typesH[name] = i
                else typesH.remove(name)

                typesH.forEach {
                    spellsQ += if (spellsQ == "spellType like ") "'%${it.value}%'"
                    else " or spellType like '%${it.value}%'"
                }

                schoolsH.forEach {
                    schoolsQ += if (schoolsQ == " and schoolId = ") "${it.value}"
                    else " or schoolId = ${it.value}"
                }

                if (spellsQ == "spellType like ") spellsQ = "skillType='-1'"
                if (schoolsQ == " and schoolId=") schoolsQ = " and schoolId='-1'"
                tableView.setItems(Spell().where(spellsQ + schoolsQ))
            }

            typesH[name] = i
            typesMb.items.add(cm)
        }
    }

    private fun openFullInfo(cell: ActionsCell, spell: Spell) {
        currentSpell = spell
        currentCell = cell
        find(SpellFragment::class).openModal(stageStyle = StageStyle.UTILITY)
    }

    fun callAdd(cell: ActionsCell) {
        val spell = cell.rowItem
        CharactersSpell(characterId = character.id, itemId = spell.id, cost = spell.finalCost,
                        level = spell.level).create()
        tabsView.setCurrentPoints(character.currentPoints + spell.finalCost)
        @Suppress("UNCHECKED_CAST")
        val col: TableColumn<Spell, Boolean> = tableView.columns.find { it == costColumn } as
                TableColumn<Spell, Boolean>
        col.isVisible = false
        col.isVisible = true
        spell.add = true
        cell.addButton.isVisible = false
        cell.removeButton.isVisible = true
        cell.tableRow.styleClass.add("isAdd")
    }

    fun callRemove(cell: ActionsCell) {
        val spell = cell.rowItem
        tabsView.setCurrentPoints(character.currentPoints - spell.finalCost)
        val params = HashMap<String, Any>()
        params.put("characterId", character.id)
        params.put("itemId", spell.id)
        CharactersSpell().find_by(params).destroy()
        @Suppress("UNCHECKED_CAST")
        val col: TableColumn<Spell, Boolean> = tableView.columns.find { it == costColumn } as
                TableColumn<Spell, Boolean>
        col.isVisible = false
        col.isVisible = true
        spell.add = false
        cell.addButton.isVisible = true
        cell.removeButton.isVisible = false
        cell.tableRow.styleClass.remove("isAdd")
    }

    inner class LevelCell : TableCell<Spell, Boolean>() {
        private val textField = textfield {
            minWidth = NodeAttributes.minWidth
            alignment = Pos.CENTER
        }

        val label = label {
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
                rowItem.finalCost = CharacterParams.spellCost(rowItem)
                @Suppress("UNCHECKED_CAST")
                val col: TableColumn<Spell, Boolean> = tableView.columns.find { it == costColumn } as
                        TableColumn<Spell, Boolean>
                col.isVisible = false
                col.isVisible = true
                textField.positionCaret(value.length)
            }

            graphic = textField
        }
    }

    inner class CostCell : TableCell<Spell, Boolean>() {
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

            label.text = if(rowItem.add) rowItem.finalCost.toString()
            else CharacterParams.spellCost(rowItem).toString()

            graphic = label
        }
    }

    inner class ActionsCell : TableCell<Spell, Boolean>() {
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
