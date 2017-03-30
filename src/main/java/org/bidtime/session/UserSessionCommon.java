/**
 * 
 */
package org.bidtime.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.bidtime.session.bean.SessionUserBase;

/**
 * @author Administrator
 * 
 */
public class UserSessionCommon implements IUserSessionBase {
	
	private static final Logger logger = Logger
			.getLogger(UserSessionCommon.class);
	
	private static final String USER_SESSION_INFO = "USER_SESSION_INFO";
	private static final String DOUBLE_USER_ONLINE = "DOUBLE_USER_ONLINE";

	// httpSession_removeAttr
	protected static void session_destroy(HttpSession session) {
		session_destroy(session, true);
	}

	private static String getSessionId(HttpServletRequest request, boolean newSession) {
		HttpSession session = request.getSession(newSession);
		if (session != null) {
			return session.getId();
		} else {
			return null;
		}
	}
	
	public static String getSessionId(HttpServletRequest request) {
		return getSessionId(request, true);
	}

	// request_logout
	public void logout(HttpServletRequest request) {
		HttpSession session = request.getSession();
		session_destroy(session, true);
	}
	
	public boolean login(HttpServletRequest request, SessionUserBase u) {
		return login(request, u, true);
	}
	
	// request_login
	private boolean login(HttpServletRequest request, SessionUserBase u, boolean newSession) {
		HttpSession session = request.getSession(newSession);
		return user2DoubleOnLine(session, u);
	}

	// re_login
	public boolean relogin(HttpServletRequest request) {
		SessionUserBase u = getUser(request);
		return login(request, u, false);
	}

	// re_login
	public boolean relogin(HttpServletRequest request, SessionUserBase u) {
		return login(request, u, false);
	}

	// user_isLoginOfHttpSession
//	private boolean user_isLoginOfHttpSession(HttpSession session) {
//		SessionUserBase u = getUser(session);
//		if (u != null) {
//			return true;
//		} else {
//			return false;
//		}
//	}

	////////////////////////////////////////////////////////////////////////////////////

	// httpSession_destroy
	protected static void session_destroy(HttpSession session, boolean bInvalid) {
		if (session != null) {
			try {
				if (bInvalid) {
					session.invalidate();
				} else {
					session.removeAttribute(DOUBLE_USER_ONLINE);
					session.setAttribute(DOUBLE_USER_ONLINE, null);
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	// isDoubleOnLine
	protected boolean isDoubleOnLine(HttpSession session) {
		Object value = session.getAttribute(DOUBLE_USER_ONLINE);
		if (value != null) {
			return true;
		} else {
			return false;
		}
	}

	public SessionLoginState getSessionLoginState(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if (session != null) {
			int loginState = StateConst.NOT_LOGIN;	//未登陆
			SessionUserBase u = getUser(session);
			if (u != null) {
				if (isDoubleOnLine(session)) {
					loginState = StateConst.ANOTHER_LOGIN;	//登陆后被踢
				} else {
					loginState = StateConst.LOGGED_IN;	//正常登陆
				}
			}
			SessionLoginState sessionLogin = new SessionLoginState(u, loginState);
			return sessionLogin;
		} else {
			return null;
		}
	}

	protected boolean user2DoubleOnLine(HttpSession session, SessionUserBase u) {
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
				session_old.setAttribute(DOUBLE_USER_ONLINE, new Boolean(true));
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	// getObject
	protected static Object getObject(HttpSession session) {
		if (session != null) {
			return session.getAttribute(USER_SESSION_INFO);				
		} else {
			return null;
		}
	}
	
	// getUser
	protected static SessionUserBase getUser(HttpSession session) {
		Object obj = getObject(session);
		if (obj != null) {
			return (SessionUserBase)obj;
		} else {
			return null;
		}
	}

	// get user
	public SessionUserBase getUser(HttpServletRequest request, boolean newSession) {
		return getUser(request.getSession(newSession));
	}

	// getUser
	public SessionUserBase getUser(HttpServletRequest request) {
		return getUser(request, false);
	}

	// user_getUserIdOfHttpSession
	protected static String getUserId(HttpSession session) {
		return getUserId(session, null);
	}
	
	protected static String getUserId(HttpSession session, String defValue) {
		SessionUserBase u = getUser(session);
		if (u != null) {
			return u.getId();
		} else {
			return defValue;
		}
	}
	public Object get(HttpServletRequest req, String ext) {
		return get(req, ext, false);
	}
	
	public Object get(HttpServletRequest req, String ext, boolean delete) {
		HttpSession session = req.getSession();
		return get(session, ext, delete);
	}
	
//	private Object get(HttpSession session, String key) {
//		if (session != null) {
//			Object object = session.getAttribute(key);
//			return object;
//		} else {
//			return null;
//		}
//	}
	
	private Object get(HttpSession session, String ext, boolean delete) {
		if (session != null) {
			String key = session.getId()+ext;
			Object object = session.getAttribute(key);
			if (delete) {
				session.removeAttribute(key);
			}
			return object;
		} else {
			return null;
		}
	}
	
	// set ext
	
	public void set(HttpServletRequest req, String ext, Object o) {
		set(req, ext, o, true);
	}
	
	public void set(HttpServletRequest req, String ext, Object value, boolean newSession) {
		HttpSession session = req.getSession(newSession);
		set(session, ext, value);
	}
	
	private void set(HttpSession session, String ext, Object value) {
		if (session != null) {
			session.setAttribute(session.getId()+ext, value);
		}
	}

//	protected static String user_getNameOfHttpSession(HttpSession session, String defValue) {
//		SessionUserBase u = user_getUserOfHttpSession(session);
//		if (u != null) {
//			return u.getName();
//		} else {
//			return defValue;
//		}
//	}

}