package fr.hemit.webservices.dto.factories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.hemit.domain.Chapter;
import fr.hemit.domain.Scene;
import fr.hemit.webservices.dto.ChapterDto;
import fr.hemit.webservices.dto.SceneDto;

@Service
public class ChapterDtoFactory {

	@Autowired
	SceneDtoFactory sceneFact;
	
	public ChapterDto createChapterDtoFromChapter(Chapter c, boolean loadScenes){
		ChapterDto dto = new ChapterDto();
		dto.setChapterId(c.getChapterId());
		dto.setTitle(c.getTitle());
		dto.setSummary(c.getSummary());
		dto.setLastModification(c.getLastModification());
		
		if (loadScenes){
			List<SceneDto> scenes = new ArrayList<>();
			for (Scene s: c.getScenes()){
				scenes.add (sceneFact.createSceneDtoFromScene(s));
			}
			dto.setScenes(scenes);
		}
		
		return dto;
	}
}
