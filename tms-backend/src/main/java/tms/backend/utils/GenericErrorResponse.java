package tms.backend.utils;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

/**
 * Container for spring default error response as the ones inside spring are too dynamic.
 */
@Getter
@Setter
@Builder
public class GenericErrorResponse {
    private Instant timestamp;
    private int status;
    private String error;
    private String path;

}