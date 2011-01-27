package org.jpos.transaction;

import java.io.Serializable;

public interface GroupSelector extends TransactionParticipant {

	String select(long id, Serializable context);

}
