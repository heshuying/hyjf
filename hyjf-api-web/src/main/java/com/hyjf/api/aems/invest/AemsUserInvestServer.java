package com.hyjf.api.aems.invest;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.aems.borrowlist.AemsBorrowListService;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.enums.utils.MsgEnum;
import com.hyjf.common.result.ResultApiBean;
import com.hyjf.common.validator.CheckUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.web.InvestListCustomize;
import com.hyjf.mybatis.model.customize.web.InvestRepayCustomize;
import com.hyjf.mybatis.model.customize.web.RepayListCustomize;

/**
 * @author jijun 20180910
 * @desc 第三方查询投资记录
 */

@RestController
@RequestMapping(value = AemsInvestDefine.REQUEST_MAPPING)
public class AemsUserInvestServer extends BaseController {

	private Logger logger = LoggerFactory.getLogger(AemsUserInvestServer.class);

	@Autowired
	private AemsBorrowListService investableBorrowService;

	/**
	 * 获取回款记录
	 */
	@RequestMapping(method = RequestMethod.POST,value = AemsInvestDefine.REPAY_LIST)
	public ResultApiBean<List<RepayListCustomize>> repayList(@RequestBody AemsRepayListRequest bean) {
		// 验证
		CheckUtil.check(Validator.isNotNull(bean.getInstCode()), MsgEnum.STATUS_CE000001);
		CheckUtil.check(Validator.isNotNull(bean.getStartTime()), MsgEnum.STATUS_CE000001);
		CheckUtil.check(Validator.isNotNull(bean.getEndTime()), MsgEnum.STATUS_CE000001);

		logger.info("bean:{}", JSONObject.toJSONString(bean));

		// 验签
		CheckUtil.check(this.AEMSVerifyRequestSign(bean, AemsInvestDefine.REQUEST_MAPPING+AemsInvestDefine.REPAY_LIST), MsgEnum.ERR_SIGN);

		// 返回查询结果
		return new ResultApiBean<List<RepayListCustomize>>(investableBorrowService.searchRepayList(bean));
	}
	
	/**
	 * 获取投资信息
	 * @param bean
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value = AemsInvestDefine.INVEST_LIST)
	public ResultApiBean<List<InvestListCustomize>> investList(@RequestBody AemsInvestListRequest bean) {
		// 验证
		CheckUtil.check(Validator.isNotNull(bean.getInstCode()), MsgEnum.STATUS_CE000001);
		CheckUtil.check(Validator.isNotNull(bean.getStartTime()), MsgEnum.STATUS_CE000001);
		CheckUtil.check(Validator.isNotNull(bean.getEndTime()), MsgEnum.STATUS_CE000001);

		logger.info("bean:{}", JSONObject.toJSONString(bean));

		// 验签
		CheckUtil.check(this.AEMSVerifyRequestSign(bean, AemsInvestDefine.REQUEST_MAPPING+AemsInvestDefine.INVEST_LIST), MsgEnum.ERR_SIGN);

		// 返回查询结果
		return new ResultApiBean<List<InvestListCustomize>>(investableBorrowService.searchInvestListNew(bean));
	}
	
	/**
	 * 获取回款记录信息
	 * @param bean
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST,value = AemsInvestDefine.INVEST_REPAY_LIST)
	public ResultApiBean<List<InvestRepayCustomize>> investRepaysList(@RequestBody AemsInvestRepayBean bean) {
		// 验证
		CheckUtil.check(Validator.isNotNull(bean.getInstCode()), MsgEnum.STATUS_CE000001);
		CheckUtil.check(Validator.isNotNull(bean.getTimestamp()), MsgEnum.STATUS_CE000001);
		CheckUtil.check(Validator.isNotNull(bean.getUserIds()), MsgEnum.STATUS_CE000001);
		
		logger.info("bean:{}", JSONObject.toJSONString(bean));
		
		// 验签
		CheckUtil.check(this.AEMSVerifyRequestSign(bean, AemsInvestDefine.REQUEST_MAPPING+AemsInvestDefine.INVEST_REPAY_LIST), MsgEnum.ERR_SIGN);
		
		// 返回查询结果
		return new ResultApiBean<List<InvestRepayCustomize>>(investableBorrowService.searchInvestRepaysListNew(bean));
	}

}
