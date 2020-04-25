package com.hyjf.admin.manager.config.instconfig;

import com.hyjf.mybatis.model.auto.HjhInstConfig;

public class HjhInstConfigWrap extends HjhInstConfig{
	
	private static final long serialVersionUID = 1L;
	
	private String capitalAvailable;

	public String getCapitalAvailable() {
		return capitalAvailable;
	}

	public void setCapitalAvailable(String capitalAvailable) {
		this.capitalAvailable = capitalAvailable;
	}
	
	
}
