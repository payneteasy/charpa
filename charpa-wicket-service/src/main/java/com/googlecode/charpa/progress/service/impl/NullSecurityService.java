package com.googlecode.charpa.progress.service.impl;

import com.googlecode.charpa.progress.service.ProgressId;
import com.googlecode.charpa.progress.service.spi.ISecurityService;

/**
 * Dumb implementation which allows any progress to be seen by any user.
 */
public class NullSecurityService implements ISecurityService {

	public String getCurrentSecurityInfo() {
		return null;
	}

	public boolean userSeesProgress(ProgressId progressId,
			String currentSecurityInfo, String progressSecurityInfo) {
		return true;
	}

}
