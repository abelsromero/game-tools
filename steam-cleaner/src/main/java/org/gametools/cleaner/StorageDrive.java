package org.gametools.cleaner;

public record StorageDrive(String id, String path) {

    public String getAppsPath() {
        return "%s/%s".formatted(path, AppsRepository.getAppsPath());
    }

    public String getCompatdataPath() {
        return "%s/%s".formatted(path, AppsRepository.getCompatdataPath());
    }
}
