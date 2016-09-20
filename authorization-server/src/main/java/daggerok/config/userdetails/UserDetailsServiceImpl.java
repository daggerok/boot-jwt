package daggerok.config.userdetails;

import daggerok.data.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import static java.lang.String.format;

@Repository
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    final UserRepository userRepository;

    @Override
    @SneakyThrows
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        val user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(format("= user for '%s' wasn't found =", username)));

        return new User(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.isEnabled(),
                user.isEnabled(),
                user.isEnabled(),
                user.getGrantedAuthorities()
        );
    }
}
