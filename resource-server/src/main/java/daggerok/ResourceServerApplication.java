package daggerok;

import daggerok.config.GlobalMethodSecurityConfig;
import daggerok.config.ResourceServerConfig;
import daggerok.config.RestRepositoryConfig;
import daggerok.config.ResourceServerCorsFilter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

import javax.servlet.Filter;

@SpringBootApplication
@Import({   RestRepositoryConfig.class,
            ResourceServerConfig.class,
            GlobalMethodSecurityConfig.class })
public class ResourceServerApplication {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public Filter simpleCorsFilter() {
        return new ResourceServerCorsFilter();
    }

    /**

     1) check access denied without authorization:

     http :9000/api/items
     {"error": "unauthorized","error_description": "Full authentication is required to access this resource"}

     2) fetch token for read:

     curl mobile_app:@localhost:9999/uaa/oauth/token -d grant_type=password -d username=rest_reader_username -d password=rest_reader_password | pj
     {
         "access_token": "eyJhbGciOiJSUzI1NiIsInR5...",
         "expires_in": 43199,
         "jti": "052eebec-e19e-449c-af30-de8195515167",
         "refresh_token": "eyJhbGciOiJSUzI1NiIsInR...",
         "scope": "rest",
         "token_type": "bearer"
     }

     3) add it into header as expected and get protected resource:

     set TOKEN eyJhbGciOiJSUzI1NiIsInR5...
     http :9000/api/items "Authorization: Bearer $TOKEN"
     {
         "_embedded": {
                 "items": []
             },
             "_links": {...

     4) try write data without permissions:

     http post :9000/api/items content=newwwww "Authorization: Bearer $TOKEN"
     {"error": "access_denied","error_description": "Access is denied"}

     5) fetch token for write:

     curl mobile_app:@localhost:9999/uaa/oauth/token -d grant_type=password \
          -d username=rest_writer_username \
          -d password=rest_writer_password | pj
     {
         "access_token": "eyJhbGciOiJSUzI1NiIsInR5...",
         ...
     }

     set TOKEN eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJle

     http post :9000/api/items content=newwwww "Authorization: Bearer $TOKEN"
     {
         "_links": {
                 "item": {
                 "href": "http://localhost:9000/api/items/57ddf1c0b0065b4da5599218"
             },
                 "self": {
                 "href": "http://localhost:9000/api/items/57ddf1c0b0065b4da5599218"
             }
         },
         "content": "newwwww",
         "id": "57ddf1c0b0065b4da5599218"
     }

     yay!
     */

    public static void main(String[] args) {
        SpringApplication.run(ResourceServerApplication.class, args);
    }
}
