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
            raf.writeBytes(contentAfterDelete.toString());
            raf.setLength(raf.length() - deletedLineLength);
        } catch (IOException ex) {
            System.err.println("ERROR!");
        }
    }

    void editTask(String userID) {
            StringBuilder contentAfterUpdate = new StringBuilder(userID + ", todo, 05-09-2022 14:55:07, updateee, [HI]\n");

            long updatePos = 0;
            int fileSizeDecreaseAmount = 0;

            boolean isIDFound = false;

        try (RandomAccessFile raf = new RandomAccessFile(fileName, "rw")) {
            String line;
            while ((line = raf.readLine()) != null) {
                String id = line.split(",")[0].trim();


                if (userID.equals(id)) {
                    if (contentAfterUpdate.length() < line.length()) {
                        fileSizeDecreaseAmount = line.length() - contentAfterUpdate.length() + 1;
                    }
                    updatePos = raf.getFilePointer() - (line.length() + 1);
                    isIDFound = true;
                    continue;
                }
                if (isIDFound) contentAfterUpdate.append(line).append('\n');
            }
            raf.seek(updatePos);
            raf.writeBytes(contentAfterUpdate.toString());
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
}
