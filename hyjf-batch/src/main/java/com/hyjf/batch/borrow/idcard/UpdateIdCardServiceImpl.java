package com.hyjf.batch.borrow.idcard;

import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.log.LogUtil;
import com.hyjf.mybatis.mapper.customize.admin.AdminChinapnrLogCustomizeMapper;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.admin.AdminChinapnrLogCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

@Service
public class UpdateIdCardServiceImpl extends BaseServiceImpl implements UpdateIdCardService {

	@Autowired
	private AdminChinapnrLogCustomizeMapper chinapnrLogCustomizeMapper;

	@Override
	public List<AdminChinapnrLogCustomize> getAllOpenAccount() {
		List<AdminChinapnrLogCustomize> allOpenAccounts = chinapnrLogCustomizeMapper.getAllOpenAccount();
		return allOpenAccounts;
	}

	private static String ip = null;

	private static String getIp() {
		if (ip == null) {
			try {
				ip = InetAddress.getLocalHost().getHostAddress();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ip = "127.0.0.1";
			}
		}
		return ip;
	}

	@Override
	public int updateIdCard(AdminChinapnrLogCustomize chinapnrLog) {

		String msgData = chinapnrLog.getMsgdata();
		try {
			JSONObject data = JSONObject.parseObject(msgData);
			String userName = data.getString("UsrName");// 用户真实姓名
			String usrCustId = data.getString("UsrCustId");// 汇付的客户号
			String usrId = data.getString("UsrId");// 汇付的帐号
			String idCard = data.getString("IdNo");// 身份证号码
			UsersInfoExample example = new UsersInfoExample();
			example.createCriteria().andUserIdEqualTo(chinapnrLog.getUserId());
			List<UsersInfo> users = usersInfoMapper.selectByExample(example);
			if (users != null && users.size() == 1) {
				LogUtil.startLog(UpdateIdCardServiceImpl.class.getSimpleName(), "updateIdCardByUserId",
						"开始更新用户身份证信息,用户ID:" + chinapnrLog.getUserId());
				UsersInfo usersInfo = users.get(0);
				// 获取用户user_id
				int userId = usersInfo.getUserId();
				// 获取用户当前的真实姓名
				if (StringUtils.isNotEmpty(idCard) && !idCard.equals("0")) {
					// 调用汇付接口(4.4.18. 用户信息查询)
					ChinapnrBean bean = new ChinapnrBean();
					bean.setVersion(ChinaPnrConstant.VERSION_10);
					bean.setCmdId(ChinaPnrConstant.CMDID_QUERY_USR_INFO); // 消息类型(必须)
					bean.setCertId(idCard); // 身份证号
					// 写log用参数
					bean.setLogUserId(userId); // 操作者ID
					bean.setLogRemark("用户身份证更新"); // 备注
					bean.setLogClient("0"); // PC
					bean.setLogIp(getIp()); // IP地址
					// 调用汇付接口
					ChinapnrBean chinaPnrBean = ChinapnrUtil.callApiBg(bean);

					if (chinaPnrBean == null) {
						usersInfo.setIdcard("9");
						int flag = usersInfoMapper.updateByPrimaryKeySelective(usersInfo);
						LogUtil.errorLog(UpdateIdCardServiceImpl.class.getSimpleName(), "updateIdCardByUserId",
								"更新用户身份证信息错误,用户ID:" + chinapnrLog.getUserId(), new Exception("未获取到汇付返回的用户信息"));
						return flag;
					} else {
						// 接口返回正常时,执行更新操作
						String repdesc = URLDecoder.decode(chinaPnrBean.getRespDesc(), "UTF-8");
						System.out.println(repdesc);
						if (ChinaPnrConstant.RESPCODE_SUCCESS.equals(chinaPnrBean.getRespCode())) {
							if (usrId.equals(chinaPnrBean.getUsrId()) && usrCustId.equals(chinaPnrBean.getUsrCustId())) {// 如果汇付天下的客户号不是空
								// 是否包含平台的user_id
								String userIdStr = String.valueOf(userId);
								int index = usrId.lastIndexOf(userIdStr);
								if (index + userIdStr.length() == usrId.length()) {// 如果平台的user_id的长度加上index为汇付客户号
									usersInfo.setIdcard(idCard);
									int sexInt = Integer.parseInt(idCard.substring(16, 17));
									if (sexInt % 2 == 0) {
										sexInt = 2;
									} else {
										sexInt = 1;
									}
									usersInfo.setSex(sexInt);
									// 出生日期
									String birthDay = idCard.substring(6, 14);
									usersInfo.setBirthday(birthDay);
									if (StringUtils.isNotEmpty(userName) && !userName.equals("0")) {
										try {
											userName = URLDecoder.decode(userName, "UTF-8");
											usersInfo.setTruename(userName);
										} catch (UnsupportedEncodingException e) {
											LogUtil.errorLog(UpdateIdCardServiceImpl.class.getSimpleName(),
													"updateIdCardByUserId",
													"更新用户身份证信息错误,真实姓名解码错误。用户ID:" + chinapnrLog.getUserId(),
													new Exception("真实姓名解码错误"));
											e.printStackTrace();
										}
									}
									int flag = usersInfoMapper.updateByPrimaryKeySelective(usersInfo);
									LogUtil.endLog(UpdateIdCardServiceImpl.class.getSimpleName(),
											"updateIdCardByUserId", "结束更新用户身份证信息,用户ID:" + chinapnrLog.getUserId());
									return flag;
								} else {
									LogUtil.errorLog(UpdateIdCardServiceImpl.class.getSimpleName(),
											"updateIdCardByUserId", "更新用户身份证信息错误,用户ID:" + chinapnrLog.getUserId(),
											new Exception("用户userId不对应"));
								}
							} else {
								LogUtil.errorLog(UpdateIdCardServiceImpl.class.getSimpleName(), "updateIdCardByUserId",
										"更新用户身份证信息错误,用户ID:" + chinapnrLog.getUserId(), new Exception("汇付返回信息同log信息不符"));
							}
						} else {
							LogUtil.errorLog(UpdateIdCardServiceImpl.class.getSimpleName(), "updateIdCardByUserId",
									"更新用户身份证信息错误,用户ID:" + chinapnrLog.getUserId(), new Exception("调用汇付接口失败"));
						}
					}
				} else {
					LogUtil.errorLog(UpdateIdCardServiceImpl.class.getSimpleName(), "updateIdCardByUserId",
							"更新用户身份证信息错误,用户ID:" + chinapnrLog.getUserId(), new Exception("log表中的身份证错误"));
				}
			} else {
				if (users == null) {
					LogUtil.errorLog(UpdateIdCardServiceImpl.class.getSimpleName(), "updateIdCardByUserId",
							"更新用户身份证信息错误,用户ID:" + chinapnrLog.getUserId(), new Exception("未查询到用户详情数据"));
				} else {
					LogUtil.errorLog(UpdateIdCardServiceImpl.class.getSimpleName(), "updateIdCardByUserId",
							"更新用户身份证信息错误,用户ID:" + chinapnrLog.getUserId(), new Exception("用户详情数据多于一条"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			LogUtil.errorLog(UpdateIdCardServiceImpl.class.getSimpleName(), "updateIdCardByUserId",
					"开始更新用户身份证信息,用户ID:" + chinapnrLog.getUserId(), new Exception("json数据转化错误"));

		}
		return 0;
	}
}