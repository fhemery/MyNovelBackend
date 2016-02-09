package fr.hemit.webservices.rest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fr.hemit.BasicTests;
import fr.hemit.domain.Chapter;
import fr.hemit.domain.Novel;
import fr.hemit.repository.NovelRepository;

public class NovelWebServiceTests extends BasicTests {
	
	private static final long NominalNovelId = 2;
	private static final long NonExistingNovelId = 3;
	private static final long NonOwnedNovelId = 4;
	
	private static final int newNovelId = 3;

	@InjectMocks
	private NovelWebService svc = new NovelWebService();
	
	@Mock
	private NovelRepository novelRep;
	
	private ResponseEntity<Novel> novelResponse;
	
	@Before
	@Override
	public void setUp(){
		super.setUp();
		this.setPrincipal(userFred);
		
		Novel nov = new Novel();
		nov.setUser(userFred);
		ArrayList<Novel> novels = new ArrayList<>();
		novels.add(nov);
		
		Novel nonAllowedNovel = new Novel();
		nonAllowedNovel.setUser(userOther);
		
		Mockito.when(novelRep.findOne(NominalNovelId)).thenReturn(nov);
		Mockito.when(novelRep.findOne(NonOwnedNovelId)).thenReturn(nonAllowedNovel);
		Mockito.when(novelRep.findAllByUser(userFred)).thenReturn(novels);
	}

	@Test
	public void getNovelById_NominalCase() {
		novelResponse = svc.getNovelById(NominalNovelId, currentUserPrincipal);
		
		this.assertStatus200Ok(novelResponse);
		Assert.assertNotNull(novelResponse.getBody());
	}
	
	@Test
	public void getNovelById_IfNovelDoesNotExist_Returns404(){
		this.assertStatus404(svc.getNovelById(NonExistingNovelId, currentUserPrincipal));
	}
	
	@Test
	public void getNovel_IfNovelDoesNotBelongToUser_Returns503(){
		this.assertStatus503(svc.getNovelById(NonOwnedNovelId, currentUserPrincipal));
	}
	
	@Test
	public void getNovels_NominalCase(){
		ResponseEntity<List<Novel>> response = svc.getNovels(currentUserPrincipal);
		
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
		Assert.assertEquals(1, response.getBody().size());
	}
	
	@Test
	public void createNovel_NominalCase_Returns200(){
		Novel newNovel = new Novel();
		newNovel.setTitle("My title");
		
		ResponseEntity<Novel> response = svc.createNovel(newNovel, currentUserPrincipal);
		this.assertStatus201Created(response);
	}
	
	@Test
	public void createNovel_NominalCase_SetsNovelId(){
		Novel newNovel = getNewNovel();
		ResponseEntity<Novel> response = svc.createNovel(newNovel, currentUserPrincipal);
		
		Assert.assertEquals(newNovelId, response.getBody().getNovelId() );
	}
	
	@Test
	public void createNovel_NominalCase_SetsDateOfLastModification(){
		Novel newNovel = getNewNovel();
		
		ResponseEntity<Novel> response = svc.createNovel(newNovel, currentUserPrincipal);
		Date now = new Date();
		Assert.assertTrue( now.getTime() - response.getBody().getLastModification().getTime() < 10000 );
	}
	
	@Test 
	public void createNovel_NominalCase_CreatesOneChapter(){
		Novel newNovel = getNewNovel();
		
		ResponseEntity<Novel> response = svc.createNovel(newNovel, currentUserPrincipal);
		Assert.assertEquals(1, response.getBody().getChapters().size());
	}
	
	@Test
	public void createNovel_NominalCase_SetsDateOfLastModificationOnChapterAsWell(){
		Novel newNovel = getNewNovel();
		
		ResponseEntity<Novel> response = svc.createNovel(newNovel, currentUserPrincipal);
		Chapter chapter = response.getBody().getChapters().get(0);
		Date now = new Date();
		Assert.assertTrue( now.getTime() - chapter.getLastModification().getTime() < 10000 );
	}
	
	private Novel getNewNovel(){
		Novel newNovel = new Novel();
		newNovel.setTitle("My title");
		
		Mockito.when(novelRep.save(newNovel)).then(updateNovelId(newNovelId, newNovel));
		return newNovel;
	}
	
	private Answer<Novel> updateNovelId(int id, Novel novel){
		return new Answer<Novel>(){
			public Novel answer(InvocationOnMock invocation){
				novel.setNovelId(id);
				return novel;
			}
		};
	}
}
