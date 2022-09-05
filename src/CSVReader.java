import java.io.*;
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


    void listByPriority() {
        System.out.println("Tasks listed by priority (high > normal > low):");
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            PriorityQueue<TaskByPriority> taskByPriorities = new PriorityQueue<>(new TaslByPriorityComparator());

            // To skip the header
            br.readLine();

            while((line = br.readLine()) != null) {
                String[] splittedLine = line.split(",");
                taskByPriorities.add(new TaskByPriority(splittedLine[3].trim(), splittedLine[4].trim()));
            }

            while (!taskByPriorities.isEmpty()) {
                TaskByPriority task = taskByPriorities.poll();
                System.out.println(task.getName() + ", " + task.getPriority());
            }

        } catch (IOException ex) {
            System.err.println("ERROR: File can't be read!");
        }
    }
}
