package com.hyjf.mqreceiver.hgdatareport.cert.olddata.borrowuser;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportServiceImpl;
import com.hyjf.mybatis.model.auto.CertBorrow;
import com.hyjf.mybatis.model.auto.CertBorrowExample;
import com.hyjf.mybatis.model.customize.cert.CertBorrowUpdate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * 合规数据上报 CERT 国家互联网应急中心 借款人老数据上报
 * @author sss
 */

@Service
public class CertOldBorrowUserServiceImpl extends BaseHgCertReportServiceImpl implements CertOldBorrowUserService {
    Logger logger = LoggerFactory.getLogger(CertOldBorrowUserServiceImpl.class);

    private String thisMessName = "借款人老数据上报";
    private String logHeader = "【" + CustomConstants.HG_DATAREPORT + CustomConstants.UNDERLINE + CustomConstants.HG_DATAREPORT_CERT + " " + thisMessName + "】";

    /**
     * 查未上报的标的借款人
     * IsUserInfo=0 的
     * @return
     */
    @Override
    public List<CertBorrow> insertCertBorrowUsers() {
        CertBorrowExample example = new CertBorrowExample();
        CertBorrowExample.Criteria cra = example.createCriteria();
        cra.andIsUserInfoEqualTo(0);

        example.setLimitStart(0);
        example.setLimitEnd(3000);

        return certBorrowMapper.selectByExample(example);
    }

    /**
     * 查询未上报标的的  用户信息上报成功  散标未上报的
     *
     * @return
     */
    @Override
    public List<CertBorrow> insertCertBorrow() {
        CertBorrowExample example = new CertBorrowExample();
        CertBorrowExample.Criteria cra = example.createCriteria();
        cra.andIsUserInfoEqualTo(1);
        cra.andIsScatterEqualTo(0);

        example.setLimitStart(0);
        example.setLimitEnd(3000);
        return certBorrowMapper.selectByExample(example);
    }

    /**
     * 批量修改状态
     *
     * @param update
     */
    @Override
    public int updateCertBorrowStatusBatch(CertBorrowUpdate update) {

        CertBorrowExample example = new CertBorrowExample();
        CertBorrowExample.Criteria cra = example.createCriteria();
        cra.andIdIn(update.getIds());

        return certBorrowMapper.updateByExampleSelective(update.getCertBorrow(),example);
    }
}
