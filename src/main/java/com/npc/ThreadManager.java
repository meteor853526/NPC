package com.npc;

import java.util.concurrent.*;

public class ThreadManager {
    private final ThreadPoolExecutor executorService;


    public ThreadManager(ThreadPoolExecutor ThreadPool) {
        this.executorService  = ThreadPool;

    }

    public void execute(Runnable task) {
        executorService.submit(task);
    }

    public int getActCount(){
        return executorService.getActiveCount();
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
