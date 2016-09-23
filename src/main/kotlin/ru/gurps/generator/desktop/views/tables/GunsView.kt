package ru.gurps.generator.desktop.views.tables

import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.KeyCode
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.HBox
import javafx.stage.StageStyle
import ru.gurps.generator.desktop.fragments.full.GunFragment
import ru.gurps.generator.desktop.lib.LegalityClassMenu
import ru.gurps.generator.desktop.lib.LocalSearch
import ru.gurps.generator.desktop.lib.TechnicalLevelMenu
import ru.gurps.generator.desktop.models.characters.CharactersGun
import ru.gurps.generator.desktop.models.rules.Gun
import ru.gurps.generator.desktop.models.rules.MeleeWeapon
import ru.gurps.generator.desktop.singletons.Characters
import ru.gurps.generator.desktop.singletons.NodeAttributes
import ru.gurps.generator.desktop.views.TabsView
import tornadofx.*
import java.util.*

class GunsView : View() {
    override val root: AnchorPane by fxml("/templates/tables/GunsView.fxml")
    val character = Characters.current!!
    val tabsView: TabsView by inject()

    private val tableView: TableView<Any> by fxid()
    private val legalityClassColumn: TableColumn<Gun, Int> by fxid()
    private val skillsColumn: TableColumn<Gun, String> by fxid()
    private val nameColumn: TableColumn<Gun, String> by fxid()
    private val tlColumn: TableColumn<Gun, String> by fxid()
    private val damageColumn: TableColumn<Gun, String> by fxid()
    private val costColumn: TableColumn<Gun, String> by fxid()
    private val actionsColumn: TableColumn<Gun, Boolean> by fxid()

    private val searchButton: Button by fxid()
    private val searchText: TextField by fxid()

    private val tlMb: MenuButton by fxid()
    private val legalityClassMb: MenuButton by fxid()

    private val gunModel = Gun()
    var currentGun = gunModel
    var currentCell = ActionsCell()

    val lazyLoad by lazy {
        TechnicalLevelMenu(model = gunModel, tableView = tableView, tlMb = tlMb).set()
        LegalityClassMenu(model = gunModel, tableView = tableView, mb = legalityClassMb).set()
        searchButton.setOnAction { tableView.setItems(LocalSearch(MeleeWeapon(), searchText).searchName()) }

        setGuns()
        setItems()
    }

    private fun setGuns() {
        legalityClassColumn.cellValueFactory = PropertyValueFactory<Gun, Int>("legalityClass")
        skillsColumn.cellValueFactory = PropertyValueFactory<Gun, String>("skills")
        nameColumn.cellValueFactory = PropertyValueFactory<Gun, String>("name")
        tlColumn.cellValueFactory = PropertyValueFactory<Gun, String>("tl")
        damageColumn.cellValueFactory = PropertyValueFactory<Gun, String>("damage")
        costColumn.cellValueFactory = PropertyValueFactory<Gun, String>("cost")
        actionsColumn.setCellValueFactory { SimpleBooleanProperty(true) }
        actionsColumn.setCellFactory { ActionsCell() }
    }

    private fun setItems() {
        val grenades = gunModel.all()
        val params: HashMap<String, Any> = HashMap()
        params["characterId"] = character.id
        (grenades as ObservableList<Gun>).forEach {
            params["itemId"] = it.id
            val record = CharactersGun().find_by(params) as CharactersGun
            if (record.id > 0) {
                it.count = record.count
                it.add = true
            }
        }

        tableView.placeholder = Label(messages["equipments_not_found"])
        tableView.items = grenades
    }

    private fun openFullInfo(cell: ActionsCell, grenade: Gun) {
        currentCell = cell
        currentGun = grenade
        find(GunFragment::class).openModal(stageStyle = StageStyle.UTILITY)
    }

    fun callAdd(cell: ActionsCell) {
        val item = cell.rowItem
        CharactersGun(character.id, item.id, item.count).create()
        tabsView.setInventoryCost(item.getFinalConstCents())
        item.add = true
        cell.addButton.isVisible = false
        cell.removeButton.isVisible = true
        cell.countTextField.isVisible = false
        cell.countLabel.isVisible = true
        cell.tableRow.styleClass.add("isAdd")
    }

    fun callRemove(cell: ActionsCell) {
        val item = cell.rowItem
        val params = HashMap<String, Any>()
        params.put("characterId", character.id)
        params.put("itemId", item.id)
        CharactersGun().find_by(params).destroy()
        tabsView.setInventoryCost(-item.getFinalConstCents())
        item.add = false
        cell.addButton.isVisible = true
        cell.removeButton.isVisible = false
        cell.countLabel.isVisible = false
        cell.countTextField.isVisible = true
        cell.tableRow.styleClass.remove("isAdd")
    }

    inner class ActionsCell : TableCell<Gun, Boolean>() {
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

        val countTextField = textfield {
            isVisible = false
            alignment = Pos.CENTER
            minWidth = NodeAttributes.minWidth
            maxWidth = NodeAttributes.maxWidth
            textProperty().addListener { _, old, value ->
                if (old == value || !Regex("-?\\d+").matches(value)) return@addListener
                rowItem.count = value.toInt()
                countLabel.text = value
                finalCostLabel.text = rowItem.getFinalCost()
            }
        }

        val countLabel = label {
            isVisible = false
            alignment = Pos.CENTER
            minWidth = NodeAttributes.minWidth
            maxWidth = NodeAttributes.maxWidth
        }

        private val finalCostLabel = label {
            alignment = Pos.CENTER
            minWidth = NodeAttributes.minWidth
            maxWidth = NodeAttributes.maxWidth
        }

        private val countStack = stackpane {
            children.addAll(countLabel, countTextField)
        }

        private val paddedButton = stackpane {
            children.addAll(addButton, removeButton)
        }

        private val hBox = hbox {
            alignment = Pos.CENTER_LEFT
            spacing = 10.0
            children.addAll(countStack, finalCostLabel, paddedButton, fullButton)
        }

        private val vBox = vbox {
            alignment = Pos.CENTER_LEFT
            spacing = 1.0
            children.add(hBox)
        }

        private fun setRow() {
            if (rowItem.add) {
                removeButton.isVisible = true
                countLabel.isVisible = true
                tableRow.styleClass.add("isAdd")
            } else {
                addButton.isVisible = true
                countTextField.isVisible = true
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

        private fun setCountAndCost() {
            countLabel.text = rowItem.count.toString()
            countTextField.text = rowItem.count.toString()
            finalCostLabel.text = rowItem.getFinalCost()
        }

        override fun updateItem(t: Boolean?, empty: Boolean) {
            super.updateItem(t, empty)

            if (tableRow.item == null || empty) {
                graphic = null
                return
            }

            setCallBacks()
            setCountAndCost()
            setRow()
            graphic = vBox
        }
    }
}
