public class Main {
    public static void main(String[] args) {
        CSVReader csvRead = new CSVReader("data.csv");
        csvRead.listTasks();
        csvRead.listTasksByPriority();
        csvRead.listTasksByStatus();
        csvRead.listTasksByTimestamp();
    }
}
