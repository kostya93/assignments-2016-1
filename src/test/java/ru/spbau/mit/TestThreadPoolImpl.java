package ru.spbau.mit;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


public class TestThreadPoolImpl {

    private static final int POOL_SIZE = 10;
    private static final int NUMBER_OF_TASKS = 100;
    private static final ThreadPool THREAD_POOL = new ThreadPoolImpl(POOL_SIZE);

    @Test
    public void testSimple() throws LightExecutionException, InterruptedException {
        LightFuture<Integer> lf1 = THREAD_POOL.submit(() -> 1);
        LightFuture<Integer> lf2 = THREAD_POOL.submit(() -> 2);
        assertEquals(new Integer(1), lf1.get());
        assertEquals(new Integer(2), lf2.get());
    }

    @Test
    public void testNumberOfThreads() throws InterruptedException {
        ThreadPool threadPool = new ThreadPoolImpl(POOL_SIZE);
        final boolean[] flag = {false};
        CyclicBarrier cyclicBarrier = new CyclicBarrier(POOL_SIZE + 1, () -> flag[0] = true);
        for (int i = 0; i < NUMBER_OF_TASKS; i++) {
            threadPool.submit(() -> {
                try {
                    cyclicBarrier.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                }
                return 0;
            });
        }
        Thread.sleep(100);
        assertFalse(flag[0]);
        threadPool.shutdown();

        ThreadPool threadPool2 = new ThreadPoolImpl(POOL_SIZE);
        flag[0] = false;
        CyclicBarrier cyclicBarrier2 = new CyclicBarrier(POOL_SIZE, () -> flag[0] = true);
        for (int i = 0; i < NUMBER_OF_TASKS; i++) {
            threadPool2.submit(() -> {
                try {
                    cyclicBarrier2.await();
                } catch (InterruptedException | BrokenBarrierException e) {
                    Thread.currentThread().interrupt();
                }
                return 0;
            });
        }
        Thread.sleep(100);
        assertTrue(flag[0]);
        threadPool2.shutdown();
    }

    @Test
    public void testThenApply() throws LightExecutionException, InterruptedException {
        List<LightFuture<Integer>> futureList = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_TASKS; i++) {
            final int finalI = i;
            LightFuture<Integer> lightFuture = THREAD_POOL.submit(() -> finalI);
            futureList.add(lightFuture);
            lightFuture = lightFuture.thenApply(res -> res * 2);
            futureList.add(lightFuture);
        }
        for (int i = 0; i < NUMBER_OF_TASKS; i++) {
            int ind = 2 * i;
            Integer r1 = futureList.get(ind).get();
            Integer r2 = futureList.get(ind + 1).get();
            assertEquals(r1, new Integer(i));
            assertEquals(r2, new Integer(i * 2));
        }
    }

    @Test
    public void testShutdown() {
        ThreadPool threadPool = new ThreadPoolImpl(POOL_SIZE);
        for (int i = 0; i < NUMBER_OF_TASKS; i++) {
            threadPool.submit(() -> 1);
        }
        threadPool.shutdown();
        for (int i = 0; i < NUMBER_OF_TASKS; i++) {
            assertTrue(threadPool.submit(()->1) == null);
        }
        assertEquals(((ThreadPoolImpl) threadPool).numberOfThreads(), 0);
    }
}
