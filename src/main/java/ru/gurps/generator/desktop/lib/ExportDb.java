package ru.gurps.generator.desktop.lib;

import ru.gurps.generator.desktop.singletons.Property;

import java.io.*;
import java.net.URLDecoder;


public class ExportDb {
    private static Property config = Property.INSTANCE;
    public ExportDb() {
        try {
            String jarFolder = URLDecoder.decode(ExportDb.class.getProtectionDomain().getCodeSource().getLocation()
                    .getPath().replaceAll("\\w*.jar", ""), "UTF-8");

            File file = new File(config.getDbFolder() + config.getDbName());

            if (!file.exists() || file.isDirectory()) {
                File dir = new File(jarFolder + "db");
                if (!dir.exists() || !dir.isDirectory()) dir.mkdir();
                ExportResource("/db/gurps.mv.db");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void ExportResource(String resourceName) throws Exception {
        InputStream stream;
        OutputStream resStreamOut;
        stream = ExportDb.class.getResourceAsStream(resourceName);
        if (stream == null) {
            throw new Exception("Cannot get resource " + resourceName + " from Jar file.");
        }
        int readBytes;
        byte[] buffer = new byte[4096];
        resStreamOut = new FileOutputStream(config.getDbFolder() + config.getDbName());
        while ((readBytes = stream.read(buffer)) > 0) {
            resStreamOut.write(buffer, 0, readBytes);
        }
        stream.close();
        resStreamOut.close();
    }
}

