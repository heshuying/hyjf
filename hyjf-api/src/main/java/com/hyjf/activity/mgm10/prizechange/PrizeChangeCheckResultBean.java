package com.hyjf.activity.mgm10.prizechange;

import com.hyjf.base.bean.BaseResultBean;


public class PrizeChangeCheckResultBean extends BaseResultBean {
    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 失败原因编码  0：校验成功  1：请求参数不正确  2：用户推荐星不足  3：兑奖数量需要大于0  4：奖品不足 5：其他原因
     */
    private String errCode = "0";

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
    
    
    
}
