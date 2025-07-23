package tms.frontend.controller;

import feign.FeignException;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import tms.frontend.error.ResourceNotFoundException;
import tms.frontend.error.ServerInternalErrorException;
import tms.frontend.model.Task;
import tms.frontend.model.TaskDTO;
import tms.frontend.service.TaskService;

import java.util.List;

/**
 * This class is the main controller for the tasks web interface.
 */
@Slf4j
@Controller
public class TaskController {

    @Autowired
    private TaskService taskService;


    @GetMapping
    public String index(Model model) {
        return listAllTasks(model);
    }

    /**
     * List all tasks.
     *
     * @param model model
     * @return view
     */
    @GetMapping("/task-list")
    public String listAllTasks(Model model) {
        try {
            List<Task> tasks = taskService.getAllTasks();
            model.addAttribute("tasks", tasks);
            log.info("Retrieved {} tasks", tasks.size());

        } catch (Exception e) {
            log.error("Got error: ", e);
            model.addAttribute("opResult", "error");

        }
        return "tasks";
    }

    /**
     * Create a task.
     *
     * @param model model
     * @return view
     */
    @GetMapping("/create-task")
    public String createTask(Model model) {
        model.addAttribute("taskDto", new TaskDTO());
        return "edit";
    }

    /**
     * Edit a task.
     *
     * @param id    task id
     * @param model model
     * @return view
     */
    @GetMapping("/edit-task/{id}")
    public String editTask(@PathVariable String id, Model model) {
        try {
            Task task = taskService.getTask(id);
            log.info("Editing task {}", task);
            model.addAttribute("taskDto", TaskDTO.fromTask(task));
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new ServerInternalErrorException();
        }
        return "edit";
    }

    /**
     * Details of a task.
     *
     * @param id    task id
     * @param model model
     * @return view
     */
    @GetMapping("/task-details/{id}")
    public String taskDetails(@PathVariable String id, Model model) {
        try {
            Task task = taskService.getTask(id);
            model.addAttribute("taskDto", TaskDTO.fromTask(task));
            log.info("Details for task {}", task);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new ServerInternalErrorException();
        }
        return "details";
    }

    /**
     * Delete a task.
     *
     * @param id    task id
     * @param model model
     * @return view
     */
    @GetMapping("/delete-task/{id}")
    public String deleteTask(@PathVariable String id, Model model) {
        try {
            taskService.deleteTask(id);
            model.addAttribute("opResult", "success");
            log.info("Deleted task {}", id);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException();
        } catch (Exception e) {
            throw new ServerInternalErrorException();
        }
        return listAllTasks(model);
    }

    /**
     * Create or update a task base on the entity id field presence.
     *
     * @param model         model
     * @param taskDto       task DTO
     * @param redirectAttrs redirect attributes
     * @return view
     */
    @PostMapping("/save-task")
    public String saveTask(Model model,
                           @ModelAttribute("taskDto")
                           @Valid TaskDTO taskDto,
                           RedirectAttributes redirectAttrs) {
        try {
            Task task = taskDto.toTask();
            if (task.getId() != null) {
                taskService.updateTask(task);
                log.info("Updated task: {}", task);
            } else {
                log.info("Created task: {}", task);
                taskService.createTask(task);
            }
            redirectAttrs.addAttribute("opResult", "success");
        } catch (Exception e) {
            log.error("Got error: ", e);
            redirectAttrs.addAttribute("opResult", "error");
        }
        return "redirect:/";
    }


}
