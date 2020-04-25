/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.hgdatareport.bifa.exceptiondata;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.hgdatareport.BaseHgDateReportServiceImpl;
import com.hyjf.batch.hgdatareport.bifa.BifaCommonConstants;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.DigitalUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.hgdatareport.dao.BifaBorrowInfoDao;
import com.hyjf.mongo.hgdatareport.dao.BifaBorrowStatusDao;
import com.hyjf.mongo.hgdatareport.dao.BifaCreditTenderInfoDao;
import com.hyjf.mongo.hgdatareport.dao.BifaHjhPlanInfoDao;
import com.hyjf.mongo.hgdatareport.entity.*;
import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jun
 * @version BifaExceptionDataServiceImpl, v0.1 2018/11/30 15:25
 */
@Service
public class BifaExceptionDataServiceImpl extends BaseHgDateReportServiceImpl implements BifaExceptionDataService{

    public Logger _log = LoggerFactory.getLogger(BifaExceptionDataServiceImpl.class);

    private static final String thisMessName = "定时任务处理上报失败数据";
    public static final String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_BIFA + " " + thisMessName + "】";

    //散标
    @Autowired
    BifaBorrowInfoDao bifaBorrowInfoDao;
    //产品状态
    @Autowired
    BifaBorrowStatusDao bifaBorrowStatusDao;
    //转让数据
    @Autowired
    BifaCreditTenderInfoDao bifaCreditTenderInfoDao;

    //智投
    @Autowired
    BifaHjhPlanInfoDao bifaHjhPlanInfoDao;

    @Override
    public void executeExceptionDataHandle() {
        //修复散标(放款,最后一期还款完成)异常
        this.bifaBorrowInfoReportAgain();
        //修复智投(新增)异常
        this.bifaHjhPlanInfoReportAgain();
        //修复债转(散标/智投)异常
        this.bifaBifaCreditInfoReportAgain();
        //修复产品状态更新(散标放款后,最后一期还款完成/智投下的标的放款后)异常
        this.bifaBorrowStatusReportAgain();

    }

    /**
     * 债转数据重新上报
     */
    private void bifaBifaCreditInfoReportAgain() {
        List<BifaCreditTenderInfoEntity> list = bifaCreditTenderInfoDao.find(new Query(Criteria.where("reportStatus").is("9")));
        if (CollectionUtils.isNotEmpty(list)) {
            for (BifaCreditTenderInfoEntity entity : list) {
                try {
//                    String creditNid=entity.getSource_product_code().substring(3,entity.getSource_product_code().length());
//                    if (CustomConstants.BORROW_CREDIT_STATUS.equals(entity.getFlag())){
//                        //散标转让
//                        BorrowCredit borrowCredit =this.selectBorrowCreditInfo(creditNid);
//                        if (null == borrowCredit || borrowCredit.getBidNid()==null) {
//                            _log.error(logHeader + "未获取到散标转让信息！！"+"creditNid:"+creditNid);
//                            continue;
//                        }
//                        //--> 判断当前标的转让对应的散标信息是否上报
//                        this.checkBifaBorrowInfoIsReport(borrowCredit.getBidNid());
//                        this.checkBifaBorrowStatusIsReport(borrowCredit.getBidNid());
//
//                    }else if (CustomConstants.HJH_CREDIT_STATUS.equals(entity.getFlag())){
//                        //智投转让
//                        HjhDebtCredit hjhDebtCredit =this.selectHjhDebtCreditInfo(creditNid);
//                        if (null == hjhDebtCredit || hjhDebtCredit.getBorrowNid()==null) {
//                            _log.error(logHeader + "未获取到智投转让信息！！"+"creditNid:"+creditNid);
//                            continue;
//                        }
//                        this.checkBifaBorrowInfoIsReport(hjhDebtCredit.getBorrowNid());
//                        this.checkBifaBorrowStatusIsReport(hjhDebtCredit.getBorrowNid());
//                    }

                    // -->修复转让异常数据
                    String methodName = "productRegistration";
                    BifaCreditTenderInfoEntity withoutFlagObj = this.removeFlag(entity);
                    BifaCreditTenderInfoEntity result = this.reportData(methodName,withoutFlagObj);
                    //将flag补回
                    result.setFlag(entity.getFlag());

                    Query query = new Query(Criteria.where("_id").is(entity.getId()));
                    Update update = new Update();
                    update.set("reportStatus", result.getReportStatus());
                    update.set("errCode",result.getErrCode());
                    update.set("errDesc",result.getErrDesc());
                    update.set("updateTime",GetDate.getDate());
                    bifaCreditTenderInfoDao.findAndModify(query,update,BifaCommonConstants.HT_BIFA_CREDITTENDERINFO);
                    if ("1".equals(result.getReportStatus()) || "7".equals(result.getReportStatus())) {
                        _log.info(logHeader + "上报债转异常数据修复成功！！"+JSONObject.toJSONString(result));
                    }else if ("9".equals(result.getReportStatus())){
                        _log.error(logHeader + "上报债转异常数据修复失败！！"+JSONObject.toJSONString(result));
                    }
                } catch (Exception e) {
                    _log.error(logHeader + "上报债转异常数据修复失败！！", e);
                }
            }
        }
    }

    /**
     * 不上报转让类型flag
     * @param entity
     * @return
     */
    private BifaCreditTenderInfoEntity removeFlag(BifaCreditTenderInfoEntity entity) {
        BifaCreditTenderInfoEntity result = entity.cloneObj();
        result.setFlag(null);
        return result;
    }

    /**
     * 获取智投转让数据
     * @param creditNid
     * @return
     */
    private HjhDebtCredit selectHjhDebtCreditInfo(String creditNid) {
        HjhDebtCreditExample example = new HjhDebtCreditExample();
        HjhDebtCreditExample.Criteria criteria = example.createCriteria();
        criteria.andCreditNidEqualTo(creditNid);
        List<HjhDebtCredit> list = this.hjhDebtCreditMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    /**
     * 获取散标转让信息
     * @param creditNid
     * @return
     */
    private BorrowCredit selectBorrowCreditInfo(String creditNid) {
        BorrowCreditExample borrowCreditExample = new BorrowCreditExample();
        BorrowCreditExample.Criteria borrowCreditCra = borrowCreditExample.createCriteria();
        borrowCreditCra.andCreditNidEqualTo(Integer.parseInt(creditNid));
        List<BorrowCredit> list = this.borrowCreditMapper.selectByExample(borrowCreditExample);
        if (CollectionUtils.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    /**
     * 产品状态更新数据上报
     */
    private void bifaBorrowStatusReportAgain() {
        List<BifaBorrowStatusEntity> list = bifaBorrowStatusDao.find(new Query(Criteria.where("reportStatus").is("9")));
        if (CollectionUtils.isNotEmpty(list)){
            for (BifaBorrowStatusEntity entity:list) {
                try {
                    if (entity.getSource_product_code()==null){
                        _log.error(logHeader + "上报产品状态更新异常数据修复失败！！,source_product_code为空!!!");
                        continue;
                    }
                    // -->判断当前产品状态更新对应的散标信息是否上报
                    boolean isHjh = this.isHjh(entity.getSource_product_code());
                    if (!isHjh) {
                        //非智投下面的标的报送散标信息
                        this.checkBifaBorrowInfoIsReport(entity.getSource_product_code());
                    }else {
                        //智投下面的标的先报送新增智投信息
                        this.checkBifaHjhPlanInfoIsReport(entity.getSource_product_code());
                    }
                    // -->重新上报产品状态更新
                    String methodName = "productStatusUpdate";
                    BifaBorrowStatusEntity result = this.reportData(methodName,entity);
                    Query query = new Query(Criteria.where("_id").is(entity.getId())
                            .and("product_status").is(entity.getProduct_status()));
                    Update update = new Update();
                    update.set("reportStatus", result.getReportStatus());
                    update.set("errCode",result.getErrCode());
                    update.set("errDesc",result.getErrDesc());
                    update.set("updateTime",GetDate.getDate());
                    bifaBorrowStatusDao.findAndModify(query,update,BifaCommonConstants.HT_BIFA_BORROW_STATUS);
                    if ("1".equals(result.getReportStatus()) || "7".equals(result.getReportStatus())) {
                        _log.info(logHeader + "上报产品状态更新异常数据修复成功！！"+JSONObject.toJSONString(result));
                    }else if ("9".equals(result.getReportStatus())){
                        _log.error(logHeader + "上报产品状态更新异常数据修复失败！！"+JSONObject.toJSONString(result));
                    }
                } catch (Exception e) {
                    _log.error(logHeader + "上报产品状态更新异常数据修复失败！！", e);
                }

            }
        }

    }

    /**
     * 检查智投信息有无上报
     * @param planNid
     */
    private void checkBifaHjhPlanInfoIsReport(String planNid) throws Exception {
        //先查看是否存在上报成功的记录
        //1上报成功 7重复上报
        BifaHjhPlanInfoEntity bifaHjhPlanInfoEntity=bifaHjhPlanInfoDao.findOne(new Query(Criteria.where("reportStatus").in("1","7").and("source_product_code").is(planNid)));
        if (bifaHjhPlanInfoEntity==null){
            //先查看有没有上报失败的历史数据
            Query query = new Query(Criteria.where("source_product_code").is(planNid).and("reportStatus").is("9"));
            bifaHjhPlanInfoEntity=bifaHjhPlanInfoDao.findOne(query);
            //当前智投数据没有上报过
            if (bifaHjhPlanInfoEntity == null){
                //等于null说明没有上报成功 拉取智投信息
                HjhPlan hjhplan = this.selectHjhPlanInfo(planNid);
                if (null == hjhplan) {
                    throw new Exception(logHeader + "未获取到新增的智投信息！！"+"planNid:"+planNid);
                }
                // --> 数据变换
                bifaHjhPlanInfoEntity = new BifaHjhPlanInfoEntity();
                boolean result = this.convertBifaHjhPlanInfo(hjhplan,bifaHjhPlanInfoEntity);
                if (!result){
                    throw new Exception(logHeader + "新增的智投数据变换失败！！"+JSONObject.toJSONString(bifaHjhPlanInfoEntity));
                }
                //上报智投数据（实时上报）
                String methodName = "productRegistration";
                BifaHjhPlanInfoEntity reportResult = this.reportData(methodName,bifaHjhPlanInfoEntity);
                if ("1".equals(reportResult.getReportStatus()) || "7".equals(reportResult.getReportStatus())) {
                    //上报成功
                    _log.info(logHeader + "上报产品状态更新补偿上报新增智投信息成功！！！" + reportResult);
                }else if ("9".equals(reportResult.getReportStatus())){
                    //上报失败 已经上报过的视为本次上报失败
                    _log.error(logHeader + "上报产品状态更新补偿上报新增智投信息失败！！！"+JSONObject.toJSONString(reportResult));
                }
                this.insertBifaHjhPlanReportData(reportResult);
                _log.info(logHeader + "上报产品状态更新补偿上报新增智投信息保存本地！！！"+JSONObject.toJSONString(reportResult));

            } else {
                String methodName = "productRegistration";
                BifaHjhPlanInfoEntity reportResult = this.reportData(methodName,bifaHjhPlanInfoEntity);
                Update update = new Update();
                update.set("reportStatus", reportResult.getReportStatus());
                update.set("errCode",reportResult.getErrCode());
                update.set("errDesc",reportResult.getErrDesc());
                update.set("updateTime",GetDate.getDate());
                bifaHjhPlanInfoDao.findAndModify(query,update,BifaCommonConstants.HT_BIFA_HJH_PLANINFO);
                if ("1".equals(reportResult.getReportStatus()) || "7".equals(reportResult.getReportStatus())) {
                    _log.info(logHeader + "上报新增智投信息异常数据修复成功！！！"+JSONObject.toJSONString(reportResult));
                }else if ("9".equals(reportResult.getReportStatus())){
                    _log.error(logHeader + "上报新增智投信息异常数据修复失败！！！"+JSONObject.toJSONString(reportResult));
                }
            }

        }

    }

    /**
     * 新增智投保存到本地mongo
     * @param reportResult
     */
    private void insertBifaHjhPlanReportData(BifaHjhPlanInfoEntity reportResult) {
        bifaHjhPlanInfoDao.insert(reportResult);
    }

    /**
     * 装配智投信息到智投集合对应的实体
     * @param hjhplan
     * @param bifaHjhPlanInfoEntity
     * @return
     */
    private boolean convertBifaHjhPlanInfo(HjhPlan hjhplan, BifaHjhPlanInfoEntity bifaHjhPlanInfoEntity) {
        try {
            bifaHjhPlanInfoEntity.setProduct_reg_type("02");
            bifaHjhPlanInfoEntity.setProduct_name(hjhplan.getPlanName());
            bifaHjhPlanInfoEntity.setProduct_mark("智投服务");
            bifaHjhPlanInfoEntity.setSource_code(SOURCE_CODE);
            bifaHjhPlanInfoEntity.setSource_product_code(hjhplan.getPlanNid());
            bifaHjhPlanInfoEntity.setPlan_raise_amount("1000000");
            bifaHjhPlanInfoEntity.setRate(this.convertRateHjhPlan(hjhplan.getExpectApr()));
            bifaHjhPlanInfoEntity.setTerm_type(this.convertTermType(hjhplan.getBorrowStyle()));
            bifaHjhPlanInfoEntity.setTerm(String.valueOf(hjhplan.getLockPeriod()));
            bifaHjhPlanInfoEntity.setIsshow("1");
            bifaHjhPlanInfoEntity.setRemark("无");
            bifaHjhPlanInfoEntity.setAmount_limmts(hjhplan.getMinInvestment().toString());
            _log.info("_____________test:" + new BigDecimal(RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE)) + ", " + hjhplan.getMaxInvestment());
            _log.info("_____________mint:" + DigitalUtils.min(new BigDecimal(RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE)), hjhplan.getMaxInvestment()).toString());
            bifaHjhPlanInfoEntity.setAmount_limmtl(DigitalUtils.min(new BigDecimal(RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE)), hjhplan.getMaxInvestment()).toString());
            bifaHjhPlanInfoEntity.setRed_rate("超出参考回报部分作为服务费用");
            bifaHjhPlanInfoEntity.setSource_product_url(MessageFormat.format(SOURCE_PRODUCT_URL_HJHPLAN,hjhplan.getPlanNid()));
            Date currDate =GetDate.getDate();
            bifaHjhPlanInfoEntity.setCreateTime(currDate);
            bifaHjhPlanInfoEntity.setUpdateTime(currDate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String convertRateHjhPlan(BigDecimal expectApr) {
        BigDecimal bd12 = new BigDecimal("1200");
        BigDecimal divide =expectApr.divide(bd12,6,RoundingMode.DOWN);
        return divide.toString();
    }

    /**
     * 获取智投信息
     * @param planNid
     * @return
     */
    private HjhPlan selectHjhPlanInfo(String planNid) {
        HjhPlanExample example = new HjhPlanExample();
        HjhPlanExample.Criteria criteria = example.createCriteria();
        criteria.andPlanNidEqualTo(planNid);
        List<HjhPlan> list = this.hjhPlanMapper.selectByExample(example);
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }else {
            return null;
        }
    }


    /**
     * 获取散标信息
     * 智投下的放款标的改为上报状态更新之后不上报散标信息
     * @param borrowNid
     * @return
     */
    private void checkBifaBorrowInfoIsReport(String borrowNid) throws Exception {
        //获取上报成功的记录
        Query query = new Query(Criteria.where("source_product_code").is(borrowNid).and("reportStatus").in("1","7"));
        BifaBorrowInfoEntity borrowInfo = bifaBorrowInfoDao.findOne(query);
        //没有上报成功的数据才执行修复操作
        if (borrowInfo == null){
            //没有上报成功的记录 在看看有没有上报失败的记录
            query = new Query(Criteria.where("source_product_code").is(borrowNid).and("reportStatus").is("9"));
            borrowInfo = bifaBorrowInfoDao.findOne(query);
            if (borrowInfo==null){
                //没有上报过该散标信息
                //从数据库中拉取散标信息
                // --> 拉数据
                // 散标信息
                Borrow borrow = this.selectBorrowInfo(borrowNid);
                if (null == borrow) {
                    throw new Exception(logHeader + "未获取到散标信息！！"+"borrowNid:"+borrowNid);
                }
                // 借款人信息
                Map<String, String> borrowUserInfo = this.getBorrowUserInfo(borrow.getBorrowNid(),borrow.getCompanyOrPersonal());
                if(null == borrowUserInfo) {
                    throw new Exception(logHeader + "未获取到标的借款人信息！！");
                }
                //获取标的对应的还款信息
                BorrowRepay borrowRepay =this.selectBorrowRepay(borrowNid);

                //抵押車輛信息
                List<BorrowCarinfo> borrowCarsinfo = this.selectBorrowCarInfo(borrowNid);

                //抵押房產信息
                List<BorrowHouses> borrowHouses = this.selectBorrowHouseInfo(borrowNid);


                BifaBorrowInfoEntity bifaBorrowInfoEntity = new BifaBorrowInfoEntity();
                // --> 数据变换

                boolean result = this.convertBifaBorrowInfo(
                        borrow, borrowUserInfo,borrowRepay,borrowCarsinfo,borrowHouses,bifaBorrowInfoEntity);
                if (!result){
                    throw new Exception(logHeader + "数据变换失败！！"+JSONObject.toJSONString(bifaBorrowInfoEntity));
                }

                // --> 上报数据（实时上报）
                //上报数据失败时 将数据存放到mongoDB
                String methodName = "productRegistration";
                BifaBorrowInfoEntity reportResult = this.reportData(methodName,bifaBorrowInfoEntity);
                if ("1".equals(reportResult.getReportStatus()) || "7".equals(reportResult.getReportStatus())){
                    _log.info(logHeader + "上报产品状态更新补偿上报散标信息成功！！！"+JSONObject.toJSONString(bifaBorrowInfoEntity));
                }else if ("9".equals(reportResult.getReportStatus())) {
                    _log.error(logHeader + "上报产品状态更新补偿上报散标信息失败！！！" + JSONObject.toJSONString(bifaBorrowInfoEntity));
                }

                // --> 保存上报数据
                this.insertBifaBorrowReportData(bifaBorrowInfoEntity);
                _log.info(logHeader + "上报产品状态更新补偿上报散标数据保存本地！！"+JSONObject.toJSONString(bifaBorrowInfoEntity));


            }else {
                //mongo中有上报历史数据,则再次上报历史数据
                String methodName = "productRegistration";
                BifaBorrowInfoEntity result = this.reportData(methodName, borrowInfo);
                Update update = new Update();
                update.set("reportStatus", result.getReportStatus());
                update.set("errCode", result.getErrCode());
                update.set("errDesc", result.getErrDesc());
                update.set("updateTime",GetDate.getDate());
                bifaBorrowInfoDao.findAndModify(query, update, BifaCommonConstants.HT_BIFA_BORROWINFO);
                if ("1".equals(result.getReportStatus()) || "7".equals(result.getReportStatus())) {
                    _log.info(logHeader + "上报散标异常数据修复成功！！" + JSONObject.toJSONString(result));
                } else if ("9".equals(result.getReportStatus())){
                    _log.error(logHeader + "上报散标异常数据修复失败！！" + JSONObject.toJSONString(result));
                }
            }

        }
    }


    /**
     * 保存散标信息到本地mongo
     * @param data
     */
    private void insertBifaBorrowReportData(BifaBorrowInfoEntity data) {
        bifaBorrowInfoDao.insert(data);
    }


    private boolean convertBifaBorrowInfo(Borrow borrow, Map<String, String> borrowUserInfo, BorrowRepay borrowRepay, List<BorrowCarinfo> borrowCarsinfo,
                                          List<BorrowHouses> borrowHouses, BifaBorrowInfoEntity bifaBorrowInfoEntity) {

        try {
            bifaBorrowInfoEntity.setProduct_reg_type("01");
            bifaBorrowInfoEntity.setProduct_name(borrow.getProjectName());
            bifaBorrowInfoEntity.setProduct_mark(this.convertProductMark(borrow.getProjectType()));
            bifaBorrowInfoEntity.setSource_code(SOURCE_CODE);
            bifaBorrowInfoEntity.setSource_product_code(borrow.getBorrowNid());
            bifaBorrowInfoEntity.setBorrow_sex(borrowUserInfo.get("sex"));
            bifaBorrowInfoEntity.setAmount(String.valueOf(borrow.getAccount()));
            bifaBorrowInfoEntity.setRate(this.convertRateBorrow(borrow.getBorrowApr()));
            bifaBorrowInfoEntity.setTerm_type(this.convertTermType(borrow.getBorrowStyle()));
            bifaBorrowInfoEntity.setTerm(String.valueOf(borrow.getBorrowPeriod()));
            bifaBorrowInfoEntity.setPay_type(this.convertPayType(borrow.getBorrowStyle()));
            //服务费=放款服务费+还款服务费
            bifaBorrowInfoEntity.setService_cost(this.getServiceCost(borrow.getBorrowNid()));
            bifaBorrowInfoEntity.setRisk_margin("0");
            bifaBorrowInfoEntity.setLoan_type(this.convertLoanType(borrow.getAssetAttributes()));
            bifaBorrowInfoEntity.setLoan_credit_rating(borrow.getBorrowLevel());
            bifaBorrowInfoEntity.setSecurity_info("");//留空不报送
            bifaBorrowInfoEntity.setCollateral_desc(this.convertCollateralDesc(borrowCarsinfo, borrowHouses));
            bifaBorrowInfoEntity.setCollateral_info(this.convertCollateralInfo(borrowCarsinfo, borrowHouses));
            bifaBorrowInfoEntity.setOverdue_limmit("到期还款日当天24点未提交还款的标的");
            bifaBorrowInfoEntity.setBad_debt_limmit("3月");
            bifaBorrowInfoEntity.setAmount_limmts(String.valueOf(borrow.getTenderAccountMin()));
            bifaBorrowInfoEntity.setAmount_limmtl(String.valueOf(borrow.getTenderAccountMax()));
            bifaBorrowInfoEntity.setAllow_transfer("0");
            bifaBorrowInfoEntity.setClose_limmit("0");
            bifaBorrowInfoEntity.setSecurity_type(this.convertSecurityType(borrow.getAssetAttributes()));
            bifaBorrowInfoEntity.setProject_source("合作机构推荐");
            bifaBorrowInfoEntity.setSource_product_url(MessageFormat.format(SOURCE_PRODUCT_URL_BORROW, borrow.getBorrowNid()));
            bifaBorrowInfoEntity.setBorrow_name_idcard_digest(selectUserIdToSHA256(null, borrowUserInfo.get("trueName"), borrowUserInfo.get("idCard")).getSha256());
            Date currDate = GetDate.getDate();
            bifaBorrowInfoEntity.setCreateTime(currDate);
            bifaBorrowInfoEntity.setUpdateTime(currDate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 抵押标:报送抵押
     * 质押标:报送质押
     * 无抵押内容/质押内容 或者其他值填保证
     *
     * @return
     */
    private String convertSecurityType(Integer assetAttributes) {
        if (assetAttributes == null || assetAttributes == 0 || assetAttributes == 1) {
            return "抵押";
        } else if (assetAttributes == 2) {
            return "质押";
        } else {
            return "保证";
        }
    }

    /**
     * 抵押/质押物、估值、平均处置周期转换
     *
     * @param borrowCarsinfo
     * @param borrowHouses
     * @return
     */
    private String convertCollateralInfo(List<BorrowCarinfo> borrowCarsinfo, List<BorrowHouses> borrowHouses) {
        BigDecimal carsTotalPrice = new BigDecimal("0");
        BigDecimal housesTotalPrice = new BigDecimal("0");
        StringBuffer result = new StringBuffer("");
        if (CollectionUtils.isNotEmpty(borrowCarsinfo)) {
            for (BorrowCarinfo carinfo : borrowCarsinfo) {
                carsTotalPrice = carsTotalPrice.add(carinfo.getToprice());
            }
        }

        if (CollectionUtils.isNotEmpty(borrowHouses)) {
            for (BorrowHouses house : borrowHouses) {
                housesTotalPrice = housesTotalPrice.add(new BigDecimal(house.getHousesToprice()));
            }
        }

        if (!carsTotalPrice.equals(BigDecimal.ZERO)) {
            result.append("车辆评估价(元):" + carsTotalPrice.toString());
        }
        if (!housesTotalPrice.equals(BigDecimal.ZERO)) {
            if (result.length() == 0) {
                result.append("房产抵押价值(元):" + housesTotalPrice.toString());
            } else {
                result.append(",房产抵押价值(元):" + housesTotalPrice.toString());
            }
        }
        return result.toString();
    }

    /**
     * 抵押/质押物描述转换
     *
     * @param borrowCarsinfo
     * @param borrowHouses
     * @return
     */
    private String convertCollateralDesc(List<BorrowCarinfo> borrowCarsinfo, List<BorrowHouses> borrowHouses) {
        StringBuffer sb = new StringBuffer();
        //车辆
        if (CollectionUtils.isNotEmpty(borrowCarsinfo)) {
            for (int i = 0; i < borrowCarsinfo.size(); i++) {
                //最后一个
                if (i == borrowCarsinfo.size() - 1) {
                    sb.append(borrowCarsinfo.get(i).getBrand() + borrowCarsinfo.get(i).getModel() + "车一辆");
                } else {
                    //非最后一个
                    sb.append(borrowCarsinfo.get(i).getBrand() + borrowCarsinfo.get(i).getModel() + "车一辆, ");
                }

            }
        }

        //房屋
        if (CollectionUtils.isNotEmpty(borrowHouses)) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            for (int i = 0; i < borrowHouses.size(); i++) {
                //最后一个
                if (i == borrowHouses.size() - 1) {
                    sb.append(borrowHouses.get(i).getHousesArea() + "㎡" + this.convertHousesType(borrowHouses.get(i).getHousesType()) + "房产一处");
                } else {
                    //非最后一个
                    sb.append(borrowHouses.get(i).getHousesArea() + "㎡" + this.convertHousesType(borrowHouses.get(i).getHousesType()) + "房产一处, ");
                }
            }

        }

        return sb.toString();

    }

    /**
     * 担保方式转换
     *
     * @param assetAttributes
     * @return
     */
    public String convertLoanType(Integer assetAttributes) {
        String result = "";
        if (assetAttributes == null || assetAttributes == 0 || assetAttributes == 1 || assetAttributes == 2) {
            result = "抵质押";
        } else if (assetAttributes == 3) {
            result = "信用";
        }
        return result;
    }

    /**
     * 服务费=放款服务费+还款服务费
     */
    private String getServiceCost(String borrowNid) {
        return this.borrowRecoverCustomizeMapper.selectServiceCostSum(borrowNid).toString();
    }

    /**
     * 月利率
     *
     * @param borrowApr
     * @return
     */
    private String convertRateBorrow(BigDecimal borrowApr) {
        //12期 百分号转小数
        BigDecimal bd12 = new BigDecimal("1200");
        BigDecimal divide = borrowApr.divide(bd12, 6, RoundingMode.DOWN);
        return divide.toString();
    }

    /**
     * 借款人车辆信息
     * @param borrowNid
     * @return
     */
    private List<BorrowCarinfo> selectBorrowCarInfo(String borrowNid) {
        BorrowCarinfoExample example = new BorrowCarinfoExample();
        BorrowCarinfoExample.Criteria cri = example.createCriteria();
        cri.andBorrowNidEqualTo(borrowNid);
        return this.borrowCarinfoMapper.selectByExample(example);
    }

    /**
     * 借款人房产信息
     * @param borrowNid
     * @return
     */
    private List<BorrowHouses> selectBorrowHouseInfo(String borrowNid) {
        BorrowHousesExample example = new BorrowHousesExample();
        BorrowHousesExample.Criteria criteria = example.createCriteria();
        criteria.andBorrowNidEqualTo(borrowNid);
        return this.borrowHousesMapper.selectByExample(example);
    }


    private Map<String,String> getBorrowUserInfo(String borrowNid, String companyOrPersonal) {
        Map<String, String> resultMap = new HashMap<String, String>();
        if ("1".equals(companyOrPersonal)) {
            //公司
            BorrowUsersExample example = new BorrowUsersExample();
            BorrowUsersExample.Criteria criteria = example.createCriteria();
            criteria.andBorrowNidEqualTo(borrowNid);
            List<BorrowUsers> borrowUsers = this.borrowUsersMapper.selectByExample(example);
            resultMap.put("trueName", "");
            resultMap.put("sex", this.convertSex(9));
            String idCard = borrowUsers.get(0).getSocialCreditCode();
            if (StringUtils.isEmpty(idCard)) {
                idCard = borrowUsers.get(0).getRegistCode();
            }
            resultMap.put("idCard", idCard);
        } else if ("2".equals(companyOrPersonal)) {
            //个人
            BorrowManinfoExample example = new BorrowManinfoExample();
            BorrowManinfoExample.Criteria criteria = example.createCriteria();
            criteria.andBorrowNidEqualTo(borrowNid);
            List<BorrowManinfo> borrowManinfos = this.borrowManinfoMapper.selectByExample(example);
            resultMap.put("trueName", borrowManinfos.get(0).getName());
            resultMap.put("sex", this.convertSex(borrowManinfos.get(0).getSex()));
            resultMap.put("idCard", borrowManinfos.get(0).getCardNo());
        }
        return resultMap;
    }


    /**
     * 判断是否是汇计划下的标的
     * @param nid
     * @return
     */
    private boolean isHjh(String nid) {
        HjhPlanExample example = new HjhPlanExample();
        HjhPlanExample.Criteria cri = example.createCriteria();
        cri.andPlanNidEqualTo(nid);
        int count = this.hjhPlanMapper.countByExample(example);
        if (count>0){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 新增智投数据上报
     */
    private void bifaHjhPlanInfoReportAgain() {
        List<BifaHjhPlanInfoEntity> list = bifaHjhPlanInfoDao.find(new Query(Criteria.where("reportStatus").is("9")));
        if (CollectionUtils.isNotEmpty(list)){
            for (BifaHjhPlanInfoEntity entity : list) {
                try {
                    String methodName = "productRegistration";
                    BifaHjhPlanInfoEntity result = this.reportData(methodName,entity);
                    Query query = new Query(Criteria.where("_id").is(entity.getId()));
                    Update update = new Update();
                    update.set("reportStatus", result.getReportStatus());
                    update.set("errCode",result.getErrCode());
                    update.set("errDesc",result.getErrDesc());
                    update.set("updateTime",GetDate.getDate());
                    bifaHjhPlanInfoDao.findAndModify(query,update,BifaCommonConstants.HT_BIFA_HJH_PLANINFO);
                    if ("1".equals(result.getReportStatus()) || "7".equals(result.getReportStatus())) {
                        _log.info(logHeader + "上报智投异常数据修复成功！！"+JSONObject.toJSONString(result));
                    }else if ("9".equals(result.getReportStatus())){
                        _log.error(logHeader + "上报智投异常数据修复失败！！"+JSONObject.toJSONString(result));
                    }
                } catch (Exception e) {
                    _log.error(logHeader + "上报智投异常数据修复失败！！", e);
                }

            }
        }

    }

    private void checkBifaBorrowStatusIsReport(String source_product_code) {
        if (source_product_code != null){
            Borrow borrow = this.selectBorrowInfo(source_product_code);
            String statusStr = "";
            if (borrow.getStatus()==4){
                //还款中(放款后)
                statusStr="1";
            }else if (borrow.getStatus()==5){
                statusStr="3";
            }
            Query query = new Query(Criteria.where("source_product_code").is(borrow.getBorrowNid()).and("product_status").is(statusStr));
            //1上报成功 7重复上报 满足这两种条件则不再上报
            BifaBorrowStatusEntity borrowStatus= bifaBorrowStatusDao.findOne(query);
            if (borrowStatus != null){
                if (!"1".equals(borrowStatus.getReportStatus()) && !"7".equals(borrowStatus.getReportStatus())){
                    // -->重新上报产品状态更新
                    String methodName = "productStatusUpdate";
                    BifaBorrowStatusEntity result = this.reportData(methodName,borrowStatus);
                    Update update = new Update();
                    update.set("reportStatus", result.getReportStatus());
                    update.set("errCode",borrowStatus.getErrCode());
                    update.set("errDesc",borrowStatus.getErrDesc());
                    update.set("updateTime",GetDate.getDate());
                    bifaBorrowStatusDao.findAndModify(query,update,BifaCommonConstants.HT_BIFA_BORROW_STATUS);
                    if ("1".equals(result.getReportStatus()) || "7".equals(result.getReportStatus())) {
                        _log.info(logHeader + "上报产品状态更新异常数据修复成功！！"+JSONObject.toJSONString(borrowStatus));
                    }else if ("9".equals(result.getReportStatus())){
                        _log.error(logHeader + "上报产品状态更新异常数据修复失败！！"+JSONObject.toJSONString(borrowStatus));
                    }
                }
            }else {
                _log.error(logHeader + "从mongo库ht_bifa_borrow_status集合中获取产品状态信息失败!!!,source_product_code:"+source_product_code);
            }

        }

    }

    /**
     * 获取散标信息
     * @param
     * @return
     */
    private Borrow selectBorrowInfo(String borrowNid) {
        BorrowExample borrowExample = new BorrowExample();
        BorrowExample.Criteria borrowCra = borrowExample.createCriteria();
        //只获取 status等于4 状态为还款中
        borrowCra.andBorrowNidEqualTo(borrowNid);
        List<Borrow> borrowList = this.borrowMapper.selectByExample(borrowExample);
        if(CollectionUtils.isNotEmpty(borrowList)){
            return borrowList.get(0);
        }
        return null;
    }

    /**
     * 散标数据重新上报
     */
    private void bifaBorrowInfoReportAgain() {
        List<BifaBorrowInfoEntity> list = bifaBorrowInfoDao.find(new Query(Criteria.where("reportStatus").is("9")));
        if (CollectionUtils.isNotEmpty(list)){
            for (BifaBorrowInfoEntity entity : list) {
                try {
                    String methodName = "productRegistration";
                    BifaBorrowInfoEntity result = this.reportData(methodName,entity);
                    Query query = new Query(Criteria.where("_id").is(entity.getId()));
                    Update update = new Update();
                    update.set("reportStatus", result.getReportStatus());
                    update.set("errCode",result.getErrCode());
                    update.set("errDesc",result.getErrDesc());
                    update.set("updateTime",GetDate.getDate());
                    bifaBorrowInfoDao.findAndModify(query,update,BifaCommonConstants.HT_BIFA_BORROWINFO);
                    //1上报成功  7重复上报
                    if ("1".equals(result.getReportStatus()) || "7".equals(result.getReportStatus())) {
                        _log.info(logHeader + "上报散标异常数据修复成功！！"+JSONObject.toJSONString(result));
                    }else if ("9".equals(result.getReportStatus())){
                        _log.error(logHeader + "上报散标异常数据修复失败！！"+JSONObject.toJSONString(result));
                    }
                } catch (Exception e) {
                    _log.error(logHeader + "上报散标异常数据修复失败！！", e);
                }
            }

        }
    }


}
