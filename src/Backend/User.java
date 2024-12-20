package Backend;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static java.lang.Math.max;

/**
 * @author vip comp
 */
public class User {
    private String userID;
    private String email;
    private String username;
    private String password;
    private LocalDate dateOfBirth;
    private String status = "Offline";
    private String bio;
    private BufferedImage profilePhoto;
    private BufferedImage coverPhoto;
    private String profilePhotoPath;
    private String coverPhotoPath;
    private ArrayList<Content> contents;
    private ArrayList<User> friends;
    private ArrayList<User> blockedUsers;
    private ArrayList<User> incomingRequests;
    private ArrayList<User> sentRequests;
    private ArrayList<Group> groups;


    public User(String userID, String email, String username, String password, LocalDate dateOfBirth, String status, String bio, BufferedImage profilePhoto, BufferedImage coverPhoto, String profilePhotoPath, String coverPhotoPath, ArrayList<Content> contents, ArrayList<User> friends, ArrayList<User> blockedUsers, ArrayList<User> incomingRequests, ArrayList<User> sentRequests) {
        this.userID = userID;
        this.email = email;
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.status = status;
        this.bio = bio;
        this.profilePhoto = profilePhoto;
        this.coverPhoto = coverPhoto;
        this.profilePhotoPath = !profilePhotoPath.isEmpty() ? "CoverProfileImages/Profiles/" + userID + profilePhotoPath.substring(profilePhotoPath.lastIndexOf(".")) : "";
        this.coverPhotoPath = !coverPhotoPath.isEmpty() ? "CoverProfileImages/Covers/" + userID + coverPhotoPath.substring(coverPhotoPath.lastIndexOf(".")) : "";
        this.contents = contents;
        this.friends = friends;
        this.blockedUsers = blockedUsers;
        this.incomingRequests = incomingRequests;
        this.sentRequests = sentRequests;
        this.groups = new ArrayList<>();

    }


    public ArrayList<Group> getGroups() {
        return groups;
    }


    public void addGroup(Group group) {
        groups.add(group);
    }


    public void removeGroup(Group group) {
        groups.remove(group);
    }


    public boolean isPrimaryAdmin(Group group) {
        return group.getAdmins().get(0).equals(this);
    }


    public boolean isAdmin(Group group) {
        return group.getAdmins().contains(this);
    }


    public boolean isMember(Group group) {
        return group.getMembers().contains(this);
    }

    public boolean sendFriendRequest(User user) {
        Notification manager = new Notification();
        if (user == null || user == this || blockedUsers.contains(user) || friends.contains(user)) {
            return false; // Invalid conditions
        }
        if (user.incomingRequests.add(this)) {
            manager.addNotificationToFile(user.getUserID(), "Friend Request", username + "sent a friend request");
            sentRequests.add(user);
            return true;
        }
        return false; // Already sent
    }

    public boolean acceptFriendRequest(User user) {
        if (incomingRequests.remove(user)) {
            friends.add(user);
            user.friends.add(this);
            user.sentRequests.remove(this);
            return true;
        }
        return false; // No pending request
    }

    public boolean declineFriendRequest(User user) {
        if (incomingRequests.remove(user)) {
            user.sentRequests.remove(this);
            return true;
        }
        return false;
    }

    public boolean blockUser(User user) {
        if (user == null || user == this) {
            return false; // Invalid conditions
        }
        if (blockedUsers.add(user)) {
            friends.remove(user);
            sentRequests.remove(user);
            incomingRequests.remove(user);
            user.friends.remove(this);
            user.sentRequests.remove(this);
            user.incomingRequests.remove(this);
            return true;
        }
        return false; // Already blocked
    }

    public boolean unblockUser(User user) {
        return blockedUsers.remove(user);
    }

    public boolean removeFriend(User user) {
        if (friends.remove(user)) {
            user.friends.remove(this); // Remove bidirectionally
            return true;
        }
        return false; // Not a friend
    }


    public String getUserID() {
        return userID;
    }

    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getStatus() {
        return status;
    }

    public String getBio() {
        return bio;
    }

    public BufferedImage getProfilePhoto() {
        return profilePhoto;
    }

    public BufferedImage getCoverPhoto() {
        return coverPhoto;
    }

    public String getProfilePhotoPath() {
        return profilePhotoPath;
    }

    public String getCoverPhotoPath() {
        return coverPhotoPath;
    }

    public ArrayList<Content> getContents() {
        return contents;
    }


    public ArrayList<User> getIncomingRequests() {
        return incomingRequests;
    }

    public ArrayList<User> getSentRequests() {
        return sentRequests;
    }

    public ArrayList<User> getBlockedUsers() {
        return blockedUsers;
    }

    public ArrayList<User> getFriends() {
        return friends;
    }

    public void editStatus(String status) {
        this.status = status;
    }

    public void editUsername(String username) {
        this.username = username;
    }

    public void editEmail(String email) {
        this.email = email;
    }

    public void editPassword(String password) {
        this.password = password;
    }

    public void editDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void editBio(String bio) {
        this.bio = bio;
    }


    public void addContent(String text, BufferedImage image, String imageFilePath, String type) {
        long maxContentID = 0;
        Notification manager = new Notification();
        for (Content content : contents) {
            maxContentID = max(maxContentID, Long.parseLong(content.getContentID()));
        }
        for (User user : friends) {
            manager.addNotificationToFile(user.getUserID(), "new post", userID + " added new post");
        }
        contents.add(new Content(String.valueOf(maxContentID + 1), userID, LocalDateTime.now(), text, image, imageFilePath, type));
    }

    public void removeContent(String contentID) {
        for (int i = 0; i < contents.size(); i++) {
            if (contents.get(i).getContentID().equals(contentID))
                contents.remove(i);
        }
    }

    public void removeProfilePhoto() {
        profilePhoto = null;
        profilePhotoPath = "";
    }

    public void removeCoverPhoto() {
        coverPhoto = null;
        coverPhotoPath = "";
    }


    public void editProfilePhoto(BufferedImage profilePhoto, String profilePhotoPath) {
        this.profilePhoto = profilePhoto;
        this.profilePhotoPath = "CoverProfileImages/Profiles/" + userID + profilePhotoPath.substring(profilePhotoPath.lastIndexOf("."));
    }

    public void editCoverPhoto(BufferedImage coverPhoto, String coverPhotoPath) {
        this.coverPhoto = coverPhoto;
        this.coverPhotoPath = "CoverProfileImages/Covers/" + userID + coverPhotoPath.substring(coverPhotoPath.lastIndexOf("."));
    }
    public boolean isFriend(String userName) {
    if (friends == null || friends.isEmpty()) {
        return false; 
    }
    for (User friend : friends) {
        if (friend.getUsername().equals(userName)) {
            return true; 
        }
    }
    return false; 
   }



    /*private String hashPass(String password) throws NoSuchAlgorithmException {
        passHasher pH = new passHasher();
        return pH.hashPassword(password);
    }*/

}
