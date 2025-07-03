// Process.java
class Process {
    int Pid, ArrivalTime, BurstTime, RemainingTime, WaitingTime, TurnaroundTime;
    String state;

    Process(int pid, int arrivalTime, int burstTime) {
        this.Pid = pid;
        this.ArrivalTime = arrivalTime;
        this.BurstTime = burstTime;
        this.RemainingTime = burstTime;
        this.state = "WAITING";
    }
}