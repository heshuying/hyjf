package com.hyjf.app.bank.user.bindCard;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseServiceImpl;
import com.hyjf.common.bank.LogAcqResBean;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankAccountLog;
import com.hyjf.mybatis.model.auto.BankCard;
import com.hyjf.mybatis.model.auto.BankCardExample;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.BankConfigExample;
import com.hyjf.mybatis.model.auto.BankInterface;
import com.hyjf.mybatis.model.auto.BankInterfaceExample;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

/**
 * App绑卡Service实现类
 * 
 * @author liuyang
 *
 */
@Service
public class AppBindCardServiceImpl extends BaseServiceImpl implements AppBindCardService {
	private Logger logger = LoggerFactory.getLogger(AppBindCardServiceImpl.class);
	/**
	 * 用户绑卡回调方法
	 *
	 * @param bean
	 * @return
	 * @throws Exception
	 */
	@Override
	public synchronized void updateAfterBindCard(BankCallBean bean) throws Exception {
		int nowTime = GetDate.getNowTime10(); // 当前时间
		LogAcqResBean logAcq = bean.getLogAcqResBean();
		// 用户Id
		Integer userId = Integer.parseInt(bean.getLogUserId());
		Users user = this.getUsers(userId);
		BankCardExample accountBankExample = new BankCardExample();
		BankCardExample.Criteria aCriteria = accountBankExample.createCriteria();
		aCriteria.andUserIdEqualTo(userId);
		aCriteria.andCardNoEqualTo(logAcq.getCardNo()); // 银行卡账号
		aCriteria.andStatusEqualTo(1);
		List<BankCard> list = this.bankCardMapper.selectByExample(accountBankExample);
		// 如果该卡已被用户绑定，则不再录入数据库
		if (list == null || list.size() == 0) {
			// 银行Id
			String bankId = this.getBankIdByCardNo(logAcq.getCardNo());
			// 同步银行卡信息
			this.updateAccountBankByUserId(userId);
			// 插入操作记录表
			BankAccountLog bankAccountLog = new BankAccountLog();
			bankAccountLog.setUserId(userId);
			bankAccountLog.setUserName(user.getUsername());
			bankAccountLog.setBankCode(bankId == null ? "0" : String.valueOf(bankId));
			bankAccountLog.setBankAccount(logAcq.getCardNo());
			bankAccountLog.setBankName(bankId == null ? "" : this.getBankNameById(bankId));
			bankAccountLog.setCardType(0);// 卡类型 0普通提现卡1默认卡2快捷支付卡
			bankAccountLog.setOperationType(0);// 操作类型 0绑定 1删除
			bankAccountLog.setStatus(0);// 成功
			bankAccountLog.setCreateTime(nowTime);// 操作时间
			boolean isUpdateFlag = this.bankAccountLogMapper.insertSelective(bankAccountLog) > 0 ? true : false;
			if (!isUpdateFlag) {
				throw new Exception("插入操作日志表失败~!");
			}
			list = this.bankCardMapper.selectByExample(accountBankExample);
			if (list != null && list.size() > 0) {
			    BankCard bankCard=new BankCard();
			    bankCard.setId(list.get(0).getId());
			    bankCard.setMobile(bean.getMobile());
			    bankCardMapper.updateByPrimaryKeySelective(bankCard);
			}
			
		}
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

	/**
	 * 获取银行列表
	 * 
	 * @param string
	 * @return
	 * @author Administrator
	 */
	public BankConfig getBankcardConfig(String bankCode) {
		BankConfigExample example = new BankConfigExample();
		BankConfigExample.Criteria cra = example.createCriteria();
		if (StringUtils.isNotEmpty(bankCode)) {
			cra.andCodeEqualTo(bankCode);
		} else {
			return null;
		}
		List<BankConfig> banks = bankConfigMapper.selectByExample(example);
		if (banks != null && banks.size() > 0) {
			return banks.get(0);
		}
		return null;
	}

	@Override
	public void updateAccountBankByUserId(Integer userId) {
		String respCode = "";
		try {
			LogUtil.startLog(AppBindCardServiceImpl.class.getName(), "updateAccountBankByUserId", "开始更新用户银行卡信息,用户ID:" + userId);
			BankOpenAccountExample example = new BankOpenAccountExample();
			example.createCriteria().andUserIdEqualTo(userId);
			List<BankOpenAccount> list = bankOpenAccountMapper.selectByExample(example);
			if (list != null && list.size() != 0) {
				BankOpenAccount bankOpenAccount = list.get(0);
				Users user = this.getUsers(userId);
				// 调用汇付接口(4.2.2 用户绑卡接口)
				BankCallBean bean = new BankCallBean();
				bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
				bean.setTxCode(BankCallMethodConstant.TXCODE_CARD_BIND_DETAILS_QUERY);
				bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
				bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
				bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
				bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
				bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
				bean.setChannel(BankCallConstant.CHANNEL_APP);// 交易渠道
				bean.setAccountId(bankOpenAccount.getAccount());// 存管平台分配的账号
				bean.setState("1"); // 查询状态 0-所有（默认） 1-当前有效的绑定卡
				bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
				bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
				bean.setLogRemark("绑卡关系查询");
				bean.setLogUserId(String.valueOf(userId));
				// 调用汇付接口 4.4.11 银行卡查询接口
				BankCallBean bankCallBean = BankCallUtils.callApiBg(bean);
				respCode = bankCallBean == null ? "" : bankCallBean.getRetCode();
				BankCardExample bankCardExample = new BankCardExample();
				BankCardExample.Criteria cri = bankCardExample.createCriteria();
				cri.andUserIdEqualTo(bankOpenAccount.getUserId());
				// 如果接口调用成功
				if (BankCallConstant.RESPCODE_SUCCESS.equals(respCode)) {
					String usrCardInfolist = bankCallBean.getSubPacks();
					JSONArray array = JSONObject.parseArray(usrCardInfolist);
					if (array != null && array.size() != 0) {
						List<BankCard> bankCardList = new ArrayList<BankCard>();
						for (int j = 0; j < array.size(); j++) {
							JSONObject obj = array.getJSONObject(j);
							BankCard bank = new BankCard();
							bank.setUserId(bankOpenAccount.getUserId());
							bank.setUserName(user.getUsername());
							bank.setStatus(1);// 默认都是1
							bank.setCardNo(obj.getString("cardNo"));
							// 根据银行卡号查询银行ID
							String bankId = this.getBankIdByCardNo(obj.getString("cardNo"));
							bank.setBankId(bankId == null ? 0 : Integer.valueOf(bankId));
							bank.setBank(bankId == null ? "" : this.getBankNameById(bankId));
							// 银行联号
							String payAllianceCode = "";
							// 调用江西银行接口查询银行联号
							BankCallBean payAllianceCodeQueryBean = this.payAllianceCodeQuery(obj.getString("cardNo"), userId);
							if (payAllianceCodeQueryBean != null && BankCallConstant.RESPCODE_SUCCESS.equals(payAllianceCodeQueryBean.getRetCode())) {
								payAllianceCode = payAllianceCodeQueryBean.getPayAllianceCode();
							}
							// 如果此时银行联号还是为空,调用本地查询银行联号
							if (StringUtils.isBlank(payAllianceCode)) {
								payAllianceCode = this.getPayAllianceCodeByBankId(bankId);
							}
							bank.setPayAllianceCode(payAllianceCode);
							SimpleDateFormat sdf = GetDate.yyyymmddhhmmss;
							bank.setCreateTime(sdf.parse(obj.getString("txnDate") + obj.getString("txnTime")));
							bank.setCreateUserId(userId);
							bank.setCreateUserName(user.getUsername());
							bankCardList.add(bank);
						}
						// 数据库操作
						bankCardMapper.deleteByExample(bankCardExample);
						for (BankCard bank : bankCardList) {
							bankCardMapper.insertSelective(bank);
						}
					}
				}
			}
			LogUtil.endLog(AppBindCardServiceImpl.class.getName(), "updateAccountBankByUserId", "更新用户银行卡信息结束,用户ID:" + userId);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.errorLog(AppBindCardServiceImpl.class.getName(), "updateAccountBankByUserId", "更新用户银行卡信息失败,用户ID:" + userId, null);
		}
	}

	@Override
	public List<BankCard> getAccountBankByUserId(String logUserId) {
		BankCardExample accountBankExample = new BankCardExample();
		BankCardExample.Criteria aCriteria = accountBankExample.createCriteria();
		aCriteria.andUserIdEqualTo(Integer.valueOf(logUserId));
		aCriteria.andStatusEqualTo(1);
		List<BankCard> list = this.bankCardMapper.selectByExample(accountBankExample);
		// 如果该卡已被用户绑定，则不再录入数据库
		if (list != null && list.size() > 0) {
			return list;
		} else {
			return null;
		}
	}

	/**
	 * 根据用户ID取得用户信息(加密)
	 *
	 * @param userId
	 * @return
	 */
	@Override
	public UsersInfo getUsersInfoByUserId(Integer userId) {
		if (userId != null) {
			UsersInfoExample example = new UsersInfoExample();
			example.createCriteria().andUserIdEqualTo(userId);
			List<UsersInfo> list = this.usersInfoMapper.selectByExample(example);
			if (list.get(0).getIdcard() != null && list.get(0).getIdcard().length() > 15) {
				String idcard = list.get(0).getIdcard().substring(0, 3) + "***********" + list.get(0).getIdcard().substring(list.get(0).getIdcard().length() - 4);
				list.get(0).setIdcard(idcard);
				//获取实名信息
				String trueName = list.get(0).getTruename();
				list.get(0).setTruename(trueName.replaceFirst(trueName.substring(0,1),"*"));
			}
			if (list != null && list.size() > 0) {
				return list.get(0);
			}


		}
		return null;
	}

    /**
     * 根据用户ID取得用户信息(不加密)
     *
     * @param userId
     * @return
     */
    @Override
    public UsersInfo getUsersInfoByUserIdTrue(Integer userId) {
        if (userId != null) {
            UsersInfoExample example = new UsersInfoExample();
            example.createCriteria().andUserIdEqualTo(userId);
            List<UsersInfo> list = this.usersInfoMapper.selectByExample(example);
            if (list != null && list.size() > 0) {
                return list.get(0);
            }


        }
        return null;
    }

	/**
	 * 取得用户信息(加密)
	 *
	 */
	@Override
	public Users getUsers(Integer userId) {
		Users users = usersMapper.selectByPrimaryKey(userId);
		//获取用户电话号码
		if (users.getMobile() != null){
			users.setMobile(users.getMobile().substring(0,3)+"****"+
					users.getMobile().substring(users.getMobile().length()-4));
		}
		return users;
	}

    /**
     * 取得用户信息(不加密)
     *
     */
    @Override
    public Users getUsersTrue(Integer userId) {
        Users users = usersMapper.selectByPrimaryKey(userId);
        return users;
    }
    
    /**
     * 为加强版发送验证码
     *
     * @param request
     * @param form
     * @return Map<String, Object> {success: 1,message:调用验证码接口成功,srvAuthCode:
     *         srvAuthCode}
     */
    @Override
    public BankCallBean cardBindPlusSendSms(Integer userId, String srvTxCode, String mobile, String client,String cardNo) {
        // 调用存管接口发送验证码
        BankCallBean bean = new BankCallBean();
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallMethodConstant.TXCODE_SMSCODE_APPLY);// 交易代码cardBind
        bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
        bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
        bean.setTxDate(GetOrderIdUtils.getOrderDate());// 交易日期
        bean.setTxTime(GetOrderIdUtils.getOrderTime());// 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
        bean.setChannel(client);// 交易渠道000001手机APP 000002网页
		bean.setReqType("2");
        bean.setCardNo(cardNo);
        bean.setSrvTxCode(srvTxCode);
        bean.setMobile(mobile);// 交易渠道
        bean.setSmsType("1");
        bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));// 订单号
        bean.setLogUserId(String.valueOf(userId));// 请求用户名
        try {
            BankCallBean mobileBean = BankCallUtils.callApiBg(bean);
            if (Validator.isNull(mobileBean)) {
                return null;
            }
            // 短信发送返回结果码
            String retCode = mobileBean.getRetCode();
            if (!BankCallConstant.RESPCODE_SUCCESS.equals(retCode) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
                return null;
            }
            if (Validator.isNull(mobileBean.getSrvAuthCode()) && !BankCallConstant.RESPCODE_MOBILE_REPEAT.equals(retCode)) {
                return null;
            }
            // 业务授权码
            String srvAuthCode = mobileBean.getSrvAuthCode();
            if (Validator.isNotNull(mobileBean.getSrvAuthCode())) {
                boolean smsFlag = this.updateBankSmsLog(userId, srvTxCode, srvAuthCode);
                if (smsFlag) {
                    return mobileBean;
                } else {
                    return null;
                }
            } else {
                // 保存用户开户日志
                srvAuthCode = this.selectBankSmsLog(userId, srvTxCode, srvAuthCode);
                if (Validator.isNull(srvAuthCode)) {
                    return null;
                } else {
                    mobileBean.setSrvAuthCode(srvAuthCode);
                    return mobileBean;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
