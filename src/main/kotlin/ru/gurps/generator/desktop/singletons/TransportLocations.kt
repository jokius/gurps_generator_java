package ru.gurps.generator.desktop.singletons

import tornadofx.*
import tornadofx.FX.Companion.messages

object TransportLocations {
    init { println("This Transport Locations Singleton is load") }
    val list = mapOf(
            Pair("A", messages["arm"]),
            Pair("C", messages["caterpillar_tracks"]),
            Pair("D", messages["draft_animals"]),
            Pair("E", messages["exposed_rider"]),
            Pair("G", messages["large_glass_windows"]),
            Pair("g", messages["small_glass_windows"]),
            Pair("H", messages["helicopter_rotors"]),
            Pair("L", messages["leg"]),
            Pair("M", messages["mast_and_rigging"]),
            Pair("O", messages["open_cabin"]),
            Pair("R", messages["runners_or_skids"]),
            Pair("r", messages["retractable"]),
            Pair("S", messages["large_superstructure_or_gondola"]),
            Pair("s", messages["small_superstructure"]),
            Pair("T", messages["main_turret"]),
            Pair("t", messages["independent_turret"]),
            Pair("Wi", messages["pair_of_wings"]),
            Pair("W", messages["wheel"]),
            Pair("X", messages["exposed_weapon_mount"])
    )

    fun replace(locations: String): String {
        var full = locations
        list.forEach {
            full = full.replace(it.key, " ${it.value}")
        }

        return full
    }
}
