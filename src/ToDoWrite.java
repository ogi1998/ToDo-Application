import java.io.*;

public class ToDoWrite {
    String fileName;

    ToDoWrite(String fileName) {
        this.fileName = fileName;
    }

    void addTask(String command) {
        String name = command.substring(command.indexOf("\"") + 1, command.lastIndexOf("\""));
        StringBuilder newUser = new StringBuilder("\n");

        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
            newUser
                    .append(generateID(raf)).append(", pending, ")
                    .append(ToDoUtility.getCurrentDateAndTime())
                    .append(", ").append(name);

            if (!command.endsWith("\"")) {
                String priority = command.substring(command.lastIndexOf("\"") + 1);
                newUser.append(", ").append(priority);
            } else newUser.append(", [NORM]");

            raf.seek(raf.length());
            raf.writeBytes(newUser.toString());
        } catch (IOException ex) {
            System.err.println("ERROR: Can't write to file!");
        }
    }

    // TODO: Not working properly
    void removeTask1(String command) {
        StringBuilder contentAfterDelete = new StringBuilder();
        String idToRemove = command.substring(command.lastIndexOf(" ")).trim();
        long deletePos = findLinePositionById(idToRemove);

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
        } catch (IOException ex) {
            System.err.println("Error removing the task!");
        }
        finally {
            System.out.println("Task successfully removed");
        }
    }

    // todo task uid -e "buy more milk" ["HI"]
    void editTask(String command) {
//      StringBuilder contentAfterUpdate = new StringBuilder(userID + ", todo, 05-09-2022 14:55:07, updateee, [HI]\n");
        Task newTask = getUpdateData(command);
        StringBuilder newContent = new StringBuilder(newTask.getId());

        long updatePos = 0;
        int fileSizeDecreaseAmount = 0;

        boolean isIdFound = false;

        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
            String line;
            while ((line = raf.readLine()) != null) {
                // TODO Check for commandType and update task based on its argument
                String id = line.split(",")[0].trim();

                if (newTask.getId().equals(id)) {
                    if (newContent.length() < line.length()) {
                        fileSizeDecreaseAmount = line.length() - newContent.length() + 1;
                    }
                    updatePos = raf.getFilePointer() - (line.length() + 1);
                    isIdFound = true;
                    continue;
                }
                if (isIdFound) newContent.append(line).append('\n');
            }
            raf.seek(updatePos);
            raf.writeBytes(newContent.toString());
            raf.setLength(raf.length() - fileSizeDecreaseAmount);
        } catch (IOException ex) {
            System.err.println("ERROR: Error updating content!");
        } finally {
            System.out.println("Data successfully updated!");
        }
    }

    int generateID(RandomAccessFile raf) throws IOException {
        StringBuilder userID = new StringBuilder();
        char currentChar;

        raf.seek(raf.length() - 1);

        for (long pointer = raf.length() - 1; pointer >= 0; pointer--) {
            raf.seek(pointer);

            if ((char) raf.read() == '\n') break;
        }
        while ((currentChar = (char) raf.read()) != ',') userID.append(currentChar);

        return Integer.parseInt(userID.toString()) + 1;
    }

    long findLinePositionById(String id) {
        long position = -1;

        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
            String line;

            while ((line = raf.readLine()) != null) {
                String lineId = line.substring(0, line.indexOf(",")).trim();

                if (id.equals(lineId)) {
                    position = raf.getFilePointer() - line.length() - 1;
                    break;
                }
                if (Integer.parseInt(lineId) > Integer.parseInt(id) || raf.getFilePointer() == raf.length()) {
                    break;
                }
            }
        } catch (IOException ex) {
            System.err.println("Error finding id");
        }
        return position;
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
