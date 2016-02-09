package fr.hemit.domain;

public enum EnumUserRole {

	USER("ROLE_USER");
	
	private String roleName;
	
	EnumUserRole(String role){
		this.roleName = role;
	}
	
	@Override
	public String toString(){
		return this.roleName;
	}
}
