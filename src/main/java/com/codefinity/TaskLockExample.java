package com.codefinity;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class TaskLockExample {
    private final Lock lock = new ReentrantLock();
    private final Condition condition = lock.newCondition();
    private int counter = 1; // Initial value of the counter

    public void redLight() {
        lock.lock(); // Acquires the lock
        try {
            while (counter != 1) {
                condition.await(); // Waits until the counter becomes 1
            }
            System.out.println("red light");
            counter++; // Increments the counter
            condition.signalAll(); // Notifies other threads that method1 has completed
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt(); // Restores the interrupted status
        } finally {
            lock.unlock(); // Releases the lock
        }
    }

    public void yellowLight() throws InterruptedException {
        lock.lock(); // Acquires the lock
        try {
            while (counter != 2) {
                condition.await(); // Waits until the counter becomes 2
            }
            System.out.println("yellow light");
            counter++; // Increments the counter
            condition.signalAll(); // Notifies other threads that method2 has completed
        } finally {
            lock.unlock(); // Releases the lock
        }
    }

    public void greenLight() throws InterruptedException {
        lock.lock(); // Acquires the lock
        try {
            while (counter != 3) {
                condition.await(); // Waits until the counter becomes 3
            }
            System.out.println("green light");
            // No need to increment the counter or notify other threads
        } finally {
            lock.unlock(); // Releases the lock
        }
    }
}