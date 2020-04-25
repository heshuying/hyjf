/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: LIBIN
 * @version: 1.0
 * Created at: 2017年10月12日 下午2:11:50
 * Modification History:
 * Modified by : 
 */
package com.hyjf.api.server.tradelist;

import java.util.List;
import org.springframework.stereotype.Service;
import com.hyjf.base.service.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountListExample;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.customize.AccountDetailCustomize;

/**
 * @author LIBIN
 */
@Service
public class TradeListServiceImpl extends BaseServiceImpl implements TradeListService {
	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param params
	 * @return
	 * @author LIBIN
	 */
	@Override
	public List<AccountDetailCustomize> searchTradeList(TradeListBean trade) {
		
		AccountDetailCustomize accountDetailCustomize = new AccountDetailCustomize();
		accountDetailCustomize.setStartDate(trade.getStartDate());//必传
		accountDetailCustomize.setEndDate(trade.getEndDate());//必传
		accountDetailCustomize.setUserId(this.getUsersByMobile(trade.getPhone()).getUserId());//通过必传的phone
		accountDetailCustomize.setAccountId(trade.getAccountId());//必传
		accountDetailCustomize.setLimitStart(trade.getLimitStart());//必传
		accountDetailCustomize.setLimitEnd(trade.getLimitEnd());//必传
		accountDetailCustomize.setNid(trade.getNid());//选传
		accountDetailCustomize.setIsBank("1");//默认为1 江西银行
		accountDetailCustomize.setTradeStatus(trade.getTradeStatus());//选传 交易状态 :0失败 1成功 
		accountDetailCustomize.setTypeSearch(trade.getTypeSearch());//选传 收支类型 :1收入 2支出 3冻结 4解冻
		accountDetailCustomize.setTradeTypeSearch(trade.getTradeTypeSearch());//选传 交易类型ID 
		List<AccountDetailCustomize> accountInfos = this.accountDetailCustomizeMapper.queryApiAccountDetails(accountDetailCustomize);
		return accountInfos;
	}

	@Override
	public boolean existPhone(String mobile) {
		UsersExample example = new UsersExample();
		example.createCriteria().andMobileEqualTo(mobile);
		int size = usersMapper.countByExample(example);
		if (size > 0) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean existAccountId(String mobile, String accountId) {
		UsersExample example = new UsersExample();
		example.createCriteria().andMobileEqualTo(mobile);
		List<Users> userList = usersMapper.selectByExample(example);
        if(userList == null || userList.isEmpty()){
        	return false;
        }
		int userId = userList.get(0).getUserId();
		BankOpenAccountExample example1 = new BankOpenAccountExample();
		example1.createCriteria().andAccountEqualTo(accountId);
		List<BankOpenAccount> list = bankOpenAccountMapper.selectByExample(example1);
        if(list == null || list.isEmpty()){
        	return false;
        }
        if(list.get(0).getUserId() == null){
        	return false;
        } else {
        	if(list.get(0).getUserId().equals(userId)){
        		return true;
        	} else {
        		return false;
        	}
        }
	}
	
	/**
     * 根据用户mobile取得用户信息
     *
     * @param userId
     * @return
     */
    public Users getUsersByMobile(String mobile) {
        if (mobile != null) {
            UsersExample example = new UsersExample();
            example.createCriteria().andMobileEqualTo(mobile);
            List<Users> usersList = this.usersMapper.selectByExample(example);
            if (usersList != null && usersList.size() > 0) {
                return usersList.get(0);
            }
        }
        return null;
    }
}
