package FrontEnd;

import Backend.Content;
import Backend.Group;
import Backend.Session;
import Backend.User;
import org.json.JSONObject;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * @author vip comp
 */
public class NewsFeed extends javax.swing.JFrame {
    private static Session S;

    /**
     * Creates new form NewsFeed
     */
    public NewsFeed(Session S) {
        initComponents();
        this.S = S;
        //Display friends posts
        ArrayList<User> toDisplay = S.getLoggedInUser().getFriends();
        postsPanel.setLayout(new BoxLayout(postsPanel, BoxLayout.Y_AXIS));
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        for (User i : toDisplay) {
            ArrayList<Content> friendContent = i.getContents();
            for (Content c : friendContent) {
                if (c.getType().equals("Post")) {
                    JPanel postPanel = new JPanel();
                    postPanel.setLayout(new java.awt.BorderLayout());
                    JLabel authorName = new JLabel(i.getUsername());
                    postPanel.add(authorName, java.awt.BorderLayout.NORTH);


                    if (c.getImage() != null) {
                        ImageIcon imageIcon = new ImageIcon(c.getImage());
                        JLabel imageLabel = new JLabel();
                        Image scaledImage = imageIcon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH);
                        imageLabel.setIcon(new ImageIcon(scaledImage));
                        postPanel.add(imageLabel, java.awt.BorderLayout.CENTER);
                    }
                    JLabel postText = new JLabel(c.getText());
                    postPanel.add(postText, java.awt.BorderLayout.SOUTH);
                    
                    JPanel buttonPanel = new JPanel();
                    buttonPanel.setLayout(new java.awt.FlowLayout()); // Arrange buttons in a row

                    // Create buttons
                    JButton likeButton = new JButton("Like");
                    JButton addCommentButton = new JButton("Add comment");
                    JButton showCommentsButton = new JButton("Show comments");

                    likeButton.addActionListener(e -> {
                        System.out.println("Liked post by: " + S.getLoggedInUser().getUsername());
                        c.setNumberOfLikes(c.getNumberOfLikes()+1);
                        S.addNotificationToFile(c.getUserID(), "Like",S.getLoggedInUser().getUsername()+ " liked your post");
                    });
                    
                    addCommentButton.addActionListener(e -> {
                    String comment = JOptionPane.showInputDialog("Enter your comment:");
                    if (comment != null && !comment.trim().isEmpty()) {
                        System.out.println("Comment saying: " + comment + "added");
                        c.addComment(S.getLoggedInUser().getUserID(), comment);
                        S.addNotificationToFile(c.getUserID(), "Comment", S.getLoggedInUser().getUsername()+ " commented on your post");
                    }
                    });
                    
                    showCommentsButton.addActionListener(e -> {
                        // Assuming there's a method to fetch comments for the post
                        HashMap<String,String> comments = c.getComments(); // Replace with actual method
                        if (comments != null && !comments.isEmpty()) {
                            System.out.println("Comments for post by " + i.getUsername() + ":");
                            for (String line : comments.keySet()) {
                                System.out.println("Comment from: " + line + ", Saying: " + comments.get(line));
                            }
                        } else {
                            System.out.println("No comments yet!");
                        }
                    });
                    // Add buttons to the button panel
                    buttonPanel.add(likeButton);
                    buttonPanel.add(addCommentButton);
                    buttonPanel.add(showCommentsButton);

                    // Add button panel to the main post panel
                    postPanel.add(buttonPanel, java.awt.BorderLayout.SOUTH);
                    
                    postsPanel.add(postPanel);
                } else {
                    JPanel storyPanel = new JPanel();
                    storyPanel.setLayout(new java.awt.BorderLayout());
                    JLabel authorName = new JLabel(i.getUsername());
                    storyPanel.add(authorName, java.awt.BorderLayout.NORTH);

                    ImageIcon imageIcon = new ImageIcon(c.getImage());
                    JLabel imageLabel = new JLabel();
                    Image scaledImage = imageIcon.getImage().getScaledInstance(50, 50, Image.SCALE_SMOOTH);
                    imageLabel.setIcon(new ImageIcon(scaledImage));
                    storyPanel.add(imageLabel, java.awt.BorderLayout.CENTER);
                    System.out.println("Story says: " + c.getText());
                    JLabel postText = new JLabel(c.getText());
                    storyPanel.add(postText, java.awt.BorderLayout.SOUTH);
                    sidePanel.add(storyPanel);
                }
            }
        }

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        addFriend = new javax.swing.JButton();
        viewRquests = new javax.swing.JButton();
        notificationsButton = new javax.swing.JButton();
        addPost = new javax.swing.JButton();
        addStory = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        postsPanel = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        sidePanel = new javax.swing.JPanel();
        searchField = new javax.swing.JTextField();
        searchButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jButton1.setFont(new java.awt.Font("SF Pro Text", 0, 12)); // NOI18N
        jButton1.setText("Edit Profile");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        addFriend.setFont(new java.awt.Font("SF Pro Text", 0, 12)); // NOI18N
        addFriend.setText("Add friend");
        addFriend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addFriendActionPerformed(evt);
            }
        });

        viewRquests.setFont(new java.awt.Font("SF Pro Text", 0, 12)); // NOI18N
        viewRquests.setText("View Friend Requests");
        viewRquests.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                viewRquestsActionPerformed(evt);
            }
        });

        notificationsButton.setFont(new java.awt.Font("SF Pro Text", 0, 12)); // NOI18N
        notificationsButton.setText("Notifications");
        notificationsButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                notificationsButtonActionPerformed(evt);
            }
        });

        addPost.setFont(new java.awt.Font("SF Pro Text", 0, 12)); // NOI18N
        addPost.setText("Add Post");
        addPost.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addPostActionPerformed(evt);
            }
        });

        addStory.setFont(new java.awt.Font("SF Pro Text", 0, 12)); // NOI18N
        addStory.setText("Add Story");
        addStory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addStoryActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("SF Pro Text", 0, 12)); // NOI18N
        jButton7.setText("Logout");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setFont(new java.awt.Font("SF Pro Text", 0, 12)); // NOI18N
        jButton8.setText("Refresh");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("SF Pro Text", 0, 16)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(31, 80, 154));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Connect Hub");

        jScrollPane3.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane3.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        javax.swing.GroupLayout postsPanelLayout = new javax.swing.GroupLayout(postsPanel);
        postsPanel.setLayout(postsPanelLayout);
        postsPanelLayout.setHorizontalGroup(
                postsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 314, Short.MAX_VALUE)
        );
        postsPanelLayout.setVerticalGroup(
                postsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 444, Short.MAX_VALUE)
        );

        jScrollPane3.setViewportView(postsPanel);

        jScrollPane4.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        jScrollPane4.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

        javax.swing.GroupLayout sidePanelLayout = new javax.swing.GroupLayout(sidePanel);
        sidePanel.setLayout(sidePanelLayout);
        sidePanelLayout.setHorizontalGroup(
                sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 0, Short.MAX_VALUE)
        );
        sidePanelLayout.setVerticalGroup(
                sidePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 454, Short.MAX_VALUE)
        );

        jScrollPane4.setViewportView(sidePanel);

        searchField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchFieldActionPerformed(evt);
            }
        });

        searchButton.setFont(new java.awt.Font("SF Pro Text", 0, 12)); // NOI18N
        searchButton.setText("Search");
        searchButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                searchButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                        .addComponent(searchField)
                                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                        .addComponent(addStory, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButton1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 162, Short.MAX_VALUE)
                                                        .addComponent(addPost, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .addGap(6, 6, 6)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                                        .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                        .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.LEADING))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane4)
                                        .addComponent(notificationsButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(viewRquests, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(addFriend, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .addComponent(searchButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(addFriend)
                                        .addComponent(jButton1)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(7, 7, 7)
                                                .addComponent(viewRquests))
                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                                        .addComponent(addPost, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jButton7))))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(notificationsButton)
                                        .addComponent(jButton8, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addComponent(addStory, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(searchField)
                                        .addComponent(searchButton, javax.swing.GroupLayout.DEFAULT_SIZE, 28, Short.MAX_VALUE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                        .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 422, Short.MAX_VALUE)
                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void viewRquestsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_viewRquestsActionPerformed
        JDialog requestsDialog = new JDialog(this, "Requests", true);
        requestsDialog.setSize(400, 300);
        requestsDialog.setLayout(new BorderLayout());
        JPanel friendRequestsPanel = new JPanel();
        friendRequestsPanel.setLayout(new BoxLayout(friendRequestsPanel, BoxLayout.Y_AXIS));

        JPanel requestPanel = new JPanel();
        requestPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        for (User i : S.getLoggedInUser().getIncomingRequests()) {
            JLabel nameLabel = new JLabel(i.getUsername());
            requestPanel.add(nameLabel);
            JButton acceptButton = new JButton("Accept");
            acceptButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, i.getUsername() + " accepted!");
                S.getLoggedInUser().acceptFriendRequest(i);
            });
            requestPanel.add(acceptButton);
            // Add Decline button
            JButton declineButton = new JButton("Decline");
            declineButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, i.getUsername() + " declined!");
                S.getLoggedInUser().declineFriendRequest(i);
            });
            requestPanel.add(declineButton);

            // Add the requestPanel to the main friendRequestsPanel
            friendRequestsPanel.add(requestPanel);
        }
        // Add the panel to the dialog
        JScrollPane scrollPane = new JScrollPane(friendRequestsPanel);
        requestsDialog.add(scrollPane, BorderLayout.CENTER);

        // Show the dialog
        requestsDialog.setLocationRelativeTo(this); // Center it relative to the main window
        requestsDialog.setVisible(true);
    }//GEN-LAST:event_viewRquestsActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        S.logOut();
        Home HS = new Home();
        HS.show();
        dispose();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        ProfileScreen PS = new ProfileScreen(S);
        PS.show();
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void addPostActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addPostActionPerformed
        String pText = JOptionPane.showInputDialog("Enter Text: ");
        File pImg;
        JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = f.showOpenDialog(null);
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
                "Image Files (jpg, png, gif, bmp)", "jpg", "png", "gif", "bmp");
        f.setFileFilter(filter);
        if (result == JFileChooser.APPROVE_OPTION) {
            pImg = f.getSelectedFile();
        } else {
            pImg = null;
        }


        if (pText.isEmpty() && pImg == null)
            JOptionPane.showMessageDialog(null, "Invalid Post format", "Error", JOptionPane.ERROR_MESSAGE);
        else {
            BufferedImage pImage = null;
            try {
                pImage = ImageIO.read(pImg);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error Loading post picture", "Error", JOptionPane.ERROR_MESSAGE);
            }
            S.getLoggedInUser().addContent(pText, pImage, pImg.toString(), "Post");
        }

    }//GEN-LAST:event_addPostActionPerformed

    private void addStoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addStoryActionPerformed
        String pText = JOptionPane.showInputDialog("Enter Text: ");
        File pImg;
        JFileChooser f = new JFileChooser();
        f.setFileSelectionMode(JFileChooser.FILES_ONLY);
        int result = f.showOpenDialog(null);
        FileNameExtensionFilter filter = new FileNameExtensionFilter("Image Files (jpg, png, gif, bmp)", "jpg", "png", "gif", "bmp");
        f.setFileFilter(filter);
        if (result == JFileChooser.APPROVE_OPTION) {
            pImg = f.getSelectedFile();

        } else {
            pImg = null;
        }

        if (pText.isEmpty() && pImg == null)
            JOptionPane.showMessageDialog(null, "Invalid Post format", "Error", JOptionPane.ERROR_MESSAGE);
        else {
            BufferedImage sImage = null;
            try {
                sImage = ImageIO.read(pImg);
                S.getLoggedInUser().addContent(pText, sImage, pImg.toString(), "Story");
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(null, "Error loading story", "Error", JOptionPane.ERROR_MESSAGE);
            }

        }
    }//GEN-LAST:event_addStoryActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        ArrayList<Content> dispContent = S.getLoggedInUser().getContents();
        for (Content i : dispContent) {
            JLabel l = new JLabel(i.getText());
            postsPanel.add(l);
        }
        S.refresh();

    }//GEN-LAST:event_jButton8ActionPerformed

    private void addFriendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addFriendActionPerformed
        String key = JOptionPane.showInputDialog("Enter Username:");
        for (User i : S.getOthers()) {
            if (key.equals(i.getUsername())) {
                if (S.getLoggedInUser().sendFriendRequest(i)) {
                    JOptionPane.showMessageDialog(null, "Request Sent Successfully!", "Done", JOptionPane.INFORMATION_MESSAGE);
                } else JOptionPane.showMessageDialog(null, "Can't add user!", "Warning", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_addFriendActionPerformed

    private void notificationsButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_notificationsButtonActionPerformed
        JDialog notificationsDialog = new JDialog(this, "Notifications", true);
        notificationsDialog.setSize(400, 300);
        notificationsDialog.setLayout(new BorderLayout());

// Create a panel to hold all notifications
        JPanel notificationsPanel = new JPanel();
        notificationsPanel.setLayout(new BoxLayout(notificationsPanel, BoxLayout.Y_AXIS));

// Fetch notifications from the JSON file
        List<JSONObject> notifications = S.loadNotifications(S.getLoggedInUser().getUserID());

// Loop through the notifications and create UI components for each
        for (JSONObject notification : notifications) {
            String type = notification.optString("Type", "Unknown");
            String message = notification.optString("Message", "No message available");
            String userID = notification.optString("UserID");

            // Panel for individual notification
            JPanel notificationPanel = new JPanel();
            notificationPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            notificationPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

            JLabel messageLabel = new JLabel(message);
            notificationPanel.add(messageLabel);

            if (type.equalsIgnoreCase("friend request")) {
                // Add Accept button
                JButton acceptButton = new JButton("Accept");
                acceptButton.addActionListener(e -> {
                    JOptionPane.showMessageDialog(this, "Friend request from accepted!");
                    S.getLoggedInUser().acceptFriendRequest(S.getLoggedInUser()); // Adjust to actual accept logic
                    notificationsPanel.remove(notificationPanel); // Remove from view
                    notificationsPanel.revalidate();
                    notificationsPanel.repaint();
                });
                notificationPanel.add(acceptButton);

                // Add Reject button
                JButton rejectButton = new JButton("Reject");
                rejectButton.addActionListener(e -> {
                    JOptionPane.showMessageDialog(this, "Friend request from  rejected!");
                    S.getLoggedInUser().declineFriendRequest(S.getLoggedInUser()); // Adjust to actual reject logic
                    notificationsPanel.remove(notificationPanel); // Remove from view
                    notificationsPanel.revalidate();
                    notificationsPanel.repaint();
                });
                notificationPanel.add(rejectButton);
            } else {
                // For other notifications, just show them
                notificationPanel.add(new JLabel("(View only)"));
            }

            notificationsPanel.add(notificationPanel);
        }

// Add the notifications panel to a scroll pane
        JScrollPane scrollPane = new JScrollPane(notificationsPanel);
        notificationsDialog.add(scrollPane, BorderLayout.CENTER);

// Show the dialog
        notificationsDialog.setLocationRelativeTo(this); // Center it relative to the main window
        notificationsDialog.setVisible(true);
    }//GEN-LAST:event_notificationsButtonActionPerformed

    private void searchFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_searchFieldActionPerformed

    private void searchButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_searchButtonActionPerformed
        if (searchField.getText().isEmpty())
            JOptionPane.showMessageDialog(null, "Please enter seach key!", "Note!", JOptionPane.ERROR_MESSAGE);
        else {
            String key = searchField.getText(); // Take token from search field

            JDialog requestsDialog = new JDialog(this, "Search", true);
            requestsDialog.setSize(400, 400);
            requestsDialog.setLayout(new BorderLayout());
            JPanel searchPanel = new JPanel();
            searchPanel.setLayout(new BoxLayout(searchPanel, BoxLayout.Y_AXIS));
            JPanel userPanel = new JPanel();
            userPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            JPanel groupPanel = new JPanel();
            groupPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
            //Handling others options
            for (User i : S.getOthers()) {

                if (key.equals(i.getUsername())) {
                    JLabel nameLabel = new JLabel(i.getUsername());
                    userPanel.add(nameLabel);

                    // Add addFriend button
                    JButton addButton = new JButton("Add Friend");
                    addButton.addActionListener(e -> {
                        if (S.getLoggedInUser().sendFriendRequest(i)){
                            JOptionPane.showMessageDialog(this, " Request Sent to " + i.getUsername() + " ! ");
                            userPanel.remove(addButton);
                        }
                        else
                            JOptionPane.showMessageDialog(this, " Can't add " + i.getUsername() + " ! ", "Restricted", JOptionPane.ERROR_MESSAGE);
                    });
                    userPanel.add(addButton);


                    // Add removeFriend button
                    JButton removeButton = new JButton("Remove Friend");
                    addButton.addActionListener(e -> {
                        if (S.getLoggedInUser().removeFriend(i))
                            JOptionPane.showMessageDialog(this, i.getUsername() + " removed from friends ! ");
                        else
                            JOptionPane.showMessageDialog(this, " Can't remove " + i.getUsername() + " ! ", "Restricted", JOptionPane.ERROR_MESSAGE);
                    });
                    userPanel.add(removeButton);


                    // Add Block button
                    JButton blockButton = new JButton("Block");
                    blockButton.addActionListener(e -> {
                        if (S.getLoggedInUser().blockUser(i))
                            JOptionPane.showMessageDialog(this, i.getUsername() + " Blocked!");
                        else
                            JOptionPane.showMessageDialog(this, i.getUsername() + " Already blocked !", "Restricted", JOptionPane.ERROR_MESSAGE);
                    });
                    userPanel.add(blockButton);
                    
                    // Add Block button
                    JButton chatButton = new JButton("Chat");
                    blockButton.addActionListener(e -> {
                        if (S.getLoggedInUser().getFriends().contains(i))
                            System.out.println("Rredirecting to Chat");///Replace with screen
                        else
                            JOptionPane.showMessageDialog(this, i.getUsername() + " is not your friend !", "Restricted", JOptionPane.ERROR_MESSAGE);
                    });
                    userPanel.add(chatButton);
                    // Add the requestPanel to the main friendRequestsPanel
                    searchPanel.add(userPanel);
                }

            }
            for (Group g : S.getAllGroups()) {

                if (key.equals(g.getGroupName())) {
                    groupPanel.add(new JLabel(g.getGroupName()));
                    // Join Group Button
                    JButton joinButton = new JButton("Join");
                    joinButton.addActionListener(e -> {
                        S.requestMembership(g);
                        JOptionPane.showMessageDialog(this, "Requst sent!");
                    });

                    groupPanel.add(joinButton);

                    //Leave Group Button
                    JButton leaveButton = new JButton("Leave");
                    leaveButton.addActionListener(e -> {
                        S.leaveGroup(g);
                        JOptionPane.showMessageDialog(this, "Group Left!");
                    });
                    groupPanel.add(leaveButton);
                    
                    //View Group Button
                    JButton viewButton = new JButton("View");
                    viewButton.addActionListener(e -> {
                        GroupScreen GS = new GroupScreen(S,g.getGroupName());
                        GS.show();
                        dispose();
                    });
                    groupPanel.add(viewButton);

                    searchPanel.add(groupPanel);
                }
            }
            // Add the panel to the dialog
            JScrollPane scrollPane = new JScrollPane(searchPanel);
            requestsDialog.add(scrollPane, BorderLayout.CENTER);

            // Show the dialog
            requestsDialog.setLocationRelativeTo(this); // Center it relative to the main window
            requestsDialog.setVisible(true);
        }
    }//GEN-LAST:event_searchButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(NewsFeed.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(NewsFeed.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(NewsFeed.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(NewsFeed.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new NewsFeed(S).setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addFriend;
    private javax.swing.JButton addPost;
    private javax.swing.JButton addStory;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JButton notificationsButton;
    private javax.swing.JPanel postsPanel;
    private javax.swing.JButton searchButton;
    private javax.swing.JTextField searchField;
    private javax.swing.JPanel sidePanel;
    private javax.swing.JButton viewRquests;
    // End of variables declaration//GEN-END:variables
}
