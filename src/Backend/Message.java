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
    private String senderName;
    private String recieverName;
    private String message;
    private LocalDateTime timestamp;

    public Message(String senderName, String recieverName, String message) {
        this.senderName = senderName;
        this.recieverName = recieverName;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getsenderName() {
        return senderName;
    }

    public void setsenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getrecieverName() {
        return recieverName;
    }

    public void setrecieverName(String recieverName) {
        this.recieverName = recieverName;
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


