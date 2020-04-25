package com.hyjf.admin;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddDessenesitizationBean;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractConstant;
import com.hyjf.common.http.HtmlUtil;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.CustomUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.AccountChinapnrExample;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.Admin;
import com.hyjf.mybatis.model.auto.AdminExample;
import com.hyjf.mybatis.model.auto.AdminRole;
import com.hyjf.mybatis.model.auto.AdminRoleExample;
import com.hyjf.mybatis.model.auto.AdminUtmReadPermissions;
import com.hyjf.mybatis.model.auto.AdminUtmReadPermissionsExample;
import com.hyjf.mybatis.model.auto.BankConfig;
import com.hyjf.mybatis.model.auto.BankConfigExample;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.BankOpenAccountExample;
import com.hyjf.mybatis.model.auto.BankReturnCodeConfig;
import com.hyjf.mybatis.model.auto.BankReturnCodeConfigExample;
import com.hyjf.mybatis.model.auto.BanksConfig;
import com.hyjf.mybatis.model.auto.BorrowConfig;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowProjectTypeExample;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.BorrowStyleExample;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;
import com.hyjf.mybatis.model.auto.CardBin;
import com.hyjf.mybatis.model.auto.CardBinExample;
import com.hyjf.mybatis.model.auto.ChinapnrLog;
import com.hyjf.mybatis.model.auto.Department;
import com.hyjf.mybatis.model.auto.DepartmentExample;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.HjhAssetType;
import com.hyjf.mybatis.model.auto.HjhAssetTypeExample;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.HjhInstConfigExample;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.hyjf.mybatis.model.auto.HjhPlanAssetExample;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.auto.ParamNameExample;
import com.hyjf.mybatis.model.auto.SiteSetting;
import com.hyjf.mybatis.model.auto.SiteSettingExample;
import com.hyjf.mybatis.model.auto.SmsCode;
import com.hyjf.mybatis.model.auto.SmsLogWithBLOBs;
import com.hyjf.mybatis.model.auto.SpreadsUsers;
import com.hyjf.mybatis.model.auto.SpreadsUsersExample;
import com.hyjf.mybatis.model.auto.TenderAgreement;
import com.hyjf.mybatis.model.auto.TenderAgreementExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersExample;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.OADepartmentCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponRecoverCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponTenderDetailCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

public class BaseServiceImpl extends CustomizeMapper implements BaseService {

	@Autowired
	@Qualifier("myAmqpTemplate")
	private RabbitTemplate rabbitTemplate;

	/**
	 * 获取部门列表
	 *
	 * @return
	 */
	public List<Department> getDepartmentList() {
		DepartmentExample example = new DepartmentExample();
		example.createCriteria().andNameNotLike("%居间人%").andNameNotLike("%借款人%").andNameNotLike("%无主单%");
		example.setOrderByClause(" pid, sort ");
		return departmentMapper.selectByExample(example);
	}

	/**
	 * 获取部门列表
	 *
	 * @return
	 */
	public JSONArray getCrmDepartmentList(String[] selectedNode) {
		List<OADepartmentCustomize> departmentList = userInfoCustomizeMapper.queryDepartmentInfo(null);

		Map<String, String> map = new HashMap<String, String>();
		if (departmentList != null && departmentList.size() > 0) {
			for (OADepartmentCustomize oaDepartment : departmentList) {
				map.put(String.valueOf(oaDepartment.getId()), HtmlUtil.unescape(oaDepartment.getName()));
			}
		}

		return this.treeDepartmentList(departmentList, map, selectedNode, "0", "");
	}

	/**
	 * 部门树形结构
	 *
	 * @param departmentTreeDBList
	 * @param topParentDepartmentCd
	 * @return
	 */
	private JSONArray treeDepartmentList(List<OADepartmentCustomize> departmentTreeDBList, Map<String, String> map, String[] selectedNode, String topParentDepartmentCd,
			String topParentDepartmentName) {
		JSONArray ja = new JSONArray();
		JSONObject joAttr = new JSONObject();
		if (departmentTreeDBList != null && departmentTreeDBList.size() > 0) {
			JSONObject jo = null;
			for (OADepartmentCustomize departmentTreeRecord : departmentTreeDBList) {
				jo = new JSONObject();

				jo.put("id", departmentTreeRecord.getId());
				jo.put("text", departmentTreeRecord.getName());
				joAttr = new JSONObject();
				joAttr.put("id", departmentTreeRecord.getId());
				joAttr.put("parentid", departmentTreeRecord.getParentid());
				joAttr.put("parentname", Validator.isNull(topParentDepartmentName) ? "" : topParentDepartmentName);
				joAttr.put("name", departmentTreeRecord.getName());
				joAttr.put("listorder", departmentTreeRecord.getListorder());
				jo.put("li_attr", joAttr);
				if (Validator.isNotNull(selectedNode) && ArrayUtils.contains(selectedNode, String.valueOf(departmentTreeRecord.getId()))) {
					JSONObject selectObj = new JSONObject();
					selectObj.put("selected", true);
					// selectObj.put("opened", true);
					jo.put("state", selectObj);
				}

				String departmentCd = String.valueOf(departmentTreeRecord.getId());
				String departmentName = String.valueOf(departmentTreeRecord.getName());
				String parentDepartmentCd = String.valueOf(departmentTreeRecord.getParentid());
				if (topParentDepartmentCd.equals(parentDepartmentCd)) {
					JSONArray array = treeDepartmentList(departmentTreeDBList, map, selectedNode, departmentCd, departmentName);
					jo.put("children", array);
					ja.add(jo);
				}
			}
		}
		return ja;
	}

	/**
	 * 获取银行列表
	 *
	 * @return
	 */
	public List<BankConfig> getBankConfig(BankConfig bankConfig) {
		BankConfigExample example = new BankConfigExample();
		com.hyjf.mybatis.model.auto.BankConfigExample.Criteria criteria = example.createCriteria();
		// 条件查询
		criteria.andStatusEqualTo(1);
		return bankConfigMapper.selectByExample(example);
	}

	/**
	 * 获取角色列表
	 *
	 * @return
	 */
	public List<AdminRole> getAdminRoleList() {
		AdminRoleExample example = new AdminRoleExample();
		AdminRoleExample.Criteria criteria = example.createCriteria();
		criteria.andStateEqualTo(String.valueOf(CustomConstants.FLAG_STATUS_ENABLE));
		criteria.andDelFlagEqualTo(CustomConstants.FLAG_NORMAL);
		example.setOrderByClause(" sort ");
		return adminRoleMapper.selectByExample(example);
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
			if (StringUtils.isNotEmpty(checkCode)) {
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
	 * 获取用户在汇付天下的账号信息
	 *
	 * @return
	 */
	public AccountChinapnr getChinapnrUserInfo(Integer userId) {
		AccountChinapnrExample example = new AccountChinapnrExample();
		AccountChinapnrExample.Criteria criteria = example.createCriteria();
		criteria.andUserIdEqualTo(userId);
		List<AccountChinapnr> list = this.accountChinapnrMapper.selectByExample(example);
		if (list != null && list.size() == 1) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 写入日志
	 *
	 * @return
	 */
	public int insertChinapnrLog(ChinapnrLog chinapnrLog) {
		return chinapnrLogMapper.insertSelective(chinapnrLog);
	}

	@Override
	public boolean SendMail(String title, String content, String[] filenames, String[] emails) {
		Transport tran = null;
		try {
			// 发送人员
			String to[] = emails;
			Properties p = new Properties(); // Properties p =
												// System.getProperties();
			p.put("mail.smtp.auth", "true");
			p.put("mail.transport.protocol", "smtp");
			p.put("mail.smtp.host", "smtp.163.com");
			p.put("mail.smtp.port", "25");

			List<SiteSetting> user_email = siteSettingMapper.selectByExample(new SiteSettingExample());
			if (user_email != null) {
				SiteSetting set = user_email.get(0);
				// 建立会话
				Session session = Session.getInstance(p);
				Message msg = new MimeMessage(session); // 建立信息
				msg.setFrom(new InternetAddress(set.getSmtpReply())); // 发件人
				// 解析数组邮箱
				String toList = getMailList(to);
				new InternetAddress();
				InternetAddress[] iaToList = InternetAddress.parse(toList);

				msg.setRecipients(Message.RecipientType.TO, iaToList); // 收件人

				msg.setSentDate(new Date()); // 发送日期
				msg.setSubject(title); // 主题
				// msg.setText(content); // 内容
				// 多内容邮件,文本,附件等
				Multipart mp = new MimeMultipart();
				MimeBodyPart mbpContent = new MimeBodyPart();
				mbpContent.setContent(content, "text/html;charset=gb2312");

				// 向邮件添加（Multipart代表正文）
				mp.addBodyPart(mbpContent);
				if (filenames != null && filenames.length > 0) {
					// 向邮件添加附件
					for (String filename : filenames) {
						if (StringUtils.isNotEmpty(filename)) {
							// 向Multipart添加附件
							MimeBodyPart mbpFile = new MimeBodyPart();
							FileDataSource fds = new FileDataSource(filename);
							mbpFile.setDataHandler(new DataHandler(fds));
							// 乱码
							// String filename= new
							// String(fds.getName().getBytes(),"ISO-8859-1");
							filename = filename.substring(filename.lastIndexOf("\\"));
							mbpFile.setFileName(filename);
							// 向MimeMessage添加（Multipart代表附件）
							mp.addBodyPart(mbpFile);
						}
					}
				}
				msg.setContent(mp);// 设置带附件的邮件
				// 邮件服务器进行验证
				tran = session.getTransport("smtp");
				// 发送者的邮箱密码（公司邮箱用于群发）
				tran.connect(set.getSmtpServer(), set.getSmtpUsername(), set.getSmtpPassword());
				tran.sendMessage(msg, msg.getAllRecipients()); // 发送
				System.out.println("邮件发送成功");
				return true;
			} else {
				System.out.println("邮件配置无效");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				tran.close();
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
			}
		}
		return false;
	}

	// 获取邮件所有地址
	private String getMailList(String[] mailArray) {

		StringBuffer toList = new StringBuffer();
		int length = mailArray.length;
		if (mailArray != null && length < 2) {
			toList.append(mailArray[0]);
		} else {
			for (int i = 0; i < length; i++) {
				toList.append(mailArray[i]);
				if (i != (length - 1)) {
					toList.append(",");
				}

			}
		}
		return toList.toString();

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
	 * 根据admin ID取得admin信息
	 *
	 * @param userId
	 * @return
	 */
	public Admin getAdminInfoByUserId(Integer userId) {
		if (userId != null) {
			AdminExample example = new AdminExample();
			example.createCriteria().andIdEqualTo(userId);
			List<Admin> usersInfoList = this.adminMapper.selectByExample(example);
			if (usersInfoList != null && usersInfoList.size() > 0) {
				return usersInfoList.get(0);
			}
		}
		return null;
	}

	/**
	 * 
	 * 根据条件查询优惠券使用详情
	 * 
	 * @author pcc
	 * @param detail
	 * @return
	 */
	@Override
	public CouponTenderDetailCustomize getCouponTenderDetailCustomize(Map<String, Object> paramMap) {
		return couponTenderCustomizeMapper.selectCouponTenderDetailCustomize(paramMap);
	}

	@Override
	public List<CouponRecoverCustomize> getCouponRecoverCustomize(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return couponTenderCustomizeMapper.getCouponRecoverCustomize(paramMap);
	}

	/**
	 * 
	 * 根据条件查询优惠券使用详情
	 * 
	 * @author pcc
	 * @param detail
	 * @return
	 */
	@Override
	public CouponTenderDetailCustomize getCouponTenderHtjDetailCustomize(Map<String, Object> paramMap) {
		return couponTenderCustomizeMapper.selectCouponTenderHtjDetailCustomize(paramMap);
	}

	@Override
	public List<CouponRecoverCustomize> getCouponRecoverHtjCustomize(Map<String, Object> paramMap) {
		// TODO Auto-generated method stub
		return couponTenderCustomizeMapper.getCouponRecoverHtjCustomize(paramMap);
	}

	/**
	 * 根据用户Id查询渠道账号权限信息
	 */
	@Override
	public AdminUtmReadPermissions selectAdminUtmReadPermissions(Integer userId) {
		AdminUtmReadPermissionsExample example = new AdminUtmReadPermissionsExample();
		AdminUtmReadPermissionsExample.Criteria cra = example.createCriteria();
		cra.andAdminUserIdEqualTo(userId);
		List<AdminUtmReadPermissions> list = this.adminUtmReadPermissionsMapper.selectByExample(example);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	/**
	 * 
	 * 获取用户注册时的渠道名称
	 * 
	 * @author hsy
	 * @param userId
	 * @return
	 */
	public String getChannelNameByUserId(Integer userId) {

		String channelName = channelCustomizeMapper.selectChannelNameByUserId(userId);

		return channelName;
	}

	/***
	 * 获取用户在银行的开户信息
	 */
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

	/**
	 * 根据电子账号查询用户在江西银行的可用余额
	 * 
	 * @param accountId
	 * @return
	 */
	@Override
	public BigDecimal getBankBalance(Integer userId, String accountId) {
		// 账户可用余额
		BigDecimal balance = BigDecimal.ZERO;
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 版本号
		bean.setTxCode(BankCallMethodConstant.TXCODE_BALANCE_QUERY);// 交易代码
		bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
		bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
		bean.setAccountId(accountId);// 电子账号
		bean.setLogOrderId(GetOrderIdUtils.getOrderId2(Integer.valueOf(userId)));// 订单号
		bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogClient(0);// 平台
		try {
			BankCallBean resultBean = BankCallUtils.callApiBg(bean);
			if (resultBean != null && BankCallStatusConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
				balance = new BigDecimal(resultBean.getAvailBal().replace(",", ""));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return balance;
	}

	/**
	 * 还款方式
	 * 
	 * @param nid
	 * @return
	 * @author Administrator
	 */
	@Override
	public List<BorrowStyle> borrowStyleList(String nid) {
		BorrowStyleExample example = new BorrowStyleExample();
		BorrowStyleExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(Integer.valueOf(CustomConstants.FLAG_NORMAL));
		if (StringUtils.isNotEmpty(nid)) {
			cra.andNidEqualTo(nid);
		}
		return this.borrowStyleMapper.selectByExample(example);
	}

	/**
	 * 项目类型
	 * 
	 * @return
	 * @author Administrator
	 */
	@Override
	public List<BorrowProjectType> borrowProjectTypeList(String projectTypeCd) {
		BorrowProjectTypeExample example = new BorrowProjectTypeExample();
		BorrowProjectTypeExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(CustomConstants.FLAG_NORMAL);
		if (StringUtils.isNotEmpty(projectTypeCd)) {
			cra.andBorrowProjectTypeEqualTo(projectTypeCd);
		}
		// 不查询融通宝相关
		cra.andBorrowNameNotEqualTo(CustomConstants.RTB);
		return this.borrowProjectTypeMapper.selectByExample(example);
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
	public BorrowWithBLOBs getBorrowByNid(String borrowNid) {
		BorrowExample example = new BorrowExample();
		BorrowExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<BorrowWithBLOBs> list = borrowMapper.selectByExampleWithBLOBs(example);
		if (list != null && !list.isEmpty()) {
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 根据银行卡号查询银行ID
	 * 
	 * @param cardNo
	 * @return
	 */
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
		String channel = BankCallConstant.CHANNEL_PC;
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
	 * 调用手续费分账接口
	 * @param userId
	 * @param accountId
	 * @param forAccountId
	 * @param txAmount
	 * @return
	 */
	@Override
	public BankCallBean feeShare(Integer userId, String accountId, String forAccountId, String txAmount) {
		BankCallBean bean = new BankCallBean();
		String channel = BankCallConstant.CHANNEL_PC;
		String orderDate = GetOrderIdUtils.getOrderDate();
		String txDate = GetOrderIdUtils.getTxDate();
		String txTime = GetOrderIdUtils.getTxTime();
		String seqNo = GetOrderIdUtils.getSeqNo(6);
		bean.setVersion(BankCallConstant.VERSION_10);// 版本号
		bean.setTxCode(BankCallConstant.TXCODE_FEE_SHARE);// 交易代码
		bean.setTxDate(txDate);
		bean.setTxTime(txTime);
		bean.setSeqNo(seqNo);
		bean.setChannel(channel);
		bean.setAccountId(accountId);// 电子账号
		bean.setCurrency(BankCallConstant.CURRENCY_TYPE_RMB);// 币种
		bean.setTxAmount(CustomUtil.formatAmount(txAmount)); // 分账金额
		bean.setForAccountId(forAccountId);// 对手电子账号
		bean.setDescription("手续费分账");
		bean.setLogUserId(GetOrderIdUtils.getOrderId2(userId));
		bean.setLogUserId(String.valueOf(userId));
		bean.setLogOrderDate(orderDate);
		bean.setLogRemark("手续费分账");
		return BankCallUtils.callApiBg(bean);
	}

	/**
	 * 取得有无limit外的检索条件Flag
	 * 
	 * @param user
	 * @return
	 */
	protected String getWhereFlag(Map<String, Object> user) {
		// 没有limit外的检索条件
		String whereFlag = "0";
		for (Map.Entry<String, Object> entry : user.entrySet()) {
			// key!=whereFlag,limitStart,limitEnd时
			if (!(entry.getKey().equals("whereFlag") || entry.getKey().equals("limitStart")
					|| entry.getKey().equals("limitEnd"))) {
				if (entry.getValue() != null) {
					// 有limit外的检索条件
					whereFlag = "1";
					break;
				}
			}
		}
		return whereFlag;
	}
	
	/**
	 * 资金来源
	 * @param instCode
	 * @return
	 * @author LiuBin
	 */
	@Override
	public List<HjhInstConfig> hjhInstConfigList(String instCode) {
		HjhInstConfigExample example = new HjhInstConfigExample();
		HjhInstConfigExample.Criteria cra = example.createCriteria();
//		cra.andDelFlgEqualTo(Integer.valueOf(CustomConstants.FLAG_NORMAL));
		if (StringUtils.isNotEmpty(instCode)) {
			cra.andInstCodeEqualTo(instCode);
		}
		cra.andDelFlgEqualTo(0);
		return this.hjhInstConfigMapper.selectByExample(example);
	}

//	/**
//	 * 根据资金来源取得产品类型
//	 * @param instCode
//	 * @return
//	 * @author LiuBin
//	 */
//	@Override
//	public List<HjhAssetBorrowType> hjhAssetBorrowTypeList(String instCode) {
//		HjhAssetBorrowTypeExample example = new HjhAssetBorrowTypeExample();
//		HjhAssetBorrowTypeExample.Criteria cra = example.createCriteria();
//		if (StringUtils.isEmpty(instCode)) {
//			return null;
//		}
//		cra.andInstCodeEqualTo(instCode);
//		return this.hjhAssetBorrowTypeMapper.selectByExample(example);
//	}
	
	/**
	 * 根据资金来源取得产品类型
	 * @param instCode
	 * @return
	 * @author LiuBin
	 */
	@Override
	public List<HjhAssetType> hjhAssetTypeList(String instCode) {
		HjhAssetTypeExample example = new HjhAssetTypeExample();
		HjhAssetTypeExample.Criteria cra = example.createCriteria();
		cra.andStatusEqualTo(0);
		if (StringUtils.isEmpty(instCode)) {
			return null;
		}
		cra.andInstCodeEqualTo(instCode);
		cra.andDelFlgEqualTo(0);
		return this.hjhAssetTypeMapper.selectByExample(example);
	}

    /**
     * 根据用户名获取用户
     * @param userName
     * @return
     */
    @Override
	public Users getUserByUserName(String userName){
    	if(StringUtils.isEmpty(userName)){
    		return null;
    	}
    	
    	UsersExample example = new UsersExample();
    	example.createCriteria().andUsernameEqualTo(userName);
    	List<Users> userList = usersMapper.selectByExample(example);
    	
    	if(userList != null && userList.size() == 1){
    		return userList.get(0);
    	}
    	
    	return null;
    }
    

	/**
	 * 获取用户的账户信息
	 *
	 * @param userId
	 * @return 获取用户的账户信息
	 */
	@Override
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
	 * 根据出借订单号查询用户出借协议记录
	 * @param nid
	 * @return
	 */
	@Override
	public TenderAgreement selectTenderAgreement(String nid) {
		TenderAgreementExample example = new TenderAgreementExample();
		TenderAgreementExample.Criteria cra = example.createCriteria();
		cra.andTenderNidEqualTo(nid);
		List<TenderAgreement> list = this.tenderAgreementMapper.selectByExample(example);
		if(list != null && list.size() > 0 ){
			return list.get(0);
		}
		return null;
	}

	/**
	 * PDF脱敏加下载MQ
	 * @param tenderAgreement
	 * @param borrowNid
	 * @param transType
	 * @param instCode
	 */
	public void updateSaveSignInfo(TenderAgreement tenderAgreement,String borrowNid, Integer transType, String instCode) {
		String download_url = tenderAgreement.getDownloadUrl();
		String savePath = null;
		String path = "/pdf_tem/";
		String ftpPath = null;
		if (FddGenerateContractConstant.PROTOCOL_TYPE_TENDER == transType || FddGenerateContractConstant.PROTOCOL_TYPE_CREDIT == transType
				|| FddGenerateContractConstant.FDD_TRANSTYPE_PLAN_CRIDET == transType
				|| FddGenerateContractConstant.FDD_TRANSTYPE_apply_CRIDET == transType
				|| FddGenerateContractConstant.FDD_TRANSTYPE_APPLY_PLAN_CRIDET == transType) {
			savePath = path + "pdf/" + borrowNid;
			ftpPath = "PDF/" + instCode + "/" + borrowNid + "/" + tenderAgreement.getTenderNid() + "/";
		} else if (FddGenerateContractConstant.PROTOCOL_TYPE_PLAN == transType) {
			savePath = path + "pdf/" + instCode;
			ftpPath = "PDF/" + instCode + "/" + tenderAgreement.getTenderNid() + "/";
		}
		//下载协议并脱敏
		FddDessenesitizationBean bean = new FddDessenesitizationBean();
		bean.setAgrementID(tenderAgreement.getId().toString());
		bean.setSavePath(savePath);
		bean.setTransType(transType.toString());
		bean.setFtpPath(ftpPath);
		bean.setDownloadUrl(download_url);
		bean.setOrdid(tenderAgreement.getTenderNid());
		rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ROUTINGKEY_DOWNDESSENESITIZATION_CONTRACT, JSONObject.toJSONString(bean));
	}
	
	public HjhAssetBorrowType selectAssetBorrowType(String borrowNid) {
		List<HjhAssetBorrowType> list = this.hjhAssetBorrowTypeCustomizeMapper.selectAssetBorrowType(borrowNid);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null; 
	}
    
    /**
     * 根据项目编号查询资产信息
     * 
     * @param borrowNid
     * @return
     * @author PC-LIUSHOUYI
     */
    @Override
    public HjhPlanAsset getHjhPlanAsset(String borrowNid) {
		HjhPlanAssetExample example = new HjhPlanAssetExample();
		HjhPlanAssetExample.Criteria crt = example.createCriteria();
		crt.andBorrowNidEqualTo(borrowNid);
        
        List<HjhPlanAsset> list = this.hjhPlanAssetMapper.selectByExample(example);
        
        if(list != null && list.size() > 0){
        	return list.get(0);
        } else {
        	return null;
        }
    }
		
}
