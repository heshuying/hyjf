package com.hyjf.batch.eve.batchAleve;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.batch.eve.batchEve.EveFileService;
import com.hyjf.batch.util.TransUtil;
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
public class AleveFileTask {
    private static final Logger LOG = LoggerFactory.getLogger(AleveFileTask.class);
	@Autowired
	private EveFileService aleveService;
	// 判断一轮定时是否跑完,如果没跑完,不允许再跑一个
	private static Boolean isOver = true;

	public void run() {
	    LOG.info("---------存款业务红包流水全明细数据导入定时任务-----------");
		
		if (isOver) {
			isOver = false;
			 String path = "D:/test/09"; // 路径
		        File f = new File(path);
		        if (!f.exists()) {
		            System.out.println(path + " not exists");
		            return;
		        }

		        File fa[] = f.listFiles();
		        for (int i = 0; i < fa.length; i++) {
		            File fs = fa[i];
		            if (fs.isDirectory()) {
		                String fileName = fs.getName();
		                 File dir = new File("D:/test/09/"+fileName);  
		                File fin;
		                try {
		                    fin = new File(dir.getCanonicalPath() + File.separator + "3005-EVE0082-201709"+fileName);
		                    ArrayList<EveLog> list = TransUtil.readFileEve(fin); 
		                    aleveService.saveFile(list);
		                    System.out.println("已更新 " + list.size() + " 条记录");
		                   
		                } catch (IOException e) {
		                    // TODO Auto-generated catch block
		                    e.printStackTrace();
		                }  

		            } else {
		                System.out.println(fs.getName());
		            }
		        }
			
			/* // 处理开始时间
	        String startTime = GetDate.dateToString(new Date());
	        System.out.println("处理开始时间:" + startTime);
	        File dir = new File("D:/test");  
	        File fin;
	        try {
	            fin = new File(dir.getCanonicalPath() + File.separator + "3005-ALEVE0082-20171001");
	            ArrayList<AleveLog> list = TransUtil.readFileAleve(fin); 
	            aleveService.saveFile(list);
	            System.out.println("已更新 " + list.size() + " 条记录");
	           
	        } catch (IOException e) {
	            // TODO Auto-generated catch block
	            e.printStackTrace();
	        }  */
			isOver = true;
		}
		LOG.info("存款业务红包流水全明细数据导入定时任务结束...");
	}

}
