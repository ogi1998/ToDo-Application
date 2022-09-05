public class TaskByStatus {
    private final String name;
    private final String status;

    TaskByStatus(String name, String status) {
        this.name = name;
        this.status = status;
    }

    String getName() { return this.name; }
    String getStatus() { return this.status; }
}
