package com.npc;

import com.npc.test.RequestHandler;

import java.io.IOException;
import java.util.concurrent.*;

public class ThreadManagerExample {
    public static void main(String[] args) {
        //ThreadManager threadManager = new ThreadManager(10);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(10, 10, 10L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        //Runnable task = new MyTask(1,"??",null);


        //threadManager.shutdown();
    }
}
