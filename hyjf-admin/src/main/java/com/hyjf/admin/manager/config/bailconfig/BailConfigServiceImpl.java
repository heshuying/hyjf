package com.hyjf.admin.manager.config.bailconfig;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.admin.HjhBailConfigCustomize;
import com.hyjf.mybatis.model.customize.admin.HjhBailConfigInfoCustomize;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author PC-LIUSHOUYI
 */
@Service
public class BailConfigServiceImpl extends BaseServiceImpl implements BailConfigService {

    @Override
    public int countBailConfig() {
        HjhBailConfigExample example = new HjhBailConfigExample();
        example.createCriteria().andDelFlgEqualTo(0);
        return hjhBailConfigMapper.countByExample(example);
    }

    @Override
    public HjhBailConfigInfoCustomize selectHjhBailConfigInfo(Integer id) {
        return hjhBailConfigCustomizeMapper.selectHjhBailConfigInfo(id);
    }

    @Override
    public List<HjhInstConfig> hjhNoUsedInstConfigList() {
        return this.hjhBailConfigCustomizeMapper.hjhNoUsedInstConfigList();
    }

    @Override
    public String selectSendedAccount(HjhBailConfigInfoCustomize form) {
        return this.hjhBailConfigCustomizeMapper.selectSendedAccount(form);
    }

    @Override
    public String selectRepayedAccount(HjhBailConfigInfoCustomize form) {
        return this.hjhBailConfigCustomizeMapper.selectRepayedAccount(form);
    }

    @Override
    public String selectLoanCredit(HjhBailConfigInfoCustomize form) {
        return this.hjhBailConfigCustomizeMapper.selectLoanCredit(form);
    }

    @Override
    public String selectSendedAccountByCyc(HjhBailConfigInfoCustomize form) {
        return this.hjhBailConfigCustomizeMapper.selectSendedAccountByCyc(form);
    }

    @Override
    public String selectLoanAccount(HjhBailConfigInfoCustomize form) {
        return null;
    }

    @Override
    public List<String> selectRepayMethod(String instCode) {
        return this.hjhBailConfigCustomizeMapper.selectRepayMethod(instCode);
    }

    @Override
    public boolean updateBailInfoDelFlg(HjhBailConfigInfoCustomize form) {
        // 所有del_flg设为1
        HjhBailConfigInfoExample example1 = new HjhBailConfigInfoExample();
        example1.createCriteria().andInstCodeEqualTo(form.getInstCode());
        HjhBailConfigInfo hjhBailConfigInfo = new HjhBailConfigInfo();
        hjhBailConfigInfo.setDelFlg(1);
        if (this.hjhBailConfigInfoMapper.updateByExampleSelective(hjhBailConfigInfo, example1) > 0 ? false : true) {
            return false;
        }
        hjhBailConfigInfo.setDelFlg(0);
        // 所有查询出来的还款方式设为0
        List<String> repayMethodList = this.hjhBailConfigCustomizeMapper.selectRepayMethod(form.getInstCode());
        if (null != repayMethodList && repayMethodList.size() > 0) {
            for (String repayMethod : repayMethodList) {
                HjhBailConfigInfoExample example0 = new HjhBailConfigInfoExample();
                example0.createCriteria().andInstCodeEqualTo(form.getInstCode()).andBorrowStyleEqualTo(repayMethod);
                Integer isExites = this.hjhBailConfigInfoMapper.countByExample(example0);
                if (isExites > 0) {
                    this.hjhBailConfigInfoMapper.updateByExampleSelective(hjhBailConfigInfo, example0);
                } else {
                    // 不存在该还款法方式的追加一条记录
                    HjhBailConfigInfo hjhBailConfigInfo1 = new HjhBailConfigInfo();
                    hjhBailConfigInfo1.setDelFlg(0);
                    hjhBailConfigInfo1.setInstCode(form.getInstCode());
                    hjhBailConfigInfo1.setBorrowStyle(repayMethod);
                    hjhBailConfigInfo1.setCreateUserId(0);
                    hjhBailConfigInfo1.setCreateTime(new Date());
                    this.hjhBailConfigInfoMapper.insertSelective(hjhBailConfigInfo1);
                }
            }
        }
        return true;
    }

    /**
     * 获取保证金配置列表
     *
     * @return
     */
    @Override
    public List<HjhBailConfigInfoCustomize> getRecordList(HjhBailConfigCustomize hjhBailConfigCustomize) {
        return hjhBailConfigCustomizeMapper.selectHjhBailConfigList(hjhBailConfigCustomize);
    }

    /**
     * 获取单个保证金配置维护
     *
     * @return
     */
    @Override
    public HjhBailConfig getRecord(Integer record) {
        HjhBailConfig pushMoney = hjhBailConfigMapper.selectByPrimaryKey(record);
        return pushMoney;
    }

    /**
     * 根据主键判断保证金配置中数据是否存在
     *
     * @return
     */
    @Override
    public boolean isExistsRecord(HjhBailConfig record) {
        HjhBailConfigExample example = new HjhBailConfigExample();
        HjhBailConfigExample.Criteria cra = example.createCriteria();
        cra.andIdEqualTo(record.getId());
        List<HjhBailConfig> hjhBailConfigList = hjhBailConfigMapper.selectByExample(example);
        if (hjhBailConfigList != null && hjhBailConfigList.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 根据主键判断保证金配置中数据是否存在
     *
     * @return
     */
    public boolean isExistsPermission(HjhBailConfig record) {
        HjhBailConfigExample example = new HjhBailConfigExample();
        HjhBailConfigExample.Criteria cra = example.createCriteria();
        cra.andIdEqualTo(record.getId());
        if (record.getId() != null) {
            cra.andIdEqualTo(record.getId());
        }
        List<HjhBailConfig> hjhBailConfigList = hjhBailConfigMapper.selectByExample(example);
        if (hjhBailConfigList != null && hjhBailConfigList.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 保证金配置插入
     *
     * @param record
     */
    @Override
    public boolean insertRecord(HjhBailConfigInfoCustomize record) {
        // 创建时间记录
        record.setCreateTime(new Date());
        record.setUpdateTime(new Date());

        HjhBailConfig hjhBailConfig = new HjhBailConfig();
        BeanUtils.copyProperties(record, hjhBailConfig);
        // 插入保证及配置表
        if (hjhBailConfigMapper.insertSelective(hjhBailConfig) > 0 ? false : true) {
            return false;
        }
        // 合规改造删除 2018-11-03
       /* List<HjhBailConfigInfo> hjhBailConfigInfoList = bailInfoDeal(record);
        for (HjhBailConfigInfo hjhBailConfigInfo : hjhBailConfigInfoList) {
            // 设定资产编号
            hjhBailConfigInfo.setInstCode(record.getInstCode());
            // 更新ID
            hjhBailConfigInfo.setUpdateUserId(record.getUpdateUserId());
            // 创建ID
            hjhBailConfigInfo.setCreateUserId(record.getCreateUserId());
            hjhBailConfigInfo.setCreateTime(record.getCreateTime());
            hjhBailConfigInfo.setUpdateTime(record.getUpdateTime());
            // 插入保证金配置详情表
            if (hjhBailConfigInfoMapper.insertSelective(hjhBailConfigInfo) > 0 ? false : true) {
                return false;
            }
        }*/
        return true;
    }

    /**
     * 保证金配置更新
     *
     * @param record
     */
    @Override
    public boolean updateRecord(HjhBailConfigInfoCustomize record) {
        SimpleDateFormat date_sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 获取当天日期yyyyMMdd
        SimpleDateFormat yyyyMMdd = new SimpleDateFormat("yyyyMMdd");
        String nowDayYYYYMMDD = yyyyMMdd.format(new Date());

        // 根据主键获取修改前数据
        HjhBailConfig bailConfig = this.hjhBailConfigMapper.selectByPrimaryKey(record.getId());
        // 创建时间记录
        record.setUpdateTime(new Date());

        HjhBailConfig hjhBailConfig = new HjhBailConfig();
        BeanUtils.copyProperties(record, hjhBailConfig);
        // 周期内发标已发额度
        BigDecimal sendedAccountByCycBD = BigDecimal.ZERO;
        String sendedAccountByCyc = this.selectSendedAccountByCyc(record);
        if (StringUtils.isNotBlank(sendedAccountByCyc)) {
            sendedAccountByCycBD = new BigDecimal(sendedAccountByCyc);
        }
        hjhBailConfig.setCycLoanTotal(sendedAccountByCycBD);
        // 发标额度上限
//        hjhBailConfig.setPushMarkLine(record.getBailTatol().multiply(new BigDecimal("100")).divide(new BigDecimal(record.getBailRate()),2, BigDecimal.ROUND_DOWN));
        // 发标剩余额度计算
//        hjhBailConfig.setRemainMarkLine(BigDecimal.ZERO);
        // 新设保证金上限大于已发额度设置剩余额度、否则为0
//        if(hjhBailConfig.getPushMarkLine().add(bailConfig.getRepayedCapital()).compareTo(bailConfig.getLoanMarkLine()) > 0) {
//            hjhBailConfig.setRemainMarkLine(hjhBailConfig.getPushMarkLine().add(bailConfig.getRepayedCapital()).subtract(bailConfig.getLoanMarkLine()));
//        }
        // 判断开始日期是否是1号
        boolean startDay = "01".equals(record.getTimestart().substring(8, 10));
        if (!startDay) {
            // 判断开始日期是否是当月
            String startMonth = record.getTimestart().substring(5, 7);
            String nowDay = GetDate.getMonth();
            if (startMonth.equals(nowDay)) {
                String yuemoDay = date_sdf.format(GetDate.getLastDayOnMonth(new Date()));
                // 更新月使用额度
                Map<String, String> param = new HashMap<String, String>();
                param.put("instCode", record.getInstCode());
                param.put("timeStart", record.getTimestart());
                param.put("timeEnd", yuemoDay);
                String monthUsed = this.hjhBailConfigCustomizeMapper.selectAccountByCyc(param);
                if (StringUtils.isNotBlank(monthUsed)) {
                    RedisUtils.set(RedisConstants.MONTH_USED + record.getInstCode() + "_" + nowDayYYYYMMDD.substring(0, 6), monthUsed);
                } else {
                    RedisUtils.set(RedisConstants.MONTH_USED + record.getInstCode() + "_" + nowDayYYYYMMDD.substring(0, 6), "0");
                }
            }
        }

        // 更新保证及配置表
        if (hjhBailConfigMapper.updateByPrimaryKeySelective(hjhBailConfig) > 0 ? false : true) {
            return false;
        }

        // 合规改造删除 2018-12-03
        /*List<HjhBailConfigInfo> hjhBailConfigInfoList = bailInfoDeal(record);
        for (HjhBailConfigInfo hjhBailConfigInfo : hjhBailConfigInfoList) {
            // 设定资产编号
            hjhBailConfigInfo.setInstCode(record.getInstCode());
            // 设定更新时间
            hjhBailConfigInfo.setUpdateTime(new Date());
            hjhBailConfigInfo.setUpdateUserId(record.getUpdateUserId());
            // 根据资产编号和还款类型匹配唯一数据
            HjhBailConfigInfoExample example = new HjhBailConfigInfoExample();
            example.createCriteria().andInstCodeEqualTo(hjhBailConfig.getInstCode()).andBorrowStyleEqualTo(hjhBailConfigInfo.getBorrowStyle());
            // 插入保证金配置详情表
            if (hjhBailConfigInfoMapper.updateByExampleSelective(hjhBailConfigInfo, example) > 0 ? false : true) {
                return false;
            }
        }*/

        // 保证金修改日志
        HjhBailConfigLog hjhBailConfigLog = new HjhBailConfigLog();
        // 更新时间
        hjhBailConfigLog.setCreateTime(record.getCreateTime());
        hjhBailConfigLog.setCreateUserId(record.getCreateUserId());
        // 更新的资产来源
        hjhBailConfigLog.setInstCode(record.getInstCode());
        // 更新日志表
        // 保证金金额修改
//        if (!bailConfig.getBailTatol().equals(record.getBailTatol())) {
//            hjhBailConfigLog.setModifyColumn("保证金金额");
//            hjhBailConfigLog.setAfterValue(record.getBailTatol().toString());
//            hjhBailConfigLog.setBeforeValue(bailConfig.getBailTatol().toString());
//            this.hjhBailConfigLogMapper.insertSelective(hjhBailConfigLog);
//        }
        // 保证金比例
//        if (!bailConfig.getBailRate().equals(record.getBailRate())) {
//            hjhBailConfigLog.setModifyColumn("保证金比例");
//            hjhBailConfigLog.setAfterValue(record.getBailRate().toString());
//            hjhBailConfigLog.setBeforeValue(bailConfig.getBailRate().toString());
//            this.hjhBailConfigLogMapper.insertSelective(hjhBailConfigLog);
//        }
        // 日推标额度
        if (!bailConfig.getDayMarkLine().equals(record.getDayMarkLine())) {
            hjhBailConfigLog.setModifyColumn("日推标额度");
            hjhBailConfigLog.setAfterValue(record.getDayMarkLine().toString());
            hjhBailConfigLog.setBeforeValue(bailConfig.getDayMarkLine().toString());
            this.hjhBailConfigLogMapper.insertSelective(hjhBailConfigLog);
        }
        // 月推标额度
        if (!bailConfig.getMonthMarkLine().equals(record.getMonthMarkLine())) {
            hjhBailConfigLog.setModifyColumn("月推标额度");
            hjhBailConfigLog.setAfterValue(record.getMonthMarkLine().toString());
            hjhBailConfigLog.setBeforeValue(bailConfig.getMonthMarkLine().toString());
            this.hjhBailConfigLogMapper.insertSelective(hjhBailConfigLog);
        }
        // 合作额度
        if (!bailConfig.getNewCreditLine().equals(record.getNewCreditLine())) {
            hjhBailConfigLog.setModifyColumn("合作额度");
            hjhBailConfigLog.setAfterValue(record.getNewCreditLine().toString());
            hjhBailConfigLog.setBeforeValue(bailConfig.getNewCreditLine().toString());
            this.hjhBailConfigLogMapper.insertSelective(hjhBailConfigLog);
        }
        // 合作周期
        String timeStartDB = bailConfig.getTimestart() == null ? "" : bailConfig.getTimestart();
        String timeEndDB = bailConfig.getTimeend() == null ? "" : bailConfig.getTimeend();
        String timeStartPage = record.getTimestart() == null ? "" : record.getTimestart();
        String timeEndPage = record.getTimeend() == null ? "" : record.getTimeend();
        if (!timeStartDB.equals(timeStartPage) || !timeEndDB.equals(timeEndPage)) {
            hjhBailConfigLog.setModifyColumn("合作周期");
            hjhBailConfigLog.setAfterValue(timeStartPage + "~" + timeEndPage);
            hjhBailConfigLog.setBeforeValue(timeStartDB + "~" + timeEndDB);
            this.hjhBailConfigLogMapper.insertSelective(hjhBailConfigLog);
        }
        // 在贷余额授信额度
//        if (!bailConfig.getLoanCreditLine().equals(record.getLoanCreditLine())) {
//            hjhBailConfigLog.setModifyColumn("在贷余额授信额度");
//            hjhBailConfigLog.setAfterValue(record.getLoanCreditLine().toString());
//            hjhBailConfigLog.setBeforeValue(bailConfig.getLoanCreditLine().toString());
//            this.hjhBailConfigLogMapper.insertSelective(hjhBailConfigLog);
//        }
        return true;
    }

    /**
     * 保证金配置维护删除
     *
     * @param id
     */
    @Override
    public boolean deleteRecord(Integer id) {

        boolean re = true;
        // 根据id获取保证金配置
        HjhBailConfigExample hjhBailConfigExample = new HjhBailConfigExample();
        hjhBailConfigExample.createCriteria().andIdEqualTo(id);
        List<HjhBailConfig> hjhBailConfigList = this.hjhBailConfigMapper.selectByExample(hjhBailConfigExample);
        if (null == hjhBailConfigList || hjhBailConfigList.size() <= 0) {
            re = false;
        }
        String instCode = hjhBailConfigList.get(0).getInstCode();

        // 保证金配置表逻辑删除
        HjhBailConfig hjhBailConfig = this.getRecord(id);
        hjhBailConfig.setDelFlg(1);
        hjhBailConfig.setUpdateTime(new Date());
        if (hjhBailConfigMapper.updateByPrimaryKeySelective(hjhBailConfig) > 0 ? false : true) {
            re = false;
        }
        // 保证金详情配置表物理删除
//        HjhBailConfigInfoExample hjhBailConfigInfoExample = new HjhBailConfigInfoExample();
//        hjhBailConfigInfoExample.createCriteria().andInstCodeEqualTo(instCode);
//        if (hjhBailConfigInfoMapper.deleteByExample(hjhBailConfigInfoExample) > 0 ? false : true) {
//            re = false;
//        }

        // 表删除字段更新成功后删除redis值
        if (re && RedisUtils.exists(RedisConstants.DAY_MARK_LINE + hjhBailConfig.getInstCode())) {
            RedisUtils.del(RedisConstants.DAY_MARK_LINE + hjhBailConfig.getInstCode());
        }
        if (re && RedisUtils.exists(RedisConstants.MONTH_MARK_LINE + hjhBailConfig.getInstCode())) {
            RedisUtils.del(RedisConstants.MONTH_MARK_LINE + hjhBailConfig.getInstCode());
        }
        return re;
    }

    /**
     * 保证金校验方式和回滚方式处理
     *
     * @param hjhBailConfigInfoCustomize
     * @return
     */
    private List<HjhBailConfigInfo> bailInfoDeal(HjhBailConfigInfoCustomize hjhBailConfigInfoCustomize) {

        List<HjhBailConfigInfo> hjhBailConfigInfoList = new ArrayList<>();

        HjhBailConfigInfo hjhBailConfigInfoMonth = new HjhBailConfigInfo();
        // 等额本息配置
        hjhBailConfigInfoMonth.setBorrowStyle("month");
        // 新增授信校验
        hjhBailConfigInfoMonth.setIsNewCredit(hjhBailConfigInfoCustomize.getMonthNCL() == null ? 0 : hjhBailConfigInfoCustomize.getMonthNCL());
        // 在贷余额授信校验
        hjhBailConfigInfoMonth.setIsLoanCredit(hjhBailConfigInfoCustomize.getMonthLCL() == null ? 0 : hjhBailConfigInfoCustomize.getMonthLCL());
        // 保证金的回滚方式
        hjhBailConfigInfoMonth.setRepayCapitalType(hjhBailConfigInfoCustomize.getMonthRCT() == null ? 0 : hjhBailConfigInfoCustomize.getMonthRCT());
        hjhBailConfigInfoList.add(hjhBailConfigInfoMonth);

        HjhBailConfigInfo hjhBailConfigInfoEnd = new HjhBailConfigInfo();
        // 按月计息，到期还本还息配置
        hjhBailConfigInfoEnd.setBorrowStyle("end");
        // 新增授信校验
        hjhBailConfigInfoEnd.setIsNewCredit(hjhBailConfigInfoCustomize.getEndNCL() == null ? 0 : hjhBailConfigInfoCustomize.getEndNCL());
        // 在贷余额授信校验
        hjhBailConfigInfoEnd.setIsLoanCredit(hjhBailConfigInfoCustomize.getEndLCL() == null ? 0 : hjhBailConfigInfoCustomize.getEndLCL());
        // 保证金的回滚方式
        hjhBailConfigInfoEnd.setRepayCapitalType(hjhBailConfigInfoCustomize.getEndRCT() == null ? 0 : hjhBailConfigInfoCustomize.getEndRCT());
        hjhBailConfigInfoList.add(hjhBailConfigInfoEnd);

        HjhBailConfigInfo hjhBailConfigInfoEndmonth = new HjhBailConfigInfo();
        // 先息后本配置
        hjhBailConfigInfoEndmonth.setBorrowStyle("endmonth");
        // 新增授信校验
        hjhBailConfigInfoEndmonth.setIsNewCredit(hjhBailConfigInfoCustomize.getEndmonthNCL() == null ? 0 : hjhBailConfigInfoCustomize.getEndmonthNCL());
        // 在贷余额授信校验
        hjhBailConfigInfoEndmonth.setIsLoanCredit(hjhBailConfigInfoCustomize.getEndmonthLCL() == null ? 0 : hjhBailConfigInfoCustomize.getEndmonthLCL());
        // 保证金的回滚方式
        hjhBailConfigInfoEndmonth.setRepayCapitalType(hjhBailConfigInfoCustomize.getEndmonthRCT() == null ? 0 : hjhBailConfigInfoCustomize.getEndmonthRCT());
        hjhBailConfigInfoList.add(hjhBailConfigInfoEndmonth);

        HjhBailConfigInfo hjhBailConfigInfoEndday = new HjhBailConfigInfo();
        // 按天计息，到期还本息配置
        hjhBailConfigInfoEndday.setBorrowStyle("endday");
        // 新增授信校验
        hjhBailConfigInfoEndday.setIsNewCredit(hjhBailConfigInfoCustomize.getEnddayNCL() == null ? 0 : hjhBailConfigInfoCustomize.getEnddayNCL());
        // 在贷余额授信校验
        hjhBailConfigInfoEndday.setIsLoanCredit(hjhBailConfigInfoCustomize.getEnddayLCL() == null ? 0 : hjhBailConfigInfoCustomize.getEnddayLCL());
        // 保证金的回滚方式
        hjhBailConfigInfoEndday.setRepayCapitalType(hjhBailConfigInfoCustomize.getEnddayRCT() == null ? 0 : hjhBailConfigInfoCustomize.getEnddayRCT());
        hjhBailConfigInfoList.add(hjhBailConfigInfoEndday);

        HjhBailConfigInfo hjhBailConfigInfoPrincipal = new HjhBailConfigInfo();
        // 等额本金配置
        hjhBailConfigInfoPrincipal.setBorrowStyle("principal");
        // 新增授信校验
        hjhBailConfigInfoPrincipal.setIsNewCredit(hjhBailConfigInfoCustomize.getPrincipalNCL() == null ? 0 : hjhBailConfigInfoCustomize.getPrincipalNCL());
        // 在贷余额授信校验
        hjhBailConfigInfoPrincipal.setIsLoanCredit(hjhBailConfigInfoCustomize.getPrincipalLCL() == null ? 0 : hjhBailConfigInfoCustomize.getPrincipalLCL());
        // 保证金的回滚方式
        hjhBailConfigInfoPrincipal.setRepayCapitalType(hjhBailConfigInfoCustomize.getPrincipalRCT() == null ? 0 : hjhBailConfigInfoCustomize.getPrincipalRCT());
        hjhBailConfigInfoList.add(hjhBailConfigInfoPrincipal);

        HjhBailConfigInfo hjhBailConfigInfoSeason = new HjhBailConfigInfo();
        // 等额本金配置
        hjhBailConfigInfoSeason.setBorrowStyle("season");
        // 新增授信校验
        hjhBailConfigInfoSeason.setIsNewCredit(hjhBailConfigInfoCustomize.getSeasonNCL() == null ? 0 : hjhBailConfigInfoCustomize.getSeasonNCL());
        // 在贷余额授信校验
        hjhBailConfigInfoSeason.setIsLoanCredit(hjhBailConfigInfoCustomize.getSeasonLCL() == null ? 0 : hjhBailConfigInfoCustomize.getSeasonLCL());
        // 保证金的回滚方式
        hjhBailConfigInfoSeason.setRepayCapitalType(hjhBailConfigInfoCustomize.getSeasonRCT() == null ? 0 : hjhBailConfigInfoCustomize.getSeasonRCT());
        hjhBailConfigInfoList.add(hjhBailConfigInfoSeason);

        HjhBailConfigInfo hjhBailConfigInfoEndmonths = new HjhBailConfigInfo();
        // 等额本金配置
        hjhBailConfigInfoEndmonths.setBorrowStyle("endmonths");
        // 新增授信校验
        hjhBailConfigInfoEndmonths.setIsNewCredit(hjhBailConfigInfoCustomize.getEndmonthsNCL() == null ? 0 : hjhBailConfigInfoCustomize.getEndmonthsNCL());
        // 在贷余额授信校验
        hjhBailConfigInfoEndmonths.setIsLoanCredit(hjhBailConfigInfoCustomize.getEndmonthsLCL() == null ? 0 : hjhBailConfigInfoCustomize.getEndmonthsLCL());
        // 保证金的回滚方式
        hjhBailConfigInfoEndmonths.setRepayCapitalType(hjhBailConfigInfoCustomize.getEndmonthsRCT() == null ? 0 : hjhBailConfigInfoCustomize.getEndmonthsRCT());
        hjhBailConfigInfoList.add(hjhBailConfigInfoEndmonths);

        return hjhBailConfigInfoList;
    }
}
