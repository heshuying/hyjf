package com.hyjf.admin.manager.borrow.borrowregist;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.hyjf.admin.annotate.Token;
import com.hyjf.admin.coupon.config.CouponConfigDefine;
import com.hyjf.admin.interceptor.TokenInterceptor;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowStyle;
import com.hyjf.mybatis.model.auto.HjhAssetBorrowType;
import com.hyjf.mybatis.model.auto.HjhPlanAsset;
import com.hyjf.mybatis.model.auto.ParamName;
import com.hyjf.mybatis.model.customize.BorrowCommonCustomize;
import com.hyjf.mybatis.model.customize.admin.BorrowRegistCustomize;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = BorrowRegistDefine.REQUEST_MAPPING)
public class BorrowRegistController extends BaseController {

	@Autowired
	private BorrowRegistService borrowRegistService;

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@Token(save = true)
	@RequestMapping(BorrowRegistDefine.INIT)
	@RequiresPermissions(BorrowRegistDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, @ModelAttribute(BorrowRegistDefine.BORROW_REGIST_FORM) BorrowRegistBean form) {

		LogUtil.startLog(BorrowRegistController.class.toString(), BorrowRegistDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(BorrowRegistDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(BorrowRegistController.class.toString(), BorrowRegistDefine.INIT);
		return modeAndView;
	}

	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
    @Token(save = true)
	@RequestMapping(BorrowRegistDefine.SEARCH_ACTION)
	@RequiresPermissions(BorrowRegistDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, BorrowRegistBean form) {

		LogUtil.startLog(BorrowRegistController.class.toString(), BorrowRegistDefine.SEARCH_ACTION);
		ModelAndView modeAndView = new ModelAndView(BorrowRegistDefine.LIST_PATH);

		// 创建分页
		this.createPage(request, modeAndView, form);
		LogUtil.endLog(BorrowRegistController.class.toString(), BorrowRegistDefine.SEARCH_ACTION);
		return modeAndView;
	}

	/**
	 * 创建分页机能
	 * 
	 * @param request
	 * @param modeAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, BorrowRegistBean form) {

		// 项目类型
		List<BorrowProjectType> borrowProjectTypeList = this.borrowRegistService.borrowProjectTypeList("");
		modelAndView.addObject("borrowProjectTypeList", borrowProjectTypeList);

		// 还款方式
		List<BorrowStyle> borrowStyleList = this.borrowRegistService.borrowStyleList("");
		modelAndView.addObject("borrowStyleList", borrowStyleList);

		// 备案状态
		List<ParamName> borrowStatusList = this.borrowRegistService.getParamNameList(CustomConstants.REGIST_STATUS);
		modelAndView.addObject("registStatusList", borrowStatusList);

		BorrowCommonCustomize borrowCommonCustomize = new BorrowCommonCustomize();
		// 借款编码
		borrowCommonCustomize.setBorrowNidSrch(form.getBorrowNidSrch());
		// 项目名称
		borrowCommonCustomize.setBorrowNameSrch(form.getBorrowNameSrch());
		// 用户名
		borrowCommonCustomize.setUsernameSrch(form.getUsernameSrch());
		// 标的期限
		borrowCommonCustomize.setBorrowPeriodSrch(form.getBorrowPeriodSrch());
		// 标的項目類型
		borrowCommonCustomize.setProjectTypeSrch(form.getProjectTypeSrch());
		// 标的还款方式
		borrowCommonCustomize.setBorrowStyleSrch(form.getBorrowStyleSrch());
		// 保证金状态
		borrowCommonCustomize.setRegistStatusSrch(form.getRegistStatusSrch());
		// 添加时间
		borrowCommonCustomize.setTimeStartSrch(form.getTimeStartSrch());
		// 添加时间
		borrowCommonCustomize.setTimeEndSrch(form.getTimeEndSrch());

		Integer count = this.borrowRegistService.countBorrowRegist(borrowCommonCustomize);
		if (count != null && count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			borrowCommonCustomize.setLimitStart(paginator.getOffset());
			borrowCommonCustomize.setLimitEnd(paginator.getLimit());
			List<BorrowRegistCustomize> recordList = this.borrowRegistService.selectBorrowRegistList(borrowCommonCustomize);
			form.setPaginator(paginator);
			modelAndView.addObject("recordList", recordList);
			String sumAccount = this.borrowRegistService.sumBorrowRegistAccount(borrowCommonCustomize);
			modelAndView.addObject("sumAccount", sumAccount);
		}
		modelAndView.addObject(BorrowRegistDefine.BORROW_REGIST_FORM, form);
	}

	/**
	 * 标的信息备案
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = BorrowRegistDefine.DEBT_REGIST_ACTION)
	@RequiresPermissions(BorrowRegistDefine.PERMISSIONS_DEBT_REGIST)
	public String debtRegist(HttpServletRequest request) {

		LogUtil.startLog(BorrowRegistController.class.toString(), BorrowRegistDefine.DEBT_REGIST_ACTION);
		// 返回结果
		JSONObject result = new JSONObject();

		// 项目编号
		String borrowNid = request.getParameter("borrowNid");
		
		/**合规改版新增借款人 缴费授权 + 还款授权 校验 start 注意：跟产品确认过手动备案不需要发短信，目前两个需求全校验*/
		
		/**因业务需求暂时注掉各种授权校验代码 */
		
/*		if(StringUtils.isNotEmpty(borrowNid)){
			List<HjhPlanAsset> assetList = this.borrowRegistService.getAssetListByBorrowNid(borrowNid);
			if(assetList != null && assetList.size() > 0 ){
				
				//1.当资产表有数据时-->资产来源是 第三方
				//(1)通过borrowNid获取 借款人id，担保机构id,(收款人)受托人id，然后分别去做校验
				Borrow borrow1 = this.borrowRegistService.getBorrowByNid(borrowNid);
				if(borrow1 != null){
					
					// (1.1)担保机构id可以为空,不为空时校验，为空不校验
					if(borrow1.getRepayOrgUserId() != null && borrow1.getRepayOrgUserId() != 0 ){
						HjhUserAuth hjhUserAuth2 = borrowRegistService.getHjhUserAuthByUserID(borrow1.getRepayOrgUserId());
						if(hjhUserAuth2 == null){
							result.put("success", "1");
							result.put("msg", "担保机构未做缴费授权，请联系担保机构授权！");
							return result.toString();
						} else {
							// 还款授权状态 DB 默认为 0
							if (hjhUserAuth2.getAutoRepayStatus() == null || hjhUserAuth2.getAutoRepayStatus().toString().equals("0")) {
								result.put("success", "1");
								result.put("msg", "担保机构未做还款授权，请联系担保机构授权！");
								return result.toString();
							}
							// 缴费授权状态 DB 默认为 0
							if (hjhUserAuth2.getAutoPaymentStatus() == null || hjhUserAuth2.getAutoPaymentStatus().toString().equals("0")) {
								result.put("success", "1");
								result.put("msg", "担保机构未做缴费授权，请联系担保机构授权！");
								return result.toString();
							}	
						}
					}
					
					// (1.2)受托人id可以为空,不为空时校验
					if(borrow1.getEntrustedUserId() != null){
						HjhUserAuth hjhUserAuth3 = borrowRegistService.getHjhUserAuthByUserID(borrow1.getEntrustedUserId());
						if(hjhUserAuth3 == null){
							result.put("success", "1");
							result.put("msg", "收款人未做缴费授权，请联系收款人授权！");
							return result.toString();
						} else {
							// 缴费授权状态 DB 默认为 0
							if (hjhUserAuth3.getAutoPaymentStatus() == null || hjhUserAuth3.getAutoPaymentStatus().toString().equals("0")) {
								result.put("success", "1");
								result.put("msg", "收款人未做缴费授权，请联系收款人授权！");
								return result.toString();
							}	
						}
					}
					
					// (1.3)借款人id必须非空
					if(borrow1.getUserId() != null){
						HjhUserAuth hjhUserAuth1 = borrowRegistService.getHjhUserAuthByUserID(borrow1.getUserId());
						if(hjhUserAuth1 == null){
							result.put("success", "1");
							result.put("msg", "借款人未做缴费授权，请联系借款人授权！");
							return result.toString();
						} else {
							// 缴费授权状态 DB 默认为 0
							if (hjhUserAuth1.getAutoPaymentStatus() == null || hjhUserAuth1.getAutoPaymentStatus().toString().equals("0")) {
								result.put("success", "1");
								result.put("msg", "借款人未做缴费授权，请联系借款人授权！");
								return result.toString();
							}
							
							// 是否可用担保机构还款(0:否,1:是) DB默认为0
							if(borrow1.getIsRepayOrgFlag() != null && borrow1.getIsRepayOrgFlag() == 1){
								如果是担保机构还款，还款授权可以不做
							} else {
								// 还款授权状态 DB 默认为 0
								if (hjhUserAuth1.getAutoRepayStatus() == null || hjhUserAuth1.getAutoRepayStatus().toString().equals("0")) {
									result.put("success", "1");
									result.put("msg", "借款人未做还款授权，请联系借款人授权！");
									return result.toString();
								}
							}
						}
					} else {
						result.put("success", "1");
						result.put("msg", "该借款人不存在！");
					}

				} else {
					result.put("success", "1");
					result.put("msg", "该借款项目不存在！");
				}	

			} else {
				//2.当资产表无数据时-->资产来源是 汇盈平台
				//(1)通过borrowNid获取 借款人id，担保机构id,(收款人)受托人id，然后分别去做校验
				Borrow borrow = this.borrowRegistService.getBorrowByNid(borrowNid);
				if(borrow != null){
					// (1.1)借款人id必须非空
					if(borrow.getUserId() != null){
						HjhUserAuth hjhUserAuth1 = borrowRegistService.getHjhUserAuthByUserID(borrow.getUserId());
						if(hjhUserAuth1 == null){
							result.put("success", "1");
							result.put("msg", "借款人未做缴费授权，请联系借款人授权！");
							return result.toString();
						} else {
							// 还款授权状态 DB 默认为 0
							if (hjhUserAuth1.getAutoRepayStatus() == null || hjhUserAuth1.getAutoRepayStatus().toString().equals("0")) {
								result.put("success", "1");
								result.put("msg", "借款人未做还款授权，请联系借款人授权！");
								return result.toString();
							}
							// 缴费授权状态 DB 默认为 0
							if (hjhUserAuth1.getAutoPaymentStatus() == null || hjhUserAuth1.getAutoPaymentStatus().toString().equals("0")) {
								result.put("success", "1");
								result.put("msg", "借款人未做缴费授权，请联系借款人授权！");
								return result.toString();
							}	
						}
					} else {
						result.put("success", "1");
						result.put("msg", "该借款人不存在！");
					}
					
					// (1.2)担保机构id可以为空,不为空时校验，为空不校验
					if(borrow.getRepayOrgUserId() != null && borrow.getRepayOrgUserId() != 0){
						HjhUserAuth hjhUserAuth2 = borrowRegistService.getHjhUserAuthByUserID(borrow.getRepayOrgUserId());
						if(hjhUserAuth2 == null){
							result.put("success", "1");
							result.put("msg", "担保机构未做缴费授权，请联系担保机构授权！");
							return result.toString();
						} else {
							// 还款授权状态 DB 默认为 0
							if (hjhUserAuth2.getAutoRepayStatus() == null || hjhUserAuth2.getAutoRepayStatus().toString().equals("0")) {
								result.put("success", "1");
								result.put("msg", "担保机构未做还款授权，请联系担保机构授权！");
								return result.toString();
							}
							// 缴费授权状态 DB 默认为 0
							if (hjhUserAuth2.getAutoPaymentStatus() == null || hjhUserAuth2.getAutoPaymentStatus().toString().equals("0")) {
								result.put("success", "1");
								result.put("msg", "担保机构未做缴费授权，请联系担保机构授权！");
								return result.toString();
							}	
						}
					}
					
					// (1.3)受托人id可以为空,不为空时校验
					if(borrow.getEntrustedUserId() != null){
						HjhUserAuth hjhUserAuth3 = borrowRegistService.getHjhUserAuthByUserID(borrow.getEntrustedUserId());
						if(hjhUserAuth3 == null){
							result.put("success", "1");
							result.put("msg", "收款人未做缴费授权，请联系收款人授权！");
							return result.toString();
						} else {
							// 缴费授权状态 DB 默认为 0
							if (hjhUserAuth3.getAutoPaymentStatus() == null || hjhUserAuth3.getAutoPaymentStatus().toString().equals("0")) {
								result.put("success", "1");
								result.put("msg", "收款人未做缴费授权，请联系收款人授权！");
								return result.toString();
							}	
						}
					} 
				} else {
					result.put("success", "1");
					result.put("msg", "该借款项目不存在！");
				}	
			}
		} else {
			result.put("success", "1");
			result.put("msg", "项目编号为空！");
		}*/
		/**合规改版新增借款人 缴费授权 + 还款授权 校验 end*/
		
		if (StringUtils.isBlank(borrowNid)) {
			result.put("success", "1");
			result.put("msg", "项目编号为空！");
		} else {
			result = this.borrowRegistService.debtRegist(borrowNid, result);
			/*----------------upd by liushouyi HJH3 Start-----------------------*/
			if("0".equals(result.get("success"))){
				Borrow borrow = this.borrowRegistService.getBorrowByNid(borrowNid);
				HjhAssetBorrowType hjhAssetBorrowType = this.borrowRegistService.selectAssetBorrowType(borrowNid);
				if ("10000000".equals(borrow.getInstCode())) {
					// 成功后到保证金审核
			        // 消息队列名称需要新建指向手动录标的保证金审核
					if (null != hjhAssetBorrowType && null != hjhAssetBorrowType.getAutoBail() && hjhAssetBorrowType.getAutoBail() == 1) {
						this.borrowRegistService.sendToMQ(borrowNid, "10000000", RabbitMQConstants.ROUTINGKEY_BORROW_BAIL);
					}
				} else {
					HjhPlanAsset hjhPlanAsset = this.borrowRegistService.getHjhPlanAsset(borrowNid);
					if (null == hjhPlanAsset) {
						result.put("success", "1");
						result.put("msg", "未获取到相应资产信息 ！");
					} else {
						// 三方资产更新资产表状态
						boolean updateResult = this.borrowRegistService.updateRecordBorrow(hjhPlanAsset, hjhAssetBorrowType);
						if (updateResult) {
							// 成功后到初审队列
							if(hjhPlanAsset.getEntrustedFlg() != null && hjhPlanAsset.getEntrustedFlg().intValue() ==1){
							}else{
								this.borrowRegistService.sendToMQ(hjhPlanAsset.getAssetId(), hjhPlanAsset.getInstCode(), RabbitMQConstants.ROUTINGKEY_BORROW_PREAUDIT);
							}
						} else {
							result.put("success", "1");
							result.put("msg", "资产表更新失败 ！");
						}
					}
				}
			}
			/*----------------upd by liushouyi HJH3 End-----------------------*/
		}
		LogUtil.endLog(BorrowRegistController.class.toString(), BorrowRegistDefine.DEBT_REGIST_ACTION);
		return result.toString();
	}

}
