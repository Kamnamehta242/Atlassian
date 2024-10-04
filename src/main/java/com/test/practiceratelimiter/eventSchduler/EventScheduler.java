package com.test.practiceratelimiter.eventSchduler;

import java.time.LocalDateTime;
import java.util.PriorityQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EventScheduler {
    private PriorityQueue<Event> eventQueue = new PriorityQueue<>();
    private ConcurrentHashMap<String, Event> eventMap = new ConcurrentHashMap<>(); // For cancellation
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    public EventScheduler() {
        startEventProcessing();
    }

    public void scheduleEvent(String id, LocalDateTime executeAt, String payload) {
        Event event = new Event(id, executeAt, payload);
        synchronized (eventQueue) {
            eventQueue.offer(event);
            eventMap.put(id, event);
            eventQueue.notify(); // Notify the worker thread
        }
    }

    public void cancelEvent(String id) {
        eventMap.remove(id);
        // Since we cannot remove an item from a heap directly, 
        // we will mark it as canceled and skip it during execution
    }

    private void startEventProcessing() {
        executorService.submit(() -> {
            while (true) {
                Event nextEvent = null;
                synchronized (eventQueue) {
                    while (eventQueue.isEmpty()) {
                        eventQueue.wait(); // Wait for an event to be scheduled
                    }
                    nextEvent = eventQueue.peek();
                }
                if (nextEvent != null) {
                    LocalDateTime now = LocalDateTime.now();
                    if (now.isAfter(nextEvent.getExecuteAt())) {
                        synchronized (eventQueue) {
                            eventQueue.poll(); // Remove the event
                            if (eventMap.containsKey(nextEvent.getId())) {
                                eventMap.remove(nextEvent.getId()); // Remove from the map
                                triggerEvent(nextEvent);
                            }
                        }
                    } else {
                        // Sleep until the next event time
                        try {
                            Thread.sleep(nextEvent.getExecuteAt().getSecond() * 1000L);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                    }
                }
            }
        });
    }

    private void triggerEvent(Event event) {
        // Implement the actual event triggering logic here
        System.out.println("Triggering event: " + event.getId() + ", Payload: " + event.getPayload());
    }
}
