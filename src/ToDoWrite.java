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
        StringBuilder res = new StringBuilder("");
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;

            while ((line = br.readLine()) != null) {
                String id = line.split(",")[0].trim();

                if (userID.equals(id)) continue;

                res.append(line).append('\n');
            }
        } catch (IOException ex) {
            System.err.println("ERROR!");
        }

        try (FileWriter fw = new FileWriter(fileName)) {
          fw.write(res.toString());
        } catch (IOException ex) {
            System.err.println("ERRROR!");
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
