package com.hyjf.bank.service.user.appiontment;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.hyjf.bank.service.BaseServiceImpl;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.AccountChinapnr;
import com.hyjf.mybatis.model.auto.DebtPlanAccedeExample;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;

@Service
public class AppointmentServiceImpl extends BaseServiceImpl implements AppointmentService {



	/**
	 * 
	 * @method: checkAppointmentStatus
	 * @description: 查看预约授权状态
	 * @return
	 * @param tenderUsrcustid
	 * @return
	 * @return: Map<String, Object>
	 * @throws Exception
	 * @mender: zhouxiaoshuai
	 * @date: 2016年7月26日 下午1:53:08
	 */
	@Override
	public Map<String, Object> checkAppointmentStatus(Integer usrId, int appointment) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 出借人的账户信息
		AccountChinapnr outCust = this.getAccountChinapnr(usrId);
		if (outCust == null) {
			map.put("appointmentFlag", false); //是否调用接口
			map.put("isError", "1");//开户信息不存在
			return map;
		}
		if (appointment == 2) {
			// 此笔加入是否已经完成 0出借中 1出借完成 2还款中 3还款完成
			DebtPlanAccedeExample example = new DebtPlanAccedeExample();
			DebtPlanAccedeExample.Criteria cri = example.createCriteria();
			cri.andUserIdEqualTo(usrId);
			cri.andStatusNotEqualTo(5);
			int pcount = debtPlanAccedeMapper.countByExample(example);
			if (pcount > 0) {
				map.put("appointmentFlag", false);
				map.put("isError", "2");//有申购中/锁定中的汇添金计划，暂时不能取消授权
				return map;
			}
		}
		// 调用预约授权查询接口
		BankCallBean queryTransStatBean = queryAppointmentStatus(outCust.getChinapnrUsrcustid());
		if (queryTransStatBean == null ) {
			map.put("appointmentFlag", false);
			map.put("isError", "3");//查询接口失败
			return map;
		}
		//返回码
		String queryRespCode = queryTransStatBean.getRetCode();
		System.out.print("调用自动出借授权查询接口返回码：" + queryRespCode);
		//返回成功码
		if(BankCallConstant.RESPCODE_SUCCESS.equals(queryRespCode)){
			//签约状态 0：未签约  1：已签约
			int authState = Integer.valueOf(queryTransStatBean.getState());
			if(appointment == 1 && authState == 0){
				map.put("appointmentFlag", true);
				map.put("isError", "0");//校验通过
				return map;
			}else if(appointment == 1 && authState == 1){
				map.put("appointmentFlag", false);
				map.put("isError", "0");//校验通过
				return map;
			}else if(appointment == 2 && authState == 0){
				map.put("appointmentFlag", false);
				map.put("isError", "0");//校验通过
				return map;
			}else if(appointment == 2 && authState == 1){
				map.put("appointmentFlag", true);
				map.put("orderId", queryTransStatBean.getOrderId());//签约订单id
				map.put("isError", "0");//校验通过
				return map;
			}
		}
		//失败
		map.put("appointmentFlag", false);
		map.put("isError", "4");//错误
		System.out.println("调用预约授权查询接口失败,[返回码："+ queryRespCode + "]");
		return map;
	}

	/**
	 * 更新预约状态
	 */
	@Override
	public boolean updateUserAuthStatus(String tenderPlanType, String appointment, String userId) {
		int authType = 0;
		if (tenderPlanType != null && tenderPlanType.equals("P")) {
			authType = 1;
		}
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("authType", authType);
		params.put("appointment", Integer.parseInt(appointment));
		params.put("userId", userId);
		int AuthFlag = webPandectCustomizeMapper.updateUserAuthStatus(params);
		int AppointmentFlag = webPandectCustomizeMapper.insertAppointmentAuthLog(params);
		if (AuthFlag > 0 && AppointmentFlag > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 调用预约授权查询接口
	 *
	 * @return
	 */
	private BankCallBean queryAppointmentStatus(Long Usrcustid) {
		// 调用预约授权查询接口
		BankCallBean bean = new BankCallBean();
		bean.setVersion(BankCallConstant.VERSION_10);// 接口版本号
		// 银行存管
		bean.setTxCode(BankCallMethodConstant.TXCODE_CREDIT_AUTH_QUERY);// 交易代码creditAuthQuery
		bean.setInstCode(PropUtils.getSystem(BankCallConstant.BANK_INSTCODE));// 机构代码
		bean.setBankCode(PropUtils.getSystem(BankCallConstant.BANK_BANKCODE));// 银行代码
		bean.setTxDate(GetOrderIdUtils.getTxDate());// 交易日期
		bean.setTxTime(GetOrderIdUtils.getTxTime());// 交易时间
		bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号6位
		bean.setChannel(BankCallConstant.CHANNEL_PC);// 交易渠道000001手机APP 000002网页  000003微信 000004柜面
		bean.setType(BankCallConstant.QUERY_TYPE_1);//1 自动投标签约
		bean.setAccountId(String.valueOf(Usrcustid));//客户号
		
		BankCallBean retBean = BankCallUtils.callApiBg(bean);
		if(retBean == null){
			System.out.println("调用预约授权查询接口失败![参数：" + bean.getAllParams() + "]");
			return null;
		}
		return retBean;
	}

}
