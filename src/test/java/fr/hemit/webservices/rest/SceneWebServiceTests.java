package fr.hemit.webservices.rest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import fr.hemit.BasicTests;
import fr.hemit.domain.Chapter;
import fr.hemit.domain.Novel;
import fr.hemit.domain.Scene;
import fr.hemit.domain.User;
import fr.hemit.repository.NovelRepository;
import fr.hemit.repository.SceneRepository;
import fr.hemit.webservices.dto.SceneDto;
import fr.hemit.webservices.dto.factories.SceneDtoFactory;

public class SceneWebServiceTests extends BasicTests {
	
	private static final long ownedNovelId = 2;
	private static final long nonExistingNovelId = 3;
	private static final long ownedByOtherNovelId = 4;
	
	private static final long ownedNovelChapterId = 12;
	private static final long nonOwnedNovelChapterId = 14;
	private static final long newSceneId = 72;
	
	private static final long ownedSceneId = 87;	
	
	@InjectMocks
	private SceneWebService svc = new SceneWebService();
	
	@Mock
	private SceneRepository sceneRepo;
	
	@Mock
	private NovelRepository novelRepo;
	
	@Mock
	private SceneDtoFactory sceneFact;
	
	@Captor
	private ArgumentCaptor<Scene> sceneCaptor;
	
	@Before
	@Override
	public void setUp(){
		super.setUp();
		this.setPrincipal(userFred);
		
		Novel fredNov = new Novel();
		Chapter newChapter = new Chapter("my chapter");
		newChapter.setChapterId(ownedNovelChapterId);
		fredNov.addChapter(newChapter);
		fredNov.setUser(userFred);
		
		Novel otherNov = new Novel();
		otherNov.setUser(userOther);
		
		Scene ownedScene = new Scene();
		ownedScene.setChapter(newChapter);
		newChapter.setNovel(fredNov);
		
		Mockito.when(novelRepo.findOne(ownedNovelId)).thenReturn(fredNov);
		Mockito.when(novelRepo.findOne(nonExistingNovelId)).thenReturn(null);
		Mockito.when(novelRepo.findOne(ownedByOtherNovelId)).thenReturn(otherNov);
		
		Mockito.when(sceneRepo.findOne(ownedSceneId)).thenReturn(ownedScene);

		SceneDto sceneFactDto = new SceneDto();
		sceneFactDto.setSceneId(newSceneId);
		sceneFactDto.setLastModification(new Date());
		Mockito.when(sceneFact.createSceneDtoFromScene(any(Scene.class))).thenReturn(sceneFactDto);
	}
	
	@Test
	public void createScene_NominalCase_Returns201(){
		ResponseEntity<SceneDto> response = svc.createScene(ownedNovelId, getNewScene(), currentUserPrincipal);
		assertStatus201Created(response);
	}
	
	@Test
	public void createScene_NominalCase_SetsSceneId(){
		ResponseEntity<SceneDto> response = svc.createScene(ownedNovelId, getNewScene(), currentUserPrincipal);
		Assert.assertEquals(newSceneId, response.getBody().getSceneId());
	}
	
	@Test
	public void createScene_NominalCase_SetsLastModificationDate(){
		ResponseEntity<SceneDto> response = svc.createScene(ownedNovelId, getNewScene(), currentUserPrincipal);
		Date now = new Date();
		Assert.assertTrue( now.getTime() - response.getBody().getLastModification().getTime() < 10000 );
	}
	
	@Test
	public void createScene_NominalCase_SetsChapter(){
		svc.createScene(ownedNovelId, getNewScene(), currentUserPrincipal);
		
		Mockito.verify(sceneRepo).save(sceneCaptor.capture());
		Scene providedScene = sceneCaptor.getValue();
		Assert.assertEquals("my chapter", providedScene.getChapter().getTitle());
	}
	
	@Test
	public void createScene_Returns404_WhenNovelDoesNotExist(){
		assertStatus404(svc.createScene(nonExistingNovelId, getNewScene(), currentUserPrincipal));
	}
	
	@Test
	public void createScene_Returns503_WhenNovelDoesNotBelongToUser(){
		assertStatus503(svc.createScene(ownedByOtherNovelId, getNewScene(), currentUserPrincipal));
	}
	
	@Test
	public void createScene_Returns404_WhenChapterDoesNotBelongToNovel(){
		SceneDto s = getNewScene();
		s.setChapterId(nonOwnedNovelChapterId);
		assertStatus404(svc.createScene(ownedNovelId, s, currentUserPrincipal));
	}
	
	@Test
	public void updateScene_Returns404_WhenSceneDoesNotExist(){
		final long nonExistingSceneId = 88;
		Mockito.when(sceneRepo.findOne(nonExistingSceneId)).thenReturn(null);
		assertStatus404(svc.updateScene(1, nonExistingSceneId, new SceneDto(), currentUserPrincipal));
	}
	
	@Test
	public void updateScene_Returns404_WhenSceneDoesNotBelongToNovel(){
		initSceneToUpdate();
		
		assertStatus404(svc.updateScene(2, sceneToUpdateId, new SceneDto(), currentUserPrincipal));
	}
	
	@Test
	public void updateScene_Returns503_WhenNovelDoesNotBelongToUser(){
		Scene s = initSceneToUpdate();
		s.getChapter().getNovel().setUser(new User(10, "someone else"));
		
		assertStatus503(svc.updateScene(1000, sceneToUpdateId, new SceneDto(), currentUserPrincipal));
	}
	
	@Test
	public void updateScene_NominalCase_Returns200(){
		initSceneToUpdate();
		
		assertStatus200Ok(svc.updateScene(1000, sceneToUpdateId, new SceneDto(), currentUserPrincipal));
	}
	
	@Test
	public void updateScene_NominalCase_ShouldUpdateTitleSummaryAndContent(){
		initSceneToUpdate();
		
		// Setting up proper scene to update
		SceneDto sceneToUpdate = new SceneDto();
		sceneToUpdate.setTitle("t2");
		sceneToUpdate.setSummary("summ");
		sceneToUpdate.setContent("content");
		Mockito.when(sceneRepo.save(any(Scene.class))).thenReturn(new Scene());
		
		svc.updateScene(1000l, sceneToUpdateId, sceneToUpdate , currentUserPrincipal);
		
		// Capturing result and verifying.
		Mockito.verify(sceneRepo).save(sceneCaptor.capture());
		Scene providedScene = sceneCaptor.getValue();
		assertEquals(sceneToUpdate.getTitle(), providedScene.getTitle());
		assertEquals(sceneToUpdate.getSummary(), providedScene.getSummary());
		assertEquals(sceneToUpdate.getContent(), providedScene.getContent());
	}
	
	@Test
	public void updateScene_NominalCase_ShouldUpdateLastModificationDate(){
		initSceneToUpdate();
		Mockito.when(sceneRepo.save(any(Scene.class))).thenReturn(new Scene());
		
		svc.updateScene(1000l, sceneToUpdateId, new SceneDto() , currentUserPrincipal);
		Mockito.verify(sceneRepo).save(sceneCaptor.capture());
		
		Scene providedScene = sceneCaptor.getValue();
		Date now = new Date();
		assertTrue(now.getTime() - providedScene.getLastModification().getTime() < 10000);
	}
	
	private static long sceneToUpdateId = 4;
	private Scene initSceneToUpdate(){
		Scene s = new Scene();
		Chapter c = new Chapter();
		s.setChapter(c);
		Novel n = new Novel();
		n.setNovelId(1000);
		n.setUser(userFred);
		c.setNovel(n);
		
		Mockito.when(sceneRepo.findOne(sceneToUpdateId)).thenReturn(s);
		
		return s;
	}
	
	private SceneDto getNewScene(){
		SceneDto newScene = new SceneDto();
		newScene.setChapterId(ownedNovelChapterId);
		
		Mockito.when(sceneRepo.save(any(Scene.class))).thenReturn(new Scene());
		return newScene;
	}
}
