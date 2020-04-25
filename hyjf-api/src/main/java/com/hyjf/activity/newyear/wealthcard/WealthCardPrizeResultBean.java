package com.hyjf.activity.newyear.wealthcard;

import com.hyjf.base.bean.BaseResultBean;

/**
 * 
 * 财富卡
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2017年1月9日
 * @see 下午3:37:37
 */
public class WealthCardPrizeResultBean extends BaseResultBean{

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;
    
    /**
     * 0：成功  1：请求参数不正确  2：没有集齐四张卡  3 抽奖处理失败
     */
    private String errCode;

    private String prizeName = "";
    
    /**
     * 优惠券奖品：0  实物奖品：1
     */
    private int prizeType;

    public String getPrizeName() {
        return prizeName;
    }

    public void setPrizeName(String prizeName) {
        this.prizeName = prizeName;
    }

    public int getPrizeType() {
        return prizeType;
    }

    public void setPrizeType(int prizeType) {
        this.prizeType = prizeType;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }
    
    
}
