package tms.frontend.model;

import lombok.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Task data transfer object used for the web interface.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@EqualsAndHashCode
public class TaskDTO {
    private static final String format = "dd-MM-yyyy HH:mm";
    private String id;
    private String title;
    private String description;
    private Status status;
    private String due;

    /**
     * Convert DTO to Entity
     *
     * @return Task
     */
    public Task toTask() {
        if (id != null && id.isBlank()) {
            id = null;//blank id is bad, better
        }
        return new Task(id, title, description, status,
                LocalDateTime.parse(due, DateTimeFormatter.ofPattern(format)));
    }

    /**
     * Convert Entity to DTO
     *
     * @return TaskDTO
     */
    public static TaskDTO fromTask(Task task) {
        return TaskDTO.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .due(task.getDue().format(DateTimeFormatter.ofPattern(format)))
                .build();

    }
}
