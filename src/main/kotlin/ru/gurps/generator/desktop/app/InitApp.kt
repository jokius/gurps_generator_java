package ru.gurps.generator.desktop.app

import ru.gurps.generator.desktop.singletons.Property
import ru.gurps.generator.desktop.views.PreLoaderView
import tornadofx.App
import tornadofx.FX

class InitApp : App(PreLoaderView::class) {
    init {
        FX.messages = Property.locale
    }
}