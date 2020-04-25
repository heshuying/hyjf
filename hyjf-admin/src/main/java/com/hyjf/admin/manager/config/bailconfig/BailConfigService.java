package com.hyjf.admin.manager.config.bailconfig;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.HjhBailConfig;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.customize.admin.HjhBailConfigCustomize;
import com.hyjf.mybatis.model.customize.admin.HjhBailConfigInfoCustomize;

import java.util.List;

public interface BailConfigService extends BaseService {

    /**
     * 获取保证金配置列表
     *
     * @return
     */
    List<HjhBailConfigInfoCustomize> getRecordList(HjhBailConfigCustomize hjhBailConfigCustomize);

    /**
     * 获取单个取保证金配置
     *
     * @return
     */
    HjhBailConfig getRecord(Integer record);

    /**
     * 根据主键判断取保证金配置中数据是否存在
     *
     * @return
     */
    boolean isExistsRecord(HjhBailConfig record);

    /**
     * 取保证金配置插入
     *
     * @param record
     */
    boolean insertRecord(HjhBailConfigInfoCustomize record);

    /**
     * 取保证金配置更新
     *
     * @param record
     */
    boolean updateRecord(HjhBailConfigInfoCustomize record);

    /**
     * 取保证金配置删除
     *
     * @param id
     */
    boolean deleteRecord(Integer id);

    /**
     * 获取保证金配置总数
     *
     * @return
     */
    int countBailConfig();

    /**
     * 根据页面主键获取保证金详情
     *
     * @param id
     * @return
     */
    HjhBailConfigInfoCustomize selectHjhBailConfigInfo(Integer id);

    /**
     * 获取未配置的资产来源
     *
     * @return
     */
    List<HjhInstConfig> hjhNoUsedInstConfigList();

    /**
     * 发标已发额度
     *
     * @param form
     * @return
     */
    String selectSendedAccount(HjhBailConfigInfoCustomize form);

    /**
     * 已还本金统计
     *
     * @param form
     * @return
     */
    String selectRepayedAccount(HjhBailConfigInfoCustomize form);

    /**
     * 在贷余额统计
     *
     * @param form
     * @return
     */
    String selectLoanCredit(HjhBailConfigInfoCustomize form);

    /**
     * 周期内发标已发额度
     *
     * @param form
     * @return
     */
    String selectSendedAccountByCyc(HjhBailConfigInfoCustomize form);

    /**
     * 历史标的放款总额
     *
     * @param form
     * @return
     */
    String selectLoanAccount(HjhBailConfigInfoCustomize form);

    /**
     * 查询该资产可用的还款方式
     *
     * @param instCode
     * @return
     */
    List<String> selectRepayMethod(String instCode);

    /**
     * 根据该机构可用还款方式更新可用授信方式
     *
     * @param form
     * @return
     */
    boolean updateBailInfoDelFlg(HjhBailConfigInfoCustomize form);
}
