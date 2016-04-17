package fr.hemit.webservices.dto;

import java.util.Date;
import java.util.List;

public class ChapterDto {

	public ChapterDto() {}

	private long chapterId;
	private String title;
	private String summary;
	private Date lastModification;
	private List<SceneDto> scenes;
	public long getChapterId() {
		return chapterId;
	}
	public void setChapterId(long chapterId) {
		this.chapterId = chapterId;
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
	public List<SceneDto> getScenes() {
		return scenes;
	}
	public void setScenes(List<SceneDto> scenes) {
		this.scenes = scenes;
	}
}
