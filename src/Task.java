public class Task {
    private String id;
    private String name;

    private int priority;
    private int isDone = 0;
    private String timestamp = ToDoUtility.getCurrentDateAndTime();

    private String commandType;

    public String getCommandType() {
        return commandType;
    }

    public void setCommandType(String commandType) {
        this.commandType = commandType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int isDone() {
        return isDone;
    }

    public void setDone(int done) {
        isDone = done;
    }

    public String getTimestamp() {
        return timestamp;
    }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
