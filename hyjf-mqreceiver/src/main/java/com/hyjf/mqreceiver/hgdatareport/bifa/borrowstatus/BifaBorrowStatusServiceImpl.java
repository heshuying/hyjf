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

package com.hyjf.mqreceiver.hgdatareport.bifa.borrowstatus;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.hgdatareport.dao.BifaBorrowStatusDao;
import com.hyjf.mongo.hgdatareport.entity.BifaBorrowStatusEntity;
import com.hyjf.mongo.hgdatareport.entity.InvestorBean;
import com.hyjf.mqreceiver.hgdatareport.BaseHgDateReportServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.bifa.borrowinfo.BifaBorrowInfoService;
import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * @author liubin
 */

@Service
public class BifaBorrowStatusServiceImpl extends BaseHgDateReportServiceImpl implements BifaBorrowStatusService {

    Logger _log = LoggerFactory.getLogger(BifaBorrowStatusServiceImpl.class);
    private String thisMessName = "产品状态更新信息上报";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_BIFA + " " + thisMessName + "】";
    
    @Autowired
    BifaBorrowStatusDao bifaBorrowStatusDao;

    //散标
    @Autowired
    private BifaBorrowInfoService bifaBorrowInfoService;

    @Override
    public BifaBorrowStatusEntity getBifaBorrowStatusFromMongoDB(String borrowNid,Integer status) {
        String statusStr = "";
        switch (status){
            case 4:
                //还款中(放款后)
                statusStr="1";
                break;
            case 5:
                //最后一笔还款完后
                statusStr="3";
                break;
            default:
                break;
        }
        //失败的情况留给batch处理
        return bifaBorrowStatusDao.findOne(new Query(Criteria.where("source_product_code").is(borrowNid).and("product_status").is(statusStr)));
    }


    @Override
    public List<BorrowTender> selectBorrowTenders(String borrowNid) {
        BorrowTenderExample example = new BorrowTenderExample();
        BorrowTenderExample.Criteria cra = example.createCriteria();
        cra.andBorrowNidEqualTo(borrowNid);
        List<BorrowTender> list = this.borrowTenderMapper.selectByExample(example);
        return list;
    }


    @Override
    public boolean convertBorrowStatus(Borrow borrow, List<BorrowTender> borrowTenders, BifaBorrowStatusEntity bifaBorrowStatusEntity) {
        try {
            if (4==borrow.getStatus()){
                //标的放款后
                bifaBorrowStatusEntity.setSource_code(SOURCE_CODE);
                //智投下的散标报送智投编号
                if (StringUtils.isNotEmpty(borrow.getPlanNid())){
                    bifaBorrowStatusEntity.setSource_product_code(borrow.getPlanNid());
                }else {
                    bifaBorrowStatusEntity.setSource_product_code(borrow.getBorrowNid());
                }
                bifaBorrowStatusEntity.setProduct_status("1");
                bifaBorrowStatusEntity.setProduct_status_desc("满标");
                String recoverLastTime = "";
                if (borrow.getRecoverLastTime() != null){
                    recoverLastTime = GetDate.times10toStrYYYYMMDD(borrow.getRecoverLastTime());
                }
                bifaBorrowStatusEntity.setBegindate(recoverLastTime);

                //最后一期还款后 包含分期和不分期两种业务场景
                String lastRepayTime=this.getLastRepayTime(borrow);

                bifaBorrowStatusEntity.setEnddate(lastRepayTime);
                bifaBorrowStatusEntity.setProduct_date(GetDate.formatDate());

                List<InvestorBean> list = new ArrayList<InvestorBean>();
                for (BorrowTender borrowTender : borrowTenders) {
                    UsersInfo ui=this.getUsersInfoByUserId(borrowTender.getUserId());
                    InvestorBean entity = new InvestorBean();
                    entity.setInvest_amt(borrowTender.getAccount().toString());
                    entity.setInvestor_name_idcard_digest(selectUserIdToSHA256(ui.getUserId(), ui.getTruename(),ui.getIdcard()).getSha256());
                    list.add(entity);
                }
                bifaBorrowStatusEntity.setInvestorlist(list);

            }else if (5==borrow.getStatus()){
                //最后一期还款后 包含分期和不分期两种业务场景
                bifaBorrowStatusEntity.setSource_code(SOURCE_CODE);
                //智投下的散标报送智投编号
                if (StringUtils.isNotEmpty(borrow.getPlanNid())){
                    bifaBorrowStatusEntity.setSource_product_code(borrow.getPlanNid());
                }else {
                    bifaBorrowStatusEntity.setSource_product_code(borrow.getBorrowNid());
                }

                bifaBorrowStatusEntity.setProduct_status("3");
                bifaBorrowStatusEntity.setProduct_status_desc("还款结束");
                String recoverLastTime = "";
                if (borrow.getRecoverLastTime() != null){
                    recoverLastTime = GetDate.times10toStrYYYYMMDD(borrow.getRecoverLastTime());
                }
                bifaBorrowStatusEntity.setBegindate(recoverLastTime);
                String lastRepayTime=this.getLastRepayTime(borrow);
                bifaBorrowStatusEntity.setEnddate(lastRepayTime);
                bifaBorrowStatusEntity.setProduct_date(GetDate.formatDate());

                List<InvestorBean> list = new ArrayList<InvestorBean>();
                for (BorrowTender borrowTender : borrowTenders) {
                    UsersInfo ui=this.getUsersInfoByUserId(borrowTender.getUserId());
                    InvestorBean entity = new InvestorBean();
                    entity.setInvest_amt(borrowTender.getAccount().toString());
                    entity.setInvestor_name_idcard_digest(selectUserIdToSHA256(ui.getUserId(), ui.getTruename(),ui.getIdcard()).getSha256());
                    list.add(entity);
                }
                bifaBorrowStatusEntity.setInvestorlist(list);
            }


            Date currDate =GetDate.getDate();
            bifaBorrowStatusEntity.setCreateTime(currDate);
            bifaBorrowStatusEntity.setUpdateTime(currDate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    private String getLastRepayTime(Borrow borrow) {
        boolean isMonth = false;
        if (CustomConstants.BORROW_STYLE_MONTH.equals(borrow.getBorrowStyle()) || CustomConstants.BORROW_STYLE_ENDMONTH.equals(borrow.getBorrowStyle()) 
        		|| CustomConstants.BORROW_STYLE_PRINCIPAL.equals(borrow.getBorrowStyle())) {
            isMonth = true;
        } else if (CustomConstants.BORROW_STYLE_END.equals(borrow.getBorrowStyle()) || CustomConstants.BORROW_STYLE_ENDDAY.equals(borrow.getBorrowStyle())) {
            isMonth = false;
        }
        String lastRepayTime="";
        if (isMonth){
            //分期
            List<BorrowRepayPlan> borrowRepayPlans = this.getBorrowRepayPlan(borrow.getBorrowNid());
            if (CollectionUtils.isNotEmpty(borrowRepayPlans)){
                lastRepayTime=borrowRepayPlans.get(borrowRepayPlans.size()-1).getRepayTime();
            }
        }else {
            //不分期
            BorrowRepay borrowRepay = this.getBorrowRepay(borrow.getBorrowNid());
            lastRepayTime = borrowRepay.getRepayTime();
        }
        lastRepayTime = GetDate.times10toStrYYYYMMDD(Integer.parseInt(lastRepayTime));
        return lastRepayTime;
    }


    @Override
    public void insertReportData(BifaBorrowStatusEntity data) {
        bifaBorrowStatusDao.insert(data);
    }


    /**
     * 分期还款
     * @param borrowNid
     * @return
     */
    private List<BorrowRepayPlan> getBorrowRepayPlan(String borrowNid) {
        BorrowRepayPlanExample example = new BorrowRepayPlanExample();
        BorrowRepayPlanExample.Criteria criteria = example.createCriteria();
        // 标的编号
        criteria.andBorrowNidEqualTo(borrowNid);
        // 按照期数升序排列
        example.setOrderByClause("repay_period");
        return this.borrowRepayPlanMapper.selectByExample(example);
    }


    private BorrowRepay getBorrowRepay(String borrowNid) {
        BorrowRepayExample example = new BorrowRepayExample();
        BorrowRepayExample.Criteria criteria = example.createCriteria();
        // 标的编号
        criteria.andBorrowNidEqualTo(borrowNid);
        List<BorrowRepay> repayList = this.borrowRepayMapper.selectByExample(example);
        if(null != repayList && repayList.size() > 0){
            return repayList.get(0);
        }
        return null;

    }
    
    
    @Override
    public void checkBifaBorrowStatusIsReported(Borrow borrow) throws Exception {
            // --> 增加防重校验（根据不同平台不同上送方式校验不同）
            BifaBorrowStatusEntity bifaBorrowStatusEntity =
                    this.getBifaBorrowStatusFromMongoDB(borrow.getBorrowNid(),borrow.getStatus());
            if (null != bifaBorrowStatusEntity) {
                // 已经上报成功
                throw new Exception(logHeader + " 已经上报。" + JSONObject.toJSONString(bifaBorrowStatusEntity));
            }
            if (null == bifaBorrowStatusEntity) {
                // --> 拉数据
                //标的投资信息
                List<BorrowTender> borrowTenders = this.selectBorrowTenders(borrow.getBorrowNid());
                if (CollectionUtils.isEmpty(borrowTenders)){
                    throw new Exception(logHeader + "未获取到标的投资信息！！"+"borrowNid:"+borrow.getBorrowNid());
                }
                // --> 数据变换
                bifaBorrowStatusEntity= new BifaBorrowStatusEntity();
                boolean resultStatusUpdate = this.convertBorrowStatus(borrow,borrowTenders,bifaBorrowStatusEntity);
                if (!resultStatusUpdate){
                    throw new Exception(logHeader + "数据变换失败！！"+JSONObject.toJSONString(bifaBorrowStatusEntity));
                }

                // --> 上报数据（实时上报）
                //上报数据失败时 将数据存放到mongoDB
                String methodNameStatusUpdate = "productStatusUpdate";
                BifaBorrowStatusEntity reportResultStatusUpdate = this.reportData(methodNameStatusUpdate,bifaBorrowStatusEntity);
                if("1".equals(reportResultStatusUpdate.getReportStatus()) || "7".equals(reportResultStatusUpdate.getReportStatus())){
                    _log.info(logHeader + "上报数据成功！！"+JSONObject.toJSONString(reportResultStatusUpdate));
                } else if ("9".equals(reportResultStatusUpdate.getReportStatus())) {
                    _log.error(logHeader + "上报数据失败！！"+JSONObject.toJSONString(reportResultStatusUpdate));
                }
                // --> 保存上报数据
                this.insertReportData(reportResultStatusUpdate);
                _log.info(logHeader + "上报数据保存本地！！"+JSONObject.toJSONString(reportResultStatusUpdate));

            }

    }
}
