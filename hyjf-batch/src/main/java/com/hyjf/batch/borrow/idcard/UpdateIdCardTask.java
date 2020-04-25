package com.hyjf.batch.borrow.idcard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.TimeCountTool;
import com.hyjf.mybatis.model.customize.admin.AdminChinapnrLogCustomize;

/**
 * 更新银行卡信息定时任务
 * 
 * @author 王坤
 * @since 2016年1月18日 下午4:24:13
 */
public class UpdateIdCardTask {

	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	private UpdateIdCardService updateIdCardService;

	/**
	 * 更新用户的身份证号码
	 */
	public void run() {

		if (isRun == 0) {
			isRun = 1;
			updateIdCard();
			isRun = 0;
		} else {
			LogUtil.errorLog(UpdateIdCardTask.class.getSimpleName(), "updateIdCardByUserId", "用户身份证信息更新任务正在执行", null);
		}
	}

	public void updateIdCard() {
		TimeCountTool timeCountTool = new TimeCountTool();
		timeCountTool.printBeginDate();
		List<AdminChinapnrLogCustomize> chinapnrLogList = updateIdCardService.getAllOpenAccount();
		int count = 0;
		if (chinapnrLogList != null && chinapnrLogList.size() > 0) {
			LogUtil.startLog(UpdateIdCardTask.class.getSimpleName(), "updateIdCardByUserId",
					"更新用户身份证信息共:" + chinapnrLogList.size() + "条");
			for (int i = 0; i < chinapnrLogList.size(); i++) {
				AdminChinapnrLogCustomize accountChinapnr = chinapnrLogList.get(i);
				int flag = updateIdCardService.updateIdCard(accountChinapnr);
				if (flag > 0) {
					count++;
				}
			}
		}
		LogUtil.endLog(UpdateIdCardTask.class.getSimpleName(), "updateIdCardByUserId", "更新用户身份证信息完成:" + count + "条成功");
		timeCountTool.printBeginDate();
	}

}
