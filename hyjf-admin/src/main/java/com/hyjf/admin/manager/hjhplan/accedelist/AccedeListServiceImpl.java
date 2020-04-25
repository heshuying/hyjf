package com.hyjf.admin.manager.hjhplan.accedelist;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.bank.service.fdd.fddgeneratecontract.FddGenerateContractService;
import com.hyjf.common.file.FileUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.pdf.PdfGenerator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.StringPool;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.HjhAccedeExample;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhPlanExample;
import com.hyjf.mybatis.model.auto.TenderAgreement;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.admin.AdminPlanAccedeListCustomize;
import com.hyjf.mybatis.model.customize.web.hjh.UserHjhInvistDetailCustomize;

import cn.emay.slf4j.Logger;
import cn.emay.slf4j.LoggerFactory;
/**
 * 汇计划加入明细Service实现类
 * 
 * @ClassName AccedeListServiceImpl
 * @author LIBIN
 * @date 2017年8月16日 上午9:48:22
 */
@Service
public class AccedeListServiceImpl extends BaseServiceImpl implements AccedeListService {
	
	/** 用户名 */
	private static final String VAL_NAME = "val_name";
	/** 性别 */
	private static final String VAL_SEX = "val_sex";
	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;
    @Autowired
    private FddGenerateContractService fddGenerateContractService;
	
	Logger _log = LoggerFactory.getLogger(this.getClass());
	
    /** 用户ID */
    private static final String VAL_USERID = "userId";

	/**
	 * 检索加入明细的件数
	 * 
	 * @Title countAccedeRecord
	 * @param form
	 * @return
	 */
	@Override
	public int countAccedeRecord(AccedeListBean form) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 加入订单号
		if (StringUtils.isNotEmpty(form.getAccedeOrderIdSrch())) {
			param.put("accedeOrderIdSrch", form.getAccedeOrderIdSrch());
		}
		// 计划编号
		if (StringUtils.isNotEmpty(form.getDebtPlanNidSrch())) {
			param.put("debtPlanNidSrch", form.getDebtPlanNidSrch());
		}
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			param.put("userNameSrch", form.getUserNameSrch());
		}
		// 锁定期
		if (StringUtils.isNotEmpty(form.getDebtLockPeriodSrch())) {
			/*param.put("debtLockPeriodSrch", Integer.parseInt(form.getDebtLockPeriodSrch()));*/
			param.put("debtLockPeriodSrch", form.getDebtLockPeriodSrch().trim());
		}
		// 推荐人
		if (StringUtils.isNotEmpty(form.getRefereeNameSrch())) {
			param.put("refereeNameSrch", form.getRefereeNameSrch());
		}
		// 订单状态
		if (StringUtils.isNotEmpty(form.getOrderStatus())) {
			param.put("orderStatus", form.getOrderStatus());//原 userAttributeSrch
		}
		// 操作平台
		if (StringUtils.isNotEmpty(form.getPlatformSrch())) {
			param.put("platformSrch", form.getPlatformSrch());
		}
		// 加入开始时间(推送开始时间)
		if (StringUtils.isNotEmpty(form.getSearchStartDate())) {
			param.put("searchStartDate", form.getSearchStartDate());
		}
		// 加入结束时间(推送结束时间)
		if (StringUtils.isNotEmpty(form.getSearchEndDate())) {
			param.put("searchEndDate", form.getSearchEndDate());
		}
		// 加入开始时间(计息开始时间)
		if (StringUtils.isNotEmpty(form.getCountInterestTimeStartDate())) {
			param.put("countInterestTimeStartDate", form.getCountInterestTimeStartDate());
		}
		// 加入结束时间(计息结束时间)
		if (StringUtils.isNotEmpty(form.getCountInterestTimeEndDate())) {
			param.put("countInterestTimeEndDate", form.getCountInterestTimeEndDate());
		}
		// 匹配期查询传入
		if (StringUtils.isNotEmpty(form.getMatchDatesSrch())) {
			param.put("matchDatesSrch", form.getMatchDatesSrch());
		}
		// 出借笔数查询传入
		if (StringUtils.isNotEmpty(form.getInvestCountsSrch())) {
			param.put("investCountsSrch", form.getInvestCountsSrch());
		}

		//预计开始退出时间
		if (StringUtils.isNotEmpty(form.getEndDateStartSrch())) {
			param.put("endDateStartSrch", form.getEndDateStartSrch());
		}
		if (StringUtils.isNotEmpty(form.getEndDateEndSrch())) {
			param.put("endDateEndSrch", form.getEndDateEndSrch());
		}
		//实际退出时间
		if (StringUtils.isNotEmpty(form.getAcctualPaymentTimeStartSrch())) {
			param.put("acctualPaymentTimeStartSrch", form.getAcctualPaymentTimeStartSrch());
		}
		if (StringUtils.isNotEmpty(form.getAcctualPaymentTimeEndSrch())) {
			param.put("acctualPaymentTimeEndSrch", form.getAcctualPaymentTimeEndSrch());
		}

		return this.adminPlanAccedeListCustomizeMapper.countAccedeRecord(param);
	}

	/**
	 * 检索加入明细列表
	 * 
	 * @Title selectAccedeRecordList
	 * @param form
	 * @return
	 */
	@Override
	public List<AdminPlanAccedeListCustomize> selectAccedeRecordList(AccedeListBean form) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 加入订单号
		if (StringUtils.isNotEmpty(form.getAccedeOrderIdSrch())) {
			param.put("accedeOrderIdSrch", form.getAccedeOrderIdSrch());
		}
		// 计划编号
		if (StringUtils.isNotEmpty(form.getDebtPlanNidSrch())) {
			param.put("debtPlanNidSrch", form.getDebtPlanNidSrch());
		}
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			param.put("userNameSrch", form.getUserNameSrch());
		}
		// 锁定期
		if (StringUtils.isNotEmpty(form.getDebtLockPeriodSrch())) {
			/*param.put("debtLockPeriodSrch", Integer.parseInt(form.getDebtLockPeriodSrch()));*/
			param.put("debtLockPeriodSrch", form.getDebtLockPeriodSrch().trim());
		}
		// 推荐人
		if (StringUtils.isNotEmpty(form.getRefereeNameSrch())) {
			param.put("refereeNameSrch", form.getRefereeNameSrch());
		}
		// 订单状态
		if (StringUtils.isNotEmpty(form.getOrderStatus())) {
			param.put("orderStatus", form.getOrderStatus());//原 userAttributeSrch
		}
		// 操作平台
		if (StringUtils.isNotEmpty(form.getPlatformSrch())) {
			param.put("platformSrch", form.getPlatformSrch());
		}
		// 加入开始时间(推送开始时间)
		if (StringUtils.isNotEmpty(form.getSearchStartDate())) {
			param.put("searchStartDate", form.getSearchStartDate());
		}
		// 加入结束时间(推送结束时间)
		if (StringUtils.isNotEmpty(form.getSearchEndDate())) {
			param.put("searchEndDate", form.getSearchEndDate());
		}
		// 加入开始时间(计息开始时间)
		if (StringUtils.isNotEmpty(form.getCountInterestTimeStartDate())) {
			param.put("countInterestTimeStartDate", form.getCountInterestTimeStartDate());
		}
		// 加入结束时间(计息结束时间)
		if (StringUtils.isNotEmpty(form.getCountInterestTimeEndDate())) {
			param.put("countInterestTimeEndDate", form.getCountInterestTimeEndDate());
		}
		// 匹配期查询传入
		if (StringUtils.isNotEmpty(form.getMatchDatesSrch())) {
			param.put("matchDatesSrch", form.getMatchDatesSrch());
		}
		// 出借笔数查询传入
		if (StringUtils.isNotEmpty(form.getInvestCountsSrch())) {
			param.put("investCountsSrch", form.getInvestCountsSrch());
		}
		//预计开始退出时间
		if (StringUtils.isNotEmpty(form.getEndDateStartSrch())) {
			param.put("endDateStartSrch", form.getEndDateStartSrch());
		}
		if (StringUtils.isNotEmpty(form.getEndDateEndSrch())) {
			param.put("endDateEndSrch", form.getEndDateEndSrch());
		}
		//实际退出时间
		if (StringUtils.isNotEmpty(form.getAcctualPaymentTimeStartSrch())) {
			param.put("acctualPaymentTimeStartSrch", form.getAcctualPaymentTimeStartSrch());
		}
		if (StringUtils.isNotEmpty(form.getAcctualPaymentTimeEndSrch())) {
			param.put("acctualPaymentTimeEndSrch", form.getAcctualPaymentTimeEndSrch());
		}



		param.put("limitStart", form.getLimitStart());
		param.put("limitEnd", form.getLimitEnd());
		return this.adminPlanAccedeListCustomizeMapper.selectAccedeRecordListExport(param);
	}
	
	/**
	 * 发送协议处理
	 * @Title resendMessageAction
	 * @param form
	 * @return
	 */
	@Override
	public String resendMessageAction(String userId, String planOrderId, String debtPlanNid,String sendEmail) {
		try {
			// 向每个出借人发送邮件
			if (Validator.isNotNull(userId) && NumberUtils.isNumber(userId)) {
				Users users = getUsersByUserId(Integer.valueOf(userId));
				if (users == null) {
					return "用户不存在";
				}
				// 通过 userId 获取用户邮箱
				String email = users.getEmail();
				if(StringUtils.isNotBlank(sendEmail)){
					// 当用户使用入力发送协议时
					email=sendEmail;
				}
				if (Validator.isNull(email)) {
					return "用户邮箱不存在";
				}
				Map<String, String> msg = new HashMap<String, String>();
				msg.put(VAL_NAME, users.getUsername());
				// 获取用户信息
				UsersInfo usersInfo = this.getUsersInfoByUserId(Integer.valueOf(userId));
				if (Validator.isNotNull(usersInfo) && Validator.isNotNull(usersInfo.getSex())) {
					if (usersInfo.getSex() == 2) {
						msg.put(VAL_SEX, "女士");
					} else {
						msg.put(VAL_SEX, "先生");
					}
				}
				// 文件名称
				String fileName = debtPlanNid + "_" + planOrderId + ".pdf";
				// 生成临时文件夹目录：/hyjfdata/data/upfiles/filetemp/pdfPlanAccedes_/
				String filePath = PropUtils.getSystem(CustomConstants.HYJF_MAKEPDF_TEMPPATH) + "PlanAccedes_" + GetDate.getMillis() + StringPool.FORWARD_SLASH;
				// 根据唯一订单号+计划编号查询计划的加入记录
				Map<String, Object> param = new HashMap<String, Object>();
				// 加入订单号
				if (StringUtils.isNotEmpty(planOrderId)) {
					param.put("accedeOrderIdSrch", planOrderId);
				}
				// 计划编号
				if (StringUtils.isNotEmpty(debtPlanNid)) {
					param.put("debtPlanNidSrch", debtPlanNid);
				}
				List<AdminPlanAccedeListCustomize> recordList = adminPlanAccedeListCustomizeMapper.selectAccedeRecordList(param);
				// 该加入记录存在的前提下
				if (recordList != null && recordList.size() == 1) {
					// PDF将要录入信息
					Map<String, Object> contents = new HashMap<String, Object>();
					//基本信息
					Map<String ,Object> params=new HashMap<String ,Object>();
					params.put("accedeOrderId", planOrderId);
					params.put("userId", userId);
					//System.out.println("*****************************username:"+users.getUsername());
					UsersInfo userInfo = this.getUsersInfoByUserId(Integer.valueOf(userId));
					// 1.放入用户信息
					contents.put("userInfo", userInfo);
					contents.put("username", users.getUsername());
					UserHjhInvistDetailCustomize userHjhInvistDetailCustomize = hjhPlanCustomizeMapper.selectUserHjhInvistDetail(params);
					contents.put("userHjhInvistDetail", userHjhInvistDetailCustomize);
					// 依据模板生成内容
					String pdfUrl = PdfGenerator.generateLocal(fileName, CustomConstants.NEW_HJH_INVEST_CONTRACT, contents);
					
					if (StringUtils.isNotEmpty(pdfUrl)) {
						File path = new File(filePath);
						if (!path.exists()) {
							path.mkdirs();
						}
						FileUtil.getRemoteFile(pdfUrl.substring(0, pdfUrl.length() - 1), filePath + fileName);
					}
					String[] emails = { email };
					
					// 将生成的PDF嵌入邮件模板中
					// 需要产品提供邮件模板????---- CustomConstants.EMAILPARAM_TPL_LOANS
					// mod by nxl 修改计划->智投服务
					/*MailMessage message = new MailMessage(null, msg, "汇盈金服互联网金融服务平台汇计划出借服务协议",null, new String[] { filePath + fileName }, emails, CustomConstants.HJD_JOIN_AGREEMENT, MessageDefine.MAILSENDFORMAILINGADDRESS);*/
					MailMessage message = new MailMessage(null, msg, "汇盈金服互联网金融服务平台智投服务协议",null, new String[] { filePath + fileName }, emails, CustomConstants.HJD_JOIN_AGREEMENT, MessageDefine.MAILSENDFORMAILINGADDRESS);
					mailMessageProcesser.gather(message);
					// 邮件发送成功后修改DB发邮件的状态
					HjhAccedeExample HjhAccedeExample = new HjhAccedeExample();
					HjhAccedeExample.createCriteria().andUserIdEqualTo(Integer.valueOf(userId));
					if (Validator.isNotNull(planOrderId)) {
						HjhAccedeExample.createCriteria().andAccedeOrderIdEqualTo(planOrderId);
					}
					if (Validator.isNotNull(debtPlanNid)) {
						HjhAccedeExample.createCriteria().andPlanNidEqualTo(debtPlanNid);	
					}
					HjhAccede hjhAccede = new HjhAccede();
					hjhAccede.setSendStatus(1);
					this.hjhAccedeMapper.updateByExampleSelective(hjhAccede, HjhAccedeExample);
					return null;
				}
			} else {
				System.out.println("计划信息异常（0条或者大于1条信息）,下载汇计划出借计划服务协议协议PDF失败。");
				return "计划信息异常（0条或者大于1条信息）,下载汇计划出借计划服务协议协议PDF失败。";
			}
		} catch (Exception e) {
			LogUtil.errorLog(AccedeListServiceImpl.class.getName(), "sendMail", e);
		}
		return "系统异常";
	}

	
	/**
	 * 根据用户ID取得用户信息
	 *
	 * @param userId
	 * @return
	 */
	@Override
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
	 * 检索加入明细列表
	 * 
	 * @Title selectAccedeRecordList
	 * @param form
	 * @return
	 */
	@Override
	public List<AdminPlanAccedeListCustomize> selectAccedeRecordListExport(AccedeListBean form) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 加入订单号
		if (StringUtils.isNotEmpty(form.getAccedeOrderIdSrch())) {
			param.put("accedeOrderIdSrch", form.getAccedeOrderIdSrch());
		}
		// 计划编号
		if (StringUtils.isNotEmpty(form.getDebtPlanNidSrch())) {
			param.put("debtPlanNidSrch", form.getDebtPlanNidSrch());
		}
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			param.put("userNameSrch", form.getUserNameSrch());
		}
		// 推荐人
		if (StringUtils.isNotEmpty(form.getRefereeNameSrch())) {
			param.put("refereeNameSrch", form.getRefereeNameSrch());
		}
		// 订单状态
		if (StringUtils.isNotEmpty(form.getOrderStatus())) {
			param.put("orderStatus", form.getOrderStatus());//原 userAttributeSrch
		}
		// 操作平台
		if (StringUtils.isNotEmpty(form.getPlatformSrch())) {
			param.put("platformSrch", form.getPlatformSrch());
		}
		// 加入开始时间(推送开始时间)
		if (StringUtils.isNotEmpty(form.getSearchStartDate())) {
			param.put("searchStartDate", form.getSearchStartDate());
		}
		// 加入结束时间(推送结束时间)
		if (StringUtils.isNotEmpty(form.getSearchEndDate())) {
			param.put("searchEndDate", form.getSearchEndDate());
		}
		param.put("limitStart", form.getLimitStart());
		param.put("limitEnd", form.getLimitEnd());
		return this.adminPlanAccedeListCustomizeMapper.selectAccedeRecordListExport(param);
	}


	/**
	 * 执行前每个方法前需要添加BusinessDesc描述
	 * @param param
	 * @return
	 * @author PC-LIUSHOUYI
	 */
		
	@Override
	public AdminPlanAccedeListCustomize sumAccedeRecord(AccedeListBean form) {
		Map<String, Object> param = new HashMap<String, Object>();
		// 加入订单号
		if (StringUtils.isNotEmpty(form.getAccedeOrderIdSrch())) {
			param.put("accedeOrderIdSrch", form.getAccedeOrderIdSrch());
		}
		// 计划编号
		if (StringUtils.isNotEmpty(form.getDebtPlanNidSrch())) {
			param.put("debtPlanNidSrch", form.getDebtPlanNidSrch());
		}
		// 用户名
		if (StringUtils.isNotEmpty(form.getUserNameSrch())) {
			param.put("userNameSrch", form.getUserNameSrch());
		}
		// 锁定期
		if (StringUtils.isNotEmpty(form.getDebtLockPeriodSrch())) {
			param.put("debtLockPeriodSrch", Integer.parseInt(form.getDebtLockPeriodSrch()));
		}
		// 推荐人
		if (StringUtils.isNotEmpty(form.getRefereeNameSrch())) {
			param.put("refereeNameSrch", form.getRefereeNameSrch());
		}
		// 订单状态
		if (StringUtils.isNotEmpty(form.getOrderStatus())) {
			param.put("orderStatus", form.getOrderStatus());//原 userAttributeSrch
		}
		// 操作平台
		if (StringUtils.isNotEmpty(form.getPlatformSrch())) {
			param.put("platformSrch", form.getPlatformSrch());
		}
		// 加入开始时间(推送开始时间)
		if (StringUtils.isNotEmpty(form.getSearchStartDate())) {
			param.put("searchStartDate", form.getSearchStartDate());
		}
		// 加入结束时间(推送结束时间)
		if (StringUtils.isNotEmpty(form.getSearchEndDate())) {
			param.put("searchEndDate", form.getSearchEndDate());
		}
		// 加入开始时间(计息开始时间)
		if (StringUtils.isNotEmpty(form.getCountInterestTimeStartDate())) {
			param.put("countInterestTimeStartDate", form.getCountInterestTimeStartDate());
		}
		// 加入结束时间(计息结束时间)
		if (StringUtils.isNotEmpty(form.getCountInterestTimeEndDate())) {
			param.put("countInterestTimeEndDate", form.getCountInterestTimeEndDate());
		}
		// 匹配期查询传入
		if (StringUtils.isNotEmpty(form.getMatchDatesSrch())) {
			param.put("matchDatesSrch", form.getMatchDatesSrch());
		}
		// 出借笔数查询传入
		if (StringUtils.isNotEmpty(form.getInvestCountsSrch())) {
			param.put("investCountsSrch", form.getInvestCountsSrch());
		}
		//预计开始退出时间
		if (StringUtils.isNotEmpty(form.getEndDateStartSrch())) {
			param.put("endDateStartSrch", form.getEndDateStartSrch());
		}
		if (StringUtils.isNotEmpty(form.getEndDateEndSrch())) {
			param.put("endDateEndSrch", form.getEndDateEndSrch());
		}
		//实际退出时间
		if (StringUtils.isNotEmpty(form.getAcctualPaymentTimeStartSrch())) {
			param.put("acctualPaymentTimeStartSrch", form.getAcctualPaymentTimeStartSrch());
		}
		if (StringUtils.isNotEmpty(form.getAcctualPaymentTimeEndSrch())) {
			param.put("acctualPaymentTimeEndSrch", form.getAcctualPaymentTimeEndSrch());
		}
		return this.adminPlanAccedeListCustomizeMapper.sumAccedeRecord(param);
	}

	/**
	 * 根据用户ID,计划加入订单号查询用户加入记录
	 * @param userId
	 * @param planOrderId
	 * @return
	 */
	@Override
	public HjhAccede selectAccedeRecord(String userId, String planOrderId) {
		HjhAccedeExample example = new HjhAccedeExample();
		HjhAccedeExample.Criteria cra = example.createCriteria();
		cra.andAccedeOrderIdEqualTo(planOrderId);
		cra.andUserIdEqualTo(Integer.parseInt(userId));
		List<HjhAccede> list = this.hjhAccedeMapper.selectByExample(example);
		if (list != null && list.size() >0){
			return  list.get(0);
		}
		return null;
	}

	@Override
	public HjhAccede searchHjhAccedeByOrderId(String planOrderId) {
		HjhAccedeExample example = new HjhAccedeExample();
		HjhAccedeExample.Criteria cra = example.createCriteria();
		cra.andAccedeOrderIdEqualTo(planOrderId);
		List<HjhAccede> list = this.hjhAccedeMapper.selectByExample(example);
		if (list != null && list.size() >0){
			return  list.get(0);
		}
		return null;
	}

	@Override
	public HjhPlan searchHjhPlanByPlanNid(String planNid) {
		HjhPlanExample example = new HjhPlanExample();
		HjhPlanExample.Criteria cra = example.createCriteria();
		cra.andPlanNidEqualTo(planNid);
		List<HjhPlan> list = this.hjhPlanMapper.selectByExample(example);
		if (list != null && list.size() >0){
			return  list.get(0);
		}
		return null;
	}

	@Override
	public String sendFddHjhServiceAgrm(String userid, String planOrderId, String debtPlanNid, String sendEmail) {
		try {
			// 参考FddDownPDFandDessensitizationMessageHandle.sendPlanMail
			// 向每个出借人发送邮件
			if (Validator.isNotNull(userid) && NumberUtils.isNumber(userid)) {
				Users users = getUsersByUserId(Integer.valueOf(userid));
				if (users == null) {
					return "用户不存在";
				}
				// 通过 userId 获取用户邮箱
				String email = users.getEmail();
				if(StringUtils.isNotBlank(sendEmail)){
					// 当用户使用入力发送协议时
					email=sendEmail;
				}
				if (Validator.isNull(email)) {
					return "用户邮箱不存在";
				}
				Map<String, String> msg = new HashMap<String, String>();
				msg.put(VAL_NAME, users.getUsername());
				// 获取用户信息
				UsersInfo usersInfo = this.getUsersInfoByUserId(Integer.valueOf(userid));
				if (Validator.isNotNull(usersInfo) && Validator.isNotNull(usersInfo.getSex())) {
					if (usersInfo.getSex() == 2) {
						msg.put(VAL_SEX, "女士");
					} else {
						msg.put(VAL_SEX, "先生");
					}
				}
				msg.put(VAL_USERID, userid);
				msg.put(VAL_NAME, users.getUsername());
				//String fileName = hjhAccede.getAccedeOrderId()+".pdf";
				String fileName = planOrderId+".pdf";
				String filePath = "/pdf_tem/pdf/" + debtPlanNid;
				TenderAgreement tenderAgreement = new TenderAgreement();
				List<TenderAgreement> tenderAgreementsNid= fddGenerateContractService.selectByExample(planOrderId);
				if(tenderAgreementsNid!=null && tenderAgreementsNid.size()>0){
					tenderAgreement = tenderAgreementsNid.get(0);
					 _log.info("sendMail", "***************************下载法大大协议--出借orderId:"+planOrderId);
					 _log.info("sendMail", "***************************下载法大大协议--出借pdfUrl:"+tenderAgreement.getImgUrl());
				        if(tenderAgreement!=null){
	                        String pdfUrl = tenderAgreement.getDownloadUrl();
	                        if(StringUtils.isNotBlank(pdfUrl)){
	                            _log.info("sendMail", "***************************下载法大大协议--pdfUrl:"+pdfUrl);
	                            //FileUtil.getRemoteFile(pdfUrl, filePath + fileName);
	                            FileUtil.downLoadFromUrl(pdfUrl,fileName,filePath);
	                        }
	                    }	 
				}
				String[] emails = { email };
				 _log.info("sendMail***************************下载法大大协议--出借filePath:"+filePath + fileName);
				 // mod by 智投服务 修改计划->智投服务
                /*MailMessage message = new MailMessage(Integer.valueOf(userid), msg, "汇计划出借服务协议", null, new String[] { filePath + "/" + fileName }, emails, CustomConstants.EMAITPL_EMAIL_LOCK_REPAY,
                        MessageDefine.MAILSENDFORMAILINGADDRESS);*/
				MailMessage message = new MailMessage(Integer.valueOf(userid), msg, "智投服务协议", null, new String[] { filePath + "/" + fileName }, emails, CustomConstants.EMAITPL_EMAIL_LOCK_REPAY,
						MessageDefine.MAILSENDFORMAILINGADDRESS);
                int f =  mailMessageProcesser.gather(message);
                _log.info("计划订单状态由出借成功变为锁定中，发送此邮件提醒用户出借--------------------------结束："+f);
				// 邮件发送成功后修改DB发邮件的状态
				HjhAccedeExample HjhAccedeExample = new HjhAccedeExample();
				HjhAccedeExample.createCriteria().andUserIdEqualTo(Integer.valueOf(userid));
				if (Validator.isNotNull(planOrderId)) {
					HjhAccedeExample.createCriteria().andAccedeOrderIdEqualTo(planOrderId);
				}
				if (Validator.isNotNull(debtPlanNid)) {
					HjhAccedeExample.createCriteria().andPlanNidEqualTo(debtPlanNid);	
				}
				HjhAccede hjhAccede = new HjhAccede();
				hjhAccede.setSendStatus(1);
				this.hjhAccedeMapper.updateByExampleSelective(hjhAccede, HjhAccedeExample);
				// 此处发完后，不返回任何消息即发送成功
				return null; 
			} else {
				return "系统异常";
			}
		} catch (Exception e) {
			LogUtil.errorLog(AccedeListServiceImpl.class.getName(), "sendMail", e);
		}
		return "系统异常";
	}
}
