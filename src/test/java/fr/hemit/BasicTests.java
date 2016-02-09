package fr.hemit;

import java.security.Principal;

import org.junit.Assert;
import org.junit.Before;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import fr.hemit.domain.User;
import fr.hemit.repository.UserRepository;

public class BasicTests {
	
	protected final User userFred = new User(1, "fred");
	protected final User userOther = new User(2, "someoneElse");
	
	@Mock 
	protected Principal currentUserPrincipal;

	@Mock
	protected UserRepository userRep;
		
	@Before
	public void setUp(){
		MockitoAnnotations.initMocks(this);
		Mockito.when(userRep.findOneByUsername(userFred.getUsername())).thenReturn(userFred);
	}
	
	protected void setPrincipal(User usr){
		Mockito.when(currentUserPrincipal.getName()).thenReturn(usr.getUsername());
	}
	
	protected <T> void assertStatus200Ok(ResponseEntity<T> response){
		Assert.assertEquals(HttpStatus.OK, response.getStatusCode());
	}
	
	protected <T> void assertStatus201Created(ResponseEntity<T> response){
		Assert.assertEquals(HttpStatus.CREATED, response.getStatusCode());
	}

	protected <T> void assertStatus400BadRequest(ResponseEntity<T> response){
		Assert.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
	}
	
	protected <T> void assertStatus404(ResponseEntity<T> response){
		Assert.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
	}
	
	protected <T> void assertStatus503(ResponseEntity<T> response){
		Assert.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
	}
}
