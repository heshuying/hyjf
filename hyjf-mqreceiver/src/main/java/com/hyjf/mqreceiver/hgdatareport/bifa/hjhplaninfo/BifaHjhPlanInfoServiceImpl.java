/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 *
 * @author: lb
 * @version: 1.0
 * Created at: 2017年9月15日 上午9:43:49
 * Modification History:
 * Modified by :
 */

package com.hyjf.mqreceiver.hgdatareport.bifa.hjhplaninfo;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisConstants;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.DigitalUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.hgdatareport.dao.BifaHjhPlanInfoDao;
import com.hyjf.mongo.hgdatareport.entity.BifaBorrowInfoBean;
import com.hyjf.mongo.hgdatareport.entity.BifaHjhPlanInfoEntity;
import com.hyjf.mqreceiver.hgdatareport.BaseHgDateReportServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.bifa.borrowinfo.BifaBorrowInfoService;
import com.hyjf.mqreceiver.hgdatareport.bifa.credittenderinfo.BifaCreditTenderInfoHandle;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhPlanExample;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author liubin
 */

@Service
public class BifaHjhPlanInfoServiceImpl extends BaseHgDateReportServiceImpl implements BifaHjhPlanInfoService {

    Logger _log = LoggerFactory.getLogger(BifaCreditTenderInfoHandle.class);

    private String thisMessName = "智投信息上报";
    private String logHeaderHjhPlanInfo = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_BIFA + " " + thisMessName + "】";

    @Autowired
    BifaHjhPlanInfoDao bifaHjhPlanInfoDao;
    @Autowired
    BifaBorrowInfoService bifaBorrowInfoService;

    @Override
    public BifaHjhPlanInfoEntity getBifaHjhPlanInfoFromMongoDB(String planNid) {
        //失败的情况留给batch处理
        return bifaHjhPlanInfoDao.findOne(new Query(Criteria.where("source_product_code").is(planNid)));
    }

    @Override
    public HjhPlan selectHjhPlanInfo(String planNid) {
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

    @Override
    public boolean convertBifaHjhPlanInfo(HjhPlan hjhplan, BifaHjhPlanInfoEntity bifaHjhPlanInfoEntity) {

        try {
            bifaHjhPlanInfoEntity.setProduct_reg_type("02");
            bifaHjhPlanInfoEntity.setProduct_name(hjhplan.getPlanName());
            bifaHjhPlanInfoEntity.setProduct_mark("智投服务");
            bifaHjhPlanInfoEntity.setSource_code(SOURCE_CODE);
            bifaHjhPlanInfoEntity.setSource_product_code(hjhplan.getPlanNid());
            bifaHjhPlanInfoEntity.setPlan_raise_amount("1000000");
            bifaHjhPlanInfoEntity.setRate(this.convertRateHjhPlan(hjhplan.getBorrowStyle(),hjhplan.getExpectApr()));
            bifaHjhPlanInfoEntity.setTerm_type(this.convertTermType(hjhplan.getBorrowStyle()));
            bifaHjhPlanInfoEntity.setTerm(String.valueOf(hjhplan.getLockPeriod()));
            bifaHjhPlanInfoEntity.setIsshow("1");
            bifaHjhPlanInfoEntity.setRemark("无");
            bifaHjhPlanInfoEntity.setAmount_limmts(hjhplan.getMinInvestment().toString());
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

    @Override
    public void insertReportData(BifaHjhPlanInfoEntity data) {
        bifaHjhPlanInfoDao.insert(data);
    }

    @Override
    public boolean convertBifaHjhPlanInfo(HjhPlan hjhplan, Borrow borrow, Map<String, String> borrowUserInfo, BifaHjhPlanInfoEntity bifaHjhPlanInfoEntity) {
        try {
            bifaHjhPlanInfoEntity.setProduct_reg_type("02");
            bifaHjhPlanInfoEntity.setProduct_name(hjhplan.getPlanName());
            bifaHjhPlanInfoEntity.setProduct_mark("智投服务");
            bifaHjhPlanInfoEntity.setSource_code(SOURCE_CODE);
            bifaHjhPlanInfoEntity.setSource_product_code(hjhplan.getPlanNid());
            bifaHjhPlanInfoEntity.setPlan_raise_amount("1000000");
            bifaHjhPlanInfoEntity.setRate(this.convertRateHjhPlan(hjhplan.getBorrowStyle(),hjhplan.getExpectApr()));
            bifaHjhPlanInfoEntity.setTerm_type(this.convertTermType(hjhplan.getBorrowStyle()));
            bifaHjhPlanInfoEntity.setTerm(String.valueOf(hjhplan.getLockPeriod()));
            bifaHjhPlanInfoEntity.setIsshow("1");
            bifaHjhPlanInfoEntity.setRemark("无");
            bifaHjhPlanInfoEntity.setAmount_limmts(hjhplan.getMinInvestment().toString());
            bifaHjhPlanInfoEntity.setAmount_limmtl(DigitalUtils.min(new BigDecimal(RedisUtils.get(RedisConstants.REVALUATION_AGGRESSIVE)), hjhplan.getMaxInvestment()).toString());
            bifaHjhPlanInfoEntity.setRed_rate("超出参考回报部分作为服务费用");
            bifaHjhPlanInfoEntity.setSource_product_url(MessageFormat.format(SOURCE_PRODUCT_URL_HJHPLAN,hjhplan.getPlanNid()));
            //计划关联标的
            BifaBorrowInfoBean bifaBorrowInfoBean=this.convertHjhPlanBorrowInfo(borrow,borrowUserInfo);
            bifaHjhPlanInfoEntity.getBorrowerlist().add(bifaBorrowInfoBean);
            Date currDate =GetDate.getDate();
            bifaHjhPlanInfoEntity.setCreateTime(currDate);
            bifaHjhPlanInfoEntity.setUpdateTime(currDate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 天标和月标的利率分开处理
     * @param borrowStyle
     * @param expectApr
     * @return
     */
    private String convertRateHjhPlan(String borrowStyle,BigDecimal expectApr) {
        if (CalculatesUtil.STYLE_ENDDAY.equals(borrowStyle)) {
            //天智投利率＝年利率*30/365
            BigDecimal bd12 = new BigDecimal("36500");
            BigDecimal divide =expectApr.multiply(new BigDecimal(30)).divide(bd12,6,RoundingMode.DOWN);
            return divide.toString();
        } else {
            //月智投利率＝年利率/12
            BigDecimal bd12 = new BigDecimal("1200");
            BigDecimal divide =expectApr.divide(bd12,6,RoundingMode.DOWN);
            return divide.toString();
        }
    }

    @Override
    public List<BifaHjhPlanInfoEntity> getCountFromMongoDB() {
        List<BifaHjhPlanInfoEntity> datas=bifaHjhPlanInfoDao.find(new Query());
        return datas;
    }

    @Override
    public List<HjhPlan> selectHjhPlanInfoList() {
        HjhPlanExample example = new HjhPlanExample();
        List<HjhPlan> list = this.hjhPlanMapper.selectByExample(example);
        return list;
    }

    @Override
    public BifaHjhPlanInfoEntity checkRelaHjhPlanIsReported(String planNid) throws Exception {
        BifaHjhPlanInfoEntity bhpiFromMongoDB = this.getBifaHjhPlanInfoFromMongoDB(planNid);
        if (null == bhpiFromMongoDB) {
            //不存在该放款标的所对应的智投时拉取该智投信息
            //拉取智投信息并且上报
            HjhPlan hjhplan = this.selectHjhPlanInfo(planNid);
            if (null == hjhplan) {
                throw new Exception(logHeaderHjhPlanInfo + "未获取到智投信息！！" + "planNid:" + planNid);
            }
            // --> 数据变换
            bhpiFromMongoDB = new BifaHjhPlanInfoEntity();
            boolean result = this.convertBifaHjhPlanInfo(hjhplan, bhpiFromMongoDB);
            if (!result) {
                throw new Exception(logHeaderHjhPlanInfo + "智投中的放款标的数据变换失败！！" + JSONObject.toJSONString(bhpiFromMongoDB));
            }
            // --> 上报数据（实时上报）
            //上报数据失败时 将数据存放到mongoDB
            String methodName = "productRegistration";
            BifaHjhPlanInfoEntity reportResult = this.reportData(methodName, bhpiFromMongoDB);
            if ("9".equals(reportResult.getReportStatus())) {
                _log.info(logHeaderHjhPlanInfo + "上报智投中的放款标的数据失败！！" + JSONObject.toJSONString(bhpiFromMongoDB));
            } else if ("1".equals(reportResult.getReportStatus())) {
                _log.info(logHeaderHjhPlanInfo + "上报智投中的放款标的数据成功。" + JSONObject.toJSONString(bhpiFromMongoDB));
            }
            // --> 保存上报数据
            this.insertReportData(bhpiFromMongoDB);
            _log.info(logHeaderHjhPlanInfo + "智投中的放款标的数据保存本地！！" + JSONObject.toJSONString(bhpiFromMongoDB));
        }

        return bhpiFromMongoDB;
    }

    @Override
    public BifaHjhPlanInfoEntity removeBorrowNid(BifaHjhPlanInfoEntity bifaHjhPlanInfoEntity) {
        //创建副本
        BifaHjhPlanInfoEntity result = bifaHjhPlanInfoEntity.cloneObj();

        //除掉borrowlist中每个对象的source_product_code
        if (result!=null && CollectionUtils.isNotEmpty(result.getBorrowerlist())){
            for (BifaBorrowInfoBean borrowInfo : result.getBorrowerlist()) {
                borrowInfo.setSource_product_code(null);
            }
        }
        return result;
    }


    /**
     * 转换汇计划内的标的信息
     * @param borrow
     * @return
     */
    private BifaBorrowInfoBean convertHjhPlanBorrowInfo(Borrow borrow, Map<String, String> borrowUserInfo) {
        BifaBorrowInfoBean bifaBorrowInfoBean = new BifaBorrowInfoBean();
        bifaBorrowInfoBean.setSource_product_code(borrow.getBorrowNid());
        bifaBorrowInfoBean.setBorrowamt(borrow.getAccount().toString());
        bifaBorrowInfoBean.setTerm_type(this.convertTermType(borrow.getBorrowStyle()));
        bifaBorrowInfoBean.setTerm(String.valueOf(borrow.getBorrowPeriod()));
        bifaBorrowInfoBean.setBegindate(GetDate.times10toStrYYYYMMDD(borrow.getRecoverLastTime()));
        bifaBorrowInfoBean.setEnddate(GetDate.times10toStrYYYYMMDD(Integer.parseInt(borrow.getRepayLastTime())));
        bifaBorrowInfoBean.setBorrow_name_idcard_digest(selectUserIdToSHA256(null, borrowUserInfo.get("trueName"),borrowUserInfo.get("idCard")).getSha256());
        return bifaBorrowInfoBean;
    }

}
