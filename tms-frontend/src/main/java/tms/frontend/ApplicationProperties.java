package tms.frontend;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

/**
 * Application Properties.
 */
@Getter
@Setter
@Configuration
@ConfigurationProperties
@Validated
public class ApplicationProperties {
    /**
     * Generate test data
     */
    @NotNull
    private String tmsBackendApiAddress;

}
