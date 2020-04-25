package com.hyjf.base.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

import com.hyjf.mybatis.model.auto.*;
import org.apache.commons.lang.StringUtils;

import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.vip.apply.ApplyDefine;

public class BaseServiceImpl extends CustomizeMapper implements BaseService {

	/**
	 * 取得AppKey
	 *
	 */
	public String getAppKey(String appId) {
		// TODO
		if ("12345678".equals(appId)) {
			return "abcdefgh";
		}
		return null;
	}

	/**
	 * 取得用户信息
	 *
	 */
	public Users getUsers(Integer userId) {
		return usersMapper.selectByPrimaryKey(userId);
	}

	/**
	 * 添加短信记录表
	 *
	 * @param content
	 *            短信内容
	 * @param mobile
	 *            手机号码
	 * @param checkCode
	 *            验证码(没有验证码，设为空)
	 * @param remark
	 *            发送备注，如 注册填写“注册”
	 * @param status
	 *            发送状态，第三方返回状态
	 * @return
	 */

	public boolean AddMessage(String content, String mobile, String checkCode, String remark, int status) {

		if (StringUtils.isNotEmpty(mobile) && StringUtils.isNotEmpty(content)) {
			// 带验证码
			if (checkCode != null) {
				SmsCode smsCode = new SmsCode();
				smsCode.setMobile(mobile);
				smsCode.setCheckfor(MD5.toMD5Code(mobile + checkCode));
				smsCode.setCheckcode(checkCode);
				smsCode.setPosttime(GetDate.getNowTime10());
				smsCode.setStatus(status);
				smsCodeMapper.insertSelective(smsCode);
			}

			// 插入短信记录
			SmsLogWithBLOBs smsLog = new SmsLogWithBLOBs();

			smsLog.setMobile(mobile);
			smsLog.setContent(content);
			smsLog.setPosttime(GetDate.getNowTime10());
			smsLog.setStatus(status);
			smsLog.setType(remark);
			smsLogMapper.insertSelective(smsLog);
			return true;
		}
		return false;
	}

	/**
	 * 获取用户的汇付信息
	 *
	 * @param userId
	 * @return 用户的汇付信息
	 */

	@Override
	public AccountChinapnr getAccountChinapnr(Integer userId) {
		AccountChinapnr accountChinapnr = null;
		if (userId == null) {
			return null;
		}
		AccountChinapnrExample example = new AccountChinapnrExample();
		AccountChinapnrExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<AccountChinapnr> list = accountChinapnrMapper.selectByExample(example);
		if (list != null && !list.isEmpty()) {
			accountChinapnr = list.get(0);
		}
		return accountChinapnr;
	}

	/**
	 * 获取用户信息
	 *
	 * @param username
	 * @param password
	 * @return 获取用户信息
	 */
	public Users getUsers(String username, String password) {
		UsersExample example = new UsersExample();
		UsersExample.Criteria criteria = example.createCriteria();
		criteria.andUsernameEqualTo(username);

		List<Users> usersList = this.usersMapper.selectByExample(example);
		if (usersList != null && usersList.size() > 0) {
			return usersList.get(0);
		}
		return null;
	}

	/**
	 * 获取用户的账户信息
	 *
	 * @param userId
	 * @return 获取用户的账户信息
	 */
	public Account getAccount(Integer userId) {
		AccountExample example = new AccountExample();
		AccountExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<Account> listAccount = accountMapper.selectByExample(example);
		if (listAccount != null && listAccount.size() > 0) {
			return listAccount.get(0);
		}
		return null;
	}

	/**
	 * 获取系统配置
	 *
	 * @return
	 */
	public String getBorrowConfig(String configCd) {
		BorrowConfig borrowConfig = this.borrowConfigMapper.selectByPrimaryKey(configCd);
		return borrowConfig.getConfigValue();
	}

	/**
	 * 查询检证日志
	 *
	 * @return
	 */
	@Override
	public ChinapnrExclusiveLogWithBLOBs selectChinapnrExclusiveLog(long id) {
		return this.chinapnrExclusiveLogMapper.selectByPrimaryKey(id);
	}

	/**
	 * 更新检证日志
	 *
	 * @return
	 */
	@Override
	public int updateChinapnrExclusiveLog(ChinapnrExclusiveLogWithBLOBs record) {
		ChinapnrExclusiveLogExample example = new ChinapnrExclusiveLogExample();
		example.createCriteria().andIdEqualTo(record.getId()).andUpdatetimeEqualTo(record.getUpdatetime()).andUpdateuserNotEqualTo("callback2");
		record.setUpdatetime(String.valueOf(GetDate.getMyTimeInMillis()));
		record.setUpdateuser("callback2");
		return chinapnrExclusiveLogMapper.updateByExampleSelective(record, example);
	}

	/**
	 * 更新检证状态
	 *
	 * @return
	 */
	public int updateChinapnrExclusiveLogStatus(long uuid, String status) {
		ChinapnrExclusiveLogWithBLOBs record = new ChinapnrExclusiveLogWithBLOBs();
		record.setId(uuid);
		record.setStatus(status);
		return chinapnrExclusiveLogMapper.updateByPrimaryKeySelective(record);
	}

	/**
	 * 根据汇付账户查询user_id
	 *
	 * @param chinapnrUsrcustid
	 * @return
	 */
	public Integer selectUserIdByUsrcustid(Long chinapnrUsrcustid) {
		return this.accountChinapnrCustomizeMapper.selectUserIdByUsrcustid(chinapnrUsrcustid);
	}

	/**
	 * 根据用户ID取得用户信息
	 *
	 * @param userId
	 * @return
	 */
	public Users getUsersByUserId(Integer userId) {
		if (userId != null) {
			UsersExample example = new UsersExample();
			example.createCriteria().andUserIdEqualTo(userId);
			List<Users> usersList = this.usersMapper.selectByExample(example);
			if (usersList != null && usersList.size() > 0) {
				return usersList.get(0);
			}
		}
		return null;
	}

	/**
	 * 根据用户ID取得用户的推荐人信息
	 * 
	 * @param userId
	 * @return
	 */
	public SpreadsUsers getSpreadsUsersByUserId(Integer userId) {
		if (userId != null) {
			SpreadsUsersExample example = new SpreadsUsersExample();
			example.createCriteria().andUserIdEqualTo(userId);
			List<SpreadsUsers> list = this.spreadsUsersMapper.selectByExample(example);
			if (list != null && list.size() > 0) {
				return list.get(0);
			}
		}
		return null;
	}

	/**
	 * 根据用户ID取得用户信息
	 *
	 * @param userId
	 * @return
	 */
	public UsersInfo getUsersInfoByUserId(Integer userId) {
		if (userId != null) {
			UsersInfoExample example = new UsersInfoExample();
			example.createCriteria().andUserIdEqualTo(userId);
			List<UsersInfo> usersInfoList = this.usersInfoMapper.selectByExample(example);
			if (usersInfoList != null && usersInfoList.size() > 0) {
				return usersInfoList.get(0);
			}
		}
		return null;
	}

	/**
	 * 获取数据字典表的下拉列表
	 *
	 * @return
	 */
	public List<ParamName> getParamNameList(String nameClass) {
		ParamNameExample example = new ParamNameExample();
		ParamNameExample.Criteria cra = example.createCriteria();
		cra.andNameClassEqualTo(nameClass);
		cra.andDelFlagEqualTo(CustomConstants.FLAG_NORMAL);
		example.setOrderByClause(" sort ASC ");
		return this.paramNameMapper.selectByExample(example);
	}

	/**
	 * 获取数据字典名称
	 *
	 * @return
	 */
	public String getParamName(String nameClass, String nameCd) {
		ParamNameExample example = new ParamNameExample();
		ParamNameExample.Criteria cra = example.createCriteria();
		cra.andNameClassEqualTo(nameClass);
		cra.andNameCdEqualTo(nameCd);
		cra.andDelFlagEqualTo(CustomConstants.FLAG_NORMAL);
		List<ParamName> paramNameList = this.paramNameMapper.selectByExample(example);
		if (paramNameList != null && paramNameList.size() > 0) {
			return paramNameList.get(0).getName();
		}

		return "";
	}

	/**
	 * 
	 * 特殊字符编码
	 * 
	 * @author renxingchen
	 * @param token
	 * @return
	 * @throws Exception
	 */
	public String strEncode(String str) {
		try {
			str = URLEncoder.encode(str, "utf-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return str;
	}

	/**
	 * 获取用户在汇付天下的账号信息
	 *
	 * @return
	 */
	public AccountChinapnr getChinapnrUserInfo(Integer userId) {
		if (userId != null) {
			AccountChinapnrExample example = new AccountChinapnrExample();
			AccountChinapnrExample.Criteria criteria = example.createCriteria();
			criteria.andUserIdEqualTo(userId);
			List<AccountChinapnr> list = this.accountChinapnrMapper.selectByExample(example);
			if (list != null && list.size() == 1) {
				return list.get(0);
			}
		}
		return null;
	}

	@Override
	public BankOpenAccount getBankOpenAccount(Integer userId) {
		BankOpenAccountExample accountExample = new BankOpenAccountExample();
		BankOpenAccountExample.Criteria crt = accountExample.createCriteria();
		crt.andUserIdEqualTo(userId);
		List<BankOpenAccount> bankAccounts = this.bankOpenAccountMapper.selectByExample(accountExample);
		if (bankAccounts != null && bankAccounts.size() == 1) {
			return bankAccounts.get(0);
		}
		return null;
	}
	@Override
    public String getBankRetMsg(String retCode) {
        //如果错误码不为空
        if (StringUtils.isNotBlank(retCode)) {
            BankReturnCodeConfigExample example = new BankReturnCodeConfigExample();
            example.createCriteria().andRetCodeEqualTo(retCode);
            List<BankReturnCodeConfig> retCodes = this.bankReturnCodeConfigMapper.selectByExample(example);
            if (retCodes != null && retCodes.size() == 1) {
                String retMsg = retCodes.get(0).getErrorMsg();
                if (StringUtils.isNotBlank(retMsg)) {
                    return retMsg;
                } else {
                    return "请联系客服！";
                }
            } else {
                return "请联系客服！";
            }
        } else {
            return "操作失败！";
        }
    }

    @Override
    public String getBankIdByCardNo(String cardNo) {
        String bankId = null;
        if (cardNo == null || cardNo.length() < 14 || cardNo.length() > 19) {
            return "";
        }
        // 把常用的卡BIN放到最前面
        // 6位卡BIN
        String cardBin_6 = cardNo.substring(0, 6);
        bankId = this.getBankId(6, cardBin_6);
        if (StringUtils.isNotBlank(bankId)) {
            return bankId;
        }
        // 7位卡BIN
        String cardBin_7 = cardNo.substring(0, 7);
        bankId = this.getBankId(7, cardBin_7);
        if (StringUtils.isNotBlank(bankId)) {
            return bankId;
        }
        // 8位卡BIN
        String cardBin_8 = cardNo.substring(0, 8);
        bankId = this.getBankId(8, cardBin_8);
        if (StringUtils.isNotBlank(bankId)) {
            return bankId;
        }
        // 9位卡BIN
        String cardBin_9 = cardNo.substring(0, 9);
        bankId = this.getBankId(9, cardBin_9);
        if (StringUtils.isNotBlank(bankId)) {
            return bankId;
        }
        // 2位卡BIN
        String cardBin_2 = cardNo.substring(0, 2);
        bankId = this.getBankId(2, cardBin_2);
        if (StringUtils.isNotBlank(bankId)) {
            return bankId;
        }
        // 3位卡BIN
        String cardBin_3 = cardNo.substring(0, 3);
        bankId = this.getBankId(3, cardBin_3);
        if (StringUtils.isNotBlank(bankId)) {
            return bankId;
        }
        // 4位卡BIN
        String cardBin_4 = cardNo.substring(0, 4);
        bankId = this.getBankId(4, cardBin_4);
        if (StringUtils.isNotBlank(bankId)) {
            return bankId;
        }
        // 5位卡BIN
        String cardBin_5 = cardNo.substring(0, 5);
        bankId = this.getBankId(5, cardBin_5);
        if (StringUtils.isNotBlank(bankId)) {
            return bankId;
        }
        // 10位卡BIN
        String cardBin_10 = cardNo.substring(0, 10);
        bankId = this.getBankId(10, cardBin_10);
        if (StringUtils.isNotBlank(cardBin_10)) {
            return bankId;
        }
        return bankId;
    }

    private String getBankId(int cardBinLength, String cardBin) {
        CardBinExample example = new CardBinExample();
        CardBinExample.Criteria cra = example.createCriteria();
        cra.andBinLengthEqualTo(cardBinLength);
        cra.andBinValueEqualTo(cardBin);
        List<CardBin> list = this.cardBinMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0).getBankId();
        }
        return null;
    }

    /**
     * 根据银行Id查询所属银行名称
     * 
     * @param bankId
     * @return
     */
    @Override
    public String getBankNameById(String bankId) {
    	BanksConfig bankConfig = banksConfigMapper.selectByPrimaryKey(Integer.parseInt(bankId));
		if (bankConfig != null) {
			return bankConfig.getBankName();
		}
        return null;
    }
    
    /**
     * 调用江西银行查询联行号
     * @param cardNo
     * @return
     */
    @Override
    public BankCallBean payAllianceCodeQuery(String cardNo,Integer userId) {
        BankCallBean bean = new BankCallBean();
        String channel = BankCallConstant.CHANNEL_APP;
        String orderDate = GetOrderIdUtils.getOrderDate();
        String txDate = GetOrderIdUtils.getTxDate();
        String txTime = GetOrderIdUtils.getTxTime();
        String seqNo = GetOrderIdUtils.getSeqNo(6);
        bean.setVersion(BankCallConstant.VERSION_10);// 版本号
		bean.setTxCode(BankCallConstant.TXCODE_PAY_ALLIANCE_CODE_QUERY);// 交易代码
		bean.setTxDate(txDate);
        bean.setTxTime(txTime);
        bean.setSeqNo(seqNo);
        bean.setChannel(channel);
        bean.setAccountId(cardNo);
        bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
        bean.setLogOrderDate(orderDate);
        bean.setLogUserId(String.valueOf(userId));
        bean.setLogRemark("联行号查询");
        bean.setLogClient(0);
        return BankCallUtils.callApiBg(bean);
    }
    
    /**
     * 根据银行Id查询本地存的银联行号
     * @param bankId
     * @return
     */
    @Override
    public String getPayAllianceCodeByBankId(String bankId) {
    	BanksConfig bankConfig = banksConfigMapper.selectByPrimaryKey(Integer.parseInt(bankId));
		if (bankConfig != null) {
			return bankConfig.getPayAllianceCode();
		}
        return null;
    }

	/**
	 * 插入开户日志表
	 *
	 * @param userId
	 * @param userName
	 * @param mobile
	 * @param orderId
	 * @return
	 */
	@Override
	public boolean updateUserAccountLog(Integer userId, String userName, String mobile, String orderId) {
		Date date = new Date();
		BankOpenAccountLogExample example = new BankOpenAccountLogExample();
		example.createCriteria().andUserIdEqualTo(userId).andOrderIdEqualTo(orderId);
		List<BankOpenAccountLog> bankOpenAccountLogs = this.bankOpenAccountLogMapper.selectByExample(example);
		if (bankOpenAccountLogs != null && bankOpenAccountLogs.size() > 0) {
			BankOpenAccountLog openAccountLog = bankOpenAccountLogs.get(0);
			openAccountLog.setMobile(mobile);
			openAccountLog.setStatus(0);
			openAccountLog.setUpdateTime(date);
			openAccountLog.setUpdateUserId(userId);
			openAccountLog.setUpdateUserName(openAccountLog.getCreateUserName());
			boolean updateFlag = this.bankOpenAccountLogMapper.updateByPrimaryKeySelective(openAccountLog) > 0 ? true : false;
			return updateFlag;
		} else {
			BankOpenAccountLog bankOpenAccountLog = new BankOpenAccountLog();
			bankOpenAccountLog.setUserId(userId);
			bankOpenAccountLog.setUserName(userName);
			bankOpenAccountLog.setMobile(mobile);
			bankOpenAccountLog.setStatus(0);
			bankOpenAccountLog.setOrderId(orderId);
			bankOpenAccountLog.setCreateTime(date);
			bankOpenAccountLog.setCreateUserId(userId);
			bankOpenAccountLog.setCreateUserName(userName);
			/*bankOpenAccountLog.setName(name);
			bankOpenAccountLog.setIdNo(idno);*/
			bankOpenAccountLog.setClient(4);
			boolean flag = this.bankOpenAccountLogMapper.insertSelective(bankOpenAccountLog) > 0 ? true : false;
			return flag;
		}
	}


	/**
	 * 根据用户ID,短信发送订单号查询用户开户记录
	 *
	 * @param userId
	 * @param orderId
	 * @return
	 */
	@Override
	public BankOpenAccountLog checkBankOpenAccountLog(Integer userId, String orderId) {
		BankOpenAccountLogExample example = new BankOpenAccountLogExample();
		BankOpenAccountLogExample.Criteria cra = example.createCriteria();
		cra.andUserIdEqualTo(userId);
		cra.andOrderIdEqualTo(orderId);
		List<BankOpenAccountLog> list = this.bankOpenAccountLogMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	
	/**
	 * 接口规则（MD5）加密结果
	 * @param params
	 * @return
	 * @author liubin
	 */
	private String getSign(Object...params) {
		String sign = StringUtils.EMPTY;
		String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);

		//字符串拼接（accessKey + param1 + param2 + ... + accessKey)）
		StringBuilder builder = new StringBuilder();
		builder.append(accessKey);
		for (Object param : params) {
			if (params != null) {
				builder.append(String.valueOf(param));
			}
		}
		builder.append(accessKey);
		
		sign = builder.toString();
		return StringUtils.lowerCase(MD5.toMD5Code(sign));
	}
	
	/**
	 * 验证签名
	 * @param chkValue 调用方传入签名
	 * @param params 调用方传入验签参数
	 * @return
	 * @author liubin
	 */
    @Override
    public boolean checkSign(String chkValue, Object...params) {
		String sign = getSign(params);
		return StringUtils.equals(sign, chkValue) ? true : false;
	}
    /**
     * 
     * 根据手机号查询用户信息
     * @author pcc
     * @param mobile
     * @return
     */
    @Override
    public Users getUsersByMobile(String mobile) {
        UsersExample example=new UsersExample();
        example.createCriteria().andMobileEqualTo(mobile);
        List<Users> list=usersMapper.selectByExample(example);
        if(list.size()!=0){
            return list.get(0);
        }
        return null;
    }

    /**
     * 根据机构编号检索机构信息
     *
     * @param instCode
     * @return
     */
    @Override
    public HjhInstConfig selectInstConfigByInstCode(String instCode) {
        HjhInstConfigExample example = new HjhInstConfigExample();
        HjhInstConfigExample.Criteria cra = example.createCriteria();
        cra.andInstCodeEqualTo(instCode);
        List<HjhInstConfig> list = this.hjhInstConfigMapper.selectByExample(example);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
    
    /**
     * 根据id获取活动记录
     * @param actId
     * @return
     */
    @Override
	public ActivityList getActivityById(Integer actId){
    	return activityListMapper.selectByPrimaryKey(actId);
    }
    
    /**
     * 查询活动有效性 0：活动不存在 1：活动未开始 2：活动已结束 3：活动进行中
     * @param activityId
     * @return
     */
	@Override
	public Integer checkActivityStatus(String activityId) {
        if(activityId==null){
            return 0;
        }
        ActivityList activityList=activityListMapper.selectByPrimaryKey(new Integer(activityId));
        if(activityList==null){
            return 0;
        }
        if(activityList.getTimeStart()>GetDate.getNowTime10()){
            return 1;
        }
        if(activityList.getTimeEnd()<GetDate.getNowTime10()){
            return 2;
        }
        return 3;
    }
	
	@Override
	public boolean updateUserAccountLogPage(Integer userId, String userName, String mobile, String orderId,String name,String idno) {
        Date date = new Date();
        BankOpenAccountLogExample example = new BankOpenAccountLogExample();
        example.createCriteria().andUserIdEqualTo(userId).andOrderIdEqualTo(orderId);
        List<BankOpenAccountLog> bankOpenAccountLogs = this.bankOpenAccountLogMapper.selectByExample(example);
        if (bankOpenAccountLogs != null && bankOpenAccountLogs.size() > 0) {
            BankOpenAccountLog openAccountLog = bankOpenAccountLogs.get(0);
            openAccountLog.setMobile(mobile);
            openAccountLog.setStatus(0);
            openAccountLog.setUpdateTime(date);
            openAccountLog.setUpdateUserId(userId);
            openAccountLog.setUpdateUserName(openAccountLog.getCreateUserName());
            boolean updateFlag = this.bankOpenAccountLogMapper.updateByPrimaryKeySelective(openAccountLog) > 0 ? true : false;
            return updateFlag;
        } else {
            BankOpenAccountLog bankOpenAccountLog = new BankOpenAccountLog();
            bankOpenAccountLog.setUserId(userId);
            bankOpenAccountLog.setUserName(userName);
            bankOpenAccountLog.setMobile(mobile);
            bankOpenAccountLog.setStatus(0);
            bankOpenAccountLog.setOrderId(orderId);
            bankOpenAccountLog.setCreateTime(date);
            bankOpenAccountLog.setCreateUserId(userId);
            bankOpenAccountLog.setCreateUserName(userName);
            bankOpenAccountLog.setName(name);
            bankOpenAccountLog.setIdNo(idno);
            bankOpenAccountLog.setClient(4);
            boolean flag = this.bankOpenAccountLogMapper.insertSelective(bankOpenAccountLog) > 0 ? true : false;
            return flag;
        }
    }

	/**
	 * 根据标的编号查询借款信息
	 *
	 * @param borrowNid
	 * @return
	 */
	@Override
	public Borrow selectBorrowInfoByBorrowNid(String borrowNid) {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria cra = example.createCriteria();
		cra.andBorrowNidEqualTo(borrowNid);
		List<Borrow> result = this.borrowMapper.selectByExample(example);
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}


}
