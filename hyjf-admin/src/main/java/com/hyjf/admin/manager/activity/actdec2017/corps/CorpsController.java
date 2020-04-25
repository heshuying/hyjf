package com.hyjf.admin.manager.activity.actdec2017.corps;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.security.utils.BASE64;
import com.hyjf.mybatis.model.auto.ActJanBargain;
import com.hyjf.mybatis.model.auto.ActdecWinning;

/**
 * 
 * 十二月份活动
 * @author dddzs
 * @version hyjf 1.0
 * @since hyjf 1.0 2016年7月26日
 * @see 上午9:56:26
 */
@Controller
@RequestMapping(value = CorpsDefine.REQUEST_MAPPING)
public class CorpsController extends BaseController {

	@Autowired
	private CorpsService corpsService;

	 /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(CorpsDefine.INIT)
    @RequiresPermissions(CorpsDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, CorpsBean form) {
        LogUtil.startLog(CorpsController.class.toString(), CorpsDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(CorpsDefine.LIST_PATH);

        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(CorpsController.class.toString(), CorpsDefine.INIT);
        return modelAndView;
    }

    /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(CorpsDefine.SEARCH_ACTION)
    @RequiresPermissions(CorpsDefine.PERMISSIONS_SEARCH)
    public ModelAndView search(HttpServletRequest request, CorpsBean form) {
        LogUtil.startLog(CorpsController.class.toString(), CorpsDefine.SEARCH_ACTION);
        ModelAndView modelAndView = new ModelAndView(CorpsDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        LogUtil.endLog(CorpsController.class.toString(), CorpsDefine.SEARCH_ACTION);
        return modelAndView;
    }

    /**
     * 创建分页机能
     * 
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, CorpsBean form) {

		Integer count =corpsService.countRecordListDetail(form);
        
        if (count != null && count > 0) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), Integer.valueOf(String.valueOf(count)));

            List<ActdecWinning> recordList = corpsService.selectRecordListDetail(form,paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            for(ActdecWinning record : recordList){
            	if(StringUtils.isNotBlank(record.getWinningName())){
            		record.setWinningName(new String(BASE64.decode(record.getWinningName())));
            	}
            	
            	if(StringUtils.isNotBlank(record.getCaptainName())){
            		record.setCaptainName(new String(BASE64.decode(record.getCaptainName())));
            	}
            	if(StringUtils.isNotBlank(record.getCorpsName())){
            		String [] xt=record.getCorpsName().split(",");
            		record.setCorpsName(new String(BASE64.decode(xt[0]))+","+new String(BASE64.decode(xt[1]))+","+new String(BASE64.decode(xt[2])));
            	}
            }
            modelAndView.addObject("recordList", recordList);
        }

        modelAndView.addObject(CorpsDefine.ACTDEC_BALLOON_FORM, form);
    }
    
    /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(CorpsDefine.HONGBAO_ACTION)
    @RequiresPermissions(CorpsDefine.PERMISSIONS_SEARCH)
	@ResponseBody
    public JSONObject hongboa(HttpServletRequest request, CorpsBean form) {
        LogUtil.startLog(CorpsController.class.toString(), CorpsDefine.HONGBAO_ACTION);
        JSONObject result = new JSONObject();
        result.put("str", corpsService.updateRecordListD(1));
        LogUtil.endLog(CorpsController.class.toString(), CorpsDefine.HONGBAO_ACTION);
        return result;
    }
    /**
     * 画面初始化
     * 
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(CorpsDefine.PINGGUO_ACTION)
    @RequiresPermissions(CorpsDefine.PERMISSIONS_SEARCH)
	@ResponseBody
    public JSONObject pingguo(HttpServletRequest request, CorpsBean form) {
        LogUtil.startLog(CorpsController.class.toString(), CorpsDefine.PINGGUO_ACTION);
        JSONObject result = new JSONObject();
        result.put("str", corpsService.updateRecordListD(2));
        LogUtil.endLog(CorpsController.class.toString(), CorpsDefine.PINGGUO_ACTION);
        return result;
    }

}
