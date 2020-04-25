/**
 * Description:用户开户service接口
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年12月4日 下午1:45:13
 * Modification History:
 * Modified by :
 */
package com.hyjf.api.surong.user.open;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.base.service.BaseService;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

public interface SuRongOpenAccountService extends BaseService {

	/**
	 * 保存用户开户信息
	 * 
	 * @param bean
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
	 * 根据手机号统计用户的数量
	 * @param userId 
	 * 
	 * @param mobile
	 * @return
	 */
	public JSONObject selectUserByMobile(int userId, String mobile);
	/**
	 * 
	 * @method: checkIfSendCoupon
	 * @description: 	查看是否可用注册68代金券				
	 *  @param user
	 *  @return 
	 * @return: boolean
	* @mender: zhouxiaoshuai
	 * @date:   2016年8月24日 下午3:08:12
	 */
	public boolean checkIfSendCoupon(Users user);
	
	   /**
     * 根据用户id查询相应的开户信息
     * 
     * @param userId
     * @return
     */
    public AccountChinapnr selectAccountChinapnrById(int userId);
    
    /**
      * 根据用户id查询相应的用户信息
      * 
      * @param userId
      * @return
      */
     public UsersInfo selectUserInfoById(int userId);
	
}
