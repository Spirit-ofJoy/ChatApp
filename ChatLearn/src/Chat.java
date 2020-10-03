
import java.io.*;
import java.util.*;

public class Chat implements Serializable {
    private String message;
    private String from;

    public Chat(String message, String from) {
        this.message = message;
        this.from = from;
    }

    @Override
    public String toString() {
        return String.format("%s: \n%s\n", from, message);
    }
}