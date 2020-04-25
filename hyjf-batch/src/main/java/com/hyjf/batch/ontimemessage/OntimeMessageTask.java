package com.hyjf.batch.ontimemessage;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.hyjf.common.log.LogUtil;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageDefine;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.messageprocesser.SmsMessage;
import com.hyjf.mybatis.model.auto.SmsOntimeWithBLOBs;
import com.hyjf.mybatis.model.customize.SmsCodeCustomize;

public class OntimeMessageTask {
	/** 类名 */
	private static final String THIS_CLASS = OntimeMessageTask.class.getName();

	/** 运行状态 */
	private static int isRun = 0;

	/** 任务状态:未执行 */
	private static final Integer STATUS_WAIT = 0;

	/** 任务状态:已完成 */
	private static final Integer STATUS_SUCCESS = 2;

	/** 任务状态:执行中 */
	private static final Integer STATUS_RUNNING = 1;

	@Autowired
	OntimeMessageService ontimeMessageService;

	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	@Qualifier("mailProcesser")
	private MessageProcesser mailMessageProcesser;

	public void run() {
		// 调用定时发短信接口
		ontime();
	}

	/**
	 * 调用定时发短信接口
	 *
	 * @return
	 */
	private boolean ontime() {
		String methodName = "ontime";
		if (isRun == 0) {
			isRun = 1;

			try {
				// 取得未发送的定时短信任务
				List<SmsOntimeWithBLOBs> listApicron = ontimeMessageService.getOntimeList(STATUS_WAIT);
				if (listApicron != null && listApicron.size() > 0) {
					// 循环进行定时发短信
					for (SmsOntimeWithBLOBs apicron : listApicron) {
						int errorCnt = 0;
						// 错误信息
						StringBuffer sbError = new StringBuffer();
						LogUtil.startLog(THIS_CLASS, methodName, "定时发短信任务开始。[手机号：" + apicron.getMobile() + "]");
						// 更新任务API状态为进行中
						ontimeMessageService.updatetOntime(apicron.getId(), STATUS_RUNNING);
						if (apicron.getContent() == null) {
							sbError.append("发送消息不能为空");
							continue;
						}
						if (apicron.getChannelType() == null) {
							sbError.append("发送消息渠道不能为空");
							continue;
						}
						String mobile = apicron.getMobile();
						String send_message = apicron.getContent();
						String channelType = apicron.getChannelType();
						if (StringUtils.isEmpty(mobile)) {
							// 在筛选条件下查询出用户
							List<SmsCodeCustomize> msgs = ontimeMessageService.queryUser(apicron);
							// 用户数
							System.out.println("发送用户数" + msgs.size());
							// 用户未手写手机号码
							int number = 200;// 分组每组数
							if (msgs != null && msgs.size() != 0) {
								int i = msgs.size() / number;
								for (int j = 0; j <= i; j++) {
									int tosize = (j + 1) * number;
									List<SmsCodeCustomize> smslist;
									if (tosize > msgs.size()) {
										smslist = msgs.subList(j * number, msgs.size());
									} else {
										smslist = msgs.subList(j * number, tosize);
									}
									String phones = "";
									for (int z = 0; z < smslist.size(); z++) {
										if (StringUtils.isNotEmpty(smslist.get(z).getUser_phones())
												&& Validator.isPhoneNumber(smslist.get(z).getUser_phones())) {
											if (z < smslist.size() - 1) {
												phones += smslist.get(z).getUser_phones() + ",";
											} else {
												phones += smslist.get(z).getUser_phones();
											}
										}
									}
									try {
										SmsMessage smsMessage = new SmsMessage(null, null, phones, send_message,
												MessageDefine.SMSSENDFORUSERSNOTPL, null, null, channelType);
										smsProcesser.gather(smsMessage);
									} catch (Exception e) {
										LogUtil.errorLog(THIS_CLASS, methodName, e);
										errorCnt++;
									}
								}
							}
						} else {
							// 发送短信
							try {
								String[] mobiles = mobile.split(",");
								for (int i = 0; i < (mobiles.length / 200) + 1; i++) {
									String mbl = "";
									for (int j = i * 200; j < ((i + 1) * 200) && j < mobiles.length; j++) {
										mbl += mobiles[j] + ",";
									}
									if (mbl.endsWith(",")) {
										mbl = mbl.substring(0, mbl.length() - 1);
									}
									SmsMessage smsMessage = new SmsMessage(null, null, mbl, send_message,
											MessageDefine.SMSSENDFORUSERSNOTPL, null, null, channelType);
									smsProcesser.gather(smsMessage);
								}
							} catch (Exception e) {
								sbError.append(e.getMessage()).append("<br/>");
								LogUtil.errorLog(THIS_CLASS, methodName, e);
								errorCnt++;
							}
						}

						// 有错误时
						if (errorCnt > 0) {
							throw new Exception("定时发送短信时发生错误。" + "[错误记录id：" + apicron.getId() + "]," + "[错误件数："
									+ errorCnt + "]");
						}
						// 更新任务API状态为完成
						ontimeMessageService.updatetOntime(apicron.getId(), STATUS_SUCCESS);
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
			}
		}
		return false;
	}
}
