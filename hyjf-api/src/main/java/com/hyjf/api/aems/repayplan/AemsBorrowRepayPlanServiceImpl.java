/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.api.aems.repayplan;

import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.util.StringUtil;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.apiweb.aems.AemsBorrowRepayPlanCustomize;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AEMS系统:查询还款计划Service
 *
 * @author liuyang
 * @version AemsBorrowRepayPlanServiceImpl, v0.1 2018/10/16 17:36
 */
@Service
public class AemsBorrowRepayPlanServiceImpl extends BaseServiceImpl implements AemsBorrowRepayPlanService {
    /**
     * 获取标的列表
     *
     * @param requestBean
     * @return
     */
    @Override
    public List<AemsBorrowRepayPlanCustomize> selectBorrowRepayPlanList(AemsBorrowRepayPlanRequestBean requestBean) {

        Map<String, Object> param = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(requestBean.getInstCode())) {
            param.put("instCode", requestBean.getInstCode());
        }
        // 最后还款日开始
        if (StringUtils.isNotBlank(requestBean.getEndDate())) {
            param.put("startDate", requestBean.getStartDate() + " 00:00:00");
        }
        // 最后还款日结束
        if (StringUtils.isNotBlank(requestBean.getStartDate())) {
            param.put("endDate", requestBean.getEndDate() + " 23:59:59");
        }
        if (requestBean.getLimitStart() >= 0) {
            param.put("limitStart", requestBean.getLimitStart());
        }
        if (requestBean.getLimitEnd() > 0) {
            param.put("limitEnd", requestBean.getLimitEnd());
        }
        if (StringUtils.isNotBlank(requestBean.getIsMonth())) {
            param.put("isMonth", requestBean.getIsMonth());
        }
        if (StringUtils.isNotBlank(requestBean.getProductId())) {
            param.put("productId", requestBean.getProductId());
        }
        if (StringUtils.isNotBlank(requestBean.getRepayType())) {
            param.put("repayType", requestBean.getRepayType());
        }
        return aemsBorrowRepayPlanCustomizeMapper.selectBorrowRepayPlanList(param);
    }

    /**
     * 根据机构编号,查询还款计划数量
     *
     * @param requestBean
     * @return
     */
    @Override
    public Integer selectBorrowRepayPlanCountsByInstCode(AemsBorrowRepayPlanRequestBean requestBean) {
        Map<String, Object> param = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(requestBean.getInstCode())) {
            param.put("instCode", requestBean.getInstCode());
        }
        // 最后还款日开始
        if (StringUtils.isNotBlank(requestBean.getEndDate())) {
            param.put("startDate", requestBean.getStartDate() + " 00:00:00");
        }
        // 最后还款日结束
        if (StringUtils.isNotBlank(requestBean.getStartDate())) {
            param.put("endDate", requestBean.getEndDate() + " 23:59:59");
        }
        if (StringUtils.isNotBlank(requestBean.getIsMonth())) {
            param.put("isMonth", requestBean.getIsMonth());
        }
        if (StringUtils.isNotBlank(requestBean.getProductId())) {
            param.put("productId", requestBean.getProductId());
        }
        if (StringUtils.isNotBlank(requestBean.getRepayType())) {
            param.put("repayType", requestBean.getRepayType());
        }
        return aemsBorrowRepayPlanCustomizeMapper.selectBorrowRepayPlanCountsByInstCode(param);
    }

    /**
     * 根据标的号获取还款计划
     *
     * @param borrowNid
     * @return
     */
    @Override
    public List<BorrowRepayPlan> selectBorrowRepayPlanByBorrowNid(String borrowNid) {
        BorrowRepayPlanExample example = new BorrowRepayPlanExample();
        BorrowRepayPlanExample.Criteria cra = example.createCriteria();
        cra.andBorrowNidEqualTo(borrowNid);
        List<BorrowRepayPlan> borrowRepayPlanList = this.borrowRepayPlanMapper.selectByExample(example);
        if (borrowRepayPlanList != null && borrowRepayPlanList.size() > 0) {
            return borrowRepayPlanList;
        }
        return null;
    }

    /**
     * 根据标的编号查询资产推送表
     *
     * @param borrowNid
     * @return
     */
    @Override
    public HjhPlanAsset selectHjhPlanAssetByBorrowNid(String borrowNid) {
        HjhPlanAssetExample example = new HjhPlanAssetExample();
        HjhPlanAssetExample.Criteria cra = example.createCriteria();
        cra.andBorrowNidEqualTo(borrowNid);
        List<HjhPlanAsset> list = this.hjhPlanAssetMapper.selectByExample(example);
        if (list != null && list.size() != 0) {
            return list.get(0);
        }
        return null;
    }
}
