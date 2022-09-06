import java.io.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
interface DataManipulator {
    void manipulateData(String[] fields);
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
                String[] fields = line.split(",");
                dm.manipulateData(fields);
            }
        } catch (IOException ex) {
            System.err.println("ERROR: File can't be read!");
        }
    }
    void listTasks() {
        StringBuilder result = new StringBuilder("");

        System.out.println("List of all tasks: ");
        System.out.println("\tid\t|\tstatus\t|\tcreated/modified timestamp\t|\t name\t|\tpriority\t|");

        read(fields -> {
            for (String field: fields) {
                result.append('\t').append(field.trim()).append('\t').append('|');
            }
            result.append('\n');
        });

        System.out.println(result);
    }
    void listTasksByStatus() {
        boolean shouldPrintSeperator = false;
        ArrayDeque<TaskByStatus> tasksByStatus = new ArrayDeque<>();

        System.out.println("Tasks listed by status (not done ([]) > done ([X]):");

        read(fields -> {
            if (fields[1].trim().equals("done"))
                tasksByStatus.add(new TaskByStatus(fields[3].trim(), "[X}"));
            else tasksByStatus.addFirst(new TaskByStatus(fields[3].trim(), "[]"));
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
        read(fields -> {
            tasksByPriority.add(new TaskByPriority(fields[3].trim(), fields[4].trim()));
        });

        while (!tasksByPriority.isEmpty()) {
            TaskByPriority task = tasksByPriority.poll();
            System.out.println(task.getName() + ", " + task.getPriority());
        }
    }
    void listTasksByTimestamp() {
        TreeMap<Long, String> tasksByTimestamp = new TreeMap<>(Comparator.reverseOrder());

        System.out.println("Tasks listed by timestamp (newest first):");

        read(fields -> {

            LocalDateTime date = LocalDateTime.parse(fields[2].trim(), DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
            long milliseconds = date.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();

            tasksByTimestamp.put(milliseconds, fields[3]);
        });

        for (Map.Entry<Long, String> entry: tasksByTimestamp.entrySet()) {
            System.out.println(entry.getValue());
        }
    }
}