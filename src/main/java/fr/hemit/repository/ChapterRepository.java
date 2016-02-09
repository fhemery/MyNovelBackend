package fr.hemit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import fr.hemit.domain.Chapter;
import fr.hemit.domain.Novel;

public interface ChapterRepository extends JpaRepository<Chapter, Long> {

	public List<Chapter> findAllByNovel(Novel novel);
	
}