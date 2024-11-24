package org.gametools.utilities;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * https://developer.valvesoftware.com/wiki/VDF
 */
public class VdfParser {

    public VdfFile parse(String filePath) {

        final Stack<Entry> parents = new Stack<>();
        Entry currentEntry = null;

        try (BufferedReader br = buildBufferedReader(filePath)) {
            String line;
            int lineIndex = 0;

            while ((line = br.readLine()) != null) {
                line = line.trim();
//                System.out.println("Line " + lineIndex + ": " + line);

                // base case
                if (lineIndex == 0) {
                    currentEntry = new Entry();
                    currentEntry.key = stripQuotes(line);
                    lineIndex++;
                    continue;
                }

                if (line.startsWith("{")) {
                    continue;
                } else if (line.startsWith("}")) {
                    if (parents.isEmpty()) {
                        // root
                        return new VdfFile(Map.of(currentEntry.key, currentEntry.properties));
                    } else {
                        Entry parent = parents.pop();
                        parent.properties.put(currentEntry.key, currentEntry.properties);
                        currentEntry = parent;
                    }
                } else {
                    if (isEntryKey(line)) {
                        parents.push(currentEntry);
                        currentEntry = new Entry();
                        currentEntry.key = stripQuotes(line);
                    } else {
                        Pair split = splitQuotes(line);
                        if (!split.value().isEmpty())
                            currentEntry.properties.put(split.key(), split.value());
                    }
                }
                lineIndex++;
            }
            return null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static BufferedReader buildBufferedReader(String filePath) {
        return new BufferedReader(buildReader(filePath));
    }

    private static Reader buildReader(String filePath) {
        try {
            if (filePath.contains(".jar!/")) {
                String path = filePath.startsWith("jar:") ? filePath : "jar:" + filePath;
                return new InputStreamReader(url(path).openStream());
            } else {
                return new FileReader(filePath);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static URL url(String filePath) {
        try {
            return new URI(filePath).toURL();
        } catch (URISyntaxException | MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    private static Pair splitQuotes(String line) {
        int startKey = 1;
        int endKey = 1;
        int startValue = 1;
        int endValue = line.length() - 1;

        int found = 0;

        int index = 0;
        for (char ch : line.toCharArray()) {
            if (ch == '"') {
                found++;
                switch (found) {
                    case 2:
                        endKey = index;
                        break;
                    case 3:
                        startValue = index + 1;
                        break;
                }
            }
            index++;
        }

        return new Pair(line.substring(startKey, endKey), line.substring(startValue, endValue));
    }

    private static class Entry {
        String key;
        Map<String, Object> properties = new HashMap<>();
    }

    private static boolean isEntryKey(String line) {
        int found = 0;
        for (char ch : line.toCharArray()) {
            if (ch == '"') found++;
        }
        return found == 2;
    }

    private static String stripQuotes(String line) {
        return line.length() == 2 ? "" : line.substring(1, line.length() - 1);
    }

}
