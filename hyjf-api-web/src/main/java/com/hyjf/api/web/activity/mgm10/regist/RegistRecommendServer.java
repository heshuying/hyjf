package com.hyjf.api.web.activity.mgm10.regist;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.activity.mgm10.regist.RegistRecommendBean;
import com.hyjf.activity.mgm10.regist.RegistRecommendDefind;
import com.hyjf.activity.mgm10.regist.RegistRecommendResultBean;
import com.hyjf.activity.mgm10.regist.RegistRecommendService;
import com.hyjf.api.web.BaseController;

/**
 * <p>
 * 10月份MGM活动-注册开户用户发放推荐星
 * </p>
 * 
 * @author zhangjp
 * @version 1.0.0
 */
@Controller
// 该活动已过期作废 关闭请求地址
// @RequestMapping(RegistRecommendDefind.REQUEST_MAPPING)
public class RegistRecommendServer extends BaseController {

	Logger _log = LoggerFactory.getLogger(RegistRecommendServer.class);
	@Autowired
	private RegistRecommendService registRecommendService;

	/**
	 * 活动期内邀请用户注册 调用该接口生成一条发放推荐星数据 以备batch发放推荐星
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping(value = RegistRecommendDefind.INVITE_USER, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public RegistRecommendResultBean inviteUser(HttpServletRequest request, RegistRecommendBean paramBean) throws Exception {
		// 该活动已过期作废
		/*_log.info(RegistRecommendServer.class.getName() + "==>" + RegistRecommendDefind.INVITE_USER + "==>"
				+ "活动期内邀请用户注册获得推荐星开始！");
		
		RegistRecommendResultBean result = new RegistRecommendResultBean();
		if (!this.checkSign(paramBean, RegistRecommendDefind.METHOD_INVITE_USER)) {
			result.setStatus(BaseResultBean.STATUS_FAIL);
			result.setStatusDesc("验签失败！");
			_log.info("验签失败！---->" + JSONObject.toJSONString(paramBean));
			return result;
		}
		
		JSONObject checkResult  = new JSONObject();
		Properties properties = PropUtils.getSystemResourcesProperties();
		String activityId = properties.getProperty(CustomConstants.MGM10_ACTIVITY_ID).trim();
		Integer inviteUser = paramBean.getInviteUser();
		Integer inviteByUser = paramBean.getInviteByUser();
		checkResult = registRecommendService.validateService(activityId,inviteUser,inviteByUser);
		String errorCode = checkResult.getString("error");
		if (errorCode != "0") {
			result.setStatus(BaseResultBean.STATUS_FAIL);
			result.setStatusDesc(checkResult.getString("data"));
			result.setErrorCode(errorCode);
			_log.info("参数校验失败！---->InviteUser:"+paramBean.getInviteUser()+"--->errorCode："+errorCode);
		}else{
			this.registRecommendService.insertInviteInfo(inviteUser, inviteByUser);
			result.setStatus(BaseResultBean.STATUS_SUCCESS);
			result.setStatusDesc("数据更新成功");
			_log.info("数据更新成功！---->userId:"+paramBean.getUserId());
		}
		
		_log.info(RegistRecommendServer.class.getName() + "==>" + RegistRecommendDefind.INVITE_USER + "==>"
				+ "活动期内邀请用户注册获得推荐星结束！");
		return result;
		*/
		RegistRecommendResultBean result = new RegistRecommendResultBean();
		_log.info(RegistRecommendServer.class.getName() + "==>该活动已过期作废！");
		return result;

	}
}
