package gateway.user;

import org.springframework.security.crypto.password.PasswordEncoder;

public class CustomPasswordEncoder implements PasswordEncoder {
    @Override
    public String encode(CharSequence plainTextPassword) {
        return (String) plainTextPassword;
    }

    @Override
    public boolean matches(CharSequence plainTextPassword, String passwordInDatabase) {
        return plainTextPassword.equals(passwordInDatabase);
    }
}
