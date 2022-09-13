import java.io.IOException;
import java.io.RandomAccessFile;

public class ToDoWriteHelpers {
    private final String fileName;

    ToDoWriteHelpers(String fileName) {
        this.fileName = fileName;
    }

    int generateID(RandomAccessFile raf) throws IOException {
        if (raf.length() == 0) return 1;

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
                    position = raf.getFilePointer() - (line.length() + 1);
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

        switch (newTask.getCommandType()) {
            case "-e" -> {
                String name = command.substring(command.indexOf("\""), command.lastIndexOf("\"") + 1);
                newTask.setName(name);
                if (command.endsWith("[HI]") || command.endsWith("[LO]")) {
                    String priority = command.substring(command.indexOf("["), command.indexOf("]") + 1);
                    if (priority.equals("[HI]"))
                        newTask.setPriority(1);
                    else if (priority.equals("[LO]"))
                        newTask.setPriority(-1);
                }
            }
            case "-d" -> newTask.setDone(1);
            case "-u" -> newTask.setDone(-1);
        }
        return newTask;
    }
}
