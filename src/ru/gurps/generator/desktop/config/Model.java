package ru.gurps.generator.desktop.config;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import static java.lang.annotation.ElementType.FIELD;

public class Model extends Db {
    @Target(FIELD)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface Ignore {}

    protected String tableName(){
        String className = this.getClass().getSimpleName();
        String tableName;

        if(className.substring(className.length() - 1).equals("y"))
            tableName = className.substring(0, className.length() - 1) + "ies";
        else tableName = className + "s";

        return tableName;
    }

    public Model create() {
        String names = "(id,";
        String values = "VALUES(DEFAULT,";

        for (Field field : this.getClass().getDeclaredFields())
            try {
                if (!field.isAnnotationPresent(Ignore.class) && field.get(this) != null && !field.getName().equals("id")) {
                    names += " " + field.getName() + ",";
                    values += " '" + field.get(this) + "',";
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        names = names.substring(0, names.length() - 1) + ")";
        values = values.substring(0, values.length() - 1) + ")";
        try {
            createConnection();
            connect.createStatement().executeUpdate("INSERT INTO " + tableName() + names + " " + values);
            ResultSet result = connect.createStatement().executeQuery("SELECT id FROM " + tableName() + " ORDER BY id ASC");
            result.last();
            result = connect.createStatement().executeQuery("SELECT * FROM " + tableName() + " WHERE id=" + result.getInt("id"));
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
                else if (Integer.class.isAssignableFrom(field.getType()))
                    field.set(this, Integer.parseInt(parametr.getValue()));
                else if (Double.class.isAssignableFrom(field.getType()))
                    field.set(this, Double.parseDouble(parametr.getValue()));
                else if (Boolean.class.isAssignableFrom(field.getType()))
                    field.set(this, Boolean.parseBoolean(parametr.getValue()));

                params += parametr.getKey() + "='" + parametr.getValue() + "',";
            }
        }
        params = params.substring(0, params.length() - 1);
            createConnection();
            connect.createStatement().executeUpdate("UPDATE " + tableName() + " SET " + params + " WHERE id=" + id());
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
            connect.createStatement().executeUpdate("UPDATE " + tableName() + " SET " + key + "='" + value + "' WHERE id=" + id());
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
            if(field.isAnnotationPresent(Ignore.class)) continue;
            try {
                if (field.getName().equals("id")) id = Integer.toString((Integer) field.get(this));
                else if (field.get(this) != null) params += field.getName() + "='" + field.get(this) + "',";
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        params = params.substring(0, params.length() - 1);

        try {
            createConnection();
            connect.createStatement().executeUpdate("UPDATE " + tableName() + " SET " + params + " WHERE id=" + id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete(int id) {
        try {
            createConnection();
            connect.createStatement().executeUpdate("DELETE FROM " + tableName() + " WHERE id=" + id);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete() {
        try {
            createConnection();
            connect.createStatement().executeUpdate("DELETE FROM " + tableName() + " WHERE id=" + id());
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean delete_all(ObservableList<Model> models){
        if(models.isEmpty()) return true;
        String params = "";
        for(Model model : models){
            try {
                Integer id = (Integer) model.getClass().getDeclaredField("id").get(model);
                if(params.equals("")) params += "id='" + id + "'";
                else params += " or id='" + id + "'";
            } catch(IllegalAccessException | NoSuchFieldException e) {
                e.printStackTrace();
            }
        }

        try {
            createConnection();
            connect.createStatement().executeUpdate("DELETE FROM " + tableName() + " WHERE " + params);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public ObservableList all() {
        ObservableList list = FXCollections.observableArrayList();
        try {
            createConnection();
            ResultSet results = connect.createStatement().executeQuery("SELECT * FROM " + tableName());

            while (results.next()) {
                list.add(setModel(results));
            }
        } catch (SQLException e) {
            if(e.getErrorCode() == 2000) return list;
            e.printStackTrace();
        }

        return list;
    }

    public Model find(int id) {
        try {
            createConnection();
            ResultSet result = connect.createStatement().executeQuery("SELECT * FROM " + tableName() + " WHERE id=" + id);
            result.next();
            return setModel(result);
        } catch (SQLException e) {
            if(e.getErrorCode() == 2000) return this;
            e.printStackTrace();
        }

        return this;

    }

    protected ObservableList hasMany(Model model) {
        ObservableList list = FXCollections.observableArrayList();
        try {
            String column = this.getClass().getSimpleName() + "id";
            Integer value = (Integer) this.getClass().getDeclaredField("id").get(this);
            createConnection();
            ResultSet results = connect.createStatement().executeQuery("SELECT * FROM " + model.tableName() + " WHERE " + column + "=" + value);
            while (results.next()) {
                list.add(model.setModel(results));
            }
        } catch(SQLException e) {
            if(e.getErrorCode() == 2000) return list;
            e.printStackTrace();
        } catch(IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }

        return list;
    }

    public Model find_by(String column, Object value) {
        try {
            createConnection();
            ResultSet result = connect.createStatement().executeQuery("SELECT * FROM " + tableName() + " WHERE " + column + "='" + value + "'");
            result.next();
            return setModel(result);
        } catch (SQLException e) {
            if(e.getErrorCode() == 2000) return this;
            e.printStackTrace();
        }
        return this;
    }

    public Model find_by(HashMap<String, Object> paramsHash) {
        String params = "";
        String query;
        if (paramsHash.isEmpty()) query = "SELECT * FROM " + tableName();
        else {
            for (Map.Entry<String, Object> parametr : paramsHash.entrySet())
                params += parametr.getKey() + "='" + parametr.getValue() + "' and ";

            params = params.substring(0, params.length() - 5);
            query = "SELECT * FROM " + tableName() + " WHERE " + params;
        }

        try {
            ResultSet results = connect.createStatement().executeQuery(query);
            results.next();
            return setModel(results);
        } catch (SQLException e) {
            if(e.getErrorCode() == 2000) return this;
            e.printStackTrace();
        }

        return this;
    }

    public ObservableList where(HashMap<String, Object> paramsHash) {
        String params = "";
        String query;
        ObservableList list = FXCollections.observableArrayList();
        if (paramsHash.isEmpty()) {
            query = "SELECT * FROM " + tableName();
        } else {
            for (Map.Entry<String, Object> parametr : paramsHash.entrySet()) {
                params += parametr.getKey() + "='" + parametr.getValue() + "' and ";
            }
            params = params.substring(0, params.length() - 5);

            query = "SELECT * FROM " + tableName() + " WHERE " + params;
        }
        try {
            createConnection();
            ResultSet results = connect.createStatement().executeQuery(query);
            while (results.next()) {
                list.add(setModel(results));
            }
        } catch (SQLException e) {
            if(e.getErrorCode() == 2000) return list;
            e.printStackTrace();
        }

        return list;
    }

    public ObservableList where(String column, Object value) {
        ObservableList list = FXCollections.observableArrayList();
        try {
            createConnection();
            ResultSet results = connect.createStatement().executeQuery("SELECT * FROM " + tableName() + " WHERE " + column + "=" + value);
            while (results.next()) {
                list.add(setModel(results));
            }
        } catch (SQLException e) {
            if(e.getErrorCode() == 2000) return list;
            e.printStackTrace();
        }

        return list;
    }

    public ObservableList where(String query) {
        ObservableList list = FXCollections.observableArrayList();
        try {
            createConnection();
            ResultSet results = connect.createStatement().executeQuery("SELECT * FROM " + tableName() + " WHERE " + query);
            while (results.next()) {
                list.add(setModel(results));
            }
        } catch (SQLException e) {
            if(e.getErrorCode() == 2000) return list;
            e.printStackTrace();
        }

        return list;
    }

    private Model setModel(ResultSet result) throws SQLException {
        Model model = this;
        try {
            model = this.getClass().newInstance();
        for (Field field : model.getClass().getDeclaredFields()) {
            if(!field.isAnnotationPresent(Ignore.class)) {
                    if (String.class.isAssignableFrom(field.getType()))
                        field.set(model, result.getString(field.getName()));
                    else if (Integer.class.isAssignableFrom(field.getType()))
                        field.set(model, result.getInt(field.getName()));
                    else if (Double.class.isAssignableFrom(field.getType()))
                        field.set(model, result.getDouble(field.getName()));
                    else if (Boolean.class.isAssignableFrom(field.getType()))
                        field.set(model, result.getBoolean(field.getName()));
            }
        }

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return model;
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
