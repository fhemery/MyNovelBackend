package fr.hemit.webservices.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.http.ResponseEntity;

import fr.hemit.BasicTests;
import fr.hemit.domain.EnumUserRole;
import fr.hemit.domain.User;
import fr.hemit.webservices.rest.UserWebService;

public class UserWebServiceTests extends BasicTests {

	private static final String newUserName = "newUser";
	
	@InjectMocks
	private UserWebService svc = new UserWebService();
		
	private User usr;
	
	private ResponseEntity<User> resultingUser;

	@Before
	public void setUp(){
		super.setUp();
		
		usr = new User();
		usr.setUsername(newUserName);
		usr.setPassword("secret");
		
		
		Mockito.when(userRep.findOneByUsername(newUserName)).thenReturn(null);
		Mockito.when(userRep.save(usr)).then(updateUserId(3));
	}

	@Test
	public void Create_ForValidRequest_CreatesUser() {
		resultingUser = svc.createUser(usr);
		this.assertStatus201Created(resultingUser);
		Assert.assertEquals(3, resultingUser.getBody().getUserid());
	}
	
	@Test
	public void testWhenGoodPasswordIsRemoved(){
		resultingUser = svc.createUser(usr);
		Assert.assertEquals("", resultingUser.getBody().getPassword());
	}
	
	@Test
	public void testWhenGoodUserGetsBasicUserRole(){
		User resultingUser = svc.createUser(usr).getBody();
		Assert.assertEquals(1, resultingUser.getRoles().size());
		Assert.assertEquals(EnumUserRole.USER.toString(), resultingUser.getRoles().get(0).getRole());
	}
	
	@Test
	public void testWhenGoodUserIsEnabled(){
		User resultingUser = svc.createUser(usr).getBody();
		Assert.assertTrue(resultingUser.isEnabled());
	}
	
	@Test
	public void testReturns401WhenUserAlreadyExists(){
		usr.setUsername("existing");
		Mockito.when(userRep.findOneByUsername("existing")).thenReturn(new User());
		
		this.assertStatus400BadRequest(svc.createUser(usr));
	}
	
	// Helper methods
	private Answer<User> updateUserId(int id){
		usr.setUserid(id);
		return new Answer<User>(){
			public User answer(InvocationOnMock invocation){
				return usr;
			}
		};
	}
}
