package todo.task;

import java.util.Comparator;

public class TaskByPriorityComparator implements Comparator<Task> {
    // Comparator by priorities: high > normal > low
    @Override
    public int compare(Task t1, Task t2) {
        if (t1.getPriority() == 1) {
            if (t2.getPriority() == 0 || t2.getPriority() == -1) return -1;
            return 0;
        }
        else if (t1.getPriority() == 0) {
            if (t2.getPriority() == 1) return 1;
            if (t2.getPriority() == -1) return -1;
            return 0;
        }
        else {
            if (t2.getPriority() == 1 || t2.getPriority() == 0) return 1;
            return 0;
        }
    }
}
