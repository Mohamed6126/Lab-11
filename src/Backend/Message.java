/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend;

import java.time.LocalDateTime;

/**
 *
 * @author Metro
 */
public class Message {
    private String senderID;
    private String recieverID;
    private String message;
    private LocalDateTime timestamp;

    public Message(String senderID, String recieverID, String message) {
        this.senderID = senderID;
        this.recieverID = recieverID;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getRecieverID() {
        return recieverID;
    }

    public void setRecieverID(String recieverID) {
        this.recieverID = recieverID;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

}


