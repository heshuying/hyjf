package com.hyjf.bank.service.user.bindcard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.hyjf.bank.service.mq.MqService;
import com.hyjf.common.util.RabbitMQConstants;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.BaseServiceImpl;
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
 * 用户绑卡Serice实现类
 * 
 * @author liuyang
 *
 */
@Service
public class BindCardServiceImpl extends BaseServiceImpl implements BindCardService {
	// 声明log日志
		private Logger logger = LoggerFactory.getLogger(BindCardServiceImpl.class);

    @Autowired
    private MqService mqService;

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
	@Override
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
	 * @param bankCode
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

	private void updateAccountBankByUserId(Integer userId) {
		String respCode = "";
		try {
			LogUtil.startLog(BindCardServiceImpl.class.getSimpleName(), "updateAccountBankByUserId", "开始更新用户银行卡信息,用户ID:" + userId);
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
				bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
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
							// 如果此时银联行号还是为空,根据bankId查询本地存的银联行号
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
						int count = bankCardMapper.countByExample(bankCardExample);
						if (count > 0) {// modify by cwyang 初始化状态即账户没卡时,不用操作数据库
							// 数据库操作
							boolean isDelFlag = bankCardMapper.deleteByExample(bankCardExample) > 0 ? true : false;
							if (!isDelFlag) {
								throw new Exception("银行卡删除失败~!,userid is " + bankOpenAccount.getUserId());
							}
						}

						for (BankCard bank : bankCardList) {
							boolean isInsertFlag = bankCardMapper.insertSelective(bank) > 0 ? true : false;
							if (!isInsertFlag) {
								throw new Exception("银行卡插入失败~!");
							}
						}
					}
				}
			}
			LogUtil.endLog(BindCardServiceImpl.class.getSimpleName(), "updateAccountBankByUserId", "更新用户银行卡信息结束,用户ID:" + userId);
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.errorLog(BindCardServiceImpl.class.getSimpleName(), "updateAccountBankByUserId", "更新用户银行卡信息失败,用户ID:" + userId, null);
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
     * 为加强版发送验证码
     *
     * @param userId
     * @param instCode
     * @param srvTxCode
     * @param mobile
     * @param client
     * @param cardNo
     * @return Map<String, Object> {success: 1,message:调用验证码接口成功,srvAuthCode:
     *         srvAuthCode}
     */
    @Override
    public BankCallBean cardBindPlusSendSms(Integer userId,String instCode, String srvTxCode, String mobile, String client,String cardNo) {
        // 调用存管接口发送验证码
        BankCallBean bean = new BankCallBean();
        bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bean.setTxCode(BankCallMethodConstant.TXCODE_SMSCODE_APPLY);// 交易代码cardBind
        bean.setInstCode(instCode);// 机构代码
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

	@Override
	public ModelAndView getCallbankMV(BindCardPageBean bean) {
		ModelAndView mv = new ModelAndView();
        // 获取共同参数
        String bankCode = PropUtils.getSystem(BankCallConstant.BANK_BANKCODE);
        String bankInstCode = PropUtils.getSystem(BankCallConstant.BANK_INSTCODE);
        String orderDate = GetOrderIdUtils.getOrderDate();
        String txDate = GetOrderIdUtils.getTxDate();
        String txTime = GetOrderIdUtils.getTxTime();
        String seqNo = GetOrderIdUtils.getSeqNo(6);
        String idType = BankCallConstant.ID_TYPE_IDCARD;
        
        // 调用开户接口
        BankCallBean bindCardBean = new BankCallBean();
        bindCardBean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
        bindCardBean.setTxCode(bean.getTxCode());// 消息类型(用户开户)
        bindCardBean.setInstCode(bankInstCode);// 机构代码
        bindCardBean.setBankCode(bankCode);
        bindCardBean.setTxDate(txDate);
        bindCardBean.setTxTime(txTime);
        bindCardBean.setSeqNo(seqNo);
        bindCardBean.setChannel(bean.getChannel());
        bindCardBean.setIdType(idType);
        bindCardBean.setIdNo(bean.getIdNo());
        bindCardBean.setName(bean.getName());
        bindCardBean.setAccountId(bean.getAccountId());
        bindCardBean.setUserIP(bean.getUserIP());
        bindCardBean.setRetUrl(bean.getRetUrl());
        bindCardBean.setSuccessfulUrl(bean.getSuccessfulUrl());
        bindCardBean.setForgotPwdUrl(bean.getForgetPassworedUrl());
        bindCardBean.setNotifyUrl(bean.getNotifyUrl());
        // 页面调用必须传的
        String orderId = GetOrderIdUtils.getOrderId2(bean.getUserId());
        bindCardBean.setLogBankDetailUrl(BankCallConstant.BANK_URL_BIND_CARD_PAGE);
        bindCardBean.setLogOrderId(orderId);
        bindCardBean.setLogOrderDate(orderDate);
        bindCardBean.setLogUserId(String.valueOf(bean.getUserId()));
        bindCardBean.setLogRemark("外部服务接口:绑卡页面");
        bindCardBean.setLogIp(bean.getUserIP());
        bindCardBean.setLogClient(Integer.parseInt(bean.getPlatform()));
        try {
            mv = BankCallUtils.callApi(bindCardBean);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mv;
	}

	@Override
    public void updateCardNoToBank(BankCallBean bankCallBean) {
        Integer userId = Integer.parseInt(bankCallBean.getLogUserId());
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
        bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道
        bean.setAccountId(bankCallBean.getAccountId());// 存管平台分配的账号
        bean.setState("1"); // 查询状态 0-所有（默认） 1-当前有效的绑定卡
        bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());
        bean.setLogUserId(String.valueOf(userId));
        // 调用汇付接口 4.4.11 银行卡查询接口
        BankCallBean call = BankCallUtils.callApiBg(bean);
        String respCode = call == null ? "" : call.getRetCode();
        // 如果接口调用成功
        if (BankCallConstant.RESPCODE_SUCCESS.equals(respCode)) {
            String usrCardInfolist = call.getSubPacks();
            JSONArray array = JSONObject.parseArray(usrCardInfolist);
            JSONObject obj = null;
            logger.info(array.size()+"           array:"+array.toString());
            if(array != null && array.size() != 0){
                obj = array.getJSONObject(0);
            }else{
            	return;
            }
            logger.info(obj.getString("cardNo"));
            BankCardExample accountBankExample = new BankCardExample();
    		BankCardExample.Criteria aCriteria = accountBankExample.createCriteria();
    		aCriteria.andUserIdEqualTo(userId);
    		aCriteria.andCardNoEqualTo(obj.getString("cardNo")); // 银行卡账号
    		aCriteria.andStatusEqualTo(1);
    		List<BankCard> list = this.bankCardMapper.selectByExample(accountBankExample);
    		if(list!=null&&list.size()>0){
    			return;
    		}
            BankCard bank = new BankCard();
            bank.setUserId(userId);
            // 设置绑定的手机号
            bank.setMobile(bankCallBean.getMobile());
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
            // 如果此时银联行号还是为空,根据bankId查询本地存的银联行号
            if (StringUtils.isBlank(payAllianceCode)) {
                payAllianceCode = this.getPayAllianceCodeByBankId(bankId);
            }
            bank.setPayAllianceCode(payAllianceCode);
            bankCallBean.setPayAllianceCode(payAllianceCode);
            // 三方接口回调优化：增加返回手机号和银行卡号 add by liushouyi 20180821
            bankCallBean.setCardNo(obj.getString("cardNo"));
            SimpleDateFormat sdf = GetDate.yyyymmddhhmmss;
            try {
                bank.setCreateTime(sdf.parse(obj.getString("txnDate") + obj.getString("txnTime")));
            } catch (ParseException e) {
            }
            bank.setCreateUserId(userId);
            bank.setCreateUserName(user.getUsername());
            bankCardMapper.insertSelective(bank);

            // add 合规数据上报 埋点 liubin 20181122 start
            // 推送数据到MQ 用户信息修改（绑卡异步）
            JSONObject params = new JSONObject();
            params.put("userId", userId);
            this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.USERINFO_CHANGE_DELAY_KEY, JSONObject.toJSONString(params));
            // add 合规数据上报 埋点 liubin 20181122 end
        }
    }

    // 江西银行绑卡接口修改 update by wj 2018-5-17 start
    /**
     * 判断江西银行绑卡使用新/旧接口
     * @param interfaceType
     * @author: WangJun
     * @return 0旧接口/1新接口
     */
    @Override
    public Integer getBandCardBindUrlType(String interfaceType) {
        BankInterfaceExample bankInterfaceExample = new BankInterfaceExample();
        BankInterfaceExample.Criteria criteria= bankInterfaceExample.createCriteria();
        criteria.andInterfaceTypeEqualTo(interfaceType);
        criteria.andIsDeleteEqualTo(0);
        criteria.andIsUsableEqualTo(1);
        bankInterfaceExample.setLimitStart(0);
        bankInterfaceExample.setLimitEnd(1);
        bankInterfaceExample.setOrderByClause("`update_time` DESC");
        List<BankInterface> bankInterfaces = bankInterfaceMapper.selectByExample(bankInterfaceExample);
        if(bankInterfaces.size() > 0){
            //返回接口类型
            return bankInterfaces.get(0).getInterfaceStatus();
        }else {
            //默认绑卡旧接口
            return 0;
        }
    }
    // 江西银行绑卡接口修改 update by wj 2018-5-17 end

}
