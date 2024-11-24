package org.gametools.utilities;

import org.gametools.testing.ClasspathResource;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class VdfParserTest {

    final VdfParser parser = new VdfParser();

    @Test
    void should_parse_libraryfolder_file() {
        final String filePath = ClasspathResource.absolutePath("libraryfolders.vdf");

        final VdfFile file = parser.parse(filePath);

        assertThat(file).isNotNull();
        final Map<String, Object> properties = file.properties();
        assertThat(properties).isNotEmpty();

        final String expectedRootLKey = "libraryfolders";
        assertThat(properties).containsOnlyKeys(expectedRootLKey);
        assertThat((Map) properties.get(expectedRootLKey)).containsOnlyKeys("0", "1");

        final String[] expectedStorageDriveKeys = {
            "apps",
            "contentid",
            "path",
            "time_last_update_verified",
            "totalsize",
            "update_clean_bytes_tally"
        };
        Map<String, Object> app0 = (Map) ((Map) properties.get(expectedRootLKey)).get("0");
        assertThat(app0).containsOnlyKeys(expectedStorageDriveKeys);
        Map<String, Object> app1 = (Map) ((Map) properties.get(expectedRootLKey)).get("1");
        assertThat(app1).containsOnlyKeys(expectedStorageDriveKeys);

        assertThat(((Map<String, Object>) app0.get("apps")).keySet()).allSatisfy(key -> assertThat(key).matches("\\d+"));
        assertThat(((Map<String, Object>) app1.get("apps")).keySet()).allSatisfy(key -> assertThat(key).matches("\\d+"));
    }

    @Test
    void should_parse_appmanifest_file() {
        final String filePath = ClasspathResource.absolutePath("appmanifest_22380.acf");

        final VdfFile file = parser.parse(filePath);

        assertThat(file).isNotNull();
        final Map<String, Object> properties = file.properties();
        assertThat(properties).isNotEmpty();

        final String expectedRootLKey = "AppState";
        assertThat(properties).containsOnlyKeys(expectedRootLKey);

        final String[] expectedAppStateEntryKey = {
            "appid",
            "Universe",
            "name",
            "StateFlags",
            "installdir",
            "LastUpdated",
            "LastPlayed",
            "SizeOnDisk",
            "StagingSize",
            "buildid",
            "LastOwner",
            "UpdateResult",
            "BytesToDownload",
            "BytesDownloaded",
            "BytesToStage",
            "BytesStaged",
            "TargetBuildID",
            "AutoUpdateBehavior",
            "AllowOtherDownloadsWhileRunning",
            "ScheduledAutoUpdate",
            "InstalledDepots",
            "SharedDepots",
            "UserConfig",
            "MountedConfig"
        };
        Map<String, Object> appState = (Map) properties.get(expectedRootLKey);
        assertThat(appState).containsOnlyKeys(expectedAppStateEntryKey);

        assertThat(appState.get("appid")).isEqualTo("22380");
        assertThat(appState.get("name")).isEqualTo("Fallout: New Vegas");
        assertThat(appState.get("installdir")).isEqualTo("Fallout New Vegas");

        // language changes in both when installing Spanish
        final Map<String, String> expectedEntries = Map.of(
            "platform_override_dest", "linux",
            "platform_override_source", "windows",
            "language", "english"
        );
        assertThat((Map<String, String>) appState.get("UserConfig")).containsExactlyInAnyOrderEntriesOf(expectedEntries);
        assertThat((Map<String, String>) appState.get("MountedConfig")).containsExactlyInAnyOrderEntriesOf(expectedEntries);
    }

}
