package daggerok.utils.password;

import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class PasswordGenerator {
    private final static BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public String encode(@NonNull String password) {
        return encoder.encode(password);
    }
}
