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
import fr.hemit.domain.Novel;
import fr.hemit.webservices.dto.ChapterDto;
import fr.hemit.webservices.dto.NovelDto;

public class NovelDtoFactoryTests extends BasicTests {
	Novel nov;
	Date now;
	
	@InjectMocks
	private NovelDtoFactory fact = new NovelDtoFactory();
	
	@Mock
	private ChapterDtoFactory chapterFact;
	
	@Before
	@Override
	public void setUp(){
		super.setUp();
		
		List<Chapter> chapters = new ArrayList<>();
		chapters.add(new Chapter());
		nov = new Novel();
		nov.setChapters(chapters);
		nov.setNovelId(1);
		nov.setTitle("t");
		nov.setSummary("s");
		now = new Date();
		nov.setLastModification(now);
		
		Mockito.when(chapterFact.createChapterDtoFromChapter(any(Chapter.class), any(boolean.class)))
			.thenReturn(new ChapterDto());
	}
	
	@Test
	public void CreateNovelDtoFromNovel_ShouldFillBasicInformation(){
		NovelDto dto = fact.createNovelDtoFromNovel(nov, false);
		assertEquals(1, dto.getNovelId());
		assertEquals("t", dto.getTitle());
		assertEquals("s", dto.getSummary());
		assertEquals(now, dto.getLastModification());
	}
	
	@Test
	public void CreateNovelDtoFromNovel_ShouldFillChaptersIfRequested(){
		NovelDto dto = fact.createNovelDtoFromNovel(nov, true);
		assertEquals(1,  dto.getChapters().size());
	}
	
	@Test
	public void CreateNovelDtoFromNovel_ShouldNotFillChaptersIfNotRequested(){
		NovelDto dto = fact.createNovelDtoFromNovel(nov, false);
		assertNull(dto.getChapters());
	}
}
