package ru.gurps.generator.desktop.singletons

object Money {
    init { println("This Money change is load") }

    fun toDollars(cents: Long): String = "$%.2f".format(cents.toDouble() / 100.0)
}