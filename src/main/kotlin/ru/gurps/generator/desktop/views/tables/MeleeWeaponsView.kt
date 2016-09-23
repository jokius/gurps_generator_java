package ru.gurps.generator.desktop.views.tables

import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.KeyCode
import javafx.stage.StageStyle
import ru.gurps.generator.desktop.interfaces.models.MeleeWeaponInterface
import ru.gurps.generator.desktop.interfaces.models.relationship.CharacterInventory
import ru.gurps.generator.desktop.config.Model
import ru.gurps.generator.desktop.fragments.full.MeleeWeaponFragment
import ru.gurps.generator.desktop.fragments.full.MeleeWeaponRangedFragment
import ru.gurps.generator.desktop.lib.LocalSearch
import ru.gurps.generator.desktop.lib.TechnicalLevelMenu
import ru.gurps.generator.desktop.models.characters.CharactersMeleeWeapon
import ru.gurps.generator.desktop.models.characters.CharactersMeleeWeaponRanged
import ru.gurps.generator.desktop.models.rules.MeleeWeapon
import ru.gurps.generator.desktop.models.rules.MeleeWeaponRanged
import ru.gurps.generator.desktop.singletons.Characters
import ru.gurps.generator.desktop.singletons.NodeAttributes
import ru.gurps.generator.desktop.views.TabsView
import tornadofx.*
import java.util.*

class MeleeWeaponsView : View() {
    override val root: Accordion by fxml("/templates/tables/MeleeWeaponsView.fxml")
    val character = Characters.current!!
    val tabsView: TabsView by inject()

    private val tlMbMW: MenuButton by fxid()
    private val searchButtonMW: Button by fxid()
    private val searchTextMW: TextField by fxid()
    private val tableViewMW: TableView<Any> by fxid()
    private val skillsColumnMW: TableColumn<MeleeWeapon, String> by fxid()
    private val tlColumnMW: TableColumn<MeleeWeapon, String> by fxid()
    private val nameColumnMW: TableColumn<MeleeWeapon, String> by fxid()
    private val reachColumnMW: TableColumn<MeleeWeapon, String> by fxid()
    private val damageColumnMW: TableColumn<MeleeWeapon, String> by fxid()
    private val stColumnMW: TableColumn<MeleeWeapon, Int> by fxid()
    private val twoHandsColumnMW: TableColumn<MeleeWeapon, String> by fxid()
    private val costColumnMW: TableColumn<MeleeWeapon, String> by fxid()
    private val trainingColumnMW: TableColumn<MeleeWeapon, String> by fxid()
    private val actionsColumnMW: TableColumn<MeleeWeaponInterface, Boolean> by fxid()

    private val tlMbMWR: MenuButton by fxid()
    private val searchButtonMWR: Button by fxid()
    private val searchTextMWR: TextField by fxid()
    private val tableViewMWR: TableView<Any> by fxid()
    private val skillsColumnMWR: TableColumn<MeleeWeaponRanged, String> by fxid()
    private val tlColumnMWR: TableColumn<MeleeWeaponRanged, String> by fxid()
    private val nameColumnMWR: TableColumn<MeleeWeaponRanged, String> by fxid()
    private val rangeColumnMWR: TableColumn<MeleeWeaponRanged, String> by fxid()
    private val damageColumnMWR: TableColumn<MeleeWeaponRanged, String> by fxid()
    private val stColumnMWR: TableColumn<MeleeWeaponRanged, Int> by fxid()
    private val twoHandsColumnMWR: TableColumn<MeleeWeaponRanged, String> by fxid()
    private val costColumnMWR: TableColumn<MeleeWeaponRanged, String> by fxid()
    private val actionsColumnMWR: TableColumn<MeleeWeaponInterface, Boolean> by fxid()

    private val meleeWeaponModel = MeleeWeapon()
    private val meleeWeaponRangedModel = MeleeWeaponRanged()
    var currentWeapon: MeleeWeaponInterface = meleeWeaponModel
    var currentCell: ActionsCell = ActionsCell()

    val lazyLoad by lazy {
        setCheckBox()
        localSearch()
        setMeleeWeapons()
        setMeleeWeaponRanges()
        setItems(model = meleeWeaponModel, tableView = tableViewMW, relationship = CharactersMeleeWeapon())
        setItems(model = meleeWeaponRangedModel, tableView = tableViewMWR, relationship = CharactersMeleeWeaponRanged())
    }

    private fun setMeleeWeapons() {
        skillsColumnMW.cellValueFactory = PropertyValueFactory<MeleeWeapon, String>("skills")
        nameColumnMW.cellValueFactory = PropertyValueFactory<MeleeWeapon, String>("name")
        tlColumnMW.cellValueFactory = PropertyValueFactory<MeleeWeapon, String>("tl")
        reachColumnMW.cellValueFactory = PropertyValueFactory<MeleeWeapon, String>("reach")
        damageColumnMW.cellValueFactory = PropertyValueFactory<MeleeWeapon, String>("damage")
        stColumnMW.cellValueFactory = PropertyValueFactory<MeleeWeapon, Int>("st")
        twoHandsColumnMW.cellValueFactory = PropertyValueFactory<MeleeWeapon, String>("twoHands")
        trainingColumnMW.cellValueFactory = PropertyValueFactory<MeleeWeapon, String>("training")
        costColumnMW.cellValueFactory = PropertyValueFactory<MeleeWeapon, String>("cost")
        actionsColumnMW.setCellValueFactory { SimpleBooleanProperty(true) }
        actionsColumnMW.setCellFactory { ActionsCell() }
    }

    private fun setMeleeWeaponRanges() {
        skillsColumnMWR.cellValueFactory = PropertyValueFactory<MeleeWeaponRanged, String>("skills")
        nameColumnMWR.cellValueFactory = PropertyValueFactory<MeleeWeaponRanged, String>("name")
        tlColumnMWR.cellValueFactory = PropertyValueFactory<MeleeWeaponRanged, String>("tl")
        rangeColumnMWR.cellValueFactory = PropertyValueFactory<MeleeWeaponRanged, String>("range")
        damageColumnMWR.cellValueFactory = PropertyValueFactory<MeleeWeaponRanged, String>("damage")
        stColumnMWR.cellValueFactory = PropertyValueFactory<MeleeWeaponRanged, Int>("st")
        twoHandsColumnMWR.cellValueFactory = PropertyValueFactory<MeleeWeaponRanged, String>("twoHands")
        costColumnMWR.cellValueFactory = PropertyValueFactory<MeleeWeaponRanged, String>("cost")
        actionsColumnMWR.setCellValueFactory { SimpleBooleanProperty(true) }
        actionsColumnMWR.setCellFactory { ActionsCell() }
    }

    private fun setCheckBox() {
        TechnicalLevelMenu(model = MeleeWeapon(), tableView = tableViewMW, tlMb = tlMbMW).set()
        TechnicalLevelMenu(model = MeleeWeaponRanged(), tableView = tableViewMWR, tlMb = tlMbMWR).set()
    }

    private fun localSearch() {
        searchButtonMW.setOnAction { tableViewMW.setItems(LocalSearch(MeleeWeapon(), searchTextMW).searchName()) }
        searchButtonMWR.setOnAction {
            tableViewMWR.setItems(LocalSearch(MeleeWeaponRanged(), searchTextMWR).searchName())
        }
    }

    private fun setItems(model: Model, tableView: TableView<Any>, relationship: Model) {
        val collection = model.all()
        val params: HashMap<String, Any> = HashMap()
        params["characterId"] = character.id
        (collection as ObservableList<MeleeWeaponInterface>).forEach {
            params["itemId"] = it.id
            val record = relationship.find_by(params) as CharacterInventory
            if (record.id > 0) {
                it.count = record.count
                it.add = true
            }
        }

        tableView.placeholder = Label(messages["equipments_not_found"])
        tableView.items = collection
    }


    private fun openFullInfo(cell: ActionsCell, weapon: MeleeWeaponInterface) {
        currentCell = cell
        currentWeapon = weapon
        when(weapon::class){
            MeleeWeapon::class -> find(MeleeWeaponFragment::class).openModal(stageStyle = StageStyle.UTILITY)
            MeleeWeaponRanged::class ->
                find(MeleeWeaponRangedFragment::class).openModal(stageStyle = StageStyle.UTILITY)
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
        when(item::class) {
            MeleeWeapon::class -> {
                if ((item as MeleeWeapon).meleeWeaponRanged.id < 1) return
                (tableViewMWR.items.find { (it as MeleeWeaponRanged).id == item.meleeWeaponRanged.id }
                        as MeleeWeaponRanged).add = true
                tableViewMWR.refresh()
            }
            MeleeWeaponRanged::class -> {
                if ((item as MeleeWeaponRanged).meleeWeapon.id < 1) return
                (tableViewMW.items.find { (it as MeleeWeapon).id == item.meleeWeapon.id }
                        as MeleeWeapon).add = true
                tableViewMW.refresh()
            }
        }
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
        when(item::class){
            MeleeWeapon::class -> {
                if ((item as MeleeWeapon).meleeWeaponRanged.id < 1) return
                (tableViewMWR.items.find { (it as MeleeWeaponRanged).id == item.meleeWeaponRanged.id }
                        as MeleeWeaponRanged).add = false
                tableViewMWR.refresh()
            }
            MeleeWeaponRanged::class -> {
                if ((item as MeleeWeaponRanged).meleeWeapon.id < 1) return
                (tableViewMW.items.find { (it as MeleeWeapon).id == item.meleeWeapon.id }
                        as MeleeWeapon).add = false
                tableViewMW.refresh()
            }
        }
    }

    inner class ActionsCell : TableCell<MeleeWeaponInterface, Boolean>() {
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
