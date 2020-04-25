/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.manager.content.operationreport;

import com.hyjf.common.enums.utils.DateEnum;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.operationreport.dao.OperationReportColumnMongDao;
import com.hyjf.mongo.operationreport.entity.HalfYearOperationReportEntity;
import com.hyjf.mongo.operationreport.entity.MonthlyOperationReportEntity;
import com.hyjf.mongo.operationreport.entity.QuarterOperationReportEntity;
import com.hyjf.mongo.operationreport.entity.YearOperationReportEntity;
import com.hyjf.mybatis.model.auto.HalfYearOperationReport;
import com.hyjf.mybatis.model.auto.MonthlyOperationReport;
import com.hyjf.mybatis.model.auto.QuarterOperationReport;
import com.hyjf.mybatis.model.auto.YearOperationReport;
import com.hyjf.mybatis.model.customize.OperationReportInfoCustomize;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计运营报告详情（月，季度，半年，全年）
 *
 * @author yinhui
 * @version StatisticsOperationReportInfoServiceImpl, v0.1 2018/6/19 17:57
 */
@Service
public class StatisticsOperationReportInfoServiceImpl extends StatisticsOperationReportBase implements StatisticsOperationReportInfoService {

    //保存月度运营报告
    @Override
    public void setMonthReport(String year, String month) throws Exception {

        BigDecimal currentMonthDealMoney = BigDecimal.ZERO;//本月成交金额
        BigDecimal lastMonthDealMoney = BigDecimal.ZERO;//去年本月成交金额
        BigDecimal dealMoneyPercent = BigDecimal.ZERO;//今年成交金额 和 去年本月成交金额 同比增长
        BigDecimal operationProfit = BigDecimal.ZERO;//本月赚取收益
        BigDecimal lastYearProfit = BigDecimal.ZERO;//去年本月赚取收益
        BigDecimal profitIncrease = BigDecimal.ZERO;//今年赚取收益 和 去年本月赚取收益 同比增长
        BigDecimal avgProfit = BigDecimal.ZERO;// 本月平均年利率
        Integer sumCompleteCount = 0;//本月成交笔数
        Integer appDealNum = 0;//App成交笔数
        Integer pcDealNum = 0;//pc成交笔数
        Integer wechatDealNum = 0;//微信成交笔数
        BigDecimal appDealProportion = BigDecimal.ZERO;// app成交占比(%)
        BigDecimal pcDealProportion = BigDecimal.ZERO;// pc成交占比(%)
        BigDecimal wechatDealProportion = BigDecimal.ZERO;// 微信成交占比(%)

        //因为是从1月份生成12月份的
        if ("1".equals(month)) {
            year = String.valueOf(Integer.valueOf(year) - 1);
        }

        StringBuilder strCnName = new StringBuilder();//中文标题
        StringBuilder strEnName = new StringBuilder();//英文标题

        String monthValue = DateEnum.getValue(String.valueOf(Integer.valueOf(month) - 1));

        strCnName.append(year).append("年").append(Integer.valueOf(month) - 1).append("月");
        strEnName.append("Monthly Operation Report of ").append(monthValue).append(" ").append(year).toString();

        //业绩总览
        Map<String, BigDecimal> mapPerformanceSum = getPerformanceSum();
        if (CollectionUtils.isEmpty(mapPerformanceSum)) {
            throw new NullPointerException();
        }

        //本月成交金额
        List<OperationReportInfoCustomize> listMonthDealMoney = getMonthDealMoney(0, 1);
        if (CollectionUtils.isEmpty(listMonthDealMoney)) {
            throw new NullPointerException();
        }
        currentMonthDealMoney = listMonthDealMoney.get(0).getSumAccount();

        //去年本月成交金额
        List<OperationReportInfoCustomize> listLastMonthDealMoney = getMonthDealMoney(12, 13);
        if (CollectionUtils.isEmpty(listMonthDealMoney)) {
            throw new NullPointerException();
        }
        lastMonthDealMoney = listLastMonthDealMoney.get(0).getSumAccount();

        //今年成交金额 和 去年本月成交金额 同比增长 百分比
        dealMoneyPercent = this.computeRiseProportion(currentMonthDealMoney, lastMonthDealMoney);

        //本月赚取收益,去年本月赚取收益,本月平均年率
        Map<String, BigDecimal> mapRevenueAndYield = getRevenueAndYield(1, 12, 13);
        if (CollectionUtils.isEmpty(mapRevenueAndYield)) {
            throw new NullPointerException();
        }
        operationProfit = mapRevenueAndYield.get("operationProfit");//本月赚钱的收益
        lastYearProfit = mapRevenueAndYield.get("lastYearProfit");//去年本月赚取收益

        //同比
        profitIncrease = this.computeRiseProportion(operationProfit, lastYearProfit);
        avgProfit = mapRevenueAndYield.get("avgProfit").setScale(2, BigDecimal.ROUND_DOWN);//本月平均年率

        List<OperationReportInfoCustomize> listgetCompleteCount = getCompleteCount(1);
        for (OperationReportInfoCustomize completeCountDto : listgetCompleteCount) {
            if ("pcDealNum".equals(completeCountDto.getTitle())) {
                pcDealNum = completeCountDto.getDealSum();//pc成交笔数

            } else if ("wechatDealNum".equals(completeCountDto.getTitle())) {

                wechatDealNum = completeCountDto.getDealSum();//微信成交笔数
            } else if ("appDealNum".equals(completeCountDto.getTitle())) {

                appDealNum = completeCountDto.getDealSum();//App成交笔数
            }
        }
        //本月成交笔数
        sumCompleteCount = pcDealNum + wechatDealNum + appDealNum;
        //校验 百分比是否等于100%
        bigflag = checkPercent(sumCompleteCount, pcDealNum, wechatDealNum, appDealNum);
        appDealProportion = assignCompute(appDealNum, sumCompleteCount, bigflag);// app成交占比(%)
        pcDealProportion = assignCompute(pcDealNum, sumCompleteCount, bigflag);// pc成交占比(%)
        wechatDealProportion = assignCompute(wechatDealNum, sumCompleteCount, bigflag);// 微信成交占比(%)

        //保存 运营报告显示栏
        String operationReportId = this.saveOperationReport(strCnName.toString(),
                strEnName.toString(),
                1, mapPerformanceSum.get("allAmount"), mapPerformanceSum.get("allProfit"), mapPerformanceSum.get("registNum"), sumCompleteCount,
                currentMonthDealMoney, operationProfit, year);

        //保存 月度运营报告
        this.saveMonthlyOperationReport(operationReportId, strCnName.toString(), strEnName.toString(), Integer.valueOf(month) - 1,
                lastMonthDealMoney, dealMoneyPercent, lastYearProfit, profitIncrease, avgProfit, appDealNum, appDealProportion,
                wechatDealNum, wechatDealProportion, pcDealNum, pcDealProportion);

        //保存 用户分析
        this.saveUserOperationReport(operationReportId, 1, 1);

        //保存 年度之最-十大出借人
        this.saveTenthOperationReport(operationReportId, 1, 1, currentMonthDealMoney);
    }


    /**
     * 保存 月度运营报告
     *
     * @param operationReportId         运营报告ID
     * @param cnName                    中文标题
     * @param enName                    英文标题
     * @param month                     月份
     * @param lastYearMonthAmount       去年本月成交金额（元）
     * @param amountIncrease            成交金额同比增长(%)
     * @param lastYearMonthProfit       去年本月赚取收益（元）
     * @param profitIncrease            收益同比增长(%)
     * @param monthAvgProfit            本月平均出借利率(%)
     * @param monthAppDealNum           本月APP成交笔数
     * @param monthAppDealProportion    本月app成交占比(%)
     * @param monthWechatDealNum        本月微信成交笔数
     * @param monthWechatDealProportion 本月微信成交占比(%)
     * @param MonthPcDealNum            本月PC成交笔数
     * @param monthPcDealProportion     本月PC成交占比(%)
     */
    public void saveMonthlyOperationReport(String operationReportId, String cnName, String enName, Integer month,
                                           BigDecimal lastYearMonthAmount, BigDecimal amountIncrease,
                                           BigDecimal lastYearMonthProfit, BigDecimal profitIncrease, BigDecimal monthAvgProfit,
                                           Integer monthAppDealNum, BigDecimal monthAppDealProportion,
                                           Integer monthWechatDealNum, BigDecimal monthWechatDealProportion,
                                           Integer MonthPcDealNum, BigDecimal monthPcDealProportion) {
        MonthlyOperationReport monthlyOperationReport = new MonthlyOperationReport();
//        monthlyOperationReport.setOperationReportId(operationReportId);//运营报告ID
        monthlyOperationReport.setCnName(cnName);//中文标题
        monthlyOperationReport.setEnName(enName);//英文标题
        monthlyOperationReport.setMonth(month);//月份
        monthlyOperationReport.setLastYearMonthAmount(lastYearMonthAmount.setScale(2, BigDecimal.ROUND_HALF_UP));//去年本月成交金额（元）
        monthlyOperationReport.setAmountIncrease(amountIncrease.setScale(2, BigDecimal.ROUND_HALF_UP));//成交金额同比增长(%)
        monthlyOperationReport.setLastYearMonthProfit(lastYearMonthProfit.setScale(2, BigDecimal.ROUND_HALF_UP));//去年本月赚取收益（元）
        monthlyOperationReport.setProfitIncrease(profitIncrease.setScale(2, BigDecimal.ROUND_HALF_UP));//收益同比增长(%)
        monthlyOperationReport.setMonthAvgProfit(monthAvgProfit.setScale(2, BigDecimal.ROUND_HALF_UP));//本月平均出借利率(%)
        monthlyOperationReport.setMonthAppDealNum(monthAppDealNum);//本月APP成交笔数
        monthlyOperationReport.setMonthAppDealProportion(monthAppDealProportion.setScale(2, BigDecimal.ROUND_HALF_UP));//本月app成交占比(%)
        monthlyOperationReport.setMonthWechatDealNum(monthWechatDealNum);//本月微信成交笔数
        monthlyOperationReport.setMonthWechatDealProportion(monthWechatDealProportion.setScale(2, BigDecimal.ROUND_HALF_UP));//本月微信成交占比(%)
        monthlyOperationReport.setMonthPcDealNum(MonthPcDealNum);//本月PC成交笔数
        monthlyOperationReport.setMonthPcDealProportion(monthPcDealProportion.setScale(2, BigDecimal.ROUND_HALF_UP));//本月PC成交占比(%)
        monthlyOperationReport.setCreateTime(GetDate.getNowTime10());
        monthlyOperationReport.setCreateUserId(1);

        MonthlyOperationReportEntity monthlyOperationReportEntity = new MonthlyOperationReportEntity();
        BeanUtils.copyProperties(monthlyOperationReport, monthlyOperationReportEntity);
        monthlyOperationReportEntity.setOperationReportId(operationReportId);//运营报告ID

//        monthlyOperationReportMapper.insert(monthlyOperationReport);
        monthlyOperationReportMongDao.insert(monthlyOperationReportEntity);

    }


    //保存季度运营报告
    @Override
    public void setQuarterReport(String year, String month) throws Exception {

        BigDecimal quarterDealMoney = BigDecimal.ZERO;//本季度成交金额 （3个月份）
        BigDecimal newQuarterDealMoney = BigDecimal.ZERO;//本月成交金额 （3月份）
        BigDecimal agoCurrentQuarterDealMoney = BigDecimal.ZERO;//前月成交金额 （2月份）
        BigDecimal beforeCurrentQuarterDealMoney = BigDecimal.ZERO;//前前月成交金额 （1月份）
        BigDecimal lastQuarterDealMoney = BigDecimal.ZERO;//去年本季度成交金额
        BigDecimal dealQuarterPercent = BigDecimal.ZERO;//今年成交金额 和 去年本月成交金额 同比增长
        BigDecimal operationProfit = BigDecimal.ZERO;//本月赚取收益
        BigDecimal lastYearProfit = BigDecimal.ZERO;//去年本月赚取收益
        BigDecimal profitIncrease = BigDecimal.ZERO;//今年赚取收益 和 去年本月赚取收益 同比增长
        BigDecimal avgProfit = BigDecimal.ZERO;// 本月平均年利率
        Integer quarterType = 0;//季度类型(1.一季度2.二季度3.三季度4.四季度)
        Integer sumCompleteCount = 0;//本月成交笔数
        Integer appDealNum = 0;//App成交笔数
        Integer pcDealNum = 0;//pc成交笔数
        Integer wechatDealNum = 0;//微信成交笔数
        BigDecimal appDealProportion = BigDecimal.ZERO;// app成交占比(%)
        BigDecimal pcDealProportion = BigDecimal.ZERO;// pc成交占比(%)
        BigDecimal wechatDealProportion = BigDecimal.ZERO;// 微信成交占比(%)

        //因为是从1月份生成12月份的
        if ("1".equals(month)) {
            year = String.valueOf(Integer.valueOf(year) - 1);
        }

        StringBuilder strCnName = new StringBuilder();//中文标题
        StringBuilder strEnName = new StringBuilder();//英文标题

        String monthValue = DateEnum.getValue(String.valueOf(Integer.valueOf(month) - 1));

        if ((Integer.valueOf(month) - 1) == 3) {
            quarterType = 1;
            strCnName.append(year).append("年").append("第一季度");
            strEnName.append("The First Quarter Operation Report of ").append(year).toString();
        } else {
            quarterType = 3;
            strCnName.append(year).append("年").append("第三季度");
            strEnName.append("The third Quarter Operation Report of ").append(year).toString();
        }

        //业绩总览
        Map<String, BigDecimal> mapPerformanceSum = getPerformanceSum();
        if (CollectionUtils.isEmpty(mapPerformanceSum)) {
            throw new NullPointerException();
        }

        //今年三个月成交金额
        List<OperationReportInfoCustomize> listMonthDealMoney = getMonthDealMoney(0, 3);
        if (CollectionUtils.isEmpty(listMonthDealMoney)) {
            throw new NullPointerException();
        }
        newQuarterDealMoney = listMonthDealMoney.get(2).getSumAccount();
        agoCurrentQuarterDealMoney = listMonthDealMoney.get(1).getSumAccount();
        beforeCurrentQuarterDealMoney = listMonthDealMoney.get(0).getSumAccount();
        quarterDealMoney = newQuarterDealMoney.add(agoCurrentQuarterDealMoney).add(beforeCurrentQuarterDealMoney);

        //去年三个月成交金额
        List<OperationReportInfoCustomize> listLastMonthDealMoney = getMonthDealMoney(12, 15);
        if (CollectionUtils.isEmpty(listMonthDealMoney)) {
            throw new NullPointerException();
        }
        for (OperationReportInfoCustomize dto : listLastMonthDealMoney) {
            lastQuarterDealMoney = lastQuarterDealMoney.add(dto.getSumAccount());
        }

        //今年季度成交金额 和 去年季度成交金额 同比增长 百分比
        dealQuarterPercent = this.computeRiseProportion(quarterDealMoney, lastQuarterDealMoney);

        //本季度赚取收益,去年本季度赚取收益,本季度平均年率
        Map<String, BigDecimal> mapRevenueAndYield = getRevenueAndYield(3, 12, 15);
        if (CollectionUtils.isEmpty(mapRevenueAndYield)) {
            throw new NullPointerException();
        }
        operationProfit = mapRevenueAndYield.get("operationProfit");//本季度赚钱的收益
        lastYearProfit = mapRevenueAndYield.get("lastYearProfit");//去年本季度赚取收益

        //同比
        avgProfit = mapRevenueAndYield.get("avgProfit").setScale(2, BigDecimal.ROUND_DOWN);//本季度平均年率
        profitIncrease = this.computeRiseProportion(operationProfit, lastYearProfit);

        //渠道分析
        List<OperationReportInfoCustomize> listgetCompleteCount = getCompleteCount(3);
        for (OperationReportInfoCustomize completeCountDto : listgetCompleteCount) {
            if ("pcDealNum".equals(completeCountDto.getTitle())) {
                pcDealNum = completeCountDto.getDealSum();//pc成交笔数

            } else if ("wechatDealNum".equals(completeCountDto.getTitle())) {

                wechatDealNum = completeCountDto.getDealSum();//微信成交笔数
            } else if ("appDealNum".equals(completeCountDto.getTitle())) {

                appDealNum = completeCountDto.getDealSum();//App成交笔数
            }
        }
        //本季度成交笔数
        sumCompleteCount = pcDealNum + wechatDealNum + appDealNum;
        //校验 百分比是否等于100%
        bigflag = checkPercent(sumCompleteCount, pcDealNum, wechatDealNum, appDealNum);
        appDealProportion = assignCompute(appDealNum, sumCompleteCount, bigflag);// app成交占比(%)
        pcDealProportion = assignCompute(pcDealNum, sumCompleteCount, bigflag);// pc成交占比(%)
        wechatDealProportion = assignCompute(wechatDealNum, sumCompleteCount, bigflag);// 微信成交占比(%)

        //保存 运营报告显示栏
        String operationReportId = this.saveOperationReport(strCnName.toString(),
                strEnName.toString(),
                2, mapPerformanceSum.get("allAmount"), mapPerformanceSum.get("allProfit"), mapPerformanceSum.get("registNum"), sumCompleteCount,
                quarterDealMoney, operationProfit, year);

        //保存 季度运营报告
        this.saveQuarterOperationReport(operationReportId, strCnName.toString(), strEnName.toString(), quarterType,
                beforeCurrentQuarterDealMoney, agoCurrentQuarterDealMoney, newQuarterDealMoney,
                lastQuarterDealMoney, dealQuarterPercent, lastYearProfit, profitIncrease,
                avgProfit, appDealNum, appDealProportion,
                wechatDealNum, wechatDealProportion, pcDealNum, pcDealProportion);

        //保存 用户分析
        this.saveUserOperationReport(operationReportId, 2, 3);

        //保存 年度之最-十大出借人
        this.saveTenthOperationReport(operationReportId, 2, 3, quarterDealMoney);
    }

    /**
     * 保存季度运营报告
     *
     * @param operationReportId           运营报告ID
     * @param cnName
     * @param enName
     * @param type                        季度类型(1.一季度2.二季度3.三季度4.四季度)
     * @param firstMonthAmount            第一月成交金额（元）
     * @param secondMonthAmount           第二月成交金额（元）
     * @param thirdMonthAmount            第三月成交金额（元）
     * @param lastYearQuarterAmount       去年本季度累计成交金额（元）
     * @param amountIncrease              成交金额同比增长(%)
     * @param lastYearQuarterProfit       去年季度累计赚取收益（元）
     * @param profitIncrease              收益同比增长(%)
     * @param quarterAvgProfit            本季度平均出借利率(%)
     * @param quarterAppDealNum           本季度APP成交笔数
     * @param quarterAppDealProportion    本季度app成交占比(%)
     * @param quarterWechatDealNum        本季度微信成交笔数
     * @param quarterWechatDealProportion 本季度微信成交占比(%)
     * @param quarterPcDealNum            本季度PC成交笔数
     * @param quarterPcDealProportion     本季度PC成交占比(%)
     * @return
     */
    private void saveQuarterOperationReport(String operationReportId, String cnName, String enName, Integer type,
                                            BigDecimal firstMonthAmount, BigDecimal secondMonthAmount, BigDecimal thirdMonthAmount,
                                            BigDecimal lastYearQuarterAmount, BigDecimal amountIncrease,
                                            BigDecimal lastYearQuarterProfit, BigDecimal profitIncrease,
                                            BigDecimal quarterAvgProfit, Integer quarterAppDealNum,
                                            BigDecimal quarterAppDealProportion, Integer quarterWechatDealNum,
                                            BigDecimal quarterWechatDealProportion, Integer quarterPcDealNum,
                                            BigDecimal quarterPcDealProportion) {

        QuarterOperationReport quarterOperationReport = new QuarterOperationReport();
//        quarterOperationReport.setOperationReportId(operationReportId);//运营报告ID
        quarterOperationReport.setCnName(cnName);
        quarterOperationReport.setEnName(enName);
        quarterOperationReport.setQuarterType(type);//季度类型(1.一季度2.二季度3.三季度4.四季度)
        quarterOperationReport.setFirstMonthAmount(firstMonthAmount);//第一月成交金额（元）
        quarterOperationReport.setSecondMonthAmount(secondMonthAmount);//第二月成交金额（元）
        quarterOperationReport.setThirdMonthAmount(thirdMonthAmount);//第三月成交金额（元）
        quarterOperationReport.setLastYearQuarterAmount(lastYearQuarterAmount.setScale(2, BigDecimal.ROUND_HALF_UP));//去年本季度累计成交金额（元）
        quarterOperationReport.setAmountIncrease(amountIncrease.setScale(2, BigDecimal.ROUND_HALF_UP));//成交金额同比增长(%)
        quarterOperationReport.setLastYearQuarterProfit(lastYearQuarterProfit.setScale(2, BigDecimal.ROUND_HALF_UP));//去年季度累计赚取收益（元）
        quarterOperationReport.setProfitIncrease(profitIncrease.setScale(2, BigDecimal.ROUND_HALF_UP));//收益同比增长(%)
        quarterOperationReport.setQuarterAvgProfit(quarterAvgProfit);//本季度平均出借利率(%)
        quarterOperationReport.setQuarterAppDealNum(quarterAppDealNum);//本季度APP成交笔数
        quarterOperationReport.setQuarterAppDealProportion(quarterAppDealProportion.setScale(2, BigDecimal.ROUND_HALF_UP));//本季度app成交占比(%)
        quarterOperationReport.setQuarterWechatDealNum(quarterWechatDealNum);//本季度微信成交笔数
        quarterOperationReport.setQuarterWechatDealProportion(quarterWechatDealProportion.setScale(2, BigDecimal.ROUND_HALF_UP));//本季度微信成交占比(%)
        quarterOperationReport.setQuarterPcDealNum(quarterPcDealNum);//本季度PC成交笔数
        quarterOperationReport.setQuarterPcDealProportion(quarterPcDealProportion.setScale(2, BigDecimal.ROUND_HALF_UP));//本季度PC成交占比(%)
        quarterOperationReport.setCreateTime(GetDate.getNowTime10());
        quarterOperationReport.setCreateUserId(1);

        QuarterOperationReportEntity quarterOperationReportEntity = new QuarterOperationReportEntity();
        BeanUtils.copyProperties(quarterOperationReport, quarterOperationReportEntity);
        quarterOperationReportEntity.setOperationReportId(operationReportId);//运营报告ID

//        quarterOperationReportMapper.insert(quarterOperationReport);
        quarterOperationReportMongDao.insert(quarterOperationReportEntity);
    }

    //保存半年度运营报告
    @Override
    public void setHalfYearReport(String year, String month) throws Exception {

        BigDecimal halfYearDealMoney = BigDecimal.ZERO;//上半年累计成交金额
        BigDecimal operationProfit = BigDecimal.ZERO;//上半年赚取收益
        BigDecimal avgProfit = BigDecimal.ZERO;// 上半年平均年利率
        BigDecimal halfYearRechargeMoney = BigDecimal.ZERO;// 上半年累计充值金额
        BigDecimal halfYearBase = BigDecimal.ZERO;// 上半年累计充值金额最高的金额
        Integer halfYearRechargeCount = 0;// 上半年累计充值笔数
        Integer halfYearBaseMonth = 0;// 上半年累计充值金额最高的月份
        Integer sumCompleteCount = 0;//上半年成交笔数

        HalfYearOperationReport halfYearOperationReport = new HalfYearOperationReport();

        //因为是从1月份生成12月份的
        if ("1".equals(month)) {
            year = String.valueOf(Integer.valueOf(year) - 1);
        }

        StringBuilder strCnName = new StringBuilder();//中文标题
        StringBuilder strEnName = new StringBuilder();//英文标题

        strCnName.append(year).append("年").append("上半年度");
        strEnName.append("Annual Operation Report for the First Half of ").append(year).toString();


        //业绩总览
        Map<String, BigDecimal> mapPerformanceSum = getPerformanceSum();
        if (CollectionUtils.isEmpty(mapPerformanceSum)) {
            throw new NullPointerException();
        }

        //上半年6个月成交金额
        List<OperationReportInfoCustomize> listMonthDealMoney = getMonthDealMoney(0, 6);
        if (CollectionUtils.isEmpty(listMonthDealMoney)) {
            throw new NullPointerException();
        }

        halfYearOperationReport.setFirstMonthAmount(listMonthDealMoney.get(0).getSumAccount());//第一月成交金额（元）
        halfYearOperationReport.setSecondMonthAmount(listMonthDealMoney.get(1).getSumAccount());//第二月成交金额（元）
        halfYearOperationReport.setThirdMonthAmount(listMonthDealMoney.get(2).getSumAccount());//第三月成交金额（元）
        halfYearOperationReport.setFourthMonthAmount(listMonthDealMoney.get(3).getSumAccount());//第四月成交金额（元）
        halfYearOperationReport.setFifthMonthAmount(listMonthDealMoney.get(4).getSumAccount());//第五月成交金额（元）
        halfYearOperationReport.setSixthMonthAmount(listMonthDealMoney.get(5).getSumAccount());//第六月成交金额（元）

        int b = 0;
        for (OperationReportInfoCustomize dto : listMonthDealMoney) {
            halfYearDealMoney = halfYearDealMoney.add(dto.getSumAccount());

            b++;
            //计算成交金额最高的月份
            if (dto.getSumAccount().compareTo(halfYearBase) == 1) {
                halfYearBase = dto.getSumAccount();
                halfYearBaseMonth = b;
            }
        }

        //上半年赚取收益,去年上半年赚取收益,上半年平均年率
        Map<String, BigDecimal> mapRevenueAndYield = getRevenueAndYield(6, 12, 18);
        if (CollectionUtils.isEmpty(mapRevenueAndYield)) {
            throw new NullPointerException();
        }
        operationProfit = mapRevenueAndYield.get("operationProfit").setScale(2, BigDecimal.ROUND_HALF_UP);//上半年赚钱的收益
        avgProfit = mapRevenueAndYield.get("avgProfit").setScale(2, BigDecimal.ROUND_DOWN);//上半年平均年率

        //充值金额、充值笔数
        Map<String, BigDecimal> mapRechargeMoneyAndSum = getRechargeMoneyAndSum(6);
        if (!CollectionUtils.isEmpty(mapRechargeMoneyAndSum)) {
            halfYearRechargeMoney = mapRechargeMoneyAndSum.get("rechargeMoney");// 上半年累计充值金额
            halfYearRechargeCount = mapRechargeMoneyAndSum.get("rechargeCount").intValue();// 上半年累计充值笔数
        }

        //set渠道分析
        sumCompleteCount = this.setCompleteCount(halfYearOperationReport, 6);

        //保存 运营报告显示栏
        String operationReportId = this.saveOperationReport(strCnName.toString(),
                strEnName.toString(),
                3, mapPerformanceSum.get("allAmount"), mapPerformanceSum.get("allProfit"), mapPerformanceSum.get("registNum"),
                sumCompleteCount, halfYearDealMoney, operationProfit, year);

        //set 上半年业绩
        this.setHalfYearPerformance(halfYearOperationReport, strCnName.toString(), strEnName.toString(),
                halfYearDealMoney, operationProfit, sumCompleteCount, halfYearRechargeCount, halfYearRechargeMoney,
                halfYearBaseMonth, halfYearBase, avgProfit);

        //set 借款期限
        this.setHalfYearAndYearLoanTime(halfYearOperationReport, 6);
        //保存 半年度运营报告
        this.saveHalfYearOperationReport(halfYearOperationReport, operationReportId);

        //保存 用户分析
        this.saveUserOperationReport(operationReportId, 3, 6);

        //保存 年度之最-十大出借人
        this.saveTenthOperationReport(operationReportId, 3, 6, halfYearDealMoney);
    }

    private void saveHalfYearOperationReport(HalfYearOperationReport halfYearOperationReport, String operationReportId) {
        HalfYearOperationReportEntity halfYearOperationReportEntity = new HalfYearOperationReportEntity();
        BeanUtils.copyProperties(halfYearOperationReport, halfYearOperationReportEntity);
        halfYearOperationReportEntity.setOperationReportId(operationReportId);//运营报告ID

//        halfYearOperationReportMapper.insert(halfYearOperationReport);
        halfYearOperationReportMongDao.insert(halfYearOperationReportEntity);
    }

    /**
     * set 上半年业绩
     *
     * @param halfYearOperationReport
     * @param cnName
     * @param enName
     * @param amount                  上半年度累计成交金额（元）
     * @param profit                  上半年度累计赚取收益（元）
     * @param successDeal             上半年累计成交笔数
     * @param rechargeDeal            上半年累计充值笔数
     * @param rechargeAmount          上半年累计充值金额
     * @param successMonth            上半年成交量最高单月
     * @param successMonthAmount      该月累计成交金额
     * @param avgProfit               上半年平均出借利率(%)
     */
    private void setHalfYearPerformance(HalfYearOperationReport halfYearOperationReport,
                                        String cnName, String enName,
                                        BigDecimal amount, BigDecimal profit,
                                        Integer successDeal, Integer rechargeDeal,
                                        BigDecimal rechargeAmount, Integer successMonth, BigDecimal successMonthAmount,
                                        BigDecimal avgProfit) {

//        halfYearOperationReport.setOperationReportId(operationReportId);//运营报告ID
        halfYearOperationReport.setCnName(cnName);//中文标题
        halfYearOperationReport.setEnName(enName);//英文标题
        halfYearOperationReport.setHalfYearAmount(amount);//上半年度累计成交金额（元）
        halfYearOperationReport.setHalfYearProfit(profit);//上半年度累计赚取收益（元）
        halfYearOperationReport.setHalfYearSuccessDeal(successDeal);//上半年累计成交笔数
        halfYearOperationReport.setHalfYearRechargeDeal(rechargeDeal);//上半年累计充值笔数
        halfYearOperationReport.setHalfYearRechargeAmount(rechargeAmount);//上半年累计充值金额
        halfYearOperationReport.setHalfYearSuccessMonth(successMonth);//上半年成交量最高单月
        halfYearOperationReport.setHalfYearSuccessMonthAmount(successMonthAmount);//该月累计成交金额
        halfYearOperationReport.setHalfYearAvgProfit(avgProfit);//上半年平均出借利率(%)
        halfYearOperationReport.setCreateTime(GetDate.getNowTime10());//
        halfYearOperationReport.setCreateUserId(1);//
    }

    //保存全年度运营报告
    @Override
    public void setYearReport(String year, String month) throws Exception {

        BigDecimal yearDealMoney = BigDecimal.ZERO;//全年累计成交金额
        BigDecimal operationProfit = BigDecimal.ZERO;//全年赚取收益
        BigDecimal avgProfit = BigDecimal.ZERO;// 全年平均年利率
        BigDecimal yearRechargeMoney = BigDecimal.ZERO;// 全年累计充值金额
        BigDecimal yearBase = BigDecimal.ZERO;// 全年累计充值金额最高的金额
        Integer yearRechargeCount = 0;// 全年累计充值笔数
        Integer yearBaseMonth = 0;// 全年累计充值金额最高的月份
        Integer sumCompleteCount = 0;//全年成交笔数

        YearOperationReport yearOperationReport = new YearOperationReport();

        //因为是从1月份生成12月份的
        if ("01".equals(month) || "1".equals(month)) {
            year = String.valueOf(Integer.valueOf(year) - 1);
        }

        StringBuilder strCnName = new StringBuilder();//中文标题
        StringBuilder strEnName = new StringBuilder();//英文标题

        strCnName.append(year).append("年年度");
        strEnName.append("Annual Operation Report of ").append(year).toString();


        //业绩总览
        Map<String, BigDecimal> mapPerformanceSum = getPerformanceSum();
        if (CollectionUtils.isEmpty(mapPerformanceSum)) {
            throw new NullPointerException();
        }

        //全年度12个月成交金额
        List<OperationReportInfoCustomize> listMonthDealMoney = getMonthDealMoney(0, 12);
        if (CollectionUtils.isEmpty(listMonthDealMoney)) {
            throw new NullPointerException();
        }

        yearOperationReport.setFirstMonthAmount(listMonthDealMoney.get(0).getSumAccount());//第一月成交金额（元）
        yearOperationReport.setSecondMonthAmount(listMonthDealMoney.get(1).getSumAccount());//第二月成交金额（元）
        yearOperationReport.setThirdMonthAmount(listMonthDealMoney.get(2).getSumAccount());//第三月成交金额（元）
        yearOperationReport.setFourthMonthAmount(listMonthDealMoney.get(3).getSumAccount());//第四月成交金额（元）
        yearOperationReport.setFifthMonthAmount(listMonthDealMoney.get(4).getSumAccount());//第五月成交金额（元）
        yearOperationReport.setSixthMonthAmount(listMonthDealMoney.get(5).getSumAccount());//第六月成交金额（元）

        yearOperationReport.setSeventhMonthAmoun(listMonthDealMoney.get(6).getSumAccount());//第7月成交金额（元）
        yearOperationReport.setEighteenMonthAmount(listMonthDealMoney.get(7).getSumAccount());//第8月成交金额（元）
        yearOperationReport.setNinthMonthAmount(listMonthDealMoney.get(8).getSumAccount());//第9月成交金额（元）
        yearOperationReport.setTenthMonthAmount(listMonthDealMoney.get(9).getSumAccount());//第10月成交金额（元）
        yearOperationReport.setEleventhMonthAmount(listMonthDealMoney.get(10).getSumAccount());//第11月成交金额（元）
        yearOperationReport.setTwelveMonthAmount(listMonthDealMoney.get(11).getSumAccount());//第12月成交金额（元）

        int b = 0;
        for (OperationReportInfoCustomize dto : listMonthDealMoney) {
            yearDealMoney = yearDealMoney.add(dto.getSumAccount());

            b++;
            //计算成交金额最高的月份
            if (dto.getSumAccount().compareTo(yearBase) == 1) {
                yearBase = dto.getSumAccount();
                yearBaseMonth = b;
            }
        }

        //全年赚取收益,去年全年赚取收益,全年平均年率
        Map<String, BigDecimal> mapRevenueAndYield = getRevenueAndYield(12, 12, 24);
        if (CollectionUtils.isEmpty(mapRevenueAndYield)) {
            throw new NullPointerException();
        }
        operationProfit = mapRevenueAndYield.get("operationProfit");//全年赚钱的收益
        avgProfit = mapRevenueAndYield.get("avgProfit").setScale(2, BigDecimal.ROUND_DOWN);//全年平均年率

        //充值金额、充值笔数
        Map<String, BigDecimal> mapRechargeMoneyAndSum = getRechargeMoneyAndSum(12);
        if (!CollectionUtils.isEmpty(mapRechargeMoneyAndSum)) {
            yearRechargeMoney = mapRechargeMoneyAndSum.get("rechargeMoney");// 全年累计充值金额
            yearRechargeCount = mapRechargeMoneyAndSum.get("rechargeCount").intValue();// 全年累计充值笔数
        }

        //set渠道分析
        sumCompleteCount = this.setCompleteCount(yearOperationReport, 12);

        //保存 运营报告显示栏
        String operationReportId = this.saveOperationReport(strCnName.toString(),
                strEnName.toString(),
                4, mapPerformanceSum.get("allAmount"), mapPerformanceSum.get("allProfit"), mapPerformanceSum.get("registNum"),
                sumCompleteCount, yearDealMoney, operationProfit, year);

        //set 全年业绩
        this.setYearPerformance(yearOperationReport, strCnName.toString(), strEnName.toString(),
                yearDealMoney, operationProfit, sumCompleteCount, yearRechargeCount, yearRechargeMoney,
                yearBaseMonth, yearBase, avgProfit);

        //set 借款期限
        this.setHalfYearAndYearLoanTime(yearOperationReport, 12);
        //保存 全年度运营报告
        this.saveYearOperationReport(yearOperationReport, operationReportId);

        //保存 用户分析
        this.saveUserOperationReport(operationReportId, 4, 12);

        //保存 年度之最-十大出借人
        this.saveTenthOperationReport(operationReportId, 4, 12, yearDealMoney);
    }

    /**
     * set 全年业绩
     *
     * @param yearOperationReport
     * @param cnName
     * @param enName
     * @param amount              全年度累计成交金额（元）
     * @param profit              全年度累计赚取收益（元）
     * @param successDeal         全年累计成交笔数
     * @param rechargeDeal        全年累计充值笔数
     * @param rechargeAmount      全年累计充值金额
     * @param successMonth        全年成交量最高单月
     * @param successMonthAmount  该月累计成交金额
     * @param avgProfit           全年平均出借利率(%)
     */
    private void setYearPerformance(YearOperationReport yearOperationReport,
                                    String cnName, String enName,
                                    BigDecimal amount, BigDecimal profit,
                                    Integer successDeal, Integer rechargeDeal,
                                    BigDecimal rechargeAmount, Integer successMonth, BigDecimal successMonthAmount,
                                    BigDecimal avgProfit) {

//        yearOperationReport.setOperationReportId(operationReportId);//运营报告ID
        yearOperationReport.setCnName(cnName);//中文标题
        yearOperationReport.setEnName(enName);//英文标题
        yearOperationReport.setYearAmount(amount);//全年度累计成交金额（元）
        yearOperationReport.setYearProfit(profit);//全年度累计赚取收益（元）
        yearOperationReport.setYearSuccessDeal(successDeal);//全年累计成交笔数
        yearOperationReport.setYearSuccessMonth(successMonth);//全年成交量最高单月
        yearOperationReport.setYearSuccessMonthAmount(successMonthAmount);//该月累计成交金额
        yearOperationReport.setYearAvgProfit(avgProfit);//全年平均出借利率(%)
        yearOperationReport.setCreateTime(GetDate.getNowTime10());//
        yearOperationReport.setCreateUserId(1);//
    }

    private void saveYearOperationReport(YearOperationReport yearOperationReport, String operationReportId) {
        YearOperationReportEntity yearOperationReportEntity = new YearOperationReportEntity();
        BeanUtils.copyProperties(yearOperationReport, yearOperationReportEntity);
        yearOperationReportEntity.setOperationReportId(operationReportId);//运营报告ID

//        yearOperationReportMapper.insert(YearOperationReport);
        yearOperationReportMongDao.insert(yearOperationReportEntity);
    }

}
