package com.hyjf.web.user.wechatbindcard;

import java.util.Map;

import com.hyjf.bank.service.BaseService;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

public interface UserWeChatBindCardService extends BaseService{

	 /**
     * 用户绑卡后处理
     * 
     * @param bean
     * @return
     */
    public String updateAfterBindCard(ChinapnrBean bean, Map<String, String> params);
}
