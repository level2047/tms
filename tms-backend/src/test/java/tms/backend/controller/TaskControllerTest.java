package tms.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tms.backend.model.Status;
import tms.backend.model.Task;
import tms.backend.repository.TaskRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private ObjectMapper mapper;

    private List<Task> tasks;

    @BeforeEach
    void generateData() {
        tasks = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            Task task = Task.builder()
                    .id(String.valueOf(i))
                    .title("Title" + i)
                    .description("Description" + i)
                    .due(LocalDateTime.now())
                    .status(Status.NEW)
                    .build();
            tasks.add(task);
        }
    }

    @Test
    void getAllTasks() throws Exception {
        when(taskRepository.findAll()).thenReturn(tasks);
        mockMvc.perform(get("/tasks").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void getTask() throws Exception {
        Task task = tasks.get(2);
        when(taskRepository.findById("2")).thenReturn(Optional.of(task));
        mockMvc.perform(get("/tasks/2").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Title2"));
    }

    @Test
    void getTaskNotFound() throws Exception {
        Task task = tasks.get(2);
        when(taskRepository.findById("10")).thenReturn(Optional.empty());
        mockMvc.perform(get("/tasks/10").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Task not found"));
    }

    @Test
    void createOrUpdateTask() throws Exception {
        Task task = Task.builder()
                .id("3")
                .title("Title3")
                .description("Description3")
                .due(LocalDateTime.now())
                .status(Status.NEW)
                .build();

        String taskJson = mapper.writeValueAsString(task);
        when(taskRepository.save(task)).thenReturn(task);
        doReturn(task).when(taskRepository).save(any(Task.class));
        mockMvc.perform(
                        post("/tasks")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(taskJson)

                )
                .andExpect(content().json(taskJson))
                .andExpect(status().isCreated());
        mockMvc.perform(
                        put("/tasks")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(taskJson)

                )
                .andExpect(content().json(taskJson))
                .andExpect(status().isOk());
    }

    @Test
    void deleteTask() throws Exception {
        Task task = tasks.get(2);
        when(taskRepository.findById("2")).thenReturn(Optional.of(task));
        doNothing().when(taskRepository).delete(any(Task.class));
        mockMvc.perform(delete("/tasks/2").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteTaskNotFound() throws Exception {
        mockMvc.perform(delete("/tasks/2").contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Task not found"));

    }

}
