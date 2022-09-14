package todo.util;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public class ToDoUtility {

    private ToDoUtility() {}
    public static long dateToMillis(String date) {
        LocalDateTime localDT = LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        return localDT
                .atZone(ZoneId.systemDefault())
                .toInstant()
                .toEpochMilli();
    }

    public static String getCurrentDateAndTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        return dtf.format(LocalDateTime.now());
    }
}
