package org.gametools.cleaner;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.gametools.testing.TestResource.*;

class StorageLocatorTest {

    @Test
    void should_return_storage_drives() {
        final String libraryFile = fromFile("libraryfolders.vdf");
        final StorageLocator storageLocator = new StorageLocator(libraryFile);

        List<StorageDrive> drives = storageLocator.getDrives();

        assertThat(drives).containsExactlyInAnyOrder(
            new StorageDrive("0", "/home/user/.local/share/Steam"),
            new StorageDrive("1", "/media/drive/games/steam")
        );
    }

    @Test
    void should_return_zero_drives_when_library_file_is_empty() {
        final String emptyFile = emptyFile();
        assertThat(new File(emptyFile)).exists();

        final StorageLocator storageLocator = new StorageLocator(emptyFile);

        List<StorageDrive> drives = storageLocator.getDrives();

        assertThat(drives).isEmpty();
    }

    @Test
    void should_return_zero_drives_when_library_file_is_missing() {
        final String emptyFile = "/random/missing/file";
        assertThat(new File(emptyFile)).doesNotExist();

        final StorageLocator storageLocator = new StorageLocator(emptyFile);

        List<StorageDrive> drives = storageLocator.getDrives();

        assertThat(drives).isEmpty();
    }
}
