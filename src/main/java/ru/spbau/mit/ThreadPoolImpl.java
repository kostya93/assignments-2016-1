package ru.spbau.mit;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;
import java.util.function.Supplier;

public class ThreadPoolImpl implements ThreadPool {

    private final Queue<LightFutureImpl> lightFutureQueue;
    private final List<Thread> threadList;
    private volatile boolean isShutdown;

    public ThreadPoolImpl(int n) {
        isShutdown = false;
        lightFutureQueue = new LinkedList<>();
        threadList = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            Thread thread = new Thread(this::makeWorker);
            thread.start();
            threadList.add(thread);
        }
    }

    public int numberOfThreads() {
        int threads = 0;
        for (Thread t : threadList) {
            if (t.isAlive()) {
                threads++;
            }
        }
        return threads;
    }

    private void makeWorker() {
        try {
            while (!Thread.interrupted()) {
                LightFutureImpl f;
                synchronized (lightFutureQueue) {
                    while (lightFutureQueue.isEmpty()) {
                        lightFutureQueue.wait();
                    }
                    f = lightFutureQueue.poll();
                    lightFutureQueue.notify();
                }
                f.eval();
            }
        } catch (InterruptedException ignored) { }
    }


    private final class LightFutureImpl<R> implements LightFuture<R> {
        private volatile boolean isReady;
        private volatile R res;
        private final Supplier<R> supplier;
        private LightExecutionException lightExecutionException;
        private volatile boolean isInterrupted;
        private final Object notReady;
        private LightFutureImpl depend;


        private LightFutureImpl(Supplier<R> supplier) {
            this.supplier = supplier;
            isReady = false;
            res = null;
            lightExecutionException = null;
            notReady = new Object();
            depend = null;
        }

        public void setDepend(LightFutureImpl future) {
            depend = future;
        }

        @Override
        public boolean isReady() {
            return isReady;
        }

        @Override
        public R get() throws LightExecutionException, InterruptedException {
            if (res == null && lightExecutionException == null && !isInterrupted) {
                synchronized (notReady) {
                    while (res == null && lightExecutionException == null && !isInterrupted) {
                        notReady.wait();
                    }
                }
            }

            if (isInterrupted && res == null) {
                throw new LightExecutionException(new Exception());
            }

            if (lightExecutionException != null) {
                throw lightExecutionException;
            }

            return res;
        }

        @Override
        public <U> LightFuture<U> thenApply(Function<? super R, ? extends U> f) {
            Supplier<U> supplier = () -> f.apply(res);
            LightFutureImpl<U> lightFuture = new LightFutureImpl<>(supplier);
            lightFuture.setDepend(this);
            synchronized (lightFutureQueue) {
                lightFutureQueue.add(lightFuture);
                lightFutureQueue.notify();
            }
            return lightFuture;
        }

        void eval() {
            synchronized (notReady) {
                if (isReady) {
                    notReady.notifyAll();
                    return;
                }

                if (depend != null) {
                    depend.eval();
                }

                try {
                    res = supplier.get();
                } catch (Exception e) {
                    lightExecutionException = new LightExecutionException(e);
                }

                isReady = true;
                notReady.notifyAll();
            }
        }

        public void setInterrupted() {
            synchronized (notReady) {
                isInterrupted = true;
                notReady.notifyAll();
            }
        }
    }




    @Override
    public <R> LightFuture<R> submit(Supplier<R> supplier) {
        if (isShutdown) {
            return null;
        }

        LightFutureImpl<R> future = new LightFutureImpl<>(supplier);
        synchronized (lightFutureQueue) {
            lightFutureQueue.add(future);
            lightFutureQueue.notify();
        }
        return future;
    }

    @Override
    public void shutdown() {
        isShutdown = true;
        threadList.forEach(Thread::interrupt);
        synchronized (lightFutureQueue) {
            lightFutureQueue.forEach(LightFutureImpl::setInterrupted);
            lightFutureQueue.notify();
        }
        threadList.clear();
    }
}
