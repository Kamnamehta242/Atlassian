package com.test.practiceratelimiter.Schduler;

import java.net.URI;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.concurrent.*;

public class Scheduler {
    private final ScheduledExecutorService executorService;
    private final BlockingQueue<ScheduledTask> taskQueue;

    public Scheduler(int poolSize) {
        this.executorService = Executors.newScheduledThreadPool(poolSize);
        this.taskQueue = new LinkedBlockingQueue<>();
    }

    public void schedule(URI uri, LocalDateTime executeAt) throws InterruptedException {
        ScheduledTask task = new ScheduledTask(uri, executeAt);
        long delay = calculateDelay(executeAt);
        
        if (delay < 0) {
            System.out.println("Cannot schedule a task in the past.");
            return;
        }

        executorService.schedule(() -> {
            try {
                task.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, delay, TimeUnit.MILLISECONDS);

        // Rate Limiting
        taskQueue.put(task);
        if (taskQueue.size() > 10) {
            System.out.println("Too many scheduled tasks! Please wait.");
            taskQueue.take(); // Remove the oldest task if limit exceeds
        }
    }

    private long calculateDelay(LocalDateTime executeAt) {
        return Duration.between(LocalDateTime.now(), executeAt).toMillis();
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
