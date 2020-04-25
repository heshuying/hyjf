/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.content.operationreport;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.operationreport.dao.*;
import com.hyjf.mongo.operationreport.entity.OperationReportColumnEntity;
import com.hyjf.mongo.operationreport.entity.TenthOperationReportEntity;
import com.hyjf.mongo.operationreport.entity.UserOperationReportEntity;
import com.hyjf.mybatis.mapper.customize.OperationReportInfoCustomizeMapper;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.OperationReportInfoCustomize;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计运营报告详情（月，季度，半年，全年） 的基本类
 *
 * @author yinhui
 * @version StatisticsOperationReportBase, v0.1 2018/6/22 9:38
 */
@Service
public class StatisticsOperationReportBase extends BaseServiceImpl {

    @Autowired
    public OperationReportInfoCustomizeMapper operationReportInfoCustomizeMapper;

    @Autowired
    public OperationReportColumnMongDao operationReportColumnMongDao;//运营报告
    @Autowired
    public HalfYearOperationReportMongDao halfYearOperationReportMongDao;//半年度度运营报告
    @Autowired
    public MonthlyOperationReportMongDao monthlyOperationReportMongDao;//月度运营报告
    @Autowired
    public OperationReportActivityMongDao operationReportActivityMongDao;//运营报告活动
    @Autowired
    public QuarterOperationReportMongDao quarterOperationReportMongDao;//季度运营报告
    @Autowired
    public TenthOperationReportMongDao tenthOperationReportMongDao;//运营报告十大出借
    @Autowired
    public UserOperationReportMongDao userOperationReportMongDao;//用户分析报告
    @Autowired
    public YearOperationReportMongDao yearOperationReportMongDao;//年度运营报告


    public static BigDecimal bigHundred = new BigDecimal(100);
    public static BigDecimal bigZer0 = BigDecimal.ZERO;
    public static BigDecimal bigflag = BigDecimal.ZERO;

    /**
     * 保存 运营报告显示栏
     *
     * @param cnName          中文名字
     * @param enName          英文名字
     * @param type            运营报告类型(1.月度2.季度3.上半年度4.年度)
     * @param allAmount       累计交易额（元）
     * @param allProfit       累计收益（元）
     * @param registNum       平台注册人数
     * @param successDealNum  本月成交笔数(笔)
     * @param operationAmount 本月成交金额（元）
     * @param operationProfit 本月赚取收益（元）
     * @param year
     */
    public String saveOperationReport(String cnName, String enName, Integer type,
                                      BigDecimal allAmount, BigDecimal allProfit, BigDecimal registNum,
                                      Integer successDealNum, BigDecimal operationAmount,
                                      BigDecimal operationProfit, String year) {
        OperationReport operationReport = new OperationReport();
        operationReport.setCnName(cnName);//中文名字
        operationReport.setEnName(enName);//英文名字
        operationReport.setOperationReportType(type);//运营报告类型(1.月度2.季度3.上半年度4.年度)
        operationReport.setAllAmount(allAmount.setScale(2, BigDecimal.ROUND_HALF_UP));//累计交易额（元）
        operationReport.setAllProfit(allProfit.setScale(2, BigDecimal.ROUND_HALF_UP));//累计收益（元）
        operationReport.setRegistNum(registNum.setScale(0, BigDecimal.ROUND_HALF_UP));//平台注册人数
        operationReport.setSuccessDealNum(successDealNum);//本月成交笔数(笔)
        operationReport.setOperationAmount(operationAmount.setScale(2, BigDecimal.ROUND_HALF_UP));//本月成交金额（元）
        operationReport.setOperationProfit(operationProfit.setScale(2, BigDecimal.ROUND_HALF_UP));//本月赚取收益（元）
        operationReport.setIsRelease(0);//是否发布
        operationReport.setIsDelete(0);//是否删除（0.未删除 1.已删除）
        operationReport.setCreateTime(GetDate.getNowTime10());
        operationReport.setCreateUserId(1);
        operationReport.setYear(year);//年

//        operationReportMapper.insert(operationReport);
        OperationReportColumnEntity operationReportColumnEntity = new OperationReportColumnEntity();
        BeanUtils.copyProperties(operationReport, operationReportColumnEntity);
        operationReportColumnMongDao.insert(operationReportColumnEntity);
        String id = operationReportColumnEntity.getId();
        return id;

    }

    /**
     * 保存  用户分析报告
     *
     * @param operationReportId 运营报告Id
     * @param type              运营报告类型(1.月度2.季度3.上半年度4.年度)
     * @param intervalMonth
     */
    public void saveUserOperationReport(String operationReportId, Integer type, int intervalMonth) throws Exception {
        Integer manTenderNum = 0;//男性出借人数
        Integer womanTenderNum = 0;//女性出借人数
        Integer ageFirstStageTenderNum = 0;//18~29岁出借人数（人）
        Integer ageSecondStageTenderNum = 0;//30~39岁出借人数（人）
        Integer ageThirdStageTenderNum = 0;//40~49岁出借人数（人）
        Integer ageFourthStageTenderNum = 0;//50~59岁出借人数（人）
        Integer ageFirveStageTenderNum = 0;//60岁以上出借人数（人）
        Integer ageTenderNumSum = 0;//年龄出借人数总
        Integer amountFirstStageTenderNum = 0;//1万以下出借人数
        Integer amountSecondStageTenderNum = 0;//1~5万出借人数
        Integer amountThirdStageTenderNum = 0;//5~10万出借人数
        Integer amountFourthStageTenderNum = 0;//10~50万出借人数
        Integer amountFirveStageTenderNum = 0;//50万以上出借人数
        Integer amountStageTenderNumSum = 0;//金额出借人数数总


        UserOperationReport userOperationReport = new UserOperationReport();
//        userOperationReport.setOperationReportId(operationReportId);//运营报告ID
        userOperationReport.setOperationReportType(type);//运营报告类型(1.月度2.季度3.上半年度4.年度)
        userOperationReport.setCreateTime(GetDate.getNowTime10());
        userOperationReport.setCreateUserId(1);

        //性别分布
        Map<String, Integer> mapexDistribute = this.getSexDistribute(intervalMonth);
        if (!CollectionUtils.isEmpty(mapexDistribute)) {
            manTenderNum = mapexDistribute.get("manTenderNum");
            womanTenderNum = mapexDistribute.get("womanTenderNum");

            //校验 百分比是否等于100%
            bigflag = checkPercent(manTenderNum + womanTenderNum, manTenderNum, womanTenderNum);
            userOperationReport.setManTenderNum(manTenderNum);//男性出借人数
            userOperationReport.setManTenderNumProportion(assignCompute(manTenderNum, manTenderNum + womanTenderNum, bigflag).setScale(2, BigDecimal.ROUND_HALF_UP));//男性出借人数占比(%)
            userOperationReport.setWomanTenderNum(womanTenderNum);//女性出借人数
            userOperationReport.setWomanTenderNumProportion(assignCompute(womanTenderNum, manTenderNum + womanTenderNum, bigflag).setScale(2, BigDecimal.ROUND_HALF_UP));//女性出借人数占比(%)
        }


        //年龄分布
        Map<String, Integer> mapAgeDistribute = this.getAgeDistribute(intervalMonth);
        if (!CollectionUtils.isEmpty(mapAgeDistribute)) {

            ageFirstStageTenderNum = mapAgeDistribute.get("18-29");
            ageSecondStageTenderNum = mapAgeDistribute.get("30-39");
            ageThirdStageTenderNum = mapAgeDistribute.get("40-49");
            ageFourthStageTenderNum = mapAgeDistribute.get("50-59");
            ageFirveStageTenderNum = mapAgeDistribute.get("60-");

            ageTenderNumSum = ageFirstStageTenderNum + ageSecondStageTenderNum + ageThirdStageTenderNum +
                    ageFourthStageTenderNum + ageFirveStageTenderNum;

            //校验 百分比是否等于100%
            bigflag = checkPercent(ageTenderNumSum, ageFirstStageTenderNum, ageSecondStageTenderNum, ageThirdStageTenderNum,
                    ageFourthStageTenderNum, ageFirveStageTenderNum);

            //set 年分布
            userOperationReport.setAgeFirstStageTenderNum(ageFirstStageTenderNum);//18~29岁出借人数（人）
            userOperationReport.setAgeFirstStageTenderProportion(assignCompute(ageFirstStageTenderNum, ageTenderNumSum, bigflag).setScale(2, BigDecimal.ROUND_HALF_UP));//18~29岁出借人数占比（%）
            userOperationReport.setAgeSecondStageTenderNum(ageSecondStageTenderNum);//30~39岁出借人数（人）
            userOperationReport.setAgeSecondStageTenderProportion(assignCompute(ageSecondStageTenderNum, ageTenderNumSum, bigflag).setScale(2, BigDecimal.ROUND_HALF_UP));//30~39岁出借人数占比（%）
            userOperationReport.setAgeThirdStageTenderNum(ageThirdStageTenderNum);//40~49岁出借人数（人）
            userOperationReport.setAgeThirdStageTenderProportion(assignCompute(ageThirdStageTenderNum, ageTenderNumSum, bigflag).setScale(2, BigDecimal.ROUND_HALF_UP));//40~49岁出借人数占比（%）
            userOperationReport.setAgeFourthStageTenderNum(ageFourthStageTenderNum);//50~59岁出借人数（人）
            userOperationReport.setAgeFourthStageTenderProportion(assignCompute(ageFourthStageTenderNum, ageTenderNumSum, bigflag).setScale(2, BigDecimal.ROUND_HALF_UP));//50~59岁出借人数占比（%）
            userOperationReport.setAgeFirveStageTenderNum(ageFirveStageTenderNum);//60岁以上出借人数（人）
            userOperationReport.setAgeFirveStageTenderProportion(assignCompute(ageFirveStageTenderNum, ageTenderNumSum, bigflag).setScale(2, BigDecimal.ROUND_HALF_UP));//60岁以上出借人数占比（%）
        }

        //金额分布
        Map<String, Integer> mapMoneyDistribute = this.getMoneyDistribute(intervalMonth);
        if (!CollectionUtils.isEmpty(mapMoneyDistribute)) {
            amountFirstStageTenderNum = mapMoneyDistribute.get("0-1");
            amountSecondStageTenderNum = mapMoneyDistribute.get("1-5");
            amountThirdStageTenderNum = mapMoneyDistribute.get("5-10");
            amountFourthStageTenderNum = mapMoneyDistribute.get("10-50");
            amountFirveStageTenderNum = mapMoneyDistribute.get("50-");

            amountStageTenderNumSum = amountFirstStageTenderNum + amountSecondStageTenderNum + amountThirdStageTenderNum +
                    amountFourthStageTenderNum + amountFirveStageTenderNum;

            //校验 百分比是否等于100%
            bigflag = checkPercent(amountStageTenderNumSum, amountFirstStageTenderNum, amountSecondStageTenderNum, amountThirdStageTenderNum,
                    amountFourthStageTenderNum, amountFirveStageTenderNum);

            userOperationReport.setAmountFirstStageTenderNum(amountFirstStageTenderNum);//1万以下出借人数
            userOperationReport.setAmountFirstStageTenderProportion(assignCompute(amountFirstStageTenderNum, amountStageTenderNumSum, bigflag).setScale(2, BigDecimal.ROUND_HALF_UP));//1万以下出借人数占比（%）
            userOperationReport.setAmountSecondStageTenderNum(amountSecondStageTenderNum);//1~5万出借人数
            userOperationReport.setAmountSecondStageTenderProportion(assignCompute(amountSecondStageTenderNum, amountStageTenderNumSum, bigflag).setScale(2, BigDecimal.ROUND_HALF_UP));//1~5万出借人数（%）
            userOperationReport.setAmountThirdStageTenderNum(amountThirdStageTenderNum);//5~10万出借人数
            userOperationReport.setAmountThirdStageTenderProportion(assignCompute(amountThirdStageTenderNum, amountStageTenderNumSum, bigflag).setScale(2, BigDecimal.ROUND_HALF_UP));//5~10万出借人数（%）
            userOperationReport.setAmountFourthStageTenderNum(amountFourthStageTenderNum);//10~50万出借人数
            userOperationReport.setAmountFourthStageTenderProportion(assignCompute(amountFourthStageTenderNum, amountStageTenderNumSum, bigflag).setScale(2, BigDecimal.ROUND_HALF_UP));//10~50万出借人数（%）
            userOperationReport.setAmountFirveStageTenderNum(amountFirveStageTenderNum);//50万以上出借人数
            userOperationReport.setAmountFirveStageTenderProportion(assignCompute(amountFirveStageTenderNum, amountStageTenderNumSum, bigflag).setScale(2, BigDecimal.ROUND_HALF_UP));//50万以上出借人数（%）
        }

        UserOperationReportEntity userOperationReportEntity = new UserOperationReportEntity();
        BeanUtils.copyProperties(userOperationReport,userOperationReportEntity);
        userOperationReportEntity.setOperationReportId(operationReportId);//运营报告ID

//        userOperationReportMapper.insert(userOperationReport);
        userOperationReportMongDao.insert(userOperationReportEntity);
    }

    /**
     * 保存 当月之最-十大出借人
     *
     * @param operationReportId
     * @param type              运营报告类型(1.月度2.季度3.上半年度4.年度)
     * @param intervalMonth
     * @param sumTenderAmount   累计成交金额
     */
    public void saveTenthOperationReport(String operationReportId, Integer type, int intervalMonth, BigDecimal sumTenderAmount) {
        BigDecimal tenderAmountSum = BigDecimal.ZERO;//十大出借人出借总和
        String tenderUsername = null;//出借者用户名
        BigDecimal tenderAmountMoney = BigDecimal.ZERO;//出借金额
        Integer userId = 0;//用户ID

        TenthOperationReport tenthOperationReport = new TenthOperationReport();
//        tenthOperationReport.setOperationReportId(operationReportId);//运营报告ID
        tenthOperationReport.setOperationReportType(type);//运营报告类型(1.月度2.季度3.上半年度4.年度)
        tenthOperationReport.setCreateTime(GetDate.getNowTime10());
        tenthOperationReport.setCreateUserId(1);

        //计算 十大出借人出借金额
        List<OperationReportInfoCustomize> listTenMostMoney = this.getTenMostMoney(intervalMonth);
        if (!CollectionUtils.isEmpty(listTenMostMoney)) {

            for (int i = 0; i < listTenMostMoney.size(); i++) {
                OperationReportInfoCustomize dto = listTenMostMoney.get(i);
                switch (i) {
                    case 0:
                        tenderUsername = dto.getUserName();
                        userId = dto.getUserId();
                        tenderAmountMoney = dto.getSumAccount();
                        tenderAmountSum = tenderAmountSum.add(dto.getSumAccount());
                        tenthOperationReport.setFirstTenderUsername(dto.getUserName());//第1名用户名
                        tenthOperationReport.setFirstTenderAmount(dto.getSumAccount());//第1名出借金额(元)
                        break;
                    case 1:
                        tenderAmountSum = tenderAmountSum.add(dto.getSumAccount());
                        tenthOperationReport.setSecondTenderUsername(dto.getUserName());//第2名用户名
                        tenthOperationReport.setSecondTenderAmount(dto.getSumAccount());//第2名出借金额(元)
                        break;
                    case 2:
                        tenderAmountSum = tenderAmountSum.add(dto.getSumAccount());
                        tenthOperationReport.setThirdTenderUsername(dto.getUserName());//第3名用户名
                        tenthOperationReport.setThirdTenderAmount(dto.getSumAccount());//第3名出借金额(元)
                        break;
                    case 3:
                        tenderAmountSum = tenderAmountSum.add(dto.getSumAccount());
                        tenthOperationReport.setFourthTenderUsername(dto.getUserName());//第4名用户名
                        tenthOperationReport.setFourthTenderAmount(dto.getSumAccount());//第4名出借金额(元)
                        break;
                    case 4:
                        tenderAmountSum = tenderAmountSum.add(dto.getSumAccount());
                        tenthOperationReport.setFifthTenderUsername(dto.getUserName());//第5名用户名
                        tenthOperationReport.setFifthTenderAmount(dto.getSumAccount());//第5名出借金额(元)
                        break;
                    case 5:
                        tenderAmountSum = tenderAmountSum.add(dto.getSumAccount());
                        tenthOperationReport.setSixthTenderUsername(dto.getUserName());//第6名用户名
                        tenthOperationReport.setSixthTenderAmount(dto.getSumAccount());//第6名出借金额(元)
                        break;
                    case 6:
                        tenderAmountSum = tenderAmountSum.add(dto.getSumAccount());
                        tenthOperationReport.setSeventhTenderUsername(dto.getUserName());//第7名用户名
                        tenthOperationReport.setSeventhTenderAmount(dto.getSumAccount());//第7名出借金额(元)
                        break;
                    case 7:
                        tenderAmountSum = tenderAmountSum.add(dto.getSumAccount());
                        tenthOperationReport.setEighthTenderUsername(dto.getUserName());//第8名用户名
                        tenthOperationReport.setEighthTenderAmount(dto.getSumAccount());//第8名出借金额(元)
                        break;
                    case 8:
                        tenderAmountSum = tenderAmountSum.add(dto.getSumAccount());
                        tenthOperationReport.setNinthTenderUsername(dto.getUserName());//第9名用户名
                        tenthOperationReport.setNinthTenderAmount(dto.getSumAccount());//第9名出借金额(元)
                        break;
                    case 9:
                        tenderAmountSum = tenderAmountSum.add(dto.getSumAccount());
                        tenthOperationReport.setTenthTenderUsername(dto.getUserName());//第10名用户名
                        tenthOperationReport.setTenthTenderAmount(dto.getSumAccount());//第10名出借金额(元)
                        break;
                    default:
                        break;

                }

            }

            //10大出借人金额之占比(%)
            BigDecimal tenTenderProportion = tenderAmountSum.divide(sumTenderAmount, 4, BigDecimal.ROUND_HALF_UP).multiply(bigHundred);
            tenthOperationReport.setTenTenderAmount(tenderAmountSum);//10大出借人金额之和(元)
            tenthOperationReport.setTenTenderProportion(tenTenderProportion.setScale(2, BigDecimal.ROUND_HALF_UP));//10大出借人金额之占比(%)
            tenthOperationReport.setOtherTenderAmount(sumTenderAmount.subtract(tenderAmountSum));//其他出借人金额之和(元)
            tenthOperationReport.setOtherTenderProportion(bigHundred.subtract(tenTenderProportion).setScale(2, BigDecimal.ROUND_HALF_UP));//其他出借人金额之和占比（%）

            //查找 最多金用户的年龄和地区
            OperationReportInfoCustomize UserAgeAndAreaDto = this.getUserAgeAndArea(userId);
            if (UserAgeAndAreaDto != null) {

                tenthOperationReport.setMostTenderUserAge(UserAgeAndAreaDto.getDealSum());//最多金用户年龄（岁）
                tenthOperationReport.setMostTenderUserArea(UserAgeAndAreaDto.getTitle());//最多金用户地区
            }
            tenthOperationReport.setMostTenderUsername(tenderUsername);//最多金用户名
            tenthOperationReport.setMostTenderAmount(tenderAmountMoney);//最多金出借金额（元）
        }

        //大赢家，收益最高
        List<OperationReportInfoCustomize> listOneInterestsMost = this.getOneInterestsMost(intervalMonth);
        if (!CollectionUtils.isEmpty(listOneInterestsMost)) {
            OperationReportInfoCustomize interestsMostDto = listOneInterestsMost.get(0);
            userId = interestsMostDto.getUserId();
            tenthOperationReport.setBigMinnerUsername(interestsMostDto.getUserName());//大赢家用户名
            tenthOperationReport.setBigMinnerProfit(interestsMostDto.getSumAccount());//大赢家用户预期收益

            //查找 最多金用户的年龄和地区
            OperationReportInfoCustomize UserAgeAndAreaDto = this.getUserAgeAndArea(userId);
            if (UserAgeAndAreaDto != null) {
                tenthOperationReport.setBigMinnerUserAge(UserAgeAndAreaDto.getDealSum());//大赢家用户年龄
                tenthOperationReport.setBigMinnerUserArea(UserAgeAndAreaDto.getTitle());//大赢家用户地区
            }
        }

        //超活跃，出借笔数最多
        List<OperationReportInfoCustomize> listtOneInvestMost = this.getOneInvestMost(intervalMonth);
        if (!CollectionUtils.isEmpty(listtOneInvestMost)) {
            OperationReportInfoCustomize investMostDto = listtOneInvestMost.get(0);
            userId = investMostDto.getUserId();
            tenthOperationReport.setActiveTenderUsername(investMostDto.getUserName());//超活跃用户名
            tenthOperationReport.setActiveTenderNum(Long.valueOf(investMostDto.getDealSum()));//超活跃用户出借次数

            //查找 最多金用户的年龄和地区
            OperationReportInfoCustomize UserAgeAndAreaDto = this.getUserAgeAndArea(userId);
            if (UserAgeAndAreaDto != null) {
                tenthOperationReport.setActiveTenderUserAge(UserAgeAndAreaDto.getDealSum());//超活跃用户年龄
                tenthOperationReport.setActiveTenderUserArea(UserAgeAndAreaDto.getTitle());//超活跃用户地区
            }
        }

        TenthOperationReportEntity tenthOperationReportEntity = new TenthOperationReportEntity();
        BeanUtils.copyProperties(tenthOperationReport,tenthOperationReportEntity);
        tenthOperationReportEntity.setOperationReportId(operationReportId);//运营报告ID

//        tenthOperationReportMapper.insert(tenthOperationReport);
        tenthOperationReportMongDao.insert(tenthOperationReportEntity);
    }


    /**
     * 业绩总览
     */
    public Map<String, BigDecimal> getPerformanceSum() {
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        List<OperationReportInfoCustomize> listPerformanceSum = operationReportInfoCustomizeMapper.getPerformanceSum();

        if (!CollectionUtils.isEmpty(listPerformanceSum)) {
            for (OperationReportInfoCustomize opear : listPerformanceSum) {
                if ("累计交易总额".equals(opear.getTitle())) {
                    map.put("allAmount", opear.getSumAccount());
                } else if ("累计用户收益".equals(opear.getTitle())) {
                    map.put("allProfit", opear.getSumAccount());
                } else if ("平台注册人数".equals(opear.getTitle())) {
                    map.put("registNum", opear.getSumAccount());
                }
            }

            //null值转换
            this.nullConvertValue(BigDecimal.class, map, "allAmount", "allProfit", "registNum");
        }

        return map;
    }

    /**
     * 当月、季、半年、全年业绩  下面的  成交金额,根据月份计算
     *
     * @param startMonth 开始月份，如果是当月就填 0
     * @param endMonth   结束月份
     * @return
     */
    public List<OperationReportInfoCustomize> getMonthDealMoney(int startMonth, int endMonth) {
        return operationReportInfoCustomizeMapper.getMonthDealMoney(startMonth, endMonth);
    }

    /**
     * 年这个时候到手收益 和 去年这个时候到手收益 和  出借利率
     *
     * @param intervalMonth 今年间隔月份
     * @param startMonth    去年开始月份
     * @param endMonth      去年结束月份
     * @return
     */
    public Map<String, BigDecimal> getRevenueAndYield(int intervalMonth, int startMonth, int endMonth) {
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        List<OperationReportInfoCustomize> listOperationReportInfoCustomize = operationReportInfoCustomizeMapper.getRevenueAndYield(intervalMonth, startMonth, endMonth);
        if (!CollectionUtils.isEmpty(listOperationReportInfoCustomize)) {
            for (OperationReportInfoCustomize opear : listOperationReportInfoCustomize) {
                if ("本次到手收益".equals(opear.getTitle())) {
                    map.put("operationProfit", opear.getSumAccount());
                } else if ("去年本次到手收益".equals(opear.getTitle())) {
                    map.put("lastYearProfit", opear.getSumAccount());
                } else if ("平均预期收益率".equals(opear.getTitle())) {
                    map.put("avgProfit", opear.getSumAccount());
                }
            }

            //null值转换
            this.nullConvertValue(BigDecimal.class, map, "operationProfit", "lastYearProfit", "avgProfit");
        }
        return map;
    }

    /**
     * 充值金额、充值笔数
     *
     * @param intervalMonth 今年间隔月份
     */
    public Map<String, BigDecimal> getRechargeMoneyAndSum(int intervalMonth) {
        Map<String, BigDecimal> map = new HashMap<String, BigDecimal>();
        List<OperationReportInfoCustomize> listRechargeMoneyAndSum = operationReportInfoCustomizeMapper.getRechargeMoneyAndSum(intervalMonth);
        if (!CollectionUtils.isEmpty(listRechargeMoneyAndSum)) {
            for (OperationReportInfoCustomize opear : listRechargeMoneyAndSum) {
                if ("充值金额".equals(opear.getTitle())) {
                    map.put("rechargeMoney", opear.getSumAccount());
                } else if ("充值笔数".equals(opear.getTitle())) {
                    map.put("rechargeCount", opear.getSumAccount());
                }
            }

            //null值转换
            this.nullConvertValue(BigDecimal.class, map, "rechargeMoney", "rechargeCount");
        }
        return map;
    }

    /**
     * 渠道分析 ，成交笔数
     *
     * @param intervalMonth 今年间隔月份
     * @return
     */
    public List<OperationReportInfoCustomize> getCompleteCount(int intervalMonth) {
        List<OperationReportInfoCustomize> listOperation = new ArrayList<>();
        Integer appCount = 0;//app成交笔数
        BigDecimal appDealAmount = BigDecimal.ZERO;
        List<OperationReportInfoCustomize> listCompleteCount = operationReportInfoCustomizeMapper.getCompleteCount(intervalMonth);
        if (!CollectionUtils.isEmpty(listCompleteCount)) {
            OperationReportInfoCustomize dtoApp = new OperationReportInfoCustomize();
            for (OperationReportInfoCustomize opear : listCompleteCount) {
                OperationReportInfoCustomize dto = new OperationReportInfoCustomize();
                if ("IOSAPP".equals(opear.getTitle())) {
                    appCount = appCount + opear.getDealSum();
                    appDealAmount = appDealAmount.add(opear.getSumAccount());
                } else if ("PC".equals(opear.getTitle())) {
                    dto.setTitle("pcDealNum");
                    dto.setDealSum(opear.getDealSum());
                    dto.setSumAccount(opear.getSumAccount());
                } else if ("安卓APP".equals(opear.getTitle())) {
                    appCount = appCount + opear.getDealSum();
                    appDealAmount = appDealAmount.add(opear.getSumAccount());
                } else if ("微信".equals(opear.getTitle())) {
                    dto.setTitle("wechatDealNum");
                    dto.setDealSum(opear.getDealSum());
                    dto.setSumAccount(opear.getSumAccount());
                }
                listOperation.add(dto);
            }
            dtoApp.setTitle("appDealNum");
            dtoApp.setDealSum(appCount);
            dtoApp.setSumAccount(appDealAmount);
            listOperation.add(dtoApp);
        }
        return listOperation;
    }

    /**
     * 借款期限
     *
     * @param intervalMonth 今年间隔月份
     * @return
     */
    public Map<String, Integer> getBorrowPeriod(int intervalMonth) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        Integer sumPeriod = 0;
        Integer dayless30 = 0;
        List<OperationReportInfoCustomize> listBorrowPeriod = operationReportInfoCustomizeMapper.getBorrowPeriod(intervalMonth);
        if (!CollectionUtils.isEmpty(listBorrowPeriod)) {
            for (OperationReportInfoCustomize opear : listBorrowPeriod) {
                sumPeriod = sumPeriod + opear.getDealSum();
                if ("30天".equals(opear.getTitle())) { // 30天
                    map.put("30day", opear.getDealSum());
                } else if ("1个月".equals(opear.getTitle())) { // 1个月
                    map.put("1months", opear.getDealSum());
                } else if ("2个月".equals(opear.getTitle())) { // 2个月
                    map.put("2months", opear.getDealSum());
                } else if ("3个月".equals(opear.getTitle())) { // 3个月
                    map.put("3months", opear.getDealSum());
                } else if ("4个月".equals(opear.getTitle())) { // 4个月
                    map.put("4months", opear.getDealSum());
                } else if ("5个月".equals(opear.getTitle())) { // 5个月
                    map.put("5months", opear.getDealSum());
                } else if ("6个月".equals(opear.getTitle())) { // 6个月
                    map.put("6months", opear.getDealSum());
                } else if ("9个月".equals(opear.getTitle())) { // 9个月
                    map.put("9months", opear.getDealSum());
                } else if ("10个月".equals(opear.getTitle())) { // 10个月
                    map.put("10months", opear.getDealSum());
                } else if ("12个月".equals(opear.getTitle())) { // 12个月
                    map.put("12months", opear.getDealSum());
                } else if ("15个月".equals(opear.getTitle())) { // 15个月
                    map.put("15months", opear.getDealSum());
                } else if ("18个月".equals(opear.getTitle())) { // 18个月
                    map.put("18months", opear.getDealSum());
                } else if ("24个月".equals(opear.getTitle())) { // 24个月
                    map.put("24months", opear.getDealSum());
                } else {//30天以下
                    dayless30 = dayless30+opear.getDealSum();
                    map.put("30dayless", dayless30);
                }
            }

            map.put("sumPeriod", sumPeriod);
            //null值转换
            this.nullConvertValue(Integer.class, map, "30day", "1months", "2months", "3months",
                    "4months", "5months", "6months", "9months", "10months", "12months", "15months", "18months", "24months", "30dayless", "sumPeriod");
        }

        return map;
    }

    /**
     * 用户分析 - 性别分布
     *
     * @param intervalMonth 今年间隔月份
     * @return
     */
    public Map<String, Integer> getSexDistribute(int intervalMonth) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        List<OperationReportInfoCustomize> listSexDistribute = operationReportInfoCustomizeMapper.getSexDistribute(intervalMonth);
        if (!CollectionUtils.isEmpty(listSexDistribute)) {
            for (OperationReportInfoCustomize opear : listSexDistribute) {
                if ("男".equals(opear.getTitle())) {
                    map.put("manTenderNum", opear.getDealSum());
                } else if ("女".equals(opear.getTitle())) {
                    map.put("womanTenderNum", opear.getDealSum());
                }
            }

            //null值转换
            this.nullConvertValue(Integer.class, map, "manTenderNum", "womanTenderNum");
        }
        return map;
    }


    /**
     * 用户分析 - 年龄分布
     *
     * @param intervalMonth 今年间隔月份
     * @return
     */
    public Map<String, Integer> getAgeDistribute(int intervalMonth) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        List<OperationReportInfoCustomize> listAgeDistribute = operationReportInfoCustomizeMapper.getAgeDistribute(intervalMonth);
        if (!CollectionUtils.isEmpty(listAgeDistribute)) {
            for (OperationReportInfoCustomize opear : listAgeDistribute) {
                if ("18-29岁".equals(opear.getTitle())) {
                    map.put("18-29", opear.getDealSum());
                } else if ("30-39岁".equals(opear.getTitle())) {
                    map.put("30-39", opear.getDealSum());
                } else if ("40-49岁".equals(opear.getTitle())) {
                    map.put("40-49", opear.getDealSum());
                } else if ("50-59岁".equals(opear.getTitle())) {
                    map.put("50-59", opear.getDealSum());
                } else if ("60岁以上".equals(opear.getTitle())) {
                    map.put("60-", opear.getDealSum());
                }
            }

            //null值转换
            this.nullConvertValue(Integer.class, map, "18-29", "30-39", "40-49", "50-59", "60-");
        }
        return map;
    }

    /**
     * 用户分析 - 金额分布
     *
     * @param intervalMonth 今年间隔月份
     * @return
     */
    public Map<String, Integer> getMoneyDistribute(int intervalMonth) {
        Map<String, Integer> map = new HashMap<String, Integer>();
        List<OperationReportInfoCustomize> listMoneyDistribute = operationReportInfoCustomizeMapper.getMoneyDistribute(intervalMonth);
        if (!CollectionUtils.isEmpty(listMoneyDistribute)) {
            for (OperationReportInfoCustomize opear : listMoneyDistribute) {
                if ("1万以下".equals(opear.getTitle())) {
                    map.put("0-1", opear.getDealSum());
                } else if ("1万-5万".equals(opear.getTitle())) {
                    map.put("1-5", opear.getDealSum());
                } else if ("5万-10万".equals(opear.getTitle())) {
                    map.put("5-10", opear.getDealSum());
                } else if ("10万-50万".equals(opear.getTitle())) {
                    map.put("10-50", opear.getDealSum());
                } else if ("50万以上".equals(opear.getTitle())) {
                    map.put("50-", opear.getDealSum());
                }
            }

            //null值转换
            this.nullConvertValue(Integer.class, map, "0-1", "1-5", "5-10", "10-50", "50-");
        }
        return map;
    }

    /**
     * 十大出借人
     *
     * @param intervalMonth 今年间隔月份
     * @return
     */
    public List<OperationReportInfoCustomize> getTenMostMoney(int intervalMonth) {

        return operationReportInfoCustomizeMapper.getTenMostMoney(intervalMonth);
    }

    /**
     * 超活跃，出借笔数最多
     *
     * @param intervalMonth 今年间隔月份
     * @return
     */
    public List<OperationReportInfoCustomize> getOneInvestMost(int intervalMonth) {
        return operationReportInfoCustomizeMapper.getOneInvestMost(intervalMonth);
    }

    /**
     * 大赢家，收益最高
     *
     * @param intervalMonth 今年间隔月份
     * @return
     */
    public List<OperationReportInfoCustomize> getOneInterestsMost(int intervalMonth) {
        return operationReportInfoCustomizeMapper.getOneInterestsMost(intervalMonth);
    }

    /**
     * 通过用户ID查询 用户年龄，用户地区
     *
     * @param userId 用户ID
     * @return
     */
    public OperationReportInfoCustomize getUserAgeAndArea(Integer userId) {
        return operationReportInfoCustomizeMapper.getUserAgeAndArea(userId);
    }

    /**
     * set 渠道分析
     *
     * @param object        halfYearOperationReport YearOperationReport
     * @param intervalMonth 间隔月份
     * @return 成交笔数总数
     */
    public Integer setCompleteCount(Object object, Integer intervalMonth) {
        Integer sumCompleteCount = 0;//全年成交笔数
        Integer appDealNum = 0;//App成交笔数
        Integer pcDealNum = 0;//pc成交笔数
        Integer wechatDealNum = 0;//微信成交笔数
        BigDecimal appDealProportion = BigDecimal.ZERO;// app成交占比(%)
        BigDecimal pcDealProportion = BigDecimal.ZERO;// pc成交占比(%)
        BigDecimal wechatDealProportion = BigDecimal.ZERO;// 微信成交占比(%)
        BigDecimal appDealAmount = BigDecimal.ZERO;// app成交金额
        BigDecimal pcDealAmount = BigDecimal.ZERO;// pc成交金额
        BigDecimal wechatDealAmount = BigDecimal.ZERO;// 微信成交金额
        BigDecimal dealAmountSum = BigDecimal.ZERO;// 成交金额总
        BigDecimal dealAmountPercent = new BigDecimal(100);// 成交金额占比差额计算

        //渠道分析
        List<OperationReportInfoCustomize> listgetCompleteCount = getCompleteCount(intervalMonth);
        for (OperationReportInfoCustomize completeCountDto : listgetCompleteCount) {
            if ("pcDealNum".equals(completeCountDto.getTitle())) {
                pcDealNum = completeCountDto.getDealSum();//pc成交笔数
                pcDealAmount = completeCountDto.getSumAccount();//pc成交金额

            } else if ("wechatDealNum".equals(completeCountDto.getTitle())) {
                wechatDealNum = completeCountDto.getDealSum();//微信成交笔数
                wechatDealAmount = completeCountDto.getSumAccount();//微信成交金额

            } else if ("appDealNum".equals(completeCountDto.getTitle())) {
                appDealNum = completeCountDto.getDealSum();//App成交笔数
                appDealAmount = completeCountDto.getSumAccount();//app成交金额

            }
        }
        //全年成交笔数
        sumCompleteCount = pcDealNum + wechatDealNum + appDealNum;
        //校验 百分比是否等于100%
        bigflag = checkPercent(sumCompleteCount, pcDealNum, wechatDealNum, appDealNum);
        appDealProportion = assignCompute(appDealNum, sumCompleteCount, bigflag).setScale(2, BigDecimal.ROUND_HALF_UP);// app成交占比(%)
        pcDealProportion = assignCompute(pcDealNum, sumCompleteCount, bigflag).setScale(2, BigDecimal.ROUND_HALF_UP);// pc成交占比(%)
        wechatDealProportion = assignCompute(wechatDealNum, sumCompleteCount, bigflag).setScale(2, BigDecimal.ROUND_HALF_UP);// 微信成交占比(%)

        if (object instanceof HalfYearOperationReport) { // 上半年
            HalfYearOperationReport halfYearOperationReport = (HalfYearOperationReport) object;
            halfYearOperationReport.setHalfYearAppDealNum(appDealNum);//上半年度APP成交笔数
            halfYearOperationReport.setHalfYearAppDealProportion(appDealProportion);//上半年度app成交占比(%)
            halfYearOperationReport.setHalfYearWechatDealNum(wechatDealNum);//上半年度微信成交笔数
            halfYearOperationReport.setHalfYearWechatDealProportion(wechatDealProportion);//上半年度微信成交占比(%)
            halfYearOperationReport.setHalfYearPcDealNum(pcDealNum);//上半年度PC成交笔数
            halfYearOperationReport.setHalfYearPcDealProportion(pcDealProportion);//上半年度PC成交占比(%)

        } else if (object instanceof YearOperationReport) { // 全年
            //全年成交笔数
            dealAmountSum = pcDealAmount.add(wechatDealAmount).add(appDealAmount);

            dealAmountPercent = dealAmountPercent.subtract(appDealAmount.divide(dealAmountSum, 4, BigDecimal.ROUND_HALF_UP).multiply(bigHundred))
                    .subtract(wechatDealAmount.divide(dealAmountSum, 4, BigDecimal.ROUND_HALF_UP).multiply(bigHundred));

            YearOperationReport yearOperationReport = (YearOperationReport) object;
            yearOperationReport.setYearAppDealNum(appDealNum);//全年度APP成交笔数
            yearOperationReport.setYearAppDealProportion(appDealProportion);//全年度app成交占比(%)
            yearOperationReport.setYearWechatDealNum(wechatDealNum);//全年度微信成交笔数
            yearOperationReport.setYearWechatDealProportion(wechatDealProportion);//全年度微信成交占比(%)
            yearOperationReport.setYearPcDealNum(pcDealNum);//全年度PC成交笔数
            yearOperationReport.setYearPcDealProportion(pcDealProportion);//全年度PC成交占比(%)

            yearOperationReport.setYearAppDealAmount(appDealAmount);//全年度APP成交金额
            yearOperationReport.setYearAppAmountProportion(appDealAmount.divide(dealAmountSum, 4, BigDecimal.ROUND_HALF_UP).multiply(bigHundred));//全年度app成交金额占比(%)
            yearOperationReport.setYearWechatDealAmount(wechatDealAmount);//全年度微信成交金额
            yearOperationReport.setYearWechatAmountProportion(wechatDealAmount.divide(dealAmountSum, 4, BigDecimal.ROUND_HALF_UP).multiply(bigHundred));//全年度微信成交金额占比(%)
            yearOperationReport.setYearPcDealAmount(pcDealAmount);//全年度PC成交金额
            yearOperationReport.setYearPcAmountProportion(dealAmountPercent);//全年度PC成交金额占比(%)
        }

        return sumCompleteCount;
    }

    /**
     * set 借款期限
     *
     * @param object        halfYearOperationReport YearOperationReport
     * @param intervalMonth
     */
    public void setHalfYearAndYearLoanTime(Object object, Integer intervalMonth) {
        Map<String, Integer> map = this.getBorrowPeriod(intervalMonth);
        if (CollectionUtils.isEmpty(map)) {
            return;
        }

        Integer dayless30 = map.get("30dayless");
        Integer day30 = map.get("30day");
        Integer months1 = map.get("1months");
        Integer months2 = map.get("2months");
        Integer months3 = map.get("3months");
        Integer months4 = map.get("4months");
        Integer months5 = map.get("5months");
        Integer months6 = map.get("6months");
        Integer months9 = map.get("9months");
        Integer months10 = map.get("10months");
        Integer months12 = map.get("12months");
        Integer months15 = map.get("15months");
        Integer months18 = map.get("18months");
        Integer months24 = map.get("24months");

        Integer sumPeriod = map.get("sumPeriod");

        //校验 百分比是否等于100%
        bigflag = checkPercent(sumPeriod, dayless30, day30, months1, months2, months3, months4, months5, months6, months9, months10,
                months12, months15, months18, months24);

        if (object instanceof HalfYearOperationReport) { //半年

            HalfYearOperationReport halfYearOperationReport = (HalfYearOperationReport) object;

            halfYearOperationReport.setLessThirtyDayNum(dayless30);//30天以下
            halfYearOperationReport.setLessThirtyDayProportion(assignCompute(dayless30, sumPeriod, bigflag));//30天以下占比
            halfYearOperationReport.setThirtyDayNum(day30);//30天
            halfYearOperationReport.setThirtyDayProportion(assignCompute(day30, sumPeriod, bigflag));//30天占比
            halfYearOperationReport.setOneMonthNum(months1);//1个月
            halfYearOperationReport.setOneMonthProportion(assignCompute(months1, sumPeriod, bigflag));//1个月占比
            halfYearOperationReport.setTwoMonthNum(months2);//2个月
            halfYearOperationReport.setTwoMonthProportion(assignCompute(months2, sumPeriod, bigflag));//2个月占比
            halfYearOperationReport.setThreeMonthNum(months3);//3个月
            halfYearOperationReport.setThreeMonthProportion(assignCompute(months3, sumPeriod, bigflag));//3个月占比
            halfYearOperationReport.setFourMonthNum(months4);//4个月
            halfYearOperationReport.setFourMonthProportion(assignCompute(months4, sumPeriod, bigflag));//4个月占比
            halfYearOperationReport.setFiveMonthNum(months5);//5个月
            halfYearOperationReport.setFiveMonthProportion(assignCompute(months5, sumPeriod, bigflag));//5个月占比
            halfYearOperationReport.setSixMonthNum(months6);//6个月
            halfYearOperationReport.setSixMonthProportion(assignCompute(months6, sumPeriod, bigflag));//6个月占比
            halfYearOperationReport.setNineMonthNum(months9);//9个月
            halfYearOperationReport.setNineMonthProportion(assignCompute(months9, sumPeriod, bigflag));//9个月占比
            halfYearOperationReport.setTenMonthNum(months10);//10个月
            halfYearOperationReport.setTenMonthProportion(assignCompute(months10, sumPeriod, bigflag));//10个月占比
            halfYearOperationReport.setTwelveMonthNum(months12);//12个月
            halfYearOperationReport.setTwelveMonthProportion(assignCompute(months12, sumPeriod, bigflag));//12个月占比
            halfYearOperationReport.setFifteenMonthNum(months15);//15个月
            halfYearOperationReport.setFifteenMonthProportion(assignCompute(months15, sumPeriod, bigflag));//15个月占比
            halfYearOperationReport.setEighteenMonthNum(months18);//18个月
            halfYearOperationReport.setEighteenMonthProportion(assignCompute(months18, sumPeriod, bigflag));//18个月占比
            halfYearOperationReport.setTwentyFourMonthNum(months24);//24个月
            halfYearOperationReport.setTwentyFourMonthProportion(assignCompute(months24, sumPeriod, bigflag));//24个月占比

        } else if (object instanceof YearOperationReport) { //全年

            YearOperationReport yearOperationReport = (YearOperationReport) object;

            yearOperationReport.setLessThirtyDayNum(dayless30);//30天以下
            yearOperationReport.setLessThirtyDayProportion(assignCompute(dayless30, sumPeriod, bigflag));//30天以下占比
            yearOperationReport.setThirtyDayNum(day30);//30天
            yearOperationReport.setThirtyDayProportion(assignCompute(day30, sumPeriod, bigflag));//30天占比
            yearOperationReport.setOneMonthNum(months1);//1个月
            yearOperationReport.setOneMonthProportion(assignCompute(months1, sumPeriod, bigflag));//1个月占比
            yearOperationReport.setTwoMonthNum(months2);//2个月
            yearOperationReport.setTwoMonthProportion(assignCompute(months2, sumPeriod, bigflag));//2个月占比
            yearOperationReport.setThreeMonthNum(months3);//3个月
            yearOperationReport.setThreeMonthProportion(assignCompute(months3, sumPeriod, bigflag));//3个月占比
            yearOperationReport.setFourMonthNum(months4);//4个月
            yearOperationReport.setFourMonthProportion(assignCompute(months4, sumPeriod, bigflag));//4个月占比
            yearOperationReport.setFiveMonthNum(months5);//5个月
            yearOperationReport.setFiveMonthProportion(assignCompute(months5, sumPeriod, bigflag));//5个月占比
            yearOperationReport.setSixMonthNum(months6);//6个月
            yearOperationReport.setSixMonthProportion(assignCompute(months6, sumPeriod, bigflag));//6个月占比
            yearOperationReport.setNineMonthNum(months9);//9个月
            yearOperationReport.setNineMonthProportion(assignCompute(months9, sumPeriod, bigflag));//9个月占比
            yearOperationReport.setTenMonthNum(months10);//10个月
            yearOperationReport.setTenMonthProportion(assignCompute(months10, sumPeriod, bigflag));//10个月占比
            yearOperationReport.setTwelveMonthNum(months12);//12个月
            yearOperationReport.setTwelveMonthProportion(assignCompute(months12, sumPeriod, bigflag));//12个月占比
            yearOperationReport.setFifteenMonthNum(months15);//15个月
            yearOperationReport.setFifteenMonthProportion(assignCompute(months15, sumPeriod, bigflag));//15个月占比
            yearOperationReport.setEighteenMonthNum(months18);//18个月
            yearOperationReport.setEighteenMonthProportion(assignCompute(months18, sumPeriod, bigflag));//18个月占比
            yearOperationReport.setTwentyFourMonthNum(months24);//24个月
            yearOperationReport.setTwentyFourMonthProportion(assignCompute(months24, sumPeriod, bigflag));//24个月占比
        }

    }

    /**
     * 将百分比的差额 平摊出去
     *
     * @param number 个数
     * @param sum    总数
     * @param differ 差值
     * @return
     */
    public static BigDecimal assignCompute(Integer number, Integer sum, BigDecimal differ) {
        BigDecimal bigResult = BigDecimal.ZERO;
        //判断 金额的百分比 结果是否大于0
        if (computeDealProportion(number, sum).compareTo(bigZer0) > 0) {
            // differ = 百分比的差值， 0.01 或者 -0.01
            if (differ.compareTo(bigZer0) != 0) {
                bigResult = computeDealProportion(number, sum).subtract(differ);
            } else {
                bigResult = computeDealProportion(number, sum);
            }
            //用于全局控制的 百分比差额
            bigflag = BigDecimal.ZERO;
        }
        return bigResult.setScale(2, BigDecimal.ROUND_DOWN);
    }

    /**
     * 计算金额的百分比
     *
     * @param number 个数
     * @param sum    总数
     * @return
     */
    public static BigDecimal computeDealProportion(Integer number, Integer sum) {
        if (sum <= 0) {
            return BigDecimal.ZERO;
        }
        // 个数 除以 总数 = 百分比， 然后将百分比乘以100转换为% ，再保留两位小数
        return new BigDecimal(number).divide(new BigDecimal(sum), 4, BigDecimal.ROUND_DOWN).multiply(bigHundred);

    }

    /**
     * 检查 计算出来的百分比是否等于100，如果不等于就将 第一个百分比的结果减去差额，但因为不能确认第一个值是否为0，就将每个值判断
     *
     * @param sum
     * @param num
     * @return
     */
    public static BigDecimal checkPercent(Integer sum, Integer... num) {
        BigDecimal parcent = BigDecimal.ZERO;
        for (Integer i : num) {
            parcent = parcent.add(new BigDecimal(i).divide(new BigDecimal(sum), 4, BigDecimal.ROUND_DOWN).multiply(bigHundred));
        }
        return parcent.subtract(bigHundred);
    }

    /**
     * 计算同比增长
     * 同比增长率=（本期额-去年同期额）/去年同期额*100%
     *
     * @param currentPeriod  本期额
     * @param lastYearPeriod 去年同期额
     * @return
     */
    public BigDecimal computeRiseProportion(BigDecimal currentPeriod, BigDecimal lastYearPeriod) {
        BigDecimal result = BigDecimal.ZERO;
        if (lastYearPeriod.compareTo(BigDecimal.ZERO) <= 0) {
            return result;
        }
        result = (currentPeriod.subtract(lastYearPeriod)).divide(lastYearPeriod, 4, BigDecimal.ROUND_DOWN).multiply(bigHundred);

        return result;

    }

    /**
     * 空值转换
     *
     * @param map
     * @param param
     */
    public <T> void nullConvertValue(T t, Map map, String... param) {

        for (String str : param) {
            if (map.get(str) == null) {
                if (String.class.equals(t)) {
                    map.put(str, "0");
                } else if (Integer.class.equals(t)) {
                    map.put(str, 0);
                } else if (BigDecimal.class.equals(t)) {
                    map.put(str, new BigDecimal(0.00));
                }
            }
        }
    }
}
