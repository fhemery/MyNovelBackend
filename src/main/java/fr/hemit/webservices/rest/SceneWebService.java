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
import fr.hemit.webservices.dto.SceneDto;
import fr.hemit.webservices.dto.factories.SceneDtoFactory;

@RestController
@RequestMapping("api/novel/{novelId}/scene")
public class SceneWebService {

	@Autowired
	private SceneRepository sceneRepo;
	
	@Autowired
	private NovelRepository novelRepo;
	
	@Autowired
	private SceneDtoFactory sceneFact;
	
	@RequestMapping(value = "/", method = RequestMethod.POST)
	public ResponseEntity<SceneDto> createScene(@PathVariable("novelId") long novelId,
			@RequestBody SceneDto scene,
			Principal princ){
		// Checking novel and chapter exists
		Novel nv = novelRepo.findOne(novelId);
		if (nv == null){
			return new ResponseEntity<SceneDto>(HttpStatus.NOT_FOUND);
		}
		else if (!nv.getUser().getUsername().equals(princ.getName())){
			return new ResponseEntity<SceneDto>(HttpStatus.FORBIDDEN);
		}
		Chapter chapterToFind = new Chapter();
		chapterToFind.setChapterId(scene.getChapterId());
		int chapterIndex = nv.getChapters().indexOf(chapterToFind);
		
		if (chapterIndex == -1){
			return new ResponseEntity<SceneDto>(HttpStatus.NOT_FOUND);
		}
		
		// Updating scene
		Scene sceneToSave = new Scene();
		sceneToSave.setTitle(scene.getTitle());
		sceneToSave.setSummary(scene.getSummary());
		sceneToSave.setLastModification(new Date());
		sceneToSave.setChapter(nv.getChapters().get(chapterIndex));
		Scene newScene = sceneRepo.save(sceneToSave);
		
		// Sending response
		return new ResponseEntity<SceneDto>(sceneFact.createSceneDtoFromScene(newScene), HttpStatus.CREATED);
	}
	
	@RequestMapping(value="/{sceneId}", method = RequestMethod.POST)
	public ResponseEntity<SceneDto> updateScene(@PathVariable("novelId") long novelId,
			@PathVariable("sceneId") long sceneId,
			@RequestBody SceneDto scene,
			Principal princ){
		
		Scene dbScene = sceneRepo.findOne(sceneId);
		if (dbScene == null 
				|| dbScene.getChapter().getNovel().getNovelId() != novelId){
			return new ResponseEntity<SceneDto>(HttpStatus.NOT_FOUND);
		}
		
		Novel n = dbScene.getChapter().getNovel();
		if (!n.getUser().getUsername().equals(princ.getName())){
			return new ResponseEntity<SceneDto>(HttpStatus.FORBIDDEN);
		}
		
		dbScene.setTitle(scene.getTitle());
		dbScene.setSummary(scene.getSummary());
		dbScene.setContent(scene.getContent());
		dbScene.setLastModification(new Date());
		
		sceneRepo.save(dbScene);
		
		return new ResponseEntity<SceneDto>(HttpStatus.OK);
	}
}
