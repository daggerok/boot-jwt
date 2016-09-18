package daggerok.config;

import lombok.SneakyThrows;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    //@Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManagerBean;

    @Override
    @SneakyThrows
    public void configure(final ClientDetailsServiceConfigurer clients) {
        // @formatter:off
        clients.inMemory()
                .withClient("web_app")
                    .scopes("rest")
                    .autoApprove(true)
                    .authorities("REST_READ_AUTH", "REST_WRITE_AUTH")
                    .authorizedGrantTypes("implicit", "refresh_token", "password", "authorization_code")
         .and().withClient("mobile_app")
                    .scopes("rest")
                    .autoApprove(true)
                    .authorities("REST_READ_AUTH")
                    .authorizedGrantTypes("implicit", "refresh_token", "password", "authorization_code")
        ;
        // @formatter:on
    }

    @Override
    @SneakyThrows
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        // @formatter:off
        endpoints.tokenStore(tokenStore())
                 .tokenEnhancer(jwtTokenEnhancer())
                 .authenticationManager(authenticationManagerBean);
        // @formatter:on
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtTokenEnhancer());
    }

    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer() {

        val jwtAccessTokenConverter = new JwtAccessTokenConverter();
        // -keystore etc/boot-jwt.jks
        val jks = new ClassPathResource("etc/boot-jwt.jks");
        // -storepass boot-jwt
        val password = "boot-jwt".toCharArray();
        val keyStoreKeyFactory = new KeyStoreKeyFactory(jks, password);
        // keytool -genkeypair -alias bootjwt
        val alias = "bootjwt";
        jwtAccessTokenConverter.setKeyPair(keyStoreKeyFactory.getKeyPair(alias));
        return jwtAccessTokenConverter;
    }

    /**
     * TODO: re-implement using UserDetailsService instead...
     */
    @Configuration
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        @SneakyThrows
        @Bean//(name = "authenticationManagerBean")
        public AuthenticationManager authenticationManagerBean() {
            return super.authenticationManagerBean();
        }

        @Override
        @SneakyThrows
        protected void configure(final HttpSecurity http) {
            // @formatter:off
            http.csrf().disable()
                    .exceptionHandling().authenticationEntryPoint((req, res, ex) -> res.sendError(SC_UNAUTHORIZED))
                .and()
                    .authorizeRequests().antMatchers("/**").authenticated()
                .and()
                    .httpBasic();
            // @formatter:on
        }

        @Override
        @SneakyThrows
        protected void configure(final AuthenticationManagerBuilder auth) {
            // @formatter:off
            auth.inMemoryAuthentication()
                    .withUser("rest_reader_username")
                        .password("rest_reader_password")
                        .authorities("REST_READ_AUTH")
                .and()
                    .withUser("rest_writer_username")
                        .password("rest_writer_password")
                        .authorities("REST_READ_AUTH", "REST_WRITE_AUTH");
            // @formatter:on
        }
    }
}
