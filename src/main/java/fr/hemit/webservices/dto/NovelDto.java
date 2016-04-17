package fr.hemit.webservices.dto;

import java.util.Date;
import java.util.List;

public class NovelDto {

	public NovelDto() {}

	private long novelId;

	private String title;

	private String summary;

	private Date lastModification;

	private List<ChapterDto> chapters;

	public long getNovelId() {
		return novelId;
	}

	public void setNovelId(long novelId) {
		this.novelId = novelId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public Date getLastModification() {
		return lastModification;
	}

	public void setLastModification(Date lastModification) {
		this.lastModification = lastModification;
	}

	public List<ChapterDto> getChapters() {
		return chapters;
	}

	public void setChapters(List<ChapterDto> chapters) {
		this.chapters = chapters;
	}
}
