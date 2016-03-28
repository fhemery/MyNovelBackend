package fr.hemit.webservices.rest;

import java.util.Date;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.ResponseEntity;

import fr.hemit.BasicTests;
import fr.hemit.domain.Chapter;
import fr.hemit.domain.Novel;
import fr.hemit.domain.Scene;
import fr.hemit.repository.NovelRepository;
import fr.hemit.repository.SceneRepository;

public class SceneWebServiceTests extends BasicTests {
	
	private static final long ownedNovelId = 2;
	private static final long nonExistingNovelId = 3;
	private static final long ownedByOtherNovelId = 4;
	
	private static final long ownedNovelChapterId = 12;
	private static final long nonOwnedNovelChapterId = 14;
	private static final long newSceneId = 72;
	
	@InjectMocks
	private SceneWebService svc = new SceneWebService();
	
	@Mock
	private SceneRepository sceneRepo;
	
	@Mock
	private NovelRepository novelRepo;
	
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
		
		Mockito.when(novelRepo.findOne(ownedNovelId)).thenReturn(fredNov);
		Mockito.when(novelRepo.findOne(nonExistingNovelId)).thenReturn(null);
		Mockito.when(novelRepo.findOne(ownedByOtherNovelId)).thenReturn(otherNov);
	}
	
	@Test
	public void createScene_NominalCase_Returns201(){
		ResponseEntity<Scene> response = svc.createScene(ownedNovelId, ownedNovelChapterId, getNewScene(), currentUserPrincipal);
		assertStatus201Created(response);
	}
	
	@Test
	public void createScene_NominalCase_SetsSceneId(){
		ResponseEntity<Scene> response = svc.createScene(ownedNovelId, ownedNovelChapterId, getNewScene(), currentUserPrincipal);
		Assert.assertEquals(newSceneId, response.getBody().getSceneId());
	}
	
	@Test
	public void createScene_NominalCase_SetsLastModificationDate(){
		ResponseEntity<Scene> response = svc.createScene(ownedNovelId, ownedNovelChapterId, getNewScene(), currentUserPrincipal);
		Date now = new Date();
		Assert.assertTrue( now.getTime() - response.getBody().getLastModification().getTime() < 10000 );
	}
	
	@Test
	public void createScene_NominalCase_SetsChapter(){
		ResponseEntity<Scene> response = svc.createScene(ownedNovelId, ownedNovelChapterId, getNewScene(), currentUserPrincipal);
		Assert.assertEquals("my chapter", response.getBody().getChapter().getTitle());
	}
	
	@Test
	public void createScene_Returns404_WhenNovelDoesNotExist(){
		assertStatus404(svc.createScene(nonExistingNovelId, ownedNovelChapterId, getNewScene(), currentUserPrincipal));
	}
	
	@Test
	public void createScene_Returns503_WhenNovelDoesNotBelongToUser(){
		assertStatus503(svc.createScene(ownedByOtherNovelId, ownedNovelChapterId, getNewScene(), currentUserPrincipal));
	}
	
	@Test
	public void createScene_Returns404_WhenChapterDoesNotBelongToNovel(){
		assertStatus404(svc.createScene(ownedNovelId, nonOwnedNovelChapterId, getNewScene(), currentUserPrincipal));
	}
	
	private Scene getNewScene(){
		Scene newScene = new Scene("My title");
		
		Mockito.when(sceneRepo.save(newScene)).then(updateSceneId(newSceneId, newScene));
		return newScene;
	}
	
	private Answer<Scene> updateSceneId(long id, Scene scene){
		return new Answer<Scene>(){
			public Scene answer(InvocationOnMock invocation){
				scene.setSceneId(id);
				return scene;
			}
		};
	}
}
