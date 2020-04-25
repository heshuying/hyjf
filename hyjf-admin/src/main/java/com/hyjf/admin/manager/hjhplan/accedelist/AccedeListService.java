package com.hyjf.admin.manager.hjhplan.accedelist;

import com.hyjf.admin.BaseService;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.customize.admin.AdminPlanAccedeListCustomize;

import java.util.List;

/**
 * 汇计划加入明细Service
 * 
 * @ClassName AccedeListService
 * @author LIBIN
 * @date 2017年8月16日 上午9:47:35
 */
public interface AccedeListService extends BaseService {
	
	/**
	 * 检索加入明细件数
	 * 
	 * @Title countAccedeRecord
	 * @param form
	 * @return
	 */
	int countAccedeRecord(AccedeListBean form);
	
	/**
	 * 检索加入明细列表
	 * 
	 * @Title selectAccedeRecordList
	 * @param form
	 * @return
	 */
	List<AdminPlanAccedeListCustomize> selectAccedeRecordList(AccedeListBean form);
	
	/**
	 * EMAIL入力后发送协议
	 * @author pcc
	 * @param userid
	 * @param planOrderId
	 * @param debtPlanNid
	 */
	String resendMessageAction(String userid, String planOrderId, String debtPlanNid,String sendEmail);
	/**
	 * 检索加入明细列表导出
	 * 
	 * @Title selectAccedeRecordList
	 * @param form
	 * @return
	 */
	List<AdminPlanAccedeListCustomize> selectAccedeRecordListExport(AccedeListBean form);
	/**
	 * 金额值合计
	 * 
	 * @Title sumAccedeRecord
	 * @param param
	 * @return
	 */
	AdminPlanAccedeListCustomize sumAccedeRecord(AccedeListBean form);

	/**
	 * 根据用户ID,计划加入订单号查询用户加入记录
	 * @param userId
	 * @param planOrderId
	 * @return
	 */
	HjhAccede selectAccedeRecord(String userId, String planOrderId);
	
	/**
	 * 根据计划加入订单号查询用户加入记录
	 * @param planOrderId
	 * @return
	 */
	HjhAccede searchHjhAccedeByOrderId(String planOrderId);
	
	/**
	 * 根据计划编号查询计划
	 * @param planNid
	 * @return
	 */
	HjhPlan searchHjhPlanByPlanNid(String planNid);
	
	/**
	 * 发送法大大新汇计划出借服务协议
	 * 发送法大大新汇计划智投服务协议
	 * @author pcc
	 * @param userid
	 * @param planOrderId
	 * @param debtPlanNid
	 */
	String sendFddHjhServiceAgrm(String userid, String planOrderId, String debtPlanNid,String sendEmail);
}
