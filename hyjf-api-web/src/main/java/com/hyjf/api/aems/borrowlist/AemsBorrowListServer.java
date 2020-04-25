package com.hyjf.api.aems.borrowlist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.hyjf.api.aems.common.AemsCommonSvrChkService;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.enums.utils.MsgEnum;
import com.hyjf.common.result.ResultApiBean;
import com.hyjf.common.validator.CheckUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.web.ApiProjectListCustomize;

/**
 * aems标的列表查询
 * jijun 20180907
 */
@RestController
@RequestMapping(value = AemsBorrowListDefine.REQUEST_MAPPING)
public class AemsBorrowListServer extends BaseController {
	@Autowired
	private AemsCommonSvrChkService commonSvrChkService;
	
	@Autowired
	private AemsBorrowListService investableBorrowService;
	
	/**
	 * 获取可投标的信息
	 * @param bean
	 * @return
	 */
    @RequestMapping(method = RequestMethod.POST,value = AemsBorrowListDefine.BORROW_LIST_ACTION)
    public ResultApiBean<List<ApiProjectListCustomize>> borrowList(@RequestBody AemsBorrowListRequestBean bean){
		// 验证
    	// 共通验证
    	commonSvrChkService.checkRequired(bean);
    	// 分页验证
    	commonSvrChkService.checkLimit(bean.getLimitStart(), bean.getLimitEnd());
    	// 标的状态验证
		CheckUtil.check(Validator.isNotNull(bean.getBorrowStatus()), MsgEnum.STATUS_CE000001);
    	// 标的状态=2投资中 验证
		CheckUtil.check(bean.getBorrowStatus().equals("2"), MsgEnum.ERR_OBJECT_VALUE, "borrowStatus");
		
//		// 验签
		CheckUtil.check(this.AEMSVerifyRequestSign(bean, AemsBorrowListDefine.REQUEST_MAPPING+AemsBorrowListDefine.BORROW_LIST_ACTION),
				MsgEnum.ERR_SIGN);

    	// 返回查询结果
    	return new ResultApiBean<List<ApiProjectListCustomize>>(investableBorrowService.searchProjectListNew(bean));
    }

}
