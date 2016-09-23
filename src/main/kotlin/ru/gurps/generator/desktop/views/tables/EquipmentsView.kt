package ru.gurps.generator.desktop.views.tables

import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.KeyCode
import javafx.scene.layout.AnchorPane
import javafx.stage.StageStyle
import ru.gurps.generator.desktop.fragments.full.EquipmentFragment
import ru.gurps.generator.desktop.lib.LocalSearch
import ru.gurps.generator.desktop.lib.TechnicalLevelMenu
import ru.gurps.generator.desktop.models.characters.CharactersEquipment
import ru.gurps.generator.desktop.models.rules.Equipment
import ru.gurps.generator.desktop.models.rules.EquipmentsApplicationArea
import ru.gurps.generator.desktop.singletons.Characters
import ru.gurps.generator.desktop.singletons.NodeAttributes
import ru.gurps.generator.desktop.views.TabsView
import tornadofx.*
import java.util.*

class EquipmentsView : View() {
    override val root: AnchorPane by fxml("/templates/tables/EquipmentsView.fxml")
    val character = Characters.current!!
    val tabsView: TabsView by inject()

    private val tableView: TableView<Any> by fxid()
    private val applicationAreaColumn: TableColumn<Equipment, String> by fxid()
    private val nameColumn: TableColumn<Equipment, String> by fxid()
    private val tlColumn: TableColumn<Equipment, String> by fxid()
    private val weightColumn: TableColumn<Equipment, String> by fxid()
    private val costColumn: TableColumn<Equipment, String> by fxid()
    private val actionsColumn: TableColumn<Equipment, Boolean> by fxid()

    private val searchButton: MenuButton by fxid()
    private val searchAll: MenuItem by fxid()
    private val searchName: MenuItem by fxid()
    private val searchDescription: MenuItem by fxid()
    private val reset: MenuItem by fxid()
    private val searchText: TextField by fxid()

    private val applicationAreaMb: MenuButton by fxid()
    private val applicationAreaH: HashMap<String, Int> = HashMap()

    private val tlMb: MenuButton by fxid()

    private val equipmentModel = Equipment()
    var currentEquipment = equipmentModel
    var currentCell = ActionsCell()

    val lazyLoad by lazy {
        setCheckBox()
        localSearch()
        setEquipment()
        setItems()
    }

    private fun setEquipment() {
        applicationAreaColumn.cellValueFactory = PropertyValueFactory<Equipment, String>("applicationAreaName")
        nameColumn.cellValueFactory = PropertyValueFactory<Equipment, String>("name")
        tlColumn.cellValueFactory = PropertyValueFactory<Equipment, String>("tl")
        weightColumn.cellValueFactory = PropertyValueFactory<Equipment, String>("weight")
        costColumn.cellValueFactory = PropertyValueFactory<Equipment, String>("cost")
        actionsColumn.setCellValueFactory { SimpleBooleanProperty(true) }
        actionsColumn.setCellFactory { ActionsCell() }
    }

    private fun setCheckBox(){
        (EquipmentsApplicationArea().all() as ObservableList<EquipmentsApplicationArea>).forEach {
            val cm = CheckMenuItem(it.name)
            cm.isSelected = true
            cm.selectedProperty().addListener { _, _, value ->
                if (value) applicationAreaH[it.name] = it.id
                else applicationAreaH.remove(it.name)

                var query = "applicationAreaId in (${applicationAreaH.map { it.value }.joinToString { "$it" }})"

                if (applicationAreaH.isEmpty()) query = "applicationAreaId='-1'"
                tableView.setItems(equipmentModel.where(query))
            }

            applicationAreaH[it.name] = it.id
            applicationAreaMb.items.add(cm)
        }

        TechnicalLevelMenu(model = equipmentModel, tableView = tableView, tlMb = tlMb).set()
    }

    private fun localSearch() {
        val ls = LocalSearch(equipmentModel, searchText)
        val all = equipmentModel.all()
        searchText.textProperty().addListener { _, _, value ->
            if (value == "") {
                searchButton.isDisable = true
                tableView.setItems(all)
            } else
                searchButton.isDisable = false
        }

        searchAll.setOnAction { tableView.setItems(ls.searchAll()) }
        searchName.setOnAction { tableView.setItems(ls.searchName()) }
        searchDescription.setOnAction { tableView.setItems(ls.searchDescription()) }

        reset.setOnAction {
            searchText.text = ""
            searchButton.isDisable = true
            tableView.setItems(all)
        }
    }

    private fun setItems () {
        val equipments = equipmentModel.all()
        val params: HashMap<String, Any> = HashMap()
        params["characterId"] = character.id
        (equipments as ObservableList<Equipment>).forEach {
            params["itemId"] = it.id
            val record = CharactersEquipment().find_by(params) as CharactersEquipment
            if (record.id > 0) {
                it.count = record.count
                it.add = true
            }
        }

        tableView.placeholder = Label(messages["equipments_not_found"])
        tableView.items = equipments
    }

    private fun openFullInfo(cell: ActionsCell, equipment: Equipment) {
        currentCell = cell
        currentEquipment = equipment
        find(EquipmentFragment::class).openModal(stageStyle = StageStyle.UTILITY)
    }

    fun callAdd(cell: ActionsCell) {
        val item = cell.rowItem
        CharactersEquipment(character.id, item.id, item.count).create()
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
        CharactersEquipment().find_by(params).destroy()
        tabsView.setInventoryCost(-item.getFinalConstCents())
        item.add = false
        cell.addButton.isVisible = true
        cell.removeButton.isVisible = false
        cell.countLabel.isVisible = false
        cell.countTextField.isVisible = true
        cell.tableRow.styleClass.remove("isAdd")
    }

    inner class ActionsCell : TableCell<Equipment, Boolean>() {
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
