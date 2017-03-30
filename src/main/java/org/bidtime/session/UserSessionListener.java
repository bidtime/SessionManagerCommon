package org.bidtime.session;

import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.log4j.Logger;

public class UserSessionListener implements HttpSessionListener {

	private static final Logger logger = Logger
			.getLogger(UserSessionListener.class);

	private static ConcurrentHashMap<String, HttpSession> user_map = new ConcurrentHashMap<String, HttpSession>();

	public static HttpSession addUser(String sId, HttpSession u) {
		return user_map.put(sId, u);
		// HttpSession session_old = user_map.put(String.valueOf(sId),u);
		// if (hs_old!=null && u!=null &&
		// !hs_old.getId().equalsIgnoreCase(u.getId())) {
		// if (session_old!=null && session_old != u) {
		// UserSessionInfo.httpSession_logout(session_old);
		// return session_old;
		// } else {
		// return session_old;
		// }
	}
	
	public void setEventNumSessions(HttpSessionEvent hse, int n) {
		// OnlineCounter.raise(hse.getSession());
		ServletContext ctx = hse.getSession( ).getServletContext( );
        Integer numSessions = (Integer) ctx.getAttribute("numSessions");
        if (numSessions == null) {
            numSessions = new Integer(n);
        }
        else {
            int count = numSessions.intValue();
            numSessions = new Integer(count + n);
        }
        ctx.setAttribute("numSessions", numSessions);
        logger.info("numSessions:"+numSessions);
	}

	public void sessionCreated(HttpSessionEvent hse) {
		// OnlineCounter.raise(hse.getSession());
		setEventNumSessions(hse, 1);
	}

	public void sessionDestroyed(HttpSessionEvent hse) {
		HttpSession session = hse.getSession();
		sessionRemove(session);
		setEventNumSessions(hse, -1);
	}

	private static void sessionRemove(HttpSession session) {
		if (session != null) {
			String userId = UserSessionCommon.getUserId(session, null);
			if (userId != null) {
				try {
					UserSessionCommon.session_destroy(session);
				} finally {
					user_map.remove(userId);
				}
			}
		}
	}

	/*
	 * 　再然后，把这个HttpSessionListener实现类注册到网站应用中，也就是在网站应用的web.xml中加入如下内容：
	 * 
	 * <web-app> …… <listener> <listener-class>
	 * com.eb.session.OnlineCounterListener </listener-class> </listener> ……
	 * </web-app>
	 */
}
