package Backend;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException {

        Session mySession = new Session();

        mySession.login("ahmed", "123");
        BufferedImage image = ImageIO.read(new File("Images/user1/125.jpg"));

        User logeedInUser = mySession.getLoggedInUser();
        //logeedInUser.addContent("test",image,"Images/user1/125.jpg","Post");
        System.out.println(mySession.getMemberGroups().size());
        System.out.println(mySession.getNormalAdminGroups().size());
        System.out.println(mySession.getPrimaryAdminGroups().size());

        for (int i = 0; i < mySession.getPrimaryAdminGroups().size(); i++) {
            Group myGroup = mySession.getPrimaryAdminGroups().get(i);
            if (myGroup.getGroupid().equals("group2")) {
                mySession.addPostToGroup(myGroup,"test",image,"Images/user1/125.jpg","Post");
            }
        }
        System.out.println(mySession.loadNotifications("user2"));
        mySession.refresh();
    }
}
