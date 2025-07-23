package tms.frontend.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Custom exception to handle HTTP 500
 */
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class ServerInternalErrorException extends RuntimeException {
}
