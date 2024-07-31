package com.codefinity;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        TaskLockExample taskLockExample = new TaskLockExample();

        Thread t1 = new Thread(() -> {
            try {
                taskLockExample.method3();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        Thread t2 = new Thread(taskLockExample::method1);

        Thread t3 = new Thread(() -> {
            try {
                taskLockExample.method2();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        t2.start();
        t3.start();
        t1.start();

        t3.join();
        t1.join();
        t2.join();


    }
}
