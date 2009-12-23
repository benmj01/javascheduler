package ru.scheduler;

import org.apache.log4j.Logger;

import java.util.*;

/**
 * Created by IntelliJ IDEA.
 * User: ALeX
 * Date: 18.12.2009
 * Time: 14:17:54
 */
public class Scheduler extends Observable {
    private static final Logger log = Logger.getLogger(Scheduler.class);
    private final PriorityQueue<Task> queue;
    private final Thread thread;
    private final Executor executor;
    private static Scheduler instance;
    private boolean changed;
    private List<Task> snapshot;


    private class Executor implements Runnable {
        private boolean stop = false;

        public void run() {
            log.info("executor started");
            while (!stop) {
                synchronized (queue) {
                    if (queue.isEmpty()) {
                        log.info("task queue is empty, waiting...");
                        try {
                            queue.wait();
                        } catch (InterruptedException e) {
                            //do nothing
                        }
                    }
                    if (!queue.isEmpty()) {
                        Task task = queue.peek();
                        if (task.getDate().compareTo(new Date()) <= 0) {
                            log.info("execute task");
                            try {
                                queue.poll().execute();
                                setChanged();
                                notifyObservers();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {

                            long wait = task.getDate().getTime() - System.currentTimeMillis();
                            log.info("wait task: " + wait + " millis");
                            try {
                                queue.wait(wait);
                            } catch (InterruptedException e) {
                                //do nothing
                            }
                        }
                    }
                }
            }
            log.info("executor exited");
        }

        public void stop() {
            stop = true;
        }
    }

    private Scheduler() {
        queue = new PriorityQueue<Task>();
        executor = new Executor();
        thread = new Thread(executor);
        thread.start();
    }

    public static Scheduler getInstance() {
        if (instance == null) {
            instance = new Scheduler();
        }
        return instance;
    }

    public void addTask(Task task) {
        synchronized (queue) {
            queue.add(task);
            queue.notify();
        }
        changed = true;
        setChanged();
        notifyObservers();
    }

    public void removeTask(Task task) {
        synchronized (queue) {
            queue.remove(task);
            queue.notify();
        }
        changed = true;
        setChanged();
        notifyObservers();
    }

    public void stop() {
        executor.stop();
        thread.interrupt();
        log.info("scheduller stopped");
    }

    public List<Task> getTasks() {
        if (changed) {
            synchronized (queue) {
                snapshot = new ArrayList<Task>(queue.size());
                for (Task t : queue) {
                    snapshot.add(t);
                }
            }
        }
        return snapshot;
    }

    public int getTaskCount() {
        return queue.size();
    }
}
