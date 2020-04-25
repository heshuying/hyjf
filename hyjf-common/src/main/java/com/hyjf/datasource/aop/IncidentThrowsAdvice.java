package com.hyjf.datasource.aop;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ThrowsAdvice;
import org.springframework.stereotype.Component;

import com.hyjf.datasource.DatabaseContextHolder;


@Component("incidentThrowsAdvice")
public class IncidentThrowsAdvice implements ThrowsAdvice {
	
	private static Logger _log = LoggerFactory.getLogger(IncidentThrowsAdvice.class);
	
	public void afterThrowing(Exception e) throws Throwable {
		DatabaseContextHolder.clearCustomerType();
		_log.error(e.getMessage());
	}
	
	

}
