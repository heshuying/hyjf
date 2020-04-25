/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
package com.hyjf.web.error;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.web.BaseController;

/**
 * 
 * 前台文章类数据控制器
 * 
 * @author renxingchen
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年4月11日
 * @see 上午11:10:39
 */
@Controller("errorPageController")
@RequestMapping(value = ErrorPageDefine.REQUEST_MAPPING)
public class ErrorPageController extends BaseController {


	/**
	 * 
	 * 首页面跳转
	 * 
	 * @author renxingchen
	 * @return
	 */
	@RequestMapping(value = ErrorPageDefine.REPEAT_SUBMIT_ERROR_ACTION)
	public ModelAndView repeatSubmitError() {
		ModelAndView modelAndView = new ModelAndView(ErrorPageDefine.REPEAT_SUBMIT_ERROR_PAGE);
		return modelAndView;
	}

	
}
