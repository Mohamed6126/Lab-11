package Backend;

import java.security.NoSuchAlgorithmException;

/**
 *
 * @author vip comp
 */
public interface Hasher {
    String hashPassword(String password)throws NoSuchAlgorithmException;
}
