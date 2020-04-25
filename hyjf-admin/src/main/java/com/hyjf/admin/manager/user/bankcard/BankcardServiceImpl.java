package com.hyjf.admin.manager.user.bankcard;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.BankConfigExample;
import com.hyjf.mybatis.model.customize.admin.AdminBankcardListCustomize;

@Service
public class BankcardServiceImpl extends BaseServiceImpl implements BankcardService {

	/**
	 * 获取权限列表
	 * 
	 * @return
	 */
	public List<AdminBankcardListCustomize> getRecordList(Map<String, Object> bankCardUser, int limitStart, int limitEnd) {

		if (limitStart == 0 || limitStart > 0) {
			bankCardUser.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			bankCardUser.put("limitEnd", limitEnd);
		}
		// 查询用户列表
		List<AdminBankcardListCustomize> users = adminBankcardCustomizeMapper.selectBankCardList(bankCardUser);
		return users;
	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param form
	 * @return
	 * @author Administrator
	 */

	@Override
	public int countRecordTotal(Map<String, Object> bankCardUser) {
		// 查询用户列表
		int countTotal = adminBankcardCustomizeMapper.countRecordTotal(bankCardUser);
		return countTotal;

	}

	/**
	 * 获取银行列表
	 * 
	 * @param string
	 * @return
	 * @author Administrator
	 */
	@Override
	public List<BankConfig> getBankcardList() {
		List<BankConfig> banks = bankConfigMapper.selectByExample(new BankConfigExample());
		return banks;
	}

    @Override
    public int countRecordTotalNew(Map<String, Object> bankCardUser) {
        // 查询用户列表
        int countTotal = adminBankcardCustomizeMapper.countRecordTotalNew(bankCardUser);
        return countTotal;
    }

    @Override
    public List<AdminBankcardListCustomize> getRecordListNew(Map<String, Object> bankCardUser, int limitStart, int limitEnd) {
        if (limitStart == 0 || limitStart > 0) {
            bankCardUser.put("limitStart", limitStart);
        }
        if (limitEnd > 0) {
            bankCardUser.put("limitEnd", limitEnd);
        }
        // 查询用户列表
        List<AdminBankcardListCustomize> users = adminBankcardCustomizeMapper.selectNewBankCardList(bankCardUser);
        return users;
    }

}
