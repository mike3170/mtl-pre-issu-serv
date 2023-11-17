/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stit.entity;

import java.io.Serializable;
//import javax.persistence.Basic;
//import javax.persistence.Column;
//import javax.persistence.Entity;
//import javax.persistence.Id;
//import javax.persistence.NamedQueries;
//import javax.persistence.NamedQuery;
//import javax.persistence.Table;

/**
 *
 * @author User
 */
//@Entity
//@Table(name = "USERS")
//@NamedQueries({
//	@NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u")})
public class Users implements Serializable {

	/*
	private static final long serialVersionUID = 1L;
	@Id
  @Basic(optional = false)
  @Column(name = "USERNAME")
	private String username;
	@Basic(optional = false)
  @Column(name = "PASSWORD")
	private String password;
	@Basic(optional = false)
  @Column(name = "ENABLED")
	private short enabled;

	public Users() {
	}

	public Users(String username) {
		this.username = username;
	}

	public Users(String username, String password, short enabled) {
		this.username = username;
		this.password = password;
		this.enabled = enabled;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public short getEnabled() {
		return enabled;
	}

	public void setEnabled(short enabled) {
		this.enabled = enabled;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (username != null ? username.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		// TODO: Warning - this method won't work in the case the id fields are not set
		if (!(object instanceof Users)) {
			return false;
		}
		Users other = (Users) object;
		if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "com.stit.entity.Users[ username=" + username + " ]";
	}
  */
	
}
