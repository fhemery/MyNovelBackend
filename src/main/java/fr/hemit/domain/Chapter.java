package fr.hemit.domain;

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

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

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
	
	@OneToMany(mappedBy="chapter", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	private List<Scene> scenes;
	
	@ManyToOne
	@JoinColumn(name="novel_id")
	@JsonIgnore
	private Novel novel;
	
	@Override
    public int hashCode() {
        return new HashCodeBuilder(17, 31). // two randomly chosen prime numbers
            append(chapterId).
            toHashCode();
    }

    @Override
    public boolean equals(Object obj) {
       if (!(obj instanceof Chapter))
            return false;
        if (obj == this)
            return true;

        Chapter rhs = (Chapter) obj;
        return new EqualsBuilder().
            // if deriving: appendSuper(super.equals(obj)).
            append(chapterId, rhs.chapterId).
            isEquals();
    }

	
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
	
	public List<Scene> getScenes() {
		return scenes;
	}

	public void setScenes(List<Scene> scenes) {
		this.scenes = scenes;
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
