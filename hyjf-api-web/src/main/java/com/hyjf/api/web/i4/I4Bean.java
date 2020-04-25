package com.hyjf.api.web.i4;

import java.io.Serializable;

public class I4Bean implements Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2569482809922162227L;

	/**
	 * App 的 iTunesID
	 */
	private String appid;
	
	/**
	 * 用户设备标识，ios7.0以上系统
	 */
	private String idfa;
	/**
	 * WIFI MAC 地址
	 */
	private String mac;
	/**
	 * 用户设备标识可选
	 */
	private String openudid;
	/**
	 * 用户设备ios系统版本
	 */
	private String os;
	/**
	 * 激活回调地址
	 */
	private String callback;
	

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

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public String getOpenudid() {
		return openudid;
	}

	public void setOpenudid(String openudid) {
		this.openudid = openudid;
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		this.os = os;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}
