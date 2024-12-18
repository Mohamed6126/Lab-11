package Backend;


import java.awt.image.BufferedImage;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 *
 * @author vip comp
 */
public interface UserBuilder {

    UserBuilder setUserID(String userID);
    UserBuilder setEmail(String email);
    UserBuilder setUsername(String username);
    UserBuilder setPassword(String password);
    UserBuilder setDateOfBirth(LocalDate dateOfBirth);
    UserBuilder setStatus(String status);
    UserBuilder setBio(String bio);
    UserBuilder setProfilePhoto(BufferedImage profilePhoto);
    UserBuilder setCoverPhoto(BufferedImage coverPhoto);
    UserBuilder setProfilePhotoPath(String profilePhotoPath);
    UserBuilder setCoverPhotoPath(String coverPhotoPath);
    UserBuilder setContents(ArrayList<Content> contents);
    UserBuilder setFriends(ArrayList<User> friends);
    UserBuilder setBlockedUsers(ArrayList<User> blockedUsers);
    UserBuilder setIncomingRequests(ArrayList<User> incomingRequests);
    UserBuilder setSentRequests(ArrayList<User> sentRequests);





    User build();
}
