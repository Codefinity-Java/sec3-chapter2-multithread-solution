import com.codefinity.TaskLockExample;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskLockExampleTest {

    private TaskLockExample taskLockExample;
    private BlockingQueue<String> executionOrder;

    @BeforeEach
    void setUp() {
        taskLockExample = new TaskLockExample();
        executionOrder = new LinkedBlockingQueue<>();
    }

    @RepeatedTest(3)
    void testMethodExecutionOrder() throws InterruptedException {
        // Поток для method1
        Thread t1 = new Thread(() -> {
            taskLockExample.redLight();
            executionOrder.offer("method1");
        });

        // Поток для method2
        Thread t2 = new Thread(() -> {
            try {
                taskLockExample.yellowLight();
                executionOrder.offer("method2");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Поток для method3
        Thread t3 = new Thread(() -> {
            try {
                taskLockExample.greenLight();
                executionOrder.offer("method3");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        // Запуск потоков
        t3.start();
        t2.start();
        t1.start();

        // Ждем завершения всех потоков
        t3.join();
        t2.join();
        t1.join();

        // Проверяем правильный порядок выполнения
        assertEquals("method1", executionOrder.poll());
        assertEquals("method2", executionOrder.poll());
        assertEquals("method3", executionOrder.poll());
    }
}