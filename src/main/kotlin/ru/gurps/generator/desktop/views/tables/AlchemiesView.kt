package ru.gurps.generator.desktop.views.tables

import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.KeyCode
import javafx.scene.layout.AnchorPane
import javafx.stage.StageStyle
import ru.gurps.generator.desktop.fragments.full.AlchemyFragment
import ru.gurps.generator.desktop.lib.LocalSearch
import ru.gurps.generator.desktop.models.characters.CharactersAlchemy
import ru.gurps.generator.desktop.models.rules.Alchemy
import ru.gurps.generator.desktop.singletons.Characters
import ru.gurps.generator.desktop.singletons.NodeAttributes
import ru.gurps.generator.desktop.views.TabsView
import tornadofx.*
import java.util.*

class AlchemiesView : View() {
    override val root: AnchorPane by fxml("/templates/tables/AlchemiesView.fxml")
    val character = Characters.current!!
    val tabsView: TabsView by inject()

    private val tableView: TableView<Any> by fxid()
    private val nameColumn: TableColumn<Alchemy, String> by fxid()
    private val nameEnColumn: TableColumn<Alchemy, String> by fxid()
    private val actionsColumn: TableColumn<Alchemy, Boolean> by fxid()
    private val alternativeNamesColumn: TableColumn<Alchemy, String> by fxid()
    private val costColumn: TableColumn<Alchemy, String> by fxid()
    private val recipeCostColumn: TableColumn<Alchemy, String> by fxid()

    private val searchButton: MenuButton by fxid()
    private val searchAll: MenuItem by fxid()
    private val searchName: MenuItem by fxid()
    private val searchNameEn: MenuItem by fxid()
    private val searchDescription: MenuItem by fxid()
    private val reset: MenuItem by fxid()
    private val searchText: TextField by fxid()
    private val alchemyModel = Alchemy()
    var currentAlchemy = alchemyModel
    var currentCell: ActionsCell = ActionsCell()

    val lazyLoad by lazy {
        setItems()
        setAlchemies()
        localSearch()
    }

    private fun setAlchemies() {
        nameColumn.cellValueFactory = PropertyValueFactory("name")
        nameEnColumn.cellValueFactory = PropertyValueFactory("nameEn")
        alternativeNamesColumn.cellValueFactory = PropertyValueFactory("alternativeNames")
        costColumn.cellValueFactory = PropertyValueFactory("cost")
        recipeCostColumn.cellValueFactory = PropertyValueFactory("recipeCostDollars")
        actionsColumn.cellValueFactory = PropertyValueFactory("add")
        actionsColumn.setCellFactory { ActionsCell() }

    }

    private fun localSearch() {
        val ls = LocalSearch(Alchemy(), searchText)
        val all = alchemyModel.all()
        searchText.textProperty().addListener { _, _, value ->
            if (value == "") {
                searchButton.isDisable = true
                tableView.items = all
            } else
                searchButton.isDisable = false
        }

        searchAll.setOnAction { tableView.items = ls.searchAll() }
        searchName.setOnAction { tableView.items = ls.searchName() }
        searchNameEn.setOnAction { tableView.items = ls.searchNameEn() }
        searchDescription.setOnAction { tableView.items = ls.searchDescription() }

        reset.setOnAction {
            searchText.text = ""
            searchButton.isDisable = true
            tableView.items = all
        }
    }

    private fun setItems() {
        val alchemies = alchemyModel.all()
        val params = HashMap<String, Any>()
        params.put("characterId", character.id)

        (alchemies as ObservableList<Alchemy>).forEach {
            params.put("itemId", it.id)
            val charactersAlchemy = CharactersAlchemy().find_by(params) as CharactersAlchemy
            it.add = charactersAlchemy.id != -1
        }

        tableView.placeholder = Label(messages["alchemies_not_found"])
        tableView.items = alchemies
    }

    private fun openFullInfo(cell: ActionsCell, alchemy: Alchemy) {
        currentAlchemy = alchemy
        currentCell = cell
        find(AlchemyFragment::class).openModal(stageStyle = StageStyle.UTILITY)
    }

    fun callAdd(cell: ActionsCell) {
        val alchemy = cell.rowItem
        CharactersAlchemy(character.id, alchemy.id).create()
        tabsView.setInventoryCost(alchemy.recipeCost)
        alchemy.add = true
        cell.addButton.isVisible = false
        cell.removeButton.isVisible = true
        cell.tableRow.styleClass.add("isAdd")
    }

    fun callRemove(cell: ActionsCell) {
        val alchemy = cell.rowItem
        val params = HashMap<String, Any>()
        params.put("characterId", character.id)
        params.put("itemId", alchemy.id)
        (CharactersAlchemy().find_by(params) as CharactersAlchemy).destroy()
        tabsView.setInventoryCost(-alchemy.recipeCost)
        alchemy.add = false
        cell.addButton.isVisible = true
        cell.removeButton.isVisible = false
        cell.tableRow.styleClass.remove("isAdd")
    }

    inner class ActionsCell : TableCell<Alchemy, Boolean>() {
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
