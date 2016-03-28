package fr.hemit.webservices.rest;

import java.security.Principal;
import java.util.Date;

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
import fr.hemit.domain.Scene;
import fr.hemit.repository.NovelRepository;
import fr.hemit.repository.SceneRepository;

@RestController
@RequestMapping("api/novel/{novelId}/chapter/{chapterId}/scene")
public class SceneWebService {

	@Autowired
	private SceneRepository sceneRepo;
	
	@Autowired
	private NovelRepository novelRepo;
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<Scene> createScene(@PathVariable("novelId") long novelId,
			@PathVariable("chapterId") long chapterId,
			@RequestBody Scene scene,
			Principal princ){
		// Checking novel and chapter exists
		Novel nv = novelRepo.findOne(novelId);
		if (nv == null){
			return new ResponseEntity<Scene>(HttpStatus.NOT_FOUND);
		}
		else if (!nv.getUser().getUsername().equals(princ.getName())){
			return new ResponseEntity<Scene>(HttpStatus.FORBIDDEN);
		}
		Chapter chapterToFind = new Chapter();
		chapterToFind.setChapterId(chapterId);
		int chapterIndex = nv.getChapters().indexOf(chapterToFind);
		
		if (chapterIndex == -1){
			return new ResponseEntity<Scene>(HttpStatus.NOT_FOUND);
		}
		
		// Updating scene
		scene.setLastModification(new Date());
		scene.setChapter(nv.getChapters().get(chapterIndex));
		Scene newScene = sceneRepo.save(scene);
		
		// Sending response
		return new ResponseEntity<Scene>(newScene, HttpStatus.CREATED);
	}
}
