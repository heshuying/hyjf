package com.hyjf.admin.manager.config.stzhwhitelist;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.druid.support.json.JSONUtils;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.google.gson.JsonObject;
import com.hyjf.admin.BaseController;
import com.hyjf.admin.coupon.user.CouponUserBean;
import com.hyjf.admin.coupon.user.CouponUserController;
import com.hyjf.admin.coupon.user.CouponUserDefine;
import com.hyjf.admin.exception.openaccountenquiryexception.OpenAccountEnquiryDefine;
import com.hyjf.admin.manager.config.instconfig.HjhInstConfigWrap;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetCode;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.auto.HjhInstConfigExample;
import com.hyjf.mybatis.model.customize.AdminSystem;
import com.hyjf.mybatis.model.customize.STZHWhiteListCustomize;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

/**
 * 现金贷资产方配置页面
 *
 * @author qingbing
 */
@Controller
@RequestMapping(value = StzfWhiteConfigDefine.REQUEST_MAPPING)
public class StzfWhiteConfigController extends BaseController {

    @Autowired
    private StzfWhiteConfigService stzfConfigService;
    
    public static JedisPool pool = RedisUtils.getPool();

    /**
     * 受托支付白名单配置画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(StzfWhiteConfigDefine.INIT)
    @RequiresPermissions(StzfWhiteConfigDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, @ModelAttribute(StzfWhiteConfigDefine.INSTCONFIG_FORM) StzfWhiteConfigBean form) {
        // 日志开始
        LogUtil.startLog(StzfWhiteConfigController.class.toString(), StzfWhiteConfigDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(StzfWhiteConfigDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        // 日志结束
        LogUtil.endLog(StzfWhiteConfigController.class.toString(), StzfWhiteConfigDefine.INIT);
        return modelAndView;
    }

    /**
     * 创建分页机能
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, StzfWhiteConfigBean form) {
        List<STZHWhiteListCustomize> recordList = stzfConfigService.getRecordList(-1, -1);
        if (recordList != null) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
            recordList = this.stzfConfigService.getRecordList(paginator.getOffset(), paginator.getLimit());
            form.setPaginator(paginator);
            form.setRecordList(recordList);
            modelAndView.addObject(StzfWhiteConfigDefine.INSTCONFIG_FORM, form);
        }
    }

    /**
     * 画面迁移(含有id更新，不含有id添加)
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(StzfWhiteConfigDefine.INFO_ACTION)
    @RequiresPermissions(value = {StzfWhiteConfigDefine.PERMISSIONS_INFO, StzfWhiteConfigDefine.PERMISSIONS_ADD, StzfWhiteConfigDefine.PERMISSIONS_MODIFY}, logical = Logical.OR)
    public ModelAndView info(HttpServletRequest request, @ModelAttribute(StzfWhiteConfigDefine.INSTCONFIG_FORM) StzfWhiteConfigBean form) {
        LogUtil.startLog(StzfWhiteConfigController.class.toString(), StzfWhiteConfigDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(StzfWhiteConfigDefine.INFO_PATH);

        if (StringUtils.isNotEmpty(form.getIds())) {
            Integer id = Integer.valueOf(form.getIds());
            STZHWhiteListCustomize record = this.stzfConfigService.getRecord(id);
            record.setRegionList(this.stzfConfigService.getRegionList());
            modelAndView.addObject(StzfWhiteConfigDefine.INSTCONFIG_FORM, record);
            return modelAndView;
        }
        form.setRegionList(this.stzfConfigService.getRegionList());
        LogUtil.endLog(StzfWhiteConfigController.class.toString(), StzfWhiteConfigDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 受托支付白名单添加
     * @param form
     * @return
     */
    @RequestMapping(StzfWhiteConfigDefine.INSERT_ACTION)
    @RequiresPermissions(StzfWhiteConfigDefine.PERMISSIONS_ADD)
    public ModelAndView add(STZHWhiteListCustomize form) {
        // 日志开始
        LogUtil.startLog(StzfWhiteConfigController.class.toString(), StzfWhiteConfigDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(StzfWhiteConfigDefine.INFO_PATH);
        // 调用校验
        if (validatorFieldCheck(modelAndView, form) != null) {
            // 失败返回
            return validatorFieldCheck(modelAndView, form);
        }
        AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        form.setCreateuser(adminSystem.getId());
        form.setUpdateuser(adminSystem.getId());
        if (form.getInstname()!=null) {
        	//机构编号
            form.setInstcode(form.getInstname());
            HjhInstConfigExample hjhInstConfigExample =new HjhInstConfigExample();
            hjhInstConfigExample.createCriteria().andInstCodeEqualTo(form.getInstcode());
            List<HjhInstConfig> hjhInstConfig=stzfConfigService.getRegionName(hjhInstConfigExample);
            if (hjhInstConfig.size()>0) {
                form.setInstname(hjhInstConfig.get(0).getInstName());
			} 
		}
        // 成功插入
        int result = this.stzfConfigService.insertRecord(form);
        modelAndView.addObject(StzfWhiteConfigDefine.SUCCESS, StzfWhiteConfigDefine.SUCCESS);
        // 日志结束
        LogUtil.endLog(StzfWhiteConfigController.class.toString(), StzfWhiteConfigDefine.INSERT_ACTION);
        return modelAndView;
    }

    /**
     * 受托支付白名单修改
     * @param form
     * @return
     */
    @RequestMapping(StzfWhiteConfigDefine.UPDATE_ACTION)
    @RequiresPermissions(StzfWhiteConfigDefine.PERMISSIONS_MODIFY)
    public ModelAndView update(STZHWhiteListCustomize form) {
        // 日志开始
        LogUtil.startLog(StzfWhiteConfigController.class.toString(), StzfWhiteConfigDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(StzfWhiteConfigDefine.INFO_PATH);
        // 调用校验
        if (validatorFieldCheck(modelAndView, form) != null) {
            // 失败返回
            return validatorFieldCheck(modelAndView, form);
        }
        
        STZHWhiteListCustomize instConfig = stzfConfigService.getRecord(form.getId());
        AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        form.setUpdateuser(adminSystem.getId());
        if (form.getInstname()!=null) {
        	//机构编号
            form.setInstcode(form.getInstname());
            HjhInstConfigExample hjhInstConfigExample =new HjhInstConfigExample();
            hjhInstConfigExample.createCriteria().andInstCodeEqualTo(form.getInstcode());
            List<HjhInstConfig> hjhInstConfig=stzfConfigService.getRegionName(hjhInstConfigExample);
            if (hjhInstConfig.size()>0) {
                form.setInstname(hjhInstConfig.get(0).getInstName());
			} 
		}
        // 成功修改
        int result = this.stzfConfigService.updateRecord(form);
        modelAndView.addObject(StzfWhiteConfigDefine.SUCCESS, StzfWhiteConfigDefine.SUCCESS);
        // 日志结束
        LogUtil.endLog(StzfWhiteConfigController.class.toString(), StzfWhiteConfigDefine.INSERT_ACTION);
        return modelAndView;
    }
    /**
     * 加载姓名详细信息
     */
    @ResponseBody
    @RequestMapping(value = StzfWhiteConfigDefine.LOAD_NAME_CONFIG,produces="text/html;charset=UTF-8")
    public String loadNadmeConfig(HttpServletRequest request, RedirectAttributes attr, STZHWhiteListCustomize form) {
        LogUtil.startLog(StzfWhiteConfigController.class.toString(), StzfWhiteConfigDefine.LOAD_NAME_CONFIG);
        STZHWhiteListCustomize stzhWhiteListCustomize=new STZHWhiteListCustomize();
        /*if(StringUtils.isEmpty(form.getUserName())||StringUtils.isEmpty(form.getStUserName())){
            return StringUtils.EMPTY;
        }else {*/
        	STZHWhiteListCustomize stzhWhiteList=this.stzfConfigService.selectByObject(form);
        	JSONObject jsonString = new JSONObject();
        	if (stzhWhiteList!=null) {
        		String string=JSONArray.toJSONString(stzhWhiteList);
        		JSONObject jsonStrings = (JSONObject) jsonString.toJSON(stzhWhiteList);
        		jsonStrings.put("status", "y");
        		System.out.println(jsonStrings.toJSONString());
				return jsonStrings.toJSONString();
			}
//		}
        	jsonString.put("info", "用户名不存在");
        	jsonString.put("status", "n");
            return jsonString.toJSONString();
    } 

    /**
     * 调用校验表单方法
     *
     * @param modelAndView
     * @param form
     * @return
     */
    private ModelAndView validatorFieldCheck(ModelAndView modelAndView, STZHWhiteListCustomize form) {
        // 字段校验
//		if (form.getType() != null
//				&& !ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "name", form.getType(), 20, true)) {
//			return modelAndView;
//		}
//		if (form.getMonthTender() != null
//				&& !ValidatorFieldCheckUtil.validateMaxLength(modelAndView, "value", form.getMonthTender(), 20, true)) {
//			return modelAndView;
//		}
        return null;
    }
    /**
     * ajax用户名称校验
     * 
     * @param request
     * 
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(StzfWhiteConfigDefine.ERROR)
    public String stzfWhiteConfigError(HttpServletRequest request, HttpServletResponse response) {
    	LogUtil.startLog(OpenAccountEnquiryDefine.THIS_CLASS, OpenAccountEnquiryDefine.UPDATE_PATH);
    	JSONObject jsonString = new JSONObject();
    	STZHWhiteListCustomize stzhWhiteListCustomize=new STZHWhiteListCustomize();
    	String userNameString = request.getParameter("param").trim();
    	stzhWhiteListCustomize.setUserName(userNameString);
    	STZHWhiteListCustomize stzhWhiteList=this.stzfConfigService.selectByObject(stzhWhiteListCustomize);
    	if (stzhWhiteList!=null) {
    		jsonString.put("status", "y");
            return jsonString.toJSONString();
		}
    	jsonString.put("info", "用户名不存在!");
        return jsonString.toJSONString();
    }
    /**
     * ajax用户名称校验
     * 
     * @param request
     * 
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(StzfWhiteConfigDefine.ST_ERROR)
    public String stzfWhiteConfigStNameError(HttpServletRequest request, HttpServletResponse response) {
    	LogUtil.startLog(OpenAccountEnquiryDefine.THIS_CLASS, OpenAccountEnquiryDefine.UPDATE_PATH);
    	JSONObject jsonString = new JSONObject();
    	STZHWhiteListCustomize stzhWhiteListCustomize=new STZHWhiteListCustomize();
    	String userNameString = request.getParameter("param").trim();
    	stzhWhiteListCustomize.setStUserName(userNameString);
    	STZHWhiteListCustomize stzhWhiteList=this.stzfConfigService.selectByObject(stzhWhiteListCustomize);
    	if (stzhWhiteList!=null) {
    		jsonString.put("status", "y");
            return jsonString.toJSONString();
		}
    	jsonString.put("info", "用户名不存在!");
        return jsonString.toJSONString();
    }
    	
	/**
	 * 并发情况下保证设置一个值
	 * @param key
	 * @param value
	 */
	private void redisAdd(String key,String value){

		Jedis jedis = pool.getResource();
		
		while ("OK".equals(jedis.watch(key))) {
			List<Object> results = null;
			
			String balance = jedis.get(key);
			BigDecimal bal = new BigDecimal(0);
			if (balance != null) {
				bal =  new BigDecimal(balance);
			}
			BigDecimal val =  new BigDecimal(value);
			
			Transaction tx = jedis.multi();
			String valbeset = bal.add(val).toString();
			tx.set(key, valbeset);
			results = tx.exec();
			if (results == null || results.isEmpty()) {
				jedis.unwatch();
			} else {
				String ret = (String) results.get(0);
				if (ret != null && ret.equals("OK")) {
					// 成功后
					break;
				} else {
					jedis.unwatch();
				}
			}
		}
	}
	
	/**
	 * 并发情况下保证设置一个值
	 * @param key
	 * @param value
	 */
	private boolean redisSubstrack(String key,String value){

		Jedis jedis = pool.getResource();
		boolean result = false;
		
		while ("OK".equals(jedis.watch(key))) {
			List<Object> results = null;
			
			String balance = jedis.get(key);
			BigDecimal bal = new BigDecimal(balance);
			BigDecimal val = new BigDecimal(value);
			
			if(val.compareTo(bal)>0){
				return false;
			}
			
			Transaction tx = jedis.multi();
			String valbeset = bal.subtract(val).toString();
			tx.set(key, valbeset);
			results = tx.exec();
			if (results == null || results.isEmpty()) {
				jedis.unwatch();
			} else {
				String ret = (String) results.get(0);
				if (ret != null && ret.equals("OK")) {
					// 成功后
					result = true;
					break;
				} else {
					jedis.unwatch();
				}
			}
		}
		
		return result;
	}
}
