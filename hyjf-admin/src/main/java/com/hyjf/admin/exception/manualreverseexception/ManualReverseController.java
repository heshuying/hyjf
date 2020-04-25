package com.hyjf.admin.exception.manualreverseexception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.codehaus.plexus.util.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseController;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.paginator.Paginator;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.common.validator.CustomErrors;
import com.hyjf.common.validator.ValidatorFieldCheckUtil;
import com.hyjf.mybatis.model.customize.ManualReverseCustomize;

import cn.emay.slf4j.Logger;
import cn.emay.slf4j.LoggerFactory;

/**
 * 手动冲正
 * 
 * @author PC-LIUSHOUYI
 */

@Controller
@RequestMapping(value = ManualReverseDefine.REQUEST_MAPPING)
public class ManualReverseController extends BaseController {

	
	@Autowired
	private ManualReverseService manualReverseService;
	@Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;
	
	/** 类名 */
	private static final String THIS_CLASS = ManualReverseController.class.getName();
	Logger _log = LoggerFactory.getLogger(THIS_CLASS);
	
	/**
	 * 画面初始化
	 * 
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ManualReverseDefine.INIT)
	@RequiresPermissions(ManualReverseDefine.PERMISSIONS_VIEW)
	public ModelAndView init(HttpServletRequest request, ManualReverseBean form) {
		LogUtil.startLog(THIS_CLASS, ManualReverseDefine.INIT);
		ModelAndView modelAndView = new ModelAndView(ManualReverseDefine.LIST_PATH);
		// 创建分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, ManualReverseDefine.INIT);
		return modelAndView;
	}

	/**
	 * 分页技能维护
	 * 
	 * @param request
	 * @param modelAndView
	 * @param form
	 */
	private void createPage(HttpServletRequest request, ModelAndView modelAndView, ManualReverseBean form) {
		// 已开户用户数量
		int count = this.manualReverseService.countManualReverse(form);
		if (count > 0) {
			Paginator paginator = new Paginator(form.getPaginatorPage(), count);
			List<ManualReverseCustomize> record = manualReverseService.selectManualReverseList(form, paginator.getOffset(), paginator.getLimit());
			form.setPaginator(paginator);
			form.setRecordList(record);
		}
		modelAndView.addObject(ManualReverseDefine.FORM, form);
	}

	/**
	 * 
	 * 列表检索Action
	 * 
	 * @author liushouyi
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ManualReverseDefine.SEARCH_ACTION)
	@RequiresPermissions(ManualReverseDefine.PERMISSIONS_SEARCH)
	public ModelAndView search(HttpServletRequest request, ManualReverseBean form) {
		LogUtil.startLog(THIS_CLASS, ManualReverseDefine.SEARCH_ACTION);
		ModelAndView modelAndView = new ModelAndView(ManualReverseDefine.LIST_PATH);
		// 分页
		this.createPage(request, modelAndView, form);
		LogUtil.endLog(THIS_CLASS, ManualReverseDefine.SEARCH_ACTION);
		return modelAndView;
	}

    /**
     * 
     * 画面迁移
     * @author liushouyi
     * @param request
     * @param modelAndView
     * @param form
     * @return
     */
    @RequestMapping(ManualReverseDefine.INFO_ACTION)
    public ModelAndView info(HttpServletRequest request, ManualReverseBean form) {
        LogUtil.startLog(THIS_CLASS, ManualReverseDefine.INFO_ACTION);
        ModelAndView modelAndView = new ModelAndView(ManualReverseDefine.INFO_PATH);
        modelAndView.addObject(ManualReverseDefine.FORM, form);
        LogUtil.endLog(THIS_CLASS, ManualReverseDefine.INFO_ACTION);
        return modelAndView;
    }

	/**
	 * 
	 * 插入数据
	 * 
	 * @author liushouyi
	 * @param request
	 * @param form
	 * @return
	 */
	@RequestMapping(ManualReverseDefine.INSERT_ACTION)
	@RequiresPermissions(ManualReverseDefine.PERMISSIONS_ADD)
	public ModelAndView insert(HttpServletRequest request, ManualReverseBean form) {
		LogUtil.startLog(THIS_CLASS, ManualReverseDefine.INSERT_ACTION);
		ModelAndView modelAndView = new ModelAndView(ManualReverseDefine.INFO_PATH);

		// 画面验证
		this.validatorFieldCheckInsert(modelAndView, form);
		if (ValidatorFieldCheckUtil.hasValidateError(modelAndView)) {
 			modelAndView.addObject(ManualReverseDefine.FORM, form);
			return modelAndView;
		}
		String userName = form.getUserName();
		String accountId = form.getAccountId();
		//用户名与账户匹配校验
		//用户名带出账户信息后无法修改账户信息
//		if (StringUtils.isNotEmpty(userName) && StringUtils.isNotEmpty(accountId)) {
//			String result = this.manualReverseService.getAccountId(userName);
//			if (!accountId.equals(result)) {
//				//用户名与账户不匹配
//				CustomErrors.add(modelAndView, "userName", "admin.username.not.account", ValidatorFieldCheckUtil.getErrorMessage("admin.username.not.account"));
//				modelAndView.addObject(ManualReverseDefine.FORM, form);
//				return modelAndView;
//			}
//		} else
		//用户名与账户均为空的场合返回报错
		if (StringUtils.isEmpty(userName) && StringUtils.isEmpty(accountId)) {
			//用户名与账户必填一项
			CustomErrors.add(modelAndView, "userName", "admin.username.account.not.exists", ValidatorFieldCheckUtil.getErrorMessage("admin.username.account.not.exists"));
			modelAndView.addObject(ManualReverseDefine.FORM, form);
			return modelAndView;
		} else if (StringUtils.isEmpty(userName) && StringUtils.isNotEmpty(accountId)) {
			//通过账号ID获取用户信息
			userName = this.manualReverseService.getUser(accountId);
			if (StringUtils.isEmpty(userName)) {
				//用户账户填写有误
				CustomErrors.add(modelAndView, "accountId", "admin.account.not.exists", ValidatorFieldCheckUtil.getErrorMessage("admin.account.not.exists"));				
				modelAndView.addObject(ManualReverseDefine.FORM, form);
				return modelAndView;
			}
			form.setUserName(userName);
		} 
		//订单号存在校验
		//不校验直接进行冲正处理。因为本身就是异常处理，有些情况可能系统根本没记录
//		if (!this.manualReverseService.isExistsOrderId(form)) {
//			//订单号不存在
//			CustomErrors.add(modelAndView, "seqNo", "admin.seqno.not.exists", ValidatorFieldCheckUtil.getErrorMessage("admin.seqno.not.exists"));				
//			modelAndView.addObject(ManualReverseDefine.FORM, form);
//			return modelAndView;
//		}
		//是否已经冲正
		
		//资金列表插入数据 用户余额恢复成功 插入调账明细一条成功数据
		boolean result = this.manualReverseService.insertAccountList(form, request);
		//调账处理表插入数据
		this.manualReverseService.insertManualReverse(form,result); 

        modelAndView.addObject(ManualReverseDefine.SUCCESS, ManualReverseDefine.SUCCESS);
		LogUtil.endLog(THIS_CLASS, ManualReverseDefine.INSERT_ACTION);
		return modelAndView;
	}
	
	/**
	 * 画面校验(新增数据)
	 * 
	 * @param modelAndView
	 * @param form
	 */
	private void validatorFieldCheckInsert(ModelAndView modelAndView, ManualReverseBean form) {
		
		//原交易流水号
		ValidatorFieldCheckUtil.validateNum(modelAndView, "seqNo",form.getSeqNo(), 6, true);
		//原交易订单号 数字，汇盈系统生成的
		//ValidatorFieldCheckUtil.validateNum(modelAndView, "bankSeqNo",form.getBankSeqNo(), true);
	}
	
    /**
     * 用户是否存在
     *
     * @param request
     * @return
     */
    @ResponseBody 
    @RequestMapping(value = ManualReverseDefine.ISEXISTSUSER_ACTION, method = RequestMethod.POST)
    public String isExistsUser(HttpServletRequest request) {
        LogUtil.startLog(THIS_CLASS, ManualReverseDefine.ISEXISTSUSER_ACTION);
        String message = this.manualReverseService.isExistsUser(request);
        LogUtil.endLog(THIS_CLASS, ManualReverseDefine.ISEXISTSUSER_ACTION);
        return message;
    }
    
    /**
     * 用户存在的场合带出用户开户账户
     * 
     * @param request
     * @param userName
     * @return
     */
    @ResponseBody
    @RequestMapping(value = ManualReverseDefine.GET_ACCOUNTID_ACTION, method = RequestMethod.POST)
    public String getAccountIdAction(HttpServletRequest request, RedirectAttributes attr,String userName) {
        LogUtil.startLog(THIS_CLASS, ManualReverseDefine.GET_ACCOUNTID_ACTION);
        String result = this.manualReverseService.getAccountId(userName);
        LogUtil.endLog(THIS_CLASS, ManualReverseDefine.GET_ACCOUNTID_ACTION);
    	return result;
    }
    
    /**
     * 用户名与账户匹配校验
     * 
     * @param userName
     * @param accountId
     * @return
     */
    public boolean userNameAccountIdChk(String userName, String accountId) {
    	//通过用户名带出账户信息
        String result = this.manualReverseService.getAccountId(userName);
        //与前台输入账户信息匹配
        if (accountId.equals(result)) {
        	return true;
        }
    	return false;
    }
    //自动冲正手动发送MQ测试用
    @ResponseBody
    @RequestMapping(value = "autoM", method = RequestMethod.POST)
    public void for_test() {
        Map<String, String> map = new HashMap<String, String>();
        map.put("status", "1");
        rabbitTemplate.convertAndSend(RabbitMQConstants.ALEV_FANOUT_EXCHANGE,"",JSONObject.toJSONString(map));
        _log.info("--------------手动MQ已推送--------------");
    }
}

	