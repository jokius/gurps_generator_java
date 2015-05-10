package ru.gurps.generator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import ru.gurps.generator.controller.UsersController;

import java.io.*;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {
    public static final ResourceBundle locale = ResourceBundle.getBundle("bundles.generator", new Locale("ru", "RU"));

    @Override
    public void start(Stage primaryStage) throws Exception {
        String parent = "\\w*.jar";
        String jarFolder = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().replaceAll(parent, "");
        File file = new File(jarFolder + "db/gurps.mv.db");

        if(!file.exists() || file.isDirectory()) {
            File dir = new File(jarFolder + "db");
            if(!dir.exists() || !dir.isDirectory()) dir.mkdir();
            ExportResource("/db/gurps.mv.db");
        }

        usersStage(primaryStage);
    }

    protected void usersStage(Stage stage){
        stage.setResizable(false);
        FXMLLoader view = new FXMLLoader(Main.class.getResource("resources/views/selectUser.fxml"));
        view.setResources(locale);
        UsersController controller = new UsersController(stage);
        view.setController(controller);
        try {
            Parent root = view.load();
            stage.setScene(new Scene(root, 397, 293));
            stage.setTitle(locale.getString("app_name_user_select"));
            stage.show();
        } catch(IOException e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    /**
     * Export a resource embedded into a Jar file to the local file path.
     *
     * @param resourceName ie.: "/SmartLibrary.dll"
     * @return The path to the exported resource
     * @throws Exception
     */
    static public String ExportResource(String resourceName) throws Exception {
        InputStream stream = null;
        OutputStream resStreamOut = null;
        String jarFolder;
        try {
            stream = Main.class.getResourceAsStream(resourceName);//note that each / is a directory down in the "jar tree" been the jar the root of the tree
            if(stream == null) {
                throw new Exception("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            jarFolder = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath()).getParentFile().getPath().replace('\\', '/');
            resStreamOut = new FileOutputStream(jarFolder + resourceName);
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }

            stream.close();
            resStreamOut.close();
        } catch (Exception ex) {
            throw ex;
        }

        return jarFolder + resourceName;
    }
}