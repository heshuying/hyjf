/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.mybatis.model.customize;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @author yinhui
 * @version ActivityMidauInfoCustomize, v0.1 2018/9/10 15:18
 */
public class ActivityMidauInfoCustomize  implements Serializable {

    /**
     * serialVersionUID
     */

    private static final long serialVersionUID = 1L;

    //编号
    public int ids;

    //奖励名称
    public String rewardName;

    //出借金额
    public BigDecimal investMoney;

    //产品类型
    public String productType;

    //产品期限
    public String productStyle;

    public int getIds() {
        return ids;
    }

    public void setIds(int ids) {
        this.ids = ids;
    }

    public String getRewardName() {
        return rewardName;
    }

    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }

    public BigDecimal getInvestMoney() {
        return investMoney;
    }

    public void setInvestMoney(BigDecimal investMoney) {
        this.investMoney = investMoney;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductStyle() {
        return productStyle;
    }

    public void setProductStyle(String productStyle) {
        this.productStyle = productStyle;
    }
}
