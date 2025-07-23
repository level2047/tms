package tms.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tms.backend.model.Task;
import tms.backend.repository.TaskRepository;
import tms.backend.utils.GenericErrorResponse;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * Main controller responsible for tasks API.
 */
@Slf4j
@RestController
public class TaskController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private TaskRepository taskRepository;

    /**
     * Get a list of all tasks
     *
     * @return task list
     */
    @Operation(summary = "Get all tasks")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = Task.class)))}),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class)))})
    @GetMapping("/tasks")
    public List<Task> getAllTasks() {
        List<Task> tasks = taskRepository.findAll();
        log.info("Retrieved {} tasks", tasks.size());
        return tasks;
    }

    /**
     * Retrieve a task by ID
     *
     * @param id task id
     * @return ResponseEntity
     */
    @Operation(summary = "Get a task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class)))})
    @GetMapping("/tasks/{id}")
    public ResponseEntity<?> getTask(@PathVariable String id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            log.info("Retrieved task {}", task.get().getId());
            return ResponseEntity.status(HttpStatus.OK).body(task);
        } else {
            return getNotFoundResponse(id);
        }
    }

    /**
     * Create a new task
     *
     * @param task task body to create
     * @return created task
     */
    @Operation(summary = "Create a task")
    @ApiResponses({
            @ApiResponse(responseCode = "201", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class)))})
    @PostMapping("/tasks")
    public ResponseEntity<Task> createTask(@RequestBody Task task) {
        Task createdTask = taskRepository.save(task);
        log.info("Created task {}", createdTask.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTask);
    }

    /**
     * Update a task
     *
     * @param task task body to update
     * @return updated task
     */
    @Operation(summary = "Update a task")
    @ApiResponses({
            @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json",
                    schema = @Schema(implementation = Task.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class)))})
    @PutMapping("/tasks")
    public Task updateTask(@RequestBody Task task) {
        Task updatedTask = taskRepository.save(task);
        log.info("Updated task {}", updatedTask.getId());
        return taskRepository.save(task);
    }

    /**
     * Delete a task
     *
     * @param id task id
     * @return operation status
     */
    @Operation(summary = "Delete a task")
    @ApiResponses({
            @ApiResponse(responseCode = "204", content = {@Content(mediaType = "application/json")}),
            @ApiResponse(responseCode = "404", description = "Task not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GenericErrorResponse.class)))})
    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable String id) {
        Optional<Task> task = taskRepository.findById(id);
        if (task.isPresent()) {
            taskRepository.delete(task.get());
            log.info("Deleted task {}", task.get().getId());
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } else {
            return getNotFoundResponse(id);
        }
    }

    private ResponseEntity<GenericErrorResponse> getNotFoundResponse(String id) {
        log.info("Task {} not found", id);
        return getErrorResponseEntity(HttpStatus.NOT_FOUND, "Task not found");
    }

    private ResponseEntity<GenericErrorResponse> getErrorResponseEntity(HttpStatus status, String error) {
        GenericErrorResponse response = GenericErrorResponse.builder()
                .path(request.getRequestURI())
                .error(error)
                .status(status.value())
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.status(status).body(response);
    }


}
