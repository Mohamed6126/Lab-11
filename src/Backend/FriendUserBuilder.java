package Backend;

import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * @author vip comp
 */
public class FriendUserBuilder implements UserBuilder {

    private String userID;
    private String username;
    private LocalDate dateOfBirth;
    private String status;
    private String bio;
    private BufferedImage profilePhoto;
    private BufferedImage coverPhoto;
    private String profilePhotoPath;
    private String coverPhotoPath;
    private ArrayList<Content> contents;
    private ArrayList<User> incomingRequests;
    private ArrayList<User> friends;
    private ArrayList<User> blockedUsers;
    private ArrayList<User> sentRequests;


    public UserBuilder setUserID(String userID) {
        this.userID = userID;
        return this;

    }

    public UserBuilder setEmail(String email) {
        return this;
    }

    @Override
    public UserBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    @Override
    public UserBuilder setPassword(String password) {
        return this;
    }

    public UserBuilder setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
        return this;
    }

    @Override
    public UserBuilder setStatus(String status) {
        this.status = status;
        return this;
    }

    public UserBuilder setBio(String bio) {
        this.bio = bio;
        return this;
    }

    public UserBuilder setProfilePhoto(BufferedImage profilePhoto) {
        this.profilePhoto = profilePhoto;
        return this;
    }

    public UserBuilder setCoverPhoto(BufferedImage coverPhoto) {
        this.coverPhoto = coverPhoto;
        return this;
    }

    public UserBuilder setProfilePhotoPath(String profilePhotoPath) {
        this.profilePhotoPath = profilePhotoPath;
        return this;
    }

    public UserBuilder setCoverPhotoPath(String coverPhotoPath) {
        this.coverPhotoPath = coverPhotoPath;
        return this;
    }

    public UserBuilder setContents(ArrayList<Content> contents) {
        if (contents != null) {
            this.contents = new ArrayList<>(contents);
        }
        return this;
    }



    public UserBuilder setFriends(ArrayList<User> friends) {
        if (friends != null) {
            this.friends = new ArrayList<>(friends);
        }
        return this;
    }

    public UserBuilder setBlockedUsers(ArrayList<User> blockedUsers) {
        if (blockedUsers != null) {
            this.blockedUsers = new ArrayList<>(blockedUsers);
        }
        return this;
    }

    public UserBuilder setIncomingRequests(ArrayList<User> incomingRequests) {
        if (incomingRequests != null) {
            this.incomingRequests = new ArrayList<>(incomingRequests);
        }
        return this;
    }

    public UserBuilder setSentRequests(ArrayList<User> sentRequests) {
        if (sentRequests != null) {
            this.sentRequests = new ArrayList<>(sentRequests);
        }
        return this;
    }


    @Override
    public User build() {
        return new User(userID, null, username, null, dateOfBirth, status, bio, profilePhoto, coverPhoto, profilePhotoPath, coverPhotoPath, contents,  friends, blockedUsers, incomingRequests, sentRequests);
    }
}
