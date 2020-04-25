/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.admin.exception.certsendexception;

import com.hyjf.mybatis.model.auto.CertErrLog;
import com.hyjf.mybatis.model.auto.CertLog;

import java.util.List;


public interface CertSendExceptionService {


    /**
     * 根据分页查找数据
     * @param limtStart
     * @param limtEnd
     * @param form
     * @return
     */
    List<CertErrLog> selectCertErrLog(int limtStart, int limtEnd, CertSendExceptionBean form);

    /**
     * 查找数量
     * @param form
     * @return
     */
    Integer selectCertErrLogCount(CertSendExceptionBean form);

    /**
     * 重新跑批
     * @param id
     */
    void updateErrorCount(Integer id);
}
