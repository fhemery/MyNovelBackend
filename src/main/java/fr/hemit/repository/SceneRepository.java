package fr.hemit.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.hemit.domain.Scene;

public interface SceneRepository extends JpaRepository<Scene, Long> {
	
}