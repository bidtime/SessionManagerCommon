package org.bidtime.session.bean;

import java.io.Serializable;

public abstract class SessionUserBase implements Serializable {
	private static final long serialVersionUID = 1L;

	public abstract String getId();
	public abstract String getName();
}
