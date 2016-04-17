package fr.hemit.webservices.dto.factories;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.hemit.domain.Chapter;
import fr.hemit.domain.Novel;
import fr.hemit.webservices.dto.ChapterDto;
import fr.hemit.webservices.dto.NovelDto;

@Service
public class NovelDtoFactory {
	
	@Autowired
	private ChapterDtoFactory chapterFactory;
	
	public NovelDto createNovelDtoFromNovel(Novel novel, boolean loadChapters) {
		NovelDto nov = new NovelDto();
		nov.setNovelId(novel.getNovelId());
		nov.setTitle(novel.getTitle());
		nov.setSummary(novel.getSummary());
		nov.setLastModification(novel.getLastModification());

		if (loadChapters) {
			List<ChapterDto> chapters = new ArrayList<>();
			for (Chapter c : novel.getChapters()) {
				chapters.add(chapterFactory.createChapterDtoFromChapter(c, false));
			}
			nov.setChapters(chapters);
		}

		return nov;
	}
}
