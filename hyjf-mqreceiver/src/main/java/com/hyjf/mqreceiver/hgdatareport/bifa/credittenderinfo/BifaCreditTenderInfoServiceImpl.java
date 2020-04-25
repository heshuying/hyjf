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

package com.hyjf.mqreceiver.hgdatareport.bifa.credittenderinfo;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.MessageFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mongo.hgdatareport.dao.BifaCreditTenderInfoDao;
import com.hyjf.mongo.hgdatareport.entity.BifaBorrowInfoEntity;
import com.hyjf.mongo.hgdatareport.entity.BifaCreditTenderInfoEntity;
import com.hyjf.mqreceiver.hgdatareport.BaseHgDateReportServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.bifa.BifaCommonConstants;
import com.hyjf.mqreceiver.hgdatareport.bifa.borrowinfo.BifaBorrowInfoService;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowCarinfo;
import com.hyjf.mybatis.model.auto.BorrowCredit;
import com.hyjf.mybatis.model.auto.BorrowCreditExample;
import com.hyjf.mybatis.model.auto.BorrowHouses;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.HjhDebtCreditExample;
import com.hyjf.mybatis.model.auto.UsersInfo;


/**
 * @author liubin
 */

@Service
public class BifaCreditTenderInfoServiceImpl extends BaseHgDateReportServiceImpl implements BifaCreditTenderInfoService {


    @Autowired
    BifaCreditTenderInfoDao bifaCreditTenderInfoDao;

    @Autowired
    private BifaBorrowInfoService bifaBorrowInfoService;

    @Override
    public BifaCreditTenderInfoEntity getBifaBorrowCreditInfoFromMongDB(String credit) {
        //失败的情况留给batch处理
        return bifaCreditTenderInfoDao.findOne(new Query(Criteria.where("source_product_code").is(credit)));
    }

    @Override
    public BorrowCredit selectBorrowCreditInfo(String creditNid) {
        BorrowCreditExample borrowCreditExample = new BorrowCreditExample();
        BorrowCreditExample.Criteria borrowCreditCra = borrowCreditExample.createCriteria();
        borrowCreditCra.andCreditNidEqualTo(Integer.parseInt(creditNid));
        List<BorrowCredit> list = this.borrowCreditMapper.selectByExample(borrowCreditExample);
        if (CollectionUtils.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    @Override
    public boolean convertBifaBorrowCreditInfo(BorrowCredit borrowCredit, Borrow borrow, UsersInfo creditUserInfo, BifaCreditTenderInfoEntity bifaCreditInfoEntity){
        try {
            bifaCreditInfoEntity.setProduct_reg_type("03");
            bifaCreditInfoEntity.setProduct_name(borrow.getProjectName());
            bifaCreditInfoEntity.setProduct_mark(this.convertProductMark(borrow.getProjectType()));
            bifaCreditInfoEntity.setSource_code(SOURCE_CODE);
            bifaCreditInfoEntity.setSource_product_code(BifaCommonConstants.HZR+borrowCredit.getCreditNid());
            bifaCreditInfoEntity.setTransfer_sex(this.convertSex(creditUserInfo.getSex()));
            bifaCreditInfoEntity.setHold_time(this.getHoldTime(borrow.getBorrowPeriod(),borrow.getBorrowStyle()));
            bifaCreditInfoEntity.setOverplus_time(String.valueOf(borrowCredit.getCreditTerm())+"天");
            bifaCreditInfoEntity.setAmt(borrowCredit.getCreditCapital().toString());
            bifaCreditInfoEntity.setTransfer_rate(this.convertTransferRate(borrowCredit.getBidApr()));
            bifaCreditInfoEntity.setTransfer_fee(this.getTransferFee(borrowCredit.getCreditNid()));
            bifaCreditInfoEntity.setRemark("无");
            bifaCreditInfoEntity.setSource_product_url(MessageFormat.format(SOURCE_PRODUCT_URL_BORROW_CREDIT,BifaCommonConstants.HZR + borrowCredit.getCreditNid()));
            bifaCreditInfoEntity.setTransfer_name_idcard_digest(selectUserIdToSHA256(creditUserInfo.getUserId(), creditUserInfo.getTruename(),creditUserInfo.getIdcard()).getSha256());
            Date currDate =GetDate.getDate();
            bifaCreditInfoEntity.setCreateTime(currDate);
            bifaCreditInfoEntity.setUpdateTime(currDate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }




    @Override
    public boolean insertReportData(BifaCreditTenderInfoEntity data) {
        bifaCreditTenderInfoDao.insert(data);
        return true;
    }

    @Override
    public HjhDebtCredit selectHjhDebtCreditInfo(String creditNid) {
        HjhDebtCreditExample example = new HjhDebtCreditExample();
        HjhDebtCreditExample.Criteria criteria = example.createCriteria();
        criteria.andCreditNidEqualTo(creditNid);
        List<HjhDebtCredit> list = this.hjhDebtCreditMapper.selectByExample(example);
        if (CollectionUtils.isNotEmpty(list)){
            return list.get(0);
        }
        return null;
    }

    @Override
    public boolean convertBifaHjhCreditInfo(HjhDebtCredit hjhDebtCredit, Borrow borrow, UsersInfo creditUserInfo, BifaCreditTenderInfoEntity bifaCreditInfoEntity) {
        try {
            bifaCreditInfoEntity.setProduct_reg_type("03");
            bifaCreditInfoEntity.setProduct_name(borrow.getProjectName());
            bifaCreditInfoEntity.setProduct_mark(this.convertProductMark(borrow.getProjectType()));
            bifaCreditInfoEntity.setSource_code(SOURCE_CODE);
            bifaCreditInfoEntity.setSource_product_code(BifaCommonConstants.HZR+hjhDebtCredit.getCreditNid());
            bifaCreditInfoEntity.setTransfer_sex(this.convertSex(creditUserInfo.getSex()));
            bifaCreditInfoEntity.setHold_time(this.getHoldTime(borrow.getBorrowPeriod(),borrow.getBorrowStyle()));
            bifaCreditInfoEntity.setOverplus_time(String.valueOf(hjhDebtCredit.getRemainDays())+"天");
            bifaCreditInfoEntity.setAmt(hjhDebtCredit.getCreditCapital().toString());
            bifaCreditInfoEntity.setTransfer_rate(this.convertTransferRate(hjhDebtCredit.getBorrowApr()));
            bifaCreditInfoEntity.setTransfer_fee(this.getHjhTransferFee(hjhDebtCredit.getCreditNid()));
            bifaCreditInfoEntity.setRemark("无");
            bifaCreditInfoEntity.setSource_product_url(MessageFormat.format(SOURCE_PRODUCT_URL_HJH_CREDIT,hjhDebtCredit.getPlanNid()));
            bifaCreditInfoEntity.setTransfer_name_idcard_digest(selectUserIdToSHA256(creditUserInfo.getUserId(), creditUserInfo.getTruename(),creditUserInfo.getIdcard()).getSha256());
            Date currDate =GetDate.getDate();
            bifaCreditInfoEntity.setCreateTime(currDate);
            bifaCreditInfoEntity.setUpdateTime(currDate);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String getHoldTime(Integer borrowPeriod, String borrowStyle) {
        // 是否月标(true:月标, false:天标)
    	 boolean isDay = false;
         if (CustomConstants.BORROW_STYLE_ENDDAY.equals(borrowStyle)) {
        	 isDay = true;
         } else {
        	 isDay = false;
         }
        if (isDay){
            return borrowPeriod+"天";
        }else {
            return borrowPeriod+"月";
        }
    }


    /**
     * 散标转让服务费
     * @param creditNid
     * @return
     */
    private String getTransferFee(Integer creditNid) {
        return this.creditTenderMapper.selectByCreditNid(String.valueOf(creditNid)).toString();
    }

    /**
     * 智投转让服务费
     * @param creditNid
     * @return
     */
    private String getHjhTransferFee(String creditNid) {
        return this.hjhDebtCreditTenderMapper.selectServiceFeeSumByCreditNid(creditNid).toString();
    }

    /**
     * 转让月利率
     * @param bidApr
     * @return
     */
    private String convertTransferRate(BigDecimal bidApr) {
        //12期 百分号转小数
        BigDecimal bd12 = new BigDecimal("1200");
        BigDecimal divide =bidApr.divide(bd12,6,RoundingMode.DOWN);
        return divide.toString();
    }

}
