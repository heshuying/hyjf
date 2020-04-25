package com.hyjf.api.web.idfa;

import java.io.Serializable;

public class IdfaBean implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2569482809922162226L;

	/**
	 * App 的 iTunesID
	 */
	private String appid;
	
	/**
	 * 需要查询的设备 idfa
	 */
	private String idfa;

    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getIdfa() {
        return idfa;
    }

    public void setIdfa(String idfa) {
        this.idfa = idfa;
    }
}
