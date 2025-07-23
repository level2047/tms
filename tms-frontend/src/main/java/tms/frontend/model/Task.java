package tms.frontend.model;


import lombok.*;

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
@EqualsAndHashCode
public class Task {
    private String id;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime due;
}
