package com.hyjf.api.server.invest;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.server.borrow.borrowlist.BorrowListService;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.enums.utils.MsgEnum;
import com.hyjf.common.result.ResultApiBean;
import com.hyjf.common.validator.CheckUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.web.InvestListCustomize;
import com.hyjf.mybatis.model.customize.web.InvestRepayCustomize;
import com.hyjf.mybatis.model.customize.web.RepayListCustomize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author xiasq
 * @version UserInvestServer, v0.1 2017/12/4 9:31 第三方查询出借信息
 */

@RestController
@RequestMapping(value = InvestDefine.REQUEST_MAPPING)
public class UserInvestServer extends BaseController {
	private Logger logger = LoggerFactory.getLogger(UserInvestServer.class);

	@Autowired
	private BorrowListService investableBorrowService;

	/**
	 * 获取回款记录
	 */
	@RequestMapping(value = InvestDefine.REPAY_LIST)
	public ResultApiBean<List<RepayListCustomize>> repayList(@RequestBody RepayListRequest bean) {
		// 验证
		CheckUtil.check(Validator.isNotNull(bean.getInstCode()), MsgEnum.STATUS_CE000001);
		CheckUtil.check(Validator.isNotNull(bean.getStartTime()), MsgEnum.STATUS_CE000001);
		CheckUtil.check(Validator.isNotNull(bean.getEndTime()), MsgEnum.STATUS_CE000001);

		logger.info("bean:{}", JSONObject.toJSONString(bean));

		// 验签
		CheckUtil.check(this.verifyRequestSign(bean, InvestDefine.REPAY_LIST), MsgEnum.ERR_SIGN);

		// 返回查询结果
		return new ResultApiBean<List<RepayListCustomize>>(investableBorrowService.searchRepayList(bean));
	}
	/**
	 * 获取出借信息
	 * 
	 * @param bean
	 * @return
	 */
	@RequestMapping(value = InvestDefine.INVEST_LIST)
	public ResultApiBean<List<InvestListCustomize>> investList(@RequestBody InvestListRequest bean) {
		// 验证
		CheckUtil.check(Validator.isNotNull(bean.getInstCode()), MsgEnum.STATUS_CE000001);
		CheckUtil.check(Validator.isNotNull(bean.getStartTime()), MsgEnum.STATUS_CE000001);
		CheckUtil.check(Validator.isNotNull(bean.getEndTime()), MsgEnum.STATUS_CE000001);

		logger.info("bean:{}", JSONObject.toJSONString(bean));

		// 验签
		CheckUtil.check(this.verifyRequestSign(bean, InvestDefine.INVEST_LIST), MsgEnum.ERR_SIGN);

		// 返回查询结果
		return new ResultApiBean<List<InvestListCustomize>>(investableBorrowService.searchInvestListNew(bean));
	}
	
	/**
	 * 获取用户开户信息
	 * 
	 * @param bean
	 * @return
	 */
	@RequestMapping(value = InvestDefine.INVEST_REPAY_LIST)
	public ResultApiBean<List<InvestRepayCustomize>> investRepaysList(@RequestBody InvestRepayBean bean) {
		// 验证
		CheckUtil.check(Validator.isNotNull(bean.getInstCode()), MsgEnum.STATUS_CE000001);
		CheckUtil.check(Validator.isNotNull(bean.getTimestamp()), MsgEnum.STATUS_CE000001);
		CheckUtil.check(Validator.isNotNull(bean.getUserIds()), MsgEnum.STATUS_CE000001);
		
		logger.info("bean:{}", JSONObject.toJSONString(bean));
		
		// 验签
		CheckUtil.check(this.verifyRequestSign(bean, InvestDefine.INVEST_REPAY_LIST), MsgEnum.ERR_SIGN);
		
		// 返回查询结果
		return new ResultApiBean<List<InvestRepayCustomize>>(investableBorrowService.searchInvestRepaysListNew(bean));
	}

}
