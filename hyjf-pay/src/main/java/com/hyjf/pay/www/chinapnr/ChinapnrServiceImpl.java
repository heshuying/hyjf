package com.hyjf.pay.www.chinapnr;

import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogExample;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.mybatis.model.auto.ChinapnrLog;
import com.hyjf.mybatis.model.auto.ChinapnrSendlogWithBLOBs;
import com.hyjf.pay.lib.PnrApiBean;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;

@Service
public class ChinapnrServiceImpl extends CustomizeMapper implements ChinapnrService {

	/**
	 * 更新检证日志
	 *
	 * @return
	 */
	@Override
	public boolean updateChinapnrExclusiveLog(ChinapnrExclusiveLogWithBLOBs record) {
		return chinapnrExclusiveLogMapper.updateByPrimaryKeySelective(record) > 0 ? true : false;
	}

	/**
	 * 查询检证日志
	 *
	 * @return
	 */
	@Override
	public ChinapnrExclusiveLogWithBLOBs selectChinapnrExclusiveLog(long id) {
		return chinapnrExclusiveLogMapper.selectByPrimaryKey(id);
	}

	/**
	 * 
	 * 查询检证日志
	 * 
	 * @author liuyang
	 * @param orderId
	 * @return
	 */
	@Override
	public ChinapnrExclusiveLogWithBLOBs selectChinapnrExclusiveLogByOrderId(String orderId) {
		ChinapnrExclusiveLogExample example = new ChinapnrExclusiveLogExample();
		ChinapnrExclusiveLogExample.Criteria cra = example.createCriteria();
		cra.andOrdidEqualTo(orderId);
		List<ChinapnrExclusiveLogWithBLOBs> result = chinapnrExclusiveLogMapper.selectByExampleWithBLOBs(example);
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * 写入返回日志
	 *
	 * @return
	 */
	@Override
	public int insertChinapnrLog(ChinapnrLog chinapnrLog) {
		return chinapnrLogMapper.insertSelective(chinapnrLog);
	}

	@Override
	public long insertChinapnrExclusiveLog(ChinapnrBean bean, String methodName) {
		// 发送前插入状态记录
		String nowTime = GetDate.getServerDateTime(9, new Date());
		ChinapnrExclusiveLogWithBLOBs exclusiveLog = new ChinapnrExclusiveLogWithBLOBs();
		exclusiveLog.setCmdid(bean.get(ChinaPnrConstant.PARAM_CMDID));
		exclusiveLog.setResptype(bean.get(ChinaPnrConstant.PARAM_CMDID));
		exclusiveLog.setOrdid(bean.get(ChinaPnrConstant.PARAM_ORDID));
		exclusiveLog.setContent(bean.getJson().replace("&amp;", "&"));
		exclusiveLog.setStatus(ChinaPnrConstant.STATUS_SENDING);
		exclusiveLog.setType(bean.getLogType());
		exclusiveLog.setDelFlag(CustomConstants.FLAG_NORMAL);
		exclusiveLog.setCreatetime(nowTime);
		exclusiveLog.setUpdatetime(nowTime);
		exclusiveLog.setCreateuser(methodName);
		exclusiveLog.setUpdateuser(methodName);
		boolean flag = chinapnrExclusiveLogMapper.insertSelective(exclusiveLog) > 0 ? true : false;
		if (flag) {
			return exclusiveLog.getId();
		} else {
			return 0;
		}
	}

	@Override
	public boolean insertChinapnrSendLog(ChinapnrBean bean, PnrApiBean pnrApiBean) {
		int nowTime = GetDate.getNowTime10();
		// 发送前插入日志记录
		ChinapnrSendlogWithBLOBs sendlog = new ChinapnrSendlogWithBLOBs();
		sendlog.setOrdid(bean.get(ChinaPnrConstant.PARAM_ORDID));
		sendlog.setBorrowNid(bean.get(ChinaPnrConstant.PARAM_BORROWNID));
		sendlog.setOrddate(bean.get(ChinaPnrConstant.PARAM_ORDDATE));
		sendlog.setUserId(bean.getLogUserId());
		sendlog.setMsgType(bean.get(ChinaPnrConstant.PARAM_CMDID));
		sendlog.setMsgdata(bean.get(ChinaPnrConstant.PARAM_MSGDATA));
		sendlog.setChkvalue(bean.get(ChinaPnrConstant.PARAM_CHKVALUE));
		sendlog.setContent(pnrApiBean.getJson());
		sendlog.setRemark(bean.get(ChinaPnrConstant.PARAM_REMARK));
		sendlog.setClient(GetterUtil.getInteger(bean.get(ChinaPnrConstant.PARAM_CLIENT)));
		sendlog.setCreateTime(nowTime);
		boolean flag = chinapnrSendlogMapper.insertSelective(sendlog) > 0 ? true : false;
		return flag;
	}

	@Override
	public boolean insertChinapnrLog(ChinapnrBean bean, int isBig) {
		ChinapnrLog chinapnrLog = new ChinapnrLog();
		chinapnrLog.setIsbg(isBig);
		chinapnrLog.setUserId(bean.getInteger(ChinaPnrConstant.PARAM_USRID));
		chinapnrLog.setOrdid(bean.getOrdId());
		chinapnrLog.setBorrowNid(bean.getLogBorrowNid());
		chinapnrLog.setRespCode(bean.getRespCode());
		chinapnrLog.setRespDesc(bean.getRespDesc());
		chinapnrLog.setMsgType(bean.getCmdId());
		chinapnrLog.setRespType(bean.getRespType());
		chinapnrLog.setMsgdata(bean.getJson());
		chinapnrLog.setTrxid(bean.getTrxId());
		chinapnrLog.setAddtime(GetDate.getServerDateTime(8, new Date()));
		chinapnrLog.setRemark(bean.getLogRemark());
		chinapnrLog.setIp(bean.getLogIp());
		boolean flag = chinapnrLogMapper.insertSelective(chinapnrLog) > 0 ? true : false;
		return flag;
	}

	@Override
	public boolean updateChinapnrExclusiveLog(ChinapnrBean bean, ChinapnrExclusiveLogWithBLOBs record, String methodName, String status, String remark) {
		// 更新状态记录
		String nowTime = GetDate.getServerDateTime(9, new Date());
		ChinapnrExclusiveLogWithBLOBs exclusiveLog = new ChinapnrExclusiveLogWithBLOBs();
		exclusiveLog.setStatus(status);
		exclusiveLog.setResult(bean.getJson());
		exclusiveLog.setRemark(remark);
		exclusiveLog.setCmdid(bean.getCmdId());
		exclusiveLog.setResptype(bean.getRespType());
		exclusiveLog.setRespcode(bean.getRespCode());
		exclusiveLog.setUpdatetime(nowTime);
		exclusiveLog.setUpdateuser(methodName);
		ChinapnrExclusiveLogExample chinapnrExample = new ChinapnrExclusiveLogExample();
		chinapnrExample.createCriteria().andUpdatetimeEqualTo(record.getUpdatetime()).andIdEqualTo(record.getId());
		boolean flag = chinapnrExclusiveLogMapper.updateByExampleSelective(exclusiveLog, chinapnrExample) > 0 ? true : false;
		return flag;
	}

}
