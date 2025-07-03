// CPUSchedulerSimulator.java
public class CPUSchedulerSimulator {
    public static void main(String[] args) {

        try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
            java.util.List<Process> processes = new java.util.ArrayList<>();

            // The user chooses the desired algorithm
            System.out.println("Select the desired algorithm: 1) FCFS 2) SJF 3) RR");
            int algorithm_option;
            algorithm_option = scanner.nextInt();
            if (algorithm_option < 1 || algorithm_option > 3) {
                throw new IllegalArgumentException("You inserted an invalid character/s. Please choose the numbers 1, 2, or 3 next time :)");
            }
            String algorithm = algorithm_option == 1 ? "FCFS" : algorithm_option == 2 ? "SJF" : "RR";

            // Get the number of processes from the user
            System.out.print("Please enter the number of processes: ");
            int nofp = scanner.nextInt();
            if (nofp <= 0) {
                throw new IllegalArgumentException("The number of processes must be a positive number. please enter a positive integer next time :)");
            }

            // Get process details
            for (int i = 0; i < nofp; i++) {
                System.out.println(Colours.YELLOW + "Process " + (i + 1) + ":" + Colours.RESET);

                System.out.print("Enter the Process ID: ");
                int Pid;
                Pid = scanner.nextInt();
                if (Pid <= 0) {
                    throw new IllegalArgumentException("Process ID must be a positive number. Please enter a positive integer next time :)");
                }

                System.out.print("Arrival time: ");
                int ArrTime;
                ArrTime = scanner.nextInt();
                if (ArrTime < 0) {
                    throw new IllegalArgumentException("Arrival Time time cannot be a negative number. PLEASE enter a positive integer next time :)");
                }

                System.out.print("Burst time: ");
                int BurstTime;
                BurstTime = scanner.nextInt();
                if (BurstTime <= 0) {
                    throw new IllegalArgumentException("Burst time must be positive.");
                }

                processes.add(new Process(Pid, ArrTime, BurstTime));
            }

            // Setting the time quantum for RR algorithm
            int TimeQuantum = 2;
            if (algorithm.equals("RR")) {
                System.out.print("Enter the time quantum for the Round Robin Algorithm :P: ");
                TimeQuantum = scanner.nextInt();
                if (TimeQuantum <= 0) {
                    throw new IllegalArgumentException("Time quantum must be positive.");
                }
            }

            // Telling the user that the simulation speed will simulate the real time of processing this
            System.out.print("The simulation speed will be running per second" +
                    " to simulate processing these processes in real time): ");
            int SimulationSpeed = 1000;

            // Run the simulation
            System.out.println(Colours.CYAN_BOLD + "\nRunning " + algorithm + " simulation..." + Colours.RESET);
            Scheduler.simulate(processes, algorithm, TimeQuantum, SimulationSpeed);

            // Display the desired results
            System.out.println(Colours.WHITE_BOLD + "\tResults:" + Colours.RESET);
            System.out.println(Colours.YELLOW + "PID\t\tWaiting Time\t\tArrival Time\t\tBurstTime\t\tTurnaround Time" + Colours.RESET);
            double avgWait = 0, avgTurnaround = 0;
            for (Process p : processes) {
                System.out.printf("%-10d%-20d%-20d%-20d%-20d%n", p.Pid, p.WaitingTime, p.ArrivalTime, p.BurstTime, p.TurnaroundTime);
                avgWait += p.WaitingTime;
                avgTurnaround += p.TurnaroundTime;
            }
            System.out.println(Colours.CYAN_BOLD + "\nAverage waiting time: " + (avgWait / nofp) + Colours.RESET);
            System.out.println(Colours.CYAN_BOLD + "Average turnaround time: " + (avgTurnaround / nofp) + Colours.RESET);

        } catch (java.util.InputMismatchException e) {
            System.out.println(Colours.RED_BOLD + "Error: Invalid input. Please enter numeric values where required." + Colours.RESET);
            System.exit(1);
        } catch (IllegalArgumentException e) {
            System.out.println(Colours.RED_BOLD + "Error: " + e.getMessage() + Colours.RESET);
            System.exit(1);
        } catch (Exception e) {
            System.out.println(Colours.RED_BOLD + "An unexpected error occurred: " + e.getMessage() + Colours.RESET);
            System.exit(1);
        }
    }

    /* Shows which state is the queue in and displaying all processes waiting */
    static void displayQueue(java.util.Queue<Process> queue, int currentTime) {
        System.out.print(Colours.YELLOW_BOLD + "Time " + currentTime +
                ": Ready Queue: [" + Colours.RESET);
        if (queue.isEmpty()) {
            System.out.print("empty");
        } else {
            String prefix = "";
            for (Process p : queue) {
                System.out.print(prefix + "P" + p.Pid + "(" + p.RemainingTime + ")");
                prefix = ", ";
            }
        }
        System.out.println(Colours.YELLOW_BOLD + "]" + Colours.RESET);
    }

    /* Help us visualise the output using loading bars and appending the sqr shape*/
    static String getProgressBar(int percentage) {
        int bars = percentage / 5;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            sb.append(i < bars ? "■" : "□");
        }
        return sb.toString();
    }

    /*control the speed to help mimic an actual time simulation*/
    static void sleep(int ms) {
        try {
            java.util.concurrent.TimeUnit.MILLISECONDS.sleep(ms);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}