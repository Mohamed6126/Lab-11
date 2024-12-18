package Backend;

public class OtherUserFactory implements UserFactory {

    @Override
    public UserBuilder createUserBuilder() {
        return new OtherUserBuilder();
    }
}
