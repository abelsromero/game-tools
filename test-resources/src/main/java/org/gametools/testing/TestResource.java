package org.gametools.testing;

import java.nio.file.Path;

public class TestResource {

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
