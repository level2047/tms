package tms.backend.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import tms.backend.ApplicationProperties;
import tms.backend.model.Status;
import tms.backend.model.Task;
import tms.backend.utils.DataGenerator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


@Slf4j
@DataMongoTest(properties = {"de.flapdoodle.mongodb.embedded.version=6.0.15",
        "spring.data.mongodb.port=28018"})
@Import({ApplicationProperties.class, DataGenerator.class})
public class TaskRepositoryTest {

    @Autowired
    private TaskRepository taskRepository;

    @Test
    void getAllTasks() {
        List<Task> all = taskRepository.findAll();
        assertEquals(7, all.size());
    }

    @Test
    void getTask() {
        Optional<Task> task = taskRepository.findById(getTaskIdFromTitle("Title1"));
        assertTrue(task.isPresent());
    }

    @Test
    void getTaskNotFound() {
        Optional<Task> task = taskRepository.findById("some id");
        assertFalse(task.isPresent());
    }

    @Test
    void createTask() {
        Task task = Task.builder()
                .title("Title new")
                .description("Description new")
                .due(LocalDateTime.now())
                .status(Status.NEW)
                .build();
        taskRepository.save(task);
        List<Task> all = taskRepository.findAll();
        assertEquals(8, all.size());

        Optional<Task> newTask = taskRepository.findById(getTaskIdFromTitle("Title new"));
        assertTrue(newTask.isPresent());
    }

    @Test
    void updateTask() {
        Optional<Task> task = taskRepository.findById(getTaskIdFromTitle("Title1"));
        assertTrue(task.isPresent());
        task.get().setDescription("Updated");
        taskRepository.save(task.get());

        task = taskRepository.findById(task.get().getId());
        assertTrue(task.isPresent());
        assertEquals("Updated", task.get().getDescription());

    }

    @Test
    void deleteTask() {
        Optional<Task> task = taskRepository.findById(getTaskIdFromTitle("Title2"));
        taskRepository.delete(task.get());
        task = taskRepository.findById(task.get().getId());
        assertFalse(task.isPresent());
    }

    private String getTaskIdFromTitle(String title) {
        return taskRepository.findAll()
                .stream()
                .filter(task -> task.getTitle().equals(title))
                .map(Task::getId)
                .findFirst()
                .orElseThrow();
    }

}
