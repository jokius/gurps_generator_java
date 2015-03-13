package ru.gurps.generator.config;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Model extends Db {
    private String table = this.getClass().getSimpleName() + "s";

    public ResultSet create(HashMap<String, String> paramsHash){
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

    public boolean update(int id, HashMap<String, String> paramsHash){
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

    public boolean save(){
        String params = "";
        String id = "";
        for( Field field : this.getClass().getDeclaredFields()){
            try {
                if(field.getName().equals("id")) id = Integer.toString((Integer) field.get(this));
                else if(field.get(this) != null) params += field.getName() + "=" + field.get(this) + ",";
            } catch (IllegalAccessException e) {
                e.printStackTrace();
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

    public boolean delete(int id){
        try {
            createConnection();
            connect.createStatement().executeUpdate("DELETE FROM " + table + " WHERE id=" + id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ResultSet all(){
        try {
            createConnection();
            return connect.createStatement().executeQuery("SELECT * FROM " + table);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResultSet find(int id){
        try {
            createConnection();
            return connect.createStatement().executeQuery("SELECT * FROM " + table + " WHERE id=" + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public ResultSet find_by(String column, String value){
        try {
            createConnection();
            return connect.createStatement().executeQuery("SELECT * FROM " + table + " WHERE " + column + "=" + value);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public ResultSet where(HashMap<String, String> paramsHash){
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

    public boolean isAny(ResultSet records){
        try {
            return records.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isEmpty(ResultSet records){
        try {
            return !records.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
}
