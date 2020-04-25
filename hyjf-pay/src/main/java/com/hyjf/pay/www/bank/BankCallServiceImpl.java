package com.hyjf.pay.www.bank;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogExample;
import com.hyjf.mybatis.model.auto.ChinapnrExclusiveLogWithBLOBs;
import com.hyjf.mybatis.model.auto.ChinapnrLog;
import com.hyjf.mybatis.model.auto.ChinapnrSendlogWithBLOBs;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.bean.BankCallPnrApiBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;

@Service
public class BankCallServiceImpl extends CustomizeMapper implements BankCallService {

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

	@Override
	public boolean insertChinapnrSendLog(BankCallPnrApiBean pnrApiBean, BankCallBean bean) {
		int nowTime = GetDate.getNowTime10();
		ChinapnrSendlogWithBLOBs sendlog = new ChinapnrSendlogWithBLOBs();
		sendlog.setOrdid(bean.getLogOrderId());
		sendlog.setOrddate(bean.getLogOrderDate());
		sendlog.setClient(bean.getLogClient());
		sendlog.setUserId(StringUtils.isNotEmpty(bean.getLogUserId()) ? Integer.valueOf(bean.getLogUserId()) : null);
		sendlog.setMsgType(bean.getTxCode());
		sendlog.setMsgdata(bean.get(BankCallConstant.PARAM_LOGMSGDATA));
		sendlog.setChkvalue(bean.get(BankCallConstant.PARAM_SIGN));
		sendlog.setContent(pnrApiBean.getJson());
		sendlog.setRemark(bean.getLogRemark());
		sendlog.setClient(GetterUtil.getInteger(bean.get(BankCallConstant.PARAM_LOGCLIENT)));
		sendlog.setCreateTime(nowTime);
		sendlog.setTxDate(StringUtils.isNotEmpty(bean.getTxDate()) ? Integer.valueOf(bean.getTxDate()) : null);
		sendlog.setTxTime(StringUtils.isNotEmpty(bean.getTxTime()) ? Integer.valueOf(bean.getTxTime()) : null);
		sendlog.setSeqNo(bean.getSeqNo());
		boolean sendFlag = chinapnrSendlogMapper.insertSelective(sendlog) > 0 ? true : false;
		return sendFlag;
	}
	
	@Override
	public Long insertChinapnrExclusiveLog(BankCallBean bean) {
		int nowTime = GetDate.getNowTime10();
		ChinapnrExclusiveLogWithBLOBs exclusiveLog = new ChinapnrExclusiveLogWithBLOBs();
		exclusiveLog.setClient(bean.getLogClient());
		exclusiveLog.setCmdid(bean.getTxCode());
		exclusiveLog.setOrdid(bean.getLogOrderId());
		exclusiveLog.setContent(bean.getJson().replace("&amp;", "&"));
		exclusiveLog.setStatus(BankCallConstant.STATUS_SENDING);
		exclusiveLog.setType(bean.getLogType());
		exclusiveLog.setDelFlag(CustomConstants.FLAG_NORMAL);
		exclusiveLog.setTxDate(StringUtils.isNotEmpty(bean.getTxDate()) ? Integer.valueOf(bean.getTxDate()) : null);
		exclusiveLog.setTxTime(StringUtils.isNotEmpty(bean.getTxTime()) ? Integer.valueOf(bean.getTxTime()) : null);
		exclusiveLog.setSeqNo(bean.getSeqNo());
		exclusiveLog.setCreateuser(bean.getLogUserId());
		exclusiveLog.setCreatetime(String.valueOf(nowTime));
		exclusiveLog.setUpdateuser(bean.getLogUserId());
		exclusiveLog.setUpdatetime(String.valueOf(nowTime));
		chinapnrExclusiveLogMapper.insertSelective(exclusiveLog);
		return exclusiveLog.getId();
	}
	
	@Override
	public boolean insertChinapnrLog(BankCallBean bean, int returnType) {
		String nowTime = GetDate.getServerDateTime(8, new Date());
		ChinapnrLog chinapnrLog = new ChinapnrLog();
		chinapnrLog.setIsbg(returnType);
		chinapnrLog.setOrdid(bean.getLogOrderId());
		chinapnrLog.setUserId(StringUtils.isNotEmpty(bean.getLogUserId()) ? Integer.parseInt(bean.getLogUserId()) : null);
		chinapnrLog.setClient(bean.getLogClient());
		chinapnrLog.setMsgType(bean.getTxCode());
		chinapnrLog.setMsgdata(bean.getJson());
		chinapnrLog.setTxDate(StringUtils.isNotEmpty(bean.getTxDate()) ? Integer.valueOf(bean.getTxDate()) : null);
		chinapnrLog.setTxTime(StringUtils.isNotEmpty(bean.getTxTime()) ? Integer.valueOf(bean.getTxTime()) : null);
		chinapnrLog.setSeqNo(bean.getSeqNo());
		chinapnrLog.setRemark(bean.getLogRemark());
		chinapnrLog.setIp(bean.getLogIp());
		chinapnrLog.setAddtime(nowTime);
		boolean chinapnrLogFlag = chinapnrLogMapper.insertSelective(chinapnrLog) > 0 ? true : false;
		return chinapnrLogFlag;
	}

	@Override
	public boolean updateChinapnrExclusiveLog(Long exclusiveId, BankCallBean bean, int nowTime) {
		// 拼接相应的参数
		ChinapnrExclusiveLogWithBLOBs exclusiveLog = new ChinapnrExclusiveLogWithBLOBs();
		exclusiveLog.setId(exclusiveId);
		exclusiveLog.setStatus(bean.getLogOrderStatus());
		exclusiveLog.setResult(bean.getJson());
		exclusiveLog.setRespcode(bean.getRetCode());
		exclusiveLog.setUpdatetime(String.valueOf(nowTime));
		exclusiveLog.setUpdateuser(bean.getLogUserId());
		// 初始化相应的查询条件
//		ChinapnrExclusiveLogExample chinapnrExample = new ChinapnrExclusiveLogExample();
//		chinapnrExample.createCriteria().andUpdatetimeEqualTo(record.getUpdatetime()).andIdEqualTo(record.getId());
//		boolean flag = chinapnrExclusiveLogMapper.updateByExampleSelective(exclusiveLog, chinapnrExample) > 0 ? true : false;
		
		boolean flag = chinapnrExclusiveLogMapper.updateByPrimaryKeySelective(exclusiveLog)  > 0 ? true : false;
		return flag;

	}

	@Override
	public boolean updateChinapnrExclusiveLog(Long exclusiveId, int status) {
	    ChinapnrExclusiveLogWithBLOBs exclusiveLog = new ChinapnrExclusiveLogWithBLOBs();
	    exclusiveLog.setId(exclusiveId);
	    exclusiveLog.setStatus(String.valueOf(status));
		boolean flag = this.chinapnrExclusiveLogMapper.updateByPrimaryKeySelective(exclusiveLog)>0?true:false;
		return flag;
	}

}
