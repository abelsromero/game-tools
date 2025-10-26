package org.gametools.utilities;// ShortcutsVdfParser.java

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

// https://developer.valvesoftware.com/wiki/Steam_Library_Shortcuts
// https://developer.valvesoftware.com/wiki/Add_Non-Steam_Game#File_format
public class BinaryVdfParser {

    private static final int TYPE_OBJECT = 0x00;  // start nested object (followed by key)
    private static final int TYPE_STRING = 0x01;  // string key -> string value
    private static final int TYPE_INT32 = 0x02;  // int32 key -> int32 value (little endian)
    private static final int TYPE_END = 0x08;  // end of object marker


    public Map<String, List<Map<String, Object>>> parse(Path path) throws IOException {
        return parse(path, null);
    }

    public Map<String, List<Map<String, Object>>> parse(Path path, List<String> properties) throws IOException {

        try (BufferedInputStream bis = new BufferedInputStream(new FileInputStream(path.toFile()))) {
            DataInputStream dis = new DataInputStream(bis);

            if (Files.size(path) == 0) {
                return Collections.emptyMap();
            }

            int firstType = dis.readUnsignedByte();
            if (firstType != TYPE_OBJECT) {
                // some implementations still start with a 0x00; if not, try to continue gracefully
                throw new IOException("Unexpected VDF: expected top-level TYPE_OBJECT (0x00). Got: 0x" +
                    Integer.toHexString(firstType));
            }

            // For shortcuts file, 1 element is enough
            final Map<String, List<Map<String, Object>>> rootElements = new LinkedHashMap<>();

            String rootKey = readNullTerminatedString(dis);
            Map<String, Object> elements = parseObject(dis, properties);

            List<Map<String, Object>> shortcuts = new ArrayList<>();
            for (Map.Entry<String, Object> e : elements.entrySet()) {
                String key = e.getKey();
                if (isNumericKey(key) && e.getValue() instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> entry = (Map<String, Object>) e.getValue();
                    shortcuts.add(entry);
                }
            }

            rootElements.put(rootKey, shortcuts);

            return rootElements;
        }
    }

    private Map<String, Object> parseObject(DataInputStream dis, List<String> properties) throws IOException {
        final Map<String, Object> map = new LinkedHashMap<>();
        while (true) {
            int type;
            try {
                type = dis.readUnsignedByte();
            } catch (EOFException eof) {
                throw new IOException("Unexpected EOF while parsing VDF object", eof);
            }

            if (type == TYPE_END) {
                break;
            }

            if (type == TYPE_OBJECT) {
                String key = readNullTerminatedString(dis);
                Map<String, Object> child = parseObject(dis, properties);
                map.put(key, child);
            } else if (type == TYPE_STRING) {
                String key = readNullTerminatedString(dis);
                String value = readNullTerminatedString(dis);
                if (properties == null || properties.contains(key)) {
                    map.put(key, value);
                }
            } else if (type == TYPE_INT32) {
                String key = readNullTerminatedString(dis);
                int value = readInt32LE(dis);
                if (properties == null || properties.contains(key)) {
                    map.put(key, value);
                }
            } else {
                // Unknown/unsupported type. For minimal parser, try to be resilient:
                String key = readNullTerminatedString(dis);
                // Try to skip an entry: if next is probably a string, read it; else skip 4 bytes
                // But safest is to throw so user knows format is unexpected.
                throw new IOException("Unsupported VDF entry type: 0x" + Integer.toHexString(type) + " at key: " + key);
            }
        }
        return map;
    }

    private boolean isNumericKey(String s) {
        if (s == null || s.isEmpty()) return false;
        for (int i = 0; i < s.length(); i++) {
            if (!Character.isDigit(s.charAt(i))) return false;
        }
        return true;
    }

    private int readInt32LE(DataInputStream dis) throws IOException {
        int b0 = dis.readUnsignedByte();
        int b1 = dis.readUnsignedByte();
        int b2 = dis.readUnsignedByte();
        int b3 = dis.readUnsignedByte();
        return (b0) | (b1 << 8) | (b2 << 16) | (b3 << 24);
    }

    private String readNullTerminatedString(DataInputStream dis) throws IOException {
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int b;
        while ((b = dis.readUnsignedByte()) != 0x00) {
            baos.write(b);
        }
        return baos.toString(StandardCharsets.UTF_8);
    }

}
