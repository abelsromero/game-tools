package org.gametools.cleaner;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.gametools.testing.TestResource.fromClasspath;

class StorageLocatorTest {

    @Test
    void should_return_storage_drives() {
        final String libraryFile = fromClasspath("libraryfolders.vdf");
        final StorageLocator storageLocator = new StorageLocator(libraryFile);

        List<StorageDrive> drives = storageLocator.getDrives();

        assertThat(drives).containsExactlyInAnyOrder(
            new StorageDrive("0", "/home/user/.local/share/Steam"),
            new StorageDrive("1", "/media/drive/games/steam")
        );

    }

}
