package ru.gurps.generator.desktop.singletons

object Dmg {
    fun thrust(value: Int): String {
        var st = value
        var dmg = "0"
        if (st == 1 || st == 2) {
            dmg = "1d - 6"
        } else if (st == 3 || st == 4) {
            dmg = "1d - 5"
        } else if (st == 5 || st == 6) {
            dmg = "1d - 4"
        } else if (st == 7 || st == 8) {
            dmg = "1d - 3"
        } else if (st == 9 || st == 10) {
            dmg = "1d - 2"
        } else if (st == 11 || st == 12) {
            dmg = "1d - 1"
        } else if (st == 13 || st == 14) {
            dmg = "1d"
        } else if (st == 15 || st == 16) {
            dmg = "1d + 1"
        } else if (st == 17 || st == 18) {
            dmg = "1d + 2"
        } else if (st == 19 || st == 20) {
            dmg = "2d - 1"
        } else if (st == 21 || st == 22) {
            dmg = "2d"
        } else if (st == 23 || st == 24) {
            dmg = "2d + 1"
        } else if (st == 25 || st == 26) {
            dmg = "2d + 2"
        } else if (st == 27 || st == 28) {
            dmg = "3d - 1"
        } else if (st == 29 || st == 30) {
            dmg = "3d"
        } else if (st == 31 || st == 32) {
            dmg = "3d + 1"
        } else if (st == 33 || st == 34) {
            dmg = "3d + 2"
        } else if (st == 35 || st == 36) {
            dmg = "4d - 1"
        } else if (st == 37 || st == 38) {
            dmg = "4d"
        } else if (st in 39..44) {
            dmg = "4d + 1"
        } else if (st in 45..49) {
            dmg = "5d"
        } else if (st in 50..54) {
            dmg = "5d + 2"
        } else if (st in 55..59) {
            dmg = "6d"
        } else if (st in 60..64) {
            dmg = "7d - 1"
        } else if (st in 65..69) {
            dmg = "7d + 1"
        } else if (st in 70..74) {
            dmg = "8d"
        } else if (st in 75..79) {
            dmg = "8d + 2"
        } else if (st in 80..84) {
            dmg = "9d"
        } else if (st in 85..89) {
            dmg = "9d + 2"
        } else if (st in 90..94) {
            dmg = "10d"
        } else if (st in 95..99) {
            dmg = "10d + 2"
        } else if (st in 100..109) {
            dmg = "11d"
        } else if (st >= 110) {
            var d = 0
            while (st > 109) {
                st -= 10
                d++
            }

            dmg = (11 + d).toString() + "d"
        }
        return dmg
    }

    fun swing(value: Int): String {
        var st = value
        var dmg = "0"
        if (st == 1 || st == 2) {
            dmg = "1d - 5"
        } else if (st == 3 || st == 4) {
            dmg = "1d - 4"
        } else if (st == 5 || st == 6) {
            dmg = "1d - 3"
        } else if (st == 7 || st == 8) {
            dmg = "1d - 2"
        } else if (st == 9) {
            dmg = "1d - 1"
        } else if (st == 10) {
            dmg = "1d"
        } else if (st == 11) {
            dmg = "1d + 1"
        } else if (st == 12) {
            dmg = "1d + 2"
        } else if (st == 13) {
            dmg = "2d - 1"
        } else if (st == 14) {
            dmg = "2d"
        } else if (st == 15) {
            dmg = "2d + 1"
        } else if (st == 16) {
            dmg = "2d + 2"
        } else if (st == 17) {
            dmg = "3d - 1"
        } else if (st == 18) {
            dmg = "3d"
        } else if (st == 19) {
            dmg = "3d + 1"
        } else if (st == 20) {
            dmg = "3d + 2"
        } else if (st == 21) {
            dmg = "4d - 1"
        } else if (st == 22) {
            dmg = "4d"
        } else if (st == 23) {
            dmg = "4d + 1"
        } else if (st == 24) {
            dmg = "4d + 2"
        } else if (st == 25) {
            dmg = "5d - 1"
        } else if (st == 26) {
            dmg = "5d"
        } else if (st == 27 || st == 28) {
            dmg = "5d + 1"
        } else if (st == 29 || st == 30) {
            dmg = "5d + 2"
        } else if (st == 31 || st == 32) {
            dmg = "6d - 1"
        } else if (st == 33 || st == 34) {
            dmg = "6d"
        } else if (st == 35 || st == 36) {
            dmg = "6d + 1"
        } else if (st == 37 || st == 38) {
            dmg = "6d + 2"
        } else if (st in 39..49) {
            dmg = "7d - 1"
        } else if (st in 50..54) {
            dmg = "8d - 1"
        } else if (st in 55..59) {
            dmg = "8d + 1"
        } else if (st in 60..64) {
            dmg = "9d"
        } else if (st in 65..69) {
            dmg = "9d + 2"
        } else if (st in 70..74) {
            dmg = "10d"
        } else if (st in 75..79) {
            dmg = "10d + 2"
        } else if (st in 80..84) {
            dmg = "11d"
        } else if (st in 85..89) {
            dmg = "11d + 2"
        } else if (st in 90..94) {
            dmg = "12d"
        } else if (st in 95..99) {
            dmg = "12d + 2"
        } else if (st in 100..109) {
            dmg = "13d"
        } else if (st >= 110) {
            var d = 0
            while (st > 109) {
                st -= 10
                d++
            }

            dmg = (13 + d).toString() + "d"
        }
        return dmg
    }
}
