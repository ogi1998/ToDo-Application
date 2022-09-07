import java.io.*;
import java.nio.channels.FileChannel;

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
        String line;
        long deletePos = 0;
        boolean isIDFound = false;
        int deletedLineLength = 0;

        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
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
            raf.setLength(raf.length() - deletedLineLength);
            raf.writeBytes(contentAfterDelete.toString());
        } catch (IOException ex) {
            System.err.println("ERROR!");
        }
    }

    void editTask(String userID) {
            StringBuilder contentAfterUpdate = new StringBuilder(userID + ", todo, 05-09-2022 14:55:07, updated, [HI]\n");
            String line;
            long updatePos = 0;
            boolean isIDFound = false;

        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {

            while ((line = raf.readLine()) != null) {
                String id = line.split(",")[0].trim();

                if (isIDFound) contentAfterUpdate.append(line).append('\n');

                if (userID.equals(id)) {
                    // TO DO: Check when new line is bigger than old and when its small then old
                    // Change file size based on new line size
                    updatePos = raf.getFilePointer() - (line.length() + 1);
                    isIDFound = true;
                }
            }
            raf.seek(updatePos);
            raf.writeBytes(contentAfterUpdate.toString());
        } catch (IOException ex) {
            System.err.println("ERROR!");
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
}
