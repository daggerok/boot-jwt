package daggerok.config.userdetails;

import daggerok.data.DataUserConfig;
import daggerok.data.user.User;
import daggerok.data.user.UserRepository;
import daggerok.utils.password.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.stream.Stream;

@Configuration
@Import(DataUserConfig.class)
public class UserInitializer {

    @Autowired PasswordGenerator passwordGenerator;

    @Bean
    public CommandLineRunner userTestData(final UserRepository userRepository) {

        Stream.of("reader", "writer")
                .map(this::from)
                .forEach(userRepository::save);
        return args -> userRepository.findAll().forEach(System.out::println);
    }

    private User from(final String identifier) {

        final String id = "rest_".concat(identifier);
        final User user = User.of(id + "_username", passwordGenerator.encode(id + "_password"));

        user.setEnabled(true);

        if (identifier.contains("write")) {

            user.getGrantedAuthorities().addAll(AuthorityUtils.createAuthorityList("REST_READ_AUTH", "REST_WRITE_AUTH", "ROLE_REST_READ_AUTH", "ROLE_REST_WRITE_AUTH"));

        } else {

            user.getGrantedAuthorities().addAll(AuthorityUtils.createAuthorityList("REST_READ_AUTH", "ROLE_REST_READ_AUTH"));
        }

        return user;
    }
}
