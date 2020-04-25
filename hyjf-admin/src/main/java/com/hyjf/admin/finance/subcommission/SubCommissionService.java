package com.hyjf.admin.finance.subcommission;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.SubCommission;
import com.hyjf.mybatis.model.customize.SubCommissionListConfigCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;

import java.util.List;

import org.springframework.web.servlet.ModelAndView;

/**
 * 账户分佣Service
 */
public interface SubCommissionService extends BaseService {
	/**
	 * 检索分账记录件数
	 *
	 * @param form
	 * @return
	 */
	Integer countSubCommissionList(SubCommissionBean form);

	/**
	 * 检索分账记录列表
	 *
	 * @param form
	 * @param offset
	 * @param limit
	 * @return
	 */
	List<SubCommission> searchSubCommissionList(SubCommissionBean form, int offset, int limit);

	/**
	 * 请求前插入分佣记录表
	 *
	 * @param bean
	 * @param form
	 * @return
	 */
	boolean insetSubCommissionLog(BankCallBean bean, SubCommissionBean form);

	/**
	 * 更新失败订单状态
	 *
	 * @param bean
	 */
	void updateSubCommission(BankCallBean bean);

	/**
	 * 分账成功后,后续账户信息操作
	 * 
	 * @param resultBean
	 * @param form
	 * @return
	 */
	boolean updateSubCommissionSuccess(BankCallBean resultBean, SubCommissionBean form);

	/**
	 * 根据订单号,用户ID查询交易明细是否存在
	 * 
	 * @param orderId
	 * @param userId
	 * @return
	 */
	boolean accountListByOrderId(String orderId);

	/**
	 * 检验参数
	 * 
	 * @param modelAndView
	 * @param form
	 */
	void checkTransferParam(ModelAndView modelAndView, SubCommissionBean form);
	/**
	 * 获取用户名
	 * @param userId
	 * @param nid
	 * @return
	 */
	public List<SubCommissionListConfigCustomize> users();
}
