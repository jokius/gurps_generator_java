package ru.gurps.generator.config;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Model extends Db {
    private String table = this.getClass().getSimpleName() + "s";

    public Model create() {
        String params = "";

        for (Field field : this.getClass().getDeclaredFields())
            try {
                if (field.get(this) != null && !field.getName().equals("id"))
                    params += field.getName() + "=" + field.get(this) + ",";
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }

        try {
            createConnection();
            ResultSet result = connect.createStatement().executeQuery("SELECT id FROM " + table);
            result.last();
            int id = result.getRow() + 1;
            params += "id=" + id;

            connect.createStatement().executeUpdate("INSERT INTO " + table + " SET " + params);
            result = connect.createStatement().executeQuery("SELECT * FROM " + table + " WHERE id=" + id);
            result.next();
            return setModel(result);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public boolean update(HashMap<String, String> paramsHash) {
        String params = "";
        try {
        if (!paramsHash.isEmpty()) {
            for (Map.Entry<String, String> parametr : paramsHash.entrySet()) {
                Field field = this.getClass().getDeclaredField(parametr.getKey());
                if (String.class.isAssignableFrom(field.getType()))
                    field.set(this, parametr.getValue());
                else if (int.class.isAssignableFrom(field.getType()))
                    field.set(this, Integer.parseInt(parametr.getValue()));
                else if (double.class.isAssignableFrom(field.getType()))
                    field.set(this, Double.parseDouble(parametr.getValue()));
                else if (boolean.class.isAssignableFrom(field.getType()))
                    field.set(this, Boolean.parseBoolean(parametr.getValue()));

                params += parametr.getKey() + "='" + parametr.getValue() + "',";
            }
        }
        params = params.substring(0, params.length() - 1);
            createConnection();
            connect.createStatement().executeUpdate("UPDATE " + table + " SET " + params + " WHERE id=" + id());
            return true;
        } catch (IllegalAccessException | NoSuchFieldException | SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean update_single(String key, Object value){
        try {
            this.getClass().getDeclaredField(key).set(this, value);
            createConnection();

            connect.createStatement().executeUpdate("UPDATE " + table + " SET " + key + "=" + value + " WHERE id=" + id());
            return true;
        } catch (IllegalAccessException | NoSuchFieldException | SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean save() {
        String params = "";
        String id = "";
        for (Field field : this.getClass().getDeclaredFields()) {
            try {
                if (field.getName().equals("id")) id = Integer.toString((Integer) field.get(this));
                else if (field.get(this) != null) params += field.getName() + "=" + field.get(this) + ",";
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        params = params.substring(0, params.length() - 1);

        try {
            createConnection();
            connect.createStatement().executeUpdate("UPDATE " + table + " SET " + params + " WHERE id=" + id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(int id) {
        try {
            createConnection();
            connect.createStatement().executeUpdate("DELETE FROM " + table + " WHERE id=" + id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete() {
        try {
            createConnection();
            connect.createStatement().executeUpdate("DELETE FROM " + table + " WHERE id=" + id());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ObservableList all() {
        try {
            createConnection();
            ResultSet results = connect.createStatement().executeQuery("SELECT * FROM " + table);

            ObservableList list = FXCollections.observableArrayList();
            while (results.next()) {
                list.add(setModel(results));
            }

            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public ResultSet find(int id) {
        try {
            createConnection();
            return connect.createStatement().executeQuery("SELECT * FROM " + table + " WHERE id=" + id);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public ResultSet find_by(String column, String value) {
        try {
            createConnection();
            return connect.createStatement().executeQuery("SELECT * FROM " + table + " WHERE " + column + "=" + value);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }

    public ResultSet where(HashMap<String, String> paramsHash) {
        String params = "";
        String query;
        if (paramsHash.isEmpty()) {
            query = "SELECT * FROM " + table;
        } else {
            for (Map.Entry<String, String> parametr : paramsHash.entrySet()) {
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

    public boolean isAny(ResultSet records) {
        try {
            return records.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isEmpty(ResultSet records) {
        try {
            return !records.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private Model setModel(ResultSet result) throws SQLException {
        for (Field field : this.getClass().getDeclaredFields()) {
            try {
                if (String.class.isAssignableFrom(field.getType()))
                    field.set(this, result.getString(field.getName()));
                else if (int.class.isAssignableFrom(field.getType()))
                    field.set(this, result.getInt(field.getName()));
                else if (double.class.isAssignableFrom(field.getType()))
                    field.set(this, result.getDouble(field.getName()));
                else if (boolean.class.isAssignableFrom(field.getType()))
                    field.set(this, result.getBoolean(field.getName()));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    private int id(){
        try {
            return (int) this.getClass().getDeclaredField("id").get(this);
        } catch (IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return -1;
    }
}
