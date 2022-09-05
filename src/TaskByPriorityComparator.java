import java.util.Comparator;

public class TaskByPriorityComparator implements Comparator<TaskByPriority> {
    // Comparator by priorities: high > normal > low
    @Override
    public int compare(TaskByPriority t1, TaskByPriority t2) {
        if (t1.getPriority().equals("high")) {
            if (t2.getPriority().equals("normal") || t2.getPriority().equals("low")) return -1;
            return 0;
        }
        else if (t1.getPriority().equals("normal")) {
            if (t2.getPriority().equals("high")) return 1;
            if (t2.getPriority().equals("low")) return -1;
            return 0;
        }
        else {
            if (t2.getPriority().equals("high") || t2.getPriority().equals("normal")) return 1;
            return 0;
        }
    }
}
