package com.hyjf.batch.borrow.tender;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.GetOrderIdUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowAppoint;
import com.hyjf.mybatis.model.auto.FreezeList;

/**
 * 定时发标
 * 
 * @author wangkun
 */
public class OntimeTenderTask {
	/** 运行状态 */
	private static int isRun = 0;
	@Autowired
	@Qualifier("smsProcesser")
	private MessageProcesser smsProcesser;

	@Autowired
	OntimeTenderService ontimeTenderService;

	/**
	 * 定时发标
	 */
	public void run() {
		onTimeTender();
	}

	/**
	 * 调用定时发标
	 *
	 * @return
	 */
	private boolean onTimeTender() {
		if (isRun == 0) {
			System.out.println("定时发标 OntimeTenderTask.run ... ");
			isRun = 1;
			try {
				// 1.定时预约标的状态修改
				// a。修改标的状态为预约中
				// 查询相应的定时预约开始标的
				List<Borrow> borrowAppointStarts = this.ontimeTenderService.selectBorrowAppointStart();
				// 循环遍历修改标的预约状态为预约中
				if (borrowAppointStarts != null && borrowAppointStarts.size() > 0) {
					for (Borrow borrowAppointStart : borrowAppointStarts) {
						RedisUtils.set(borrowAppointStart.getBorrowNid() + CustomConstants.APPOINT,
								borrowAppointStart.getAccount().toString());
						this.ontimeTenderService.updateBorrowAppointStatus(borrowAppointStart.getId(), 1);

					}
				}
				// b。修改标的状态为预约完成。
				// 查询相应的定时预约完成标的
				List<Borrow> borrowAppointEnds = this.ontimeTenderService.selectBorrowAppointEnd();
				// 循环遍历修改标的预约状态为预约完成
				if (borrowAppointEnds != null && borrowAppointEnds.size() > 0) {
					for (Borrow borrowAppointEnd : borrowAppointEnds) {
						this.ontimeTenderService.updateBorrowAppointStatus(borrowAppointEnd.getId(), 2);
					}
				}
				// 2。定时发标标的进行自动出借
				// 查询相应的自动发标的标的信息
				List<Borrow> borrowOntimes = this.ontimeTenderService.queryOntimeTenderList();
				if (borrowOntimes != null && borrowOntimes.size() > 0) {
					for (Borrow borrow : borrowOntimes) {
						try {
							// a.标的自动出借
							String borrowNid = borrow.getBorrowNid();
							// 清除标总额的缓存
							RedisUtils.del(borrowNid + CustomConstants.APPOINT);
							// 查询相应的预约中标的信息
							List<BorrowAppoint> borrowAppoints = this.ontimeTenderService.selectBorrowAppoint(borrowNid);
							if (borrowAppoints != null && borrowAppoints.size() > 0) {
								for (BorrowAppoint borrowAppoint : borrowAppoints) {
									// 出借用户userId
									int userId = borrowAppoint.getUserId();
									// 预约订单号
									String appointOrderId = borrowAppoint.getOrderId();
									try {
										//解冻资金
										FreezeList freeze = this.ontimeTenderService.getUserFreeze(userId,borrowNid,appointOrderId);
										if(Validator.isNotNull(freeze)){
											//冻结标识
											String trxId = freeze.getTrxid();
											//冻结订单日期
											String freezeOrderDate = GetOrderIdUtils.getOrderDate(freeze.getCreateTime());
											//解冻预约冻结资金
											try {
												boolean appointFreezeFlag = this.ontimeTenderService.unFreezeOrder(userId, appointOrderId,trxId,freezeOrderDate);
												//预约冻结资金解冻成功
												if (appointFreezeFlag) {
													try {
														boolean deleteFreezeFlag = this.ontimeTenderService.deleteFreezeList(borrowAppoint);
														if(deleteFreezeFlag){
															try {
																// 生成订单
																String orderId = GetOrderIdUtils.getOrderId2(userId);
																// 订单日期
																String orderDate = GetDate.getServerDateTime(1, new Date());
																// 预约出借校验
																JSONObject info = new JSONObject();
																info.put("checkFlag", "0");
																this.ontimeTenderService.checkAppoint(borrowAppoint, info);
																if (info.getString("checkFlag").equals("1")) {
																	borrowAppoint.setTenderNid(orderId);
																	try {
																		// 写日志
																		boolean flag = ontimeTenderService.updateBeforeApoint(borrowAppoint);
																		if (flag) {
																			try {
																				// 调用预约出借接口
																				JSONObject appointResult = this.ontimeTenderService.appointTender(borrowAppoint, orderDate);
																				String status = appointResult.getString("status");
																				String message = appointResult.getString("message");
																				if (status.equals("1")) {
																					String freezeId = appointResult.getString("freezeId");
																					try {
																						// 预约出借
																						boolean tenderFlag = this.ontimeTenderService.updateBorrowTender(borrowAppoint, orderDate,freezeId);
																						// 出借接口调用成功后，后续处理失败
																						if (!tenderFlag) {
																							// 回滚用户的余额信息
																							this.ontimeTenderService.updateUserAccount(borrowAppoint);
																							// 解冻资金
																							try {
																								boolean unFreezeFlag = this.ontimeTenderService.unFreezeOrder(userId, orderId,freezeId,orderDate);
																								if (unFreezeFlag) {
																									// 更新出借状态为失败
																									this.ontimeTenderService.updateAppointStatus(borrowAppoint,2, "出借失败!");
																								} else {
																									// 更新出借状态为失败
																									this.ontimeTenderService.updateAppointStatus(borrowAppoint,2, "出借失败 ,请联系系统管理员!");
																								}
																							} catch (Exception e) {
																								System.out.println("接口调用成功后，解冻资金异常。出借订单号：" + orderId);
																								e.printStackTrace();
																							}
																						}
																						// 出借接口调用成功后，后续处理异常
																					} catch (Exception e) {
																						e.printStackTrace();
																						// 解冻资金
																						try {
																							boolean unFreezeFlag = this.ontimeTenderService.unFreezeOrder(userId, orderId,freezeId,orderDate);
																							if (unFreezeFlag) {
																								// 更新出借状态为失败
																								this.ontimeTenderService.updateAppointStatus(borrowAppoint,2, "出借失败!");
																							} else {
																								// 更新出借状态为失败
																								this.ontimeTenderService.updateAppointStatus(borrowAppoint,2, "出借失败 ,请联系系统管理员!");
																							}
																						} catch (Exception e1) {
																							System.out.println("接口调用成功后，系统异常，解冻资金异常。出借订单号：" + orderId);
																							e.printStackTrace();
																						}
																					}
																				} else {
																					// 更新出借状态为失败
																					this.ontimeTenderService.updateAppointStatus(borrowAppoint,2, message);
																				}
																			} catch (Exception e) {
																				e.printStackTrace();
																				// 更新出借状态为失败
																				this.ontimeTenderService.updateAppointStatus(borrowAppoint, 2,"调用自动投标接口发生异常！");
																			}
																		} else {
																			// 更新出借状态为失败
																			this.ontimeTenderService.updateAppointStatus(borrowAppoint, 2,"出借预插入失败!");
																		}
																	} catch (Exception e) {
																		e.printStackTrace();
																		// 更新出借状态为失败
																		this.ontimeTenderService.updateAppointStatus(borrowAppoint, 2,"出借预插入失败!");
																	}
																} else {
																	String checkFlag = info.getString("checkFlag");
																	String message = info.getString("message");
																	// 更新出借状态为失败
																	this.ontimeTenderService.updateAppointStatus(borrowAppoint, 2, message);
																	if (checkFlag.equals("2")) {
																		try {
																			this.ontimeTenderService.updateUserRecord(borrowAppoint);
																		} catch (Exception e) {
																			e.printStackTrace();
																		}
																	}
																}
															} catch (Exception e) {
																e.printStackTrace();
																// 更新出借状态为失败
																this.ontimeTenderService.updateAppointStatus(borrowAppoint, 2, "校验时发生异常！");
																// 回滚用户的余额信息
																this.ontimeTenderService.updateUserAccount(borrowAppoint);
															}
														}else{
															// 更新出借状态为失败
															this.ontimeTenderService.updateAppointStatus(borrowAppoint,2, "预约删除冻结记录失败 ,请联系系统管理员!");
															// 回滚用户的余额信息
															this.ontimeTenderService.updateUserAccount(borrowAppoint);
														}
													} catch (Exception e) {
														System.out.println("删除冻结记录异常。预约订单号：" + appointOrderId);
														e.printStackTrace();
														// 更新出借状态为失败
														this.ontimeTenderService.updateAppointStatus(borrowAppoint,2, "预约删除冻结记录失败 ,请联系系统管理员!");
														// 回滚用户的余额信息
														this.ontimeTenderService.updateUserAccount(borrowAppoint);
													}
												} else {
													// 更新出借状态为失败
													this.ontimeTenderService.updateAppointStatus(borrowAppoint,2, "出借失败 ,请联系系统管理员!");
													// 回滚用户的余额信息
													this.ontimeTenderService.updateUserAccount(borrowAppoint);
												}
											} catch (Exception e) {
												System.out.println("解冻预约资金异常。预约订单号：" + appointOrderId);
												e.printStackTrace();
												// 更新出借状态为失败
												this.ontimeTenderService.updateAppointStatus(borrowAppoint,2, "预约解冻失败 ,请联系系统管理员!");
												// 回滚用户的余额信息
												this.ontimeTenderService.updateUserAccount(borrowAppoint);
											}
										}else{
											// 更新出借状态为失败（未查询到冻结记录）
											this.ontimeTenderService.updateAppointStatus(borrowAppoint,2, "冻结记录未查询到!");
											// 回滚用户的余额信息
											this.ontimeTenderService.updateUserAccount(borrowAppoint);
										}
									} catch (Exception e) {
										System.out.println("接口调用成功后，系统异常，解冻资金异常。预约订单号：" + appointOrderId);
										e.printStackTrace();
									}
								}
							}
							// b.标的自动发标
							boolean flag = this.ontimeTenderService.updateSendBorrow(borrow.getId());
							if (!flag) {
								throw new Exception("标的自动发标失败！" + "[借款编号：" + borrowNid + "]");
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
			}
			System.out.println("定时发标 OntimeTenderTask.end ... ");
		}
		return false;
	}
}
