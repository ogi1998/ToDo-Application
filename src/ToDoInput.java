import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ToDoInput {
    static String readNewUser() {
        String response = "";
        try (BufferedReader br = new BufferedReader(new InputStreamReader(System.in))) {
            response = br.readLine();
        } catch (IOException ex) {
            System.err.println("ERROR: Can't read input!");
        }
        return response;
    }
}
