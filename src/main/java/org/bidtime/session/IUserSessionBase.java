package org.bidtime.session;

import javax.servlet.http.HttpServletRequest;

import org.bidtime.session.bean.SessionUserBase;

public interface IUserSessionBase {

	public void logout(HttpServletRequest request);
	
	public boolean login(HttpServletRequest request, SessionUserBase u);

	public boolean relogin(HttpServletRequest request);

	public boolean relogin(HttpServletRequest request, SessionUserBase u);
	
	// getUserOfRequest
	
	public SessionLoginState getSessionLoginState(HttpServletRequest request);
	
	// public SessionLoginState getSessionLoginState(HttpServletRequest request, boolean cache);
	
	public SessionUserBase getUser(HttpServletRequest request);

	// public SessionUserBase getUser(HttpServletRequest request, boolean cache);
	
//	public Long getUserId(HttpServletRequest request, boolean force);

//	public String getUserName(HttpServletRequest request, boolean force);
	
	// isLogin
	
//	public boolean isLogin(HttpServletRequest request);

}
