package daggerok.config;

import daggerok.config.clientdetails.ClientDetailsServiceImpl;
import daggerok.config.userdetails.UserDetailsServiceImpl;
import daggerok.data.DataClientConfig;
import daggerok.data.DataUserConfig;
import daggerok.data.client.ClientRepository;
import daggerok.data.user.UserRepository;
import lombok.SneakyThrows;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import static javax.servlet.http.HttpServletResponse.SC_UNAUTHORIZED;

@Configuration
@EnableAuthorizationServer
@Import({DataUserConfig.class, DataClientConfig.class})
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    ClientRepository clentRepository;

    @Autowired
    @Qualifier("userDetailsServiceBean")
    UserDetailsService userDetailsServiceBean;

    @Autowired @Qualifier("authenticationManagerBean")
    AuthenticationManager authenticationManagerBean;

    @Primary
    @Bean//(name = "clentDetailsServiceImpl")
    public ClientDetailsService clentDetailsServiceImpl() {
        return new ClientDetailsServiceImpl(clentRepository);
    }

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.allowFormAuthenticationForClients();
    }

    @Override
    @SneakyThrows
    public void configure(final ClientDetailsServiceConfigurer clients) {
        // @formatter:off
        clients.withClientDetails(clentDetailsServiceImpl())
        /*
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
                    */
        ;
        // @formatter:on
    }

    @Override
    @SneakyThrows
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) {
        // @formatter:off
        endpoints.tokenStore(tokenStore())
                 .tokenEnhancer(jwtTokenEnhancer())
                 .userDetailsService(userDetailsServiceBean)
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

    @Configuration
    @Order(Ordered.HIGHEST_PRECEDENCE) // WebSecurityConfigurerAdapter.Order = 100
    class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        @Autowired
        UserRepository userRepository;

        @Primary
        @Override
        @SneakyThrows
        @Bean(name = "userDetailsServiceBean")
        public UserDetailsService userDetailsServiceBean() {
            return new UserDetailsServiceImpl(userRepository);
        }

        @Override
        protected UserDetailsService userDetailsService() {
            return userDetailsServiceBean();
        }
///*
        @Primary
        @Override
        @SneakyThrows
        @Bean(name = "authenticationManagerBean")
        public AuthenticationManager authenticationManagerBean() {
            return super.authenticationManagerBean();
        }
//*/
        @Override
        @SneakyThrows
        protected void configure(final HttpSecurity http) {
            // @formatter:off
            http
                    .csrf().disable()
//                    .anonymous().disable()
//                    .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
//                .and()
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
            auth.userDetailsService(userDetailsServiceBean())
                .passwordEncoder(new BCryptPasswordEncoder());
            /*
            auth.inMemoryAuthentication()
                    .withUser("rest_reader_username")
                        .password("rest_reader_password")
                        .authorities("REST_READ_AUTH")
                .and()
                    .withUser("rest_writer_username")
                        .password("rest_writer_password")
                        .authorities("REST_READ_AUTH", "REST_WRITE_AUTH");
            */
            // @formatter:on
        }
    }
}
