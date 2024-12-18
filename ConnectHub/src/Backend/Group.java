/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Backend;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Metro
 */
public class Group {
    private String groupid;
    private String groupName;
    private String Description;
    private ArrayList<String> admins;
    private ArrayList<String> members;
    private ArrayList<String> membershipRequests;
    private ArrayList<Content> posts;


    public Group(String groupid, String groupName, String Description, ArrayList<String> admins, ArrayList<String> members, ArrayList<Content> posts, ArrayList<String> membershipRequests) {
        this.groupid = groupid;
        this.groupName = groupName;
        this.Description = Description;
        this.admins = admins;
        this.members = members;
        this.membershipRequests = membershipRequests;
        this.posts = posts;

    }


    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public ArrayList<String> getMembers() {
        return members;
    }

    public void setMembers(ArrayList<String> members) {
        this.members = members;
    }

    public ArrayList<String> getAdmins() {
        return admins;
    }

    public void setAdmins(ArrayList<String> admins) {
        this.admins = admins;
    }

    public ArrayList<String> getMembershipRequests() {
        return membershipRequests;
    }

    public void setMembershipRequests(ArrayList<String> membershipRequests) {
        this.membershipRequests = membershipRequests;
    }

    public ArrayList<Content> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Content> posts) {
        this.posts = posts;
    }


    public void addAdmin(String userID) {
        if (!admins.contains(userID)) {
            admins.add(userID);
        }
    }

    public void removeAdmin(String userID) {
        admins.remove(userID);
    }

    public void addMember(String userID) {
        if (!members.contains(userID)) {
            members.add(userID);
        }
    }

    public void removeMember(String userID) {
        members.remove(userID);
    }

    public boolean isPrimaryAdmin(User user) {
        return admins.getFirst().equals(user.getUserID());
    }


    //


    public void addPost(Content content) {
        posts.add(content);
    }

    public void removePost(Content content) {
        posts.remove(content);
    }


}
    
    

 