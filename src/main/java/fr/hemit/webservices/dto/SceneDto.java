package fr.hemit.webservices.dto;

import java.util.Date;

public class SceneDto {
	private long sceneId;
	private String title;
	private String summary;
	private String content;
	private Date lastModification;
	
	public long getSceneId() {
		return sceneId;
	}
	public void setSceneId(long sceneId) {
		this.sceneId = sceneId;
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
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public Date getLastModification() {
		return lastModification;
	}
	public void setLastModification(Date lastModification) {
		this.lastModification = lastModification;
	}
	
	
}
