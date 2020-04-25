package com.hyjf.batch.eve.batchEve;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.batch.util.TXTParseUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.model.auto.EveLog;

/**
 * 
 * <p>
 * Title:AleveTask
 * </p>
 * <p>
 * Description: 存款业务红包流水全明细数据导入
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author Kaka
 * @date 2017年10月17日 下午5:42:21
 */
public class EveFileTask {
    private static final Logger LOG = LoggerFactory.getLogger(EveFileTask.class);
    @Autowired
    private EveFileService eveService;

	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

	public void run() {
	    LOG.info("---------存款业务红包流水Eve明细数据导入定时任务-----------");
		
		if (isOver) {
			isOver = false;
			 // 处理开始时间
			LOG.info("处理EVE数据开始...");
	        // 处理开始时间
	        String startTime = GetDate.dateToString(new Date());
	        System.out.println("处理开始时间:" + startTime);
	        File dir = new File("D:/test");  
	        File fin;
	        try {
	            fin = new File(dir.getCanonicalPath() + File.separator + "3005-EVE0082-20171001");
	            ArrayList<EveLog> list = TXTParseUtils.readFileEve(fin); 
	            eveService.saveFile(list);

	            System.out.println("已更新 " + list.size() + " 条记录");
	           
	        } catch (IOException e) {
	            e.printStackTrace();
	        }  
			isOver = true;
		}
		LOG.info("存款业务红包流水Eve明细数据导入定时任务结束...");
	}

}
