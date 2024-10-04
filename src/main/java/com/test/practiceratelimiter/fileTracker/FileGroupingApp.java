package com.test.practiceratelimiter.fileTracker;

import java.io.IOException;
import java.util.Scanner;

public class FileGroupingApp {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        FileSizeCalculator calculator = new FileSizeCalculator();

        System.out.println("Enter directory to group files by extension (e.g., /path/to/directory): ");
        String directoryPath = scanner.nextLine();

        try {
            calculator.groupFilesByExtension(directoryPath);
            System.out.println("Grouped Sizes by File Extension: " + calculator.getGroupedSizes());
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
