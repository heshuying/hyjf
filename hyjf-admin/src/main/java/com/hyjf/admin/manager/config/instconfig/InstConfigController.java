package com.hyjf.admin.manager.config.instconfig;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.RedisConstants;
import com.hyjf.common.util.SessionUtils;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.auto.HjhInstConfig;
import com.hyjf.mybatis.model.customize.AdminSystem;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * 现金贷资产方配置页面
 *
 * @author qingbing
 */
@Controller
@RequestMapping(value = InstConfigDefine.REQUEST_MAPPING)
public class InstConfigController extends BaseController {

    @Autowired
    private InstConfigService instConfigService;

    public static JedisPool pool = RedisUtils.getPool();

    /**
     * 现金贷资产方配置画面初始化
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(InstConfigDefine.INIT)
    @RequiresPermissions(InstConfigDefine.PERMISSIONS_VIEW)
    public ModelAndView init(HttpServletRequest request, @ModelAttribute(InstConfigDefine.INSTCONFIG_FORM) InstConfigBean form) {
        // 日志开始
        LogUtil.startLog(InstConfigController.class.toString(), InstConfigDefine.INIT);
        ModelAndView modelAndView = new ModelAndView(InstConfigDefine.LIST_PATH);
        // 创建分页
        this.createPage(request, modelAndView, form);
        // 日志结束
        LogUtil.endLog(InstConfigController.class.toString(), InstConfigDefine.INIT);
        return modelAndView;
    }

    /**
     * 创建分页机能
     *
     * @param request
     * @param modelAndView
     * @param form
     */
    private void createPage(HttpServletRequest request, ModelAndView modelAndView, InstConfigBean form) {
    	List<HjhInstConfigWrap> resultList = new ArrayList<HjhInstConfigWrap>();
        List<HjhInstConfig> recordList = instConfigService.getRecordList(-1, -1);
        if (recordList != null) {
            Paginator paginator = new Paginator(form.getPaginatorPage(), recordList.size());
            recordList = this.instConfigService.getRecordList(paginator.getOffset(), paginator.getLimit());

            for(HjhInstConfig config : recordList){
            	HjhInstConfigWrap configWrap = new HjhInstConfigWrap();
            	BeanUtils.copyProperties(config, configWrap);
            	String capitalAvailable = RedisUtils.get(RedisConstants.CAPITAL_TOPLIMIT_+config.getInstCode());
            	if(StringUtils.isNotEmpty(capitalAvailable)){
            		configWrap.setCapitalAvailable(capitalAvailable);
            	}else{
            		configWrap.setCapitalAvailable(configWrap.getCapitalToplimit().toString());
            		RedisUtils.set(RedisConstants.CAPITAL_TOPLIMIT_+config.getInstCode(),configWrap.getCapitalToplimit().toString());
            	}
            	resultList.add(configWrap);
            }
            form.setPaginator(paginator);
            form.setRecordList(resultList);
            modelAndView.addObject(InstConfigDefine.INSTCONFIG_FORM, form);
        }
    }

    /**
     * 画面迁移(含有id更新，不含有id添加)
     *
     * @param request
     * @param form
     * @return
     */
    @RequestMapping(InstConfigDefine.INFO_ACTION)
    @RequiresPermissions(value = {InstConfigDefine.PERMISSIONS_INFO, InstConfigDefine.PERMISSIONS_ADD, InstConfigDefine.PERMISSIONS_MODIFY}, logical = Logical.OR)
    public ModelAndView info(HttpServletRequest request, @ModelAttribute(InstConfigDefine.INSTCONFIG_FORM) InstConfigBean form) {
        LogUtil.startLog(InstConfigController.class.toString(), InstConfigDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(InstConfigDefine.INFO_PATH);

        if (StringUtils.isNotEmpty(form.getIds())) {
            Integer id = Integer.valueOf(form.getIds());
            HjhInstConfig record = this.instConfigService.getRecord(id);
            HjhInstConfigWrap recordWrap = new HjhInstConfigWrap();
        	BeanUtils.copyProperties(record, recordWrap);
        	//获取发标额度余额
        	String capitalAvailable = RedisUtils.get(RedisConstants.CAPITAL_TOPLIMIT_+record.getInstCode());
        	if(StringUtils.isNotEmpty(capitalAvailable)){
        		recordWrap.setCapitalAvailable(capitalAvailable);
        	}else{
        		recordWrap.setCapitalAvailable(recordWrap.getCapitalToplimit().toString());
        		RedisUtils.set(RedisConstants.CAPITAL_TOPLIMIT_+record.getInstCode(),recordWrap.getCapitalToplimit().toString());
        	}
            modelAndView.addObject(InstConfigDefine.INSTCONFIG_FORM, recordWrap);
        }
        LogUtil.endLog(InstConfigController.class.toString(), InstConfigDefine.INFO_ACTION);
        return modelAndView;
    }

    /**
     * 保证金添加
     * @param form
     * @return
     */
    @RequestMapping(InstConfigDefine.INSERT_ACTION)
    @RequiresPermissions(InstConfigDefine.PERMISSIONS_ADD)
    public ModelAndView add(HjhInstConfig form) {
        // 日志开始
        LogUtil.startLog(InstConfigController.class.toString(), InstConfigDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(InstConfigDefine.INFO_PATH);
        // 调用校验
        if (validatorFieldCheck(modelAndView, form) != null) {
            // 失败返回
            return validatorFieldCheck(modelAndView, form);
        }
        AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        form.setCreateUser(Integer.parseInt(adminSystem.getId()));
        form.setUpdateUser(Integer.parseInt(adminSystem.getId()));
        String instCode = form.getInstCode();
        // 成功插入
        int result = this.instConfigService.insertRecord(form, instCode);
//        if(result > 0 && form.getCapitalToplimit() != null && !RedisUtils.exists(RedisConstants.CAPITAL_TOPLIMIT_+instCode)){
//        	RedisUtils.set(RedisConstants.CAPITAL_TOPLIMIT_ + instCode, form.getCapitalToplimit().toString());
//        }
        modelAndView.addObject(InstConfigDefine.SUCCESS, InstConfigDefine.SUCCESS);
        // 日志结束
        LogUtil.endLog(InstConfigController.class.toString(), InstConfigDefine.INSERT_ACTION);
        return modelAndView;
    }

    /**
     * 保证金修改
     * @param form
     * @return
     */
    @RequestMapping(InstConfigDefine.UPDATE_ACTION)
    @RequiresPermissions(InstConfigDefine.PERMISSIONS_MODIFY)
    public ModelAndView update(HjhInstConfig form) {
        // 日志开始
        LogUtil.startLog(InstConfigController.class.toString(), InstConfigDefine.INSERT_ACTION);
        ModelAndView modelAndView = new ModelAndView(InstConfigDefine.INFO_PATH);
        // 调用校验
        if (validatorFieldCheck(modelAndView, form) != null) {
            // 失败返回
            return validatorFieldCheck(modelAndView, form);
        }

        HjhInstConfig instConfig = instConfigService.getRecord(form.getId());
        AdminSystem adminSystem = (AdminSystem) SessionUtils.getSession(CustomConstants.LOGIN_USER_INFO);
        form.setUpdateUser(Integer.parseInt(adminSystem.getId()));
        // 成功修改
        int result = this.instConfigService.updateRecord(form);
        // 更新redis中的可用余额
//        if(result > 0 && form.getCapitalToplimit() != instConfig.getCapitalToplimit()){
//        	if(!RedisUtils.exists(RedisConstants.CAPITAL_TOPLIMIT_+instConfig.getInstCode())){
//            	RedisUtils.set(RedisConstants.CAPITAL_TOPLIMIT_ + instConfig.getInstCode(), form.getCapitalToplimit().toString());
//            }else {
//            	if(form.getCapitalToplimit().compareTo(instConfig.getCapitalToplimit()) > 0){
//            		redisAdd(RedisConstants.CAPITAL_TOPLIMIT_ + instConfig.getInstCode(),form.getCapitalToplimit().subtract(instConfig.getCapitalToplimit()).toString());//增加redis相应计划可投金额
//            	}else{
//            		redisSubstrack(RedisConstants.CAPITAL_TOPLIMIT_ + instConfig.getInstCode(),instConfig.getCapitalToplimit().subtract(form.getCapitalToplimit()).toString());//减少风险保证金可投金额
//
//            	}
//            }
//        }

        modelAndView.addObject(InstConfigDefine.SUCCESS, InstConfigDefine.SUCCESS);
        // 日志结束
        LogUtil.endLog(InstConfigController.class.toString(), InstConfigDefine.INSERT_ACTION);
        return modelAndView;
    }

    /**
     * 删除配置信息
     * @param request
     * @param ids
     * @return
     */
    @RequestMapping(InstConfigDefine.DELETE_ACTION)
    @RequiresPermissions(InstConfigDefine.PERMISSIONS_DELETE)
    public ModelAndView deleteRecordAction(HttpServletRequest request, String ids) {
        LogUtil.startLog(InstConfigController.class.toString(), InstConfigDefine.DELETE_ACTION);

        ModelAndView modelAndView = new ModelAndView(InstConfigDefine.RE_LIST_PATH);
        // 解析json字符串
        List<Integer> recordList = JSONArray.parseArray(ids, Integer.class);
        this.instConfigService.deleteRecord(recordList);
        LogUtil.endLog(InstConfigController.class.toString(), InstConfigDefine.DELETE_ACTION);
        return modelAndView;
    }

    /**
     * 调用校验表单方法
     *
     * @param modelAndView
     * @param form
     * @return
     */
    private ModelAndView validatorFieldCheck(ModelAndView modelAndView, HjhInstConfig form) {
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

    /**
     * 发标额度上限校验
     *
     * @param request
     * @return
     */
    @ResponseBody
    @RequestMapping(value = InstConfigDefine.TOPLIMITCHECH_ACTION, method = RequestMethod.POST)
    public String topLimitCheckAction(HttpServletRequest request) {
    	JSONObject ret = new JSONObject();
		String message = ValidatorFieldCheckUtil.getErrorMessage("required", "");
		message = message.replace("{label}", "发标额度上限");

		String capitalToplimitStr = request.getParameter("param");
		String capitalUsedStr = request.getParameter("capitalUsed");
		//新增配置instCode
		if (StringUtils.isBlank(capitalUsedStr) || "undefined".equals(capitalUsedStr)) {
			return ret.toJSONString();
		}

		if (StringUtils.isNotBlank(capitalToplimitStr)) {
			BigDecimal capitalToplimit = new BigDecimal(capitalToplimitStr);
			BigDecimal capitalUsed = new BigDecimal(capitalUsedStr);
			if (capitalToplimit.compareTo(capitalUsed) < 0) {
				message = ValidatorFieldCheckUtil.getErrorMessage("capitalAvailable.not.minus");
				ret.put("info", message);
				return ret.toString();
			}
		}
		//校验通过正常返回
		ret.put("status", "y");
		return ret.toJSONString();
    }
	/**
	 * 项目编号是否存在
	 *
	 * @param request
	 * @return
	 */
	@RequestMapping(value = InstConfigDefine.ISEXISTS_ACTION, method = RequestMethod.POST)
	@ResponseBody
	public String isExistsUser(HttpServletRequest request) {
		String message = this.instConfigService.isExists(request);
		return message;
	}
}
