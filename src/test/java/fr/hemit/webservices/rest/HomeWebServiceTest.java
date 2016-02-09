package fr.hemit.webservices.rest;

import static org.junit.Assert.*;

import org.junit.Test;

import fr.hemit.webservices.rest.HomeWebService;

public class HomeWebServiceTest {

	@Test
	public void test() {
		HomeWebService svc = new HomeWebService();
		assertEquals("hello", svc.hello());
	}

}
