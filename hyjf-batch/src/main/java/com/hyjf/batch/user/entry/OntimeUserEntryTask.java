package com.hyjf.batch.user.entry;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.auto.UsersInfo;

/**
 * 定时 员工入职，user_info表的用户属性做响应修改
 * 
 * @author 王坤
 */
public class OntimeUserEntryTask {

	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	OntimeUserEntryService ontimeUserEntryService;

	/**
	 * 定时 员工入职
	 */
	public void run() {
		userEntry();
	}

	/**
	 * 调用放款接口
	 *
	 * @return
	 */
	private boolean userEntry() {
		if (isRun == 0) {
			System.out.println("定时 员工入职  OntimeUserEntryTask.run 开始... " + new Date());
			isRun = 1;
			try {
				List<UsersInfo> users = this.ontimeUserEntryService.queryEmployeeEntryList();
				for (UsersInfo employee : users) {
					try {
						// 修改客户属性
						this.ontimeUserEntryService.updateEmployeeByExampleSelective(employee);
						// 修改 入职人员作为推荐人的情况，被推荐人属性变为‘有主单’
						this.ontimeUserEntryService.updateSpreadAttribute(employee.getUserId());
						// 删除相应的用户的推荐人
						this.ontimeUserEntryService.deleteReferrer(employee.getUserId());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
			}
			System.out.println("定时 员工入职  OntimeUserEntryTask.run 结束... " + new Date());
		}
		return false;
	}

}
