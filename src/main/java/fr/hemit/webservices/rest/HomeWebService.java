package fr.hemit.webservices.rest;

import java.security.Principal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HomeWebService {

	class HelloResponse {
		private String username;
	
		public String getUsername(){
			return this.username;
		}
		
		public void setUsername(String username){
			this.username = username;
		}
	}
	
	@RequestMapping(value="/")
	public ResponseEntity<HelloResponse> hello(Principal user){
		HelloResponse response = new HelloResponse();
		if (user.getName() != null && !user.getName().equals("")){
			response.setUsername(user.getName());
		}
		return new ResponseEntity<HelloResponse>(response, HttpStatus.OK);
	}
}
