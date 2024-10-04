package com.test.practiceratelimiter.fileTracker;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FileSizeCalculatorTest {

        private FileSizeCalculatorConcurrency calculator;
        private Path tempDirectory;

        @BeforeEach
        public void setUp() throws IOException {
            calculator = new FileSizeCalculatorConcurrency();
            // Create a temporary directory for testing
            tempDirectory = Files.createTempDirectory("fileSizeTest");
            // Create test files with some content
            Files.write(tempDirectory.resolve("file1.txt"), "Hello World".getBytes()); // 11 bytes
            Files.write(tempDirectory.resolve("file2.txt"), "Java Programming".getBytes()); // 17 bytes
            Files.write(tempDirectory.resolve("file3.pdf"), "PDF Content".getBytes()); // 12 bytes
        }

        @AfterEach
        public void tearDown() throws IOException {
            // Delete the temporary directory and its content
            Files.walk(tempDirectory)
                    .sorted(Comparator.reverseOrder()) // Delete files first, then the directory
                    .forEach(file -> {
                        try {
                            Files.delete(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
        }

    @Test
    public void testGroupFilesByExtension_normalFiles() throws IOException, InterruptedException {
        // Arrange: Create test files
//        Files.createFile(tempDirectory.resolve("file1.txt"));
//        Files.createFile(tempDirectory.resolve("file2.txt"));
//        Files.createFile(tempDirectory.resolve("file3.pdf"));

        // Act: Run the file size calculation
        calculator.groupFilesByExtension(tempDirectory.toString());

        // Assert: Verify the grouped sizes
        Map<String, Long> groupedSizes = calculator.getGroupedSizes();
        assertNotNull(groupedSizes.get(".txt"));
        assertEquals(27, groupedSizes.get(".txt"));  // Total size of .txt files (11 + 17 bytes)
        assertNotNull(groupedSizes.get(".pdf"));
        assertEquals(11, groupedSizes.get(".pdf"));  // Size of .pdf file
    }

        @Test
        public void testGroupFilesByExtension_emptyDirectory() throws IOException, InterruptedException {
            // Act: Run the file size calculation on an empty directory
            calculator.groupFilesByExtension(tempDirectory.toString());

            // Assert: Ensure no sizes are grouped
            Map<String, Long> groupedSizes = calculator.getGroupedSizes();
            assertTrue(groupedSizes.isEmpty());
        }

        @Test
        public void testGroupFilesByExtension_filesWithoutExtensions() throws IOException, InterruptedException {
            // Arrange: Create a file without extension
            Files.createFile(tempDirectory.resolve("file1"));

            // Act: Run the file size calculation
            calculator.groupFilesByExtension(tempDirectory.toString());

            // Assert: Check that files without extensions are grouped under "no_extension"
            Map<String, Long> groupedSizes = calculator.getGroupedSizes();
            assertTrue(groupedSizes.containsKey("no_extension"));
            assertEquals(1, groupedSizes.get("no_extension")); // 1 file with no extension
        }

        @Test
        public void testGroupFilesByExtension_ioExceptionHandling() throws IOException, InterruptedException {
            // Arrange: Create an unreadable file (simulating an IOException)
            Path unreadableFile = tempDirectory.resolve("unreadable.txt");
            Files.createFile(unreadableFile);
            unreadableFile.toFile().setReadable(false);

            // Act: Run the file size calculation
            calculator.groupFilesByExtension(tempDirectory.toString());

            // Assert: Ensure the file is skipped (size of 0 for unreadable file)
            Map<String, Long> groupedSizes = calculator.getGroupedSizes();
            assertNull(groupedSizes.get(".txt"));  // .txt shouldn't be included since the file couldn't be read
        }

        @Test
        public void testGroupFilesByExtension_largeNumberOfFiles() throws IOException, InterruptedException {
            // Arrange: Create a large number of files
            for (int i = 0; i < 1000; i++) {
                Files.createFile(tempDirectory.resolve("file" + i + ".txt"));
            }

            // Act: Run the file size calculation
            calculator.groupFilesByExtension(tempDirectory.toString());

            // Assert: Ensure all files are correctly grouped
            Map<String, Long> groupedSizes = calculator.getGroupedSizes();
            assertEquals(1000, groupedSizes.get(".txt"));
        }

        @Test
        public void testGroupFilesByExtension_mixedExtensions() throws IOException, InterruptedException {
            // Arrange: Create files with different extensions
            Files.createFile(tempDirectory.resolve("file1.txt"));
            Files.createFile(tempDirectory.resolve("file2.pdf"));
            Files.createFile(tempDirectory.resolve("file3.jpeg"));

            // Act: Run the file size calculation
            calculator.groupFilesByExtension(tempDirectory.toString());

            // Assert: Verify that different extensions are grouped correctly
            Map<String, Long> groupedSizes = calculator.getGroupedSizes();
            assertEquals(1, groupedSizes.get(".txt"));
            assertEquals(1, groupedSizes.get(".pdf"));
            assertEquals(1, groupedSizes.get(".jpeg"));
        }
    }
