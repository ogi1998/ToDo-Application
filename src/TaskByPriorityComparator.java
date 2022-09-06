import java.util.Comparator;

public class TaskByPriorityComparator implements Comparator<TaskByPriority> {
    // Comparator by priorities: high > normal > low
    @Override
    public int compare(TaskByPriority t1, TaskByPriority t2) {
        if (t1.getPriority().equals("[HI]")) {
            if (t2.getPriority().equals("[NOR]") || t2.getPriority().equals("[LO]")) return -1;
            return 0;
        }
        else if (t1.getPriority().equals("[NOR]")) {
            if (t2.getPriority().equals("[HI]")) return 1;
            if (t2.getPriority().equals("[LO]")) return -1;
            return 0;
        }
        else {
            if (t2.getPriority().equals("[HI]") || t2.getPriority().equals("[NOR]")) return 1;
            return 0;
        }
    }
}
