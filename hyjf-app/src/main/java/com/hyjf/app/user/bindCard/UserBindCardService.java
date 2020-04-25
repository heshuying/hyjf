package com.hyjf.app.user.bindCard;


/**
 * Description:用户绑卡
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: GOGTZ-T
 * @version: 1.0
 * Created at: 2015年12月4日 下午1:45:13
 * Modification History:
 * Modified by : 
 */

import java.util.Map;

import com.hyjf.app.BaseService;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

public interface UserBindCardService extends BaseService {

    /**
     * 用户绑卡后处理
     * 
     * @param bean
     * @return
     */
    public String updateAfterBindCard(ChinapnrBean bean, Map<String, String> params);
    
    /**
     * 获取用户的身份证号
     * 
     * @param userId
     * @return 用户的身份证号
     */
    public String getUserIdcard(Integer userId);
    
}
