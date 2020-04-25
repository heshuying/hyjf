package com.hyjf.mqreceiver.hgdatareport.cert.olddata.creditor;

import com.alibaba.fastjson.JSONArray;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.mongo.hgdatareport.dao.CertBorrowDao;
import com.hyjf.mongo.hgdatareport.entity.CertBorrowEntity;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.cert.undertake.CertCreditInfoService;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallConstant;
import com.hyjf.mqreceiver.hgdatareport.common.CertCallUtil;
import com.hyjf.mybatis.mapper.auto.BorrowRecoverMapper;
import com.hyjf.mybatis.mapper.auto.BorrowTenderMapper;
import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


/**
 * @author nxl
 */

@Service
public class CertOldTenderInfoServiceImpl extends BaseHgCertReportServiceImpl implements CertOldTenderInfoService {

    @Autowired
    private CertBorrowDao certBorrowDao;
    Logger logger = LoggerFactory.getLogger(CertOldTenderInfoServiceImpl.class);
    private String thisMessName = "债权信息信息推送";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";

    /**
     * 查找数据库里待上债权信息的数据
     *
     * @return
     */
    @Override
    public List<CertBorrow> insertCertBorrowCreditList() {
        CertBorrowExample example = new CertBorrowExample();
        CertBorrowExample.Criteria cra = example.createCriteria();
        //1 代表已上报,0:未上报,99:上报错误
        // 用户信息已上传
        cra.andIsUserInfoEqualTo(1);
        // 散标数据已上报
        cra.andIsScatterEqualTo(1);
        //散标状态已上报
        cra.andIsStatusEqualTo(1);
        //还款计划已上报
//        cra.andIsRepayPlanEqualTo(1);
        //债权信息未上报
        cra.andIsCreditEqualTo(0);

        example.setLimitStart(0);
        example.setLimitEnd(3000);
        List<CertBorrow> listStatus = certBorrowMapper.selectByExample(example);
        return listStatus;
    }

}
