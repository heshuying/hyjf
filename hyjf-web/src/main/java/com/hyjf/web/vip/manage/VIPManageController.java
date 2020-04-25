package com.hyjf.web.vip.manage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.VipInfo;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.util.WebUtils;

/**
 * 
 * VIP等级管理页面
 * @author hsy
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年6月12日
 * @see 下午2:53:35
 */
@Controller
@RequestMapping(value = VIPManageDefine.REQUEST_MAPPING)
public class VIPManageController extends BaseController {
	@Autowired
	private VIPManageService manageService;

	/**
	 * 
	 * 进入VIP等级页面
	 * @author hsy
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = VIPManageDefine.INIT_ACTION)
	public ModelAndView init(HttpServletRequest request,
			HttpServletResponse response) {

		LogUtil.startLog(VIPManageDefine.THIS_CLASS, VIPManageDefine.INIT_ACTION);
		ModelAndView modelAndView = new ModelAndView(VIPManageDefine.MANAGE_PAGE_PATH);

		WebViewUser wuser = WebUtils.getUser(request);
		if (wuser == null) {
			modelAndView.addObject("message", "用户信息失效，请您重新登录。");
			return new ModelAndView("error/systemerror");
		}
		Integer userId = wuser.getUserId();

		//获取VIP等级信息列表
		List<VipInfo> levelList = manageService.selectVIPInfoList();
		if(levelList == null || levelList.isEmpty()){
            modelAndView.addObject("message", "VIP等级列表数据未获取到。");
            return new ModelAndView("error/systemerror");
		}
		
		//循环去除每一个等级方便页面使用
		for(int i=1; i<=levelList.size(); i++) {
		    VipInfo info = levelList.get(i-1);
		    modelAndView.addObject("level" + i, info);
		}
		modelAndView.addObject("levelSize", levelList.size());
		modelAndView.addObject("levelList", levelList);
		
		//通过用户详情取的用户当前V值
		UsersInfo usersInfo = manageService.getUsersInfoByUserId(userId);
		if(usersInfo == null){
		    modelAndView.addObject("message", "用户详情信息获取失败。");
            return new ModelAndView("error/systemerror");
		}
		if(usersInfo.getVipExpDate() != null){
			modelAndView.addObject("vipExpDate", GetDate.times10toStrYYYYMMDD(usersInfo.getVipExpDate()));
		}
		
		if(usersInfo.getVipValue() == null){
		    usersInfo.setVipValue(0);
		}
		
		modelAndView.addObject("sumValue", usersInfo.getVipValue());

		LogUtil.endLog(VIPManageDefine.THIS_CLASS, VIPManageDefine.MANAGE_PAGE_PATH);
		return modelAndView;
	}


}
