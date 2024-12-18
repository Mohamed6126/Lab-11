package Backend;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.time.LocalDateTime;

public class Content {

    private String contentID;
    private String userID;
    private LocalDateTime creationTime;
    private String text;
    private BufferedImage image;
    private String imageFilePath;
    private String type;


    public Content(String contentID, String userID, LocalDateTime creationTime, String text, BufferedImage image, String oldImageFilename, String type) {
        this.contentID = contentID;
        this.userID = userID;
        this.text = text;
        this.image = image;
        this.creationTime = creationTime;
        this.imageFilePath = "Images/" + userID + "/" + contentID + oldImageFilename.substring(oldImageFilename.lastIndexOf("."));
        this.type = type;
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



}
