package org.gametools.cleaner;

import org.gametools.utilities.VdfFile;
import org.gametools.utilities.VdfParser;

import java.util.List;
import java.util.Map;

public class StorageLocator {

    // VDF 'libraryfolders' file
    private final String libraryDescriptorPath;

    public StorageLocator(String libraryDescriptorPath) {
        this.libraryDescriptorPath = libraryDescriptorPath;
    }

    public List<StorageDrive> getDrives() {
        final VdfParser vdfParser = new VdfParser();
        final VdfFile vdfFile = vdfParser.parse(libraryDescriptorPath);

        Map<String, Object> root = (Map<String, Object>) vdfFile.properties().get("libraryfolders");

        return root.values()
            .stream()
            .map(value -> ((Map<String, String>)value).get("path"))
            .map(StorageDrive::new)
            .toList();
    }
}


