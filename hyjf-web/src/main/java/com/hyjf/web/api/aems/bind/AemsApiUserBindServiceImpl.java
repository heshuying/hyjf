/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: lb
 * @version: 1.0
 * Created at: 2017年9月15日 上午9:43:49
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.web.api.aems.bind;

import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.auto.UsersExample.Criteria;
import com.hyjf.web.api.base.ApiBaseServiceImpl;
import com.hyjf.web.user.login.LoginBean;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author Zha Daojian
 * @date 2018/9/18 14:44
 * @param 
 * @return 
 **/

@Service("AemsApiUserBindService")
public class AemsApiUserBindServiceImpl extends ApiBaseServiceImpl implements AemsApiUserBindService {


	/**
	 * 给第三方平台用户登陆授权
	 * @param userId
	 * @param bindUniqueId
	 * @param bindPlatformId
	 * @return
	 */
		
	@Override
	public Boolean bindThirdUser(Integer userId, String bindUniqueId, Integer bindPlatformId) {
		int nowTime = GetDate.getNowTime10();
		BindUsers bindUsers = new BindUsers();
		bindUsers.setUserId(userId);
		bindUsers.setBindUniqueId(bindUniqueId);
		bindUsers.setBindPlatformId(bindPlatformId);
		bindUsers.setCreateTime(nowTime);
		return bindUsersMapper.insertSelective(bindUsers) > 0 ? true : false;
	}
	
	/**
     * 根据绑定信息取得用户id
     * @param bindUniqueId
     * @return
     */
    @Override
    public Integer getUserIdByBind(Long bindUniqueId, int bindPlatformId) {
        //检索条件
        BindUsersExample example = new BindUsersExample();
        BindUsersExample.Criteria cra = example.createCriteria();
        cra.andBindUniqueIdEqualTo(bindUniqueId+"");
        cra.andBindPlatformIdEqualTo(bindPlatformId);
        cra.andDelFlgEqualTo(0);//未删除
        //检索
        List<BindUsers> list = bindUsersMapper.selectByExample(example);
        //无记录时，未绑定汇盈金服
        if(list != null && list.size() > 0){
            return list.get(0).getUserId();
        }
        return null;
    }

	/**
     * 根据绑定信息取得用户id
     * @param bindPlatformId
     * @return
     */
    @Override
    public String getBindUniqueIdByUserId(int userId, int bindPlatformId) {
        //检索条件
        BindUsersExample example = new BindUsersExample();
        BindUsersExample.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(userId);
        cra.andBindPlatformIdEqualTo(bindPlatformId);
        cra.andDelFlgEqualTo(0);//未删除
        //检索
        List<BindUsers> list = bindUsersMapper.selectByExample(example);
        //无记录时，未绑定汇盈金服
        if(list != null && list.size() > 0){
            return list.get(0).getBindUniqueId();
        }
        return null;
    }
    
	/**
     * 根据登陆输入的用户名或手机号取得用户信息
     * @param loginBean
     * @return
     */
	@Override
	public Users getUserInfoByLogin(LoginBean loginBean) {
		UsersExample example1 = new UsersExample();
		UsersExample example2 = new UsersExample();
		example1.createCriteria().andUsernameEqualTo(loginBean.getLoginUserName());
		Criteria c2 = example2.createCriteria().andMobileEqualTo(loginBean.getLoginUserName());
		example1.or(c2);
		List<Users> usersList = usersMapper.selectByExample(example1);
		if (usersList != null && usersList.size() > 0 ) {
			return usersList.get(0);
		}
		return null;
	}

	@Override
	public String getByIdCard(String idCard) {
		String mobile = null;
		UsersInfoExample example1 = new UsersInfoExample();
		example1.createCriteria().andIdcardEqualTo(idCard);
		List<UsersInfo> userInfoList = usersInfoMapper.selectByExample(example1);
		if (userInfoList != null && userInfoList.size() > 0 ) {
			UsersInfo usersInfo = userInfoList.get(0);
			Users users = usersMapper.selectByPrimaryKey(usersInfo.getUserId());
			mobile = users.getMobile();
		}
		return mobile;
	}
}
	