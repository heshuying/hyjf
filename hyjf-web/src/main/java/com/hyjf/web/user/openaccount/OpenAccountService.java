/**
 * Description:用户充值
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: GOGTZ-T
 * @version: 1.0
 *           Created at: 2015年12月4日 下午1:45:13
 *           Modification History:
 *           Modified by :
 */
package com.hyjf.web.user.openaccount;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.mybatis.model.auto.CorpOpenAccountRecord;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.web.BaseService;

public interface OpenAccountService extends BaseService {

    /**
     * 保存相应的数据，更新相应的字段
     * 
     * @param bean
     * @param params
     * @return
     */
    public boolean userOpenAccount(ChinapnrBean bean);

    /**
     * 根据用户id查询相应的用户
     * 
     * @param userId
     * @return
     */
    public Users selectUserById(int userId);

    /**
     * 根据用户id获取用户的手机号
     * 
     * @param userId
     * @return
     */
    public String getUsersMobile(Integer userId);

    /**
     * 根据手机号统计用户的数量
     * @param userId
     * 
     * @param mobile
     * @return
     */
    public JSONObject selectUserByMobile(int userId, String mobile);

    /**
     * 企业用户开户
     * 
     * @param userId
     * @param username
     * @param busiCode
     * @param guarType 
     * @return
     */
    public int insertCorpOpenAccountRecord(int userId, String username, String busiCode, String guarType);

    /**
     * 企业用户开户
     * 
     * @param userId
     * @param username
     * @param busiCode
     * @return
     */
    public boolean corpOpenAccount(ChinapnrBean bean);
    /**
     * 
     * @method: checkIfSendCoupon
     * @description: 查看是否可用注册68代金券			
     *  @param user
     *  @return 
     * @return: boolean
    * @mender: zhouxiaoshuai
     * @date:   2016年8月24日 下午2:06:17
     */
	public boolean checkIfSendCoupon(Users user);
	
	
    /**
     * 获取企业开户记录
     * @param userId
     * @return
     */
    public CorpOpenAccountRecord getCorpOpenAccountRecordByUserId(int userId);


}
