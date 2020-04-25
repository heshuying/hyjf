/**
 * Description:（类功能描述-必填） 需要在每个方法前添加业务描述，方便业务业务行为的BI工作
 * Copyright: Copyright (HYJF Corporation)2017
 * Company: HYJF Corporation
 * @author: lb
 * @version: 1.0
 * Created at: 2017年9月15日 上午9:43:49
 * Modification History:
 * Modified by : 
 */
	
package com.hyjf.batch.eve.downloadFile;

import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.batch.eve.batchAleve.AleveFileService;
import com.hyjf.batch.eve.batchEve.EveFileService;
import com.hyjf.batch.util.FtpUtil;
import com.hyjf.batch.util.SFTPParameter;
import com.hyjf.common.calculate.DateUtils;
import com.hyjf.common.file.FileUtil;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.util.RabbitMQConstants;
import com.hyjf.mybatis.customize.CustomizeMapper;


/**
 * @author liubin
 */

@Service("DownloadFileService")
public class DownloadFileServiceImpl extends CustomizeMapper implements DownloadFileService {
    private static final Logger LOG = LoggerFactory.getLogger(DownloadFileServiceImpl.class);

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;
    
    @Autowired
    private AleveFileService aleveFileService;
    
    @Autowired
    private EveFileService eveFileService;
		
	@Override
	public Boolean downloadFiles() {
	    SFTPParameter para = new SFTPParameter() ;
	    Boolean re = false;

	    para.hostName = PropUtils.getSystem("hostFtp.hostName");//ftp服务器地址
	    para.userName = PropUtils.getSystem("hostFtp.userName");//ftp服务器用户名
	    para.passWord = PropUtils.getSystem("hostFtp.passWord");//ftp服务器密码
	    para.port = PropUtils.getSystem("hostFtp.port")==null?0:Integer.valueOf(PropUtils.getSystem("hostFtp.port"));//ftp服务器端口
	    
	    String beforeYear = DateUtils.getBeforeYear();//当前时间前一天的年份
        String beforeMonth = DateUtils.getBeforeMonth();//当前时间前一天的月份
        String beforeDay = DateUtils.getBeforeDay();//当前前时间前一天的日期
        String date = DateUtils.getNowDateOfDay();//当天日期返回时间类型 yyyyMMdd
        String localDir = PropUtils.getSystem("localDir");
        Boolean dir = FileUtil.createDir(localDir+"/"+date);
        String filePath = beforeYear+"/"+beforeMonth+"/"+beforeDay;
        para.downloadPath =PropUtils.getSystem("hostFtp.downloadPath")+ filePath;//ftp服务器文件目录
        para.savePath =localDir+"/"+date;
        String beforeDate = DateUtils.getBeforeDate();//当前前时间前一天的日期yyyyMMdd
        int countsAleve = aleveFileService.countByExample(beforeDate);
        int countsEve = eveFileService.countByExample(beforeDate);
        try {
            if(countsAleve==0 && countsEve==0){
                //删除前一天的文件目录
                FileUtil.deltree(localDir+"/"+beforeDate);
                //更改每天跑两次定时任务，去掉次判断
                //if(dir){
                    if(!FtpUtil.downloadFiles(para)){
                        LOG.info("下载ftp文件失败");  
                    }else{
                        HashMap<String, String> map = new HashMap<>();
                        map.put("status", "1");
                        map.put("savePath", para.savePath);
                        map.put("filePathEve", PropUtils.getSystem("evefile.name"));
                        map.put("filePathAleve", PropUtils.getSystem("alevefile.name"));
                        String msgString = JSONObject.toJSONString(map);
                        rabbitTemplate.convertAndSend(RabbitMQConstants.READ_FILE_EVE,msgString);
                        rabbitTemplate.convertAndSend(RabbitMQConstants.READ_FILE_ALEVE,msgString);
                        para.release(); 
                    }
                //}
            }
            

        } catch (Exception e) {
            LOG.info("下载ftp文件失败"); 
        }
       
        return re;
	}
}
	