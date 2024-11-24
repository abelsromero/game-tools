package org.gametools.cleaner;

import org.gametools.testing.ClasspathResource;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class StorageLocatorTest {

    @Test
    void should_return_storage_drives() {
        final String libraryFile = ClasspathResource.absolutePath("libraryfolders.vdf");
        final StorageLocator storageLocator = new StorageLocator(libraryFile);

        List<StorageDrive> drives = storageLocator.getDrives();

        assertThat(drives).containsExactlyInAnyOrder(
            new StorageDrive("/home/user/.local/share/Steam"),
            new StorageDrive("/media/drive/games/steam")
        );

    }

}
