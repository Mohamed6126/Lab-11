package Backend;

import java.awt.image.BufferedImage;
import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Random;
import javax.imageio.ImageIO;

import static java.lang.Math.max;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

// TODO
//  password hashing in login,sign up function
public final class Database {

    private final String userPassFilename = "Databases/UserPass.json";
    private final String mainFilename = "Databases/Database.json";

    private User loggedInUser;
    String loggedInUserID = "user0";
    private String loggedInPassword;

    private final ArrayList<User> others = new ArrayList<>();

    private long lastUserID = 0;

    private ArrayList<User> friendsWholeSession = new ArrayList<>();

    ArrayList<String> otherSentToLogged = new ArrayList<>();
    ArrayList<String> otherIncomingFromLogged = new ArrayList<>();
    ArrayList<String> otherBlockedLogged = new ArrayList<>();
    ArrayList<String> otherBlockedByLogged = new ArrayList<>();

    public Database() {
    }

    public ArrayList<User> getSuggestedFriends() {

        ArrayList<User> suggestedFriends = new ArrayList<>();
        Random rand = new Random();
        for (int i = 0, n = 0; i < others.size() && n < 8; i++) {

            if (rand.nextBoolean()) {
                suggestedFriends.add(others.get(i));
                n++;
            }
        }
        return suggestedFriends;

    }

    public User getLoggedInUser() {
        return loggedInUser;
    }

    public ArrayList<User> getOthers() {
        return others;
    }

    public JSONArray readDatabaseFile() {
        BufferedReader myBufferedReader;
        StringBuilder wholeString = new StringBuilder();
        try {
            myBufferedReader = new BufferedReader(new FileReader(mainFilename));
            String line;
            while ((line = myBufferedReader.readLine()) != null) {
                wholeString.append(line);
            }
            myBufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return new JSONArray(wholeString.toString());
    }

    public JSONArray readUserPassFile() {
        BufferedReader myBufferedReader;
        StringBuilder wholeString = new StringBuilder();
        try {
            myBufferedReader = new BufferedReader(new FileReader(userPassFilename));
            String line;
            while ((line = myBufferedReader.readLine()) != null) {
                wholeString.append(line);
            }
            myBufferedReader.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        JSONArray myJSONArray = new JSONArray(wholeString.toString());
        for (int i = 0; i < myJSONArray.length(); i++) {
            String userID = myJSONArray.getJSONObject(i).getString("UserID");
            lastUserID = max(lastUserID, Long.parseLong(userID.substring(4)));
        }
        return myJSONArray;
    }

    // loads all the file as JSON but in memory put loggedin user and his relations only
    // create loggedin user and his friends, others then fix also get max ID of all ppl
    public void load(String userID) throws FileNotFoundException {

        JSONArray mainJSONArray = readDatabaseFile();

        for (int i = 0; i < mainJSONArray.length(); i++) {
            JSONObject JSONObjectIter = mainJSONArray.getJSONObject(i);

            String userIDIter = mainJSONArray.getJSONObject(i).getString("UserID");
            if (userIDIter.equals(userID)) {
                loggedInUserID = JSONObjectIter.getString("UserID");
                loggedInUser = createLoggedInUserFromJSON(JSONObjectIter, mainJSONArray);
            } else {
                others.add(createOtherUserFromJSON(JSONObjectIter));
            }
            lastUserID = max(lastUserID, Long.parseLong(userIDIter.substring(4)));
        }
        fixUserAndFriendsAndOthers();
    }

    // only used when creating logged in user and takes database JSONArray
    private JSONObject getJSONObjectFromID(String userID, JSONArray mainJSONArray) {
        for (int i = 0; i < mainJSONArray.length(); i++) {
            JSONObject JSONObjectIter = mainJSONArray.getJSONObject(i);
            if (JSONObjectIter.getString("UserID").equals(userID)) {
                return JSONObjectIter;
            }
        }
        return null;
    }

    JSONObject contentToJSON(Content content) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("ContentID", content.getContentID());
        jsonObject.put("UserID", content.getUserID());
        jsonObject.put("CreationTime", content.getCreationTime());
        jsonObject.put("Text", content.getText());
        jsonObject.put("Type", content.getType());
        jsonObject.put("ImageFilePath", content.getImageFilename());
        
        if (content.getType().equalsIgnoreCase("Post")) {
            
            jsonObject.put("Likes", content.getNumberOfLikes());

            HashMap <String, String> comments = content.getComments();
            JSONArray commentsJSONArray = new JSONArray();
            if(comments!=null){
                for (Map.Entry<String, String> entry : comments.entrySet()) {
                    System.out.println(entry.getValue());
                commentsJSONArray.put(entry.getKey() + "," + entry.getValue());
            }
            jsonObject.put("Comments", commentsJSONArray);
            }
            else{
                jsonObject.put("Comments", new JSONArray());
            }
        }
        //0 likes and empty JSONArray for stories
        else{
            jsonObject.put("Likes", content.getNumberOfLikes());
            jsonObject.put("Comments", new JSONArray());       
        }
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
            Content content = new Content(contentID, userID, creationTime, text, image, imageFilename, type);
            
            //only posts have likes and comments
            if (type.equalsIgnoreCase("post")) {
                int numberOfLikes;
                HashMap<String, String> comments = new HashMap<>();
                String concatenatedString;

                numberOfLikes = contentJSONObject.getInt("Likes");
                content.setNumberOfLikes(numberOfLikes);
                
                JSONArray commentsArray = contentJSONObject.getJSONArray("Comments");
                for (int j = 0; j < commentsArray.length(); j++) {
                    concatenatedString = commentsArray.getString(j);
                    String[] separatedString = concatenatedString.split(",");
                    String commentUserId = separatedString[0];
                    String comment = separatedString[1];
                    comments.put(commentUserId, comment);
                }
            content.setComments(comments);
            }
            //no need to set numberOfLikes or comments for story 
            //because they're already initialized in the constructor
            contents.add(content);
        }
        return contents;
    }

    // 0 for data and 1 for userpass
    public JSONObject createLoggedInJSONFromUser(User myUser, int type) {

        JSONObject userJSONObject = new JSONObject();
        userJSONObject.put("UserID", myUser.getUserID());
        userJSONObject.put("Username", myUser.getUsername());
        userJSONObject.put("Email", myUser.getEmail());

        switch (type) {
            case 0:
                userJSONObject.put("Status", myUser.getStatus());
                userJSONObject.put("DateOfBirth", myUser.getDateOfBirth().toString());
                userJSONObject.put("Bio", myUser.getBio());
                userJSONObject.put("ProfilePhotoPath", myUser.getProfilePhotoPath());
                userJSONObject.put("CoverPhotoPath", myUser.getCoverPhotoPath());

                JSONArray contents = new JSONArray();
                for (Content contentIter : myUser.getContents()) {
                    contents.put(contentToJSON(contentIter));
                }
                userJSONObject.put("Contents", contents);

                JSONArray friendsJSONArray = new JSONArray();
                for (User friend : myUser.getFriends()) {
                    friendsJSONArray.put(friend.getUserID());
                }
                userJSONObject.put("FriendsID", friendsJSONArray);

                JSONArray blockedJSONArray = new JSONArray();
                for (User blocked : myUser.getBlockedUsers()) {
                    friendsJSONArray.put(blocked.getUserID());
                }
                userJSONObject.put("BlockedUsersID", blockedJSONArray);

                JSONArray incomingFriendRequestsJSONArray = new JSONArray();
                for (User incoming : myUser.getIncomingRequests()) {
                    incomingFriendRequestsJSONArray.put(incoming.getUserID());
                }
                userJSONObject.put("IncomingFriendRequestsID", incomingFriendRequestsJSONArray);

                JSONArray sentFriendRequestsJSONArray = new JSONArray();
                for (User sent : myUser.getSentRequests()) {
                    sentFriendRequestsJSONArray.put(sent.getUserID());
                }
                userJSONObject.put("SentFriendRequestsID", sentFriendRequestsJSONArray);

                break;
            case 1:
                userJSONObject.put("Password", myUser.getPassword());
        }
        return userJSONObject;
    }

    // return 1 if success, 0 if username exists but pass is wrong, -1 if username or email doesn't exist
    // assigning pass here, hashing
    public int login(String username, String password) {

        JSONArray myJSONArray = readUserPassFile();

        for (int i = 0; i < myJSONArray.length(); i++) {
            JSONObject jsonObjectIter = myJSONArray.getJSONObject(i);

            if (username.equals(jsonObjectIter.getString("Username"))
                    || username.equals(jsonObjectIter.getString("Email"))) {
                if (password.equals(jsonObjectIter.getString("Password"))) {
                    loggedInPassword = jsonObjectIter.getString("Password");
                    try {
                        load(jsonObjectIter.getString("UserID"));
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                    return 1;
                } else {
                    return 0;
                }
            }
        }
        return -1;
    }

    // hashing
    public int signUp(String username, String email, LocalDate dateOfBirth, String password) {

        JSONArray myJSONArray = readUserPassFile();
        for (int i = 0; i < myJSONArray.length(); i++) {
            JSONObject jsonObjectIter = myJSONArray.getJSONObject(i);
            if (username.equals(jsonObjectIter.getString("Username"))
                    || username.equals(jsonObjectIter.getString("Email"))) {
                return 0;
            }
        }

        if (dateOfBirth.isAfter(java.time.LocalDate.now())) {
            return -1;
        }
        if (!(email.contains("@")) || !(email.contains(".")) || !(email.indexOf("@") <= email.indexOf(".") + 3)) {
            return -2;
        }
        // validate pass and return -3 if not enough

        // get last id and write to 2 files
        JSONObject newUserJSONObject = new JSONObject();
        long newID = lastUserID + 1;
        loggedInUserID = "user" + newID;

        newUserJSONObject.put("UserID", loggedInUserID);
        newUserJSONObject.put("Username", username);
        newUserJSONObject.put("Email", email);
        newUserJSONObject.put("Password", password);
        // newUser.put("Password", password);  hashing
        newUserJSONObject.put("DateOfBirth", dateOfBirth.toString());
        newUserJSONObject.put("Status", "Online");
        newUserJSONObject.put("Bio", "");
        newUserJSONObject.put("ProfilePhotoPath", "");
        newUserJSONObject.put("CoverPhotoPath", "");
        newUserJSONObject.put("Contents", new JSONArray());
        newUserJSONObject.put("FriendsID", new JSONArray());
        newUserJSONObject.put("BlockedUsersID", new JSONArray());
        newUserJSONObject.put("IncomingFriendRequestsID", new JSONArray());
        newUserJSONObject.put("SentFriendRequestsID", new JSONArray());

        loggedInUser = createLoggedInUserFromJSON(newUserJSONObject, myJSONArray);
        try {
            saveToFiles();
            login(loggedInUser.getUsername(), loggedInPassword);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }

    public User createLoggedInUserFromJSON(JSONObject userJSONObject, JSONArray mainJSONArray) {

        UserFactory myUserFactory = new LoggedUserFactory();
        UserBuilder myUserBuilder = myUserFactory.createUserBuilder();

        myUserBuilder.setUserID(userJSONObject.getString("UserID"))
                .setUsername(userJSONObject.getString("Username"))
                .setDateOfBirth(LocalDate.parse(userJSONObject.getString("DateOfBirth")))
                .setEmail(userJSONObject.getString("Email"))
                .setBio(userJSONObject.getString("Bio"))
                .setProfilePhotoPath(userJSONObject.getString("ProfilePhotoPath"))
                .setCoverPhotoPath(userJSONObject.getString("CoverPhotoPath"))
                .setContents(loadContents(userJSONObject.getJSONArray("Contents")));

        if (!userJSONObject.getString("ProfilePhotoPath").isEmpty()) {
            try {
                myUserBuilder.setProfilePhoto(ImageIO.read(new File(userJSONObject.getString("ProfilePhotoPath"))));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            myUserBuilder.setProfilePhoto(null);
        }

        if (!userJSONObject.getString("CoverPhotoPath").isEmpty()) {
            try {
                myUserBuilder.setCoverPhoto(ImageIO.read(new File(userJSONObject.getString("CoverPhotoPath"))));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            myUserBuilder.setCoverPhoto(null);
        }

        JSONArray friendsIDJSONArray = userJSONObject.getJSONArray("FriendsID");
        ArrayList<User> friends = new ArrayList<>();
        for (int i = 0; i < friendsIDJSONArray.length(); i++) {
            JSONObject friendJSONObject = getJSONObjectFromID(friendsIDJSONArray.getString(i), mainJSONArray);
            friends.add(createFriendUserFromJSON(friendJSONObject));
        }
        myUserBuilder.setFriends(friends);

        JSONArray blockedUsersIDJSONArray = userJSONObject.getJSONArray("BlockedUsersID");
        for (int i = 0; i < blockedUsersIDJSONArray.length(); i++) {
            otherBlockedByLogged.add(blockedUsersIDJSONArray.getString(i));
        }

        myUserBuilder.setBlockedUsers(new ArrayList<>());

        JSONArray incomingRequestsIDJSONArray = userJSONObject.getJSONArray("IncomingFriendRequestsID");
        for (int i = 0; i < incomingRequestsIDJSONArray.length(); i++) {
            otherSentToLogged.add(incomingRequestsIDJSONArray.getString(i));
        }
        myUserBuilder.setIncomingRequests(new ArrayList<>());

        JSONArray sentRequestsIDJSONArray = userJSONObject.getJSONArray("SentFriendRequestsID");
        for (int i = 0; i < sentRequestsIDJSONArray.length(); i++) {
            otherIncomingFromLogged.add(sentRequestsIDJSONArray.getString(i));
        }
        myUserBuilder.setSentRequests(new ArrayList<>());

        return myUserBuilder.build();
    }

    public User createFriendUserFromJSON(JSONObject friendJSONObject) {

        UserFactory myUserFactory = new FriendUserFactory();
        UserBuilder myUserBuilder = myUserFactory.createUserBuilder();
        myUserBuilder.setStatus(friendJSONObject.getString("Status"))
                .setUserID(friendJSONObject.getString("UserID"))
                .setUsername(friendJSONObject.getString("Username"))
                .setDateOfBirth(LocalDate.parse(friendJSONObject.getString("DateOfBirth")))
                .setBio(friendJSONObject.getString("Bio"))
                .setProfilePhotoPath(friendJSONObject.getString("ProfilePhotoPath"))
                .setCoverPhotoPath(friendJSONObject.getString("CoverPhotoPath"))
                .setContents(loadContents(friendJSONObject.getJSONArray("Contents")));

        try {
            myUserBuilder.setProfilePhoto(ImageIO.read(new File(friendJSONObject.getString("ProfilePhotoPath"))));
            myUserBuilder.setCoverPhoto(ImageIO.read(new File(friendJSONObject.getString("CoverPhotoPath"))));
        } catch (IOException e) {
            myUserBuilder.setProfilePhoto(null);
            myUserBuilder.setCoverPhoto(null);
        }

        myUserBuilder.setFriends(new ArrayList<>());
        myUserBuilder.setBlockedUsers(new ArrayList<>());
        myUserBuilder.setIncomingRequests(new ArrayList<>());
        myUserBuilder.setSentRequests(new ArrayList<>());

        return myUserBuilder.build();
    }

    public User createOtherUserFromJSON(JSONObject otherJSONObject) {

        UserFactory myUserFactory = new OtherUserFactory();
        UserBuilder myUserBuilder = myUserFactory.createUserBuilder();

        String currentOtherUserID = otherJSONObject.getString("UserID");

        myUserBuilder.setUserID(currentOtherUserID)
                .setUsername(otherJSONObject.getString("Username"))
                .setDateOfBirth(LocalDate.parse(otherJSONObject.getString("DateOfBirth")))
                .setBio(otherJSONObject.getString("Bio"))
                .setProfilePhotoPath(otherJSONObject.getString("ProfilePhotoPath"))
                .setCoverPhotoPath(otherJSONObject.getString("CoverPhotoPath"));

        try {
            myUserBuilder.setProfilePhoto(ImageIO.read(new File(otherJSONObject.getString("ProfilePhotoPath"))));
            myUserBuilder.setCoverPhoto(ImageIO.read(new File(otherJSONObject.getString("CoverPhotoPath"))));
        } catch (IOException e) {
            myUserBuilder.setProfilePhoto(null);
            myUserBuilder.setCoverPhoto(null);
        }

        myUserBuilder.setFriends(new ArrayList<>());
        myUserBuilder.setIncomingRequests(new ArrayList<>());
        myUserBuilder.setSentRequests(new ArrayList<>());
        myUserBuilder.setBlockedUsers(new ArrayList<>());

        // check if others blocked logged
        JSONArray blockedOfOtherJSONArray = otherJSONObject.getJSONArray("BlockedUsersID");
        for (int i = 0; i < blockedOfOtherJSONArray.length(); i++) {
            String blockedUserID = blockedOfOtherJSONArray.getString(i);
            if (blockedUserID.equals(loggedInUserID)) {
                otherBlockedLogged.add(currentOtherUserID);
            }
        }

        return myUserBuilder.build();
    }

    private void fixUserAndFriendsAndOthers() {

        // fix password
        loggedInUser.editPassword(loggedInPassword);

        // fix relation between logged and friends and add them to whole session list
        for (User friend : loggedInUser.getFriends()) {
            friend.getFriends().add(loggedInUser);
            friendsWholeSession.add(friend);
        }

        // fix relations between logged and others
        for (int i = 0; i < otherSentToLogged.size(); i++) {
            for (int j = 0; j < others.size(); j++) {
                User otherUserObject = others.get(j);
                if (otherSentToLogged.get(i).equals(otherUserObject.getUserID())) {
                    otherUserObject.getSentRequests().add(loggedInUser);
                    loggedInUser.getIncomingRequests().add(otherUserObject);
                }
            }

        }

        for (int i = 0; i < otherIncomingFromLogged.size(); i++) {
            for (int j = 0; j < others.size(); j++) {
                User otherUserObject = others.get(j);
                if (otherIncomingFromLogged.get(i).equals(otherUserObject.getUserID())) {
                    otherUserObject.getIncomingRequests().add(loggedInUser);
                    loggedInUser.getSentRequests().add(otherUserObject);
                }
            }
        }

        for (int i = 0; i < otherBlockedLogged.size(); i++) {
            for (int j = 0; j < others.size(); j++) {
                User otherUserObject = others.get(j);
                if (otherBlockedLogged.get(i).equals(otherUserObject.getUserID())) {
                    otherUserObject.getBlockedUsers().add(loggedInUser);
                }
            }

        }

        for (int i = 0; i < otherBlockedByLogged.size(); i++) {
            for (int j = 0; j < others.size(); j++) {
                User otherUserObject = others.get(j);
                if (otherBlockedByLogged.get(i).equals(otherUserObject.getUserID())) {
                    loggedInUser.getBlockedUsers().add(otherUserObject);
                }
            }

        }

    }

    public void saveToFiles() throws IOException {

        JSONArray mainJSONArray = readDatabaseFile();
        JSONArray userPassJSONArray = readUserPassFile();

        boolean done;

        // loop on all database json file
        for (int i = 0; i < mainJSONArray.length(); i++) {

            done = false;
            // json of each user
            JSONObject JSONObjectIter = mainJSONArray.getJSONObject(i);

            // if logged in user make json object to be with new logged user details
            if (JSONObjectIter.getString("UserID").equals(loggedInUserID)) {
                JSONObjectIter = createLoggedInJSONFromUser(loggedInUser, 0);
                done = true;
            }

            // if not logged in, check if json object is one of friends
            for (int j = 0; j < friendsWholeSession.size() && !done; j++) {
                User friend = friendsWholeSession.get(j);
                if (!((JSONObjectIter.getString("UserID").equals(friend.getUserID())))) {
                    continue; // JSONObject is not the friend of logged in
                }

                // check if friend friendship relation with logged user changed
                JSONArray friendsOfFriendJSONArray = JSONObjectIter.getJSONArray("FriendsID");
                for (int k = 0; k < friendsOfFriendJSONArray.length(); k++) {
                    if (friendsOfFriendJSONArray.get(k).equals(loggedInUserID)) {
                        friendsOfFriendJSONArray.remove(k);
                    }
                }
                for (int k = 0; k < friend.getFriends().size(); k++) {
                    friendsOfFriendJSONArray.put(friend.getFriends().get(k).getUserID());
                }
                JSONObjectIter.remove("FriendsID");
                JSONObjectIter.put("FriendsID", friendsOfFriendJSONArray);

                // check if friend incoming relation with logged user changed
                JSONArray incomingJSONArray = JSONObjectIter.getJSONArray("IncomingFriendRequestsID");
                for (int k = 0; k < incomingJSONArray.length(); k++) {
                    if (incomingJSONArray.get(k).equals(loggedInUserID)) {
                        incomingJSONArray.remove(k);
                    }
                }
                for (int k = 0; k < friend.getIncomingRequests().size(); k++) {
                    incomingJSONArray.put(friend.getIncomingRequests().get(k).getUserID());
                }
                JSONObjectIter.remove("IncomingFriendRequestsID");
                JSONObjectIter.put("IncomingFriendRequestsID", incomingJSONArray);

                // check if friend sent relation with logged user changed
                JSONArray sentJSONArray = JSONObjectIter.getJSONArray("SentFriendRequestsID");
                for (int k = 0; k < sentJSONArray.length(); k++) {
                    if (sentJSONArray.get(k).equals(loggedInUserID)) {
                        sentJSONArray.remove(k);
                    }
                }
                for (int k = 0; k < friend.getSentRequests().size(); k++) {
                    sentJSONArray.put(friend.getSentRequests().get(k).getUserID());
                }
                JSONObjectIter.remove("SentFriendRequestsID");
                JSONObjectIter.put("SentFriendRequestsID", sentJSONArray);

                // check if friend blocked relation with logged user changed
                JSONArray blockedJSONArray = JSONObjectIter.getJSONArray("BlockedUsersID");
                for (int k = 0; k < blockedJSONArray.length(); k++) {
                    if (blockedJSONArray.get(k).equals(loggedInUserID)) {
                        blockedJSONArray.remove(k);
                    }
                }
                for (int k = 0; k < friend.getBlockedUsers().size(); k++) {
                    blockedJSONArray.put(friend.getBlockedUsers().get(k).getUserID());
                }
                JSONObjectIter.remove("BlockedUsersID");
                JSONObjectIter.put("BlockedUsersID", blockedJSONArray);

                done = true;
            }

            // if not logged in or friend then other
            for (int j = 0; j < others.size() && !done; j++) {
                User otherUser = others.get(j);
                if (!otherUser.getUserID().equals(JSONObjectIter.getString("UserID"))) {
                    continue;
                }

                // check all relations with logged in for others like we did for friends
                JSONArray friendsOfOtherJSONArray = JSONObjectIter.getJSONArray("FriendsID");
                for (int k = 0; k < friendsOfOtherJSONArray.length(); k++) {
                    if (friendsOfOtherJSONArray.get(k).equals(loggedInUserID)) {
                        friendsOfOtherJSONArray.remove(k);
                    }
                }
                for (int k = 0; k < otherUser.getFriends().size(); k++) {
                    friendsOfOtherJSONArray.put(otherUser.getFriends().get(k).getUserID());
                }
                JSONObjectIter.remove("FriendsID");
                JSONObjectIter.put("FriendsID", friendsOfOtherJSONArray);

                JSONArray incomingJSONArray = JSONObjectIter.getJSONArray("IncomingFriendRequestsID");
                for (int k = 0; k < incomingJSONArray.length(); k++) {
                    if (incomingJSONArray.get(k).equals(loggedInUserID)) {
                        incomingJSONArray.remove(k);
                    }
                }
                for (int k = 0; k < otherUser.getIncomingRequests().size(); k++) {
                    incomingJSONArray.put(otherUser.getIncomingRequests().get(k).getUserID());
                }
                JSONObjectIter.remove("IncomingFriendRequestsID");
                JSONObjectIter.put("IncomingFriendRequestsID", incomingJSONArray);

                JSONArray sentJSONArray = JSONObjectIter.getJSONArray("SentFriendRequestsID");
                for (int k = 0; k < sentJSONArray.length(); k++) {
                    if (sentJSONArray.get(k).equals(loggedInUserID)) {
                        sentJSONArray.remove(k);
                    }
                }
                for (int k = 0; k < otherUser.getSentRequests().size(); k++) {
                    sentJSONArray.put(otherUser.getSentRequests().get(k).getUserID());
                }
                JSONObjectIter.remove("SentFriendRequestsID");
                JSONObjectIter.put("SentFriendRequestsID", sentJSONArray);

                JSONArray blockedJSONArray = JSONObjectIter.getJSONArray("BlockedUsersID");
                for (int k = 0; k < blockedJSONArray.length(); k++) {
                    if (blockedJSONArray.get(k).equals(loggedInUserID)) {
                        blockedJSONArray.remove(k);
                    }
                }
                for (int k = 0; k < otherUser.getBlockedUsers().size(); k++) {
                    blockedJSONArray.put(otherUser.getBlockedUsers().get(k).getUserID());
                }
                JSONObjectIter.remove("BlockedUsersID");
                JSONObjectIter.put("BlockedUsersID", blockedJSONArray);
                done = true;
            }

            mainJSONArray.put(i, JSONObjectIter); // replace json object in database file with edited one
        }

        // modify json of userpass file
        for (int i = 0; i < userPassJSONArray.length(); i++) {
            JSONObject JSONObjectIter = userPassJSONArray.getJSONObject(i);
            if (JSONObjectIter.getString("UserID").equals(loggedInUserID)) {
                userPassJSONArray.remove(i);
                userPassJSONArray.put(createLoggedInJSONFromUser(loggedInUser, 1));
                break;
            }
        }

        FileWriter databaseWriter = new FileWriter(mainFilename);
        databaseWriter.write(mainJSONArray.toString(4));
        databaseWriter.close();

        FileWriter userPassWriter = new FileWriter(userPassFilename);
        userPassWriter.write(userPassJSONArray.toString(4));
        userPassWriter.close();

        // save images of content and cover, profile
        File profilesDir = new File("CoverProfileImages/Profiles");
        File coversDir = new File("CoverProfileImages/Covers");
        File[] profileFiles = profilesDir.listFiles();
        File[] coverFiles = coversDir.listFiles();

        // delete old ones in all cases
        for (int i = 0; i < profileFiles.length; i++) {
            File profileFile = profileFiles[i];
            String profileFileName = profileFile.getName();
            if (profileFileName.substring(0, profileFileName.lastIndexOf('.')).equals(loggedInUserID)) {
                profileFile.delete();
            }
        }
        for (int i = 0; i < coverFiles.length; i++) {
            File coverFile = coverFiles[i];
            String coverFileName = coverFile.getName();
            if (coverFileName.substring(0, coverFileName.lastIndexOf('.')).equals(loggedInUserID)) {
                coverFile.delete();
            }
        }

        // save new ones 
        try {
            BufferedImage newProfilePhoto = loggedInUser.getProfilePhoto();
            BufferedImage newCoverPhoto = loggedInUser.getCoverPhoto();

            if (newProfilePhoto != null) {
                String newProfilePhotoPath = loggedInUser.getProfilePhotoPath();
                String extension = newProfilePhotoPath.substring(newProfilePhotoPath.lastIndexOf('.') + 1);
                File outputfile = new File(loggedInUser.getProfilePhotoPath());
                ImageIO.write(newProfilePhoto, extension, outputfile);
            }
            if (newCoverPhoto != null) {
                String newCoverPhotoPath = loggedInUser.getCoverPhotoPath();
                String extension = newCoverPhotoPath.substring(newCoverPhotoPath.lastIndexOf('.') + 1);
                File outputfile = new File(loggedInUser.getCoverPhotoPath());
                ImageIO.write(newCoverPhoto, extension, outputfile);

            }
        } catch (IOException e) {
        }

        File dir = new File("Images/" + loggedInUser.getUserID());
        File[] filesList = dir.listFiles();

        for (int i = 0; i < filesList.length; i++) {
            filesList[i].delete();
        }

        ArrayList<Content> myContent = loggedInUser.getContents();
        for (Content contentItem : myContent) {
            File myFile = new File(contentItem.getImageFilename());
            String extension = contentItem.getImageFilename().substring(contentItem.getImageFilename().lastIndexOf('.') + 1);
            BufferedImage newImage = contentItem.getImage();
            ImageIO.write(newImage, extension, myFile);
        }

    }

}
