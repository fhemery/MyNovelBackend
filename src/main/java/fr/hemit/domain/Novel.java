package fr.hemit.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "novels")
public class Novel {

	public Novel() {
		this.chapters = new ArrayList<Chapter>();
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long novelId;

	@NotNull
	@Column(length = 255)
	private String title;
	
	@Column(length=2000)
	private String summary;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private User user;

	@NotNull
	@Column(name = "last_mod")
	private Date lastModification;

	@OneToMany(mappedBy = "novel", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<Chapter> chapters;
	
	public List<Chapter> getChapters() {
		return chapters;
	}

	public void setChapters(List<Chapter> chapters) {
		if (chapters != null) {
			this.chapters = chapters;
		}
	}

	public void addChapter(Chapter newChapter) {
		this.chapters.add(newChapter);
		newChapter.setNovel(this);
	}

	public Date getLastModification() {
		return lastModification;
	}

	public void setLastModification(Date lastModification) {
		this.lastModification = lastModification;
	}

	public long getNovelId() {
		return novelId;
	}

	public void setNovelId(long novelId) {
		this.novelId = novelId;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

}
