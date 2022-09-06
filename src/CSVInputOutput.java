import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

interface DataManipulator {
        void manipulateData(String[] fields);
    }
public class CSVInputOutput {
    String filePath;
    CSVInputOutput(String filePath) {
        this.filePath = filePath;
    }
    void read(DataManipulator dm) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                String[] fields = line.split(",");
                dm.manipulateData(fields);
            }
        } catch (IOException ex) {
            System.err.println("ERROR: File can't be read!");
        }
    }


}
