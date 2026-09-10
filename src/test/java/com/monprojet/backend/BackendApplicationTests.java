package com.monprojet.backend;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Disabled("Nécessite une base MySQL locale (application-local.properties) — "
        + "voir application-local.properties.example. Les tests de sécurité "
        + "tournent sans base : JwtServiceTest et SecuriteEndpointsTest.")
class BackendApplicationTests {

    @Test
    void contextLoads() {
    }
}
