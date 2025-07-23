package tms.frontend.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tms.frontend.ApplicationProperties;
import tms.frontend.model.Task;

import java.util.List;

/**
 * Task service responsible for calling the backend.
 */
@Service
public class TaskService {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private ObjectMapper mapper;

    private TmBackend backend;

    /**
     * Get all tasks
     *
     * @return list of tasks
     */
    public List<Task> getAllTasks() {
        return backend.getAllTasks();
    }

    /**
     * Create a task
     *
     * @param task Task
     * @return created task
     */
    public Task createTask(Task task) {
        return backend.createTask(task);
    }

    /**
     * Update a task
     *
     * @param task Task
     * @return updated task
     */
    public Task updateTask(Task task) {
        return backend.updateTask(task);
    }

    /**
     * Delete a task
     *
     * @param id task id
     */
    public void deleteTask(String id) {
        backend.deleteTask(id);
    }

    /**
     * Get a task
     *
     * @param id task id
     * @return Task
     */
    public Task getTask(String id) {
        return backend.getTask(id);
    }

    @PostConstruct
    private void initRestConsumer() {
        backend = Feign.builder()
                .decoder(new JacksonDecoder(mapper))
                .encoder(new JacksonEncoder(mapper))
                .target(TmBackend.class, applicationProperties.getTmsBackendApiAddress());
    }

}
