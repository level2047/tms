package tms.backend.repository;

import org.springframework.data.repository.ListCrudRepository;
import tms.backend.model.Task;

/**
 * MongoDB repositories.
 */
public interface TaskRepository extends ListCrudRepository<Task, String> {
}
