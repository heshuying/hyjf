package com.hyjf.admin.manager.activity.namimarketing.reffer;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.manager.activity.namimarketing.NaMiMarketingBean;
import com.hyjf.admin.manager.activity.namimarketing.NaMiMarketingService;
import com.hyjf.mybatis.mapper.customize.NaMiMarketing.NaMiMarketingCustomizeMapper;
import com.hyjf.mybatis.model.auto.PerformanceReturnDetail;
import com.hyjf.mybatis.model.customize.NaMiMarketing.NaMiMarketingCustomize;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author xiehuili on 2018/11/8.
 */
@Service
public class NaMiMarketingRefferServiceImpl extends BaseServiceImpl implements NaMiMarketingRefferService {

    @Autowired
    public NaMiMarketingCustomizeMapper naMiMarketingCustomizeMapper;

    /**
     * 查询邀请人返现明细 条数
     * @param paraMap
     * @return
     */
    @Override
    public int selectNaMiMarketingRefferCount(Map<String, Object> paraMap){
        return naMiMarketingCustomizeMapper.selectNaMiMarketingRefferCount(paraMap);
    }

    /**
     * 查询邀请人返现明细 列表
     * @param paraMap
     * @return
     */
    @Override
    public List<NaMiMarketingCustomize> selectNaMiMarketingRefferList(Map<String, Object> paraMap){
        return naMiMarketingCustomizeMapper.selectNaMiMarketingRefferList(paraMap);
    }

    /**
     * 查询邀请人返现统计 条数
     * @param paraMap
     * @return
     */
    @Override
    public int selectNaMiMarketingRefferTotalCount(Map<String, Object> paraMap){
        return naMiMarketingCustomizeMapper.selectNaMiMarketingRefferTotalCount(paraMap);
    }

    /**
     * 查询邀请人返现统计 列表
     * @param paraMap
     * @return
     */
    @Override
    public List<NaMiMarketingCustomize> selectNaMiMarketingRefferTotalList(Map<String, Object> paraMap){
        return naMiMarketingCustomizeMapper.selectNaMiMarketingRefferTotalList(paraMap);
    }

    /**
     * 查询邀请人返现统计 合计
     * @param paraMap
     * @return
     */
    @Override
    public BigDecimal selectNaMiMarketingRefferTotalAmount(Map<String, Object> paraMap){
        return naMiMarketingCustomizeMapper.selectNaMiMarketingRefferTotalAmount(paraMap);
    }
    /**
     * 查询 月份列表
     * @param paraMap
     * @return
     */
    @Override
    public List<String> selectMonthList(){
        return naMiMarketingCustomizeMapper.selectMonthList();
    }
}
