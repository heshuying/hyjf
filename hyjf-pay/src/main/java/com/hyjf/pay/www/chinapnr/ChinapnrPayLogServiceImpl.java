package com.hyjf.pay.www.chinapnr;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetterUtil;
import com.hyjf.mongo.dao.ChinapnrExclusiveLogDao;
import com.hyjf.mongo.dao.ChinapnrLogDao;
import com.hyjf.mongo.dao.ChinapnrSendLogDao;
import com.hyjf.mongo.entity.ChinapnrExclusiveLog;
import com.hyjf.mongo.entity.ChinapnrLog;
import com.hyjf.mongo.entity.ChinapnrSendlog;
import com.hyjf.pay.lib.PnrApiBean;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;
import com.hyjf.pay.lib.chinapnr.util.ChinaPnrConstant;

@Service
public class ChinapnrPayLogServiceImpl implements ChinapnrPayLogService {

	@Autowired
	private ChinapnrLogDao chinapnrLogDao;

	@Autowired
	private ChinapnrSendLogDao chinapnrSendLogDao;

	@Autowired
	private ChinapnrExclusiveLogDao chinapnrExclusiveLogDao;
	
	private static final String SENDLOG = "chinapnrsendlog";
	private static final String BACKLOG = "chinapnrlog";
	private static final String EXCLUSENDLOG = "chinapnrexclusivelog";

	/**
	 * 更新检证日志
	 *
	 * @return
	 */
	@Override
	public void updateChinapnrExclusiveLog(ChinapnrExclusiveLog record) {
		
		ChinapnrExclusiveLog exclusiveLogInDb = this.chinapnrExclusiveLogDao.findOne(
				Query.query(Criteria.where("ordid").is(record.getOrdid())), EXCLUSENDLOG);
		// 拼接相应的参数
		record.setId(exclusiveLogInDb.getId());
		
		this.chinapnrExclusiveLogDao.save(record, EXCLUSENDLOG);
	}

	/**
	 * 查询检证日志
	 *
	 * @return
	 */
	@Override
	public ChinapnrExclusiveLog selectChinapnrExclusiveLogByOrderId(String orderId) {
		Query query = new Query();
		Criteria criteria = Criteria.where("ordid").is(orderId);
		query.addCriteria(criteria);
		return this.chinapnrExclusiveLogDao.findOne(query, EXCLUSENDLOG);
	}

	/**
	 * 查询检证日志
	 *
	 * @return
	 */
	@Override
	public ChinapnrExclusiveLog selectChinapnrExclusiveLog(String id) {
		Query query = new Query();
		Criteria criteria = Criteria.where("_id").is(id);
		query.addCriteria(criteria);
		return this.chinapnrExclusiveLogDao.findOne(query, EXCLUSENDLOG);
	}

	/**
	 * 写入返回日志
	 *
	 * @return
	 */
	@Override
	public String insertChinapnrLog(ChinapnrLog chinapnrLog) {
        this.chinapnrLogDao.insert(chinapnrLog, BACKLOG);
		
		return chinapnrLog.getId();
	}

	@Override
	public String insertChinapnrExclusiveLog(ChinapnrBean bean, String methodName) {
		// 发送前插入状态记录
		String nowTime = GetDate.getServerDateTime(9, new Date());
		ChinapnrExclusiveLog exclusiveLog = new ChinapnrExclusiveLog();
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
		this.chinapnrExclusiveLogDao.insert(exclusiveLog, EXCLUSENDLOG);
		
		return exclusiveLog.getId();
	}

	@Override
	public String insertChinapnrSendLog(ChinapnrBean bean, PnrApiBean pnrApiBean) {
		int nowTime = GetDate.getNowTime10();
		// 发送前插入日志记录
		ChinapnrSendlog sendlog = new ChinapnrSendlog();
		sendlog.setOrdid(bean.get(ChinaPnrConstant.PARAM_ORDID));
		sendlog.setBorrowNid(bean.get(ChinaPnrConstant.PARAM_BORROWNID));
		sendlog.setOrddate(bean.get(ChinaPnrConstant.PARAM_ORDDATE));
		sendlog.setUserId(bean.getLogUserId());
		sendlog.setMsgType(bean.get(ChinaPnrConstant.PARAM_CMDID));
		sendlog.setMsgdata(bean.get(ChinaPnrConstant.PARAM_MSGDATA));
		sendlog.setChkvalue(bean.get(ChinaPnrConstant.PARAM_CHKVALUE));
		sendlog.setContent(pnrApiBean.getJsonMap());
		sendlog.setRemark(bean.get(ChinaPnrConstant.PARAM_REMARK));
		sendlog.setClient(GetterUtil.getInteger(bean.get(ChinaPnrConstant.PARAM_CLIENT)));
		sendlog.setCreateTime(nowTime);
		this.chinapnrSendLogDao.insert(sendlog, SENDLOG);
		return sendlog.getId();
	}

	@Override
	public String insertChinapnrLog(ChinapnrBean bean, int isBig) {
		ChinapnrLog chinapnrLog = new ChinapnrLog();
		chinapnrLog.setIsbg(isBig);
		chinapnrLog.setUserId(bean.getInteger(ChinaPnrConstant.PARAM_USRID));
		chinapnrLog.setOrdid(bean.getOrdId());
		chinapnrLog.setBorrowNid(bean.getLogBorrowNid());
		chinapnrLog.setRespCode(bean.getRespCode());
		chinapnrLog.setRespDesc(bean.getRespDesc());
		chinapnrLog.setMsgType(bean.getCmdId());
		chinapnrLog.setRespType(bean.getRespType());
		chinapnrLog.setMsgdata(bean.getJsonMap());
		chinapnrLog.setTrxid(bean.getTrxId());
		chinapnrLog.setAddtime(GetDate.getServerDateTime(8, new Date()));
		chinapnrLog.setRemark(bean.getLogRemark());
		chinapnrLog.setIp(bean.getLogIp());
		this.chinapnrLogDao.insert(chinapnrLog, BACKLOG);
		return chinapnrLog.getId();
	}

	@Override
	public void updateChinapnrExclusiveLog(ChinapnrBean bean, ChinapnrExclusiveLog record, String methodName, String status, String remark) {
		
		ChinapnrExclusiveLog exclusiveLogInDb = this.chinapnrExclusiveLogDao.findOne(
				Query.query(Criteria.where("ordid").is(record.getOrdid())), EXCLUSENDLOG);
		
		// 更新状态记录
		String nowTime = GetDate.getServerDateTime(9, new Date());
		ChinapnrExclusiveLog exclusiveLog = new ChinapnrExclusiveLog();
		exclusiveLog.setStatus(status);
		exclusiveLog.setResult(bean.getJson());
		exclusiveLog.setRemark(remark);
		exclusiveLog.setCmdid(bean.getCmdId());
		exclusiveLog.setResptype(bean.getRespType());
		exclusiveLog.setRespcode(bean.getRespCode());
		exclusiveLog.setUpdatetime(nowTime);
		exclusiveLog.setUpdateuser(methodName);
		// 拼接相应的参数
		exclusiveLog.setId(exclusiveLogInDb.getId());
		
		
		this.chinapnrExclusiveLogDao.save(exclusiveLog, EXCLUSENDLOG);
	}
	
}
