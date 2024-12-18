package Backend;

public class LoggedUserFactory implements UserFactory{
    @Override
    public UserBuilder createUserBuilder() {
        return new LoggedUserBuilder();
    }

}
