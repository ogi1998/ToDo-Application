import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ToDoUtility {
    static long dateToMillis(String date) {
        LocalDateTime localDT = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        return localDT.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
    }
}
