public class Main {
    public static void main(String[] args) {
        ListToDo listToDo = new ListToDo("tasks.csv");
//        listToDo.listTasks();
//        listToDo.listTasksByStatus();
//        listToDo.listTasksByPriority();
        listToDo.listTasksByTimestamp();
    }
}
