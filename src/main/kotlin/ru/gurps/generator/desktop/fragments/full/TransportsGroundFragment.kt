package ru.gurps.generator.desktop.fragments.full

import javafx.scene.text.Text
import ru.gurps.generator.desktop.fragments.abstracts.TransportFragment
import ru.gurps.generator.desktop.models.rules.transports.TransportsGround
import tornadofx.*

class TransportsGroundFragment : TransportFragment() {
    override val transport = transportsView.currentTransport as TransportsGround

    init {
        setItemInfo()
        setChildren()
    }

    override fun setChildren() {
        fullDescription.children.addAll(unpowered(), road(), rails())
        super.setChildren()
    }

    private fun unpowered(): Text {
        val unpowered = Text("${messages["unpowered"]}: ${transport.getUnpowered()}\r\n")
        unpowered.addClass("unpowered")
        return unpowered
    }

    private fun road(): Text {
        val road = Text("${messages["road"]}: ${transport.getRoad()}\r\n")
        road.addClass("road")
        return road
    }

    private fun rails(): Text {
        val rails = Text("${messages["rails"]}: ${transport.getRails()}\r\n")
        rails.addClass("rails")
        return rails
    }
}
