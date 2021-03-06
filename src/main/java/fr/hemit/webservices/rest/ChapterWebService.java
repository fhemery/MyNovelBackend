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
import fr.hemit.repository.ChapterRepository;
import fr.hemit.repository.NovelRepository;

@RestController
@RequestMapping("/api/novel/{novelId}/chapter")
public class ChapterWebService {

	@Autowired
	ChapterRepository chapterRep;
	
	@Autowired
	NovelRepository novelRep;

	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ResponseEntity<List<Chapter>> getChaptersForNovel(@PathVariable("novelId") long novelId, Principal princ) {
		Novel nv = novelRep.findOne(novelId);
		
		if (nv == null){
			return new ResponseEntity<List<Chapter>>(HttpStatus.NOT_FOUND);
		}
		
		if (!nv.getUser().getUsername().equals(princ.getName())){
			return new ResponseEntity<List<Chapter>>(HttpStatus.FORBIDDEN);
		}
		
		return new ResponseEntity<List<Chapter>>(chapterRep.findAllByNovel(nv), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{chapterId}", method = RequestMethod.GET)
	public ResponseEntity<Chapter> getChapterDetails(@PathVariable("novelId") long novelId,
			@PathVariable("chapterId") long chapterId,
			Principal princ){
		Chapter retrievedChapter = chapterRep.findOne(chapterId);
		if (retrievedChapter == null 
				|| retrievedChapter.getNovel().getNovelId() != novelId){
			return new ResponseEntity<Chapter>(HttpStatus.NOT_FOUND);
		}
		else if (!retrievedChapter.getNovel().getUser().getUsername().equals(princ.getName())){
			return new ResponseEntity<Chapter>(HttpStatus.FORBIDDEN);
		}
		// Load the scenes
		retrievedChapter.getScenes();
		return new ResponseEntity<Chapter>(retrievedChapter, HttpStatus.OK);
	}
	
	@RequestMapping(value="/", method = RequestMethod.POST)
	public ResponseEntity<Chapter> createChapter(@PathVariable("novelId") long novelId,
			@RequestBody Chapter chapter, Principal princ){
		Novel nv = novelRep.findOne(novelId);
		
		if (nv == null){
			return new ResponseEntity<Chapter>(HttpStatus.NOT_FOUND);
		}
		if (!nv.getUser().getUsername().equals(princ.getName())){
			return new ResponseEntity<Chapter>(HttpStatus.FORBIDDEN);
		}
		
		chapter.setNovel(nv);
		chapter.setLastModification(new Date());
		
		Chapter savedChapter = chapterRep.save(chapter);
		return new ResponseEntity<Chapter>(savedChapter, HttpStatus.CREATED);
	}

}
