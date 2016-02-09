package fr.hemit.webservices.rest;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.hemit.domain.EnumUserRole;
import fr.hemit.domain.User;
import fr.hemit.domain.UserRole;
import fr.hemit.repository.UserRepository;
import fr.hemit.utils.StringUtils;

@RestController
@RequestMapping("/api/user")
public class UserWebService {

	@Autowired
	UserRepository userRep;

	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<User> createUser(@RequestBody User user) {

		// Check user does not already exist.
		if (userRep.findOneByUsername(user.getUsername()) != null) {
			return new ResponseEntity<User>(HttpStatus.BAD_REQUEST);
		}

		// Everything is fine
		List<UserRole> roles = new ArrayList<>();
		roles.add(new UserRole(EnumUserRole.USER.toString(), user));
		user.setRoles(roles);
		user.setEnabled(true);

		// Encrypt password in database
		user.setPassword(StringUtils.HashPasswordWithMd5(user.getPassword()));
		

		// Send user to database
		user = userRep.save(user);

		// All good, set the id and clear password
		user.setPassword("");
		return new ResponseEntity<User>(user, HttpStatus.CREATED);
	}
}
