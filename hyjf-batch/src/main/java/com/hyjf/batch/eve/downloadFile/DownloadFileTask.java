package com.hyjf.batch.eve.downloadFile;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * <p>
 * Title:DownloadFileTask
 * </p>
 * <p>
 * Description: 存款业务红包流水明细文件下载定时任务
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Kaka
 * @date 2017年10月17日 下午5:42:21
 */
public class DownloadFileTask {
    private static final Logger LOG = LoggerFactory.getLogger(DownloadFileTask.class);
	@Autowired
	private DownloadFileService service;
	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

	public void run() {
	    LOG.info("---------存款业务红包流水明细文件下载定时任务-----------");
		
		if (isOver) {
			isOver = false;
			if(service.downloadFiles()){
			    
			}
			isOver = true;
		}
		LOG.info("存款业务红包流水明细文件下载定时任务结束...");
	}

}
