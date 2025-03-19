import java.io.*;
import java.util.*;

class Process {
    int pid, arrivalTime, burstTime, priority;
    int waitingTime = 0, turnaroundTime = 0;

    public Process(int pid, int arrivalTime, int burstTime, int priority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }
}

public class Scheduler {
    public static void main(String[] args) {
        List<Process> processes = readProcesses("processes.txt");
        if (processes.isEmpty()) {
            System.out.println("No processes found in file.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.println("Choose Scheduling Algorithm:");
        System.out.println("1. First-Come, First-Served (FCFS)");
        System.out.println("2. Shortest Job First (SJF)");
        System.out.print("Enter choice: ");
        int choice = scanner.nextInt();
        scanner.close();

        if (choice == 1) {
            fcfsScheduling(processes);
        } else if (choice == 2) {
            sjfScheduling(processes);
        } else {
            System.out.println("Invalid choice!");
            return;
        }

        displayResults(processes);
    }

    // Function to read processes from a file
    public static List<Process> readProcesses(String filename) {
        List<Process> processes = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                if (parts.length == 4) {
                    processes.add(new Process(
                        Integer.parseInt(parts[0]), 
                        Integer.parseInt(parts[1]), 
                        Integer.parseInt(parts[2]), 
                        Integer.parseInt(parts[3])
                    ));
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        return processes;
    }

    // FCFS Scheduling
    // FCFS Scheduling
public static void fcfsScheduling(List<Process> processes) {
    processes.sort(Comparator.comparingInt(p -> p.arrivalTime));

    int time = 0;
    System.out.println("\nGantt Chart (FCFS):");

    // Print top Gantt chart with uniform spacing
    for (Process p : processes) {
        if (time < p.arrivalTime) time = p.arrivalTime;
        System.out.printf("| P%-2d ", p.pid);  // Ensures fixed width
        p.waitingTime = time - p.arrivalTime;
        p.turnaroundTime = p.waitingTime + p.burstTime;
        time += p.burstTime;
    }
    System.out.println("|");

    // Print aligned time markers
    time = 0;
    System.out.printf("%-4d", time);
    for (Process p : processes) {
        if (time < p.arrivalTime) time = p.arrivalTime;
        time += p.burstTime;
        System.out.printf(" "+"%-4d", time);  // Ensures spacing is consistent
    }
    System.out.println();
}

// SJF Scheduling
public static void sjfScheduling(List<Process> processes) {
    processes.sort(Comparator.comparingInt(p -> p.burstTime));

    int time = 0;
    System.out.println("\nGantt Chart (SJF):");

    // Print top Gantt chart with uniform spacing
    for (Process p : processes) {
        if (time < p.arrivalTime) time = p.arrivalTime;
        System.out.printf("| P%-2d ", p.pid);  // Ensures fixed width
        p.waitingTime = time - p.arrivalTime;
        p.turnaroundTime = p.waitingTime + p.burstTime;
        time += p.burstTime;
    }
    System.out.println("|");

    // Print aligned time markers
    time = 0;
    System.out.printf("%-4d", time);
    for (Process p : processes) {
        if (time < p.arrivalTime) time = p.arrivalTime;
        time += p.burstTime;
        System.out.printf(" "+"%-4d", time);  // Ensures proper spacing
    }
    System.out.println();
}

    // Function to display results
    public static void displayResults(List<Process> processes) {
        double totalWT = 0, totalTAT = 0;
        System.out.println("\nProcess\tWT\tTAT");
        for (Process p : processes) {
            System.out.println("P" + p.pid + "\t" + p.waitingTime + "\t" + p.turnaroundTime);
            totalWT += p.waitingTime;
            totalTAT += p.turnaroundTime;
        }
        System.out.printf("Avg WT: %.2f | Avg TAT: %.2f\n", totalWT / processes.size(), totalTAT / processes.size());
    }
}