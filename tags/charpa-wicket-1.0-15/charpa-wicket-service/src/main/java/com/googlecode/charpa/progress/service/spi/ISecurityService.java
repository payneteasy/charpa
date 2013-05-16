package com.googlecode.charpa.progress.service.spi;

import com.googlecode.charpa.progress.service.ProgressId;

/**
 * Provides security services used to secure progresses.
 */
public interface ISecurityService {
	/**
	 * Returns security info represented as a string for the current user.
	 * 
	 * @return security info
	 */
	String getCurrentSecurityInfo();
	
	/**
	 * Returns true if user with the given security info sees progress, false
	 * otherwise.
	 * 
	 * @param progressId			ID of the progress in question
	 * @param currentSecurityInfo	security info of the current user
	 * @param progressSecurityInfo	security info of the progress (i.e.
	 * security info of the user who created the progress)
	 * @return true if user sees the progress
	 */
	boolean userSeesProgress(ProgressId progressId, String currentSecurityInfo,
			String progressSecurityInfo);
}
