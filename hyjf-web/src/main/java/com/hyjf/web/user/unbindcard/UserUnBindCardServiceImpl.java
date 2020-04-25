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

package com.hyjf.web.user.unbindcard;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.plexus.util.StringUtils;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetCilentIP;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetSessionOrRequestUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.AccountBank;
import com.hyjf.mybatis.model.auto.AccountBankExample;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;
import com.hyjf.web.BaseServiceImpl;
import com.hyjf.web.user.regist.UserRegistServiceImpl;

@Service
public class UserUnBindCardServiceImpl extends BaseServiceImpl implements UserUnBindCardService {

	@Override
	public Integer getUserIdByCustId(Long custId) {
		AccountChinapnrExample example = new AccountChinapnrExample();
		example.createCriteria().andChinapnrUsrcustidEqualTo(custId);
		List<AccountChinapnr> list = accountChinapnrMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0).getUserId();
		}
		return null;
	}

	@Override
	public String updateAccountBankByUserId(Integer userId) {
		String respCode = "";
		try {
			LogUtil.startLog(UserRegistServiceImpl.class.getSimpleName(), "updateAccountBankByUserId", "解绑快捷卡,开始更新用户银行卡信息,用户ID:" + userId);
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
								// =============================两步:第一步:更新银行卡,第二步:更新身份证号等等=============================
								// =============================第一步:更新银行卡=============================
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
								// 第二步,更新身份证号等等
								UsersInfoExample uie = new UsersInfoExample();
								uie.createCriteria().andUserIdEqualTo(accountChinapnr.getUserId());
								List<UsersInfo> l = usersInfoMapper.selectByExample(uie);
								if (l != null && l.size() == 1) {
									UsersInfo usersInfo = l.get(0);
									JSONObject obj = array.getJSONObject(0);
									// 身份证号
									String CertId = obj.getString("CertId");
									String UsrName = obj.getString("UsrName");
									if (Validator.isNotNull(CertId) && (usersInfo.getIdcard() == null || usersInfo.getIdcard().equals("0") || usersInfo.getIdcard().length() < 18)) {
										if (CertId.length() == 18) {
											usersInfo.setIdcard(CertId);
											int sexInt = Integer.parseInt(CertId.substring(16, 17));
											if (sexInt % 2 == 0) {
												sexInt = 2;
											} else {
												sexInt = 1;
											}
											usersInfo.setSex(sexInt);
											// 出生日期
											String birthDay = CertId.substring(6, 14);
											usersInfo.setBirthday(birthDay);
										}
									}
									if (StringUtils.isEmpty(usersInfo.getTruename()) || usersInfo.getTruename().equals("0")) {
										if (StringUtils.isNotEmpty(UsrName)) {
											usersInfo.setTruename(UsrName);
										}
									}
									usersInfoMapper.updateByPrimaryKey(usersInfo);
								}
							} else {
								accountBankMapper.deleteByExample(AccountBankExample);
							}
							return respCode;
						} else {
							String respDesc = "";
							if (StringUtils.isNotEmpty(chinapnrBean.getRespDesc())) {
								respDesc = chinapnrBean.getRespDesc();
							}
							LogUtil.errorLog(UserRegistServiceImpl.class.getSimpleName(), "updateAccountChinapnrBank",
									"解绑快捷卡,更新用户银行卡信息失败,用户ID:" + accountChinapnr.getUserId() + ",错误代码:" + respCode + ",错误内容:" + respDesc, null);
							return respDesc;
						}
					}
				}
			} else {
				respCode = "无效的用户ID";
			}
			LogUtil.endLog(UserRegistServiceImpl.class.getSimpleName(), "updateAccountBankByUserId", "解绑快捷卡,更新用户银行卡信息结束,用户ID:" + userId);
		} catch (Exception e) {
			LogUtil.errorLog(UserRegistServiceImpl.class.getSimpleName(), "updateAccountBankByUserId", "解绑快捷卡,更新用户银行卡信息失败,用户ID:" + userId, null);
		}
		return respCode;
	}

	private static String getIp() {
		String ip = "";
		try {
			ip = GetCilentIP.getIpAddr(GetSessionOrRequestUtils.getRequest());
		} catch (Exception e) {
			ip = "127.0.0.1";
		}
		return ip;
	}
}
