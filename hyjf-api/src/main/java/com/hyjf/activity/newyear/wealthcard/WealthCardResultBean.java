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
public class WealthCardResultBean extends BaseResultBean{

    /**
     * 
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;

    private Integer countJin = 0;
    
    private Integer countJi = 0;
    
    private Integer countNa = 0;
    
    private Integer countFu = 0;

    public Integer getCountJin() {
        return countJin;
    }

    public Integer getCountJi() {
        return countJi;
    }

    public Integer getCountFu() {
        return countFu;
    }

    public void setCountJin(Integer countJin) {
        this.countJin = countJin;
    }

    public void setCountJi(Integer countJi) {
        this.countJi = countJi;
    }

    public void setCountFu(Integer countFu) {
        this.countFu = countFu;
    }

    public Integer getCountNa() {
        return countNa;
    }

    public void setCountNa(Integer countNa) {
        this.countNa = countNa;
    }
    
    
}
