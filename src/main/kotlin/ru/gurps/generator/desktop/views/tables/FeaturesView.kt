package ru.gurps.generator.desktop.views.tables

import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.ObservableList
import javafx.geometry.Pos
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.input.KeyCode
import javafx.stage.StageStyle
import ru.gurps.generator.desktop.fragments.full.FeatureFragment
import ru.gurps.generator.desktop.models.characters.CharactersAddon
import ru.gurps.generator.desktop.models.characters.CharactersFeature
import ru.gurps.generator.desktop.models.rules.Feature
import ru.gurps.generator.desktop.models.rules.Modifier
import ru.gurps.generator.desktop.singletons.Characters
import ru.gurps.generator.desktop.singletons.FeatureTypes
import ru.gurps.generator.desktop.singletons.NodeAttributes
import ru.gurps.generator.desktop.views.TabsView
import tornadofx.*
import java.util.*

class FeaturesView : View() {
    override val root: Accordion by fxml("/templates/tables/FeaturesView.fxml")
    val tabsView: TabsView by inject()
    private val modifiersView: ModifiersView by inject()
    private val character = Characters.current!!

    private val tableViewA: TableView<Any> by fxid()
    private val nameColumnA: TableColumn<Feature, String> by fxid()
    private val nameEnColumnA: TableColumn<Feature, String> by fxid()
    private val featureTypeColumnA: TableColumn<Feature, String> by fxid()
    private val costColumnA: TableColumn<Feature, String> by fxid()
    private val actionsColumnA: TableColumn<Feature, Boolean> by fxid()

    private val tableViewD: TableView<Any> by fxid()
    private val nameColumnD: TableColumn<Feature, String> by fxid()
    private val nameEnColumnD: TableColumn<Feature, String> by fxid()
    private val featureTypeColumnD: TableColumn<Feature, String> by fxid()
    private val costColumnD: TableColumn<Feature, String> by fxid()
    private val actionsColumnD: TableColumn<Feature, Boolean> by fxid()

    private val featuresMbA: MenuButton by fxid()
    private val featuresMbD: MenuButton by fxid()

    private val searchButtonA: MenuButton by fxid()
    private val searchAllA: MenuItem by fxid()
    private val searchNameA: MenuItem by fxid()
    private val searchNameEnA: MenuItem by fxid()
    private val searchCostA: MenuItem by fxid()
    private val searchDescriptionA: MenuItem by fxid()
    private val resetA: MenuItem by fxid()
    private val searchTextA: TextField by fxid()

    private val searchButtonD: MenuButton by fxid()
    private val searchAllD: MenuItem by fxid()
    private val searchNameD: MenuItem by fxid()
    private val searchNameEnD: MenuItem by fxid()
    private val searchCostD: MenuItem by fxid()
    private val searchDescriptionD: MenuItem by fxid()
    private val resetD: MenuItem by fxid()
    private val searchTextD: TextField by fxid()

    private var featuresHA: HashMap<Int, String> = HashMap()
    private var featuresHD: HashMap<Int, String> = HashMap()
    var currentFeature = Feature()
    var currentCell: ActionsCell = ActionsCell()
    private val advantageModel = Feature(true)
    private val disadvantageModel = Feature(false)
    val lazyLoad by lazy {
        setItems(advantageModel, tableViewA)
        setItems(disadvantageModel, tableViewD)
        setAdvantages()
        setDisadvantages()
        localSearch(advantageModel, tableViewA, searchButtonA, searchAllA, searchNameA, searchNameEnA, searchCostA,
                searchDescriptionA, resetA, searchTextA)
        localSearch(disadvantageModel, tableViewD, searchButtonD, searchAllD, searchNameD, searchNameEnD, searchCostD,
                searchDescriptionD, resetD, searchTextD)
        checkBoxes(advantageModel, tableViewA, featuresHA, featuresMbA)
        checkBoxes(disadvantageModel, tableViewD, featuresHD, featuresMbD)
    }

    private fun setAdvantages() {
        nameColumnA.cellValueFactory = PropertyValueFactory<Feature, String>("name")
        nameEnColumnA.cellValueFactory = PropertyValueFactory<Feature, String>("nameEn")
        featureTypeColumnA.cellValueFactory = PropertyValueFactory<Feature, String>("typeShort")
        costColumnA.cellValueFactory = PropertyValueFactory<Feature, String>("cost")
        actionsColumnA.setCellValueFactory { SimpleBooleanProperty(true) }
        actionsColumnA.setCellFactory { ActionsCell() }
    }

    private fun setDisadvantages() {
        nameColumnD.cellValueFactory = PropertyValueFactory<Feature, String>("name")
        nameEnColumnD.cellValueFactory = PropertyValueFactory<Feature, String>("nameEn")
        featureTypeColumnD.cellValueFactory = PropertyValueFactory<Feature, String>("typeShort")
        costColumnD.cellValueFactory = PropertyValueFactory<Feature, String>("cost")
        actionsColumnD.setCellValueFactory { SimpleBooleanProperty(true) }
        actionsColumnD.setCellFactory { ActionsCell() }
    }


    private fun setItems(featureModel: Feature, tableView: TableView<Any>) {
        val collection = featureModel.where("advantage", featureModel.advantage)
        val params: HashMap<String, Any> = HashMap()
        params["characterId"] = character.id
        (collection as ObservableList<Feature>).forEach {
            params.put("itemId", it.id)
            val charactersFeature = CharactersFeature().find_by(params) as CharactersFeature
            if (charactersFeature.id > 0) {
                it.currentCost = charactersFeature.cost
                it.level = charactersFeature.level
                it.add = true
            }
        }

        if (featureModel.advantage) tableView.setPlaceholder(Label(messages["advantages_not_found"]))
        else tableView.setPlaceholder(Label(messages["disadvantages_not_found"]))

        tableView.items = collection
    }

    private fun localSearch(featureModel: Feature, tableView: TableView<Any>, searchButton: MenuButton,
                            searchAll: MenuItem, searchName: MenuItem, searchNameEn: MenuItem, searchCost: MenuItem,
                            searchDescription: MenuItem, reset: MenuItem, searchText: TextField) {
        searchText.textProperty().addListener { _, _, value ->
            if (value.isNullOrBlank()) {
                searchButton.isDisable = true
                tableView.setItems(featureModel.where("advantage", featureModel.advantage))
            } else searchButton.isDisable = false
        }

        searchAll.setOnAction {
            val query = "advantage=${featureModel.advantage} and " +
                    "(UPPER(name) like UPPER('%${searchText.text}%') or " +
                    "UPPER(nameEn) like UPPER('%${searchText.text}%') or " +
                    "UPPER(cost) like UPPER('%${searchText.text}%') or " +
                    "UPPER(description) like UPPER('%${searchText.text}%'))"
            tableView.setItems(featureModel.where(query))
        }

        searchName.setOnAction {
            val query = "advantage=${featureModel.advantage} and UPPER(name) like UPPER('%${searchText.text}%')"
            tableView.setItems(Feature().where(query))
        }

        searchNameEn.setOnAction {
            val query = "advantage=${featureModel.advantage} and UPPER(nameEn) like UPPER('%${searchText.text}%')"
            tableView.setItems(Feature().where(query))
        }

        searchCost.setOnAction {
            val query = "advantage=${featureModel.advantage} and UPPER(cost) like UPPER('%${searchText.text}%')"
            tableView.setItems(Feature().where(query))
        }

        searchDescription.setOnAction {
            val query = "advantage=${featureModel.advantage} and UPPER(description) like UPPER('%${searchText.text}%')"
            tableView.setItems(Feature().where(query))
        }

        reset.setOnAction {
            searchText.text = ""
            searchButton.isDisable = true
            tableView.setItems(Feature().where("advantage", featureModel.advantage))
        }
    }

    private fun checkBoxes(featureModel: Feature, tableView: TableView<Any>, featuresH: HashMap<Int, String>,
                           featuresMb: MenuButton) {
        FeatureTypes.list.forEachIndexed { i, name ->
            val cm = CheckMenuItem(name)
            cm.isSelected = true
            cm.selectedProperty().addListener { _, _, value ->
                if (value) featuresH[i] = name
                else featuresH.remove(i)

                var query = "advantage='${featureModel.advantage}' and featureType like "
                featuresH.forEach {
                    if (query == "advantage='${featureModel.advantage}' and featureType like ")
                        query += "'%${it.key}%'"
                    else
                        query += " or advantage=${featureModel.advantage} and featureType like '%${it.key}%'"
                }

                if (query == "advantage=${featureModel.advantage} and featureType like ")
                    query = "featureType='-1'"

                tableView.setItems(Feature().where(query))
            }

            featuresH[i] = name
            featuresMb.items.add(cm)
        }
    }

    private fun openFullInfo(cell: ActionsCell, feature: Feature) {
        currentFeature = feature
        currentCell = cell
        find(FeatureFragment::class).openModal(stageStyle = StageStyle.UTILITY)
    }

    fun callAdd(cell: ActionsCell) {
        val feature = cell.rowItem
        CharactersFeature(character.id, feature.id, feature.currentCost, feature.level, feature.advantage).create()
        tabsView.setCurrentPoints(character.currentPoints + feature.currentCost)
        feature.add = true
        cell.addButton.isVisible = false
        cell.removeButton.isVisible = true
        cell.tableRow.styleClass.add("isAdd")
        modifiersView.tableView.refresh()
    }

    fun callRemove(cell: ActionsCell) {
        val feature = cell.rowItem
        val params = HashMap<String, Any>()
        params.put("characterId", character.id)
        params.put("itemId", feature.id)
        var resultCost = feature.currentCost
        feature.modifiers(character.id).forEach {
            resultCost += feature.modifierCost(it)
        }

        tabsView.setCurrentPoints(character.currentPoints - resultCost)
        if (feature.addons.any()) CharactersAddon().destroy_all(CharactersAddon().where(params))
        CharactersFeature().find_by(params).destroy()
        feature.add = false
        cell.addButton.isVisible = feature.maxLevel == 1 && feature.cost != 0 && feature.addons.isEmpty()
        cell.removeButton.isVisible = false
        cell.tableRow.styleClass.remove("isAdd")
        modifiersView.tableView.items.filter { ((it as Modifier).feature.id == feature.id) }
                .forEach { (it as Modifier).add = false }
        modifiersView.tableView.refresh()
    }

    inner class ActionsCell : TableCell<Feature, Boolean>() {
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

        val paddedButton = stackpane {
            children.addAll(addButton, removeButton)
        }

        val hBox = hbox {
            alignment = Pos.CENTER
            spacing = 10.0
            children.addAll(paddedButton, fullButton)
        }

        private fun setRow() {
            if (rowItem.add) {
                removeButton.isVisible = true
                tableRow.styleClass.add("isAdd")
            } else {
                addButton.isVisible = rowItem.maxLevel == 1 && rowItem.cost != 0 && rowItem.addons.isEmpty()
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
