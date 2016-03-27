package fr.hemit.webservices.rest;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.hemit.BasicTests;
import fr.hemit.webservices.rest.HomeWebService;

public class HomeWebServiceTest extends BasicTests {

	@Test
	public void test() {
		this.setPrincipal(userFred);
		HomeWebService svc = new HomeWebService();
		assertEquals("fred", svc.hello(currentUserPrincipal).getBody().getUsername());
	}

}
