package org.gametools.utilities;// ShortcutsVdfParser.java

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ShortcutsReader {

    private final BinaryVdfParser binaryVdfParser = new BinaryVdfParser();

    public ShortcutsReader() {}

    public List<Shortcut> read(Path path) {
        try {
            final Path configPath = path.resolve("shortcuts.vdf");

            if (!Files.exists(configPath)) {
                return Collections.emptyList();
            }

            final List<String> requiredProperties = List.of("appid", "AppName", "LaunchOptions");
            List<Map<String, Object>> maps = binaryVdfParser.parse(configPath, requiredProperties).get("shortcuts");

            if (maps == null) {
                return Collections.emptyList();
            }

            return maps
                .stream()
                .map(entry -> {
                    int appId = (int) entry.get("appid");
                    String appName = entry.get("AppName").toString();
                    String launchOptions = entry.get("LaunchOptions").toString();
                    return new Shortcut(appId, appName, launchOptions);
                })
                .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
