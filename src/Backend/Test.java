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
      
        String user1Name = "ahmed";
        String user2Name = "mohamed";
        String message="Aboelwafa";
        
    /*JSONArray messages = session.getMessages(user1Name, user2Name);

        if (messages.length() > 0) {
            System.out.println("Messages between " + user1Name + " and " + user2Name + ":");
            for (int i = 0; i < messages.length(); i++) {
                JSONObject message = messages.getJSONObject(i);
                System.out.println("SenderName: " + message.getString("senderName"));
                System.out.println("Message: " + message.getString("message"));
                System.out.println("Timestamp: " + message.getString("timestamp"));
                System.out.println("---");
            }
        } else {
            System.out.println("No messages found between " + user1Name + " and " + user2Name);
        }
      */
      session.addMessage(user1Name, user2Name, message);
       
    }
}  