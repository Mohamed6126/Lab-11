package Backend;

import java.io.IOException;
import java.time.LocalDateTime;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Metro
 */
public class Test {
    public static void main(String[] args) throws IOException {
      
        Session session = new Session();
      System.out.println("Test class executed.");
        String user1ID = "user1";
        String user2ID = "user2";
JSONArray messages = session.getMessages(user1ID, user2ID);

        if (messages.length() > 0) {
            System.out.println("Messages between " + user1ID + " and " + user2ID + ":");
            for (int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                System.out.println("Sender: " + message.getString("senderID"));
                System.out.println("Message: " + message.getString("message"));
                System.out.println("Timestamp: " + message.getString("timestamp"));
                System.out.println("---");
            }
        } else {
            System.out.println("No messages found between " + user1ID + " and " + user2ID);
        }
      
       
    }
}  