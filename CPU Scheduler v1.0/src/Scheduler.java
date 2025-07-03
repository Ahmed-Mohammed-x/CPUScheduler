// Scheduler.java

/* The scheduler working overtime simulating every algorithm*/
class Scheduler {
    static void simulate(java.util.List<Process> processes, String algorithm, int timeQuantum, int simulationSpeed) {
        int currentTime = 0, completed = 0, n = processes.size();

        // Sort by arrival time initially to simulate a real example
        processes.sort(java.util.Comparator.comparingInt(p -> p.ArrivalTime));

        //queueing the processes
        java.util.Queue<Process> readyQueue = new java.util.LinkedList<>();

        //make sure they finish
        while (completed < n) {

            // Add newly arrived processes to ready queue if it exists
            for (Process p : processes) {
                if (p.ArrivalTime <= currentTime && p.state.equals("WAITING")) {
                    p.state = "READY";
                    readyQueue.add(p);
                    System.out.println(Colours.YELLOW + "Time " + currentTime +
                            ": Process P" + p.Pid + " arrived" + Colours.RESET);
                }
            }

            //Nothing to process yet
            if (readyQueue.isEmpty()) {
                System.out.println("Time " + currentTime + ": CPU idle");
                CPUSchedulerSimulator.sleep(simulationSpeed);
                currentTime++;
                continue;
            }

            // Display the ready queue
            CPUSchedulerSimulator.displayQueue(readyQueue, currentTime);

            // Select the processes based on algorithm selected by the user
            Process current;
            if (algorithm.equals("FCFS")) {

                // FCFS algorithm
                current = readyQueue.poll();
            } else if (algorithm.equals("SJF")) {

                //SJF algorithm
                current = readyQueue.stream()
                        .min(java.util.Comparator.comparingInt(p -> p.RemainingTime))
                        .orElse(null);
                readyQueue.remove(current);

            } else { // RR algorithm
                current = readyQueue.poll();
            }

            if (current == null) {
                CPUSchedulerSimulator.sleep(simulationSpeed);
                currentTime++;
                continue;
            }

            // Execute the process
            current.state = "RUNNING";
            System.out.println(Colours.GREEN_BOLD + "Time " + currentTime +
                    ": Process P" + current.Pid + " starts execution" + Colours.RESET);

            // find the execution time
            int executeTime = algorithm.equals("RR") ?
                    Math.min(timeQuantum, current.RemainingTime) :
                    current.RemainingTime;

            // Run the process with visual display (bars)
            for (int i = 0; i < executeTime; i++) {
                int progress = 100 * (current.BurstTime - current.RemainingTime + i + 1) / current.BurstTime;
                System.out.print("\rTime " + (currentTime + i) + ": P" + current.Pid +
                        " executing [" + CPUSchedulerSimulator.getProgressBar(progress) + "] " +
                        progress + "% (" + (current.RemainingTime - i - 1) + " left)");
                CPUSchedulerSimulator.sleep(simulationSpeed);
            }
            System.out.println();

            current.RemainingTime -= executeTime;
            currentTime += executeTime;

            // The process is done
            if (current.RemainingTime == 0) {
                current.state = "COMPLETED";
                current.TurnaroundTime = currentTime - current.ArrivalTime;
                current.WaitingTime = current.TurnaroundTime - current.BurstTime;
                completed++;
                System.out.println(Colours.RED_BOLD + "Time " + currentTime +
                        ": Process P" + current.Pid + " completed. Turnaround: " +
                        current.TurnaroundTime + ", Waiting: " + current.WaitingTime +
                        Colours.RESET);
            } else if (algorithm.equals("RR")) {
                System.out.println(Colours.BLUE + "Time " + currentTime +
                        ": Process P" + current.Pid + " preempted with " +
                        current.RemainingTime + " time units remaining" + Colours.RESET);
                current.state = "READY";
                readyQueue.add(current);
            }
        }
    }
}