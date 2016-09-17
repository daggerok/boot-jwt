package daggerok;

import daggerok.config.RestRepositoryConfig;
import daggerok.data.Item;
import daggerok.data.ItemRestRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.stream.Stream;

@SpringBootApplication
@Import(RestRepositoryConfig.class)
public class ResourceServerApplication {

    @Bean
    CommandLineRunner testData(ItemRestRepository items) {

        return args -> Stream.of("one", "two", "three")
                .map(Item::of)
                .forEach(items::save);
    }

    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }
}
