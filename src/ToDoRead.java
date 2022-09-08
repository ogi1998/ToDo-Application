import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

interface DataManipulator {
        void manipulateData(String[] fields);
    }
public class ToDoRead {
    String filePath;
    ToDoRead(String filePath) {
        this.filePath = filePath;
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

        System.out.println("Tasks listed by status (pending ([]) > done ([X]):");

        read(fields -> {
            if (fields[1].trim().equals("done"))
                tasksByStatus.add(new TaskByStatus(fields[3].trim(), "[X]"));
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
        System.out.println("Tasks listed by priority (high ([HI]) > normal ([NOR]) > low ([LO]):");
        read(fields -> {
            tasksByPriority.add(new TaskByPriority(fields[3].trim(), fields[4].trim()));
        });

        while (!tasksByPriority.isEmpty()) {
            TaskByPriority task = tasksByPriority.poll();
            System.out.println(task.getPriority() + " " + task.getName());
        }
    }
    void listTasksByTimestamp() {
        TreeMap<Long, String> tasksByTimestamp = new TreeMap<>(Comparator.reverseOrder());

        System.out.println("Tasks listed by timestamp (newest first):");

        read(fields -> {
            tasksByTimestamp.put(ToDoUtility.dateToMillis(fields[2].trim()), fields[3]);
        });

        for (Map.Entry<Long, String> entry: tasksByTimestamp.entrySet()) {
            System.out.println(entry.getValue());
        }
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
}
