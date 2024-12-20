package Backend;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;
import java.util.HashMap;

public class Content {

    private String contentID;
    private String userID;
    private LocalDateTime creationTime;
    private String text;
    private BufferedImage image;
    private String imageFilePath;
    private String type;
    private int numberOfLikes;
    //commenter's ID(key) and comment(value)
    private HashMap<String, String> comments; 

    public Content(String contentID, String userID, LocalDateTime creationTime, String text, BufferedImage image, String oldImageFilename, String type) {
        this.contentID = contentID;
        this.userID = userID;
        this.text = text;
        this.image = image;
        this.creationTime = creationTime;
        this.imageFilePath = "Images/" + userID + "/" + contentID + oldImageFilename.substring(oldImageFilename.lastIndexOf("."));
        this.type = type;
        this.numberOfLikes = 0;
        this.comments = new HashMap<>();
    }

    public String getContentID() {
        return contentID;
    }

    public String getUserID() {
        return userID;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public String getText() {
        return text;
    }

    public BufferedImage getImage() {
        return image;
    }

    public String getImageFilename() {
        return imageFilePath;
    }

    public void editText(String text) { // update creation date
        this.text = text;
        creationTime = java.time.LocalDateTime.now();
    }

    public void editImage(BufferedImage image, String oldImageFilename) {
        this.image = image;
        creationTime = java.time.LocalDateTime.now();
        this.imageFilePath = "Images/" + userID + "/" + contentID + oldImageFilename.substring(oldImageFilename.lastIndexOf("."));
    }

    public void editTextImage(String text, BufferedImage image, String oldImageFilename) {
        this.text = text;
        this.image = image;
        this.imageFilePath = "Images/" + userID + "/" + contentID + oldImageFilename.substring(oldImageFilename.lastIndexOf("."));
    }

    public String getType() {
        return type;
    }

    public int getNumberOfLikes() {
        return numberOfLikes;
    }

    public void setNumberOfLikes(int numberOfLikes) {
        this.numberOfLikes = numberOfLikes;
    }

    public HashMap<String, String> getComments() {
        if (this.comments.isEmpty()) {
            return null;
        }
        return comments;
    }

    public void setComments(HashMap<String, String> comments) {
        this.comments = comments;
    }

    //adds the comment to the hashmap along with the commenter's id
    public void addComment(String commenterId, String comment) {
        if (!comment.isEmpty()) {
            this.comments.put(commenterId, comment);
        }
    }
    
    public Content getContentById(String contentID)
    {
        if(this.contentID.equalsIgnoreCase(contentID))
            return this;
        else return null;
    }
}
