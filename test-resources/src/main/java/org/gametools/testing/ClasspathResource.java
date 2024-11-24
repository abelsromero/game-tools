package org.gametools.testing;

public class ClasspathResource {

    public static String absolutePath(String name) {
        return ClasspathResource.class.getClassLoader().getResource(name).getFile();
    }

}
