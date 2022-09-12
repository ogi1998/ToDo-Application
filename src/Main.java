import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

    static void run() {
        ToDoRead toDoRead = new ToDoRead("tasks.csv");
        ToDoWrite toDoWrite = new ToDoWrite("tasks.csv");

        System.out.println("Welcome to ToDo application.");
        System.out.println();

            try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
                while (true) {
                    System.out.println("Please enter your command.");
                    String command = br.readLine().trim();
                    String[] cmdArgs = command.split(" ");

                    if (command.startsWith("todo")) {
                        switch (cmdArgs[1]) {
                            case "list" -> {
                                switch (command) {
                                    case "todo list" -> toDoRead.listTasks();
                                    case "todo list -s" -> toDoRead.listTasksByStatus();
                                    case "todo list -p" -> toDoRead.listTasksByPriority();
                                    case "todo list -t" -> toDoRead.listTasksByTimestamp();
                                    default -> System.err.println("Wrong command, try again");
                                }
                            }
                            case "stats" -> toDoRead.displayStats();
                            case "add" -> toDoWrite.addTask(command);
                            case "task" -> toDoWrite.editTask(command);
                            case "remove" -> toDoWrite.removeTask(command);
                            default -> System.err.println("Wrong command, try again!");
                        }
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
