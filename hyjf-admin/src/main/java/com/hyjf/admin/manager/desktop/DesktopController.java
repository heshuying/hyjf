package com.hyjf.admin.manager.desktop;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;

/**
 * @package com.hyjf.admin.maintenance.AdminPermissions
 * @author GOGTZ-Z
 * @date 2015/07/09 17:00
 * @version V1.0  
 */
@Controller
@RequestMapping(value = DesktopDefine.REQUEST_MAPPING)
public class DesktopController extends BaseController {

	/**
	 * 权限维护画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(DesktopDefine.INIT)
	public ModelAndView init(HttpServletRequest request, DesktopBean form) {
		LogUtil.startLog(DesktopController.class.toString(), DesktopDefine.INIT);
		ModelAndView modeAndView = new ModelAndView(DesktopDefine.INFO_PATH);

		LogUtil.endLog(DesktopController.class.toString(), DesktopDefine.INIT);
		return modeAndView;
	}
}
