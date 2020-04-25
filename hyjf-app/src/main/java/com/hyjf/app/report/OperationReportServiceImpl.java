package com.hyjf.app.report;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseServiceImpl;
import com.hyjf.common.enums.utils.OperationReportTypeEnum;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.operationreport.dao.*;
import com.hyjf.mongo.operationreport.entity.*;
import com.hyjf.mybatis.model.customize.report.OperationReportCustomize;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

@Service("appOperationReportServiceImpl")
public class OperationReportServiceImpl extends BaseServiceImpl implements OperationReportService {

    private Logger logger = LoggerFactory.getLogger(OperationReportServiceImpl.class);
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

    /**
     * 获取报表明细
     *
     * @param paraMap
     * @return JSONObject
     */
    @Override
    public JSONObject getOperationReportInfo(Map<String, Object> paraMap) {
        JSONObject json = new JSONObject();
        json = getOperationReportInfoJson(paraMap);
        ;
        return json;
    }

    /**
     * 获取已发布运营报告接口
     *
     * @param paraMap
     * @return JSONObject
     */
    @Override
    public JSONObject getRecordListByRelease(Map<String, Object> paraMap) {
        JSONObject json = new JSONObject();
        json = getRecordListByReleaseJson(paraMap);
        return json;
    }

    /**
     * 获取报表详情接口
     *
     * @param paraMap 参数id 运营报告主键，必填。
     * @return
     */
    public JSONObject getOperationReportInfoJson(Map<String, Object> paraMap) {
        JSONObject json = new JSONObject();
        String id = String.valueOf(paraMap.get("id"));
        //获取运营报告对象
        Query query = new Query();
        Criteria criteria = getCriteria(paraMap, query);
        query.addCriteria(criteria);
        OperationReportColumnEntity report = operationReportColumnMongDao.findOne(query);

        Query query2 = new Query();
        Criteria criteria2 = Criteria.where("operationReportId").is(id);
        query2.addCriteria(criteria2);
        if (report != null) {
            Integer reportType = report.getOperationReportType();
            json.put("report", report);


            if (reportType == 1) {
                //查询月度报告明细
                MonthlyOperationReportEntity monthlyOperationReportEntity = monthlyOperationReportMongDao.findOne(query2);
                if (monthlyOperationReportEntity != null) {
                    json.put("monthlyOperationReport", monthlyOperationReportEntity);
                }

            } else if (reportType == 2) {
                //查询季度报告明细
                QuarterOperationReportEntity quarterOperationReportEntity = quarterOperationReportMongDao.findOne(query2);
                if (quarterOperationReportEntity != null) {
                    json.put("quarterOperationReport", quarterOperationReportEntity);
                }

            } else if (reportType == 3) {
                //查询半年报告明细
                HalfYearOperationReportEntity halfYearOperationReportEntity = halfYearOperationReportMongDao.findOne(query2);
                if (halfYearOperationReportEntity != null) {
                    json.put("halfYearOperationReport", halfYearOperationReportEntity);
                }
            } else if (reportType == 4) {
                //查询全年报告明细
                YearOperationReportEntity yearOperationReportEntity = yearOperationReportMongDao.findOne(query2);
                if (yearOperationReportEntity != null) {
                    json.put("yearOperationReport", yearOperationReportEntity);
                }
            }

            List<UserOperationReportEntity> userOperationReportList = getUserOperationReport(id, query2);
            if (userOperationReportList != null && userOperationReportList.size() > 0) {
                json.put("userOperationReport", userOperationReportList.get(0));
            }
            query2.with(new Sort(Sort.Direction.ASC, "activtyTime"));
            List<OperationReportActivityEntity> operationReportActiveList = getOperationReportActive(id, query2);
            json.put("operationReportActiveList", operationReportActiveList);

            List<TenthOperationReportEntity> tenthOperationReportList = getTenthOperationReport(id, query2);
            if (tenthOperationReportList != null && tenthOperationReportList.size() > 0) {
                TenthOperationReportEntity tenthOperationReport = tenthOperationReportList.get(0);
                if (StringUtils.isNotEmpty(tenthOperationReport.getFirstTenderUsername())) {
                    String userName1 = tenthOperationReport.getFirstTenderUsername().substring(0, 1);
                    tenthOperationReport.setFirstTenderUsername(userName1 + "*");
                }
                if (StringUtils.isNotEmpty(tenthOperationReport.getSecondTenderUsername())) {
                    String userName2 = tenthOperationReport.getSecondTenderUsername().substring(0, 1);
                    tenthOperationReport.setSecondTenderUsername(userName2 + "*");
                }
                if (StringUtils.isNotEmpty(tenthOperationReport.getThirdTenderUsername())) {
                    String userName3 = tenthOperationReport.getThirdTenderUsername().substring(0, 1);
                    tenthOperationReport.setThirdTenderUsername(userName3 + "*");
                }
                if (StringUtils.isNotEmpty(tenthOperationReport.getFourthTenderUsername())) {
                    String userName4 = tenthOperationReport.getFourthTenderUsername().substring(0, 1);
                    tenthOperationReport.setFourthTenderUsername(userName4 + "*");
                }
                if (StringUtils.isNotEmpty(tenthOperationReport.getFifthTenderUsername())) {
                    String userName5 = tenthOperationReport.getFifthTenderUsername().substring(0, 1);
                    tenthOperationReport.setFifthTenderUsername(userName5 + "*");
                }
                if (StringUtils.isNotEmpty(tenthOperationReport.getSixthTenderUsername())) {
                    String userName6 = tenthOperationReport.getSixthTenderUsername().substring(0, 1);
                    tenthOperationReport.setSixthTenderUsername(userName6 + "*");
                }
                if (StringUtils.isNotEmpty(tenthOperationReport.getSeventhTenderUsername())) {
                    String userName7 = tenthOperationReport.getSeventhTenderUsername().substring(0, 1);
                    tenthOperationReport.setSeventhTenderUsername(userName7 + "*");
                }
                if (StringUtils.isNotEmpty(tenthOperationReport.getEighthTenderUsername())) {
                    String userName8 = tenthOperationReport.getEighthTenderUsername().substring(0, 1);
                    tenthOperationReport.setEighthTenderUsername(userName8 + "*");
                }
                if (StringUtils.isNotEmpty(tenthOperationReport.getNinthTenderUsername())) {
                    String userName9 = tenthOperationReport.getNinthTenderUsername().substring(0, 1);
                    tenthOperationReport.setNinthTenderUsername(userName9 + "*");
                }
                if (StringUtils.isNotEmpty(tenthOperationReport.getTenthTenderUsername())) {
                    String userName10 = tenthOperationReport.getTenthTenderUsername().substring(0, 1);
                    tenthOperationReport.setTenthTenderUsername(userName10 + "*");
                }
                if (StringUtils.isNotEmpty(tenthOperationReport.getMostTenderUsername())) {
                    String mostTenderUsername = tenthOperationReport.getMostTenderUsername().substring(0, 1);
                    tenthOperationReport.setMostTenderUsername(mostTenderUsername + "**");
                }
                if (StringUtils.isNotEmpty(tenthOperationReport.getBigMinnerUsername())) {
                    String bigMinnerUsername = tenthOperationReport.getBigMinnerUsername().substring(0, 1);
                    tenthOperationReport.setBigMinnerUsername(bigMinnerUsername + "**");
                }
                if (StringUtils.isNotEmpty(tenthOperationReport.getActiveTenderUsername())) {
                    String activeTenderUsername = tenthOperationReport.getActiveTenderUsername().substring(0, 1);
                    tenthOperationReport.setActiveTenderUsername(activeTenderUsername + "**");
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
    public JSONObject getRecordListByReleaseJson(Map<String, Object> map) {
        JSONObject json = new JSONObject();
        if (map.get("isRelease") == null) {
            json.put("success", "success");
            json.put("isRelease", "发布状态为空");
            return json;
        }
        Query query = new Query();
        Criteria criteria = getCriteria(map, query);
        query.addCriteria(criteria);

        List<OperationReportColumnEntity> list = operationReportColumnMongDao.find(query);
//        Integer count = contentOperationReportCustomizeMapper.countRecord(map);
        Integer count = list.size();
        if (count != null && count > 0) {
            //是否有分页参数
            if (!"null".equals(String.valueOf(map.get("paginatorPage")))) {
                Paginator paginator = new Paginator((Integer.valueOf(String.valueOf(map.get("paginatorPage")))), count);
                map.put("limitStart", paginator.getOffset());
                map.put("limitEnd", paginator.getLimit());
                json.put("paginator", paginator);
            }

            Query query2 = new Query();
            Criteria criteria2 = getCriteria(map, query2);
            query2.addCriteria(criteria2);
            List<OperationReportCustomize> recordList = new ArrayList<>();
            List<OperationReportColumnEntity> mongodbList = operationReportColumnMongDao.find(query2);
//            List<OperationReportCustomize> recordList = this.contentOperationReportCustomizeMapper
//                    .getCustomizeRecordList(map);

            Query queryReport = null;
            for (OperationReportColumnEntity dto : mongodbList) {
                OperationReportCustomize operationReportCustomize = new OperationReportCustomize();
                BeanUtils.copyProperties(dto, operationReportCustomize);

                //转换
                if (OperationReportTypeEnum.MONTH.getCode() == operationReportCustomize.getOperationReportType()) {//月度
                    queryReport = new Query();
                    Criteria criteriaQuarter = Criteria.where("operationReportId").is(dto.getId());
                    queryReport.addCriteria(criteriaQuarter);
                    MonthlyOperationReportEntity monthlyOperationReportEntity = monthlyOperationReportMongDao.findOne(queryReport);
                    if (monthlyOperationReportEntity != null) {
                        operationReportCustomize.setTypeRealName(monthlyOperationReportEntity.getMonth() + "月份");
                        operationReportCustomize.setSortMonth(monthlyOperationReportEntity.getMonth());
                    }

                } else if (OperationReportTypeEnum.QUARTER.getCode() == operationReportCustomize.getOperationReportType()) {//季度
                    queryReport = new Query();
                    Criteria criteriaQuarter = Criteria.where("operationReportId").is(dto.getId());
                    queryReport.addCriteria(criteriaQuarter);
                    QuarterOperationReportEntity quarterOperationReportEntity = quarterOperationReportMongDao.findOne(queryReport);
                    if (quarterOperationReportEntity != null) {
                        //季度类型(1.一季度2.二季度3.三季度4.四季度)
                        operationReportCustomize.setSortMonth(quarterOperationReportEntity.getQuarterType());
                        if (1 == quarterOperationReportEntity.getQuarterType()) {
                            operationReportCustomize.setTypeRealName("一季度");
                        } else {
                            operationReportCustomize.setTypeRealName("三季度");
                        }
                    }

                } else if (OperationReportTypeEnum.HALFYEAR.getCode() == operationReportCustomize.getOperationReportType()) {//半年
                    operationReportCustomize.setTypeRealName("上半年");
                } else if (OperationReportTypeEnum.YEAR.getCode() == operationReportCustomize.getOperationReportType()) {//全年
                    operationReportCustomize.setTypeRealName("年度");
                }

                recordList.add(operationReportCustomize);
            }


            //java中处理天数，然后在按照前端需求排序。
            for (OperationReportCustomize operationReportCustomize : recordList) {
                if (operationReportCustomize.getOperationReportType().intValue() == 1) {
                    operationReportCustomize.setSortDay(getDaysByYearMonth(Integer.valueOf(operationReportCustomize.getYear()),
                            operationReportCustomize.getSortMonth()));
                } else if (operationReportCustomize.getOperationReportType().intValue() == 2) {
                    //季度第一季度和第三季度
                    if (operationReportCustomize.getSortMonth() == 1) {
                        operationReportCustomize.setSortMonth(3);
                    } else {
                        operationReportCustomize.setSortMonth(9);
                    }
                    operationReportCustomize.setSortDay(getDaysByYearMonth(Integer.valueOf(operationReportCustomize.getYear()),
                            operationReportCustomize.getSortMonth()));
                } else if (operationReportCustomize.getOperationReportType().intValue() == 3) {
                    operationReportCustomize.setSortMonth(6);
                    operationReportCustomize.setSortDay(getDaysByYearMonth(Integer.valueOf(operationReportCustomize.getYear()),
                            operationReportCustomize.getSortMonth()));
                } else if (operationReportCustomize.getOperationReportType().intValue() == 4) {
                    operationReportCustomize.setSortMonth(12);
                    operationReportCustomize.setSortDay(getDaysByYearMonth(Integer.valueOf(operationReportCustomize.getYear()),
                            operationReportCustomize.getSortMonth()));
                }
            }
            Collections.sort(recordList, new Comparator<OperationReportCustomize>() {
                @Override
                public int compare(OperationReportCustomize o1, OperationReportCustomize o2) {
                    String arg1 = "";
                    String arg2 = "";
                    if (o1.getSortMonth() < 10) {
                        arg1 = o1.getYear() + 0 + o1.getSortMonth() + o1.getSortDay();
                    } else {
                        arg1 = o1.getYear() + o1.getSortMonth() + o1.getSortDay();
                    }
                    if (o2.getSortMonth() < 10) {
                        arg2 = o2.getYear() + 0 + o2.getSortMonth() + o2.getSortDay();
                    } else {
                        arg2 = o2.getYear() + o2.getSortMonth() + o2.getSortDay();
                    }
                    int o = arg2.compareTo(arg1);
                    return o;
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
     */
    public int getDaysByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    //查询用户分析详情
    public List<UserOperationReportEntity> getUserOperationReport(String id, Query query2) {
        List<UserOperationReportEntity> userOperationReportEntity = userOperationReportMongDao.find(query2);
        return userOperationReportEntity;
    }

    //查询运营报告活动
    public List<OperationReportActivityEntity> getOperationReportActive(String id, Query query2) {
        List<OperationReportActivityEntity> operationReportActivityEntity = operationReportActivityMongDao.find(query2);
        return operationReportActivityEntity;
    }

    //查询十大出借详情
    public List<TenthOperationReportEntity> getTenthOperationReport(String id, Query query2) {
        List<TenthOperationReportEntity> tenthOperationReportEntity = tenthOperationReportMongDao.find(query2);
        return tenthOperationReportEntity;
    }

    public Criteria getCriteria(Map<String, Object> record, Query query) {
        Criteria criteria = Criteria.where("isDelete").is(0);
        if (record.get("timeStar") != null) {
            Integer timeStar = GetDate.strYYYYMMDDHHMMSS2Timestamp2(record.get("timeStar").toString());
            Integer timeEnd = GetDate.strYYYYMMDDHHMMSS2Timestamp2(record.get("timeEnd").toString());
            criteria.and("createTime").gte(timeStar).lte(timeEnd);
        }

        if (record.get("isRelease") != null) {
            criteria.and("isRelease").is(Integer.valueOf(record.get("isRelease").toString()));
        }

        if (record.get("limitStart") != null) {
            query.skip(Integer.valueOf(record.get("limitStart").toString()));
        }
        if (record.get("limitEnd") != null) {
            query.limit(Integer.valueOf(record.get("limitEnd").toString()));
        }
        if (record.get("id") != null) {
            criteria.and("_id").is(record.get("id").toString());
        }
        query.with(new Sort(Sort.Direction.DESC, "createTime"));

        return criteria;
    }
}
