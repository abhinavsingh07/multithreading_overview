package MultithreadedService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

// TODO: put this source code file into a new Java package with meaningful name (e.g., dv512.YourStudentID)!
// You can implement additional fields and methods in code below, but
// you are not allowed to rename or remove any of it!
// Additionally, please remember that you are not allowed to use any third-party libraries
public class MultithreadedService {

    // TODO: implement a nested public class titled Task here
    // which must have an integer ID and specified burst time (duration) in milliseconds,
    // see below
    // Add further fields and methods to it, if necessary
    // As the task is being executed for the specified burst time, 
    // it is expected to simply go to sleep every X milliseconds (specified below)
    // Random number generator that must be used for the simulation
    Random rng;
    //custom added
    ConcurrentHashMap<Integer, Task> threadDetails = new ConcurrentHashMap<Integer, Task>();
    
    //Our Task class
    class Task implements Runnable {

        private int id;
        private String startTime;
        private String endTime;
        private long burstTime;
        private long sleepTime;
        private String threadState = "WAITING";

        public Task(int id, long burstTime, long sleepTime) {
            this.id = id;
            this.burstTime = burstTime;
            this.sleepTime = sleepTime;
            threadDetails.put(id, this);
        }

        public String getThreadState() {
            return threadState;
        }

        @Override
        public String toString() {
            return "Task{" + "ID=" + id + ", BurstTime=" + burstTime + ", Start Time=" + startTime + ", Finish Time=" + endTime + '}';
        }

        @Override
        public void run() {
            SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss z");
            Date date = new Date(System.currentTimeMillis());
            long taskSleep = this.burstTime * this.sleepTime;
            Thread.currentThread().setName("ID " + this.id);
            this.startTime = formatter.format(date);
            this.threadState = "RUN";

            try {

                Thread.sleep(taskSleep);
                date = new Date(System.currentTimeMillis());
                this.endTime = formatter.format(date);
            } catch (InterruptedException ex) {
                this.threadState = "INTERRUPTED";
            }
            
            threadDetails.put(id, this);
        }

    }

    // ... add further fields, methods, and even classes, if necessary
    public MultithreadedService(long rngSeed) {
        this.rng = new Random(rngSeed);
    }

    public void reset() {
        // TODO - remove any information from the previous simulation, if necessary
    }

    // If the implementation requires your code to throw some exceptions, 
    // you are allowed to add those to the signature of this method
    public void runNewSimulation(final long totalSimulationTimeMs,
            final int numThreads, final int numTasks,
            final long minBurstTimeMs, final long maxBurstTimeMs, final long sleepTimeMs) {
//        reset();

        // TODO:
        // 1. Run the simulation for the specified time, totalSimulationTimeMs
        // 2. While the simulation is running, use a fixed thread pool with numThreads
        // (see https://docs.oracle.com/en/java/javase/11/docs/api/java.base/java/util/concurrent/Executors.html#newFixedThreadPool(int) )
        // to execute Tasks (implement the respective class, see above!)
        // 3. The total maximum number of tasks is numTasks, 
        // and each task has a burst time (duration) selected randomly
        // between minBurstTimeMs and maxBurstTimeMs (inclusive)
        // 4. The implementation should assign sequential task IDs to the created tasks (0, 1, 2...)
        // and it should assign them to threads in the same sequence (rather any other scheduling approach)
        // 5. When the simulation time is up, it should make sure to stop all of the currently executing
        // and waiting threads!
        //creating thread pool
        ExecutorService exs = Executors.newFixedThreadPool(numThreads);
        long startTime = System.currentTimeMillis();
        //assigning task
        for (int i = 0; i < numTasks; i++) {
            //calculating random bursttime in ms inclusive of range
            long randBurstTimeMs = calcRandomNo(minBurstTimeMs, maxBurstTimeMs) * 1000;
            exs.submit(new Task(i, randBurstTimeMs, sleepTimeMs));
        }
        // finding the time after the operation is executed
        long endTime = System.currentTimeMillis();
        //Stopping all tasks running,waiting
        while (true) {
            endTime = System.currentTimeMillis();
            long totalTimeSec = (endTime - startTime) / 1000;
            long totalSimulationTimeSec = totalSimulationTimeMs / 1000;
            if (totalSimulationTimeSec == totalTimeSec) {
                exs.shutdownNow();
                break;
            }

        }

    }
    /**
     * Calculate random number in a range
     * @param n1 min no
     * @param n2 max no
     * @return  random number
     */
    public long calcRandomNo(long n1, long n2) {
        n1 = n1 / 1000;
        n2 = n2 / 1000;
        return ThreadLocalRandom.current().nextInt((int) n1, (int) n2 + 1);
    }

    public void printResults() {
        // TODO:
        System.out.println("Completed tasks:");
        // 1. For each *completed* task, print its ID, burst time (duration),
        // its start time (moment since the start of the simulation), and finish time
        threadDetails.entrySet()
                .stream()
                .filter(item -> (item.getValue().getThreadState().equals("RUN")))
                .forEach(System.out::println);

        System.out.println("Interrupted tasks:");
        // 2. Afterwards, print the list of tasks IDs for the tasks which were currently
        // executing when the simulation was finished/interrupted
        threadDetails.entrySet()
                .stream()
                .filter(item -> (item.getValue().getThreadState().equals("INTERRUPTED")))
                .forEach(System.out::println);
        System.out.println("Waiting tasks:");
        // 3. Finally, print the list of tasks IDs for the tasks which were waiting for execution,
        // but were never started as the simulation was finished/interrupted
        threadDetails.entrySet()
                .stream()
                .filter(item -> (item.getValue().getThreadState().equals("WAITING")))
                .forEach(System.out::println);
    }

    // If the implementation requires your code to throw some exceptions, 
    // you are allowed to add those to the signature of this method
    public static void main(String args[]) {
        // TODO: replace the seed value below with your birth date, e.g., "20001001"
        final long rngSeed = 00000000;

        // Do not modify the code below — instead, complete the implementation
        // of other methods!
        MultithreadedService service = new MultithreadedService(rngSeed);

        final int numSimulations = 3;//3
        final long totalSimulationTimeMs = 20 * 1000L; // 15 seconds

        final int numThreads = 4;
        final int numTasks = 30;//30
        final long minBurstTimeMs = 1 * 1000L; // 1 second  
        final long maxBurstTimeMs = 10 * 1000L; // 10 seconds
        final long sleepTimeMs = 100L; // 100 ms

        for (int i = 0; i < numSimulations; i++) {
            System.out.println("Running simulation #" + i);

            service.runNewSimulation(totalSimulationTimeMs,
                    numThreads, numTasks,
                    minBurstTimeMs, maxBurstTimeMs, sleepTimeMs);

            System.out.println("Simulation results:"
                    + "\n" + "----------------------");
            service.printResults();

            System.out.println("\n");
        }

        System.out.println("----------------------");
        System.out.println("Exiting...");
        // If your program has not completed after the message printed above,
        // it means that some threads are not properly stopped! -> this issue will affect the grade
    }
}
