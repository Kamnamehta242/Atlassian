package com.test.practiceratelimiter.fileTracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class FileSizeTracker {
    private final Map<String, Long> directorySizes = new HashMap<>();

    public void trackDirectory(String directoryPath) throws IOException {
        Path path = Paths.get(directoryPath);
        long size = calculateDirectorySize(path);
        directorySizes.put(directoryPath, size);
    }

    private long calculateDirectorySize(Path directory) throws IOException {
        return Files.walk(directory)
                .filter(Files::isRegularFile) // Only consider regular files
                .mapToLong(this::getFileSize)
                .sum(); // Sum the sizes of the files
    }

    private long getFileSize(Path path) {
        try {
            return Files.size(path);
        } catch (IOException e) {
            System.err.println("Unable to access file: " + path);
            return 0; // Return 0 if file cannot be accessed
        }
    }

    public Map<String, Long> getDirectorySizes() {
        return directorySizes;
    }
}
