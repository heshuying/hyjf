package com.hyjf.web.landingpage;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hyjf.common.security.utils.RSAJSPUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.PropertiesConstants;
import com.hyjf.mybatis.model.customize.admin.AdminUserDetailCustomize;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Ads;
import com.hyjf.mybatis.model.auto.CalculateInvestInterest;
import com.hyjf.web.BaseController;
import com.hyjf.web.interceptor.InterceptorDefine;
import com.hyjf.web.util.WebUtils;

/**
 * 
 * 着陆页CONTROLLER
 * @author liuyang
 * @version hyjf 1.0
 * @see 上午11:37:43
 */
@Controller
@RequestMapping(value = LandingPageDefine.REQUEST_MAPPING)
public class LandingPageController extends BaseController {

    @Autowired
    private LandingPageService landingPageService;

    // 着陆页 banner1 type
    private static final String LAND_ADS_TYPE1 = "10";

    // 着陆页banner2 type
    private static final String LAND_ADS_TYPE2 = "11";

    /**
     * init
     * @author liuyang
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(LandingPageDefine.INIT)
    public ModelAndView init(HttpServletRequest request, HttpServletResponse response, LandingPageBean form) {
        LogUtil.startLog(LandingPageDefine.THIS_CLASS, LandingPageDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(LandingPageDefine.LANDING_PAGE_PATH_2);

        long t= GetDate.getMillis();
        //毫秒数 13位  记录初始化时间
        request.getSession().setAttribute("inittime", t);
        // 着陆页banner1 List
        List<Ads> landingPageBanner1 = this.landingPageService.getAdsList(LAND_ADS_TYPE1);
        // add by zhangjp 注册送188红包新手活动-渠道与banner页统一 start
        // 取得渠道名称
        // String utmSource = new String(request.getParameter("utm_source").getBytes("iso8859-1"),"utf-8");
        /*String utmSource = request.getParameter("utm_source");
        // 判断渠道名称中是否包含指定的关键字.
        if(!StringUtils.contains(utmSource, "投之家推广")){
        	// 如果跳转到着落页的不是指定渠道，则遍历banner
        	for(Ads ads:landingPageBanner1){
        		// 判断着落页名称中是否包含指定的关键字
        		if(StringUtils.contains(ads.getName(), "投之家推广")){
        			// 如果含有指定渠道的banner，而跳转过来的渠道并不是指定渠道，则删除该banner，不在着落页显示
        			landingPageBanner1.remove(ads);
        			break;
        		}
        	}
        }*/
        
        Integer userId = WebUtils.getUserId(request);

        if (Validator.isNull(userId)) {
            modelAndView.addObject("isLogin", 0);
        } else {
            modelAndView.addObject("isLogin", 1);
        }

        //String utmId = request.getParameter("utm_id");
        //渠道ID
        String utmId = request.getParameter("utmId");
        // 推荐人ID
        String refferUserId = request.getParameter("refferUserId");

        // 只要推荐人的 refferUserId 不为空, 就查询渠道信息,以防部门发生变更导致问题
        // 20181205 产品需求, 屏蔽渠道,只保留用户ID
//        if ((StringUtils.isNotBlank(refferUserId) && Validator.isNumber(refferUserId))){
//            // 通过当前用户ID 查询用户所在一级分部,从而关联用户所属渠道
//            // 合规自查添加
//            try {
//                AdminUserDetailCustomize userUtmInfo = landingPageService.selectUserUtmInfo(Integer.valueOf(request.getParameter("refferUserId")));
//                if (userUtmInfo != null) {
//                    form.setUtm_id(userUtmInfo.getSourceId().toString());
//                } else {
//                    form.setUtm_id("");
//                }
//            }catch (NumberFormatException e){
//                System.out.println("着陆页获取RefferUserID错误!");
//            }
//        }else
        // 推荐人为空, 渠道ID不为空
        if (StringUtils.isBlank(refferUserId) && (StringUtils.isNotBlank(utmId) && Validator.isNumber(utmId))){
            WebUtils.addCookie(request, response, "utm_id", utmId, null, InterceptorDefine.HYJF_WEB_DOMAIN_NAMES_LIST.get(0));
        }

        modelAndView.addObject("refferUserId", StringUtils.isNotBlank(refferUserId) ? refferUserId : "");
        // add by zhangjp 注册送188红包新手活动-渠道与banner页统一 end
        
    	//统计数据
  		CalculateInvestInterest calculateInvestInterest = this.landingPageService.getTenderSum();
  		if(calculateInvestInterest != null){
  			//出借总额(亿元)
  			modelAndView.addObject("tenderSum", calculateInvestInterest.getTenderSum().toString());
  			//收益总额(亿元)
  			modelAndView.addObject("interestSum", calculateInvestInterest.getInterestSum().toString());
  		}else{
  			//出借总额(亿元)
  			modelAndView.addObject("tenderSum", "0.00");
  			//收益总额(亿元)
  			modelAndView.addObject("interestSum", "0.00");
  		}
        
        // 着陆页banner2 List
        List<Ads> landingPageBanner2 = this.landingPageService.getAdsList(LAND_ADS_TYPE2);
        form.setLandingPageBanner1(landingPageBanner1);
        form.setLandingPageBanner2(landingPageBanner2);
        modelAndView.addObject(LandingPageDefine.LANDING_PAGE_FORM, form);
        request.getSession().setAttribute("from_id", form.getFrom_id());
        LogUtil.endLog(LandingPageDefine.THIS_CLASS, LandingPageDefine.INIT);
        //加密返回的两个参数
        modelAndView.addObject("pubexponent", "10001");
        //modelAndView.addObject("pubmodules", PropUtils.getSystem(PropertiesConstants.HYJF_PUBLICKEY_PASSWORD));
        modelAndView.addObject("pubmodules", RSAJSPUtil.getPunlicKeys());
        return modelAndView;
    }
}
