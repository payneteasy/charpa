package com.googlecode.charpa.progress.service.spi;

import java.io.Serializable;

public interface IResourceResolver extends Serializable {
	String resolve(String key, String defaultValue);
}
