package com.akmade.security.data;
// Generated Oct 22, 2016 7:06:32 AM by Hibernate Tools 5.2.0.Beta1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Phone generated by hbm2java
 */
@Entity
@Table(name = "phone", catalog = "security")
public class Phone implements java.io.Serializable {

	private Integer phoneId;
	private PhoneType phoneType;
	private User user;
	private String phone;
	private Date createdDate;
	private Date lastmodifiedDate;

	public Phone() {
	}

	public Phone(PhoneType phoneType, User user, String phone, Date createdDate, Date lastmodifiedDate) {
		this.phoneType = phoneType;
		this.user = user;
		this.phone = phone;
		this.createdDate = createdDate;
		this.lastmodifiedDate = lastmodifiedDate;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)

	@Column(name = "phone_id", unique = true, nullable = false)
	public Integer getPhoneId() {
		return this.phoneId;
	}

	public void setPhoneId(Integer phoneId) {
		this.phoneId = phoneId;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "phone_type_id", nullable = false)
	public PhoneType getPhoneType() {
		return this.phoneType;
	}

	public void setPhoneType(PhoneType phoneType) {
		this.phoneType = phoneType;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	public User getUser() {
		return this.user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Column(name = "phone", nullable = false, length = 45)
	public String getPhone() {
		return this.phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "created_date", nullable = false, length = 19)
	public Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "lastmodified_date", nullable = false, length = 19)
	public Date getLastmodifiedDate() {
		return this.lastmodifiedDate;
	}

	public void setLastmodifiedDate(Date lastmodifiedDate) {
		this.lastmodifiedDate = lastmodifiedDate;
	}

}
