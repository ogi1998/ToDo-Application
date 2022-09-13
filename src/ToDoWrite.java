import java.io.*;
import java.util.Arrays;

public class ToDoWrite {
    String fileName;
    ToDoWriteHelpers helpers;
    ToDoWrite(String fileName) {
        this.fileName = fileName;
        helpers = new ToDoWriteHelpers(fileName);
    }
    void addTask(String command) {
        String name = command.substring(command.indexOf("\""), command.lastIndexOf("\"") + 1);
        StringBuilder newUser = new StringBuilder();

        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
            if (raf.length() > 0) newUser.append('\n');
            newUser
                    .append(helpers.generateID(raf)).append(", 0, ")
                    .append(ToDoUtility.getCurrentDateAndTime())
                    .append(", ").append(name).append(", ");

            if (command.endsWith("]")) {
                String priority = command.substring(command.indexOf("["), command.indexOf("]") + 1);

                if (priority.equals("[LO]")) newUser.append(" -1");
                else if (priority.equals("[HI]")) newUser.append(" 1");
            } else newUser.append("0");

            raf.seek(raf.length());
            raf.writeBytes(newUser.toString());
        } catch (IOException ex) {
            System.err.println("ERROR: Can't write to file!");
        }
    }

    // TODO: Double check if it works (after adding new task and deleting first one, it doesnt work properly)
    void removeTask(String command) {
        StringBuilder contentAfterDelete = new StringBuilder();
        String idToRemove = command.substring(command.lastIndexOf(" ")).trim();
        long deletePos = helpers.findLinePositionById(idToRemove);

        if (deletePos == -1) {
            System.err.println("Task with that uid doesn't exist!");
            return;
        }
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
            raf.seek(deletePos);
            if ((char) raf.read() != '\n') raf.seek(deletePos);

            String line = raf.readLine();
            int deletedLineLength = line.length() + 1;

            while ((line = raf.readLine()) != null) {
                contentAfterDelete.append(line).append('\n');
            }
            raf.seek(deletePos);
            raf.writeBytes(contentAfterDelete.toString());
            raf.setLength(raf.length() - deletedLineLength);
            System.out.println("Task successfully removed!");
        } catch (IOException ex) {
            System.err.println("Error removing the task!");
        }
    }
    // TODO: Add conditions for all types of updates
    void editTask(String command) {
        Task newTask = helpers.getUpdateData(command);
        StringBuilder contentAfterUpdate = new StringBuilder();
        long updatePos = helpers.findLinePositionById(newTask.getId());

        if (updatePos == -1) {
            System.out.println("Task with that uid doesn't exist!");
            return;
        }
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
            raf.seek(updatePos);
            String line = raf.readLine();
            String[] splittedLine = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            splittedLine[2] = " " + ToDoUtility.getCurrentDateAndTime();

            switch (newTask.getCommandType()) {
                case "-e" -> {
                    splittedLine[3] = " " + newTask.getName();
                    if (newTask.getPriority() != 0)
                        splittedLine[4] = " " + newTask.getPriority();
                }
                case "-d" -> splittedLine[1] = " " + 1;
                case "-u" -> splittedLine[1] = " " + 0;
            }
            contentAfterUpdate.append(String.join(",", splittedLine)).append('\n');


            int fileShrinkAmount = line.length() - contentAfterUpdate.length() + 1;

            while ((line = raf.readLine()) != null)
                contentAfterUpdate.append(line).append('\n');
            raf.seek(updatePos);
            raf.writeBytes(contentAfterUpdate.toString());
            if (fileShrinkAmount > 0) raf.setLength(raf.length() - fileShrinkAmount);
            System.out.println("Task successfully updated!");
        } catch (IOException ex) {
            System.err.println("ERROR: Error updating content!");
        }
    }
}