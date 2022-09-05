import java.io.*;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Deque;
import java.util.PriorityQueue;

public class CSVReader {
    String filePath;

    CSVReader(String filePath) {
        this.filePath = filePath;
    }

    void listTasks() {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder result = new StringBuilder("");

            while ((line = br.readLine()) != null) {
                String[] attrs = line.split(",");
                for (String attr: attrs) {
                    result.append('\t').append(attr.trim()).append('\t').append("|");
                }
                result.append("\n");
            }
            System.out.println(result);
        } catch (IOException ex) {
            System.err.println("ERROR: File can't be read!");
        }
    }

    void listTasksByStatus() {
        System.out.println("Tasks listed by status (not done ([]) > done ([X])");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean shouldPrintSeperator = false;

            ArrayDeque<TaskByStatus> tasksByStatus = new ArrayDeque<>();

            // Skip first line (header)
            br.readLine();

            while ((line = br.readLine()) != null) {
                String[] splittedLine = line.split(",");
                if (splittedLine[1].trim().equals("done")) tasksByStatus.addLast(new TaskByStatus(splittedLine[3].trim(), "[X]"));
                else tasksByStatus.addFirst(new TaskByStatus(splittedLine[3].trim(), "[]"));
            }


            while (!tasksByStatus.isEmpty()) {
                TaskByStatus task = tasksByStatus.poll();

                if (!shouldPrintSeperator && task.getStatus().equals("[X]")) {
                    System.out.println("--------------------");
                    shouldPrintSeperator = true;
                }


                System.out.println(task.getStatus() + '\t' + task.getName());
            }
        } catch (IOException ex) {
            System.err.println("ERROR: File can't be read!");
        }
    }


    void listTasksByPriority() {
        System.out.println("Tasks listed by priority (high > normal > low):");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            PriorityQueue<TaskByPriority> tasksByPriority = new PriorityQueue<>(new TaskByPriorityComparator());

            // To skip the header
            br.readLine();

            while((line = br.readLine()) != null) {
                String[] splittedLine = line.split(",");
                tasksByPriority.add(new TaskByPriority(splittedLine[3].trim(), splittedLine[4].trim()));
            }

            while (!tasksByPriority.isEmpty()) {
                TaskByPriority task = tasksByPriority.poll();
                System.out.println(task.getName() + ", " + task.getPriority());
            }

        } catch (IOException ex) {
            System.err.println("ERROR: File can't be read!");
        }
    }
}
