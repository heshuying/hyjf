/**
 * Description:冻结实体
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年12月16日 上午10:31:07
 * Modification History:
 * Modified by : 
 */

package com.hyjf.app.user.invest;

public class FreezeDefine {

	private boolean freezeFlag;// 冻结标识
	private String freezeTrxId;// 冻结交易id
	/**
	 * freezeFlag
	 * @return the freezeFlag
	 */
	
	public boolean isFreezeFlag() {
		return freezeFlag;
	}
	/**
	 * @param freezeFlag the freezeFlag to set
	 */
	
	public void setFreezeFlag(boolean freezeFlag) {
		this.freezeFlag = freezeFlag;
	}
	/**
	 * freezeTrxId
	 * @return the freezeTrxId
	 */
	
	public String getFreezeTrxId() {
		return freezeTrxId;
	}
	/**
	 * @param freezeTrxId the freezeTrxId to set
	 */
	
	public void setFreezeTrxId(String freezeTrxId) {
		this.freezeTrxId = freezeTrxId;
	}

	
}
