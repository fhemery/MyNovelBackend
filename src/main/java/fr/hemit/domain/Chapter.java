package fr.hemit.domain;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name="chapters")
public class Chapter {

	public Chapter(){}
	
	public Chapter(String title) {
		this.title = title;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long chapterId;
	
	@NotNull
	@Column(length=255)
	private String title;
	
	@Column(length=2000)
	private String summary;
	
	@Column
	private Date lastModification;
	
	@ManyToOne
	@JoinColumn(name="novel_id")
	@JsonIgnore
	private Novel novel;
	
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

	public Novel getNovel() {
		return novel;
	}

	public void setNovel(Novel novel) {
		this.novel = novel;
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
	
	
}
