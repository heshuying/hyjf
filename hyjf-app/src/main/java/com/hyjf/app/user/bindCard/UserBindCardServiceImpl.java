package com.hyjf.app.user.bindCard;
/**
 * Description:用户出借实现类
 * Copyright: Copyright (HYJF Corporation)2015
 * Company: HYJF Corporation
 * @author: 郭勇
 * @version: 1.0
 * Created at: 2015年12月4日 下午1:50:02
 * Modification History:
 * Modified by :
 */

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.app.BaseServiceImpl;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.AccountBank;
import com.hyjf.mybatis.model.auto.AccountBankExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

@Service
public class UserBindCardServiceImpl extends BaseServiceImpl implements UserBindCardService {

	/**
	 * 用户绑卡回调方法
	 *
	 * @param bean
	 * @return
	 */
	public String updateAfterBindCard(ChinapnrBean bean, Map<String, String> params) {
		int nowTime = GetDate.getNowTime10(); // 当前时间

		int ret = 0;

		AccountBankExample accountBankExample = new AccountBankExample();
		AccountBankExample.Criteria aCriteria = accountBankExample.createCriteria();
		aCriteria.andUserIdEqualTo(Integer.valueOf(params.get("userId")));
		aCriteria.andAccountEqualTo(bean.getOpenAcctId()); // 银行卡账号
		aCriteria.andStatusEqualTo(0);
		List<AccountBank> list = this.accountBankMapper.selectByExample(accountBankExample);
		// 如果该卡已被用户绑定，则不再录入数据库
		if (list != null && list.size() > 0) {
			return ret + "";
		}

		// 写入绑卡信息表
		AccountBank accountBank = new AccountBank();
		accountBank.setUserId(Integer.valueOf(params.get("userId")));
		accountBank.setStatus(0);
		accountBank.setAccount(bean.getOpenAcctId());// 银行卡账号
		accountBank.setBank(bean.getOpenBankId());
		accountBank.setAddtime(nowTime + "");
		accountBank.setAddip(params.get("ip"));
		accountBank.setRespcode(bean.getRespCode());
		accountBank.setRespdesc(bean.getRespDesc());
		accountBank.setUpdateTime("");
		accountBank.setUpdateIp("");
		accountBank.setCardType("0"); // 卡类型 0普通提现卡1默认卡2快捷支付卡

		ret += this.accountBankMapper.insertSelective(accountBank);

		return ret + "";
	}

	/**
	 * 获取用户的身份证号
	 *
	 * @param userId
	 * @return 用户的身份证号
	 */
	public String getUserIdcard(Integer userId) {
		// 取得身份证号
		UsersInfoExample usersInfoExample = new UsersInfoExample();
		usersInfoExample.createCriteria().andUserIdEqualTo(userId);
		List<UsersInfo> listUsersInfo = this.usersInfoMapper.selectByExample(usersInfoExample);
		if (listUsersInfo != null && listUsersInfo.size() > 0) {
			return listUsersInfo.get(0).getIdcard();
		}
		return "";
	}

}
