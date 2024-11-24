package org.gametools.utilities;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * https://developer.valvesoftware.com/wiki/VDF
 */
public class VdfParser {

    public VdfFile parse(String filePath) throws IOException {

        final Stack<Entry> parents = new Stack<>();
        Entry currentEntry = null;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
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
                    // nothing to do
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
                        if (split.value().length() != 0)
                            currentEntry.properties.put(split.key(), split.value());
                    }
                }
                lineIndex++;
            }
            return null;
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

    private class Entry {
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
