package de.sesosas.simpletablist.classes.scheduler;

import java.util.HashMap;
import java.util.concurrent.*;

public class Scheduler {

    private final ScheduledExecutorService threadPool;
    private final HashMap<String, ScheduledFuture<?>> schedulers = new HashMap<>();

    public Scheduler(int poolSize) {
        this.threadPool = Executors.newScheduledThreadPool(poolSize);
    }

    public ScheduledExecutorService getThreadPool() {
        return this.threadPool;
    }

    public void registerSchedule(String name, Runnable runnable, long period, TimeUnit timeUnit) {
        ScheduledFuture<?> future = threadPool.scheduleAtFixedRate(runnable, 0, period, timeUnit);
        schedulers.put(name, future);
    }

    public void cancelSchedule(String name) {
        if (this.schedulers.containsKey(name)) return;

        ScheduledFuture<?> schedule = this.schedulers.get(name);
        schedule.cancel(true);
    }
}