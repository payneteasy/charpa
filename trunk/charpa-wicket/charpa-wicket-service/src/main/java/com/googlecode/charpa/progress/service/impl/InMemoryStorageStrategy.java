package com.googlecode.charpa.progress.service.impl;

import java.util.*;
import java.util.Map.Entry;

import com.googlecode.charpa.progress.service.LogMessage;
import com.googlecode.charpa.progress.service.ProgressId;
import com.googlecode.charpa.progress.service.ProgressState;
import com.googlecode.charpa.progress.service.exception.ProgressNotFoundException;
import com.googlecode.charpa.progress.service.impl.ProgressServiceImpl.ISecurityServiceFactory;
import com.googlecode.charpa.progress.service.spi.IProgressStorageStrategy;
import com.googlecode.charpa.progress.service.spi.ISecurityService;

/**
 * Stores progresses in memory.
 * Security is checked for the following methods only:
 * {@link #findProgress(ProgressId)}
 * {@link #cancelProgress(ProgressId)}
 * {@link #listLatestLogMessages(ProgressId, int)}
 * {@link #listProgresses(String)}
 */
public class InMemoryStorageStrategy implements IProgressStorageStrategy {
	
	private ISecurityServiceFactory securityServiceFactory;
	
	public InMemoryStorageStrategy(final ISecurityService securityService) {
		this(new ISecurityServiceFactory() {
			public ISecurityService getSecurityService() {
				return securityService;
			}
		});
	}
	
	InMemoryStorageStrategy(ISecurityServiceFactory securityServiceFactory) {
		super();
		this.securityServiceFactory = securityServiceFactory;
	}

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
		ProgressInfo info = findProgressCheckingSecurity(id);
		if (info == null) {
			throw new ProgressNotFoundException(id.toString());
		}
        info.setState(ProgressState.CANCELLED);
        info.setEndedTime(new Date());
	}

	public void changeProgressText(ProgressId id, String text) {
		findProgress(id).setProgressText(text);
	}

	public ProgressInfo findProgress(ProgressId id) {
		return findProgressCheckingSecurity(id);
	}

	public void finishProgress(ProgressId id) {
		ProgressInfo info = findProgress(id);
        info.setEndedTime(new Date());
        info.setState(ProgressState.FINISHED);
	}

	public void incrementProgressValue(ProgressId id, int delta) {
		findProgress(id).incrementValue(delta);
	}

	public boolean isCancelled(ProgressId id) {
		ProgressInfo info = findProgress(id);
		if (info == null) {
			throw new IllegalStateException("Progress with id " + id
					+ " was not found");
		}
		return info.getState() == ProgressState.CANCELLED;
	}

	public boolean isRunning(ProgressId progressId) {
		ProgressInfo info = findProgress(progressId);
		if (info == null) {
			throw new IllegalStateException("Progress with id " + progressId
					+ " was not found");
		}
		return info.getState() == ProgressState.RUNNING;
	}
	
	public boolean isStarted(ProgressId progressId) {
		ProgressInfo info = findProgress(progressId);
		if (info == null) {
			throw new IllegalStateException("Progress with id " + progressId
					+ " was not found");
		}
		return info.getState() != null && info.getState() != ProgressState.PENDING;
	}

	public Collection<ProgressInfo> listProgresses(String name) {
		Collection<ProgressInfo> coll;
		if (name == null) {
			coll = progresses.values();
		} else {
			coll = findProgressesByName(name);
		}
		return filterNotSeenProgresses(coll);
	}

	private Collection<ProgressInfo> findProgressesByName(String name) {
		Collection<ProgressInfo> result = new LinkedList<ProgressInfo>();
		for (ProgressInfo progress : progresses.values()) {
			if (name.equals(progress.getName())) {
				result.add(progress);
			}
		}
		return result;
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
		ProgressInfo progress = findProgressCheckingSecurity(id);
		if (progress == null) {
			throw new ProgressNotFoundException(id.toString());
		}
        synchronized (progress) {
            List<LogMessage> messages = progress.getLogMessages();
            int messagesSize = messages.size();
            int resultSize = Math.min(limit, messagesSize);
            return new ArrayList<LogMessage>(messages.subList(messagesSize - resultSize, messagesSize));
        }
	}

	public void deleteStaleProgresses(Date olderThan) {
		Iterator<Entry<ProgressId, ProgressInfo>> it = progresses.entrySet().iterator();
		while (it.hasNext()) {
			Entry<ProgressId, ProgressInfo> entry = it.next();
			ProgressInfo info = entry.getValue();
			ProgressState state = info.getState();
			if (state == ProgressState.CANCELLED || state == ProgressState.FAILED
					|| state == ProgressState.FINISHED) {
				if (info.getEndedTime().before(olderThan)) {
					it.remove();
				}
			} else if (state == ProgressState.PENDING) {
				if (info.getCreatedTime().before(olderThan)) {
					it.remove();
				}
			}
		}
	}
	
	protected ProgressInfo findProgressCheckingSecurity(ProgressId id) {
		ProgressInfo progress = progresses.get(id);
		if (progress != null) {
			if (!securityServiceFactory.getSecurityService().userSeesProgress(id,
					securityServiceFactory.getSecurityService().getCurrentSecurityInfo(),
					progress.getSecurityInfo())) {
				progress = null;
			}
		}
		return progress;
	}
	
	protected Collection<ProgressInfo> filterNotSeenProgresses(Collection<ProgressInfo> progresses) {
		String currentSecurityInfo = securityServiceFactory.getSecurityService().getCurrentSecurityInfo();
		List<ProgressInfo> result = new LinkedList<ProgressInfo>();
		for (ProgressInfo progress : progresses) {
			if (securityServiceFactory.getSecurityService().userSeesProgress(progress.getId(),
					currentSecurityInfo, progress.getSecurityInfo())) {
				result.add(progress);
			}
		}
		return result;
	}

}
