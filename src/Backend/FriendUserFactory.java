package Backend;

/**
 * @author vip comp
 */
public class FriendUserFactory implements UserFactory {

    @Override
    public UserBuilder createUserBuilder() {
        return new FriendUserBuilder();
    }


}