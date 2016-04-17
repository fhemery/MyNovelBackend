package fr.hemit.webservices.dto.factories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import fr.hemit.BasicTests;
import fr.hemit.domain.Chapter;
import fr.hemit.domain.Scene;
import fr.hemit.webservices.dto.ChapterDto;
import fr.hemit.webservices.dto.SceneDto;

public class ChapterDtoFactoryTests extends BasicTests {

	Chapter chapt;
	Date now;

	@InjectMocks
	private ChapterDtoFactory fact = new ChapterDtoFactory();

	@Mock
	 private SceneDtoFactory sceneFact;

	@Before
	@Override
	public void setUp() {
		super.setUp();

		List<Scene> scenes = new ArrayList<>();
		scenes.add(new Scene());
		chapt = new Chapter();
		chapt.setChapterId(1);
		chapt.setTitle("t");
		chapt.setSummary("s");
		now = new Date();
		chapt.setLastModification(now);
		chapt.setScenes(scenes);

		Mockito.when(sceneFact.createSceneDtoFromScene(any(Scene.class)))
				.thenReturn(new SceneDto());

	}

	@Test
	public void CreateNovelDtoFromChapter_ShouldFillBasicInformation() {
		ChapterDto dto = fact.createChapterDtoFromChapter(chapt, false);
		assertEquals(1, dto.getChapterId());
		assertEquals("t", dto.getTitle());
		assertEquals("s", dto.getSummary());
		assertEquals(now, dto.getLastModification());
	}

	@Test
	public void CreateNovelDtoFromChapter_ShouldFillScenesIfRequested() {
		ChapterDto dto = fact.createChapterDtoFromChapter(chapt, true);
		assertEquals(1, dto.getScenes().size());
	}

	@Test
	public void CreateNovelDtoFromChapter_ShouldNotFillScenessIfNotRequested() {
		ChapterDto dto = fact.createChapterDtoFromChapter(chapt, false);
		assertNull(dto.getScenes());
	}

}
