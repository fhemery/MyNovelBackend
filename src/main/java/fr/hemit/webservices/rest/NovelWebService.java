package fr.hemit.webservices.rest;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import fr.hemit.domain.Chapter;
import fr.hemit.domain.Novel;
import fr.hemit.domain.User;
import fr.hemit.repository.NovelRepository;
import fr.hemit.repository.UserRepository;

@RestController
@RequestMapping("/api/novel")
public class NovelWebService {

	@Autowired
	NovelRepository novelRep;
	
	@Autowired
	UserRepository userRep;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<List<Novel>> getNovels(Principal principal){
		// Get the user
		User usr = userRep.findOneByUsername(principal.getName());
		
		return new ResponseEntity<List<Novel>> (novelRep.findAllByUser(usr), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Novel> getNovelById(@PathVariable("id") long id, Principal principal)
	{
		Novel nov = novelRep.findOne(id);
		if (nov == null){
			return new ResponseEntity<Novel>(HttpStatus.NOT_FOUND);
		}
		
		if (!nov.getUser().getUsername().equals(principal.getName())){
			return new ResponseEntity<Novel>(HttpStatus.FORBIDDEN);
		}

		ResponseEntity<Novel> response = new ResponseEntity<Novel>(nov, HttpStatus.OK);
		return response;
	}

	@RequestMapping(value="/", method = RequestMethod.POST)
	public ResponseEntity<Novel> createNovel(@RequestBody Novel newNovel, Principal principal) {
		User usr = userRep.findOneByUsername(principal.getName());
		Date now = new Date();
		newNovel.setNovelId(0);
		newNovel.setUser(usr);
		newNovel.setLastModification(now);
		Chapter newChapter = new Chapter("New blank chapter");
		newChapter.setLastModification(now);
		newNovel.addChapter(newChapter);
		
		Novel savedNovel = novelRep.save(newNovel);
		
		return new ResponseEntity<Novel>(savedNovel, HttpStatus.CREATED);
	}

}
