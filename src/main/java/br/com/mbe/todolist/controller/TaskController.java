package br.com.mbe.todolist.controller;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.mbe.todolist.domain.task.ITaskRepository;
import br.com.mbe.todolist.domain.task.Task;
import br.com.mbe.todolist.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/tasks")
public class TaskController {

	@Autowired
	ITaskRepository taskRepository;

	@GetMapping
	public ResponseEntity list(HttpServletRequest request) {

		UUID idUser = (UUID) request.getAttribute("idUser");

		Task[] taskArr = taskRepository.findByIdUser(idUser);

		return ResponseEntity.status(HttpStatus.OK).body(taskArr);

	}

	@PostMapping
	public ResponseEntity create(@RequestBody Task task, HttpServletRequest request) {

		UUID idUser = (UUID) request.getAttribute("idUser");

		task.setIdUser(idUser);

		// Validação data/hora
		LocalDateTime currentDate = LocalDateTime.now();

		if (currentDate.isAfter(task.getStartAt()) || currentDate.isAfter(task.getEndAt())) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("As datas de início e de término devem ser maiores que a data atual");

		}

		if (task.getStartAt().isAfter(task.getEndAt())) {

			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("A data de início não deve ser maior que a data de término");

		}

		Task taskCreated = taskRepository.save(task);

		return ResponseEntity.status(HttpStatus.CREATED).body(taskCreated);

	}

	@PutMapping("/{idTask}")
	public ResponseEntity update(@RequestBody Task taskModel, HttpServletRequest request, @PathVariable UUID idTask) {

		UUID idUser = (UUID) request.getAttribute("idUser");

		Task task = taskRepository.findById(idTask).orElse(null);
		
		if(task == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Tarefa Não encontrada");
		}
		
		if (!task.getIdUser().equals(idUser)) {
			
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("Usuário sem permissão para alterar esta tarefa");
			
		}

		Utils.copyNonNulProperties(taskModel, task);

		var taskUpdated = taskRepository.save(task);
		
		return ResponseEntity.status(HttpStatus.OK).body(taskUpdated);

	}

}
