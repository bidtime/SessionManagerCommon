package org.bidtime.session.bean;

@SuppressWarnings("serial")
public abstract class SessionUserToken extends SessionUserBase {

	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
