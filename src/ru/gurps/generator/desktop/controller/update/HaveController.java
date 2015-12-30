package ru.gurps.generator.desktop.controller.update;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.web.WebView;
import ru.gurps.generator.desktop.Main;
import ru.gurps.generator.desktop.config.Model;
import ru.gurps.generator.desktop.controller.helpers.AbstractController;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

import java.util.Set;

public class HaveController extends AbstractController {
    public WebView webView;
    public Button accept;
    public Button cancel;

    private Set<Map.Entry<String, JsonElement>> entries;
    private String log = "<html>";

    @FXML
    private void initialize() {
        updateFromServer();
        log += "<head><link rel='stylesheet' href='"
                + Main.class.getResource("resources/css/main.css") + "'> </head><body>";
        webView.getEngine().loadContent(log);
        acceptSet();
        cancelSet();
    }

    private void updateFromServer() {
        String repose = getRequest("data/change_log?start=" + updateStart);
        JsonObject json = new JsonParser().parse(repose).getAsJsonObject();
        entries = json.entrySet();
        entries.forEach(this::objectSet);
        log += "</html></body>";
    }

    private void objectSet(Map.Entry<String, JsonElement> entry) {
        if(entry.getKey().equals("version")) return;
        JsonArray arr = entry.getValue().getAsJsonArray();
        if (arr.size() == 0) return;
        log += "<h3>" + Main.locale.getString(entry.getKey()) + "</h3>";
        arr.forEach(element -> {
            JsonObject object = element.getAsJsonObject();
            log += "<p>" + object.get("name").getAsString();
            if(object.get("name_en").isJsonNull()) log += "</p>";
            else log += "(" + object.get("name_en").getAsString() + ")</p>";
            log += object.get("change_info").getAsString();
        });
    }

    private void cancelSet() {
        cancel.setOnAction(event -> cancel.getScene().getWindow().hide());
    }

    private void acceptSet() {
        accept.setOnAction(event -> {
            entries.forEach(entry -> {
                if (entry.getKey().equals("version")) {
                    updateStart = entry.getValue().getAsInt();
                    saveProperties();
                    return;
                }
                entry.getValue().getAsJsonArray().forEach(this::updateEntry);
            });
            accept.getScene().getWindow().hide();
        });
    }

    private void updateEntry(JsonElement element) {
        try {
            JsonObject object = element.getAsJsonObject();
            JsonObject rawInfo = object.get("raw_info").getAsJsonObject();
            Class<?> clazz = Class.forName("ru.gurps.generator.desktop.models.rules." +
                    object.get("model").getAsString());
            Constructor<?> constructor = clazz.getConstructor();
            Object instance = constructor.newInstance();
            Object record = Model.class.getDeclaredMethod("find", int.class)
                    .invoke(instance, rawInfo.get("id").getAsInt());
            String method = clazz.getDeclaredField("id").get(record) == null ? "create" : "save";
            rawInfo.entrySet().forEach(entry -> {
                try {
                    Field field = clazz.getDeclaredField(toCamelCase(entry.getKey()));
                    if(field.isAnnotationPresent(Model.Ignore.class)) return;

                if (String.class.isAssignableFrom(field.getType()))
                    field.set(record, entry.getValue().getAsString());
                else if (Integer.class.isAssignableFrom(field.getType()))
                    field.set(record, entry.getValue().getAsInt());
                else if (Double.class.isAssignableFrom(field.getType()))
                    field.set(record, entry.getValue().getAsDouble());
                else if (Boolean.class.isAssignableFrom(field.getType()))
                    field.set(record, entry.getValue().getAsBoolean());
                } catch (IllegalAccessException | NoSuchFieldException e) {
                    e.printStackTrace();
                }
            });

            Model.class.getDeclaredMethod(method).invoke(record);

        } catch (NoSuchFieldException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException |
                InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private static String toCamelCase(String s){
        String[] parts = s.split("_");
        String camelCaseString = "";
        for (String part : parts){
            if(parts[0].equals(part)) camelCaseString += part;
            else camelCaseString += toProperCase(part);
        }
        return camelCaseString;
    }

    private static String toProperCase(String s) {
        return s.substring(0, 1).toUpperCase() +
                s.substring(1).toLowerCase();
    }
}
