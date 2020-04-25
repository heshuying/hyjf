package com.hyjf.mqreceiver.hgdatareport.cert.olddata.borrow;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.mongo.hgdatareport.dao.CertBorrowDao;
import com.hyjf.mongo.hgdatareport.entity.CertBorrowEntity;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportServiceImpl;
import com.hyjf.mqreceiver.hgdatareport.cert.olddata.borrowuser.CertOldBorrowUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * 合规数据上报 CERT 国家互联网应急中心 标的老数据上报
 * @author sss
 */

@Service
public class CertOldBorrowServiceImpl extends BaseHgCertReportServiceImpl implements CertOldBorrowService {
    Logger logger = LoggerFactory.getLogger(CertOldBorrowServiceImpl.class);

    private String thisMessName = "标的老数据上报";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";

    @Autowired
    private CertBorrowDao certBorrowDao;

    /**
     * 查未上报的标的
     *
     * @return
     */
    @Override
    public List<CertBorrowEntity> getCertBorrow() {
        return certBorrowDao.getCertBorrow();
    }

    /**
     * 修改结果
     *
     * @param users
     */
    @Override
    public void updateResult(List<Map<String,String>> param) {
        certBorrowDao.updateAll(param);
    }
}
