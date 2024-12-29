package org.gametools.cleaner;

import org.gametools.utilities.FileUtils;

import java.nio.file.Path;

public record App(Integer id, String name, String installDir) {

    public void printSummary() {
        System.out.printf("%-10d %-10s%n", id, name);
    }

    public void printDetails(StorageDrive drive) {
        System.out.printf("%-11s %d%n", "Id:", id);
        System.out.printf("%-11s %s%n", "Name:", name);
        System.out.printf("%-11s %s/%s%n", "Location:", drive.getAppsPath(), installDir);
        final String compatdataPath = drive.getCompatdataPath();
        System.out.printf("%-11s %s/%s (%s)%n", "Compatdata:", compatdataPath, id, FileUtils.sizeFormatted(Path.of(compatdataPath, id.toString())));
    }
}
