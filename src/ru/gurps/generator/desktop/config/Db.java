package ru.gurps.generator.desktop.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.FlywayException;
import org.h2.jdbc.JdbcSQLException;
import ru.gurps.generator.desktop.Main;

import java.net.URLDecoder;
import java.sql.Connection;
import java.sql.DriverManager;


public class Db {
    public static Connection connect = null;
    
    protected static void createConnection(){
        if(connect != null){
            return;
        }
        
        try {
            Class.forName("org.h2.Driver").newInstance();
            String parent = "\\w*.jar";
            String s = URLDecoder.decode(Main.class.getProtectionDomain().getCodeSource()
                    .getLocation().getPath().replaceAll(parent, ""), "UTF-8");
            String db = "jdbc:h2:" + s + "db/gurps";
            migrations(db);

            connect = DriverManager.getConnection(db, "sa", "");
        } catch(JdbcSQLException e){
            if(e.getErrorCode() == 90020){
                System.err.println(Main.locale.getString("app_is_running"));
                System.exit(0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void migrations(String db){
        Flyway flyway = new Flyway();
        flyway.setDataSource(db, "sa", "");
        try{
            flyway.init();
            flyway.migrate();
        } catch (FlywayException e){
            try {
                flyway.migrate();
            } catch (FlywayException ex){
                System.err.println(Main.locale.getString("app_is_running"));
                System.exit(0);
            }
        }
    }
}
