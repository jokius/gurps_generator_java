package ru.gurps.generator.desktop.lib;

public final class Dmg {
    
    public static String thrust(int st){
        String dmg = "0";
        if(st == 1 || st == 2) { dmg = "1d - 6"; }
        else if(st == 3 || st == 4) { dmg = "1d - 5"; }
        else if(st == 5 || st == 6) { dmg = "1d - 4"; }
        else if(st == 7 || st == 8) { dmg = "1d - 3"; }
        else if(st == 9 || st == 10) { dmg = "1d - 2"; }
        else if(st == 11 || st == 12) { dmg = "1d - 1"; }
        else if(st == 13 || st == 14) { dmg = "1d"; }
        else if(st == 15 || st == 16) { dmg = "1d + 1"; }
        else if(st == 17 || st == 18) { dmg = "1d + 2"; }
        else if(st == 19 || st == 20) { dmg = "2d - 1"; }
        else if(st == 21 || st == 22) { dmg = "2d"; }
        else if(st == 23 || st == 24) { dmg = "2d + 1"; }
        else if(st == 25 || st == 26) { dmg = "2d + 2"; }
        else if(st == 27 || st == 28) { dmg = "3d - 1"; }
        else if(st == 29 || st == 30) { dmg = "3d"; }
        else if(st == 31 || st == 32) { dmg = "3d + 1"; }
        else if(st == 33 || st == 34) { dmg = "3d + 2"; }
        else if(st == 35 || st == 36) { dmg = "4d - 1"; }
        else if(st == 37 || st == 38) { dmg = "4d"; }
        else if(st >= 39 && st < 45) { dmg = "4d + 1"; }
        else if(st >= 45 && st < 50) { dmg = "5d"; }
        else if(st >= 50 && st < 55) { dmg = "5d + 2"; }
        else if(st >= 55 && st < 60) { dmg = "6d"; }
        else if(st >= 60 && st < 65) { dmg = "7d - 1"; }
        else if(st >= 65 && st < 70) { dmg = "7d + 1"; }
        else if(st >= 70 && st < 75) { dmg = "8d"; }
        else if(st >= 75 && st < 80) { dmg = "8d + 2"; }
        else if(st >= 80 && st < 85) { dmg = "9d"; }
        else if(st >= 85 && st < 90) { dmg = "9d + 2"; }
        else if(st >= 90 && st < 95) { dmg = "10d"; }
        else if(st >= 95 && st < 100) { dmg = "10d + 2"; }
        else if(st >= 100 && st < 110) { dmg = "11d"; }
        else if(st >= 110) { 
            int d = 0;
            while(st > 109){
                st -= 10;
                d++;
            }
            
            dmg = 11 + d + "d";
        }
        return dmg;
    }

    public static String swing(int st){
        String dmg = "0";
        if(st == 1 || st == 2) { dmg = "1d - 5"; }
        else if(st == 3 || st == 4) { dmg = "1d - 4"; }
        else if(st == 5 || st == 6) { dmg = "1d - 3"; }
        else if(st == 7 || st == 8) { dmg = "1d - 2"; }
        else if(st == 9) { dmg = "1d - 1"; }
        else if(st == 10) { dmg = "1d"; }
        else if(st == 11) { dmg = "1d + 1"; }
        else if(st == 12) { dmg = "1d + 2"; }
        else if(st == 13) { dmg = "2d - 1"; }
        else if(st == 14) { dmg = "2d"; }
        else if(st == 15) { dmg = "2d + 1"; }
        else if(st == 16) { dmg = "2d + 2"; }
        else if(st == 17) { dmg = "3d - 1"; }
        else if(st == 18) { dmg = "3d"; }
        else if(st == 19) { dmg = "3d + 1"; }
        else if(st == 20) { dmg = "3d + 2"; }
        else if(st == 21) { dmg = "4d - 1"; }
        else if(st == 22) { dmg = "4d"; }
        else if(st == 23) { dmg = "4d + 1"; }
        else if(st == 24) { dmg = "4d + 2"; }
        else if(st == 25) { dmg = "5d - 1"; }
        else if(st == 26) { dmg = "5d"; }
        else if(st == 27 || st == 28) { dmg = "5d + 1"; }
        else if(st == 29 || st == 30) { dmg = "5d + 2"; }
        else if(st == 31 || st == 32) { dmg = "6d - 1"; }
        else if(st == 33 || st == 34) { dmg = "6d"; }
        else if(st == 35 || st == 36) { dmg = "6d + 1"; }
        else if(st == 37 || st == 38) { dmg = "6d + 2"; }
        else if(st >= 39 && st < 50) { dmg = "7d - 1"; }
        else if(st >= 50 && st < 55) { dmg = "8d - 1"; }
        else if(st >= 55 && st < 60) { dmg = "8d + 1"; }
        else if(st >= 60 && st < 65) { dmg = "9d"; }
        else if(st >= 65 && st < 70) { dmg = "9d + 2"; }
        else if(st >= 70 && st < 75) { dmg = "10d"; }
        else if(st >= 75 && st < 80) { dmg = "10d + 2"; }
        else if(st >= 80 && st < 85) { dmg = "11d"; }
        else if(st >= 85 && st < 90) { dmg = "11d + 2"; }
        else if(st >= 90 && st < 95) { dmg = "12d"; }
        else if(st >= 95 && st < 100) { dmg = "12d + 2"; }
        else if(st >= 100 && st < 110) { dmg = "13d"; }
        else if(st >= 110) {
            int d = 0;
            while(st > 109){
                st -= 10;
                d++;
            }

            dmg = 13 + d + "d";
        }
        return dmg;
    }
}
