public class Main {
    static void run() {
        ToDoRead toDoRead = new ToDoRead("tasks.csv");
        ToDoWrite toDoWrite = new ToDoWrite("tasks.csv");

        System.out.println("Welcome to ToDo application.");

        while (true) {
            // TODO: fix infinite loop problem not taking input after the first one
            System.out.println("Please enter your command.");
            String command = ToDoInput.readCommandInput().trim();

            if (command.startsWith("todo")) {
                if (command.contains("list")) {
                    switch (command) {
                        case "todo list" -> toDoRead.listTasks();
                        case "todo list -s" -> toDoRead.listTasksByStatus();
                        case "todo list -p" -> toDoRead.listTasksByPriority();
                        case "todo list -t" -> toDoRead.listTasksByTimestamp();
                        case "todo stats" -> toDoRead.displayStats();
                        default -> {
                            System.err.println("Wrong command, try again");
                        }
                    }
                } else if (command.contains("add")) {
                    toDoWrite.addTask(command);
                } else if (command.contains("task")) {
                    toDoWrite.editTask(command);
                } else if (command.contains("remove")) {
                    toDoWrite.removeTask(command);
                }

            } else System.err.println("Wrong command, try again!");
        }
    }
    public static void main(String[] args) {
            run();

    }
}
