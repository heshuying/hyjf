package com.hyjf.admin.manager.config.salesconfig;


import com.hyjf.mybatis.model.auto.SellDailyDistribution;
import com.hyjf.mybatis.model.customize.SellDailyDistributionCustomize;

import java.util.List;

/**
 * @author lisheng
 * @version EmailRecipientService, v0.1 2018/7/23 16:43
 */

public interface EmailRecipientService {
    int countRecordTotal(EmailRecipientBean form);

    List<SellDailyDistributionCustomize> getRecordList(EmailRecipientBean form, Integer offset, Integer limit);

    SellDailyDistributionCustomize getRecordById(Integer id);

    boolean updateRecord(EmailRecipientBean form);

    boolean updateForbidden(EmailRecipientBean form);

    boolean insertRecord(EmailRecipientBean form);

    boolean checkEmail(String Email);


}
