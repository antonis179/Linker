package org.amoustakos.linker.ddos;

import com.mashape.unirest.http.Unirest;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Client {
    private static final int NUMBER_OF_THREADS = 100000;
    private static final int NUMBER_OF_REQUESTS = 100;

    private static final int CORE_POOL = 2000;

    private static ThreadPoolExecutor exec = //(ThreadPoolExecutor) Executors.newCachedThreadPool();
                                                                    new ThreadPoolExecutor(
                                                                            CORE_POOL,
                                                                            NUMBER_OF_THREADS>CORE_POOL?NUMBER_OF_THREADS:CORE_POOL,
                                                                            1000,
                                                                            TimeUnit.MILLISECONDS,
                                                                            new ArrayBlockingQueue<>(NUMBER_OF_THREADS)
                                                                    );


    public static void main(String[] args){
        Unirest.setConcurrency(NUMBER_OF_THREADS, NUMBER_OF_THREADS);
        Long start = System.nanoTime();
        for(int i=0; i<NUMBER_OF_THREADS; i++)
            exec.execute(getRunnableClient(i));


        while(exec.getCompletedTaskCount() < NUMBER_OF_THREADS) {
            Long end = (System.nanoTime() - start)/1000000/1000;
            System.out.println("Completed " + exec.getCompletedTaskCount() + " threads in " + end + "s.");
            System.out.println("Active: " + exec.getPoolSize());
            try{Thread.sleep(3000);} catch(InterruptedException ignored) {}
        }


        Long end = (System.nanoTime() - start)/1000000;
        System.out.println("Completed " + NUMBER_OF_THREADS + " threads in " + end + "ms.");
        System.exit(0);
    }


    private static Runnable getRunnableClient(int iteration){
        return () -> {
            try {
                BlockingClient.doOperation(NUMBER_OF_REQUESTS);
            }catch(Exception e){
                System.out.println("Thread #" + iteration + " failed to execute");
            }
        };
    }



}
