
package com.hyjf.web.help;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.customize.HelpCategoryCustomize;
import com.hyjf.mybatis.model.customize.HelpContentCustomize;
import com.hyjf.web.BaseController;
import com.hyjf.web.common.WebViewUser;
import com.hyjf.web.home.HomePageService;
import com.hyjf.web.platdatastatistics.PlatDataStatisticsService;
import com.hyjf.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author: zhouxiaoshuai
 * @email: 287424494@qq.com
 * @description: 帮助中心控制器
 * @version: 1
 * @date: 2016年5月5日 下午6:12:15
 */
@Controller
@RequestMapping(value = HelpDefine.REQUEST_MAPPING)
public class HelpController extends BaseController {

	@Autowired
	private HelpService helpService;
	@Autowired
	private HomePageService homePageService;
	@Autowired
	private PlatDataStatisticsService platDataStatisticsService;

	/**
	 * 
	 * @method: help_index
	 * @description: 帮助中心索引页面
	 * @param request
	 * @param response
	 * @param modelmap
	 * @return: ModelAndView
	 * @mender: zhouxiaoshuai
	 * @date: 2016年5月9日 上午9:08:10
	 */
	@RequestMapping(HelpDefine.HELP_INDEX_ACTION)
	private ModelAndView help_index(HttpServletRequest request, HttpServletResponse response, String pageName,
			ModelMap modelmap) {
		LogUtil.startLog(HelpController.class.toString(), HelpDefine.HELP_INDEX_ACTION);
		ModelAndView modelAndView = new ModelAndView(HelpDefine.HELP_CENTER_PATH);
		// 查出帮助中心分类
		List<HelpCategoryCustomize> list = helpService.selectCategory("help");
		List<Map<String, Object>> AllList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < list.size(); i++) {
			Map<String, Object> tmpmap = new HashMap<String, Object>();
			tmpmap.put("HelpCategoryCustomize", list.get(i));
			// 查出帮助中心子分类
			List<HelpCategoryCustomize> listsun = helpService.selectSunCategory(list.get(i).getId() + "");
			if (listsun != null) {
				for (int j = 0; j < listsun.size(); j++) {
					List<HelpContentCustomize> listsunContent = helpService.selectSunContentCategory(
							String.valueOf(listsun.get(j).getId()), String.valueOf(list.get(i).getPid()));
					listsun.get(j).setListsunContent(listsunContent);
					// tmpmap.put("listsunContent", listsunContent);
				}
				tmpmap.put("listsun", listsun);
			}
			AllList.add(tmpmap);
		}
		modelmap.put("AllList", AllList);
		LogUtil.endLog(HelpController.class.toString(), HelpDefine.HELP_INDEX_ACTION);		
		String indexId = request.getParameter("indexId");
		modelAndView.addObject("indexId",indexId);
		return modelAndView;
		
	}
	
	
	/**
	 * 新手指引(新手进阶)请求
	 *
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(HelpDefine.HELP_FRESHER_ACTION)
	public ModelAndView noviceGuide(HttpServletRequest request, HttpServletResponse response) {
		LogUtil.startLog(HelpController.class.toString(), HelpDefine.HELP_FRESHER_ACTION);
		ModelAndView modelAndView = new ModelAndView(HelpDefine.HELP_FRESHER_PATH);

		//出借总额(亿元)
		modelAndView.addObject("tenderSum", homePageService.selectTenderSum().divide(new BigDecimal("100000000")).setScale(0, BigDecimal.ROUND_DOWN).toString());
		//收益总额(亿元)
		modelAndView.addObject("interestSum", homePageService.selectInterestSum().divide(new BigDecimal("100000000")).setScale(0, BigDecimal.ROUND_DOWN).toString());
		//累计出借人数(万人)
		modelAndView.addObject("totalTenderSum", homePageService.selectTotalTenderSum() / 10000);

		//当前时间
		modelAndView.addObject("date", GetDate.getDataString(GetDate.date_sdf_wz));
		WebViewUser wuser = WebUtils.getUser(request);
		if (wuser == null) {
			modelAndView.addObject("isLogin", "0");//未登陆
		}else{
			modelAndView.addObject("isLogin", "1");//已登陆
		}
		LogUtil.endLog(HelpController.class.toString(), HelpDefine.HELP_FRESHER_ACTION);
		return modelAndView;
	}
	

}
