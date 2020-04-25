package com.hyjf.admin.manager.user.account;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.IdCardToArea;
import com.hyjf.mybatis.model.auto.IdCardToAreaExample;
import com.hyjf.mybatis.model.customize.admin.AdminAccountListCustomize;

@Service
public class AccountServiceImpl extends BaseServiceImpl implements AccountService {

	/**
	 * 获取权限列表
	 * 
	 * @return
	 */
	public List<AdminAccountListCustomize> getRecordList(Map<String, Object> accountUser, int limitStart, int limitEnd) {
		// 封装查询条件

		if (limitStart == 0 || limitStart > 0) {
			accountUser.put("limitStart", limitStart);
		}
		if (limitEnd > 0) {
			accountUser.put("limitEnd", limitEnd);
		}
		// 查询用户列表
		List<AdminAccountListCustomize> users = adminAccountCustomizeMapper.selectAccountList(accountUser);
		return users;

	}

	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * 
	 * @param userListCustomizeBean
	 * @return
	 * @author Administrator
	 */

	@Override
	public int countRecordTotal(Map<String, Object> accountUser) {
		return adminAccountCustomizeMapper.countRecordTotal(accountUser);
	}
	
	
	/**************************************银行存管  pcc*************************************************/
	/**
     * 获取权限列表
     * 
     * @return
     */
    public List<AdminAccountListCustomize> getBankRecordList(Map<String, Object> accountUser, int limitStart, int limitEnd) {
        // 封装查询条件
    	
        if (limitStart == 0 || limitStart > 0) {
            accountUser.put("limitStart", limitStart);
        }
        if (limitEnd > 0) {
            accountUser.put("limitEnd", limitEnd);
        }
        // 查询用户列表
        List<AdminAccountListCustomize> users = adminAccountCustomizeMapper.selectBankAccountList(accountUser);
        return users;

    }

    /**
     * 执行前每个方法前需要添加BusinessDesc描述
     * 
     * @param userListCustomizeBean
     * @return
     * @author Administrator
     */

    @Override
    public int countBankRecordTotal(Map<String, Object> accountUser) {
        return adminAccountCustomizeMapper.countBankRecordTotal(accountUser);
    }

	/**
	 * 
	 * @Description:通过身份证号获取户籍所在地
	 * @param idCard
	 * @return String
	 * @exception:
	 * @author: xulijie
	 * @time:2017年5月3日 下午4:26:43
	 */
	@Override
	public String getAreaByIdCard(String idCard) {
		if (StringUtils.isBlank(idCard) || idCard.length() < 15) {
			return "";
		}
		IdCardToAreaExample example = new IdCardToAreaExample();
		IdCardToAreaExample.Criteria criteria = example.createCriteria();
		Integer code = Integer.parseInt(idCard.substring(0, 6));
		// 条件查询
		if (Validator.isNotNull(idCard)) {
			criteria.andCardNumberEqualTo(code);
		}
		List<IdCardToArea> list = this.idCardToAreaMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0).getArea();
		}
		return "";
	}
}
