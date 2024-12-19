package Backend;

import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;

/**
 * @author vip comp
 */
public class Session {
   
    private static final String CHAT_FILE = "Databases/chats.json";
    private final String groupsFilename = "Databases/Groups.json";
    private final String notificationFilePath = "Databases/Notification.json";

    private Database myDatabase;
    private ArrayList<Group> allGroups;


    public Session() {
        myDatabase = new Database();
        allGroups = new ArrayList<>();
        loadGroupsFromFile();
    }

    public User getLoggedInUser() {
        return myDatabase.getLoggedInUser();
    }

    public ArrayList<User> getOthers() {
        return myDatabase.getOthers();
    }

    public ArrayList<User> getSuggestedFriends() {
        return myDatabase.getSuggestedFriends();
    }

    public int login(String username, String password) {
        return myDatabase.login(username, password);
    }

    public ArrayList<Group> getAllGroups() {
        return allGroups;
    }

    // and do what? status change??
    public void logOut() {
        try {
            myDatabase.getLoggedInUser().editStatus("Offline");
            myDatabase.saveToFiles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public int signUp(String username, String email, LocalDate dateOfBirth, String password) {
        return myDatabase.signUp(username, email, dateOfBirth, password);
    }

    public void refresh() {
        try {
            myDatabase.saveToFiles();
            myDatabase.load(myDatabase.getLoggedInUser().getUserID());
            saveGroupsToFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public ArrayList<Group> getPrimaryAdminGroups() {

        ArrayList<Group> primaryAdminGroups = new ArrayList<>();

        for (int i = 0; i < allGroups.size(); i++)
            if (allGroups.get(i).getAdmins().getFirst().equals(getLoggedInUser().getUserID()))
                primaryAdminGroups.add(allGroups.get(i));

        return primaryAdminGroups;
    }

    public ArrayList<Group> getNormalAdminGroups() {
        ArrayList<Group> normalAdminGroups = new ArrayList<>();

        for (int i = 0; i < allGroups.size(); i++) {
            ArrayList<String> adminsIDs = allGroups.get(i).getAdmins();
            if (!(adminsIDs.size() == 1)) {
                for (int j = 1; j < adminsIDs.size(); j++) {
                    if (adminsIDs.get(j).equals(getLoggedInUser().getUserID()))
                        normalAdminGroups.add(allGroups.get(i));
                }

            }

        }

        return normalAdminGroups;
    }

    public ArrayList<Group> getMemberGroups() {
        ArrayList<Group> memberGroups = new ArrayList<>();

        for (int i = 0; i < allGroups.size(); i++) {
            ArrayList<String> membersID = allGroups.get(i).getMembers();
            if (membersID.contains(getLoggedInUser().getUserID()))
                memberGroups.add(allGroups.get(i));
        }
        return memberGroups;
    }


    JSONObject contentToJSON(Content content) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ContentID", content.getContentID());
        jsonObject.put("UserID", content.getUserID());
        jsonObject.put("CreationTime", content.getCreationTime());
        jsonObject.put("Text", content.getText());
        jsonObject.put("Type", content.getType());
        jsonObject.put("ImageFilePath", content.getImageFilename());
        return jsonObject;
    }

    ArrayList<Content> loadContents(JSONArray myJSONArray) {
        ArrayList<Content> contents = new ArrayList<>();

        String contentID;
        String userID;
        LocalDateTime creationTime;
        String text;
        BufferedImage image;
        String imageFilename;
        String type;

        for (int i = 0; i < myJSONArray.length(); i++) {

            JSONObject contentJSONObject = myJSONArray.getJSONObject(i);
            contentID = contentJSONObject.getString("ContentID");
            userID = contentJSONObject.getString("UserID");
            creationTime = LocalDateTime.parse(contentJSONObject.getString("CreationTime"));
            text = contentJSONObject.getString("Text");
            imageFilename = contentJSONObject.getString("ImageFilePath");
            type = contentJSONObject.getString("Type");
            try {
                image = ImageIO.read(new File(imageFilename));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            contents.add(new Content(contentID, userID, creationTime, text, image, imageFilename, type));
        }
        return contents;
    }


    public JSONArray readGroupsFile() {
        BufferedReader myBufferedReader;
        StringBuilder wholeString = new StringBuilder();
        try {
            myBufferedReader = new BufferedReader(new FileReader(groupsFilename));
            String line;
            while ((line = myBufferedReader.readLine()) != null) {
                wholeString.append(line);
            }
            myBufferedReader.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new JSONArray(wholeString.toString());
    }


    private void loadGroupsFromFile() {
        JSONArray groupsArray = readGroupsFile();

        for (int i = 0; i < groupsArray.length(); i++) {
            JSONObject groupObject = groupsArray.getJSONObject(i);
            String groupID = groupObject.getString("groupID");
            String groupName = groupObject.getString("groupName");
            String groupDescription = groupObject.getString("description");
            ArrayList<String> members = new ArrayList<>();
            ArrayList<String> admins = new ArrayList<>();
            ArrayList<String> membershipRequests = new ArrayList<>();
            ArrayList<Content> content = new ArrayList<>();

            for (int j = 0; j < groupObject.getJSONArray("members").length(); j++)
                members.add(groupObject.getJSONArray("members").getString(j));

            for (int j = 0; j < groupObject.getJSONArray("admins").length(); j++)
                admins.add(groupObject.getJSONArray("admins").getString(j));

            for (int j = 0; j < groupObject.getJSONArray("membershipRequests").length(); j++)
                membershipRequests.add(groupObject.getJSONArray("membershipRequests").getString(j));

            content = loadContents(groupObject.getJSONArray("posts"));

            Group group = new Group(groupID, groupName, groupDescription, admins, members, content, membershipRequests);
            allGroups.add(group);
        }

    }


    public void saveGroupsToFile() {


        JSONArray newGroupsArray = new JSONArray();


        for (int i = 0; i < allGroups.size(); i++) {
            Group group = allGroups.get(i);
            JSONObject groupObject = new JSONObject();

            groupObject.put("groupID", group.getGroupid());
            groupObject.put("groupName", group.getGroupName());
            groupObject.put("description", group.getDescription());

            JSONArray adminsArray = new JSONArray();
            for (String admin : group.getAdmins())
                adminsArray.put(admin);
            groupObject.put("admins", adminsArray);

            JSONArray membersArray = new JSONArray();
            for (String member : group.getMembers())
                membersArray.put(member);
            groupObject.put("members", membersArray);

            JSONArray postsArray = new JSONArray();
            for (Content post : group.getPosts()) {
                postsArray.put(contentToJSON(post));
            }
            groupObject.put("posts", postsArray);

            JSONArray requestArray = new JSONArray();
            for (String request : group.getMembershipRequests())
                requestArray.put(request);
            groupObject.put("membershipRequests", requestArray);

            newGroupsArray.put(groupObject);
        }


        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(groupsFilename));
            writer.write(newGroupsArray.toString(4));
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }


    public Group createGroup(String groupName, String description) {
        String groupID = "group" + (allGroups.size() + 1);
        Group newGroup = new Group(groupID, groupName, description, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
        newGroup.addAdmin(getLoggedInUser().getUserID());
        allGroups.add(newGroup);
        saveGroupsToFile();
        return newGroup;
    }


    public void addMemberToGroup(Group group, String memberID) {
        Notification manager = new Notification();
        for (String userID : group.getMembers()){
            manager.addNotificationToFile(userID,"group post","New member has been added " + group.getGroupid());
        }
        group.addMember(memberID);
        saveGroupsToFile();
    }

    public void removeMemberOrAdminFromGroup(Group group, String memberID) {

        group.removeMember(memberID);
        group.removeAdmin(memberID);
        saveGroupsToFile();
    }

    public void promoteToAdmin(Group group, String userID) {
        group.addAdmin(userID);
        group.removeMember(userID);
        saveGroupsToFile();
    }

    public void demoteAdmin(Group group, String userID) {
        group.removeAdmin(userID);
        group.addMember(userID);
        saveGroupsToFile();
    }


    public void addPostToGroup(Group group, String text, BufferedImage myImage, String imageFilename, String type) {
        Notification manager = new Notification();
        for (String userID : group.getMembers()){
            manager.addNotificationToFile(userID,"group post","New Post is added to group " + group.getGroupid());
        }
        getLoggedInUser().addContent(text, myImage, imageFilename, type);
        group.addPost(getLoggedInUser().getContents().getLast());
        saveGroupsToFile();
    }


    public void editPostInGroup(Group group, Content Post, String newText) {

        if (group.getPosts().contains(Post))
            Post.editText(newText);

        saveGroupsToFile();

    }

    public void removePostFromGroup(Group group, Content Post) {
        Notification manager = new Notification();
        for (String userID : group.getMembers()){
            manager.addNotificationToFile(userID,"group post","A post has been deleted " + group.getGroupid());
        }
        if (group.getPosts().contains(Post))
            group.removePost(Post);

        saveGroupsToFile();

    }


    public boolean deleteGroup(Group group) {
        allGroups.remove(group);
        saveGroupsToFile();
        return true;

    }

    public void leaveGroup(Group group) {
        Notification manager = new Notification();
        for (String userID : group.getMembers()){
            manager.addNotificationToFile(userID,"group post","This group has been deleted " + group.getGroupid());
        }
        group.removeMember(getLoggedInUser().getUserID());
        saveGroupsToFile();
    }

    public void requestMembership(Group group) {
        group.getMembershipRequests().add(getLoggedInUser().getUserID());
    }


    public void acceptMembershipRequest(Group group, String userToApproveID) {
        group.getMembers().add(userToApproveID);
        group.getMembershipRequests().remove(userToApproveID);
    }

    public void declineMembershipRequest(Group group, String userToDeclineID) {

        group.getMembershipRequests().remove(userToDeclineID);
    }
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
    
public void addMessage(String userID1, String userID2, String message) throws IOException {
    JSONObject chatData = readChatFile();  
    JSONArray chats = chatData.getJSONArray("chats");
    boolean chatExists = false;
    for (int i = 0; i < chats.length(); i++) {
        JSONObject chat = chats.getJSONObject(i);
        if ((chat.getString("user1ID").equals(userID1) && chat.getString("user2ID").equals(userID2)) || 
            (chat.getString("user1ID").equals(userID2) && chat.getString("user2ID").equals(userID1))) {

            JSONArray messages = chat.getJSONArray("messages");
            JSONObject newMessage = new JSONObject();
            newMessage.put("senderID", userID1);
            newMessage.put("message", message);
            newMessage.put("timestamp", java.time.LocalDateTime.now().toString());
            messages.put(newMessage);

            chatExists = true;
            break;
        }
    }

    if (!chatExists) {
        JSONObject newChat = new JSONObject();
        newChat.put("user1ID", userID1);
        newChat.put("user2ID", userID2);
        JSONArray newMessages = new JSONArray();
        JSONObject newMessage = new JSONObject();
        newMessage.put("senderID", userID1);
        newMessage.put("message", message);
        newMessage.put("timestamp", java.time.LocalDateTime.now().toString());
        newMessages.put(newMessage);
        newChat.put("messages", newMessages);
        chats.put(newChat);
    }

    try (FileWriter file = new FileWriter(CHAT_FILE)) {
        file.write(chatData.toString(4));  
    }
}
    private JSONObject readChatFile() throws IOException {
        File file = new File(CHAT_FILE);
        JSONObject chatData = new JSONObject();
        
        if (file.exists()) {
            
            try (FileReader reader = new FileReader(file)) {
                int i;
                StringBuilder jsonContent = new StringBuilder();
                while ((i = reader.read()) != -1) {
                    jsonContent.append((char) i);
                }
                chatData = new JSONObject(jsonContent.toString());
            }
        } else {
           
            chatData.put("chats", new JSONArray());
        }

        return chatData;
    }
    public JSONArray getMessages(String userID1, String userID2) throws IOException {
    JSONObject chatData = readChatFile();  
    JSONArray chats = chatData.getJSONArray("chats");

    for (int i = 0; i < chats.length(); i++) {
        JSONObject chat = chats.getJSONObject(i);
        if ((chat.getString("user1ID").equals(userID1) && chat.getString("user2ID").equals(userID2)) || 
            (chat.getString("user1ID").equals(userID2) && chat.getString("user2ID").equals(userID1))) {
            
            return chat.getJSONArray("messages");  
        }
    }
    return new JSONArray();  
}


}





