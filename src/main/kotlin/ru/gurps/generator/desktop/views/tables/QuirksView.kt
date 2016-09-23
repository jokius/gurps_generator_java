package ru.gurps.generator.desktop.views.tables

import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.ObservableList
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.cell.TextFieldTableCell
import javafx.scene.layout.AnchorPane
import ru.gurps.generator.desktop.models.characters.CharactersQuirk
import ru.gurps.generator.desktop.models.rules.Quirk
import ru.gurps.generator.desktop.singletons.Characters
import ru.gurps.generator.desktop.views.TabsView
import tornadofx.*

class QuirksView : View() {
    override val root: AnchorPane by fxml("/templates/tables/QuirksView.fxml")
    val character = Characters.current!!
    val tabsView: TabsView by inject()

    private val tableView: TableView<Any> by fxid()
    private val nameColumn: TableColumn<Quirk, String> by fxid()
    private val costColumn: TableColumn<Quirk, String> by fxid()
    private val characterColumn: TableColumn<Quirk, Boolean> by fxid()
    private val dbColumn: TableColumn<Quirk, Boolean> by fxid()

    private val nameText: TextField by fxid()
    private val costText: TextField by fxid()
    private val addButton: Button by fxid()

    private val quirkModel = Quirk()

    val lazyLoad by lazy {
        setItems()
        setQuirks()

        nameText.textProperty().addListener { _, _, value ->
            addButton.isDisable = value.isNullOrBlank()
        }
    }

    private fun setItems() {
        val collection = quirkModel.all()
        (collection as ObservableList<Quirk>).forEach {
            character.quirks().forEach { characterQuirk ->
                if (it.id == characterQuirk.id) {
                    it.cost = characterQuirk.cost
                    it.add = true
                }
            }
        }

        tableView.placeholder = Label(messages["quirks_not_found"])
        tableView.isEditable = true
        tableView.items = collection
    }

    private fun setQuirks(){
        nameColumn.cellValueFactory = PropertyValueFactory<Quirk, String>("name")
        costColumn.cellValueFactory = PropertyValueFactory<Quirk, String>("costString")
        costColumn.setOnEditCommit {
            if (it.newValue == "0" || !Regex("\\d+").matches(it.newValue)) return@setOnEditCommit
            val quirk = it.tableView.items[it.tablePosition.row]
            if (quirk.cost != it.newValue.toInt()) quirk.cost = it.newValue.toInt()
        }
        costColumn.cellFactory = TextFieldTableCell.forTableColumn<Quirk>()

        characterColumn.cellValueFactory = PropertyValueFactory<Quirk, Boolean>("add")
        characterColumn.setCellFactory { QuirksUserButtonCell() }

        dbColumn.setCellValueFactory { SimpleBooleanProperty(true) }
        dbColumn.setCellFactory { QuirksDbButtonCell() }

    }


    fun addButton() {
        val name = nameText.text.capitalize()
        val quirk = Quirk(name).create() as Quirk
        quirk.cost = costText.text.toInt()
        quirk.add = true

        CharactersQuirk(characterId = character.id, itemId = quirk.id, cost = quirk.cost).create()
        if (quirk.cost != 0) tabsView.setCurrentPoints(character.currentPoints + quirk.cost)
        setItems()
        nameText.text = ""
        costText.text = "0"
        addButton.isDisable = true
    }

    inner class QuirksUserButtonCell: TableCell<Quirk, Boolean>() {
        val addButton = Button(messages["add"])
        val removeButton = Button(messages["remove"])


        init {
            addButton.setOnAction {
                CharactersQuirk(character.id, rowItem.id, rowItem.cost).create()
                rowItem.add = true
                tabsView.setCurrentPoints(character.currentPoints + rowItem.cost)
                graphic = removeButton
            }

            removeButton.setOnAction {
                CharactersQuirk().find_by("itemId", rowItem.id).destroy()
                rowItem.add = false
                tabsView.setCurrentPoints(character.currentPoints - rowItem.cost)
                graphic = addButton
            }
        }

        override fun updateItem(t: Boolean?, empty: Boolean) {
            super.updateItem(t, empty)
            if (empty) {
                graphic = null
                return
            }

            graphic = if (rowItem.add) removeButton else addButton
        }
    }

    inner class QuirksDbButtonCell: TableCell<Quirk, Boolean>() {
        val removeButton = Button(messages["remove"])

        init {
            removeButton.setOnAction {
                val charactersQuirk = CharactersQuirk().find_by("itemId", rowItem.id)
                if (charactersQuirk.id != -1){
                    charactersQuirk.destroy()
                    tabsView.setCurrentPoints(character.currentPoints - rowItem.cost)
                }
                rowItem.destroy()
                setItems()
            }
        }

        override fun updateItem(t: Boolean?, empty: Boolean) {
            super.updateItem(t, empty)
            if (empty) {
                graphic = null
                return
            }

            graphic = removeButton
        }
    }
}
