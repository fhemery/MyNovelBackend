package fr.hemit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.hemit.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {

	public User findOneByUsername(String username);
	
}