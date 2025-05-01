import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.*;

class Process {
    int pid;
    int arrivalTime;
    int burstTime;
    int priority;

    public Process(int pid, int arrivalTime, int burstTime, int priority) {
        this.pid = pid;
        this.arrivalTime = arrivalTime;
        this.burstTime = burstTime;
        this.priority = priority;
    }
}

// Shared Buffer for Producer-Consumer
class Buffer {
    private final Queue<Process> queue = new LinkedList<>();
    private final int capacity;

    private final Semaphore full;
    private final Semaphore empty;
    private final Lock lock;

    public Buffer(int size) {
        this.capacity = size;
        this.full = new Semaphore(0);
        this.empty = new Semaphore(size);
        this.lock = new ReentrantLock();
    }

    public void produce(Process p) throws InterruptedException {
        empty.acquire();
        lock.lock();
        try {
            queue.add(p);
            System.out.println("[Producer] Added process PID " + p.pid);
        } finally {
            lock.unlock();
            full.release();
        }
    }

    public Process consume() throws InterruptedException {
        full.acquire();
        lock.lock();
        try {
            Process p = queue.poll();
            System.out.println("[Consumer] Processing PID " + p.pid);
            return p;
        } finally {
            lock.unlock();
            empty.release();
        }
    }
}

class Producer extends Thread {
    private final List<Process> processes;
    private final Buffer buffer;

    public Producer(List<Process> processes, Buffer buffer) {
        this.processes = processes;
        this.buffer = buffer;
    }

    public void run() {
        for (Process p : processes) {
            try {
                Thread.sleep(p.arrivalTime * 1000L);  // simulate arrival
                buffer.produce(p);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

class Consumer extends Thread {
    private final Buffer buffer;

    public Consumer(Buffer buffer) {
        this.buffer = buffer;
    }

    public void run() {
        while (true) {
            try {
                Process p = buffer.consume();
                System.out.println("[Consumer] PID " + p.pid + " started.");
                Thread.sleep(p.burstTime * 1000L);
                System.out.println("[Consumer] PID " + p.pid + " finished.");
            } catch (InterruptedException e) {
                break;
            }
        }
    }
}

public class OSProject2 {
    public static void main(String[] args) {
        String filename = "processes.txt";
        List<Process> processList = new ArrayList<>();

        // Read file
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            br.readLine(); // skip header
            while ((line = br.readLine()) != null) {
                String[] parts = line.trim().split("\\s+");
                int pid = Integer.parseInt(parts[0]);
                int arrival = Integer.parseInt(parts[1]);
                int burst = Integer.parseInt(parts[2]);
                int priority = Integer.parseInt(parts[3]);
                processList.add(new Process(pid, arrival, burst, priority));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        Buffer buffer = new Buffer(2);  // buffer size of 2
        Producer producer = new Producer(processList, buffer);
        Consumer consumer = new Consumer(buffer);

        producer.start();
        consumer.start();

        try {
            producer.join();
            // Give consumer a little time to finish up
            Thread.sleep(10000); 
            consumer.interrupt();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}