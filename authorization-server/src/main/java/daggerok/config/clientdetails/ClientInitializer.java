package daggerok.config.clientdetails;

import daggerok.data.DataClientConfig;
import daggerok.data.client.Client;
import daggerok.data.client.ClientRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import java.util.stream.Stream;

@Configuration
@Import(DataClientConfig.class)
public class ClientInitializer {

    @Bean
    public CommandLineRunner clientTestData(final ClientRepository clientRepository) {

        Stream.of(
                Client.of(
                        "web_app",
                        "resource-server",
                        "rest",
                        "implicit,refresh_token,password,authorization_code",
                        "REST_READ_AUTH,REST_WRITE_AUTH"),
                Client.of(
                        "mobile_app",
                        "resource-server",
                        "rest",
                        "implicit,refresh_token,password,authorization_code",
                        "REST_READ_AUTH")
        ).forEach(clientRepository::save);

        return args -> clientRepository.findAll().forEach(System.out::println);
    }
}
