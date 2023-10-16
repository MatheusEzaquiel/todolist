package br.com.mbe.todolist.domain.task;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ITaskRepository extends JpaRepository<Task, UUID>{
	Task[] findByIdUser(UUID id);
	
}
