/**
 * 
 */
package org.bidtime.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.bidtime.session.bean.SessionUserBase;

/**
 * @author Administrator
 * 
 */
public class UserSessionCommon extends SessionCommon implements IUserSessionBase {

	// logout
	public void logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session_destroy(session);
	}
	
	// login
	public boolean login(HttpServletRequest request, SessionUserBase u) {
		return login(request, u, true);
	}
	
	// login
	private boolean login(HttpServletRequest request, SessionUserBase u, boolean newSession) {
		HttpSession session = request.getSession(newSession);
		return user2DoubleOnLine(session, u);
	}

	// relogin
	public boolean relogin(HttpServletRequest request) {
		SessionUserBase u = getUser(request);
		return login(request, u, false);
	}

	// relogin
	public boolean relogin(HttpServletRequest request, SessionUserBase u) {
		return login(request, u, false);
	}

	// getSessionLoginState
	public SessionLoginState getSessionLoginState(HttpServletRequest request) {
		HttpSession session = request.getSession();
		return getSessionLoginState(session);
	}
	
	// get
	public Object get(HttpServletRequest req, String ext) {
		return get(req, ext, false);
	}
	
	public Object get(HttpServletRequest req, String ext, boolean delete) {
		HttpSession session = req.getSession();
		return get(session, ext, delete);
	}

	// getUser
	public SessionUserBase getUser(HttpServletRequest request, boolean newSession) {
		return getUser(request.getSession(newSession));
	}

	// getUser
	public SessionUserBase getUser(HttpServletRequest request) {
		return getUser(request, false);
	}
	
	// set
	public void set(HttpServletRequest req, String ext, Object value, boolean newSession) {
		HttpSession session = req.getSession(newSession);
		set(session, ext, value);
	}
	
	// set
	public void set(HttpServletRequest req, String ext, Object o) {
		set(req, ext, o, true);
	}

}