package fr.hemit.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name="users")
public class User {
	
	public User() {}
	
	public User(long userId, String name){
		this.userid = userId;
		this.username = name;
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long userid;
	
    @NotNull
    @Column(length = 60)
	private String password;
	
	@OneToMany(mappedBy="user", cascade = CascadeType.PERSIST)
	private List<UserRole> roles;
	
	@Column
	private String username;
	
	@Column
	private boolean enabled;

	public long getUserid() {
		return userid;
	}

	public void setUserid(long userid) {
		this.userid = userid;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<UserRole> getRoles() {
		return roles;
	}

	public void setRoles(List<UserRole> roles) {
		this.roles = roles;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	@Override
	public boolean equals(Object obj) {
		if((obj == null) || (obj.getClass() != this.getClass())) { return false; }
		User other = (User) obj;
		return this.getUsername().equals(other.getUsername());
	}
}
