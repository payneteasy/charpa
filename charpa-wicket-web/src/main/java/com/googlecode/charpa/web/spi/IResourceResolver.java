package com.googlecode.charpa.web.spi;

import java.io.Serializable;

public interface IResourceResolver extends Serializable {
	String resolve(String key, String defaultValue);
}
