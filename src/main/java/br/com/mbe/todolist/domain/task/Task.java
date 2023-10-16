package br.com.mbe.todolist.domain.task;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="tasks")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Task {
	
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;
	
	@Column(length = 50)
	private String title;
	private String description;
	private String priority;
	private LocalDateTime startAt;
	private LocalDateTime endAt;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
	@JoinColumn
	private UUID idUser;
	
	public void setTitle(String title) throws Exception {
		
		if(title.length() > 50) {
			
			throw new Exception("O título deve conter no máximo 50 caracteres");
			
		}
		
		this.title = title;
		
	}
	
}
