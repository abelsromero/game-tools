package org.gametools.cleaner;

import org.gametools.utilities.VdfFile;
import org.gametools.utilities.VdfParser;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

public class StorageLocator {

    // VDF 'libraryfolders' file
    private final String libraryDescriptorPath;

    public StorageLocator(String libraryDescriptorPath) {
        this.libraryDescriptorPath = libraryDescriptorPath;
    }

    public List<StorageDrive> getDrives() {
        if (Files.notExists(Path.of(libraryDescriptorPath)))
            return List.of();

        final VdfParser vdfParser = new VdfParser();
        final VdfFile vdfFile = vdfParser.parse(libraryDescriptorPath);

        if (vdfFile == null)
            return List.of();

        Map<String, Object> root = (Map<String, Object>) vdfFile.properties().get("libraryfolders");

        return root.entrySet()
            .stream()
            .map(entry -> new StorageDrive(entry.getKey(), ((Map<String, String>) entry.getValue()).get("path")))
            .toList();
    }
}


