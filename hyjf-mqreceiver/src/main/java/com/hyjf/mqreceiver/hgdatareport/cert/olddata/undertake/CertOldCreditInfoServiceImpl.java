package com.hyjf.mqreceiver.hgdatareport.cert.olddata.undertake;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.cert.undertake.CertCreditInfoService;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallUtil;
import com.hyjf.mybatis.mapper.auto.*;
import com.hyjf.mybatis.mapper.customize.coupon.CouponTenderCustomizeMapper;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderCustomize;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;


/**
 * @author nxl
 */

@Service
public class CertOldCreditInfoServiceImpl extends BaseHgCertReportServiceImpl implements CertOldCreditInfoService {
    Logger logger = LoggerFactory.getLogger(CertOldCreditInfoServiceImpl.class);
    private String thisMessName = "承接订单信息推送";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";
    @Autowired
    private CertCreditInfoService certCreditInfoService;

    /**
     * 查找数据库里待上承接信息的数据
     *
     * @return
     */
    @Override
    public List<CertBorrow> getCertBorrowUnderTakeList() {
        CertBorrowExample example = new CertBorrowExample();
        CertBorrowExample.Criteria cra = example.createCriteria();
        //1 代表已上报,0:未上报,99:上报错误
        // 用户信息已上传
        cra.andIsUserInfoEqualTo(1);
        // 散标数据已上报
        cra.andIsScatterEqualTo(1);
        //散标状态已上报
        cra.andIsStatusEqualTo(1);
        //承接信息未上报
        cra.andIsUndertakeEqualTo(0);
        example.setLimitStart(0);
        example.setLimitEnd(1500);
        List<CertBorrow> listStatus = certBorrowMapper.selectByExample(example);
        return listStatus;
    }

    /**
     * 查找汇计划债转投资表
     *
     * @param borrowNid
     * @return
     */
    public List<HjhDebtCreditTender> getHjhCreditTenderListByBorrowNid(String borrowNid) {
        HjhDebtCreditTenderExample example = new HjhDebtCreditTenderExample();
        HjhDebtCreditTenderExample.Criteria cra = example.createCriteria();
        cra.andBorrowNidEqualTo(borrowNid);
        List<HjhDebtCreditTender> borrowTenderList = hjhDebtCreditTenderMapper.selectByExample(example);
        return borrowTenderList;
    }

    /**
     * 查找散标承接信息
     *
     * @param borrowNid
     * @return
     */
    public List<CreditTender> getBorrowCreditTenderList(String borrowNid) {
        CreditTenderExample example = new CreditTenderExample();
        CreditTenderExample.Criteria cra = example.createCriteria();
        cra.andBidNidEqualTo(borrowNid);
        List<CreditTender> creditTenderList = creditTenderMapper.selectByExample(example);
        return creditTenderList;
    }

    /**
     * 获取承接信息,组装数据
     *
     * @param borrowNid
     * @return
     */
    @Override
    public JSONArray getCertOldCreditInfos(String borrowNid,JSONArray jsonArray) {
        List<HjhDebtCreditTender> hjhDebtCreditTenderList = getHjhCreditTenderListByBorrowNid(borrowNid);
        if (null != hjhDebtCreditTenderList && hjhDebtCreditTenderList.size() > 0) {
            jsonArray = certCreditInfoService.getHjhDebtCreditInfo(hjhDebtCreditTenderList,jsonArray,true);
        } else {
            List<CreditTender> creditTenderList = getBorrowCreditTenderList(borrowNid);
            jsonArray = certCreditInfoService.getBorrowCreditTenderInfo(creditTenderList,jsonArray,true);
        }
        return jsonArray;
    }

}
