package fr.hemit.webservices.dto.factories;

import org.springframework.stereotype.Service;

import fr.hemit.domain.Scene;
import fr.hemit.webservices.dto.SceneDto;

@Service
public class SceneDtoFactory {

	public SceneDto createSceneDtoFromScene(Scene s) {
		SceneDto scene = new SceneDto();
		scene.setSceneId(s.getSceneId());
		scene.setSummary(s.getSummary());
		scene.setTitle(s.getTitle());
		scene.setContent(s.getContent());
		scene.setLastModification(s.getLastModification());
		
		return scene;
	}

}
