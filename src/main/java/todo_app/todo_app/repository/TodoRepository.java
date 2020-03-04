package todo_app.todo_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.config.Task;
import org.springframework.stereotype.Repository;

@Repository
public interface TodoRepository extends JpaRepository<Task,Integer>{

}
