import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

interface DataManipulator {
    void manipulateData(String line);
}
public class CSVReader {

    String filePath;

    CSVReader(String filePath) {
        this.filePath = filePath;
    }

    void read(DataManipulator dm) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                dm.manipulateData(line);
            }
        } catch (IOException ex) {
            System.err.println("ERROR: File can't be read!");
        }
    }

    void listTasks() {
        StringBuilder result = new StringBuilder("");

        System.out.println("List of all tasks: ");
        System.out.println("\tid\t|\tstatus\t|\tcreated/modified timestamp\t|\t name\t|\tpriority\t|");

        read(line -> {
            String[] attrs = line.split(",");
            for (String attr: attrs) {
                result.append('\t').append(attr.trim()).append('\t').append('|');
            }
            result.append('\n');
        });

        System.out.println(result);
    }

    void listTasksByStatus() {
        boolean shouldPrintSeperator = false;
        ArrayDeque<TaskByStatus> tasksByStatus = new ArrayDeque<>();

        System.out.println("Tasks listed by status (not done ([]) > done ([X]):");

        read(line -> {
            String[] splittedLine = line.split(",");
            if (splittedLine[1].trim().equals("done"))
                tasksByStatus.add(new TaskByStatus(splittedLine[3].trim(), "[X}"));
            else tasksByStatus.addFirst(new TaskByStatus(splittedLine[3].trim(), "[]"));
        });

        while (!tasksByStatus.isEmpty()) {
            TaskByStatus task = tasksByStatus.poll();

            if (!shouldPrintSeperator && task.getStatus().equals("[X]")) {
                System.out.println("--------------------");
                shouldPrintSeperator = true;
            }
            System.out.println(task.getStatus() + '\t' + task.getName());
        }
    }
    void listTasksByPriority() {
        PriorityQueue<TaskByPriority> tasksByPriority = new PriorityQueue<>(new TaskByPriorityComparator());
        System.out.println("Tasks listed by priority (high > normal > low):");
        read(line -> {
            String[] splittedLine = line.split(",");
            tasksByPriority.add(new TaskByPriority(splittedLine[3].trim(), splittedLine[4].trim()));
        });

        while (!tasksByPriority.isEmpty()) {
            TaskByPriority task = tasksByPriority.poll();
            System.out.println(task.getName() + ", " + task.getPriority());
        }
    }

    void listTasksByTimestamp() {
        TreeMap<Long, String> tasksByTimestamp = new TreeMap<>(Comparator.reverseOrder());

        System.out.println("Tasks listed by timestamp (newest first):");

        read(line -> {
            String[] splittedLine = line.split(",");

            LocalDateTime date = LocalDateTime.parse(splittedLine[2].trim(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            long milliseconds = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

            tasksByTimestamp.put(milliseconds, splittedLine[3]);
        });

        for (Map.Entry<Long, String> entry: tasksByTimestamp.entrySet()) {
            System.out.println(entry.getValue());
        }
    }
}
