package com.hyjf.admin.manager.config.account.accountbalance;

import java.io.Serializable;

import com.hyjf.mybatis.model.auto.MerchantAccount;

/**
 * 此处为类说明
 * @author hyjf
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月11日
 * @see 下午6:31:13
 */
public class SubAccountListResult extends MerchantAccount implements Serializable {

    /**
     * 此处为属性说明
     */
    private static final long serialVersionUID = -3174376695356202205L;

    /**
     * ids
     */
    private String ids;

    /** 是否更新 */
    private boolean isUpdateFlg;

    public boolean isUpdateFlg() {
        return isUpdateFlg;
    }

    public void setUpdateFlg(boolean isUpdateFlg) {
        this.isUpdateFlg = isUpdateFlg;
    }

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

}
