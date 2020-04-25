package com.hyjf.api.server.borrow.borrowlist;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hyjf.api.server.common.CommonSvrChkService;
import com.hyjf.api.web.BaseController;
import com.hyjf.common.enums.utils.MsgEnum;
import com.hyjf.common.result.ResultApiBean;
import com.hyjf.common.validator.CheckUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.customize.web.ApiProjectListCustomize;

/**
 * @author liubin
 */
@RestController
@RequestMapping(value = BorrowListDefine.REQUEST_MAPPING)
public class BorrowListServer extends BaseController {
	@Autowired
	private CommonSvrChkService commonSvrChkService;
	
	@Autowired
	private BorrowListService investableBorrowService;
	
	/**
	 * 获取可投标的信息
	 * @param bean
	 * @return
	 */
    @RequestMapping(value = BorrowListDefine.BORROW_LIST_ACTION)
    public ResultApiBean<List<ApiProjectListCustomize>> borrowList(@RequestBody BorrowListRequestBean bean){
		// 验证
    	// 共通验证
    	commonSvrChkService.checkRequired(bean);
    	// 分页验证
    	commonSvrChkService.checkLimit(bean.getLimitStart(), bean.getLimitEnd());
    	// 标的状态验证
		CheckUtil.check(Validator.isNotNull(bean.getBorrowStatus()), MsgEnum.STATUS_CE000001);
    	// 标的状态=2出借中 验证
		CheckUtil.check(bean.getBorrowStatus().equals("2"), MsgEnum.ERR_OBJECT_VALUE, "borrowStatus");
		
//		// 验签
		CheckUtil.check(this.verifyRequestSign(bean, BorrowListDefine.BORROW_LIST_ACTION),
				MsgEnum.ERR_SIGN);

    	// 返回查询结果
    	return new ResultApiBean<List<ApiProjectListCustomize>>(investableBorrowService.searchProjectListNew(bean));
    }

}
