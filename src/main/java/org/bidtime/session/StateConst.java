package org.bidtime.session;

public class StateConst {
	
	// 0:未登陆, 1:正常登陆, 2:被其它用户踢, 3: 没有权限，4: token需要后台登陆，获取用户信息
	public static final int NOT_LOGIN = 0;
	public static final int LOGGED_IN = 1;
	public static final int ANOTHER_LOGIN = 2;
	public static final int NO_PERMISSION = 3;
	public static final int TOKEN_RELOGIN = 4;
	
	public static final int STATE_NOT_LOGIN = 1000;
	public static final int STATE_ANOTHER_LOGIN = 1001;
	
}
