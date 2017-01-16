package org.bidtime.session.bean;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class SessionUserBase implements Serializable {
	
	public abstract String getId();
	
	public abstract String getName();
	
}
