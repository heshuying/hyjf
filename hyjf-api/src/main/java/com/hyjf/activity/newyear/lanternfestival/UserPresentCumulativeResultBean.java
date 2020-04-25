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
public class UserPresentCumulativeResultBean extends BaseResultBean{

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;
 
    
    /**用户是否有优惠券  1有优惠券，0没有优惠券*/
    private String canReceiveFlag;
    /**用户累计优惠券面值*/
    private String userPresentCumulativeCoupon;
    /**用户累计优惠券面值文字*/
    private Integer userPresentCumulativeCouponCount;
    /**用户累计优惠券面值*/
    private String prizeJine;
    
    /**用户累计优惠券面值*/
    private String lastQuestion;

    private String showAnswerFlag;
    public String getUserPresentCumulativeCoupon() {
        return userPresentCumulativeCoupon;
    }
    public void setUserPresentCumulativeCoupon(String userPresentCumulativeCoupon) {
        this.userPresentCumulativeCoupon = userPresentCumulativeCoupon;
    }
    public Integer getUserPresentCumulativeCouponCount() {
        return userPresentCumulativeCouponCount;
    }
    public void setUserPresentCumulativeCouponCount(Integer userPresentCumulativeCouponCount) {
        this.userPresentCumulativeCouponCount = userPresentCumulativeCouponCount;
    }
    public String getCanReceiveFlag() {
        return canReceiveFlag;
    }
    public void setCanReceiveFlag(String canReceiveFlag) {
        this.canReceiveFlag = canReceiveFlag;
    }
    public String getPrizeJine() {
        return prizeJine;
    }
    public void setPrizeJine(String prizeJine) {
        this.prizeJine = prizeJine;
    }
    public String getLastQuestion() {
        return lastQuestion;
    }
    public void setLastQuestion(String lastQuestion) {
        this.lastQuestion = lastQuestion;
    }
    public String getShowAnswerFlag() {
        return showAnswerFlag;
    }
    public void setShowAnswerFlag(String showAnswerFlag) {
        this.showAnswerFlag = showAnswerFlag;
    }
   
}
