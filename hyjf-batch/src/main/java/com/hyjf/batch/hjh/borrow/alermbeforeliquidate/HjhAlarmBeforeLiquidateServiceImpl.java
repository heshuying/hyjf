package com.hyjf.batch.hjh.borrow.alermbeforeliquidate;

import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.mapper.customize.BorrowCustomizeMapper;
import com.hyjf.mybatis.messageprocesser.MailMessage;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.customize.BorrowCustomize;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * Created by Administrator on 2018/5/25.
 */
@Service
public class HjhAlarmBeforeLiquidateServiceImpl implements HjhAlarmBeforeLiquidateService {

    Logger logger = LoggerFactory.getLogger(HjhAlarmBeforeLiquidateServiceImpl.class);


    @Autowired
    private BorrowCustomizeMapper borrowCustomizeMapper;


    @Autowired
    @Qualifier("mailProcesser")
    private MessageProcesser mailMessageProcesser;
    /**
     * 在清算日期前一天，扫描还在出借中和复审中的原始标的，发邮件预警
     */
    @Override
    public void selectAlermBeformLiquidation() {
        logger.debug(">>>>>>在清算日期前一天，扫描还在出借中和复审中的原始标的 start<<<<<");
        // 查询目标数据,时间放在sql进行处理
        List<BorrowCustomize> list = borrowCustomizeMapper.selectUnDealBorrowBeforeLiquidate();
        logger.info(" 清算日前一天，还在出借中和复审中的原始标的，消息通知 list.size() = " + (CollectionUtils.isEmpty(list) ? 0 : list.size()));
        if (CollectionUtils.isNotEmpty(list)){
            logger.debug("查询目标数目size = {}",list.size());
            Boolean env_test = "true".equals(PropUtils.getSystem("hyjf.env.test")) ? true : false;
            logger.info("selectAlermBeformLiquidation evn_test is test ? " + env_test);
            String emailList= "";
            if (env_test){
               emailList = PropUtils.getSystem("hyjf.alerm.email.test");
            }else{
                emailList = PropUtils.getSystem("hyjf.alerm.email");
            }
            String [] toMail = emailList.split(",");
            String online = "原始标的风险状态消息通知";
            StringBuffer msg = new StringBuffer();
            StringBuffer targetId = new StringBuffer();
            msg.append("明天即将进入清算日，部分原始标的仍然处于出借或复审中，请相关人员至后台查看并及时处理，谢谢 \n");
            for (BorrowCustomize borrow : list){
                targetId.append(" " +borrow.getBorrowNid());
            }
            msg.append("目标id如下：" + targetId);
            MailMessage mailmessage = new MailMessage(null, null,online, msg.toString(), null, toMail, null, MessageDefine.MAILSENDFORMAILINGADDRESSMSG);
            //mailMessageProcesser.send(mailmessage);
            mailMessageProcesser.gather(mailmessage);
        }else{
           logger.info(">>>>清算前扫描原始标的查询目标数目为零，不发送邮件预警<<<<");
        }
        logger.debug(">>>>>>在清算日期前一天，扫描还在出借中和复审中的原始标的 end<<<<<");
    }
}
