package org.jpos.transaction;

import java.io.Serializable;

public interface TransactionParticipant {

	public static final int READONLY = 0;

	public static final int PREPARED = 0;

	public static final int NO_JOIN = 0;
	
	int prepare(long id, Serializable context);

	void abort(long id, Serializable context);

	void commit(long id, Serializable context);

}
