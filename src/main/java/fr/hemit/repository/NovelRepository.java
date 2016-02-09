package fr.hemit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.hemit.domain.Novel;
import fr.hemit.domain.User;

public interface NovelRepository extends JpaRepository<Novel, Long> {

	public List<Novel> findAllByUser(User user);
	
}