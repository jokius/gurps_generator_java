package ru.gurps.generator.desktop.views.tables

import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.ObservableList
import javafx.scene.control.*
import javafx.scene.control.cell.ComboBoxTableCell
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.control.cell.TextFieldTableCell
import javafx.scene.layout.AnchorPane
import ru.gurps.generator.desktop.models.characters.CharactersLanguage
import ru.gurps.generator.desktop.models.rules.Language
import ru.gurps.generator.desktop.singletons.Characters
import ru.gurps.generator.desktop.singletons.LanguagesValues
import ru.gurps.generator.desktop.views.TabsView
import tornadofx.*

class LanguagesView : View() {
    override val root: AnchorPane by fxml("/templates/tables/LanguagesView.fxml")
    val character = Characters.current!!
    val tabsView: TabsView by inject()

    private val tableView: TableView<Language> by fxid()
    private val nameColumn: TableColumn<Language, String> by fxid()
    private val spokenColumn: TableColumn<Language, String> by fxid()
    private val writtenColumn: TableColumn<Language, String> by fxid()
    private val costColumn: TableColumn<Language, String> by fxid()
    private val characterColumn: TableColumn<Language, Boolean> by fxid()
    private val dbColumn: TableColumn<Language, Boolean> by fxid()

    private val nameText: TextField by fxid()
    private val spokenChoiceBox: ChoiceBox<String> by fxid()
    private val writtenChoiceBox: ChoiceBox<String> by fxid()
    private val costText: TextField by fxid()
    private val addButton: Button by fxid()

    /* May be returned later
    val updateFromServer: Button by fxid()
    val sedToServer: Button by fxid()
    */

    private var language: Language = Language()

    val lazyLoad by lazy {
        nameColumn.cellValueFactory = PropertyValueFactory<Language, String>("name")

        spokenColumn.cellValueFactory = PropertyValueFactory<Language, String>("spoken")
        spokenColumn.cellFactory = ComboBoxTableCell.forTableColumn<Language, String>(LanguagesValues.spoken)
        spokenColumn.setOnEditCommit {
            val language = it.rowValue
            if (language.getSpoken() != it.newValue) language.setSpoken(it.newValue)
        }

        writtenColumn.cellValueFactory = PropertyValueFactory<Language, String>("written")
        writtenColumn.cellFactory = ComboBoxTableCell.forTableColumn<Language, String>(LanguagesValues.written)
        writtenColumn.setOnEditCommit {
            val language = it.rowValue
            if (language.getWritten() != it.newValue) language.setWritten(it.newValue)
        }

        costColumn.cellValueFactory = PropertyValueFactory<Language, String>("cost")
        costColumn.setOnEditCommit {
            if (it.newValue == "0" || !Regex("\\d+").matches(it.newValue)) return@setOnEditCommit
            val language = it.rowValue
            if (language.cost != it.newValue.toInt()) language.cost = it.newValue.toInt()
        }
        costColumn.cellFactory = TextFieldTableCell.forTableColumn<Language>()

        characterColumn.cellValueFactory = PropertyValueFactory<Language, Boolean>("add")
        characterColumn.setCellFactory { LanguagesCharacterButtonCell() }

        dbColumn.setCellValueFactory { SimpleBooleanProperty(true) }
        dbColumn.setCellFactory { LanguagesDbButtonCell() }
        tableView.isEditable = true

        nameText.textProperty().addListener { _, _, value ->
            if (value == "") return@addListener
            addButton.isDisable = false
        }

        spokenChoiceBox.items = LanguagesValues.spoken
        spokenChoiceBox.selectionModel.selectFirst()
        writtenChoiceBox.items = LanguagesValues.written
        writtenChoiceBox.selectionModel.selectFirst()
        addButton.setOnAction {
            val name = nameText.text.capitalize()
            language = Language(name).create() as Language
            language.spoken = spokenChoiceBox.selectionModel.selectedIndex
            language.written = writtenChoiceBox.selectionModel.selectedIndex
            language.cost = costText.text.toInt()
            language.add = true

            CharactersLanguage(character.id, language.id, language.spoken, language.written, language.cost).create()
            if (language.cost != 0) tabsView.setCurrentPoints(character.currentPoints + language.cost)
            setLanguages()
            nameText.text = ""
            addButton.isDisable = true
//            sedToServer.setDisable(false)
        }

//        updateFromServer.setOnAction({ updateFromServer() })
//        sedToServer.setOnAction({ sedToServer(language.name) })

        setLanguages()
        tableView.placeholder = Label(messages["languages_not_found"])
    }


    private fun setLanguages() {
        val languages = Language().all() as ObservableList<Language>
        languages.forEach {
            character.languages().forEach { characterLanguage ->
                if (it.id == characterLanguage.id) {
                    it.written = characterLanguage.written
                    it.spoken = characterLanguage.spoken
                    it.cost = characterLanguage.cost
                    it.add = true
                }
            }
        }

        tableView.items = languages
    }

    private inner class LanguagesCharacterButtonCell internal constructor() : TableCell<Language, Boolean>() {
        internal var addButton = Button(messages["add"])
        internal var removeButton = Button(messages["remove"])

        init {
            addButton.setOnAction {
                val language = tableRow.item as Language
                CharactersLanguage(character.id, language.id, language.spoken, language.written, language.cost).create()
                language.add = true
                tabsView.setCurrentPoints(character.currentPoints + language.cost)
                graphic = removeButton
            }

            removeButton.setOnAction {
                val language = tableRow.item as Language
                CharactersLanguage().find_by("itemId", language.id).destroy()
                language.add = false
                tabsView.setCurrentPoints(character.currentPoints - language.cost)
                graphic = addButton
            }
        }

        override fun updateItem(t: Boolean?, empty: Boolean) {
            super.updateItem(t, empty)
            if (empty) {
                graphic = null
                return
            }

            language = tableRow.item as Language
            graphic = if (language.add) removeButton else addButton
        }
    }

    private inner class LanguagesDbButtonCell internal constructor() : TableCell<Language, Boolean>() {
        internal var removeButton = Button(messages["remove"])

        init {
            removeButton.setOnAction {
                val language = tableRow.item as Language
                CharactersLanguage().destroy_all(CharactersLanguage().where("itemId", language.id))
                language.destroy()
                setLanguages()
            }
        }

        override fun updateItem(t: Boolean?, empty: Boolean) {
            super.updateItem(t, empty)
            if (empty) {
                graphic = null
                return
            }

            language = tableRow.item as Language
            graphic = removeButton
        }
    }
}
