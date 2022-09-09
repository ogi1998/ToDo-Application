import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    static void run() {
        ToDoRead toDoRead = new ToDoRead("tasks.csv");
        ToDoWrite toDoWrite = new ToDoWrite("tasks.csv");

        System.out.println("Welcome to ToDo application.");

            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                while (true) {
                    System.out.println("Please enter your command.");
                    String command = br.readLine().trim();
                    String[] cmdArgs = command.split(" ");

                    if (command.startsWith("todo")) {
                        if (cmdArgs[1].equals("list")) {
                            switch (command) {
                                case "todo list" -> toDoRead.listTasks();
                                case "todo list -s" -> toDoRead.listTasksByStatus();
                                case "todo list -p" -> toDoRead.listTasksByPriority();
                                case "todo list -t" -> toDoRead.listTasksByTimestamp();
                                default -> {
                                    System.err.println("Wrong command, try again");
                                }
                            }
                        } else if (cmdArgs[1].equals("stats")) {
                            toDoRead.displayStats();
                        } else if (cmdArgs[1].equals("add")) {
                            toDoWrite.addTask(command);
                        } else if (cmdArgs[1].equals("task")) {
                            toDoWrite.editTask(command);
                        } else if (cmdArgs[1].equals("remove")) {
                            toDoWrite.removeTask(command);
                        } else System.err.println("Wrong command, try again!");
                    } else System.err.println("Wrong command, try again!");
                }
            } catch (IOException ex) {
                System.err.println("ERROR: Error reading input");
        }
    }
    public static void main(String[] args) {
            run();
    }
}
