package com.hyjf.batch.hjh.borrow.issuerecover;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class OneTimeTask {
	
	private static Logger _log = LoggerFactory.getLogger(OneTimeTask.class);

	public static void main(String[] args) {
		
		try {
			
			
			String[] springfiles = new String[]{"conf/spring.xml","conf/spring-mybatis.xml","conf/spring-batch-task.xml","conf/spring-rabbitmq-sender.xml"};
			final ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(springfiles);
			context.start();
			
			
			AutoIssueRecoverTask task = (AutoIssueRecoverTask) context.getBean("autoIssueRecoverTask");
			
			task.run();
//			
			// 查询待备案列表
//			AutoIssueRecoverService autoIssueRecoverService = (AutoIssueRecoverService) context.getBean("autoIssueRecoverServiceImpl");
//			List<HjhPlanAsset> recordList = autoIssueRecoverService.selectAssetList(3);
			
//			if (sendList == null || sendList.size() == 0) {
//				return false;
//			}
//			for (HjhPlanAsset planAsset : recordList) {
//				
//				autoIssueRecoverService.sendToMQ(planAsset, RabbitMQConstants.ROUTINGKEY_BORROW_RECORD);
//			}
			

//			
//			List<HjhPlanAsset> sendList = autoIssueRecoverService.selectAssetList(0);
//			for (HjhPlanAsset planAsset : sendList) {
//				
//				System.out.println(planAsset.getAssetId());
//			}
			
//			String[] allBeans = context.getBeanDefinitionNames();
//			
//			for (int i = 0; i < allBeans.length; i++) {
//				System.out.println(allBeans[i]);
//			}
			
			context.stop();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
		

	}

}
