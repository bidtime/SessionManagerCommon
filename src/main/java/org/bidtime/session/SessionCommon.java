package org.bidtime.session;

import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.bidtime.session.bean.SessionUserBase;

public class SessionCommon {
	
	private static final Logger logger = Logger.getLogger(SessionCommon.class);

	private static final String USER_SESSION_INFO = "USER_SESSION_INFO";
	private static final String DOUBLE_USER_ONLINE = "DOUBLE_USER_ONLINE";

	// session_destroy
	protected static void session_destroy(HttpSession session) {
		session_destroy(session, true);
	}

	// session_destroy
	private static void session_destroy(HttpSession session, boolean bInvalid) {
		if (session != null) {
			try {
				if (bInvalid) {
					session.invalidate();
				} else {
					session.setAttribute(DOUBLE_USER_ONLINE, null);
					session.removeAttribute(DOUBLE_USER_ONLINE);
				}
			} catch (Exception e) {
				logger.error(e);
			}
		}
	}

	// isDoubleOnLine
	private boolean isDoubleOnLine(HttpSession session) {
		Object value = session.getAttribute(DOUBLE_USER_ONLINE);
		if (value != null) {
			return true;
		} else {
			return false;
		}
	}

	// user2DoubleOnLine
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
	
	// getSessionLoginState
	protected SessionLoginState getSessionLoginState(HttpSession session) {
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
			return new SessionLoginState(u, loginState);
		} else {
			return null;
		}
	}
	
	// get
	protected static Object get(HttpSession session) {
		return get(session, USER_SESSION_INFO);
	}
	
	// get
	protected static Object get(HttpSession session, String key) {
		return get(session, key, false);
	}
	
	// get
	protected static Object get(HttpSession session, String key, boolean delete) {
		if (session != null) {
			Object o = session.getAttribute(key);
			if (delete) {
				session.removeAttribute(key);
			}
			return o;
		} else {
			return null;
		}
	}
	
	// getUser
	protected static SessionUserBase getUser(HttpSession session) {
		Object obj = get(session);
		if (obj != null) {
			return (SessionUserBase)obj;
		} else {
			return null;
		}
	}
	
	// getUserId
	protected static String getUserIdString(HttpSession session, String defValue) {
		SessionUserBase u = getUser(session);
		if (u != null) {
			return u.getId();
		} else {
			return defValue;
		}
	}

	// getUserId
	protected static String getUserIdString(HttpSession session) {
		return getUserIdString(session, null);
	}

	// getUserName
	public static String getUserName(HttpSession session, String defValue) {
		SessionUserBase u = getUser(session);
		if (u != null) {
			return u.getName();
		} else {
			return defValue;
		}
	}
	
	// set
	protected void set(HttpSession session, String ext, Object value) {
		if (session != null) {
			session.setAttribute(ext, value);
		}
	}
	
}
