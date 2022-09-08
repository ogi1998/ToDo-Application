import java.io.*;

public class ToDoWrite {
    String fileName;

    ToDoWrite(String fileName) {
        this.fileName = fileName;
    }

    void addTask() {
        StringBuilder newUser = new StringBuilder("\n");
        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
            newUser
                    .append(generateID(raf))
                    .append(", pending, ")
                    .append(ToDoUtility.getCurrentDateAndTime())
                    .append(", ")
                    .append("some random task")
                    .append(", [NORM]");

            raf.seek(raf.length());
            raf.writeBytes(newUser.toString());

        } catch (IOException ex) {
            System.err.println("ERROR: Can't write to file!");
        }
    }

    void removeTask(String userID) {
        StringBuilder contentAfterDelete = new StringBuilder();

        long deletePos = 0;
        int deletedLineLength = 0;

        boolean isIDFound = false;

        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
            String line;
            while ((line = raf.readLine()) != null) {
                String id = line.split(",")[0].trim();

                if (isIDFound) contentAfterDelete.append(line).append('\n');
                if (userID.equals(id)) {
                    deletePos = raf.getFilePointer() - (line.length() + 1);
                    deletedLineLength = line.length();
                    isIDFound = true;
                }
            }
            raf.seek(deletePos);
            raf.writeBytes(contentAfterDelete.toString());
            raf.setLength(raf.length() - deletedLineLength);
        } catch (IOException ex) {
            System.err.println("ERROR!");
        }
    }

    // todo task uid -e "buy more milk" ["HI"]
    void editTask(String command) {

//      StringBuilder contentAfterUpdate = new StringBuilder(userID + ", todo, 05-09-2022 14:55:07, updateee, [HI]\n");
        String[] updateData = getUpdateData(command);
        StringBuilder newContent = new StringBuilder();

        long updatePos = 0;
        int fileSizeDecreaseAmount = 0;

        boolean isIDFound = false;

        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
            String line;
            while ((line = raf.readLine()) != null) {
                String id = line.split(",")[0].trim();

                if (enteredId.equals(id)) {
                    if (newContent.length() < line.length()) {
                        fileSizeDecreaseAmount = line.length() - newContent.length() + 1;
                    }
                    updatePos = raf.getFilePointer() - (line.length() + 1);
                    isIDFound = true;
                    continue;
                }
                if (isIDFound) newContent.append(line).append('\n');
            }
            raf.seek(updatePos);
            raf.writeBytes(newContent.toString());
            raf.setLength(raf.length() - fileSizeDecreaseAmount);
        } catch (IOException ex) {
            System.err.println("ERROR: Error updating content!");
        }
        finally {
            System.out.println("Data successfully updated!");
        }
    }

    int generateID(RandomAccessFile raf) throws IOException {
        StringBuilder userID = new StringBuilder("");
        char currentChar;

        raf.seek(raf.length() - 1);

        for (long pointer = raf.length() - 1; pointer >= 0; pointer--) {
            raf.seek(pointer);

            if ((char) raf.read() == '\n') break;
        }

        while ((currentChar = (char) raf.read()) != ',') userID.append(currentChar);

        return Integer.parseInt(userID.toString()) + 1;
    }

    Task getUpdateData(String command) {
        Task newTask = new Task();
        String[] commandArgs = command.split(" ");

        newTask.setCommandType(commandArgs[3].trim());
        newTask.setId(commandArgs[2].trim());

        if (newTask.getCommandType().equals("-e")) {
            newTask.setName(commandArgs[4].trim());

            if (commandArgs.length == 6) {
                newTask.setPriority(commandArgs[5].trim());

            } else if (newTask.getCommandType().equals("-d")) {
                newTask.setDone(true);
            } else newTask.setDone(false);
        }
        return newTask;
    }
}
