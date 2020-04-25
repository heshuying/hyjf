/**
 * Description:用户开户列表前端显示查询所用po
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.mybatis.model.customize.app;

import java.io.Serializable;

/**
 * @author 王坤
 */

public class AppProjectInvestListCustomize  implements Serializable {
	
	/**
	 * 序列化id
	 */
	private static final long serialVersionUID = -4720030760960740262L;
	//用户名
	private String userName;
	//vip等级
    private String vipId;
	//出借金额
	private String account;
	//出借时间
	private String investTime;	
	//活动红包
	private String redbag;
	


	/**
	 * 构造方法
	 */
		
	public String getRedbag() {
		return redbag;
	}

	public void setRedbag(String redbag) {
		this.redbag = redbag;
	}
	public AppProjectInvestListCustomize() {
		super();
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getInvestTime() {
		return investTime;
	}

	public void setInvestTime(String investTime) {
		this.investTime = investTime;
	}

    public String getVipId() {
        return vipId;
    }

    public void setVipId(String vipId) {
        this.vipId = vipId;
    }

}

	