package com.hyjf.admin.exception.freezexception;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.mybatis.model.auto.FreezeHistory;
import com.hyjf.mybatis.model.auto.FreezeHistoryExample;
import com.hyjf.mybatis.model.auto.FreezeList;
import com.hyjf.mybatis.model.auto.FreezeListExample;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;
import com.hyjf.pay.lib.chinapnr.util.ChinapnrUtil;

@Service
public class FreezExceptionServiceImpl extends BaseServiceImpl implements FreezExceptionService {

	/**
	 * 复审记录 总数COUNT
	 * 
	 * @param record
	 * @return
	 * @author Administrator
	 */

	@Override
	public Integer queryCount(FreezExceptionBean record) {
		FreezeHistoryExample example = this.createExample(record);
		return this.freezeHistoryMapper.countByExample(example);

	}

	/**
	 * 复审记录
	 * 
	 * @param record
	 * @return
	 * @author Administrator
	 */

	@Override
	public List<FreezeHistory> queryRecordList(FreezExceptionBean record) {
		FreezeHistoryExample example = this.createExample(record);
		example.setLimitStart(record.getLimitStart());
		example.setLimitEnd(record.getLimitEnd());

		example.setOrderByClause(" freeze_time DESC ");
		return this.freezeHistoryMapper.selectByExample(example);

	}

	/**
	 * 
	 * @param record
	 * @return
	 */
	private FreezeHistoryExample createExample(FreezExceptionBean record) {
		FreezeHistoryExample example = new FreezeHistoryExample();
		FreezeHistoryExample.Criteria cra = example.createCriteria();

		if (StringUtils.isNotEmpty(record.getTrxIdSrch())) {
			cra.andTrxIdEqualTo(record.getTrxIdSrch());
		}

		if (StringUtils.isNotEmpty(record.getTimeStartSrch())) {
			Date date = GetDate.stringToDate(GetDate.getDayStart(record.getTimeStartSrch()));
			Integer time = Integer.valueOf(String.valueOf(date.getTime() / 1000));
			cra.andFreezeTimeGreaterThanOrEqualTo(time);
		}

		if (StringUtils.isNotEmpty(record.getTimeEndSrch())) {
			Date date = GetDate.stringToDate(GetDate.getDayEnd(record.getTimeEndSrch()));
			Integer time = Integer.valueOf(String.valueOf(date.getTime() / 1000));
			cra.andFreezeTimeLessThanOrEqualTo(time);
		}

		example.setOrderByClause(" freeze_time DESC ");

		return example;
	}

	/**
	 * 解冻
	 * 
	 * @param record
	 */
	@Override
	public String updateFreezRecord(FreezExceptionBean record) {

		String trxId = record.getTrxId();
		ChinapnrBean unFreezeBean = usrUnFreeze(trxId);
		String respCode = unFreezeBean == null ? "" : unFreezeBean.getRespCode();
		String respDesc = unFreezeBean == null ? "" : unFreezeBean.getRespDesc();
		// 调用接口失败时(000 或 107 以外)
		if (!ChinaPnrConstant.RESPCODE_SUCCESS.equals(respCode) && !ChinaPnrConstant.RESPCODE_REPEAT_DEAL.equals(respCode)) {
			return "调用资金（货款）解冻接口失败。\r\n解冻单号：" + trxId + "\r\n汇付消息：" + respDesc;
		}

		FreezeHistoryExample example = new FreezeHistoryExample();
		FreezeHistoryExample.Criteria cra = example.createCriteria();
		cra.andTrxIdEqualTo(trxId);

		List<FreezeHistory> list = this.freezeHistoryMapper.selectByExample(example);

		if (list != null && list.size() > 0) {
			return "该解冻单号已经解冻！";
		}

		FreezeHistory freezeHistory = new FreezeHistory();
		freezeHistory.setTrxId(trxId);
		freezeHistory.setNotes(record.getNotes());
		freezeHistory.setFreezeUser(ShiroUtil.getLoginUsername());
		freezeHistory.setFreezeTime(GetDate.getMyTimeInMillis());

		this.freezeHistoryMapper.insert(freezeHistory);

		return "";
	}

	/**
	 * 资金（货款）解冻(调用汇付天下接口)
	 *
	 * @param trxId
	 * @return
	 */
	private ChinapnrBean usrUnFreeze(String trxId) {

		// 调用汇付接口
		ChinapnrBean bean = new ChinapnrBean();
		bean.setVersion(ChinaPnrConstant.VERSION_10); // 版本号(必须)
		bean.setCmdId(ChinaPnrConstant.CMDID_USR_UN_FREEZE); // 消息类型(必须)
		bean.setOrdId(GetOrderIdUtils.getOrderId2(0)); // 订单号(必须)
		bean.setOrdDate(GetOrderIdUtils.getOrderDate()); // 订单日期(必须)
		bean.setTrxId(trxId); // 本平台交易唯一标识(必须)
		bean.setBgRetUrl(ChinapnrUtil.getBgRetUrl()); // 商户后台应答地址(必须)

		// 写log用参数
		bean.setLogUserId(0);
		bean.setLogRemark("资金（货款）解冻"); // 备注
		bean.setLogClient("0"); // PC

		ChinapnrBean chinapnrBean = ChinapnrUtil.callApiBg(bean);

		if (chinapnrBean == null) {
			return null;
		}

		return chinapnrBean;
	}

	/**
	 * 解冻订单号在本库中是否存在
	 * 
	 * @param trxId
	 * @return
	 */
	public boolean selsectTrxIdIsExists(String trxId) {
		FreezeListExample example = new FreezeListExample();
		FreezeListExample.Criteria cra = example.createCriteria();
		cra.andTrxidEqualTo(trxId);

		List<FreezeList> freezeList = this.freezeListMapper.selectByExample(example);

		if (freezeList != null && freezeList.size() > 0) {
			return true;
		}

		return false;
	}

}
