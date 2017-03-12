/**
 * 
 */
package org.bidtime.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.bidtime.session.bean.SessionUserBase;
import org.bidtime.session.utils.RequestSessionUtils;

/**
 * @author Administrator
 * 
 */
public class UserSessionCache {
	
	private static final String USER_SESSION_INFO = "USER_SESSION_INFO";
	private static final String DOUBLE_USER_ONLINE = "DOUBLE_USER_ONLINE";

	private boolean singleLogin;
	
	public boolean getSingleLogin() {
		return singleLogin;
	}
	
	private static final Logger logger = Logger
			.getLogger(UserSessionCache.class);
	
	public UserSessionCache() {
		super();
	}
	
	public UserSessionCache(boolean singleLogin) {
		this.singleLogin = singleLogin;;
	}
	
	public String getSessionId(HttpServletRequest req, boolean newSession) {
		return RequestSessionUtils.getSessionId(req, newSession);
	}

	public String getSessionId(HttpServletRequest req) {
		return RequestSessionUtils.getSessionId(req);
	}
	
	public SessionLoginState getSessionLoginState(HttpServletRequest request) {
		return getSessionLoginState(request, false);
	}
	
	public SessionLoginState getSessionLoginState(HttpServletRequest request, boolean force) {
		// String sessionId = getSessionId(request, force);
		HttpSession session = request.getSession();
	    // 0:未登陆, 1:正常登陆, 2:被其它用户踢, 3: 没有权限
	    SessionLoginState sessionLogin = user_getSessionLoginState(session);
	    return sessionLogin;
	}
	
//	public SessionLoginState getSessionTokenState(HttpServletRequest request) {
//		return getSessionTokenState(request, false);
//	}
//
//	public SessionLoginState getSessionTokenState(HttpServletRequest request, boolean force) {
//		// 先从 sessionId 中取，是否有存储的
//		SessionLoginState ss = getSessionLoginState(request, force);
//		int nLoginState = 0;
//	    if (ss != null) {
//	    	nLoginState = ss.getLoginState();
//	    }
//	    if (nLoginState == 0) {
//			String token = RequestSessionUtils.getToken(request);
//			if (token != null && !token.isEmpty()) {
////				String sessionId = (String)this.onlineCache.get(token);
////				if (sessionId != null) {
////					sessionCache.replace(token, sessionId);
////				}
////				SessionLoginState sessionLogin = user_getSessionLoginState(sessionId);
////				if (sessionLogin == null) {
////					sessionLogin = new SessionLoginState(null, 4);	// 4:token 重新登陆
////				}
////				return sessionLogin;
//				return null;
//			} else {
//				return null;
//			}
//		}
//	    return ss;
//	}
	
//	public void setTokenToSession(HttpServletRequest request, HttpServletResponse res) {
//		String token = UUID.randomUUID().toString();
//		setTokenToSession(token, request, res);
//	}
//	
//	public void setTokenToSession(String token, HttpServletRequest request, HttpServletResponse res) {
//		String sessionId = getSessionId(request, true);
//		// this.sessionCache.set(token, sessionId);
//		// RequestSessionUtils.setToken(res, token, sessionCache.getDefaultTm());
//	}
	
	// httpSession_removeAttr
//	public void httpSession_destroyAttr(HttpSession session) {
//		httpSession_destroy(session, true);
//	}

	// request_logout
	public void request_logout(HttpServletRequest request) {
		httpSession_destroy(request.getSession(false), true);
	}
	
	public boolean request_login(HttpServletRequest request,
			SessionUserBase u) {
		return request_login(request, u, true);
	}
	
	// request_login
	private boolean request_login(HttpServletRequest request,
			SessionUserBase u, boolean newSession) {
		// 强制将当前用户退出登陆
		httpSession_destroy(request.getSession(newSession), true);
		// request login
		HttpSession session = request.getSession(false);
		if (session == null && newSession) {
			session = request.getSession(true);
		}
		return userToSession_DoubleOnLine(session, u);
	}

	// re_login
	public boolean re_login(HttpServletRequest request) {
		SessionUserBase u = getUserOfRequest(request);
		return request_login(request, u, false);
	}

	// re_login
	public boolean re_login(HttpServletRequest request, SessionUserBase u) {
		return request_login(request, u, false);
	}

	// getUserOfRequest
	public SessionUserBase getUserOfRequest(HttpServletRequest request) {
		return getUserOfRequest(request, false);
	}

	// getUserOfRequest
	public SessionUserBase getUserOfRequest(HttpServletRequest request, boolean newSession) {
		HttpSession session = request.getSession(newSession);
		SessionUserBase u = (SessionUserBase)session.getAttribute(session.getId());
		return u;
	}
	
	// user_getUserIdOfRequest
//	public String user_getUserIdOfRequest(HttpServletRequest request) {
//		String sessionId = getSessionId(request);
//		return user_getUserIdOfSessionId(sessionId);
//	}

	// user_getUserIdOfSessionId
//	public String user_getUserIdOfSessionId(String sessionId) {
//		SessionUserBase u = user_getUserOfSessionId(sessionId);
//		if (u != null) {
//			return u.getId();
//		} else {
//			return null;
//		}
//	}
	
	// user_getUserNameOfRequest
//	public String user_getUserNameOfRequest(HttpServletRequest request) {
//		String sessionId = getSessionId(request);
//		return user_getUserNameOfSessionId(sessionId);
//	}
	
	// user_getUserNameOfSessionId
//	public String user_getUserNameOfSessionId(String sessionId) {
//		SessionUserBase u = user_getUserOfSessionId(sessionId);
//		if (u != null) {
//			return u.getName();
//		} else {
//			return null;
//		}
//	}

//	// user_getUserOfHttpSession
//	public SessionUserBase user_getUserOfSessionId(String sessionId) {
//		Object obj = user_get(sessionId);
//		if (obj != null) {
//			return (SessionUserBase)obj;
//		} else {
//			return null;
//		}
//	}
//
//	// user_isLoginOfHttpSession
//	public boolean user_isLoginOfSessionId(String sessionId) {
//		SessionUserBase u = user_getUserOfSessionId(sessionId);
//		if (u != null) {
//			return true;
//		} else {
//			return false;
//		}
//	}

	// isUserLogin
	public boolean isUserLogined(HttpSession session) {
		Object o = session.getAttribute(session.getId());
		return ( o != null ) ? true : false;
	}
	
	// isUserLogin
	public boolean isUserAlive(HttpSession session) {
		Object o = session.getAttribute(session.getId() + DOUBLE_USER_ONLINE);
		return ( o != null ) ? true : false;
	}

	////////////////////////////////////////////////////////////////////////////////////
	/*
	protected static boolean setDoubleUserOneLine(HttpSession session, SessionUserBase u) {
		// 判断是否当前用户,是否已经登陆,如果登陆,则踢出
		return null;
	} */
	
	protected void httpSession_destroy(HttpSession session,
			boolean bInvalid) {
		if (session != null) {
			String sessionId = session.getId();
			try {
				if (bInvalid) {
					session.invalidate();
				} else {
					session.removeAttribute(sessionId);
					session.removeAttribute(sessionId + DOUBLE_USER_ONLINE);
				}
			} catch (Exception e) {
				logger.error(e);
			}

		}
	}
	
	public SessionLoginState user_getSessionLoginState(HttpSession session) {
		if (session == null) {
			return null;
		}
		String sessionId = session.getId();
		if (sessionId != null) {
			int nLoginState = 0;
			//SessionUserBase u = user_getUserOfSessionId(sessionId);
			SessionUserBase u = (SessionUserBase)session.getAttribute(sessionId);
			if (u != null) {
				Integer nResult = (Integer) session.getAttribute(DOUBLE_USER_ONLINE);
				if (nResult != null) {
					nLoginState = 2;
				} else {
					//replace sessionId's user memcache
					//this.sessionCache.replace(sessionId, u);
					nLoginState = 1;
				}
			}
			SessionLoginState sessionBean = new SessionLoginState(u, nLoginState);
			return sessionBean;
		} else {
			return null;
		}
	}
	
	protected boolean userToSession_DoubleOnLine(HttpSession session, SessionUserBase u) {
		if (session != null) {
			//将当前的session,赋值 User
			session.setAttribute(USER_SESSION_INFO, u);
			//清除当前session的Double_User_Online属性
			session.removeAttribute(DOUBLE_USER_ONLINE);
			// 判断是否当前用户,是否已经登陆,如果登陆,则踢出
			//boolean bReturn = setDoubleUserOneLine(session, u);
			HttpSession session_old = UserSessionListener.addUser(
						u.getId(), session);
			if (session_old != null && session_old != session) {
				// httpSession_removeAttr(session_old);
				session_old.setAttribute(DOUBLE_USER_ONLINE, 
						new Integer(1));
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	public Object user_get(HttpServletRequest req, String key) {
		return user_get(req, key, false);
	}
	
	public Object user_get(HttpServletRequest req, String key, boolean delete) {
//		String sessionId = getSessionId(req, false);
//		return user_get(sessionId, key, delete);
		HttpSession session = req.getSession(false);
		String kkey = session.getId() + key;
		Object o = session.getAttribute(kkey);
		if (delete) {
			session.removeAttribute(kkey);
		}
		return o;
	}	
	
//	public Object user_get(String sessionId) {
//		if (sessionId != null) {
//			return this.sessionCache.get(sessionId);
//		} else {
//			return null;
//		}
//	}
	
//	public Object user_get(String sessionId, String ext, boolean delete) {
//		if (sessionId != null) {
//			return this.sessionCache.get(sessionId, ext, delete);
//		} else {
//			return null;
//		}
//	}
	
	public void user_set(HttpServletRequest req, String key, Object o) {
		user_set(req, key, o, true);
	}
	
	public void user_set(HttpServletRequest req, String key, Object o, boolean newSession) {
		//String sessionId = getSessionId(req, newSession);
		//user_set(sessionId, key, o);
		HttpSession session = req.getSession(newSession);
		session.setAttribute(session.getId() + key, o);
	}
	
//	public void user_set(String sessionId, String ext, Object o) {
//		if (sessionId != null) {
//			this.sessionCache.set(sessionId, ext, o);
//		}
//	}

}