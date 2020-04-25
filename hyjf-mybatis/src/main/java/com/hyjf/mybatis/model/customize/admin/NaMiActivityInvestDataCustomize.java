
/**
 * Description:用户列表前端显示所用po
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
    
package com.hyjf.mybatis.model.customize.admin;

import java.io.Serializable;

/**
 * @author 王坤
 */

public class NaMiActivityInvestDataCustomize implements Serializable{
    
    /**
	 * 序列化id
	 */
		
	private static final long serialVersionUID = 7627555508742735666L;
	//借款期限
    private String borrowPeriod;
    //出借额度
    private String account;
    private Integer borrowStyle;
    public String getBorrowPeriod() {
        return borrowPeriod;
    }
    public void setBorrowPeriod(String borrowPeriod) {
        this.borrowPeriod = borrowPeriod;
    }
    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public Integer getBorrowStyle() {
        return borrowStyle;
    }
    public void setBorrowStyle(Integer borrowStyle) {
        this.borrowStyle = borrowStyle;
    }
    
}

    