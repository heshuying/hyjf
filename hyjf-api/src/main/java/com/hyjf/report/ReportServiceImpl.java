package com.hyjf.report;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.report.OperationReportCustomize;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("reportService")
public class ReportServiceImpl extends BaseServiceImpl implements ReportService {

    /**
     * 获取报表详情接口
     *
     * @param paraMap 参数id 运营报告主键，必填。
     * @return
     */
    public JSONObject getOperationReportInfo(Map<String, Object> paraMap) {
        OperationReportCustomize report = new OperationReportCustomize();
        JSONObject json = new JSONObject();
        String id = String.valueOf(paraMap.get("id"));
        //获取运营报告对象
        report = contentOperationReportCustomizeMapper.selectByPrimaryKey(Integer.valueOf(id));
        if (report != null) {
            Integer reportType = report.getOperationReportType();
            json.put("report", report);
            if (reportType == 1) {
                //查询月度报告明细
                MonthlyOperationReportExample monthlyOperationReportExample = new MonthlyOperationReportExample();
                MonthlyOperationReportExample.Criteria MonthCriteria = monthlyOperationReportExample.createCriteria();
                MonthCriteria.andOperationReportIdEqualTo(Integer.valueOf(id));
                List<MonthlyOperationReport> monthlyOperationReportList = monthlyOperationReportMapper.selectByExample(monthlyOperationReportExample);
                if (monthlyOperationReportList != null && monthlyOperationReportList.size() > 0) {
                    json.put("monthlyOperationReport", monthlyOperationReportList.get(0));
                }
            } else if (reportType == 2) {
                //查询季度报告明细
                QuarterOperationReportExample example = new QuarterOperationReportExample();
                QuarterOperationReportExample.Criteria createCriteria = example.createCriteria();
                createCriteria.andOperationReportIdEqualTo(Integer.valueOf(id));
                List<QuarterOperationReport> quarterOperationReportList = quarterOperationReportMapper.selectByExample(example);
                if (quarterOperationReportList != null && quarterOperationReportList.size() > 0) {
                    json.put("quarterOperationReport", quarterOperationReportList.get(0));
                }

            } else if (reportType == 3) {
                //查询半年报告明细
                HalfYearOperationReportExample halfYearOperationReportExample = new HalfYearOperationReportExample();
                HalfYearOperationReportExample.Criteria halfYearCriteria = halfYearOperationReportExample.createCriteria();
                halfYearCriteria.andOperationReportIdEqualTo(Integer.valueOf(id));
                List<HalfYearOperationReport> halfYearOperationReportList = halfYearOperationReportMapper.selectByExample(halfYearOperationReportExample);
                if (halfYearOperationReportList != null && halfYearOperationReportList.size() > 0) {
                    json.put("halfYearOperationReport", halfYearOperationReportList.get(0));
                }
            } else if (reportType == 4) {
                //查询全年报告明细
                YearOperationReportExample yearOperationReportExample = new YearOperationReportExample();
                YearOperationReportExample.Criteria yearCriteria = yearOperationReportExample.createCriteria();
                yearCriteria.andOperationReportIdEqualTo(Integer.valueOf(id));
                List<YearOperationReport> yearOperationReportList = yearOperationReportMapper.selectByExample(yearOperationReportExample);
                if (yearOperationReportList != null && yearOperationReportList.size() > 0) {
                    json.put("yearOperationReport", yearOperationReportList.get(0));
                }
            }

            List<UserOperationReport> userOperationReportList = getUserOperationReport(id);
            if (userOperationReportList != null && userOperationReportList.size() > 0) {
                json.put("userOperationReport", userOperationReportList.get(0));
            }
            List<OperationReportActivity> operationReportActiveList = getOperationReportActive(id);
            json.put("operationReportActiveList", operationReportActiveList);

            List<TenthOperationReport> tenthOperationReportList = getTenthOperationReport(id);
            if (tenthOperationReportList != null && tenthOperationReportList.size() > 0) {
                TenthOperationReport tenthOperationReport = tenthOperationReportList.get(0);
                if(StringUtils.isNotEmpty(tenthOperationReport.getFirstTenderUsername())){
                    String userName1 = tenthOperationReport.getFirstTenderUsername().substring(0,1);
                    tenthOperationReport.setFirstTenderUsername(userName1+"*");
                }
                if(StringUtils.isNotEmpty(tenthOperationReport.getSecondTenderUsername())){
                    String userName2 = tenthOperationReport.getSecondTenderUsername().substring(0,1);
                    tenthOperationReport.setSecondTenderUsername(userName2+"*");
                }
                if(StringUtils.isNotEmpty(tenthOperationReport.getThirdTenderUsername())){
                    String userName3 = tenthOperationReport.getThirdTenderUsername().substring(0,1);
                    tenthOperationReport.setThirdTenderUsername(userName3+"*");
                }
                if(StringUtils.isNotEmpty(tenthOperationReport.getFourthTenderUsername())){
                    String userName4 = tenthOperationReport.getFourthTenderUsername().substring(0,1);
                    tenthOperationReport.setFourthTenderUsername(userName4+"*");
                }
                if(StringUtils.isNotEmpty(tenthOperationReport.getFifthTenderUsername())){
                    String userName5 = tenthOperationReport.getFifthTenderUsername().substring(0,1);
                    tenthOperationReport.setFifthTenderUsername(userName5+"*");
                }
                if(StringUtils.isNotEmpty(tenthOperationReport.getSixthTenderUsername())){
                    String userName6 = tenthOperationReport.getSixthTenderUsername().substring(0,1);
                    tenthOperationReport.setSixthTenderUsername(userName6+"*");
                }
                if(StringUtils.isNotEmpty(tenthOperationReport.getSeventhTenderUsername())){
                    String userName7 = tenthOperationReport.getSeventhTenderUsername().substring(0,1);
                    tenthOperationReport.setSeventhTenderUsername(userName7+"*");
                }
                if(StringUtils.isNotEmpty(tenthOperationReport.getEighthTenderUsername())){
                    String userName8 = tenthOperationReport.getEighthTenderUsername().substring(0,1);
                    tenthOperationReport.setEighthTenderUsername(userName8+"*");
                }
                if(StringUtils.isNotEmpty(tenthOperationReport.getNinthTenderUsername())){
                    String userName9 = tenthOperationReport.getNinthTenderUsername().substring(0,1);
                    tenthOperationReport.setNinthTenderUsername(userName9+"*");
                }
                if(StringUtils.isNotEmpty(tenthOperationReport.getTenthTenderUsername())){
                    String userName10 = tenthOperationReport.getTenthTenderUsername().substring(0,1);
                    tenthOperationReport.setTenthTenderUsername(userName10+"*");
                }
                if(StringUtils.isNotEmpty(tenthOperationReport.getMostTenderUsername())){
                    String mostTenderUsername = tenthOperationReport.getMostTenderUsername().substring(0,1);
                    tenthOperationReport.setMostTenderUsername(mostTenderUsername+"**");
                }
                if(StringUtils.isNotEmpty(tenthOperationReport.getBigMinnerUsername())){
                    String bigMinnerUsername = tenthOperationReport.getBigMinnerUsername().substring(0,1);
                    tenthOperationReport.setBigMinnerUsername(bigMinnerUsername+"**");
                }
                if(StringUtils.isNotEmpty(tenthOperationReport.getActiveTenderUsername())){
                    String activeTenderUsername = tenthOperationReport.getActiveTenderUsername().substring(0,1);
                    tenthOperationReport.setActiveTenderUsername(activeTenderUsername+"**");
                }
                json.put("tenthOperationReport", tenthOperationReportList.get(0));
            }
            json.put("success", "success");
        } else {
            json.put("success", "success");
            json.put("resultIsNull", "数据为空");
        }
        return json;
    }

    /**
     * 获取发布的列表接口
     *
     * @param map 参数isRelease 是否发布，必填。参数paginator  分页条目，选填
     * @return JSONObject
     */
    public JSONObject getRecordListByRelease(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        if ("null".equals(String.valueOf(map.get("isRelease")))) {
            json.put("success", "success");
            json.put("isRelease", "发布状态为空");
            return json;
        }
        Integer count = contentOperationReportCustomizeMapper.countRecord(map);
        if (count != null && count > 0) {
            //是否有分页参数
            if (!"null".equals(String.valueOf(map.get("paginatorPage")))) {
                Paginator paginator = new Paginator((Integer.valueOf(String.valueOf(map.get("paginatorPage")))), count);
                map.put("limitStart", paginator.getOffset());
                map.put("limitEnd", paginator.getLimit());
                json.put("paginator", paginator);
            }
            List<OperationReportCustomize> recordList = this.contentOperationReportCustomizeMapper
                    .getCustomizeRecordList(map);
            //java中处理天数，然后在按照前端需求排序。
            for(OperationReportCustomize operationReportCustomize:recordList){
                if(operationReportCustomize.getOperationReportType().intValue()==1){
                    operationReportCustomize.setSortDay(getDaysByYearMonth(Integer.valueOf(operationReportCustomize.getYear()),Integer.valueOf(operationReportCustomize.getSortMonth())));
                }else if(operationReportCustomize.getOperationReportType().intValue()==2){
                    //季度第一季度和第三季度
                    if(operationReportCustomize.getSortMonth()==1){
                        operationReportCustomize.setSortMonth(3);
                    }else {
                        operationReportCustomize.setSortMonth(6);
                    }
                    operationReportCustomize.setSortDay(31);
                }else if(operationReportCustomize.getOperationReportType().intValue()==3){
                    operationReportCustomize.setSortMonth(6);
                    operationReportCustomize.setSortDay(31);
                }else if(operationReportCustomize.getOperationReportType().intValue()==4){
                    operationReportCustomize.setSortMonth(12);
                    operationReportCustomize.setSortDay(31);
                }
            }
            Collections.sort(recordList, new Comparator<OperationReportCustomize>() {
                        @Override
                        public int compare(OperationReportCustomize o1, OperationReportCustomize o2) {
                            String arg1 = "";
                            String arg2 = "";
                            if(o1.getSortMonth()<10){
                                arg1 = o1.getYear()+0+o1.getSortMonth()+o1.getSortDay();
                            }else{
                                arg1 = o1.getYear()+o1.getSortMonth()+o1.getSortDay();
                            }
                            if(o2.getSortMonth()<10){
                                arg2 = o2.getYear()+0+o2.getSortMonth()+o2.getSortDay();
                            }else{
                                arg2 = o2.getYear()+o2.getSortMonth()+o2.getSortDay();
                            }
                            int o = arg2.compareTo(arg1);
                            return  o;
                        }
            });
            json.put("recordList", recordList);
            json.put("success", "success");
        } else {
            json.put("success", "success");
            json.put("countIsZero", "暂无任何数据");
        }

        return json;
    }

    /**
     * 根据年 月 获取对应的月份 天数
     * */
    public   int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }
    //查询用户分析详情
    public List<UserOperationReport> getUserOperationReport(String id) {
        List<UserOperationReport> userOperationReportList = null;
        UserOperationReportExample example = new UserOperationReportExample();
        UserOperationReportExample.Criteria createCriteria = example.createCriteria();
        createCriteria.andOperationReportIdEqualTo(Integer.valueOf(id));
        userOperationReportList = userOperationReportMapper.selectByExample(example);
        return userOperationReportList;
    }

    //查询运营报告活动
    public List<OperationReportActivity> getOperationReportActive(String id) {
        List<OperationReportActivity> operationReportActivityList = null;
        OperationReportActivityExample example = new OperationReportActivityExample();
        OperationReportActivityExample.Criteria createCriteria = example.createCriteria();
        createCriteria.andOperationReportIdEqualTo(Integer.valueOf(id));
        operationReportActivityList = operationReportActivityMapper.selectByExample(example);
        return operationReportActivityList;
    }

    //查询十大出借详情
    public List<TenthOperationReport> getTenthOperationReport(String id) {
        List<TenthOperationReport> tenthOperationReportList = null;
        TenthOperationReportExample example = new TenthOperationReportExample();
        TenthOperationReportExample.Criteria createCriteria = example.createCriteria();
        createCriteria.andOperationReportIdEqualTo(Integer.valueOf(id));
        tenthOperationReportList = tenthOperationReportMapper.selectByExample(example);
        return tenthOperationReportList;
    }


}
