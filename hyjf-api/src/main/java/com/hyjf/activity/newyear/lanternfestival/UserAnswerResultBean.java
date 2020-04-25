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
public class UserAnswerResultBean extends BaseResultBean{

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;
    /**用户答题是否正确    1正确0错误*/
    private String isCorrect = "";
    
    /**提示信息*/
    private String  prompt= "";
    
    /**优惠券数量*/
    private String  couponCount= "";

    public String getIsCorrect() {
        return isCorrect;
    }

    public void setIsCorrect(String isCorrect) {
        this.isCorrect = isCorrect;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getCouponCount() {
        return couponCount;
    }

    public void setCouponCount(String couponCount) {
        this.couponCount = couponCount;
    }
    
    
    
}
