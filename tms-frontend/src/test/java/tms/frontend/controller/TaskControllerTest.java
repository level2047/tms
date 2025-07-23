package tms.frontend.controller;

import feign.FeignException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tms.frontend.model.Status;
import tms.frontend.model.Task;
import tms.frontend.model.TaskDTO;
import tms.frontend.service.TaskService;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TaskService taskService;


    @Test
    void getAllTasks() throws Exception {
        when(taskService.getAllTasks()).thenReturn(new ArrayList<>());
        mockMvc.perform(get("/task-list"))
                .andExpect(status().isOk());
        verify(taskService).getAllTasks();
    }

    @Test
    void createTask() throws Exception {
        mockMvc.perform(get("/create-task"))
                .andExpect(status().isOk());
    }

    @Test
    void editTask() throws Exception {
        Task task = Task.builder()
                .id("1")
                .title("Title 1")
                .description("Description 1")
                .status(Status.NEW)
                .due(LocalDateTime.now())
                .build();
        when(taskService.getTask("1")).thenReturn(task);
        mockMvc.perform(get("/edit-task/1"))
                .andExpect(status().isOk());
        verify(taskService).getTask("1");
    }

    @Test
    void taskDetails() throws Exception {
        Task task = Task.builder()
                .id("1")
                .title("Title 1")
                .description("Description 1")
                .status(Status.NEW)
                .due(LocalDateTime.now())
                .build();
        when(taskService.getTask("1")).thenReturn(task);
        mockMvc.perform(get("/task-details/1"))
                .andExpect(status().isOk());
        verify(taskService).getTask("1");
    }

    @Test
    void deleteTask() throws Exception {
        doNothing().when(taskService).deleteTask("1");
        mockMvc.perform(get("/delete-task/1"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Operation completed successfully")));
        verify(taskService).deleteTask("1");
    }

    @Test
    void editTaskNotFound() throws Exception {
        doThrow(FeignException.NotFound.class).when(taskService).getTask("1");
        mockMvc.perform(get("/edit-task/1"))
                .andExpect(status().isNotFound());
        verify(taskService).getTask("1");
    }

    @Test
    void deleteTaskNotFound() throws Exception {
        doThrow(FeignException.NotFound.class).when(taskService).deleteTask("1");
        mockMvc.perform(get("/delete-task/1"))
                .andExpect(status().isNotFound());
        verify(taskService).deleteTask("1");
    }

    @Test
    void saveTaskCreate() throws Exception {
        mockMvc.perform(post("/save-task")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("id=&title=111&description=222&status=NEW&due=22-07-2025+12%3A00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/?opResult=success"));
        Task task = TaskDTO.builder()
                .title("111")
                .description("222")
                .status(Status.NEW)
                .due("22-07-2025 12:00")
                .build().toTask();
        verify(taskService).createTask(task);
    }

    @Test
    void saveTaskUpdate() throws Exception {
        mockMvc.perform(post("/save-task")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content("id=1234&title=111&description=222&status=NEW&due=22-07-2025+12%3A00"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/?opResult=success"));
        Task task = TaskDTO.builder()
                .id("1234")
                .title("111")
                .description("222")
                .status(Status.NEW)
                .due("22-07-2025 12:00")
                .build().toTask();
        verify(taskService).updateTask(task);
    }

}
