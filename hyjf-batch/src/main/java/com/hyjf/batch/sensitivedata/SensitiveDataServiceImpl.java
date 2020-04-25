/*
 * @Copyright: 2005-2018 www.hyjf.com. All rights reserved.
 */
package com.hyjf.batch.sensitivedata;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.BaseServiceImpl;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.mapper.customize.SensitiveDataMapper;
import com.hyjf.mybatis.model.customize.SensitiveDataDto;
import com.hyjf.mybatis.util.mail.MailUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamSource;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fp
 * @version SensitiveDataServiceImpl, v0.1 2018/3/29 10:16
 */
@Service
public class SensitiveDataServiceImpl extends BaseServiceImpl implements SensitiveDataService {
    private static final Logger logger = LoggerFactory.getLogger(SensitiveDataServiceImpl.class);

    @Autowired
    private SensitiveDataMapper sensitiveDataMapper;


    /**
     * 1.查询满足敏感用户List
     * 2.封装到Excel
     * 3.发送邮件
     */
    @Override
    public void execTask(String date) throws Exception {
        //查询今天注册,开户的,返回手机号，用户名，证件号，银行卡号注册时间，开户时间
        List<SensitiveDataDto> sensitiveDataList = sensitiveDataMapper.selectOpenUsersByDate(date);
        List<SensitiveDataDto> newList = new ArrayList<SensitiveDataDto>();
        if (sensitiveDataList != null && sensitiveDataList.size() > 0) {
            for (int i = 0; i < sensitiveDataList.size(); i++) {
                DecimalFormat df = new DecimalFormat("#.00");
                SensitiveDataDto sensitiveDataDto = sensitiveDataList.get(i);
                //查询是否充值，返回date时间的充值总额
                double rechargeAmount = sensitiveDataMapper.selectRechargeTotalByDate(sensitiveDataDto.getUserId(), date);
                if (rechargeAmount <= 0) {
                    continue;
                }
                //查询该用户是否有债权
                int count = sensitiveDataMapper.selectTenderCount(sensitiveDataDto.getUserId());
                if (count > 0) {
                    continue;
                }
                //查询该用户是否有提现,返回date时间的投提现总额
                double withDrawAmount = sensitiveDataMapper.selectWithTotalByDate(sensitiveDataDto.getUserId(), date);
                if (withDrawAmount <= 0) {
                    continue;
                }
                sensitiveDataDto.setRechargeAmount(df.format(rechargeAmount));
                sensitiveDataDto.setWithDrawAmount(df.format(withDrawAmount));
                newList.add(sensitiveDataDto);
            }

        }
        logger.info("反欺诈预警名单[" + date + "],个数为" + newList.size() + ">>>>>>>>>>>>>>>>>>>>>>>>>>");
        //Excel封装并邮件发送
        this.bulidFileAndSend(newList, date);


    }

    /**
     * 生成excel,发送邮件
     */
    private void bulidFileAndSend(List<SensitiveDataDto> list, String date) throws Exception {
        //获取要发送邮件的list
        String toEmailStr = PropUtils.getSystem("hyjf.sensitiveDate.email");
        if (StringUtils.isBlank(toEmailStr)) {
            logger.info("反欺诈预警名单发送邮件时没有收件人,请设置hyjf.sensitiveDate.email的值>>>>>>>>>>>>>>>>>>>");
            return;
        }
        String[] toEmail = toEmailStr.split(",");//{"fengping@shizitegong.com","dangzewen@shizitegong.com"};
        if (toEmail.length < 0) {
            return;
        }
        String[] fileName = {"反欺诈预警名单-" + date + ".xls"};
        ExcelExportUtil<SensitiveDataDto> ex = new ExcelExportUtil<SensitiveDataDto>();
        String title = "反欺诈预警名单：" + date;
        String[] headerName = {"手机号", "用户名", "银行卡号", "注册时间", "开户时间", "充值金额", "提现金额"};
        String[] fileds = {"mobile", "userName", "bankNo", "registerDate", "openAccountDate", "rechargeAmount", "withDrawAmount"};
        //附件is
        InputStreamSource is = ex.exportExcelByCondition(title, headerName, fileds, list);
        logger.info("开始发送反欺诈预警名单邮件>>>>>>>>>>>>>>>>>>>>>toEmail:" + JSONObject.toJSONString(toEmail) + "");
        //邮件内容
        String content = "昨日检测到欺诈预警名单："+list.size()+"位。";
        String subject = "【反欺诈预警名单】";
        try {
        	if(list.size() > 0){//包含附件
                MailUtil.sendAttachmentsMailOnPort465(toEmail, subject, content, fileName, is);
            }else{//不包含附件
                content = "昨日暂未检测到欺诈预警名单";
                MailUtil.sendAttachmentsMailOnPort465(toEmail, subject, content, null, null);
            }
        	logger.info("发送反欺诈预警名单成功>>>>>>>>>>>>>>>>>>>>>");
		} catch (Exception e) {
			logger.error("发送反欺诈预警名单失败>>>>>>>>>>>>>>>>>>>>>",e);
		}
    }
}
