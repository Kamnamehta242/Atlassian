package com.test.practiceratelimiter.fileTracker;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileSizeCalculator {

    private final Map<String, Long> groupedSizes = new HashMap<>();

    public void groupFilesByExtension(String directoryPath) throws IOException {
        Files.walk(Paths.get(directoryPath))
                .filter(Files::isRegularFile) // Only consider regular files
                .forEach(this::addToGroup);
    }

    private void addToGroup(Path filePath) {
        String extension = getFileExtension(filePath);
        long size = getFileSize(filePath);
        groupedSizes.merge(extension, size, Long::sum); // Sum the sizes of the files in each group
    }

    private long getFileSize(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            System.err.println("Unable to access file: " + path);
            return 0; // Return 0 if file cannot be accessed
        }
    }

    private String getFileExtension(Path filePath) {
        String fileName = filePath.getFileName().toString();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex > 0) ? fileName.substring(dotIndex) : "no_extension"; // Group files without extensions
    }

    public Map<String, Long> getGroupedSizes() {
        return groupedSizes;
    }
}
