package ru.gurps.generator.desktop.views

import javafx.collections.ObservableList
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle
import ru.gurps.generator.desktop.controllers.NewVersionController
import ru.gurps.generator.desktop.lib.ExportDb
import ru.gurps.generator.desktop.models.Character
import ru.gurps.generator.desktop.singletons.Characters
import ru.gurps.generator.desktop.singletons.Property
import ru.gurps.generator.desktop.views.characters.SelectView
import tornadofx.*

class PreLoaderView: View() {
    override val root = AnchorPane()
    private val newVersionController: NewVersionController by inject()
    private val mainStage = Stage()

    init {
        with(root) {
            this += ImageView(resources["/images/gurps.png"])
            primaryStage.initStyle(StageStyle.TRANSPARENT)
            primaryStage.setOnShowing { root.scene.fill = Color.TRANSPARENT }
        }

        runAsync {
            ExportDb()
            Characters.all = Character().all() as ObservableList<Character>
        } ui {
            mainStage.scene = primaryStage.scene
            primaryStage.close()
            FX.setPrimaryStage(scope = DefaultScope, stage = mainStage)

            Property.isLastVersion = newVersionController.isLastVersion()
            replaceWith(SelectView::class, ViewTransition.Slide(0.001.seconds))
            primaryStage.show()
        }
    }
}
