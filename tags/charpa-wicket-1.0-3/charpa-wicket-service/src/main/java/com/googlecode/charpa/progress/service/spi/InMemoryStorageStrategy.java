package com.googlecode.charpa.progress.service.spi;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.googlecode.charpa.progress.service.LogMessage;
import com.googlecode.charpa.progress.service.ProgressId;
import com.googlecode.charpa.progress.service.ProgressState;
import com.googlecode.charpa.progress.service.impl.ProgressInfo;

public class InMemoryStorageStrategy implements IProgressStorageStrategy {
	
	private Map<ProgressId, ProgressInfo> progresses = Collections.synchronizedMap(new HashMap<ProgressId, ProgressInfo>());

	public void addErrorMessage(ProgressId id, String message) {
		ProgressInfo info = findProgress(id);
        info.error(message);
	}

	public void addInfoMessage(ProgressId id, String message) {
		ProgressInfo info = findProgress(id);
        info.info(message);
	}

	public void cancelProgress(ProgressId id) {
		ProgressInfo info = findProgress(id);
        info.setState(ProgressState.CANCELLED);
        info.setEndedTime(new Date());
	}

	public void changeProgressText(ProgressId id, String text) {
		findProgress(id).setProgressText(text);
	}

	public ProgressInfo findProgress(ProgressId id) {
		return progresses.get(id);
	}

	public void finishProgress(ProgressId id) {
		ProgressInfo info = findProgress(id);
        info.setEndedTime(new Date());
        info.setState(ProgressState.FINISHED);
	}

	public void incrementProgressValue(ProgressId id) {
		findProgress(id).incrementValue();
	}

	public boolean isCancelled(ProgressId id) {
		ProgressInfo info = findProgress(id);
        return info.getState() == ProgressState.CANCELLED;
	}

	public Collection<ProgressInfo> listProgresses() {
		return progresses.values();
	}

	public void progressFailed(ProgressId id, Exception exception) {
		ProgressInfo info = findProgress(id);
        info.setState(ProgressState.FAILED);
        info.setEndedTime(new Date());
        info.setProgressText(exception.getMessage());
	}

	public void createProgress(ProgressId id, ProgressInfo info) {
		progresses.put(id, info);
	}

	public void changeProgressName(ProgressId id, String name) {
		ProgressInfo info = findProgress(id);
        info.setName(name);
	}

	public void startProgress(ProgressId id, String progressName, int maxValue) {
		ProgressInfo info = findProgress(id);
        info.setName(progressName);
        info.setMax(maxValue);
        info.setProgressText(""); // removes "Starting ..." text
        info.setStartedTime(new Date());
        info.setState(ProgressState.RUNNING);
	}

	public List<LogMessage> listLatestLogMessages(ProgressId id, int limit) {
        LinkedList<LogMessage> list = new LinkedList<LogMessage>();
        int size = findProgress(id).getLogMessages().size();
        for(int i=0; i < limit && i < size; i++) {
            list.add(0, findProgress(id).getLogMessages().get(size - i - 1));
        }
        return list;
	}

}