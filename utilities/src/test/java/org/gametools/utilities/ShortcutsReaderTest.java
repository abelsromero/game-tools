package org.gametools.utilities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.gametools.testing.TestResource.fromFile;

class ShortcutsReaderTest {

    private ShortcutsReader reader = new ShortcutsReader();

    @Test
    void should_not_fail_if_file_does_not_exist() {
        var result = reader.read(Path.of("fake-dir"));

        assertThat(result).isEmpty();
    }

    @Test
    void should_not_fail_if_file_is_empty(@TempDir Path tempDir) throws IOException {
        Files.createFile(tempDir.resolve("shortcuts.vdf"));
        assertThat(tempDir).exists();

        var result = reader.read(tempDir);

        assertThat(result).isEmpty();
    }

    @Test
    void should_read() {
        final Path path = Path.of(fromFile("userdata"));

        var result = reader.read(path);

        assertThat(result)
            .containsExactlyInAnyOrder(
                new Shortcut(-1074180434, "MnM Launcher", ""),
                new Shortcut(-185686590, "MnM", "--token 123")
            );
    }

}
