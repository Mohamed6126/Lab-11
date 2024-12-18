package Backend;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static java.lang.Math.max;

public class Notification {
    private final String notificationFilePath = "Databases/Notification.json";

    public JSONArray readDatabaseFile() {
        try {
            String content = new String(Files.readAllBytes(Paths.get(notificationFilePath))).trim();
            if (content.isEmpty()) {
                content = "[]";
            }
            return new JSONArray(content);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error reading the notification file.");
        }
    }

    public List<JSONObject> loadNotifications(String userID) {
        JSONArray mainJSONArray = readDatabaseFile();
        List<JSONObject> notifications = new ArrayList<>();

        for (int i = 0; i < mainJSONArray.length(); i++) {
            JSONObject notificationObject = mainJSONArray.getJSONObject(i);
            if (notificationObject.optString("UserID").equals(userID)) {
                notifications.add(notificationObject);
            }
        }
        return notifications;
    }
    public void addNotificationToFile(String userID, String type, String message) {
        JSONArray notificationsArray = readDatabaseFile();
        JSONObject newNotification = new JSONObject();
        newNotification.put("UserID", userID);
        newNotification.put("Type", type);
        newNotification.put("Message", message);
        notificationsArray.put(newNotification);
        writeNotificationsToFile(notificationsArray);
    }


    private void writeNotificationsToFile(JSONArray notificationsArray) {
        try (FileWriter writer = new FileWriter(notificationFilePath)) {
            writer.write(notificationsArray.toString(2)); // Pretty-print JSON
            System.out.println("Notifications successfully written to file.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("An error occurred while writing to the file.");
        }
    }
}