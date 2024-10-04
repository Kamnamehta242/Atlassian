package com.test.practiceratelimiter.eventSchduler;

import com.test.practiceratelimiter.eventSchduler.EventScheduler;

import java.time.LocalDateTime;

public class EventSchedulerDemo {
    public static void main(String[] args) throws InterruptedException {
        EventScheduler scheduler = new EventScheduler();

        // Schedule some events
        scheduler.scheduleEvent("1", LocalDateTime.now().plusSeconds(5), "Event 1");
        scheduler.scheduleEvent("2", LocalDateTime.now().plusSeconds(10), "Event 2");
        scheduler.scheduleEvent("3", LocalDateTime.now().plusSeconds(3), "Event 3");

        // Wait for a while before exiting
        Thread.sleep(15000); // Give enough time for all events to trigger
    }
}
