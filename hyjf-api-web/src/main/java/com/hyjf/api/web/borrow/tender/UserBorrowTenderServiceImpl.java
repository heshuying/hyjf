package com.hyjf.api.web.borrow.tender;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.web.BaseServiceImpl;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersExample.Criteria;
import com.hyjf.mybatis.model.customize.apiweb.ApiwebTenderInfoCustomize;

@Service
public class UserBorrowTenderServiceImpl extends BaseServiceImpl implements
		UserBorrowTenderService {

	@Override
	public JSONObject getUserBorrowTender(
			UserBorrowTenderBean paramBean, Users user) throws Exception {
		JSONObject resultJson = new JSONObject();
		// 第三方平台用户名
		resultJson.put("username", paramBean.getUsername());
		// 本站用户名
		resultJson.put("usernamep", paramBean.getUsernamep());

		if (user.getOpenAccount() == 1) {
			// 已开户
			resultJson.put("level", "1");
		} else {
			// 未开户
			resultJson.put("level", "0");
		}
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("userid", user.getUserId());
		paramMap.put("starttime", paramBean.getStarttime());
		paramMap.put("endtime", paramBean.getEndtime());
		List<ApiwebTenderInfoCustomize> tenderInfoList = tenderInfoCustomizeMapper
				.getTenderInfoList(paramMap);
		for (ApiwebTenderInfoCustomize tender : tenderInfoList) {
			// 标的访问url
			tender.setUrl(PropUtils
					.getSystem(UserBorrowTenderDefine.BORROW_PREFIX_URL)
					+ tender.getBid());
		}
		resultJson.put("list", tenderInfoList);

		return resultJson;

	}

	/**
	 * 校验第三方平台传过来的账户是否存在
	 */
	@Override
	public Users checkLoginUser(UserBorrowTenderBean paramBean)
			throws Exception {
		Users sysUser = null;
		String trdUsername = paramBean.getUsername();
		String ownnerUsername = paramBean.getUsernamep();
		UsersExample userExample = new UsersExample();
		Criteria criteria = userExample.createCriteria();
		// 本站用户名
		criteria.andUsernameEqualTo(ownnerUsername);
		// 第三方平台用户名
		criteria.andUsernamepEqualTo(trdUsername);
		// 第三方来源
		criteria.andPidEqualTo(
				Integer.valueOf(paramBean.getFrom()));
		List<Users> userList = usersMapper.selectByExample(userExample);
		if (userList.size() > 0) {
			sysUser = userList.get(0);
		}
		return sysUser;
	}

}
