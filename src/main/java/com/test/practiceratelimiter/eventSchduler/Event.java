package com.test.practiceratelimiter.eventSchduler;

import java.time.LocalDateTime;

public class Event implements Comparable<Event> {
    private String id; // Unique identifier for the event
    private LocalDateTime executeAt; // When the event should be executed
    private String payload; // Data associated with the event

    public Event(String id, LocalDateTime executeAt, String payload) {
        this.id = id;
        this.executeAt = executeAt;
        this.payload = payload;
    }

    public LocalDateTime getExecuteAt() {
        return executeAt;
    }

    public String getId() {
        return id;
    }

    public String getPayload() {
        return payload;
    }

    @Override
    public int compareTo(Event other) {
        return this.executeAt.compareTo(other.executeAt);
    }
}
