package com.test.practiceratelimiter.Schduler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;

public class ScheduledTask {
    private final URI uri;
    private final LocalDateTime executeAt;

    public ScheduledTask(URI uri, LocalDateTime executeAt) {
        this.uri = uri;
        this.executeAt = executeAt;
    }

    public URI getUri() {
        return uri;
    }

    public LocalDateTime getExecuteAt() {
        return executeAt;
    }

    public void execute() throws Exception {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .POST(HttpRequest.BodyPublishers.noBody()) // Change method and body as needed
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println("Executed request to " + uri + ", response: " + response.body());
    }
}
