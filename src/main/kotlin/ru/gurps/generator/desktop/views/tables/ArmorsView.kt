package ru.gurps.generator.desktop.views.tables

import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.KeyCode
import javafx.stage.StageStyle
import ru.gurps.generator.desktop.interfaces.models.Armor
import ru.gurps.generator.desktop.interfaces.models.relationship.CharacterInventory
import ru.gurps.generator.desktop.config.Model
import ru.gurps.generator.desktop.fragments.full.ManArmorFragment
import ru.gurps.generator.desktop.fragments.full.NotManArmorFragment
import ru.gurps.generator.desktop.lib.LegalityClassMenu
import ru.gurps.generator.desktop.lib.LocalSearch
import ru.gurps.generator.desktop.lib.TechnicalLevelMenu
import ru.gurps.generator.desktop.models.characters.*
import ru.gurps.generator.desktop.models.rules.ArmorsAddon
import ru.gurps.generator.desktop.models.rules.ManArmor
import ru.gurps.generator.desktop.models.rules.NotManArmor
import ru.gurps.generator.desktop.singletons.Characters
import ru.gurps.generator.desktop.singletons.NodeAttributes
import ru.gurps.generator.desktop.views.TabsView
import tornadofx.*
import java.util.*

class ArmorsView : View() {
    override val root: Accordion by fxml("/templates/tables/ArmorsView.fxml")
    val character = Characters.current!!
    val tabsView: TabsView by inject()

    private val legalityClassMbMA: MenuButton by fxid()
    private val slotMbMA: MenuButton by fxid()
    private val tlMbMA: MenuButton by fxid()
    private val searchButtonMA: Button by fxid()
    private val searchTextMA: TextField by fxid()
    private val tableViewMA: TableView<Any> by fxid()
    private val legalityClassColumnMA: TableColumn<Armor, Int> by fxid()
    private val slotColumnMA: TableColumn<Armor, String> by fxid()
    private val tlColumnMA: TableColumn<Armor, String> by fxid()
    private val nameColumnMA: TableColumn<Armor, String> by fxid()
    private val protectionColumnMA: TableColumn<Armor, String> by fxid()
    private val damageResistColumnMA: TableColumn<Armor, String> by fxid()
    private val costColumnMA: TableColumn<Armor, String> by fxid()
    private val actionsColumnMA: TableColumn<Armor, Boolean> by fxid()

    private val legalityClassMbNMA: MenuButton by fxid()
    private val slotMbNMA: MenuButton by fxid()
    private val tlMbNMA: MenuButton by fxid()
    private val raceMbNMA: MenuButton by fxid()
    private val searchButtonNMA: Button by fxid()
    private val searchTextNMA: TextField by fxid()
    private val tableViewNMA: TableView<Any> by fxid()
    private val raceColumnNMA: TableColumn<Armor, String> by fxid()
    private val legalityClassColumnNMA: TableColumn<Armor, Int> by fxid()
    private val slotColumnNMA: TableColumn<Armor, String> by fxid()
    private val tlColumnNMA: TableColumn<Armor, String> by fxid()
    private val nameColumnNMA: TableColumn<Armor, String> by fxid()
    private val protectionColumnNMA: TableColumn<Armor, String> by fxid()
    private val damageResistColumnNMA: TableColumn<Armor, String> by fxid()
    private val costColumnNMA: TableColumn<Armor, String> by fxid()
    private val actionsColumnNMA: TableColumn<Armor, Boolean> by fxid()

    private val slotsHashMA: HashMap<String, String> = HashMap()
    private val slotsHashNMA: HashMap<String, String> = HashMap()
    private val racesHashNMA: HashMap<String, String> = HashMap()

    private val modelManArmor = ManArmor()
    private val modelNotManArmor = NotManArmor()
    var currentArmor: Armor = modelManArmor
    var currentCell: ActionsCell = ActionsCell()

    val lazyLoad by lazy {
        setCheckBox()
        localSearch()
        setManArmorTable()
        setNotManArmorTable()
        setItems(model = modelManArmor, tableView = tableViewMA, relationship = CharactersManArmor())
        setItems(model = modelNotManArmor, tableView = tableViewNMA, relationship = CharactersNotManArmor())
    }

    private fun setManArmorTable() {
        legalityClassColumnMA.cellValueFactory = PropertyValueFactory<Armor, Int>("legalityClass")
        slotColumnMA.cellValueFactory = PropertyValueFactory<Armor, String>("slot")
        nameColumnMA.cellValueFactory = PropertyValueFactory<Armor, String>("nameAndAddons")
        tlColumnMA.cellValueFactory = PropertyValueFactory<Armor, String>("tl")
        protectionColumnMA.cellValueFactory = PropertyValueFactory<Armor, String>("protectionAndAddons")
        damageResistColumnMA.cellValueFactory = PropertyValueFactory<Armor, String>("damageResistAndAddons")
        costColumnMA.cellValueFactory = PropertyValueFactory<Armor, String>("costAndAddons")
        actionsColumnMA.setCellValueFactory { SimpleBooleanProperty(true) }
        actionsColumnMA.setCellFactory { ActionsCell() }
    }

    private fun setNotManArmorTable() {
        raceColumnNMA.cellValueFactory = PropertyValueFactory<Armor, String>("race")
        legalityClassColumnNMA.cellValueFactory = PropertyValueFactory<Armor, Int>("legalityClass")
        slotColumnNMA.cellValueFactory = PropertyValueFactory<Armor, String>("slot")
        nameColumnNMA.cellValueFactory = PropertyValueFactory<Armor, String>("nameAndAddons")
        tlColumnNMA.cellValueFactory = PropertyValueFactory<Armor, String>("tl")
        protectionColumnNMA.cellValueFactory = PropertyValueFactory<Armor, String>("protectionAndAddons")
        damageResistColumnNMA.cellValueFactory = PropertyValueFactory<Armor, String>("damageResistAndAddons")
        costColumnNMA.cellValueFactory = PropertyValueFactory<Armor, String>("costAndAddons")
        actionsColumnNMA.setCellValueFactory { SimpleBooleanProperty(true) }
        actionsColumnNMA.setCellFactory { ActionsCell() }
    }

    private fun setCheckBox() {
        setSlotsMenu(model = modelManArmor, tableView = tableViewMA, mb = slotMbMA, hash = slotsHashMA)
        setSlotsMenu(model = modelNotManArmor, tableView = tableViewNMA, mb = slotMbNMA, hash = slotsHashNMA)
        TechnicalLevelMenu(model = modelManArmor, tableView = tableViewMA, tlMb = tlMbMA).set()
        TechnicalLevelMenu(model = modelNotManArmor, tableView = tableViewNMA, tlMb = tlMbNMA).set()
        LegalityClassMenu(model = modelManArmor, tableView = tableViewMA, mb = legalityClassMbMA).set()
        LegalityClassMenu(model = modelNotManArmor, tableView = tableViewNMA, mb = legalityClassMbNMA).set()
        setRaceMenu()
    }

    private fun setRaceMenu() {
        modelNotManArmor.pluck("race").distinct().forEach {
            val cm = CheckMenuItem(it)
            cm.isSelected = true
            cm.selectedProperty().addListener { _, _, value ->
                if (value) racesHashNMA[it] = it
                else racesHashNMA.remove(it)
                var query = "race in (${racesHashNMA.map { it.value }.joinToString { "'$it'" }})"
                if (racesHashNMA.isEmpty()) query = "race='-1'"
                tableViewNMA.items = modelNotManArmor.where(query)
            }
            racesHashNMA[it] = it
            raceMbNMA.items.add(cm)
        }

        raceMbNMA.items.sortBy { it.text }
    }

    private fun <T : Model> setSlotsMenu(model: T, tableView: TableView<Any>, mb: MenuButton,
                                         hash: HashMap<String, String>) {
        model.pluck("slot").distinct().forEach {
            val cm = CheckMenuItem(it)
            cm.isSelected = true
            cm.selectedProperty().addListener { _, _, value ->
                if (value) hash[it] = it
                else hash.remove(it)
                var query = "slot in (${hash.map { it.value }.joinToString { "'$it'" }})"
                if (hash.isEmpty()) query = "slot='-1'"
                tableView.items = model.where(query)
            }
            hash[it] = it
            mb.items.add(cm)
        }
        mb.items.sortBy { it.text }
    }

    private fun localSearch() {
        searchButtonMA.setOnAction { tableViewMA.setItems(LocalSearch(modelManArmor, searchTextMA).searchName()) }
        searchButtonNMA.setOnAction {
            tableViewNMA.setItems(LocalSearch(modelNotManArmor, searchTextNMA).searchName())
        }
    }

    private fun setItems(model: Model, tableView: TableView<Any>, relationship: Model) {
        val collection = model.all()
        val params: HashMap<String, Any> = HashMap()
        params["characterId"] = character.id
        (collection as ObservableList<Armor>).forEach {
            params["itemId"] = it.id
            val record = relationship.find_by(params) as CharacterInventory
            if (record.id > 0) {
                it.count = record.count
                it.add = true
                it.addons.forEach {
                    params["itemId"] = it.id
                    it.count = (CharactersArmorsAddon().find_by(params) as CharacterInventory).count
                }
            }
        }

        tableView.placeholder = Label(messages["equipments_not_found"])
        tableView.items = collection
    }

    private fun openFullInfo(cell: ActionsCell, armor: Armor) {
        currentArmor = armor
        currentCell = cell
        when (armor::class) {
            ManArmor::class -> find(ManArmorFragment::class).openModal(stageStyle = StageStyle.UTILITY)
            NotManArmor::class -> find(NotManArmorFragment::class).openModal(stageStyle = StageStyle.UTILITY)
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
        cell.reloadVBox()
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
        cell.reloadVBox()
        cell.tableRow.styleClass.remove("isAdd")
    }

    inner class ActionsCell : TableCell<Armor, Boolean>() {
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

        val finalCostLabel = label {
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
        }

        fun reloadVBox() {
            vBox.children.clear()
            vBox.children.add(hBox)
            rowItem.addons.forEach {
                if (rowItem.add) vBox.children.add(addonCountLabel(it))
                else vBox.children.add(addonCountTextField(it))
            }
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

        private fun addonCountTextField(addon: ArmorsAddon): TextField {
            return textfield(addon.count.toString()) {
                prefWidth = NodeAttributes.minWidth
                maxWidth = NodeAttributes.maxWidth
                alignment = Pos.CENTER
                textProperty().addListener { _, old, value ->
                    if (old == value || !Regex("-?\\d+").matches(value)) return@addListener
                    addon.count = value.toInt()
                    finalCostLabel.text = rowItem.getFinalCost()
                }
            }
        }

        private fun addonCountLabel(addon: ArmorsAddon): Label {
            return label(addon.count.toString()) {
                prefWidth = NodeAttributes.minWidth
                maxWidth = NodeAttributes.maxWidth
                alignment = Pos.CENTER
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

            reloadVBox()
            graphic = vBox
        }
    }
}
