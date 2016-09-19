package daggerok.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;

@Configuration
@EnableResourceServer
@Import(JwtConfig.class)
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {

    @Autowired
    TokenStore tokenStore;

    @Autowired
    JwtAccessTokenConverter jwtAccessTokenConverter;

    @Override
    public void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .cors().disable()
                .authorizeRequests()
                    .antMatchers("/**").authenticated()
                    .antMatchers(HttpMethod.GET).hasAnyAuthority("REST_READ_ROLE")
                    .antMatchers(HttpMethod.HEAD).hasAnyAuthority("REST_READ_ROLE")
                    //.antMatchers(HttpMethod.PUT).hasAnyAuthority("REST_WRITE_ROLE")
                    //.antMatchers(HttpMethod.POST).hasAnyAuthority("REST_WRITE_ROLE")
                    //.antMatchers(HttpMethod.PATCH).hasAnyAuthority("REST_WRITE_ROLE")
                    //.antMatchers(HttpMethod.DELETE).hasAnyAuthority("REST_WRITE_ROLE")
        ;

    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
        resources.resourceId("items").tokenStore(tokenStore);
    }
}
