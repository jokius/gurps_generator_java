package ru.gurps.generator.desktop.views.tables

import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.KeyCode
import javafx.stage.StageStyle
import ru.gurps.generator.desktop.interfaces.models.Transport
import ru.gurps.generator.desktop.interfaces.models.relationship.CharacterInventory
import ru.gurps.generator.desktop.config.Model
import ru.gurps.generator.desktop.fragments.full.TransportsAirFragment
import ru.gurps.generator.desktop.fragments.full.TransportsGroundFragment
import ru.gurps.generator.desktop.fragments.full.TransportsSpaceFragment
import ru.gurps.generator.desktop.fragments.full.TransportsWaterFragment
import ru.gurps.generator.desktop.lib.LocalSearch
import ru.gurps.generator.desktop.lib.TechnicalLevelMenu
import ru.gurps.generator.desktop.models.characters.transports.CharactersTransportsAir
import ru.gurps.generator.desktop.models.characters.transports.CharactersTransportsGround
import ru.gurps.generator.desktop.models.characters.transports.CharactersTransportsSpace
import ru.gurps.generator.desktop.models.characters.transports.CharactersTransportsWater
import ru.gurps.generator.desktop.models.rules.transports.TransportsAir
import ru.gurps.generator.desktop.models.rules.transports.TransportsGround
import ru.gurps.generator.desktop.models.rules.transports.TransportsSpace
import ru.gurps.generator.desktop.models.rules.transports.TransportsWater
import ru.gurps.generator.desktop.singletons.Characters
import ru.gurps.generator.desktop.singletons.NodeAttributes
import ru.gurps.generator.desktop.views.TabsView
import tornadofx.*
import java.util.*

class TransportsView : View() {
    override val root: Accordion by fxml("/templates/tables/TransportsView.fxml")
    val character = Characters.current!!
    val tabsView: TabsView by inject()

    private val tlMbG: MenuButton by fxid()
    private val searchButtonG: Button by fxid()
    private val searchTextG: TextField by fxid()
    private val tableViewG: TableView<Any> by fxid()

    private val skillsColumnG: TableColumn<Transport, String> by fxid()
    private val tlColumnG: TableColumn<Transport, String> by fxid()
    private val nameColumnG: TableColumn<Transport, String> by fxid()
    private val htColumnG: TableColumn<Transport, String> by fxid()
    private val moveColumnG: TableColumn<Transport, String> by fxid()
    private val occupantColumnG: TableColumn<Transport, String> by fxid()
    private val rangeColumnG: TableColumn<Transport, String> by fxid()
    private val costColumnG: TableColumn<Transport, String> by fxid()

    private val actionsColumnG: TableColumn<Transport, Boolean> by fxid()

    private val tlMbW: MenuButton by fxid()
    private val searchButtonW: Button by fxid()
    private val searchTextW: TextField by fxid()
    private val tableViewW: TableView<Any> by fxid()

    private val skillsColumnW: TableColumn<Transport, String> by fxid()
    private val tlColumnW: TableColumn<Transport, String> by fxid()
    private val nameColumnW: TableColumn<Transport, String> by fxid()
    private val htColumnW: TableColumn<Transport, String> by fxid()
    private val moveColumnW: TableColumn<Transport, String> by fxid()
    private val occupantColumnW: TableColumn<Transport, String> by fxid()
    private val rangeColumnW: TableColumn<Transport, String> by fxid()
    private val costColumnW: TableColumn<Transport, String> by fxid()

    private val actionsColumnW: TableColumn<Transport, Boolean> by fxid()

    private val tlMbA: MenuButton by fxid()
    private val searchButtonA: Button by fxid()
    private val searchTextA: TextField by fxid()
    private val tableViewA: TableView<Any> by fxid()

    private val skillsColumnA: TableColumn<Transport, String> by fxid()
    private val tlColumnA: TableColumn<Transport, String> by fxid()
    private val nameColumnA: TableColumn<Transport, String> by fxid()
    private val htColumnA: TableColumn<Transport, String> by fxid()
    private val moveColumnA: TableColumn<Transport, String> by fxid()
    private val occupantColumnA: TableColumn<Transport, String> by fxid()
    private val rangeColumnA: TableColumn<Transport, String> by fxid()
    private val costColumnA: TableColumn<Transport, String> by fxid()

    private val actionsColumnA: TableColumn<Transport, Boolean> by fxid()

    private val tlMbS: MenuButton by fxid()
    private val searchButtonS: Button by fxid()
    private val searchTextS: TextField by fxid()
    private val tableViewS: TableView<Any> by fxid()

    private val skillsColumnS: TableColumn<Transport, String> by fxid()
    private val tlColumnS: TableColumn<Transport, String> by fxid()
    private val nameColumnS: TableColumn<Transport, String> by fxid()
    private val htColumnS: TableColumn<Transport, String> by fxid()
    private val moveColumnS: TableColumn<Transport, String> by fxid()
    private val occupantColumnS: TableColumn<Transport, String> by fxid()
    private val rangeColumnS: TableColumn<Transport, String> by fxid()
    private val costColumnS: TableColumn<Transport, String> by fxid()

    private val actionsColumnS: TableColumn<Transport, Boolean> by fxid()

    private val modelTransportsGround = TransportsGround()
    private val modelTransportsWater = TransportsWater()
    private val modelTransportsAir = TransportsAir()
    private val modelTransportsSpace = TransportsSpace()

    var currentCell = ActionsCell()
    var currentTransport: Transport = modelTransportsGround

    val lazyLoad by lazy {
        setCheckBox()
        localSearch()
        setGroundTable()
        setWaterTable()
        setAirTable()
        setSpaceTable()
        setItems(model = modelTransportsGround, tableView = tableViewG, relationship = CharactersTransportsGround())
        setItems(model = modelTransportsWater, tableView = tableViewW, relationship = CharactersTransportsWater())
        setItems(model = modelTransportsAir, tableView = tableViewA, relationship = CharactersTransportsAir())
        setItems(model = modelTransportsSpace, tableView = tableViewS, relationship = CharactersTransportsSpace())
    }

    private fun setGroundTable() {
        skillsColumnG.cellValueFactory = PropertyValueFactory<Transport, String>("skills")
        nameColumnG.cellValueFactory = PropertyValueFactory<Transport, String>("name")
        tlColumnG.cellValueFactory = PropertyValueFactory<Transport, String>("tl")
        htColumnG.cellValueFactory = PropertyValueFactory<Transport, String>("ht")
        moveColumnG.cellValueFactory = PropertyValueFactory<Transport, String>("move")
        occupantColumnG.cellValueFactory = PropertyValueFactory<Transport, String>("occupant")
        rangeColumnG.cellValueFactory = PropertyValueFactory<Transport, String>("range")
        costColumnG.cellValueFactory = PropertyValueFactory<Transport, String>("cost")
        actionsColumnG.setCellValueFactory { SimpleBooleanProperty(true) }
        actionsColumnG.setCellFactory { ActionsCell() }
    }

    private fun setWaterTable() {
        skillsColumnW.cellValueFactory = PropertyValueFactory<Transport, String>("skills")
        nameColumnW.cellValueFactory = PropertyValueFactory<Transport, String>("name")
        tlColumnW.cellValueFactory = PropertyValueFactory<Transport, String>("tl")
        htColumnW.cellValueFactory = PropertyValueFactory<Transport, String>("ht")
        moveColumnW.cellValueFactory = PropertyValueFactory<Transport, String>("move")
        occupantColumnW.cellValueFactory = PropertyValueFactory<Transport, String>("occupant")
        rangeColumnW.cellValueFactory = PropertyValueFactory<Transport, String>("range")
        costColumnW.cellValueFactory = PropertyValueFactory<Transport, String>("cost")
        actionsColumnW.setCellValueFactory { SimpleBooleanProperty(true) }
        actionsColumnW.setCellFactory { ActionsCell() }
    }

    private fun setAirTable() {
        skillsColumnA.cellValueFactory = PropertyValueFactory<Transport, String>("skills")
        nameColumnA.cellValueFactory = PropertyValueFactory<Transport, String>("name")
        tlColumnA.cellValueFactory = PropertyValueFactory<Transport, String>("tl")
        htColumnA.cellValueFactory = PropertyValueFactory<Transport, String>("ht")
        moveColumnA.cellValueFactory = PropertyValueFactory<Transport, String>("move")
        occupantColumnA.cellValueFactory = PropertyValueFactory<Transport, String>("occupant")
        rangeColumnA.cellValueFactory = PropertyValueFactory<Transport, String>("range")
        costColumnA.cellValueFactory = PropertyValueFactory<Transport, String>("cost")
        actionsColumnA.setCellValueFactory { SimpleBooleanProperty(true) }
        actionsColumnA.setCellFactory { ActionsCell() }
    }

    private fun setSpaceTable() {
        skillsColumnS.cellValueFactory = PropertyValueFactory<Transport, String>("skills")
        nameColumnS.cellValueFactory = PropertyValueFactory<Transport, String>("name")
        tlColumnS.cellValueFactory = PropertyValueFactory<Transport, String>("tl")
        htColumnS.cellValueFactory = PropertyValueFactory<Transport, String>("ht")
        moveColumnS.cellValueFactory = PropertyValueFactory<Transport, String>("move")
        occupantColumnS.cellValueFactory = PropertyValueFactory<Transport, String>("occupant")
        rangeColumnS.cellValueFactory = PropertyValueFactory<Transport, String>("range")
        costColumnS.cellValueFactory = PropertyValueFactory<Transport, String>("cost")
        actionsColumnS.setCellValueFactory { SimpleBooleanProperty(true) }
        actionsColumnS.setCellFactory { ActionsCell() }
    }


    private fun setCheckBox() {
        TechnicalLevelMenu(model = modelTransportsGround, tableView = tableViewG, tlMb = tlMbG).set()
        TechnicalLevelMenu(model = modelTransportsWater, tableView = tableViewW, tlMb = tlMbW).set()
        TechnicalLevelMenu(model = modelTransportsAir, tableView = tableViewA, tlMb = tlMbA).set()
        TechnicalLevelMenu(model = modelTransportsSpace, tableView = tableViewS, tlMb = tlMbS).set()
    }

    private fun localSearch() {
        searchButtonG.setOnAction { tableViewG.setItems(LocalSearch(modelTransportsGround, searchTextG).searchName()) }
        searchButtonW.setOnAction { tableViewW.setItems(LocalSearch(modelTransportsWater, searchTextW).searchName()) }
        searchButtonA.setOnAction { tableViewA.setItems(LocalSearch(modelTransportsAir, searchTextA).searchName()) }
        searchButtonS.setOnAction { tableViewS.setItems(LocalSearch(modelTransportsWater, searchTextS).searchName()) }
    }

    private fun setItems(model: Model, tableView: TableView<Any>, relationship: Model) {
        val collection = model.all()
        val params: HashMap<String, Any> = HashMap()
        params["characterId"] = character.id
        (collection as ObservableList<Transport>).forEach {
            val record = relationship.find_by(params) as CharacterInventory
            params["itemId"] = it.id
            if (record.id > 0) {
                it.count = record.count
                it.add = true
            }
        }

        tableView.placeholder = Label(messages["equipments_not_found"])
        tableView.items = collection
    }

    private fun openFullInfo(cell: ActionsCell, transport: Transport) {
        currentCell = cell
        currentTransport = transport
        when (transport::class) {
            TransportsGround::class -> find(TransportsGroundFragment::class).openModal(stageStyle = StageStyle.UTILITY)
            TransportsWater::class -> find(TransportsWaterFragment::class).openModal(stageStyle = StageStyle.UTILITY)
            TransportsAir::class -> find(TransportsAirFragment::class).openModal(stageStyle = StageStyle.UTILITY)
            TransportsSpace::class -> find(TransportsSpaceFragment::class).openModal(stageStyle = StageStyle.UTILITY)
        }
    }

    fun callAdd(cell: ActionsCell) {
        val item = cell.rowItem
        item.addToCharacter(character)
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
        item.removeToCharacter(character)
        tabsView.setInventoryCost(-item.getFinalConstCents())
        item.add = false
        cell.addButton.isVisible = true
        cell.removeButton.isVisible = false
        cell.countLabel.isVisible = false
        cell.countTextField.isVisible = true
        cell.tableRow.styleClass.remove("isAdd")
    }

    inner class ActionsCell : TableCell<Transport, Boolean>() {
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
