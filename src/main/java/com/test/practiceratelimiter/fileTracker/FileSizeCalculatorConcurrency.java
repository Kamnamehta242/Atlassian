package com.test.practiceratelimiter.fileTracker;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.*;

public class FileSizeCalculatorConcurrency {
    private final Map<String, Long> groupedSizes = new ConcurrentHashMap<>();

    // Define the thread pool size based on available processors
    private final ExecutorService executorService = Executors.newFixedThreadPool(
            Runtime.getRuntime().availableProcessors());

    public void groupFilesByExtension(String directoryPath) throws IOException, InterruptedException {
     System.out.println("00000000000000");
        // Create a list to hold future tasks
        CompletionService<Void> completionService = new ExecutorCompletionService<>(executorService);
        Files.walk(Paths.get(directoryPath))
                .filter(Files::isRegularFile)  // Only consider regular files
                .forEach(filePath -> completionService.submit(() -> {
                    addToGroup(filePath); // Execute file processing in parallel
                    return null; // CompletionService requires a return value
                }));

        // Await completion of all tasks
        for (int i = 0; i < Files.walk(Paths.get(directoryPath)).filter(Files::isRegularFile).count(); i++) {
            completionService.take(); // Blocks until each task is finished
        }

        shutdownExecutor(); // Shut down the thread pool gracefully
    }

    private void addToGroup(Path filePath) {
        String extension = getFileExtension(filePath);
        long size = getFileSize(filePath);
        groupedSizes.merge(extension, size, Long::sum); // Sum the sizes of the files in each group
    }

    private long getFileSize(Path path) {
        try {
            System.out.println("File: " + path + " Size: " + Files.size(path));
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

    // Gracefully shuts down the ExecutorService
    private void shutdownExecutor() throws InterruptedException {
        executorService.shutdown();
        if (!executorService.awaitTermination(60, TimeUnit.SECONDS)) {
            executorService.shutdownNow(); // Force shutdown if not terminated after timeout
        }
    }

}
