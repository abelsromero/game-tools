package org.gametools.testing;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;

public class TestLibraryTemplate {

    private static final String STEAMAPPS = "steamapps";
    private static final String COMPDATA = "compatdata";

    private final Path templatePath;
    private Path target;
    private Path targetCompatdata;

    public TestLibraryTemplate(String templatePath) {
        this.templatePath = Path.of(templatePath);
    }

    public TestLibraryTemplate create() {
        final var tempPath = FileUtils.createTempDir();
        FileUtils.copyFolder(templatePath, tempPath);
        targetCompatdata = tempPath.resolve(STEAMAPPS).resolve(COMPDATA);
        target = tempPath;
        return this;
    }

    private static Path createDirectory(Path tempPath, String name) {
        try {
            return Files.createDirectory(tempPath.resolve(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public TestLibraryTemplate addCompadata(long appId) {
        createDirectory(targetCompatdata, Long.toString(appId));
        return this;
    }

    public TestLibraryTemplate addCompadata(long appId, Integer fileSizeInMb) {
        Path directory = createDirectory(targetCompatdata, Long.toString(appId));

        try {
            Path file = Files.createFile(directory.resolve("fake.file"));
            BufferedWriter bufferedWriter = Files.newBufferedWriter(file);

            long fileSizeInKb = fileSizeInMb * 1024 * 1024;
            int pageSize = 1024 * 4 * 16;
            var page = new char[pageSize];
            long pages = fileSizeInKb / pageSize;

            bufferedWriter.write(fileSizeInMb + "\n");
            for (int i = -1; i < pages; i++) {
                bufferedWriter.write(page);
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public TestLibrary get() {
        return new TestLibrary(target);
    }

    public record TestLibrary(Path path) {

        public String root() {
            return path.toAbsolutePath().toString();
        }

        public Path compatdata() {
            return path.resolve(STEAMAPPS).resolve(COMPDATA);
        }
    }

    private final class FileUtils {

        private static Path createTempDir() {
            try {
                return Files.createTempDirectory("game-tools-");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        private static void copyFolder(Path src, Path dest) {
            try (Stream<Path> stream = Files.walk(src)) {
                stream.forEach(source -> {
                    try {
                        Files.copy(source, dest.resolve(src.relativize(source)), REPLACE_EXISTING);
                    } catch (Exception e) {
                        throw new RuntimeException(e.getMessage(), e);
                    }
                });
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

}
