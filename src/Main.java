public class Main {
    public static void main(String[] args) {
        CSVList csvRead = new CSVList("data.csv");
//        csvRead.listTasks();
        csvRead.listTasksByStatus();
//        csvRead.listTasksByPriority();
    }
}
