package CSDL.spring_ml_practice.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import javax.sql.DataSource;
import java.sql.Connection;

@Slf4j
@Configuration
public class DatabaseConnectionCheck {

    private final DataSource dataSource;

    public DatabaseConnectionCheck(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean
    public ApplicationRunner checkDatabaseConnection() {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                if (connection.isValid(2)) {
                    log.info("Database connected successfully.");
                } else {
                    log.error("Failed to connect to the database.");
                }
            } catch (Exception e) {
                log.error("Error while connecting to the database: ", e);
            }
        };
    }
}