package com.hyjf.api.web.activity.newyear.getcard;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hyjf.activity.newyear.getcard.GetCardBean;
import com.hyjf.activity.newyear.getcard.GetCardDefine;
import com.hyjf.activity.newyear.getcard.GetCardService;
import com.hyjf.api.web.BaseController;
import com.hyjf.base.bean.BaseDefine;

/**
 * 新年活动获取财神卡
 * 
 * @author zhangjinpeng
 *
 */
@Controller
@RequestMapping(GetCardDefine.REQUEST_MAPPING)
public class GetCardServer extends BaseController {

	Logger _log = LoggerFactory.getLogger(GetCardServer.class);
	private static final String THIS_CLASS = GetCardServer.class.getName();

	@Autowired
	GetCardService getCardService;

	/**
	 * 出借发放财神卡
	 * 
	 * @param request
	 * @param paramBean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = GetCardDefine.NEWYEAR_TENDER_MAPPING, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public void newyearTender(HttpServletRequest request, GetCardBean paramBean) {
		_log.info(THIS_CLASS + "---新年活动出借获取财神卡开始。。。" + "出借编号：" + paramBean.getTenderNid());
		// 验签
		if (!this.checkSign(paramBean, BaseDefine.METHOD_NEWYEAR_TENDER)) {
			_log.info("验签失败，出借编号：" + paramBean.getTenderNid());
		}
		try {
			getCardService.updateGetCardTender(paramBean);
		} catch (Exception e) {
			_log.info(THIS_CLASS + "---新年活动出借获取财神卡失败" + "出借编号：" + paramBean.getTenderNid());
		}

		_log.info(THIS_CLASS + "---新年活动出借获取财神卡结束。。。" + "出借编号：" + paramBean.getTenderNid());
	}

	/**
	 * 发放财神卡
	 * 
	 * @param request
	 * @param paramBean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = GetCardDefine.NEWYEAR_SEND_CARD_MAPPING, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public void newyearSendCard(HttpServletRequest request, GetCardBean paramBean) {
		_log.info(THIS_CLASS + "---新年活动发放财神卡开始。。。");
		// 验签
		if (!this.checkSign(paramBean, BaseDefine.METHOD_NEWYEAR_SEND_CARD)) {
			_log.info("验签失败，出借编号：" + paramBean.getTenderNid());
		}
		try {
			getCardService.updateSendCard();
		} catch (Exception e) {
			_log.info(THIS_CLASS + "---新年活动发放财神卡失败");
		}

		_log.info(THIS_CLASS + "---新年活动发放财神卡结束。。。");
	}
	
	/**
	 * 活动期内用户注册或邀请好友注册
	 * 
	 * @param request
	 * @param paramBean
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = GetCardDefine.NEWYEAR_REGIST_MAPPING, method = RequestMethod.POST, produces = "application/json; charset=UTF-8")
	public void newyearRegist(HttpServletRequest request, GetCardBean paramBean) {
		_log.info(THIS_CLASS + "---活动期内用户注册且开户获取财神卡开始。。。" + "用户编号：" + paramBean.getUserId());
		// 验签
		if (!this.checkSign(paramBean, BaseDefine.METHOD_NEWYEAR_REGIST)) {
			_log.info("验签失败，出借编号：" + paramBean.getTenderNid());
		}
		try {
			getCardService.updateGetCardRegist(paramBean);
		} catch (Exception e) {
			_log.info(THIS_CLASS + "---活动期内用户注册且开户获取财神卡失败" + "用户编号：" + paramBean.getUserId());
		}

		_log.info(THIS_CLASS + "---活动期内用户注册且开户获取财神卡结束。。。" + "用户编号：" + paramBean.getUserId());
	}
	

}
