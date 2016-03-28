package fr.hemit.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="scenes")
public class Scene {

	public Scene(){}
	
	public Scene(String title){
		this.title = title;
	}
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long sceneId;
	
	@NotNull
	@Column(length=255)
	private String title;
	
	@Column(length=2000)
	private String summary;
	
	@Lob
	@Column
	private String content;
	
	@Column
	private Date lastModification;
	
	@ManyToOne
	@JoinColumn(name="chapter_id")
	@JsonIgnore
	private Chapter chapter;

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

	public Chapter getChapter() {
		return chapter;
	}

	public void setChapter(Chapter chapter) {
		this.chapter = chapter;
	}
	
	
}
