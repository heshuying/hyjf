package com.hyjf.server.module.wkcd.user.openAccount;


import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLDecoder;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.IdCard15To18;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetail;
import com.hyjf.mybatis.model.auto.AppChannelStatisticsDetailExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.server.BaseServiceImpl;

@Service
public class WkcdOpenAccountServiceImpl extends BaseServiceImpl implements WkcdOpenAccountService {
	
	@Autowired
	private PlatformTransactionManager transactionManager;
	@Autowired
	private TransactionDefinition transactionDefinition;
	
	/**
	 * 保存开户的数据
	 */
	@Override
	public boolean userOpenAccount(ChinapnrBean bean) {

		// 手动控制事务
		TransactionStatus txStatus = null;
		// 注册人ID
		Integer userId = bean.getLogUserId();
		System.out.println("saveOpenAccount----------------  userId:" + userId);
		// 判断是否存在
		UsersExample uexample = new UsersExample();
		UsersExample.Criteria crtu = uexample.createCriteria();
		crtu.andUserIdEqualTo(userId);
		List<Users> users = usersMapper.selectByExample(uexample);
		if (users != null && users.size() == 1) {
			// 更新用户表
			Users user = users.get(0);
			if (user.getOpenAccount() == 1) {
				return true;
			} else {
				try{
					// 开启事务
					txStatus = this.transactionManager.getTransaction(transactionDefinition);
					// 当前时间
					int nowTime = GetDate.getNowTime10();
					// 身份证号
					String idCard = bean.getIdNo();
					System.out.println("身份证号码:" + idCard);
					// 真实姓名
					String trueName = null;
					try {
						trueName = URLDecoder.decode(bean.getUsrName(), "UTF-8");
					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
					}
					if (idCard != null && idCard.length() < 18) {
						try {
							idCard = IdCard15To18.getEighteenIDCard(idCard);
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
					// 性别
					int sexInt = Integer.parseInt(idCard.substring(16, 17));
					if (sexInt % 2 == 0) {
						sexInt = 2;
					} else {
						sexInt = 1;
					}
					// 出生日期
					String birthDayTemp = idCard.substring(6, 14);
					String birthDay = StringUtils.substring(birthDayTemp, 0, 4) + "-"
							+ StringUtils.substring(birthDayTemp, 4, 6) + "-" + StringUtils.substring(birthDayTemp, 6, 8);
					// 更新用户表
					user.setOpenAccount(1); // 已经开户
					// 更新手机号
					user.setMobile(bean.getUsrMp());
					user.setAccountEsb(Integer.valueOf(bean.getLogClient()));
					user.setRechargeSms(0);
					user.setWithdrawSms(0);
					// 更新用户详细信息表
					UsersInfoExample uiexample = new UsersInfoExample();
					UsersInfoExample.Criteria crtui = uiexample.createCriteria();
					crtui.andUserIdEqualTo(userId);
					List<UsersInfo> userInfos = usersInfoMapper.selectByExample(uiexample);
					if (userInfos != null && userInfos.size() == 1) {
						boolean userInfoFlag = false;
						UsersInfo usersInfo = userInfos.get(0);
						usersInfo.setTruename(trueName);
						usersInfo.setIdcard(idCard);
						usersInfo.setSex(sexInt);
						usersInfo.setBirthday(birthDay);
						usersInfo.setTruenameIsapprove(1);
						usersInfo.setMobileIsapprove(1);
						userInfoFlag = usersInfoMapper.updateByPrimaryKeySelective(usersInfo) > 0 ? true : false;
						if (userInfoFlag) {
							boolean accountChinapnrFlag = false;
							// 插入汇付关联表
							AccountChinapnr accountChinapnr = new AccountChinapnr();
							accountChinapnr.setUserId(userId);
							accountChinapnr.setChinapnrUsrid(bean.getUsrId());
							accountChinapnr.setChinapnrUsrcustid(Long.valueOf(bean.getUsrCustId()));
							accountChinapnr.setAddtime(String.valueOf(nowTime));
							accountChinapnr.setAddip(bean.getLogIp());
							accountChinapnr.setIsok(1);
							accountChinapnr.setEggsIsok(0);
							accountChinapnrFlag = this.accountChinapnrMapper.insert(accountChinapnr) > 0 ? true : false;
							if (accountChinapnrFlag) {
								boolean usersFlag = false;
								crtu.andVersionEqualTo(user.getVersion());
								user.setVersion(user.getVersion().add(new BigDecimal("1")));
								usersFlag = usersMapper.updateByExampleSelective(user, uexample) > 0 ? true : false;
								if (usersFlag) {
									// 开户更新开户渠道统计开户时间
									AppChannelStatisticsDetailExample example = new AppChannelStatisticsDetailExample();
									AppChannelStatisticsDetailExample.Criteria crt = example.createCriteria();
									crt.andUserIdEqualTo(userId);
									List<AppChannelStatisticsDetail> appChannelStatisticsDetails = this.appChannelStatisticsDetailMapper
											.selectByExample(example);
									if (appChannelStatisticsDetails != null && appChannelStatisticsDetails.size() == 1) {
										AppChannelStatisticsDetail channelDetail = appChannelStatisticsDetails.get(0);
										channelDetail.setOpenAccountTime(new Date());
										this.appChannelStatisticsDetailMapper.updateByPrimaryKeySelective(channelDetail);
									}
									// 提交事务
					                this.transactionManager.commit(txStatus);
									return true;
								} else {
									throw new RuntimeException("更新用户表失败！");
								}
							} else {
								throw new RuntimeException("插入用户开户表失败！");
							}
						} else {
							throw new RuntimeException("更新用户详情表失败！");
						}
					} else {
						throw new RuntimeException("用户详情表数据错误，用户id：" + user.getUserId());
					} 
				}catch(Exception e){
					// 回滚事务
		            this.transactionManager.rollback(txStatus);
                    return false;
				}
			}
		} else {
			throw new RuntimeException("未查询到用户数据，用户id：" + userId);
		}
	}

	
	/**
	 * 根据手机号统计用户的数量
	 * @param userId 
	 * 
	 * @param mobile
	 * @return
	 */
	@Override
	public JSONObject selectUserByMobile(int userId,String mobile) {

		JSONObject ret = new JSONObject();
		UsersExample userExample = new UsersExample();
		UsersExample.Criteria crt = userExample.createCriteria();
		crt.andMobileEqualTo(mobile);
		List<Users> users = this.usersMapper.selectByExample(userExample);
		if (users != null && users.size() == 1) {
			return null;
		} else if (users.size() > 0) {
			for (Users user : users) {
				if (user.getUserId() == userId) {
					ret.put("newUserName", user.getUsername());
				} else {
					String oldUserName = "";
					if(StringUtils.isNotBlank(ret.getString("oldUserName"))){
						oldUserName =  ret.getString("oldUserName") + "," + user.getUsername();
						ret.put("oldUserName", oldUserName);
					}else{
						oldUserName = user.getUsername();
						ret.put("oldUserName", oldUserName);
					}
				}
			}
		}
		return ret;
	}

	
}


