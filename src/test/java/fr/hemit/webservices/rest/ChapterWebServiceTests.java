package fr.hemit.webservices.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.ResponseEntity;

import fr.hemit.BasicTests;
import fr.hemit.domain.Chapter;
import fr.hemit.domain.Novel;
import fr.hemit.repository.ChapterRepository;
import fr.hemit.repository.NovelRepository;

public class ChapterWebServiceTests extends BasicTests {
	
	private static final long ownedNovelId = 2;
	private static final long nonExistingNovelId = 3;
	private static final long ownedByOtherNovelId = 4;
	
	private static final long newChapterId = 72;
	
	@InjectMocks
	private ChapterWebService svc = new ChapterWebService();
	
	@Mock
	private ChapterRepository chapterRep;
	
	@Mock
	private NovelRepository novelRep;
	
	@Before
	@Override
	public void setUp(){
		super.setUp();
		this.setPrincipal(userFred);
		
		List<Chapter> chapters = new ArrayList<>();
		chapters.add(new Chapter("my chapter"));
		
		Novel fredNov = new Novel();
		fredNov.setNovelId(ownedNovelId);
		fredNov.setUser(userFred);
		
		Novel otherNov = new Novel();
		otherNov.setUser(userOther);
		
		Mockito.when(chapterRep.findAllByNovel(Matchers.any(Novel.class))).thenReturn(chapters);
		Mockito.when(novelRep.findOne(ownedNovelId)).thenReturn(fredNov);
		Mockito.when(novelRep.findOne(nonExistingNovelId)).thenReturn(null);
		Mockito.when(novelRep.findOne(ownedByOtherNovelId)).thenReturn(otherNov);
	}
	
	@Test
	public void getChaptersForNovel_NominalCase_ReturnsAllChapters(){
		ResponseEntity<List<Chapter>> response= svc.getChaptersForNovel(ownedNovelId, this.currentUserPrincipal);
		
		assertStatus200Ok(response);
		Assert.assertEquals(1, response.getBody().size());
	}
	
	@Test
	public void getChaptersForNovel_Returns404_WhenNovelDoesNotExist(){
		Mockito.when(chapterRep.findAllByNovel(Matchers.any(Novel.class))).thenReturn(new ArrayList<>());
		
		ResponseEntity<List<Chapter>> response = svc.getChaptersForNovel(nonExistingNovelId, currentUserPrincipal);
		
		assertStatus404(response);
	}
	
	@Test
	public void getChaptersForNovel_Returns403_WhenNovelIsNotUsersNovel(){
		assertStatus503(svc.getChaptersForNovel(ownedByOtherNovelId, currentUserPrincipal));
	}
	
	@Test
	public void createChapter_Returns404_WhenNovelDoesNotExist(){
		Mockito.when(chapterRep.findAllByNovel(Matchers.any(Novel.class))).thenReturn(new ArrayList<>());
		ResponseEntity<Chapter> response = svc.createChapter(nonExistingNovelId, null, currentUserPrincipal);
		
		assertStatus404(response);
	}
	
	@Test
	public void createChapter_Returns403_WhenUserDoesNotOwnNovel(){
		assertStatus503(svc.createChapter(ownedByOtherNovelId, null, currentUserPrincipal));
	}
	
	@Test
	public void createChapter_NominalCase_Returns201(){
		Chapter newChapter = new Chapter();
		newChapter.setTitle("New title");
		
		assertStatus201Created(svc.createChapter(ownedNovelId, newChapter, currentUserPrincipal));
	}
	
	@Test
	public void createChapter_NominalCase_ReturnsChapterWithId(){
		ResponseEntity<Chapter> response = svc.createChapter(ownedNovelId, getNewChapter(), currentUserPrincipal);
		Assert.assertEquals(newChapterId, response.getBody().getChapterId());
	}
	
	@Test
	public void createChapter_NominalCase_BindsChapterToNovel(){
		ResponseEntity<Chapter> response = svc.createChapter(ownedNovelId, getNewChapter(), currentUserPrincipal);
		Assert.assertEquals(ownedNovelId, response.getBody().getNovel().getNovelId());
	}
	
	@Test
	public void createChapter_NominalCase_SetsLastModificationDate(){
		ResponseEntity<Chapter> response = svc.createChapter(ownedNovelId, getNewChapter(), currentUserPrincipal);
		Date now = new Date();
		Assert.assertTrue( now.getTime() - response.getBody().getLastModification().getTime() < 10000 );
	}
	
	private Chapter getNewChapter(){
		Chapter newChapter = new Chapter("My title");
		
		Mockito.when(chapterRep.save(newChapter)).then(updateChapterId(newChapterId, newChapter));
		return newChapter;
	}
	
	private Answer<Chapter> updateChapterId(long id, Chapter chapter){
		return new Answer<Chapter>(){
			public Chapter answer(InvocationOnMock invocation){
				chapter.setChapterId(id);
				return chapter;
			}
		};
	}

}
