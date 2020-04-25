/**
 * Description:汇计划服务器
 * Copyright: Copyright (HYJF Corporation) 2017
 * Company: HYJF Corporation
 * @author: LIBIN
 * @version: 1.0
 */
package com.hyjf.app.hjhplan;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.app.BaseController;
import com.hyjf.app.hjhplan.result.HjhPlanAccedeResultBean;
import com.hyjf.app.hjhplan.result.HjhPlanBorrowResultBean;
import com.hyjf.app.hjhplan.result.HjhPlanResultBean;
import com.hyjf.app.util.SecretUtil;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanAccedeCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanBorrowCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanDetailCustomize;

@RestController
@RequestMapping(value = HjhPlanDefine.REQUEST_MAPPING)
public class HjhPlanController extends BaseController {
	private Logger logger = LoggerFactory.getLogger(HjhPlanController.class);

	/** 加入条件 */
	// mod by nxl 计划详情修改加入条件
//	private final String PLAN_ADD_CONDITION = "{0}元起，以{1}元的倍数递增";
	private final String PLAN_ADD_CONDITION = "加入金额{0}元起，且以{1}元的整数倍递增";

	/** 计息时间 */
	private final String PLAN_ON_ACCRUAL = "计划进入锁定期后开始计息";

	/** 汇计划类型简称 */
	private final String PLAN_TYPE_NAME = "HJH";

	@Autowired
	private HjhPlanService hjhPlanService;
	
	/**
	 * 查询汇计划详细信息
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping(value = HjhPlanDefine.HJH_PLAN_DETAIL_ACTION)
	public HjhPlanResultBean searchHjhPlanDetail(HttpServletRequest request, @PathVariable String planId) {
		LogUtil.startLog(HjhPlanController.class.getName(), HjhPlanDefine.HJH_PLAN_DETAIL_ACTION);

		logger.info("入参planId:{}", planId);
		HjhPlanResultBean result = new HjhPlanResultBean();

		// 根据计划编号获取相应的计划详情信息
		DebtPlanDetailCustomize customize = hjhPlanService.selectDebtPlanDetail(planId);
		if (customize == null) {
			logger.error("传入计划id无对应计划,planNid is {}...", planId);
			result.setStatus(HjhPlanResultBean.FAIL);
			result.setStatusDesc(HjhPlanResultBean.FAIL_MSG);
			return result;
		}

		logger.info("customize:{}", JSONObject.toJSONString(customize));
		// 计划基本信息
		this.setPlanInfo(result, customize);
		// 用户的用户验证
		this.setUserValidationInfo(result, request);

		LogUtil.endLog(HjhPlanController.class.getName(), HjhPlanDefine.HJH_PLAN_DETAIL_ACTION);
		return result;
	}

	/**
	 * 查询相应的汇计划的标的组成
	 *
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping(value = HjhPlanDefine.HJH_PLAN_BORROW_ACTION, produces = "application/json; charset=utf-8")
	public HjhPlanBorrowResultBean searchHjhPlanBorrow(
			@RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @PathVariable String planId) {
		LogUtil.startLog(HjhPlanController.class.getName(), HjhPlanDefine.HJH_PLAN_BORROW_ACTION);
		HjhPlanBorrowResultBean result = new HjhPlanBorrowResultBean();
		this.searchHjhPlanBorrow(result, planId, currentPage, pageSize);
		LogUtil.endLog(HjhPlanController.class.getName(), HjhPlanDefine.HJH_PLAN_BORROW_ACTION);
		return result;
	}

	/**
	 * 查询相应的汇计划的加入记录
	 * 
	 * @param currentPage
	 * @param pageSize
	 * @param planId
	 * @return
	 */
	@RequestMapping(value = HjhPlanDefine.HJH_PLAN_ACCEDE_ACTION, produces = "application/json; charset=utf-8")
	public HjhPlanAccedeResultBean searchHjhPlanAccede(
			@RequestParam(value = "currentPage", defaultValue = "1") int currentPage,
			@RequestParam(value = "pageSize", defaultValue = "10") int pageSize, @PathVariable String planId) {
		LogUtil.startLog(HjhPlanController.class.getName(), HjhPlanDefine.HJH_PLAN_ACCEDE_ACTION);
		HjhPlanAccedeResultBean result = new HjhPlanAccedeResultBean();
		this.getHjhPlanAccede(result, planId, currentPage, pageSize);
		LogUtil.endLog(HjhPlanController.class.getName(), HjhPlanDefine.HJH_PLAN_ACCEDE_ACTION);
		return result;
	}

	/**
	 * 返回汇计划基本信息
	 * 
	 * @param result
	 * @param customize
	 */
	private void setPlanInfo(HjhPlanResultBean result, DebtPlanDetailCustomize customize) {
		HjhPlanResultBean.ProjectInfo projectInfo = result.getProjectInfo();
		projectInfo.setType(PLAN_TYPE_NAME);
		// 计划开放额度
		String openAccount = customize.getAvailableInvestAccount();
		projectInfo.setAccount(CommonUtils.formatAmount(openAccount));

		//计划如果关闭，开发额度为0.00
		if(!"1".equals(customize.getPlanStatus())){
			projectInfo.setAccount("0.00");
		}

		boolean isOpenAccountMoreZero = new BigDecimal(openAccount).compareTo(BigDecimal.ZERO) > 0;
		if ("1".equals(customize.getPlanStatus()) && isOpenAccountMoreZero) {
			// 立即加入
			projectInfo.setStatus("1");
		} else {
			// 稍后开启
			projectInfo.setStatus("0");
			if(!isOpenAccountMoreZero){
			    projectInfo.setAccount("0.00");
			}
		}

		projectInfo.setPlanApr(customize.getPlanApr());
		projectInfo.setPlanPeriod(customize.getPlanPeriod());
		projectInfo.setPlanPeriodUnit(CommonUtils.getPeriodUnitByRepayStyle(customize.getBorrowStyle()));

		// 计划的加入人次
		projectInfo.setPlanPersonTime(String.valueOf(countPlanPersonTime(customize.getPlanNid())));

		// 项目进度 本期预留，填写固定值
		projectInfo.setPlanProgressStatus("4");
		projectInfo.setPlanName(customize.getPlanName());
		// 计息时间
		projectInfo.setOnAccrual(PLAN_ON_ACCRUAL);
		projectInfo.setRepayStyle(customize.getBorrowStyleName());

		HjhPlanResultBean.ProjectDetail projectDetail = result.getProjectDetail();
		projectDetail.setAddCondition(MessageFormat.format(PLAN_ADD_CONDITION, customize.getDebtMinInvestment(),
				customize.getDebtInvestmentIncrement()));

		// 数据库保存的p标签取出，避免影响前端排版
		String planInfo = customize.getPlanConcept();
		if (!StringUtils.isEmpty(planInfo)) {
			// jsoup 解析html富文本
			Document doc = Jsoup.parseBodyFragment(planInfo);
			// 获取P标签里面的值
			if (doc != null) {
				planInfo = doc.getElementsByTag("p").text();
			}
		}
		projectDetail.setPlanInfo(planInfo);
	}

    /**
     * 金额格式化
     * @param s 金额
     * @param len 小数位数
     * @return 格式后的金额
     */
    public static String insertComma(String s, int len) {
        if (s == null || s.length() < 1) {
            return "";
        }
        NumberFormat formater = null;
        double num = Double.parseDouble(s);
        if (len == 0) {
            formater = new DecimalFormat("###,###");

        } else {
            StringBuffer buff = new StringBuffer();
            buff.append("###,###.");
            for (int i = 0; i < len; i++) {
                buff.append("#");
            }
            formater = new DecimalFormat(buff.toString());
        }
        return formater.format(num);
    }

    /**
	 * 获取特定计划加入人次
	 * 
	 * @param planNid
	 * @return
	 */
	private Integer countPlanPersonTime(String planNid) {
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("planNid", planNid);
		return this.hjhPlanService.countPlanAccedeRecordTotal(params);
	}

	/**
	 * 检查当前访问用户是否登录、是否开户、是否设置交易密码、是否允许使用、是否完成风险测评、是否授权
	 * 
	 * @param result
	 * @param request
	 */
	private void setUserValidationInfo(HjhPlanResultBean result, HttpServletRequest request) {
		String token = request.getParameter("token");
		String sign = request.getParameter("sign");
		HjhPlanResultBean.UserValidation userValidation = result.getUserValidation();
		// 1. 检查登录状态
		if (StringUtils.isBlank(token)) {
			userValidation.setLogined(Boolean.FALSE);
		} else {
			userValidation.setLogined(Boolean.TRUE);
			
			Integer userId = SecretUtil.getUserId(sign);
			Users user = this.hjhPlanService.searchLoginUser(userId);
			// 2. 用户是否被禁用
			userValidation.setAllowed(user.getStatus() == 0 ? Boolean.TRUE : Boolean.FALSE);
			// 3. 是否开户
			userValidation.setOpened(user.getBankOpenAccount() == 0 ? Boolean.FALSE : Boolean.TRUE);
			// 4. 是否设置过交易密码
			userValidation.setSetPassword(user.getIsSetPassword() == 1 ? Boolean.TRUE : Boolean.FALSE);
			// 检查用户角色是否能出借  合规接口改造之后需要判断
            UsersInfo userInfo = hjhPlanService.getUsersInfoByUserId(userId);
            // 返回前端角色
            userValidation.setRoleId(userInfo.getRoleId());
            if (null != userInfo) {
                userValidation.setIsAllowedTender(Boolean.TRUE);
                if (userInfo.getRoleId() == 3) {// 担保机构用户
                    userValidation.setIsAllowedTender(Boolean.FALSE);
                }
                if("true".equals(PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN))){
                    if (userInfo.getRoleId() == 2) {// 借款人不能出借
                        userValidation.setIsAllowedTender(Boolean.FALSE);
                    }
                }
            } else {
                userValidation.setIsAllowedTender(Boolean.FALSE);
            }

			// 服务费授权状态
			userValidation.setPaymentAuthStatus(user.getPaymentAuthStatus());
			// 服务费授权开关
			userValidation.setPaymentAuthOn(CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_PAYMENT_AUTH).getEnabledStatus());
			userValidation.setInvesAuthOn(CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_AUTO_TENDER_AUTH).getEnabledStatus());
            userValidation.setCreditAuthOn(CommonUtils.getAuthConfigFromCache(CommonUtils.KEY_AUTO_CREDIT_AUTH).getEnabledStatus());
			try {
				// 5. 用户是否完成风险测评标识：0未测评 1已测评
				// modify by liuyang 20180411 用户是否完成风险测评标识 start
//				JSONObject jsonObject = CommonSoaUtils.getUserEvalationResultByUserId(userId + "");
//				logger.info("风险测评结果： {}", jsonObject.toJSONString());
//				if ((Integer)jsonObject.get("userEvaluationResultFlag") == 1) {
//					userValidation.setRiskTested(Boolean.TRUE);
//				} else {
//					userValidation.setRiskTested(Boolean.FALSE);
//				}
				if(user.getIsEvaluationFlag()==1 && null != user.getEvaluationExpiredTime()){
					//测评到期日
					Long lCreate = user.getEvaluationExpiredTime().getTime();
					//当前日期
					Long lNow = System.currentTimeMillis();
					if (lCreate <= lNow) {
						//已过期需要重新评测
						userValidation.setRiskTested("2");
					} else {
						//未到一年有效期
						userValidation.setRiskTested("1");
					}
				}else{
					userValidation.setRiskTested("0");
				}
				// modify by liuyang 20180411 用户是否完成风险测评标识 end
			} catch (Exception e) {
				logger.error("查询用户是否完成风险测评标识出错....", e);
				userValidation.setRiskTested("0");
			}

			try {
				// 6. 用户是否完成自动授权标识: 0: 未授权 1:已授权
				HjhUserAuth hjhUserAuth = this.hjhPlanService.getHjhUserAuthByUserId(userId);
				if (Validator.isNotNull(hjhUserAuth) && hjhUserAuth.getAutoInvesStatus() == 0 || hjhUserAuth == null) {
					userValidation.setAutoInves(Boolean.FALSE);
				} else {
					userValidation.setAutoInves(Boolean.TRUE);
				}

				if (Validator.isNotNull(hjhUserAuth) && hjhUserAuth.getAutoCreditStatus() == 0 || hjhUserAuth == null) {
					userValidation.setAutoTransfer(Boolean.FALSE);
				} else {
					userValidation.setAutoTransfer(Boolean.TRUE);
				}
			} catch (Exception e) {
				logger.error("用户是否完成自动授权标识出错....", e);
				userValidation.setAutoInves(Boolean.FALSE);
				userValidation.setAutoTransfer(Boolean.FALSE);
			}

		}
	}

	/**
	 * 创建相应的项目的加入记录分页信息
	 * 
	 * @param result
	 * @param planNid
	 * @param pageNo
	 * @param pageSize
	 */
	private void getHjhPlanAccede(HjhPlanAccedeResultBean result, String planNid, int pageNo, int pageSize) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("planNid", planNid);
		int recordTotal = this.hjhPlanService.countPlanAccedeRecordTotal(params);

		// 加入总人次
		result.setUserCount(recordTotal);
		// 加入总金额
		result.setAccount(this.getPlanAccedeAccount(params));
		if (recordTotal > 0) {
			int limit = pageSize;
			int page = pageNo;
			int offSet = (page - 1) * limit;
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			List<DebtPlanAccedeCustomize> recordList = hjhPlanService.selectPlanAccedeList(params);

			if (!CollectionUtils.isEmpty(recordList)) {
				List<HjhPlanAccedeResultBean.AccedeList> accedeList = result.getAccedeList();
				HjhPlanAccedeResultBean.AccedeList accede = null;
				for (DebtPlanAccedeCustomize entity : recordList) {
					accede = new HjhPlanAccedeResultBean.AccedeList();
					accede.setAccedeAccount(entity.getAccedeAccount());
					accede.setAccedeTime(entity.getAccedeTime());
					accede.setUserName(entity.getUserName());
					accedeList.add(accede);
				}
			}

			// 判断本次查询是否已经全部查出数据
			if ((page * limit) > recordTotal) {
				result.setEnd(Boolean.TRUE);
			} else {
				result.setEnd(Boolean.FALSE);
			}
		}
	}

	/**
	 * 创建计划的标的组成分页信息
	 * 
	 * @param result
	 * @param planNid
	 * @param pageNo
	 * @param pageSize
	 */
	private void searchHjhPlanBorrow(HjhPlanBorrowResultBean result, String planNid, int pageNo, int pageSize) {

		Map<String, Object> params = new HashMap<String, Object>();
		params.put("planNid", planNid);
		Date date = GetDate.getDate();
		int dayStart10 = GetDate.getDayStart10(date);
		int dayEnd10 = GetDate.getDayEnd10(date);
		params.put("startTime", dayStart10);
		params.put("endTime", dayEnd10);
		int recordTotal = this.hjhPlanService.countPlanBorrowRecordTotal(params);
		// 加入总人次
		result.setUserCount(recordTotal);
		// 加入总金额
		result.setAccount(this.getPlanAccedeAccount(params));
		if (recordTotal > 0) {
			int limit = pageSize;
			int page = pageNo;
			int offSet = (page - 1) * limit;
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			List<DebtPlanBorrowCustomize> consumeList = hjhPlanService.selectPlanBorrowList(params);

			if (!CollectionUtils.isEmpty(consumeList)) {
				List<HjhPlanBorrowResultBean.BorrowList> borrowList = result.getBorrowList();
				HjhPlanBorrowResultBean.BorrowList borrow = null;
				for (DebtPlanBorrowCustomize entity : consumeList) {
					borrow = new HjhPlanBorrowResultBean.BorrowList();
					borrow.setBorrowApr(entity.getBorrowApr());
					borrow.setBorrowNid(entity.getBorrowNid());
					borrow.setBorrowPeriod(entity.getBorrowPeriod());
					borrow.setTureName(entity.getTrueName());
					borrowList.add(borrow);
				}
			}

			// 判断本次查询是否已经全部查出数据
			if ((page * limit) > recordTotal) {
				result.setEnd(Boolean.TRUE);
			} else {
				result.setEnd(Boolean.FALSE);
			}
		}
	}

	/**
	 * 根据planNid获取计划加入金额
	 * 
	 * @param params
	 * @return
	 */
	private String getPlanAccedeAccount(Map<String, Object> params) {
		Long sum = hjhPlanService.selectPlanAccedeSum(params);// 加入总金额
		DecimalFormat df = CustomConstants.DF_FOR_VIEW;
		if (sum == null || sum == 0) {
			return "0";
		} else {
			return df.format(sum);
		}
	}
}
