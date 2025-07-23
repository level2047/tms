package tms.frontend.service;

import feign.Headers;
import feign.Param;
import feign.RequestLine;
import tms.frontend.model.Task;

import java.util.List;

/**
 * REST consumer for backend API.
 */
public interface TmBackend {

    /**
     * Get all tasks.
     *
     * @return all tasks
     */
    @RequestLine("GET /tasks")
    @Headers("Content-Type: application/json")
    List<Task> getAllTasks();

    /**
     * Get a task by id.
     *
     * @param id task id
     * @return Task
     */
    @RequestLine("GET /tasks/{id}")
    @Headers("Content-Type: application/json")
    Task getTask(@Param("id") String id);

    /**
     * Create a task.
     *
     * @param task Task entity
     * @return created Task
     */
    @RequestLine("POST /tasks")
    @Headers("Content-Type: application/json")
    Task createTask(Task task);

    /**
     * Update a task.
     *
     * @param task Task entity
     * @return update Task
     */
    @RequestLine("PUT /tasks")
    @Headers("Content-Type: application/json")
    Task updateTask(Task task);

    /**
     * Delete a task
     *
     * @param id task id
     */
    @RequestLine("DELETE /tasks/{id}")
    @Headers("Content-Type: application/json")
    void deleteTask(@Param("id") String id);

}
