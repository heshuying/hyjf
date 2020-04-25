/**
 * Description：用户预约管理
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.web.user.appoint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.FreezeList;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.customize.web.WebBorrowAppointCustomize;
import com.hyjf.web.WebBaseAjaxResultBean;
import com.hyjf.web.BaseController;
import com.hyjf.web.user.mytender.MyTenderDefine;
import com.hyjf.web.util.WebUtils;

@Controller
@RequestMapping(value = AppointDefine.REQUEST_MAPPING)
public class AppointController extends BaseController {

	@Autowired
	private AppointService appointService;

	/**
	 * 预约介绍
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = AppointDefine.INIT_INTRODUCE_ACTION)
	public ModelAndView initAppointRule(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView modelAndView = new ModelAndView(AppointDefine.APPOINT_INTRODUCE_PTAH);
		return modelAndView;
	}

	/**
	 * 处罚规则
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = AppointDefine.INIT_PUNISH_ACTION)
	public ModelAndView initPunishRule(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView modelAndView = new ModelAndView(AppointDefine.APPOINT_PUNISH_PTAH);
		return modelAndView;
	}

	/**
	 * 初始化预约管理画面
	 * 
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = AppointDefine.INIT_APPOINT_ACTION)
	public ModelAndView initAppoint(HttpServletRequest request, HttpServletResponse response) {

		ModelAndView modelAndView = new ModelAndView(AppointDefine.APPOINT_LIST_PTAH);
		return modelAndView;
	}

	/**
	 * 查询用户预约管理列表
	 * 
	 * @param project
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppointDefine.APPOINT_LIST_ACTION, method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public AppointListAjaxBean searchUserProjectList(@ModelAttribute AppointListBean form, HttpServletRequest request,
			HttpServletResponse response) {

		LogUtil.startLog(MyTenderDefine.THIS_CLASS, AppointDefine.APPOINT_LIST_ACTION);
		// 用户ID
		Integer userId = WebUtils.getUserId(request);
		form.setUserId(userId.toString());
		AppointListAjaxBean result = new AppointListAjaxBean();
		if (StringUtils.isBlank(form.getAppointStatus())) {
			// 预约中
			form.setAppointStatus("1");
			result.setAppointStatus("1");
		} else {
			result.setAppointStatus(form.getAppointStatus());
		}
		this.createAppointListPage(result, form);
		// 获取登录用户UserId
		// 获取用户信息
		Users user = this.appointService.getUsers(userId);
		// 获取用户失信积分
		int recordTotal = Validator.isNotNull(user.getRecodTotal()) ? user.getRecodTotal() : 0;
		result.setRecordTotal(String.valueOf(recordTotal));
		result.success();
		LogUtil.endLog(MyTenderDefine.THIS_CLASS, AppointDefine.APPOINT_LIST_ACTION);
		return result;
	}

	/**
	 * 创建用户出借列表分页数据
	 * 
	 * @param request
	 * @param info
	 * @param form
	 */
	private void createAppointListPage(AppointListAjaxBean result, AppointListBean form) {

		Map<String, Object> params = new HashMap<String, Object>();
		String appointStatus = StringUtils.isNotEmpty(form.getAppointStatus()) ? form.getAppointStatus() : null;
		String userId = StringUtils.isNotEmpty(form.getUserId()) ? form.getUserId() : null;
		params.put("appointStatus", appointStatus);
		params.put("userId", userId);
		// 查询用户的预约总数
		int recordTotal = this.appointService.countAppointRecordTotal(params);
		if (recordTotal > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), recordTotal, form.getPageSize());
			int limit = paginator.getLimit();
			int offSet = paginator.getOffset();
			if (offSet == 0 || offSet > 0) {
				params.put("limitStart", offSet);
			}
			if (limit > 0) {
				params.put("limitEnd", limit);
			}
			// 查询用户预约列表数据
			List<WebBorrowAppointCustomize> recordList = this.appointService.selectAppointRecordList(params);
			result.setPaginator(paginator);
			result.setAppointlist(recordList);
		} else {
			result.setPaginator(new Paginator(form.getPaginatorPage(), 0));
			result.setAppointlist(new ArrayList<WebBorrowAppointCustomize>());
		}
	}

	/**
	 * 查询用户预约管理列表
	 * 
	 * @param project
	 * @param request
	 * @param response
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = AppointDefine.CANCEL_APPOINT_ACTION, method = RequestMethod.POST, produces = "application/json;charset=utf-8")
	public WebBaseAjaxResultBean cancelAppoint(HttpServletRequest request, HttpServletResponse response) {

		LogUtil.startLog(MyTenderDefine.THIS_CLASS, AppointDefine.CANCEL_APPOINT_ACTION);
		// 订单号
		String orderId = request.getParameter("orderId");
		// 用户ID
		Integer userId = WebUtils.getUserId(request);
		WebBaseAjaxResultBean result = new WebBaseAjaxResultBean();
		if (StringUtils.isNotBlank(orderId)) {
			// 检查用户是否可以取消预约
			int appointflag = this.appointService.checkAppointStatus(orderId, userId);
			// 可以取消预约
			if (appointflag == 1) {
				try {
					//解冻资金
					FreezeList freeze = this.appointService.getUserFreeze(userId,orderId);
					if(Validator.isNotNull(freeze)){
						//冻结标识
						String trxId = freeze.getTrxid();
						//冻结订单日期
						String freezeOrderDate = GetOrderIdUtils.getOrderDate(freeze.getCreateTime());
						//解冻预约冻结资金
						try {
							boolean appointFreezeFlag = this.appointService.unFreezeOrder(userId,orderId,trxId,freezeOrderDate);
							//预约冻结资金解冻成功
							if (appointFreezeFlag) {
								// 取消预约
								boolean flag = this.appointService.userCancelAppoint(orderId, userId);
								if (flag) {
									result.success();
									result.setMessage("预约取消成功！");
								} else {
									result.setStatus(false);
									result.setMessage("预约取消失败！");
								}
							}else{
								result.setStatus(false);
								result.setMessage("预约解冻失败,请联系系统管理员！");
							}
						}catch(Exception e){
							result.setStatus(false);
							result.setMessage("预约解冻失败,请联系系统管理员！");
						}
					}else{
						result.setStatus(false);
						result.setMessage("预约取消失败,未查询到冻结记录！");
					}
				} catch (Exception e) {
					e.printStackTrace();
					result.setStatus(false);
					result.setMessage("预约取消失败！");
				}
			}else if (appointflag == 0) {// 预约不存在
				result.setStatus(false);
				result.setMessage("未查询到相应的预约信息！");
			}else if (appointflag == 2) {// 数据错误
				result.setStatus(false);
				result.setMessage("数据错误，请联系客服！");
			}else if (appointflag == 3) {// 自动发标的不能取消预约
				result.setStatus(false);
				result.setMessage("已经进入自动发标无法取消预约！");
			}
		} else {
			result.setStatus(false);
			result.setMessage("订单号错误！");
		}
		LogUtil.endLog(MyTenderDefine.THIS_CLASS, AppointDefine.CANCEL_APPOINT_ACTION);
		return result;
	}
}
