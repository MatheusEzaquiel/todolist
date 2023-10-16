package br.com.mbe.todolist.domain.user;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
	
	@Id
	@GeneratedValue(generator = "UUID")
	private UUID id;
	
	private String name;
	
	@Column(unique = true)
	private String userName;
	private String password;
	
	@CreationTimestamp
	private LocalDateTime createdAt;
	
}
