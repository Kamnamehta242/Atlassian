package com.test.practiceratelimiter.Schduler;

import java.net.URI;
import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scheduler scheduler = new Scheduler(10); // Pool size

        // Example usage
        scheduler.schedule(URI.create("https://testservice.com/api/items/_create"), 
                LocalDateTime.now().plusMinutes(1)); // Schedule to run 1 minute from now
        
        // Keep the application running to observe scheduled executions
        Thread.sleep(10000);
        scheduler.shutdown();
    }
}
