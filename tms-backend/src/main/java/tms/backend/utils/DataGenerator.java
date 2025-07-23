package tms.backend.utils;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tms.backend.ApplicationProperties;
import tms.backend.model.Status;
import tms.backend.model.Task;
import tms.backend.repository.TaskRepository;

import java.time.LocalDateTime;

/**
 * Utility to generate some test data.
 */
@Service
@Slf4j
public class DataGenerator {

    @Autowired
    private ApplicationProperties applicationProperties;

    @Autowired
    private TaskRepository taskRepository;

    @PostConstruct
    public void generateDate() {
        if (applicationProperties.getGenerateTestData()) {
            log.info("Test data generation enabled.");
            for (int i = 0; i < 7; i++) {
                Task task = Task.builder()
                        .title("Title"+i)
                        .description("Description"+i)
                        .due(LocalDateTime.now())
                        .status(Status.NEW)
                        .build();
                taskRepository.save(task);
                log.info("Generated: {}", task);
            }
        }
    }
}
