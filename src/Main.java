public class Main {
    public static void main(String[] args) {
        ListToDo listToDo = new ListToDo("tasks.csv");
        ToDoWrite toDoWrite = new ToDoWrite("tasks.csv");
//        listToDo.listTasks();
//        listToDo.listTasksByStatus();
//        listToDo.listTasksByPriority();
//        listToDo.listTasksByTimestamp();
//        toDoWrite.addTask();
//        toDoWrite.removeTask("6");
        toDoWrite.editTask("11");
    }
}
