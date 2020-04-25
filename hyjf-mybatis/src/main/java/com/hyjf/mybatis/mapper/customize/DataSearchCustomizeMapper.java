package com.hyjf.mybatis.mapper.customize;

import com.hyjf.common.util.StringUtil;
import com.hyjf.mybatis.model.customize.DataSearchCustomize;

import java.util.List;
import java.util.Map;

/**
 * @author lisheng
 * @version DataSearchCustomizeMapper, v0.1 2018/7/4 14:22
 */

public interface DataSearchCustomizeMapper {
    /**
     * 查询所有数据
     * @param req
     * @return
     */
     List<DataSearchCustomize> queryList(Map<String, Object> req);

    /**
     * 查询总条数
     * @param req
     * @return
     */
     Integer queryCount(Map<String, Object> req);
    /**
     * 查询计划数据
     * @param req
     * @return
     */
     List<DataSearchCustomize> queryPlanList(Map<String, Object> req);

    /**
     * 查询计划条数
     * @param req
     * @return
     */
     Integer queryPlanCount(Map<String, Object> req);

    /**
     * 查询散标数据
     * @param req
     * @return
     */
     List<DataSearchCustomize> querySanList(Map<String, Object> req);

    /**
     * 查询散标条数
     * @param req
     * @return
     */
     Integer querySanCount(Map<String, Object> req);
    /**
     * 查询总金额
     * @param req
     * @return
     */
     Map<String, Object> queryMoney(Map<String, Object> req);

    /**
     * 查询计划金额
     * @param req
     * @return
     */
     Map<String, Object> queryPlanMoney(Map<String, Object> req);

    /**
     * 查询散标金额
     * @param req
     * @return
     */
     Map<String, Object> querySanMoney(Map<String, Object> req);


    /**
     * 查询用户的首笔出借
     * @param req
     * @return
     */
     Map<String, Object> queryFirstTender(Map<String, Object> req);

}
