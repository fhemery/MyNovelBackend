package fr.hemit.webservices.dto.factories;

import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;

import fr.hemit.BasicTests;
import fr.hemit.domain.Scene;
import fr.hemit.webservices.dto.SceneDto;

public class SceneDtoFactoryTests extends BasicTests {
	Scene scene;
	Date now;
	
	@InjectMocks
	private SceneDtoFactory fact = new SceneDtoFactory();
	
	@Before
	@Override
	public void setUp(){
		super.setUp();
		
		scene = new Scene();
		scene.setSceneId(1);
		scene.setTitle("t");
		scene.setSummary("s");
		scene.setContent("c");
		now = new Date();
		scene.setLastModification(now);
	}
	
	@Test
	public void CreateNovelDtoFromNovel_ShouldFillBasicInformation(){
		SceneDto dto = fact.createSceneDtoFromScene(scene);
		assertEquals(1, dto.getSceneId());
		assertEquals("t", dto.getTitle());
		assertEquals("s", dto.getSummary());
		assertEquals("c", dto.getContent());
		assertEquals(now, dto.getLastModification());
	}
	
}
