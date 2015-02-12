package ru.gurps.generator.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;


public final class Db {
    public static Connection connect = null;
    
    public static ResultSet create(String table, HashMap<String, String> paramsHash){
        String params = "";

        if(!paramsHash.isEmpty()){
            for (Map.Entry<String, String> parametr : paramsHash.entrySet()){
                params += parametr.getKey() + "='" + parametr.getValue() + "',";
            }
        }

        try {
            createConnection();
            ResultSet result = connect.createStatement().executeQuery("SELECT id FROM " + table);
            result.last();
            int id = result.getRow() + 1;
            params += "id=" + id;
            
            connect.createStatement().executeUpdate("INSERT INTO " + table + " SET " + params);
            
            return connect.createStatement().executeQuery("SELECT * FROM " + table + " WHERE id=" + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return null;
    }

    public static boolean update(String table, int id, HashMap<String, String> paramsHash){
        String params = "";
        
        if(!paramsHash.isEmpty()){
            for (Map.Entry<String, String> parametr : paramsHash.entrySet()){
                params += parametr.getKey() + "='" + parametr.getValue() + "',";
            }
        }
        params = params.substring(0, params.length()-1);
        
        try {
            createConnection();
            connect.createStatement().executeUpdate("UPDATE " + table + " SET " + params + " WHERE id=" + id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean delete(String table, int id){
        try {
            createConnection();
            connect.createStatement().executeUpdate("DELETE FROM " + table + " WHERE id=" + id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static ResultSet all(String table){
        try {
            createConnection();
            return connect.createStatement().executeQuery("SELECT * FROM " + table);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public static ResultSet find(String table, int id){
        try {
            createConnection();
            return connect.createStatement().executeQuery("SELECT * FROM " + table + " WHERE id=" + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
        
    }

    public static ResultSet find_by(String table, String column, String value){
        try {
            createConnection();
            return connect.createStatement().executeQuery("SELECT * FROM " + table + " WHERE " + column + "=" + value);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }
    
    public static ResultSet where(String table, HashMap<String, String> paramsHash){
        String params = "";
        String query;
        if(paramsHash.isEmpty()){
            query = "SELECT * FROM " + table;
        }
        else{
            for (Map.Entry<String, String> parametr : paramsHash.entrySet()){
                params += parametr.getKey() + "='" + parametr.getValue() + "' and ";
            }
            params = params.substring(0, params.length() - 5);
            
            query = "SELECT * FROM " + table + " WHERE " + params;
        }

        try {
            createConnection();
            return connect.createStatement().executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
    
    public static boolean isAny(ResultSet records){
        try {
            return records.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean isEmpty(ResultSet records){
        try {
            return !records.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    
    private static void createConnection(){
        if(connect != null){
            return;
        }
        
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
