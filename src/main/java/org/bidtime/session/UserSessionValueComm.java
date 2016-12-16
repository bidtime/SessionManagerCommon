package org.bidtime.session;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

public class UserSessionValueComm {

	private static final Logger logger = Logger
			.getLogger(UserSessionValueComm.class);

	private static final String VALID_MP_NO = "USER_MP_NO";
	private static final String VALID_NAME_NO = "USER_NAME_NO";
	private static final String VALID_EMAIL_NO = "USER_EMAIl_NO";

	private static Object session_getAttributeObject(HttpSession session,
			String key) {
		try {
			if (session != null) {
				return session.getAttribute(key);
			} else {
				return null;
			}
		} catch (Exception e) {
			logger.error(e);
			return null;
		}
	}

	private static void session_setAttributeObject(HttpSession session,
			String key, Object o) {
		try {
			if (session != null) {
				session.setAttribute(key, o);
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private static void session_removeAttributeObject(HttpSession session,
			String key) {
		try {
			if (session != null) {
				session.removeAttribute(key);
			}
		} catch (Exception e) {
			logger.error(e);
		}
	}

	private static String getValidNo(HttpServletRequest request,
			String validName) {
		HttpSession session = request.getSession(false);
		if (session != null) {
			return (String) session_getAttributeObject(session, validName);
		} else {
			return null;
		}
	}

	public static boolean verifyValidNo_Mp(String account, String validNo,
			HttpServletRequest request) {
		String s = getValidNo(request, VALID_MP_NO);
		return StringUtils.equalsIgnoreCase(s, validNo + account);
	}

	public static boolean verifyValidNo_Email(String account, String validNo,
			HttpServletRequest request) {
		String s = getValidNo(request, VALID_EMAIL_NO);
		return StringUtils.equalsIgnoreCase(s, validNo + account);
	}

	public static boolean verifyValidNo_Name(String validNo,
			HttpServletRequest request) {
		String s = getValidNo(request, VALID_NAME_NO);
		return StringUtils.equals(s, validNo);
	}

	public static void setValidNo_Mp(String account, String validNo,
			HttpServletRequest request) {
		HttpSession session = UserSessionCommon
				.getHttpSessionOfRequest(request, true);
		session_setAttributeObject(session, VALID_MP_NO, validNo + account);
	}

	public static void setValidNo_Email(String account, String validNo,
			HttpServletRequest request) {
		HttpSession session = UserSessionCommon
				.getHttpSessionOfRequest(request, true);
		session_setAttributeObject(session, VALID_EMAIL_NO, validNo + account);
	}

	public static void setValidNo_Name(String validNo,
			HttpServletRequest request) {
		HttpSession session = UserSessionCommon
				.getHttpSessionOfRequest(request, true);
		session_setAttributeObject(session, VALID_NAME_NO, validNo);
	}

	public static boolean removeValidNo_Mp(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		boolean b = false;
		session_removeAttributeObject(session, VALID_MP_NO);
		return b;
	}

	public static boolean removeValidNo_Email(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		boolean b = false;
		session_removeAttributeObject(session, VALID_EMAIL_NO);
		return b;
	}

	public static boolean removeValidNo_Name(HttpServletRequest request) {
		HttpSession session = request.getSession(false);
		boolean b = false;
		session_removeAttributeObject(session, VALID_NAME_NO);
		return b;
	}

}