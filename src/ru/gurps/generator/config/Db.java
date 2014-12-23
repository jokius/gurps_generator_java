package ru.gurps.generator.config;

import java.sql.Connection;
import java.sql.DriverManager;
import ru.gurps.generator.Main;

public class Db {
    public static Connection connect = null;

    public Db() {
        try {
            Class.forName("org.h2.Driver").newInstance();
            String parent = "\\w*.jar";
            String s = ru.gurps.generator.Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll(parent, "");
            connect = DriverManager.getConnection("jdbc:h2:"+ s + "db/gurps", "sa", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
