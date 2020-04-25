package com.hyjf.mybatis.mapper.customize;

import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.customize.admin.HjhBailConfigCustomize;
import com.hyjf.mybatis.model.customize.admin.HjhBailConfigInfoCustomize;

import java.util.HashMap;
import java.util.Map;
import java.util.List;

/**
 * @Auther:yangchangwei
 * @Date:2018/8/1
 * @Description:
 */
public interface HjhBailConfigCustomizeMapper {

    /**
     * 还款回滚保证金配置相关金额
     *
     * @param map
     * @return
     */
    Integer updateRepayInstitutionAmount(HashMap map);

    /**
     * 少放款保证金配置相关金额变更
     *
     * @param map
     * @return
     */
    Integer updateLoanInstitutionAmount(HashMap map);

    /**
     * 获取保证金配置列表数据
     *
     * @param hjhBailConfigCustomize
     * @return
     */
    List<HjhBailConfigInfoCustomize> selectHjhBailConfigList(HjhBailConfigCustomize hjhBailConfigCustomize);

    /**
     * 获取保证金配置详情
     *
     * @param id
     * @return
     */
    HjhBailConfigInfoCustomize selectHjhBailConfigInfo(Integer id);

    /**
     * 获取未配置保证金的资产来源
     *
     * @return
     */
    List<HjhInstConfig> hjhNoUsedInstConfigList();

    /**
     * 发标已发额度-动态计算该机构历史所有标的已发额度
     *
     * @return
     */
    String selectSendedAccount(HjhBailConfigCustomize hjhBailConfigCustomize);

    /**
     * 已还本金
     *
     * @return
     */
    String selectRepayedAccount(HjhBailConfigCustomize hjhBailConfigCustomize);

    /**
     * 周期内发标已发额度
     *
     * @param hjhBailConfigCustomize
     * @return
     */
    String selectSendedAccountByCyc(HjhBailConfigCustomize hjhBailConfigCustomize);

    /**
     * 在贷额度
     *
     * @return
     */
    String selectLoanCredit(HjhBailConfigCustomize hjhBailConfigCustomize);

    /**
     * 历史标的放款总额
     *
     * @param form
     * @return
     */
    String selectLoanAccount(HjhBailConfigInfoCustomize form);

    /**
     * 获取资产表中某段时间推标总额
     *
     * @param param
     * @return
     */
    String selectAccountByCyc(Map<String, String> param);

    /**
     * 发标更新相关金额
     * @return
     */
    Integer updateForSendBorrow(Map<String,Object> map);

    /**
     * 查询该资产可以用的还款方式
     *
     * @param instCode
     * @return
     */
    List<String> selectRepayMethod(String instCode);
}
