package tms.backend.model;

import lombok.*;
import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * Task entity model.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Task {
    @Id
    private String id;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime due;

}
