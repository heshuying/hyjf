package com.hyjf.admin.manager.hjhplan.plancapital;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.HjhPlanCapital;
import java.util.List;
import java.util.Map;

public interface PlanCapitalListService extends BaseService {
    /**
     * 获取记录数
     * @param form
     * @return
     */
    int countRecord(PlanCapitalListBean form);
    /**
     * 获取列表
     * @param form
     * @return
     */
    List<HjhPlanCapital> getRecordList(PlanCapitalListBean form);

    /**
     * 获取总计
     * @param form
     * @return
     */
    Map<String,Object> sumRecord(PlanCapitalListBean form);
}