package com.hyjf.batch.user.leave;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.mybatis.model.auto.Users;

/**
 * 定时 员工离职，user_info表的用户属性做响应修改
 * 
 * @author HBZ
 */
public class OntimeUserLeaveTask {
	
	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	OntimeUserLeaveService ontimeUserLeaveService;

	/**
	 * 定时 员工离职
	 */
	public void run() {
		userLeave();
	}
	
	/**
	 * 调用放款接口
	 *
	 * @return
	 */
	private boolean userLeave() {
		if (isRun == 0) {
			System.out.println("定时 员工离职  OntimeUserLeaveTask.run 开始... "+new Date());
			isRun = 1;
			try {
				List<Users> users = this.ontimeUserLeaveService.queryEmployeeList();
				for (Users employee : users) {
					try{
						// 修改客户属性
						this.ontimeUserLeaveService.updateEmployeeByExampleSelective(employee);
						// 修改 离职人员作为推荐人的情况，被推荐人属性变为‘无主单’
						this.ontimeUserLeaveService.updateSpreadAttribute(employee.getUserId());
					}catch(Exception e){
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
			}
			System.out.println("定时 员工离职  OntimeUserLeaveTask.run 结束... "+new Date());
		}
		return false;
	}
}
