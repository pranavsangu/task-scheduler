import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class TaskScheduler {

    // Task to be scheduled
    static class Task implements Runnable {
        private final String taskName;

        public Task(String taskName) {
            this.taskName = taskName;
        }

        @Override
        public void run() {
            System.out.println("Executing: " + taskName + " at " + System.currentTimeMillis());
        }
    }

    public static void main(String[] args) {
        // Creating a ScheduledExecutorService with a pool of 2 threads
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        // Schedule tasks with fixed rate
        scheduler.scheduleAtFixedRate(new Task("Task 1"), 0, 5, TimeUnit.SECONDS);
        scheduler.scheduleAtFixedRate(new Task("Task 2"), 2, 10, TimeUnit.SECONDS);

        // Schedule a one-time delayed task
        scheduler.schedule(new Task("One-time Task"), 15, TimeUnit.SECONDS);

        // Adding shutdown hook to gracefully terminate the scheduler
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down scheduler...");
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
            }
        }));
    }
}
