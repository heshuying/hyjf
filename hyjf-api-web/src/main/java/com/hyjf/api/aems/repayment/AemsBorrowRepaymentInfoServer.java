package com.hyjf.api.aems.repayment;

import com.hyjf.api.aems.common.AemsCommonSvrChkService;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.enums.utils.MsgEnum;
import com.hyjf.common.result.ResultApiBean;
import com.hyjf.common.validator.CheckUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.apiweb.ApiBorrowRepaymentInfoCustomize;
import com.hyjf.mybatis.model.customize.apiweb.ApiBorrowRepaymentInfoCustomizeRe;
import com.hyjf.plan.repay.BatchHjhBorrowRepayApiService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author jijun 20180911
 * 还款明细查询接口
 */
@RestController
@RequestMapping(value = AemsBorrowRepaymentInfoDefine.REQUEST_MAPPING)
public class AemsBorrowRepaymentInfoServer extends BaseController {
	@Autowired
	private AemsCommonSvrChkService commonSvrChkService;

	@Autowired
	private AemsBorrowRepaymentInfoService borrowRepaymentInfoService;

	/**
	 * 获取还款明细查询-根据资产编号、机构编号  根据资产编号和机构编号从hyjf_hjh_plan_asset获取项目编号
	 * @param bean
	 * @return
	 */
    @RequestMapping(method = RequestMethod.POST,value = AemsBorrowRepaymentInfoDefine.BORROW_LIST_ACTION)
    public ResultApiBean<List<ApiBorrowRepaymentInfoCustomizeRe>> borrowList(@RequestBody AemsBorrowRepaymentInfoBean bean){
		// 验证
    	// 共通验证
    	commonSvrChkService.checkRequired(bean);
    	// 验证资产编号
    	CheckUtil.check(Validator.isNotNull(bean.getAssetId()), MsgEnum.STATUS_ZC000018);
    	// 分页验证
    	commonSvrChkService.checkLimit(bean.getLimitStart(), bean.getLimitEnd());
//		// 验签
    	CheckUtil.check(this.AEMSVerifyRequestSign(bean, AemsBorrowRepaymentInfoDefine.REQUEST_MAPPING+AemsBorrowRepaymentInfoDefine.BORROW_LIST_ACTION),
                MsgEnum.ERR_SIGN);

    	// 返回查询结果
		ApiBorrowRepaymentInfoCustomize borrowRepaymentInfoCustomize = new ApiBorrowRepaymentInfoCustomize();
        BeanUtils.copyProperties(bean, borrowRepaymentInfoCustomize);
    	return new ResultApiBean<List<ApiBorrowRepaymentInfoCustomizeRe>>(borrowRepaymentInfoService.selectBorrowRepaymentInfoList(borrowRepaymentInfoCustomize));
    }
}
