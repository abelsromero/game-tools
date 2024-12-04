package org.gametools.testing;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class TestResource {

    public static String emptyFile() {
        try {
            return File.createTempFile("empty", "").toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String fromClasspath(String name) {
        return TestResource.class.getClassLoader().getResource(name).getFile();
    }

    public static String fromFile(String name) {
        return Path.of("..")
            .resolve("test-resources")
            .resolve("src/main/resources")
            .resolve(name)
            .normalize()
            .toFile()
            .getAbsolutePath();
    }
}
