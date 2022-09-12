import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

interface DataManipulator {
        void manipulateData(String line);
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

        read(line -> {
            String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            fields[3] = fields[3].replace("\"", "");

            for (String field: fields) {
                result.append('\t').append(field.trim()).append('\t').append('|');
            }
            result.append('\n');
        });
        System.out.println(result);
    }
    void listTasksByStatus() {
        boolean shouldPrintSeparator = false;
        ArrayDeque<Task> tasksByStatus = new ArrayDeque<>();

        System.out.println("Tasks listed by status (pending ([]) > done ([X]):");
        read(line -> {
            String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            fields[3] = fields[3].replace("\"", "");

            Task newTask = new Task();
            newTask.setDone(Integer.parseInt(fields[1].trim()));
            newTask.setName(fields[3].trim());

            if (newTask.isDone() == 1)
                tasksByStatus.add(newTask);
            else tasksByStatus.addFirst(newTask);
        });

        while (!tasksByStatus.isEmpty()) {
            Task task = tasksByStatus.poll();

            if (!shouldPrintSeparator && task.isDone() == 1) {
                System.out.println("--------------------");
                shouldPrintSeparator = true;
            }
            String isDone = task.isDone() == 1 ? "[X]" : "[]";
            System.out.println(isDone + '\t' + task.getName());
        }
    }
    void listTasksByPriority() {
        PriorityQueue<TaskByPriority> tasksByPriority = new PriorityQueue<>(new TaskByPriorityComparator());
        System.out.println("Tasks listed by priority (high ([HI]) > normal ([NOR]) > low ([LO]):");
        read(line -> {
            String[] fields = line.split(",");
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

        read(line -> {
            String[] fields = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            fields[3] = fields[3].replace("\"", "");

            Task newTask = new Task();
            newTask.setTimestamp(fields[2].trim());
            newTask.setName(fields[3].trim());
            tasksByTimestamp.put(ToDoUtility.dateToMillis(newTask.getTimestamp()), newTask.getName());
        });

        for (Map.Entry<Long, String> entry: tasksByTimestamp.entrySet()) {
            System.out.println(entry.getValue());
        }
    }

    void displayStats() {
        int[] statsCount = new int[2];

        System.out.println("Number of finished and unfinished tasks: ");
        read(line -> {
            String[] fields = line.split(",");
            if (fields[1].trim().equals("1")) statsCount[0]++;
            if (fields[1].trim().equals("0")) statsCount[1]++;
        });

        System.out.println("Finished tasks: " + statsCount[0]);
        System.out.println("Unfinished tasks: " + statsCount[1]);

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
}
