package org.gametools.utilities;

import org.apache.commons.io.file.PathUtils;

import java.io.IOException;
import java.nio.file.Path;

public class FileUtils {

    // This is an approximation. For example, 'du' includes 4K for folders.
    public static String sizeFormatted(Path path) {
        try {
            return "%d MB".formatted(PathUtils.sizeOfDirectory(path) / (1024 * 1024));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
