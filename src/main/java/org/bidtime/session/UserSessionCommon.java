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
public class UserSessionCommon {
	
	private static final Logger logger = Logger
			.getLogger(UserSessionCommon.class);
	
	private static final String USER_SESSION_INFO = "USER_SESSION_INFO";
	private static final String DOUBLE_USER_ONLINE = "DOUBLE_USER_ONLINE";

	// httpSession_removeAttr
	public static void httpSession_destroyAttr(HttpSession session) {
		httpSession_destroy(session, true);
	}
	
	public static HttpSession getSessionOfRequest(HttpServletRequest request, 
			boolean bForce) {
		HttpSession session = request.getSession(false);
		if ((bForce) && (session == null)) {
			session = request.getSession(true);
		}
		return session;
	}

	public static String getSessionId(HttpServletRequest request, boolean newSession) {
		HttpSession session = getHttpSessionOfRequest(request, newSession);
		if (session != null) {
			return session.getId();
		} else {
			return null;
		}
	}
	
	public static String getSessionId(HttpServletRequest request) {
		return getSessionId(request, true);
	}

	public static HttpSession getHttpSessionOfRequest(HttpServletRequest request,
			boolean bForce) {
		HttpSession session = request.getSession(false);
		if (bForce && session == null) {
			session = request.getSession(true);
		}
		return session;
	}

	// request_logout
	public static void request_logout(HttpServletRequest request) {
		httpSession_destroy(request.getSession(false), true);
	}
	
	public static boolean request_login(HttpServletRequest request,
			SessionUserBase u) {
		return request_login(request, u, true);
	}
	
	// request_login
	private static boolean request_login(HttpServletRequest request,
			SessionUserBase u, boolean newSession) {
		HttpSession session = request.getSession(false);
		if (session == null && newSession) {
			session = request.getSession(true);
		}
		return userToSession_DoubleOnLine(session, u);
	}

	// re_login
	public static boolean re_login(HttpServletRequest request) {
		SessionUserBase u = user_getUserOfRequest(request);
		return request_login(request, u, false);
	}

	// re_login
	public static boolean re_login(HttpServletRequest request, SessionUserBase u) {
		return request_login(request, u, false);
	}

	// user_getUserOfRequest
	public static SessionUserBase user_getUserOfRequest(HttpServletRequest request) {
		return user_getUserOfHttpSession(request.getSession(false));
	}
	
	// user_getUserOfHttpSession
	public static SessionUserBase user_getUserOfHttpSession(HttpSession session) {
		Object obj = user_getValueOfHttpSession(session);
		if (obj != null) {
			return (SessionUserBase)obj;
		} else {
			return null;
		}
	}

	// user_isLoginOfHttpSession
	public static boolean user_isLoginOfHttpSession(HttpSession session) {
		SessionUserBase u = user_getUserOfHttpSession(session);
		if (u != null) {
			return true;
		} else {
			return false;
		}
	}

	// user_getUserIdOfHttpSession
	public static String user_getIdOfHttpSession(HttpSession session, String defValue) {
		SessionUserBase u = user_getUserOfHttpSession(session);
		if (u != null) {
			return u.getId();
		} else {
			return defValue;
		}
	}

	public static String user_getNameOfHttpSession(HttpSession session, String defValue) {
		SessionUserBase u = user_getUserOfHttpSession(session);
		if (u != null) {
			return u.getName();
		} else {
			return defValue;
		}
	}

	////////////////////////////////////////////////////////////////////////////////////

	// httpSession_destroy
	protected static void httpSession_destroy(HttpSession session,
			boolean bInvalid) {
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

	// user_isDoubleOnLineOfHttpSession
	protected static boolean user_isDoubleOnLineOfHttpSession(HttpSession session) {
		boolean bDoubleOnLine = false;
		if (session != null) {
			Integer nResult = (Integer) session.getAttribute(
					UserSessionCommon.DOUBLE_USER_ONLINE);
			if (nResult != null) {
				bDoubleOnLine = true;
			}
		}
		return bDoubleOnLine;
	}

	public static SessionLoginState user_getSessionLoginState(HttpServletRequest request) {
		HttpSession session = getSessionOfRequest(request, false);
		if (session != null) {
			int nLoginState = 0;	//未登陆
			SessionUserBase u = UserSessionCommon.user_getUserOfHttpSession(session);
			if (u != null) {
				if (user_isDoubleOnLineOfHttpSession(session)) {
					nLoginState = 2;	//登陆后被踢
				} else {
					nLoginState = 1;	//正常登陆
				}
			}
			SessionLoginState sessionLogin = new SessionLoginState(u, nLoginState);
			return sessionLogin;
		} else {
			return null;
		}
	}

	protected static boolean userToSession_DoubleOnLine(HttpSession session, SessionUserBase u) {
		if (session != null) {
			//将当前的session,赋值 User
			session.setAttribute(UserSessionCommon.USER_SESSION_INFO, u);
			//清除当前session的Double_User_Online属性
			session.removeAttribute(UserSessionCommon.DOUBLE_USER_ONLINE);
			// 判断是否当前用户,是否已经登陆,如果登陆,则踢出
			//boolean bReturn = setDoubleUserOneLine(session, u);
			HttpSession session_old = UserSessionListener.addUser(
						u.getId(), session);
			if (session_old != null && session_old != session) {
				// httpSession_removeAttr(session_old);
				session_old.setAttribute(UserSessionCommon.DOUBLE_USER_ONLINE, 
						new Integer(1));
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
	
	protected static Object user_getValueOfHttpSession(HttpSession session) {
		if (session != null) {
			return session.getAttribute(
						UserSessionCommon.USER_SESSION_INFO);				
		} else {
			return null;
		}
	}

}