package daggerok;

import daggerok.config.OAuth2AuthorizationServerConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(OAuth2AuthorizationServerConfig.class)
public class AuthorizationServerApplication {

    /**

     gradle :authorization-server:bootRun

     curl mobile_app:@localhost:9999/uaa/oauth/token \
          -d grant_type=password \
          -d username=rest_reader_username \
          -d password=rest_reader_password

     {
       "access_token": "eyJhbGciOiJSUzI1NiIsInR5...",
       "expires_in": 43199,
       "jti": "be516c1e-adf0-4af0-853e-341b05e485e8",
       "refresh_token": "eyJhbGciOiJSUzI1NiIsInR...",
       "scope": "rest",
       "token_type": "bearer"
     }
     */

    public static void main(String[] args) {
        SpringApplication.run(AuthorizationServerApplication.class, args);
    }
}
