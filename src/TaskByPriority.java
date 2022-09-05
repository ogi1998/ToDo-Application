public class TaskByPriority {
    private final String name;
    private final String priority;

    TaskByPriority(String name, String priority) {
        this.name = name;
        this.priority = priority;
    }

    String getPriority() {
        return this.priority;
    }

    String getName() {
        return this.name;
    }
}
