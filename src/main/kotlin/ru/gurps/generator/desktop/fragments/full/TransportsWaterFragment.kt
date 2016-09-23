package ru.gurps.generator.desktop.fragments.full

import javafx.scene.text.Text
import ru.gurps.generator.desktop.fragments.abstracts.TransportFragment
import ru.gurps.generator.desktop.models.rules.transports.TransportsWater
import ru.gurps.generator.desktop.views.tables.TransportsView
import tornadofx.*

class TransportsWaterFragment : TransportFragment() {
    override val transport = transportsView.currentTransport as TransportsWater

    init {
        setItemInfo()
        setChildren()
    }

    override fun setChildren() {
        fullDescription.children.addAll(unpowered(), draft())
        super.setChildren()
    }

    private fun unpowered(): Text {
        val unpowered = Text("${messages["unpowered"]}: ${transport.getUnpowered()}\r\n")
        unpowered.addClass("unpowered")
        return unpowered
    }

    private fun draft(): Text {
        val draft = Text("${messages["draft"]}: ${transport.draft}\r\n")
        draft.addClass("draft")
        return draft
    }
}
