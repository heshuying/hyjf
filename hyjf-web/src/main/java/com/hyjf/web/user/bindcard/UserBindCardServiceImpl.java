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

package com.hyjf.web.user.bindcard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetSessionOrRequestUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.AccountBank;
import com.hyjf.mybatis.model.auto.AccountBankExample;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.BankAccountLog;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.BankConfigExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.web.BaseServiceImpl;

@Service
public class UserBindCardServiceImpl extends BaseServiceImpl implements UserBindCardService {


    /**
     * 用户绑卡回调方法
     *
     * @param bean
     * @return
     */
	public synchronized String updateAfterBindCard(ChinapnrBean bean, Map<String, String> params) {
		int nowTime = GetDate.getNowTime10(); // 当前时间

		int ret = 0;

		AccountBankExample accountBankExample = new AccountBankExample();
		AccountBankExample.Criteria aCriteria = accountBankExample.createCriteria();
		aCriteria.andUserIdEqualTo(Integer.valueOf(params.get("userId")));
		aCriteria.andAccountEqualTo(bean.getOpenAcctId()); //银行卡账号
		aCriteria.andStatusEqualTo(0);
		List<AccountBank> list= this.accountBankMapper.selectByExample(accountBankExample);
		//如果该卡已被用户绑定，则不再录入数据库
		if(list!=null && list.size()>0){
			return ret+"";
		}
		
        //同步银行卡信息
		this.updateAccountBankByUserId(Integer.valueOf(params.get("userId")));

		//插入操作记录表
		BankAccountLog bankAccountLog = new BankAccountLog();
		bankAccountLog.setUserId(Integer.valueOf(params.get("userId")));
		bankAccountLog.setUserName(this.getUsers(Integer.valueOf(params.get("userId"))).getUsername());
		bankAccountLog.setBankCode(bean.getOpenBankId());
		bankAccountLog.setBankAccount(bean.getOpenAcctId());
		//获取银行卡姓名
		BankConfig config = this.getBankcardConfig(bankAccountLog.getBankCode());
		if(config != null ){
			bankAccountLog.setBankName(config.getName());
		}else{
			bankAccountLog.setBankName("");
		}
		bankAccountLog.setCardType(0);//卡类型 0普通提现卡1默认卡2快捷支付卡
		bankAccountLog.setOperationType(0);//操作类型  0绑定 1删除
		bankAccountLog.setStatus(0);//成功
		bankAccountLog.setCreateTime(nowTime);//操作时间
		
		ret += this.bankAccountLogMapper.insertSelective(bankAccountLog);
		
		return ret +"";
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
		if(StringUtils.isNotEmpty(bankCode)){
			cra.andCodeEqualTo(bankCode);
		}else{
			return null;
		}
		List<BankConfig> banks = bankConfigMapper.selectByExample(example);
		if(banks != null && banks.size() > 0){
			return banks.get(0);
		}
		return null;
	}
	
	@Override
    public void updateAccountBankByUserId(Integer userId) {
        String respCode = "";
        try {
            LogUtil.startLog(UserBindCardServiceImpl.class.getSimpleName(), "updateAccountBankByUserId", "开始更新用户银行卡信息,用户ID:" + userId);
            AccountChinapnrExample example = new AccountChinapnrExample();
            example.createCriteria().andUserIdEqualTo(userId);
            List<AccountChinapnr> list = accountChinapnrMapper.selectByExample(example);
            if (list != null && list.size() != 0) {
                AccountChinapnr accountChinapnr = list.get(0);
                {
                    ChinapnrBean bean = new ChinapnrBean();
                    bean.setVersion(ChinaPnrConstant.VERSION_10);// 接口版本号
                    bean.setCmdId(ChinaPnrConstant.CMDID_QUERY_CARD_INFO); // 银行卡查询接口
                    bean.setMerCustId(PropUtils.getSystem(ChinaPnrConstant.PROP_MERCUSTID)); // 商户客户号
                    bean.setUsrCustId(String.valueOf(accountChinapnr.getChinapnrUsrcustid()));// 用户客户号

                    // 调用汇付接口 4.4.11 银行卡查询接口
                    ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);
                    respCode = chinapnrBean == null ? "" : chinapnrBean.getRespCode();
                    {
                        AccountBankExample AccountBankExample = new AccountBankExample();
                        AccountBankExample.Criteria cri = AccountBankExample.createCriteria();
                        cri.andUserIdEqualTo(accountChinapnr.getUserId());
                        // 如果接口调用成功
                        if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode)) {
                            String UsrCardInfolist = chinapnrBean.getUsrCardInfolist();
                            JSONArray array = JSONObject.parseArray(UsrCardInfolist);
                            if (array.size() != 0) {
                                Boolean hasExpress = false;// 拥有快捷卡
                                List<AccountBank> accountBankList = new ArrayList<AccountBank>();
                                for (int j = 0; j < array.size(); j++) {
                                    JSONObject obj = array.getJSONObject(j);
                                    if (!obj.getString("RealFlag").equals("R")) {
                                        // 只有实名卡才入库
                                        continue;
                                    }
                                    AccountBank bank = new AccountBank();
                                    bank.setUserId(accountChinapnr.getUserId());
                                    bank.setCardType("0");// 普通卡
                                    bank.setStatus(1);// 默认都是1
                                    if (obj.getString("IsDefault").equals("Y")) {
                                        bank.setCardType("1");// 默认卡
                                    }
                                    if (obj.getString("ExpressFlag").equals("Y")) {
                                        bank.setStatus(0);// 快捷卡
                                        bank.setCardType("2");// 快捷卡
                                        hasExpress = true;
                                    }
                                    bank.setAccount(obj.getString("CardId"));
                                    bank.setBank(obj.getString("BankId"));
                                    bank.setProvince(obj.getString("ProvId"));
                                    bank.setCity("0");
                                    bank.setArea(obj.getInteger("AreaId"));
                                    bank.setAddip(getIp());
                                    bank.setUpdateIp(getIp());
                                    if (StringUtils.isNotEmpty(obj.getString("UpdDateTime"))) {
                                        bank.setAddtime(GetDate.strYYYYMMDDTimestamp(obj.getString("UpdDateTime")) + "");
                                        bank.setUpdateTime(GetDate.strYYYYMMDDTimestamp(obj.getString("UpdDateTime")) + "");
                                    } else {
                                        bank.setAddtime(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.dateToString(new Date())) + "");
                                        bank.setUpdateTime(GetDate.strYYYYMMDDHHMMSS2Timestamp2(GetDate.dateToString(new Date())) + "");
                                    }
                                    accountBankList.add(bank);
                                }
                                if (!hasExpress) {
                                    // 如果没有快捷卡,则将status都置为0
                                    for (AccountBank bank : accountBankList) {
                                        bank.setStatus(0);
                                    }
                                }
                                // 数据库操作
                                accountBankMapper.deleteByExample(AccountBankExample);
                                for (AccountBank bank : accountBankList) {
                                    accountBankMapper.insertSelective(bank);
                                }
                            }
                        }
                    }
                }
            }
            LogUtil.endLog(UserBindCardServiceImpl.class.getSimpleName(), "updateAccountBankByUserId", "更新用户银行卡信息结束,用户ID:" + userId);
        }catch (Exception e) {
            LogUtil.errorLog(UserBindCardServiceImpl.class.getSimpleName(), "updateAccountBankByUserId", "更新用户银行卡信息失败,用户ID:" + userId, null);
        }
	}
      
	private static String getIp() {
        String ip = "";
        try {
            ip = GetCilentIP.getIpAddr(GetSessionOrRequestUtils.getRequest());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            // e.printStackTrace();
            ip = "127.0.0.1";
        }
        return ip;
    }

}
