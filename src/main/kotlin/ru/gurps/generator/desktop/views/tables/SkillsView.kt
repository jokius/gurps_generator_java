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
import ru.gurps.generator.desktop.fragments.full.SkillFragment
import ru.gurps.generator.desktop.lib.LocalSearch
import ru.gurps.generator.desktop.models.characters.CharactersSkill
import ru.gurps.generator.desktop.models.characters.CharactersSpecialization
import ru.gurps.generator.desktop.models.rules.Skill
import ru.gurps.generator.desktop.singletons.*
import ru.gurps.generator.desktop.views.TabsView
import tornadofx.*
import java.util.*

class SkillsView : View() {
    override val root: AnchorPane by fxml("/templates/tables/SkillsView.fxml")
    val character = Characters.current!!
    val tabsView: TabsView by inject()

    private val tableView: TableView<Any> by fxid()
    private val nameColumn: TableColumn<Skill, String> by fxid()
    private val nameEnColumn: TableColumn<Skill, String> by fxid()
    private val typeAndComplexityColumn: TableColumn<Skill, String> by fxid()
    private val defaultUseColumn: TableColumn<Skill, String> by fxid()
    private val levelColumn: TableColumn<Skill, Boolean> by fxid()
    private val costColumn: TableColumn<Skill, Boolean> by fxid()
    private val actionsColumn: TableColumn<Skill, Boolean> by fxid()

    private val searchButton: MenuButton by fxid()
    private val searchAll: MenuItem by fxid()
    private val searchName: MenuItem by fxid()
    private val searchNameEn: MenuItem by fxid()
    private val searchDescription: MenuItem by fxid()
    private val reset: MenuItem by fxid()
    private val searchText: TextField by fxid()

    private val characteristicsMb: MenuButton by fxid()
    private val complexitiesMb: MenuButton by fxid()

    val level: TextField by fxid()
    val finalCost: Label by fxid()
    val add: Button by fxid()
    val remove: Button by fxid()
    val full: Button by fxid()


    val complexity: Label by fxid()
    val twoHands: Label by fxid()
    val parry: Label by fxid()

    private val characteristicsH: HashMap<String, Int> = HashMap()
    private val complexitiesH: HashMap<String, Int> = HashMap()

    var currentSkill = Skill()
    var currentCell: ActionsCell = ActionsCell()
    private val skillModel = Skill()

    val lazyLoad by lazy {
        setItems()
        setSkills()
        localSearch()
        setCheckBox()
    }

    private fun setItems() {
        val collection = skillModel.all()
        val params: HashMap<String, Any> = HashMap()
        params["characterId"] = character.id
        (collection as ObservableList<Skill>).forEach {
            params.put("itemId", it.id)
            val charactersSkill = CharactersSkill().find_by(params) as CharactersSkill
            it.level = CharacterParams.skillLevel(it)
            if (charactersSkill.id != -1) {
                it.cost = charactersSkill.cost
                it.level = charactersSkill.level
                it.add = true
            }
        }

        tableView.placeholder = Label(messages["skills_not_found"])
        tableView.items = collection
    }

    private fun setSkills() {
        nameColumn.cellValueFactory = PropertyValueFactory<Skill, String>("name")
        nameEnColumn.cellValueFactory = PropertyValueFactory<Skill, String>("nameEn")
        typeAndComplexityColumn.cellValueFactory = PropertyValueFactory<Skill, String>("typeAndComplexity")
        defaultUseColumn.cellValueFactory = PropertyValueFactory<Skill, String>("defaultUse")
        levelColumn.setCellValueFactory { SimpleBooleanProperty(true) }
        costColumn.setCellValueFactory { SimpleBooleanProperty(true) }
        actionsColumn.setCellValueFactory { SimpleBooleanProperty(true) }
        actionsColumn.setCellFactory { ActionsCell() }
        levelColumn.setCellFactory { LevelCell() }
        costColumn.setCellFactory { CostCell() }
    }

    private fun localSearch() {
        val ls = LocalSearch(Skill(), searchText)
        val all = skillModel.all()
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
        CharacteristicTypes.list.forEachIndexed { i, name ->
            val cm = CheckMenuItem(name)
            cm.isSelected = true
            cm.selectedProperty().addListener { _, _, value ->
                if (value) characteristicsH[name] = i
                else characteristicsH.remove(name)

                var query = "skillType like "
                characteristicsH.forEach {
                    query += if (query == "skillType like ") "'%${it.value}%'"
                    else " or skillType like '%${it.value}%'"
                }

                complexitiesH.forEach { query += " and complexity like '%${it.value}%'" }
                if (query == "skillType like ") query = "skillType='-1'"
                tableView.setItems(skillModel.where(query))
            }

            characteristicsH[name] = i
            characteristicsMb.items.add(cm)
        }

        ComplexityTypes.list.forEachIndexed { i, name ->
            val cm = CheckMenuItem(name)
            cm.isSelected = true
            cm.selectedProperty().addListener { _, _, value ->
                if (value) complexitiesH[name] = i
                else complexitiesH.remove(name)

                var query = "complexity like "
                complexitiesH.forEach {
                    if (query == "complexity like ") query += "'%${it.value}%'"
                    else query += " or complexity like '%${it.value}%'"
                }

                characteristicsH.forEach { query += " and skillType like '%${it.value}%'" }
                if (query == "complexity like ") query = "complexity='-1'"
                tableView.setItems(Skill().where(query))
            }

            complexitiesH[name] = i
            complexitiesMb.items.add(cm)
        }
    }

    private fun openFullInfo(cell: ActionsCell, skill: Skill) {
        currentSkill = skill
        currentCell = cell
        find(SkillFragment::class).openModal(stageStyle = StageStyle.UTILITY)
    }

    fun callAddOrUpdate(cell: ActionsCell) {
        val skill = cell.rowItem
        val charactersSkill = charactersSkill(skill)
        var cost = skill.cost
        if (charactersSkill.id == -1){
            CharactersSkill(character.id, skill.id, skill.cost, skill.level).create()
            cell.tableRow.styleClass.add("isAdd")
        }
        else {
            cost = skill.cost - charactersSkill.cost
            charactersSkill.cost = skill.cost
            charactersSkill.save()
        }

        tabsView.setCurrentPoints(character.currentPoints + cost)
        @Suppress("UNCHECKED_CAST")
        val col: TableColumn<Skill, Boolean> = tableView.columns.find { it == costColumn } as
                TableColumn<Skill, Boolean>
        col.isVisible = false
        col.isVisible = true
        skill.add = true
        cell.addButton.isVisible = false
        cell.removeButton.isVisible = skill.specializations.isEmpty()
    }

    fun callRemove(cell: ActionsCell) {
        val skill = cell.rowItem
        tabsView.setCurrentPoints(character.currentPoints - skill.cost)
        charactersSkill(skill).destroy()
        if (skill.specializations.any()) {
            val charactersSpecModel = CharactersSpecialization()
            val params = HashMap<String, Any>()
            params.put("characterId", character.id)
            params["skillId"] = skill.id
            charactersSpecModel.destroy_all(charactersSpecModel.where(params))
        }

        @Suppress("UNCHECKED_CAST")
        val col: TableColumn<Skill, Boolean> = tableView.columns.find { it == costColumn } as
                TableColumn<Skill, Boolean>
        col.isVisible = false
        col.isVisible = true
        skill.add = false
        cell.addButton.isVisible = skill.specializations.isEmpty()
        cell.removeButton.isVisible = false
        cell.tableRow.styleClass.remove("isAdd")
    }

    private fun charactersSkill(skill: Skill): CharactersSkill {
        val params = HashMap<String, Any>()
        params.put("characterId", character.id)
        params.put("itemId", skill.id)
        return CharactersSkill().find_by(params) as CharactersSkill
    }

    inner class LevelCell : TableCell<Skill, Boolean>() {
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

            if (rowItem.add || rowItem.specializations.any()) {
                label.text = rowItem.level.toString()
                graphic = label
                return
            }

            textField.text = rowItem.level.toString()
            textField.textProperty().addListener { _, _, value ->
                if (!Regex("\\d+").matches(value) || rowItem.level.toString() == value ||
                        value.isNullOrBlank()) return@addListener
                rowItem.level = value.toInt()
                rowItem.cost = CharacterParams.skillCost(rowItem)
                @Suppress("UNCHECKED_CAST")
                val col: TableColumn<Skill, Boolean> = tableView.columns.find { it == costColumn } as
                        TableColumn<Skill, Boolean>
                col.isVisible = false
                col.isVisible = true
                textField.positionCaret(value.length)
            }

            graphic = textField
        }
    }

    inner class CostCell : TableCell<Skill, Boolean>() {
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
            else CharacterParams.skillCost(rowItem).toString()

            graphic = label
        }
    }

    inner class ActionsCell : TableCell<Skill, Boolean>() {
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
                removeButton.isVisible = rowItem.specializations.isEmpty()
                tableRow.styleClass.add("isAdd")
            } else {
                addButton.isVisible = rowItem.specializations.isEmpty()
                tableRow.styleClass.remove("isAdd")
            }
        }

        private fun setCallBacks() {
            tableView.setOnKeyPressed { if (it.code == KeyCode.ENTER) openFullInfo(this, rowItem) }
            tableRow.setOnMouseClicked { if (it.clickCount == 2) openFullInfo(this, rowItem) }
            addButton.setOnAction { callAddOrUpdate(this) }
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
