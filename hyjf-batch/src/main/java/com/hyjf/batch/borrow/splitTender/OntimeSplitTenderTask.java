package com.hyjf.batch.borrow.splitTender;

import java.text.DecimalFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.hyjf.batch.borrow.autoReview.AutoReviewService;
import com.hyjf.batch.borrow.autoReview.enums.BorrowSendTypeEnum;
import com.hyjf.batch.borrow.tender.OntimeTenderTask;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowBail;
import com.hyjf.mybatis.model.auto.BorrowWithBLOBs;

/**
 * 拆分标的的自动发标
 * 
 * @author HBZ
 */
public class OntimeSplitTenderTask {

	@Autowired
	OntimeSplitTenderService ontimeSplitTenderService;

	@Autowired
	private AutoReviewService autoReviewService;
	@Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;  

	/**
	 * 拆分标的的自动发标
	 */
	public void run() {

		LogUtil.startLog(OntimeSplitTenderTask.class.getName(), "拆分标的的自动发标 OntimeSplitTenderTask.run Start... ");

		// 当前时间
		long nowTime = GetDate.getMillis() / 1000;
		// 拆标自动发标等待时间（分钟）
		Integer afterTime = 0;

		// 找出所有 满标但是未复审的标
		List<Borrow> unrecheckedBorrows = this.ontimeSplitTenderService.queryAllunrecheckTenders();

		if (unrecheckedBorrows != null && unrecheckedBorrows.size() > 0) {

			try {
				// 延期自动发标时间（单位：分钟）
				afterTime = autoReviewService.getAfterTime(BorrowSendTypeEnum.FABIAO_CD);
			} catch (Exception e) {
				e.printStackTrace();
			}

			for (Borrow uncheckBorrow : unrecheckedBorrows) {
				String borrowNidMain = uncheckBorrow.getBorrowNid();
				// 当前标的满标时间
				Integer fullTime = uncheckBorrow.getBorrowFullTime();

				// 当前标的满标时间 + 等待发标时间
				Date compareDate = GetDate.getMinutesAfter(fullTime, afterTime);
				long compareDateLong = compareDate.getTime() / 1000;

				// 如果当前时间 >= 等待发标时间 + 上期标的满标时间
				if (nowTime >= compareDateLong) {

					// 获取下一个标的 借款编号
					String borrowNid = this.getNextBorrowNid(borrowNidMain);

					// 获取下一个标
					Borrow borrowInfo = this.ontimeSplitTenderService.queryBorrowByNextNid(borrowNid);

					// 获取到的场合
					if (borrowInfo != null) {

						// 是否交纳保证金
						BorrowBail borrowBail = this.ontimeSplitTenderService
								.queryBorrowBailByNid(borrowInfo.getBorrowNid());

						// 已交保证金
						if (borrowBail != null && borrowBail.getId() != null) {
							// 开始发标吧..............
							this.fireBorrow(borrowInfo, nowTime);
						}
					}
				}
			}
		}

		LogUtil.endLog(OntimeSplitTenderTask.class.getName(), "拆分标的的自动发标 OntimeSplitTenderTask.run End... ");
	}

	/**
	 * 自动发标（下一个标的）
	 * 
	 * @param borrowInfo
	 * @param nowTime
	 */
	private void fireBorrow(Borrow borrowInfo, long nowTime) {

		BorrowWithBLOBs newBorrow = new BorrowWithBLOBs();
		newBorrow.setId(borrowInfo.getId());
		// 借款到期时间
		newBorrow.setBorrowEndTime(String.valueOf(nowTime + borrowInfo.getBorrowValidTime() * 86400));
		// 是否可以进行借款
		newBorrow.setBorrowStatus(1);
		// 发标的状态
		newBorrow.setVerifyStatus(1);
		// 状态
		newBorrow.setStatus(1);
		// 初审通过时间
		newBorrow.setVerifyTime(String.valueOf(nowTime));
		// 初审备注
		newBorrow.setVerifyRemark("自动发标(初审)");
		// 初审人员
		newBorrow.setVerifyUserid("0");
		// 剩余可出借金额
		newBorrow.setBorrowAccountWait(borrowInfo.getAccount());

		this.ontimeSplitTenderService.updateByPrimaryKeySelective(newBorrow);

		// 短信参数
		Map<String, String> params = new HashMap<String, String>();
		params.put("val_title", borrowInfo.getBorrowNid());

		// 发送短信
		try {
		    SmsMessage smsMessage =
                    new SmsMessage(null, params, null, null, MessageDefine.SMSSENDFORMANAGER, null,
                    		CustomConstants.PARAM_TPL_ZDFB, CustomConstants.CHANNEL_TYPE_NORMAL);
		    smsProcesser.gather(smsMessage);
		} catch (Exception e) {
			LogUtil.errorLog(OntimeTenderTask.class.getName(), "定时发标自动任务", "短信发送失败!", e);
		}

		LogUtil.debugLog(OntimeSplitTenderTask.class.getName(), "定时发标自动任务",
				String.valueOf(borrowInfo.getBorrowNid()) + "标的已经自动发标！");
		// borrowNid，借款的borrowNid,account借款总额
		RedisUtils.set(String.valueOf(borrowInfo.getBorrowNid()), String.valueOf(borrowInfo.getAccount()));
	}

	/**
	 * 获取下一个借款编号
	 * 
	 * @param borrowNidMain
	 * @return
	 */
	/* Modified by zhuxiaodong at 2016.5.3 fix the 9->10 BUG
	private String getNextBorrowNid(String borrowNidMain) {
		return borrowNidMain.substring(0, borrowNidMain.length() - 1)
				+ (Integer.valueOf(borrowNidMain.substring(borrowNidMain.length() - 1, borrowNidMain.length())) + 1);
	}
	*/
   private String getNextBorrowNid(String borrowNidMain) {
       DecimalFormat df = new DecimalFormat("00");
       return borrowNidMain.substring(0, borrowNidMain.length() - 2)
               + df.format((Integer.valueOf(borrowNidMain.substring(borrowNidMain.length() - 2, borrowNidMain.length())) + 1));
    }
}
