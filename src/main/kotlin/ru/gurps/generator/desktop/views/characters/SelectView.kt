package ru.gurps.generator.desktop.views.characters

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.scene.control.*
import javafx.scene.control.cell.PropertyValueFactory
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import javafx.stage.StageStyle
import ru.gurps.generator.desktop.controllers.NewItemsController
import ru.gurps.generator.desktop.fragments.HaveUpdatesFragment
import ru.gurps.generator.desktop.fragments.NotHaveUpdatesFragment
import ru.gurps.generator.desktop.models.Character
import ru.gurps.generator.desktop.singletons.Characters
import ru.gurps.generator.desktop.singletons.Property
import ru.gurps.generator.desktop.views.TabsView
import tornadofx.*
import java.awt.Desktop
import java.net.URI

class SelectView : View() {
    override val root: AnchorPane by fxml("/templates/characters/SelectView.fxml")
    private val newItemsController: NewItemsController by inject()
    private val charactersData = FXCollections.observableArrayList<Character>(Characters.all)!!
    var reload: Boolean = false

    private val newName: TextField by fxid()
    private val points: TextField by fxid()
    private val characterTable: TableView<Character> by fxid()
    private val name: TableColumn<Character, String> by fxid()
    private val tableCurrentPoints: TableColumn<Character, Int> by fxid()
    private val tableMaxPoints: TableColumn<Character, Int> by fxid()
    private val lastVersionLink: Hyperlink by fxid()
    val new: Button by fxid()
    val load: Button by fxid()
    val remove: Button by fxid()
    val selectViewChoiceBox: ChoiceBox<String> by fxid()

    init {
        title = messages["app_name_character_select"]
        linkShow()
        loadTableData()
        events()
        initSelectViewChoiceBox()
    }

    override fun onDock() {
        stageParams()
        if (!reload) return
        linkShow()
        reloadData()
        loadTableData()
    }

    private fun stageParams() {
        primaryStage.isResizable = false
        primaryStage.sizeToScene()
    }

    private fun loadTableData() {
        characterTable.placeholder = Label(messages["characters_not_found"])
        name.cellValueFactory = PropertyValueFactory("name")
        tableMaxPoints.cellValueFactory = PropertyValueFactory("maxPoints")
        tableCurrentPoints.cellValueFactory = PropertyValueFactory("currentPoints")
        tableCurrentPoints.setCellFactory { CellColor() }

        characterTable.setRowFactory {
            val row = TableRow<Character>()
            row.setOnMouseClicked {
                if (row.isEmpty) return@setOnMouseClicked
                Characters.current = charactersData[row.index]
                if (it.clickCount == 1) {
                    load.isDisable = false
                    remove.isDisable = false
                } else {
                    replaceWith(TabsView::class, ViewTransition.Slide(0.001.seconds))
                }
            }
            row
        }

        characterTable.items = charactersData
    }

    private fun initSelectViewChoiceBox() {
        selectViewChoiceBox.items.addAll(Property.selectViewHash.values)
        selectViewChoiceBox.value = Property.selectView
    }

    private fun events() {
        newName.textProperty().addListener { _, _, value ->
            new.isDisable = value == "" || !Regex("\\d+").matches(value)
        }

        points.textProperty().addListener { _, _, value ->
            new.isDisable = value == "" || !Regex("\\d+").matches(value)
        }
    }

    fun new() {
        Characters.current = Character(newName.text, points.text.toInt()).create() as Character
        replaceWith(TabsView::class, ViewTransition.Slide(0.001.seconds))
    }

    fun load() {
        replaceWith(TabsView::class, ViewTransition.Slide(0.001.seconds))
    }

    fun remove() {
        charactersData.remove(Characters.current)
        Characters.current?.destroy()
        characterTable.refresh()
        load.isDisable = true
        remove.isDisable = true
    }

    fun generate() {
        Characters.current = Character()
        replaceWith(GenerateView::class, ViewTransition.Slide(0.001.seconds))
    }

    fun checkUpdateParams() {
        val fragment: Fragment = if(newItemsController.haveUpdates())
            find(HaveUpdatesFragment::class)
        else
            find(NotHaveUpdatesFragment::class)
        fragment.openModal(stageStyle = StageStyle.UTILITY)
    }

    private fun linkShow() {
        if (Property.isLastVersion!!) return
        AnchorPane.setTopAnchor(characterTable, 109.0)
        lastVersionLink.isVisible = true
        lastVersionLink.setOnAction { Desktop.getDesktop().browse(URI.create(Property.newVersionUrl)) }
    }

    private fun reloadData() {
        Characters.all = Character().all() as ObservableList<Character>
        charactersData.clear()
        charactersData.addAll(Characters.all)
    }

    internal inner class CellColor: TableCell<Character, Int>() {
        override fun updateItem(item: Int?, empty: Boolean) {
            super.updateItem(item, empty)
            if (empty || item == null || tableRow.item == null) {
                graphic = null
                return
            }

            val character: Character = tableRow.item as Character
            val label = label(item.toString()) {
                style {
                    textFill = if (character.maxPoints >= item) Color.GREEN
                    else Color.RED
                }
            }
            graphic = label
        }
    }
}