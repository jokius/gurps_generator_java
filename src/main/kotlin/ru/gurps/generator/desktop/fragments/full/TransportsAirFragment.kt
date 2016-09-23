package ru.gurps.generator.desktop.fragments.full

import javafx.scene.text.Text
import ru.gurps.generator.desktop.fragments.abstracts.TransportFragment
import ru.gurps.generator.desktop.models.rules.transports.TransportsAir
import tornadofx.*

class TransportsAirFragment : TransportFragment() {
    override val transport = transportsView.currentTransport as TransportsAir

    init {
        setItemInfo()
        setChildren()
    }

    override fun setChildren() {
        fullDescription.children.add(stall())
        super.setChildren()
    }

    private fun stall(): Text {
        val stall = Text("${messages["stall"]}: ${transport.stall}\r\n")
        stall.addClass("stall")
        return stall
    }
}
