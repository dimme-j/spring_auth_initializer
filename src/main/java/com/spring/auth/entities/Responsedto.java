package com.spring.auth.entities;

public class Responsedto {
	private String jwt;
	private String userid;
	public Responsedto(String userid, String token) {
		this.userid= userid;
		this.jwt= token;
	}
	/**
	 * @return the jwt
	 */
	public String getJwt() {
		return jwt;
	}
	/**
	 * @param jwt the jwt to set
	 */
	public void setJwt(String jwt) {
		this.jwt = jwt;
	}
	/**
	 * @return the userid
	 */
	public String getUserid() {
		return userid;
	}
	/**
	 * @param userid the userid to set
	 */
	public void setUserid(String userid) {
		this.userid = userid;
	}
	
	
}
