package com.hyjf.pay.www.anrong;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.customize.CustomizeMapper;
import com.hyjf.mybatis.model.auto.AnrongSendLogWithBLOBs;
import com.hyjf.pay.lib.anrong.bean.AnRongBean;

@Service
public class AnRongCallServiceImpl extends CustomizeMapper implements AnRongCallService {

    @Override
    public Long insertAnRongSendLog(AnRongBean bean) {
        int nowTime = GetDate.getNowTime10();
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        String txDate = sf.format(new Date());
        sf = new SimpleDateFormat("HHmmss");
        String txTime = sf.format(new Date());
        
        AnrongSendLogWithBLOBs anrongSendLog = new AnrongSendLogWithBLOBs();
        anrongSendLog.setClient(bean.getLogClient());
        anrongSendLog.setCmdid(bean.getTxCode());
        anrongSendLog.setOrdid(bean.getLoanId());
        // TXCODE 不在发送参数里面
        bean.setTxCode(null);
        Map<String, String> content = bean.getAllParams();
        content.remove(AnRongCallDefine.TXCODE);
        anrongSendLog.setContent(JSON.toJSONString(content, true).replace("&amp;", "&"));
        anrongSendLog.setStatus(AnRongCallDefine.STATUS_SENDED);
        anrongSendLog.setDelFlag(AnRongCallDefine.FLAG_NORMAL);
        anrongSendLog.setTxDate(Integer.parseInt(txDate));
        anrongSendLog.setTxTime(Integer.parseInt(txTime));
        anrongSendLog.setCreateuser(bean.getLogUserId());
        anrongSendLog.setCreatetime(String.valueOf(nowTime));
        anrongSendLog.setUpdateuser(bean.getLogUserId());
        anrongSendLog.setUpdatetime(String.valueOf(nowTime));
        anrongSendLogMapper.insertSelective(anrongSendLog);
        return anrongSendLog.getId();

    }

    @Override
    public boolean updateAnRongSendLog(Long id, String result) {
        int nowTime = GetDate.getNowTime10();
        // 拼接相应的参数
        AnrongSendLogWithBLOBs log = new AnrongSendLogWithBLOBs();
        log.setId(id);
        log.setResult(result);
        log.setUpdatetime(String.valueOf(nowTime));
        
        boolean flag = anrongSendLogMapper.updateByPrimaryKeySelective(log)  > 0 ? true : false;
        return flag;
    }



}
