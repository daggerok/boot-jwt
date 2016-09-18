package daggerok.config;

import lombok.Cleanup;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.util.FileCopyUtils;

@Configuration
public class JwtConfig {

    @Bean
    //@Qualifier("tokenStore")
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtAccessTokenConverter());
    }

    @Bean
    @SneakyThrows
    public JwtAccessTokenConverter jwtAccessTokenConverter() {

        val jwtAccessTokenConverter = new JwtAccessTokenConverter();
        @Cleanup val stream = new ClassPathResource("etc/public.cert").getInputStream();
        val key = new String(FileCopyUtils.copyToByteArray(stream));

        jwtAccessTokenConverter.setVerifierKey(key);
        return jwtAccessTokenConverter;
    }
}
