package com.akmade.security.data;
// Generated Oct 22, 2016 7:06:32 AM by Hibernate Tools 5.2.0.Beta1

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * RoleType generated by hbm2java
 */
@Entity
@Table(name = "role_type", catalog = "security")
public class RoleType implements java.io.Serializable {

	private Integer roleTypeId;
	private String type;
	private String description;
	private Set<Role> roles = new HashSet<Role>(0);

	public RoleType() {
	}

	public RoleType(String type) {
		this.type = type;
	}

	public RoleType(String type, String description, Set<Role> roles) {
		this.type = type;
		this.description = description;
		this.roles = roles;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "role_type_id", unique = true, nullable = false)
	public Integer getRoleTypeId() {
		return this.roleTypeId;
	}

	public void setRoleTypeId(Integer roleTypeId) {
		this.roleTypeId = roleTypeId;
	}

	@Column(name = "type", nullable = false, length = 50)
	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Column(name = "description")
	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "roleType")
	public Set<Role> getRoles() {
		return this.roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

}
