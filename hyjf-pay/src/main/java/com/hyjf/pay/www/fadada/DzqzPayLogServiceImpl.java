package com.hyjf.pay.www.fadada;

import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.mongo.dao.DzqzReturnLogDao;
import com.hyjf.mongo.dao.DzqzSendLogDao;
import com.hyjf.mongo.entity.DzqzReturnLog;
import com.hyjf.mongo.entity.DzqzSendLog;
import com.hyjf.pay.lib.fadada.bean.DzqzCallBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.TreeMap;

@Service
public class DzqzPayLogServiceImpl implements DzqzPayLogService {


    private static final String SENDLOG = "dzqzsendlog";
    private static final String BACKLOG = "dzqzreturnlog";

    @Autowired
    private DzqzSendLogDao sendLogDao;
    @Autowired
    private DzqzReturnLogDao returnLogDao;

    @Override
    public void saveDzqzPaySendLog(DzqzCallBean bean) {

        DzqzSendLog sendLog = new DzqzSendLog();
        sendLog.setOrdid(bean.getLogordid());
        sendLog.setCreateTime(GetDate.getNowTime10());
        sendLog.setMsgType(bean.getTxCode());
        sendLog.setTxDate(bean.getTxDate());
        sendLog.setTxTime(bean.getTxTime());
        sendLog.setContent(bean.getAllParams());
        sendLogDao.insert(sendLog,this.SENDLOG);

    }

    @Override
    public void saveDzqzPayReturnLog(DzqzCallBean bean) {
        bean.setAllParams(new TreeMap<String, String>());
        bean.convert();
        DzqzReturnLog returnLog = new DzqzReturnLog();
        returnLog.setOrdid(bean.getLogordid());
        returnLog.setCreateTime(GetDate.getNowTime10());
        returnLog.setMsgType(bean.getTxCode());
        returnLog.setTxDate(bean.getTxDate());
        returnLog.setTxTime(bean.getTxTime());
        returnLog.setContent(bean.getAllParams());
        returnLogDao.insert(returnLog,this.BACKLOG);
    }
}
