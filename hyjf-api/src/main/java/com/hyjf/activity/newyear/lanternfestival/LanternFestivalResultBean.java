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
public class LanternFestivalResultBean extends BaseResultBean{

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = 1L;
    /**用户今天是否打过题标示    0答过1未答*/
    private String userAnswerFlag = "";

    public String getUserAnswerFlag() {
        return userAnswerFlag;
    }
    public void setUserAnswerFlag(String userAnswerFlag) {
        this.userAnswerFlag = userAnswerFlag;
    }
    
}
