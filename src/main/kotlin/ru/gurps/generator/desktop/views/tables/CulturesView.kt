package ru.gurps.generator.desktop.views.tables

import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.ObservableList
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.cell.TextFieldTableCell
import javafx.scene.layout.AnchorPane
import ru.gurps.generator.desktop.models.characters.CharactersCultura
import ru.gurps.generator.desktop.models.rules.Cultura
import ru.gurps.generator.desktop.singletons.Characters
import ru.gurps.generator.desktop.views.TabsView
import tornadofx.*

class CulturesView : View() {
    override val root: AnchorPane by fxml("/templates/tables/CulturesView.fxml")
    val character = Characters.current!!
    val tabsView: TabsView by inject()

    private val tableView: TableView<Cultura> by fxid()
    private val nameColumn: TableColumn<Cultura, String> by fxid()
    private val costColumn: TableColumn<Cultura, String> by fxid()
    private val characterColumn: TableColumn<Cultura, Boolean> by fxid()
    private val dbColumn: TableColumn<Cultura, Boolean> by fxid()

    private val nameText: TextField by fxid()
    private val costText: TextField by fxid()
    private val addButton: Button by fxid()
    
    /* May be returned later
    val updateFromServer: Button by fxid()
    val sedToServer: Button by fxid()
    */

    private var cultura: Cultura = Cultura()

    val lazyLoad by lazy {
        nameColumn.cellValueFactory = PropertyValueFactory<Cultura, String>("name")

        costColumn.cellValueFactory = PropertyValueFactory<Cultura, String>("cost")
        costColumn.setOnEditCommit {
            if (it.newValue == "0" || !Regex("\\d+").matches(it.newValue)) return@setOnEditCommit
            val cultura = it.rowValue
            if (cultura.cost != it.newValue.toInt()) cultura.cost = it.newValue.toInt()
        }
        costColumn.cellFactory = TextFieldTableCell.forTableColumn<Cultura>()

        characterColumn.cellValueFactory = PropertyValueFactory<Cultura, Boolean>("add")
        characterColumn.setCellFactory { CulturesCharacterButtonCell() }

        dbColumn.setCellValueFactory { SimpleBooleanProperty(true) }
        dbColumn.setCellFactory { CulturesDbButtonCell() }
        tableView.isEditable = true

        nameText.textProperty().addListener { _, _, value ->
            if (value == "") return@addListener
            addButton.isDisable = false
        }

        addButton.setOnAction {
            val name = nameText.text.substring(0, 1).toUpperCase() + nameText.text.substring(1)
            cultura = Cultura(name).create() as Cultura
            cultura.cost = costText.text.toInt()
            cultura.add = true

            CharactersCultura(character.id, cultura.id, cultura.cost).create()
            if (cultura.cost != 0) tabsView.setCurrentPoints(character.currentPoints + cultura.cost)
            setCultures()
            nameText.text = ""
            addButton.isDisable = true
//            sedToServer.setDisable(false)
        }

//        updateFromServer.setOnAction({ updateFromServer() })
//        sedToServer.setOnAction({ sedToServer(cultura.name) })

        setCultures()
        tableView.placeholder = Label(messages["cultures_not_found"])
    }

    private fun setCultures() {
        val cultures = Cultura().all() as ObservableList<Cultura>
        cultures.forEach {
            character.cultures().forEach { characterCultura ->
                if (it.id == characterCultura.id) {
                    it.cost = characterCultura.cost
                    it.add = true
                }
            }
        }

        tableView.items = cultures
    }

    private inner class CulturesCharacterButtonCell internal constructor() : TableCell<Cultura, Boolean>() {
        internal var addButton = Button(messages["add"])
        internal var removeButton = Button(messages["remove"])

        init {
            addButton.setOnAction {
                val cultura = tableRow.item as Cultura
                CharactersCultura(character.id, cultura.id, cultura.cost).create()
                cultura.add = true
                tabsView.setCurrentPoints(character.currentPoints + cultura.cost)
                graphic = removeButton
            }

            removeButton.setOnAction {
                val cultura = tableRow.item as Cultura
                CharactersCultura().find_by("itemId", cultura.id).destroy()
                cultura.add = false
                tabsView.setCurrentPoints(character.currentPoints - cultura.cost)
                graphic = addButton
            }
        }

        override fun updateItem(t: Boolean?, empty: Boolean) {
            super.updateItem(t, empty)
            if (empty) {
                graphic = null
                return
            }

            cultura = rowItem
            graphic = if (cultura.add) removeButton else addButton
        }
    }

    private inner class CulturesDbButtonCell internal constructor() : TableCell<Cultura, Boolean>() {
        internal var removeButton = Button(messages["remove"])

        init {
            removeButton.setOnAction {
                val cultura = tableRow.item as Cultura
                CharactersCultura().destroy_all(CharactersCultura().where("itemId", cultura.id))
                cultura.destroy()
                setCultures()
            }
        }

        override fun updateItem(t: Boolean?, empty: Boolean) {
            super.updateItem(t, empty)
            if (empty) {
                graphic = null
                return
            }

            cultura = tableRow.item as Cultura
            graphic = removeButton
        }
    }
}
