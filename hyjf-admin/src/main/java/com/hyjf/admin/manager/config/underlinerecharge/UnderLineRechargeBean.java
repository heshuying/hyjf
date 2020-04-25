package com.hyjf.admin.manager.config.underlinerecharge;

import com.hyjf.mybatis.model.auto.UnderLineRecharge;

import java.io.Serializable;
import java.util.List;

public class UnderLineRechargeBean extends UnderLineRecharge implements Serializable {

    private String ids;

    private List<UnderLineRecharge> underLineRechargeList;

    public String getIds() {
        return ids;
    }

    public void setIds(String ids) {
        this.ids = ids;
    }

    public List<UnderLineRecharge> getUnderLineRechargeList() {
        return underLineRechargeList;
    }

    public void setUnderLineRechargeList(List<UnderLineRecharge> underLineRechargeList) {
        this.underLineRechargeList = underLineRechargeList;
    }
}
