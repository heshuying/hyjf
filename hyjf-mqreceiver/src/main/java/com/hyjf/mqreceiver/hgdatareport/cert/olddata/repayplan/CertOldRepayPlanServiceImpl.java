package com.hyjf.mqreceiver.hgdatareport.cert.olddata.repayplan;

import com.hyjf.mongo.hgdatareport.dao.CertBorrowDao;
import com.hyjf.mqreceiver.hgdatareport.cert.BaseHgCertReportServiceImpl;
import com.hyjf.mybatis.model.auto.CertBorrow;
import com.hyjf.mybatis.model.auto.CertBorrowExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


/**
 * @author nxl
 */

@Service
public class CertOldRepayPlanServiceImpl extends BaseHgCertReportServiceImpl implements CertOldRepayPlanService {

    @Autowired

    /**
     * 查找数据库里待上还款计划的数据
     *
     * @return
     */
    @Override
    public List<CertBorrow> insertCertBorrowRapayList() {
        CertBorrowExample example = new CertBorrowExample();
        CertBorrowExample.Criteria cra = example.createCriteria();
        //1 代表已上报,0:未上报,99:上报错误
        // 用户信息已上传
        cra.andIsUserInfoEqualTo(1);
        //散标状态数据已上报
        cra.andIsStatusEqualTo(1);
        //散标数据已上报
        cra.andIsScatterEqualTo(1);
        //还款计划信息未上报
        cra.andIsRepayPlanEqualTo(0);

        example.setLimitStart(0);
        example.setLimitEnd(3000);
        List<CertBorrow> listStatus = certBorrowMapper.selectByExample(example);
        return listStatus;
    }
}
