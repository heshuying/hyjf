package com.hyjf.activity.newyear.lanternfestival;

import com.hyjf.base.bean.BaseResultBean;

/**
 * 
 * 谜题信息
 * @author pcc
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年1月9日
 * @see 下午3:37:37
 */
public class CheckResultBean extends BaseResultBean{

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;
    /**第一行提示*/
    private String message1 = "";
    /**第二行提示*/
    private String message2 = "";
    /**校验状态 0检验不通过   1校验通过*/
    private String checkStatus="";
    public String getMessage1() {
        return message1;
    }
    public void setMessage1(String message1) {
        this.message1 = message1;
    }
    public String getMessage2() {
        return message2;
    }
    public void setMessage2(String message2) {
        this.message2 = message2;
    }
    public String getCheckStatus() {
        return checkStatus;
    }
    public void setCheckStatus(String checkStatus) {
        this.checkStatus = checkStatus;
    }
    
}
