package org.example.cinema;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest(properties = {
        "spring.security.oauth2.resourceserver.jwt.issuer-uri=http://localhost/dummy"
})

class CinemaApplicationTests {

    @Test
    void contextLoads() {
    }

}
