package com.googlecode.charpa.progress.service.spi;

import java.util.Collection;
import java.util.List;

import com.googlecode.charpa.progress.service.LogMessage;
import com.googlecode.charpa.progress.service.ProgressId;
import com.googlecode.charpa.progress.service.impl.ProgressInfo;

public interface IProgressStorageStrategy {
	void createProgress(ProgressId id, ProgressInfo info);
	ProgressInfo findProgress(ProgressId id);
	Collection<ProgressInfo> listProgresses();
	void cancelProgress(ProgressId id);
	void startProgress(ProgressId id, String progressName, int maxValue);
	void changeProgressText(ProgressId id, String text);
	void incrementProgressValue(ProgressId id);
	void finishProgress(ProgressId id);
	boolean isCancelled(ProgressId id);
	void changeProgressName(ProgressId id, String name);
	void progressFailed(ProgressId id, Exception exception);
	void addInfoMessage(ProgressId id, String message);
	void addErrorMessage(ProgressId id, String message);
	List<LogMessage> listLatestLogMessages(ProgressId id, int limit);
}
