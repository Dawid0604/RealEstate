/* Copyright 2026 RealEstate */
package pl.dawid0604.realestate.infrastructure;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.postgresql.PostgreSQLContainer;

@ActiveProfiles("test")
@SpringBootTest(classes = TestApplicationContext.class)
public abstract class IntegrationTest {

    @ServiceConnection
    static PostgreSQLContainer postgres = new PostgreSQLContainer("postgres:18-alpine");

    static {
        postgres.start();
    }
}
