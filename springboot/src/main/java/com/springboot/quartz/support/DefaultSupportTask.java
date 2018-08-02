package com.springboot.quartz.support;

import java.io.Serializable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DefaultSupportTask<PK extends Serializable> {
	
	protected final Log logger = LogFactory.getLog(getClass());
	
}
