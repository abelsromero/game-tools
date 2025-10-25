package org.gametools.utilities;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.gametools.testing.TestResource.fromFile;

class BinaryVdfParserTest {

    private BinaryVdfParser parser = new BinaryVdfParser();

    @Test
    void should_fail_if_file_does_not_exist() {
        Throwable throwable = catchThrowable(() -> parser.parse(Path.of("fake-file")));

        assertThat(throwable).isInstanceOf(FileNotFoundException.class);
    }

    @Test
    void should_not_fail_if_file_is_empty(@TempDir Path tempDir) throws IOException {
        final Path tempFile = tempDir.resolve(UUID.randomUUID().toString());
        Files.createFile(tempFile);
        assertThat(tempFile).exists();

        var result = parser.parse(tempFile);

        assertThat(result).isEmpty();
    }

    @Test
    void should_parse() throws IOException {
        final Path path = Path.of(fromFile("userdata/shortcuts.vdf"));

        var result = parser.parse(path);

        assertThat(result).containsOnlyKeys("shortcuts");
        var shortcut = result.get("shortcuts");
        assertThat(shortcut).hasSize(2);
        assertThat(shortcut.get(0))
            .containsEntry("appid", -1074180434)
            .containsEntry("AppName", "MnM Launcher")
            .containsEntry("Exe", "/media/games/mnmlauncher/MnMLauncher.exe")
            .containsEntry("StartDir", "/media/games/mnmlauncher/")
            .containsEntry("icon", "/home/uuu/Documents/MnMCovers1.png")
            .containsEntry("ShortcutPath", "")
            .containsEntry("LaunchOptions", "")
            .containsEntry("IsHidden", 0)
            .containsEntry("AllowDesktopConfig", 1)
            .containsEntry("AllowOverlay", 1)
            .containsEntry("OpenVR", 0)
            .containsEntry("Devkit", 0)
            .containsEntry("DevkitGameID", "")
            .containsEntry("DevkitOverrideAppID", 0)
            .containsEntry("LastPlayTime", 1761397712)
            .containsEntry("FlatpakAppID", "")
            .containsEntry("sortas", "")
            .containsEntry("tags", Collections.emptyMap());

        assertThat(shortcut.get(1))
            .containsEntry("appid", -185686590)
            .containsEntry("AppName", "MnM")
            .containsEntry("Exe", "/media/games/mnmlauncher/mnm/mnm.exe")
            .containsEntry("StartDir", "/media/games/mnmlauncher/")
            .containsEntry("icon", "/home/uuu/Documents/mnm/MnMCovers1.png")
            .containsEntry("ShortcutPath", "")
            .containsEntry("LaunchOptions", "--token 123")
            .containsEntry("IsHidden", 0)
            .containsEntry("AllowDesktopConfig", 1)
            .containsEntry("AllowOverlay", 1)
            .containsEntry("OpenVR", 0)
            .containsEntry("Devkit", 0)
            .containsEntry("DevkitGameID", "")
            .containsEntry("DevkitOverrideAppID", 0)
            .containsEntry("LastPlayTime", 1761397883)
            .containsEntry("FlatpakAppID", "")
            .containsEntry("sortas", "")
            .containsEntry("tags", Collections.emptyMap());
    }

    @Test
    void should_parse_only_required_properties() throws IOException {
        final Path path = Path.of(fromFile("userdata/shortcuts.vdf"));

        var result = parser.parse(path, List.of("appid", "AppName"));

        assertThat(result).containsOnlyKeys("shortcuts");
        var shortcut = result.get("shortcuts");
        assertThat(shortcut).hasSize(2);
        assertThat(shortcut.get(0))
            .containsEntry("appid", -1074180434)
            .containsEntry("AppName", "MnM Launcher")
            .containsEntry("tags", Collections.emptyMap());

        assertThat(shortcut.get(1))
            .containsEntry("appid", -185686590)
            .containsEntry("AppName", "MnM")
            .containsEntry("tags", Collections.emptyMap());
    }
}
