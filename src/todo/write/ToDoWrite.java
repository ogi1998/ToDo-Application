package todo.write;

import todo.task.Task;
import todo.util.ToDoUtility;

import java.io.*;

public class ToDoWrite {
    String fileName;
    ToDoWriteHelpers helpers;
    public ToDoWrite(String fileName) {
        this.fileName = fileName;
        helpers = new ToDoWriteHelpers(fileName);
    }
    public void addTask(String command) {
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

    public void removeTask(String command) {
        StringBuilder contentAfterDelete = new StringBuilder();
        String idToRemove = command.substring(command.lastIndexOf(" ")).trim();

        long deletePos = helpers.findLinePositionById(idToRemove, 'd');


        if (deletePos == -1) {
            System.err.println("Wrong uid!");
            return;
        }
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
            if (deletePos == 0) {
                raf.setLength(0);
                System.out.println("Task successfully removed!");
                return;
            }

            raf.seek(deletePos);
            if ((char) raf.read() != '\n')
                raf.seek(deletePos);

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
    public void editTask(String command) {
        Task newTask = helpers.getUpdateData(command);
        StringBuilder contentAfterUpdate = new StringBuilder();
        long updatePos = helpers.findLinePositionById(newTask.getId(), 'u');

        if (updatePos == -1) {
            System.err.println("Wrong uid!");
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
                default -> {
                    System.err.println("Wrong command, try again!");
                    return;
                }
            }
            contentAfterUpdate.append(String.join(",", splittedLine)).append('\n');

            int fileShrinkAmount = line.length() - contentAfterUpdate.length() + 1;

            while ((line = raf.readLine()) != null)
                contentAfterUpdate.append(line).append('\n');

            contentAfterUpdate.deleteCharAt(contentAfterUpdate.length() - 1);
            raf.seek(updatePos);
            raf.writeBytes(contentAfterUpdate.toString());

            if (fileShrinkAmount > 0)
                raf.setLength(raf.length() - fileShrinkAmount);
            System.out.println("Task successfully updated!");
        } catch (IOException ex) {
            System.err.println("ERROR: Error updating content!");
        }
    }
}