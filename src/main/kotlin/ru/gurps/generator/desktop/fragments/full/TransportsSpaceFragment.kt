package ru.gurps.generator.desktop.fragments.full

import ru.gurps.generator.desktop.fragments.abstracts.TransportFragment
import ru.gurps.generator.desktop.models.rules.transports.TransportsSpace

class TransportsSpaceFragment : TransportFragment() {
    override val transport = transportsView.currentTransport as TransportsSpace

    init {
        setItemInfo()
        setChildren()
    }
}
