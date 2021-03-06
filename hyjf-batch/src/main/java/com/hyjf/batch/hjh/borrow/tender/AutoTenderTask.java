package com.hyjf.batch.hjh.borrow.tender;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.bank.service.mq.MqService;
import com.hyjf.common.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.hyjf.bank.service.borrow.send.RedisBorrow;
import com.hyjf.bank.service.hjh.borrow.tender.BankAutoTenderService;
import com.hyjf.bank.service.user.credit.CreditService;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.HjhAccede;
import com.hyjf.mybatis.model.auto.HjhDebtCredit;
import com.hyjf.mybatis.model.auto.HjhPlan;
import com.hyjf.mybatis.model.auto.HjhUserAuth;
import com.hyjf.mybatis.model.customize.HjhAccedeCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;

/**
 * 自动投资
 * @author xiaojohn
 *
 */
public class AutoTenderTask {

	Logger _log = LoggerFactory.getLogger(AutoTenderTask.class);

	/** 运行状态 */
	private static int isRun = 0;

	@Autowired
	private MqService mqService;

	@Autowired
	BankAutoTenderService bankAutoTenderService;

	@Autowired
	private CreditService creditService;

	/**
	 * 调用任务实际方法接口
	 */
	public void run() {
		process();
	}

	/**
	 * 汇计划自动投资
	 *
	 * @return
	 */
	private boolean process() {
		// add 汇计划三期 0点前后停止自动投资设定 liubin 20180515 start
		if (!GetDate.belongCalendar(GetDate.getShortTimeDate(),
				GetDate.getDateFromShortTime("00:30"),
				GetDate.getDateFromShortTime("23:30"))
				) {
			_log.info("汇计划自动投资任务 任务时间外...");
			return true;
		}
		// add 汇计划三期 0点前后停止自动投资设定 liubin 20180515 end

		if (isRun == 0) {
			_log.info("汇计划自动投资任务 开始... ");

			isRun = 1;
			try {

				// 查询加入计划明细（改用消息队列要防止重复拉去）
				List<HjhAccedeCustomize> hjhAccedes = this.bankAutoTenderService.selectPlanJoinList();

				if (hjhAccedes == null) {
					_log.error("汇计划自动投资任务 结束... （hjhAccedes=null） ");
					return false;
				}

				_log.info("汇计划自动投资任务 取得加入计划订单数" + hjhAccedes.size());

				for (HjhAccede hjhAccede : hjhAccedes) {
					try {
						String logMsgHeader = "======计划：" + hjhAccede.getPlanNid()
								+ "的计划订单号：" + hjhAccede.getAccedeOrderId();
						_log.info(logMsgHeader + " 计划自动投资开始。");
						boolean flag = false;
						flag = autoAssetBorrow(hjhAccede);
						if (!flag) {
							_log.info(logMsgHeader + " 计划自动投资结束。投资失败！！！");
						}else {
							_log.info(logMsgHeader + " 计划自动投资结束。投资成功！");
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				isRun = 0;
			}

			_log.info("汇计划自动投资任务 结束... ");

		}else{

			_log.info("汇计划自动投资任务 正在运行... ");
		}

		return false;
	}

	/**
	 * 汇计划加入订单 自动投资/复投
	 * 0. 取得计划信息
	 * 1. 取得投资人信息（授权账户等）
	 * 2. 有可投金额循环投资
	 * 3. 从队列中取得标的
	 * 4. 自动投资债转标的（承接）
	 * 		4.1. 债转用金额计算
	 * 		4.2. 获取债转详情
	 * 		4.3. 校验是否可以债转
	 * 		4.4. 调用银行自动购买债权接口
	 * 	 	4.5. 减去被投标的可投金额，部分承接时，余额推回队列
	 * 		4.6. 更新同步数据库
	 * 		4.7. 完全承接时，结束债券
	 * 5. 自动投资原始标的（投资）
	 * 		5.1. 投资用金额计算
	 * 		5.2. 获取标的详情
	 * 		5.3. 校验是否可以投资
	 * 		5.4. 调用银行自动投标申请接口
	 *      5.5. 减去被投标的可投金额，部分投资时，余额推回队列
	 * 		5.6. 更新同步数据库
	 * @param hjhAccede
	 * @return
	 */
	private boolean autoAssetBorrow(HjhAccede hjhAccede) {
		String accedeOrderId = hjhAccede.getAccedeOrderId();//汇计划加入订单号
		final Integer ORDER_STATUS_ERR = hjhAccede.getOrderStatus() + 90;//银行交易前，异常订单状态设定，和系统异常
		final Integer ORDER_STATUS_FAIL = hjhAccede.getOrderStatus() + 80;//银行交易后，异常订单状态设定
		// 计算标的的剩余可投资金额
		int serialFaileCount = 0;//一个计划订单的连续失败次数

		// add 汇计划三期 汇计划自动投资(分散投资) liubin 20180515 start
		//是否分散投资
		int diversifyCount = -1; //非分散投资
		//已投资笔数
		int investCountForLog = hjhAccede.getInvestCounts();
		// add 汇计划三期 汇计划自动投资(分散投资) liu0180515 end

		if (hjhAccede.getOrderStatus() == 0) {
			//0自动投标中
			_log.info("======计划加入订单号 "+ accedeOrderId + "开始自动投资,订单状态" + hjhAccede.getOrderStatus() + "======");
			// add 汇计划三期 汇计划自动投资(分散投资) liubin 20180515 start
			diversifyCount = 0; //初始分散投资 （只有投资原始标的（非复投）时，使用分散投资）
			// add 汇计划三期 汇计划自动投资(分散投资) liubin 20180515 end
		}else {
			//2自动投资成功或者3锁定中 
			_log.info("======计划加入订单号 "+ accedeOrderId + "开始自动复投,订单状态" + hjhAccede.getOrderStatus() + "======");
		}

		/** 0. 取得计划信息 */
		HjhPlan hjhPlan =  bankAutoTenderService.selectHjhPlanByPlanNid(hjhAccede.getPlanNid());

		BigDecimal accedeAccount = hjhAccede.getAccedeAccount();// 计划订单加入计划金额
		BigDecimal ketouplanAmoust = hjhAccede.getAvailableInvestAccount();// 计划订单可投金额
		// add 汇计划三期 汇计划自动投资(分散投资) liubin 20180515 start
		BigDecimal groupAmoust = ketouplanAmoust;// 每组可投金额（分散投资的最小分散金额）
		int groupCount = 1; //最小分散组数
		//标的队列名称
		String queueName = RedisConstants.HJH_PLAN_LIST + RedisConstants.HJH_BORROW_INVEST + RedisConstants.HJH_SLASH + hjhAccede.getPlanNid();//原始标的队列
		// ★临时队列有标的时，先推回主队列（恢复前次分散投资）★
		RedisUtils.lpoprpush(queueName + RedisConstants.HJH_SLASH_TMP, queueName);
		// add 汇计划三期 汇计划自动投资(分散投资) liubin 20180515 end
		_log.info("====[" + accedeOrderId + "]" + "加入计划金额：" + accedeAccount.toString());
		_log.info("====[" + accedeOrderId + "]" + "初始可投金额：" + ketouplanAmoust.toString());

		/** 1. 取得投资人信息（授权账户等） */
		//获取投资授权码
		HjhUserAuth hjhUserAuth = this.bankAutoTenderService.selectUserAuthByUserId(hjhAccede.getUserId());
		if (hjhUserAuth == null || StringUtils.isEmpty(hjhUserAuth.getAutoOrderId())) {
			_log.error("====[" + accedeOrderId + "]" + "未获取到投资授权码  "+hjhAccede.getUserId());
			return false;
		}
		if (StringUtils.isEmpty(hjhUserAuth.getAutoCreditOrderId())) {
			_log.error("====[" + accedeOrderId + "]" + "未获取到债转授权码  "+hjhAccede.getUserId());
			return false;
		}
		//获取投资账户
		BankOpenAccount bankOpenAccount = this.bankAutoTenderService.getBankOpenAccount(hjhAccede.getUserId());
		if (bankOpenAccount == null) {
			_log.error("====[" + accedeOrderId + "]" + "用户没开户 "+hjhAccede.getUserId());
			return false;
		}
		String tenderUsrcustid = bankOpenAccount.getAccount();//获取江西银行电子账号

		/** 2. 有可投金额循环投资	 */
		// 计划订单的可投金额 >= minAccountEnable 进行投资/复投
		// 计划订单的可投金额 < minAccountEnable 该计划订单投资/复投结束
		BigDecimal minAccountEnable = getMinAccountEnable(hjhAccede);
		while(ketouplanAmoust.compareTo(minAccountEnable) >= 0){
			// add 汇计划三期 汇计划自动投资(投资笔数累计) liubin 20180515 start
			_log.info("====计划加入订单号 "+ accedeOrderId + "投前累计投资笔数：" + investCountForLog + "====");
			investCountForLog += 1;
			// add 汇计划三期 汇计划自动投资(投资笔数累计) liubin 20180515 end

			// ketouplanAmoust小于1元时报警告信息
			if (ketouplanAmoust.compareTo(new BigDecimal(1)) == -1) {
				_log.warn("警告====[" + accedeOrderId + "]" + "的可投资金额为" + ketouplanAmoust.toString() + ",小于1元");
			}

			/** 3. 从队列中取得标的	 */
			/*******************取一个标的************************/
			RedisBorrow redisBorrow = null;

			String borrowStr = null;//标的的JsonString
			// del 汇计划三期 汇计划自动投资(分散投资) liubin 20180515 start
			//String queueName = null;//标的队列名称
			// del 汇计划三期 汇计划自动投资(分散投资) liubin 20180515 end
			String borrowFlag = null;//标的类型（债转标，原始标）

			// 连续10次出投人相同后，换个计划订单投
			if (serialFaileCount >= CustomConstants.HJH_SERIAL_FAILE_COUNT) {
				_log.error("[" + accedeOrderId + "]" + "借款人/出让人和计划订单的投资人连续相同次数超过"+CustomConstants.HJH_SERIAL_FAILE_COUNT+"次,跳过该计划订单");
				return false;
			}
			_log.info("[" + accedeOrderId + "]" + "连续相同次数"+serialFaileCount+"次");
			// 取债转标的（优先） (连续5次不能投债转标时,取原始标的)
			if (serialFaileCount < CustomConstants.HJH_ASSIGN_SERIAL_FAILE_COUNT) {
				queueName = RedisConstants.HJH_PLAN_LIST + RedisConstants.HJH_BORROW_CREDIT + RedisConstants.HJH_SLASH + hjhAccede.getPlanNid();
				borrowStr = getBorrowFromQueue(queueName);
				borrowFlag = RedisConstants.HJH_BORROW_CREDIT;
			}else {
				_log.info("[" + accedeOrderId + "]" + "出让人和计划订单的投资人连续相同次数超过"+CustomConstants.HJH_ASSIGN_SERIAL_FAILE_COUNT+"次,只投原始标的");
			}

			// 取原始标的(无债转标的时)
			if (borrowStr == null) {
				queueName = RedisConstants.HJH_PLAN_LIST + RedisConstants.HJH_BORROW_INVEST + RedisConstants.HJH_SLASH + hjhAccede.getPlanNid();
				// add 汇计划三期 汇计划自动投资(分散投资) liubin 20180515 start
				// ****分散投资主规则****
				// 初始分散投资时
				if (diversifyCount == 0) {
					// 取得最小分散组数（计划最小分散笔数 & 队列中的标的数 取小的）
					groupCount = (int) DigitalUtils.min((long)hjhPlan.getMinInvestCounts(), RedisUtils.llen(queueName));
					groupCount = groupCount < 1 ? 1 : groupCount;//小于1时，组数取1
					// 计算每组投资金额（取百位）
					groupAmoust = ketouplanAmoust.divide(new BigDecimal(groupCount), -2, BigDecimal.ROUND_DOWN);
				}
				// 分散投资时（突然插入债转可能可投余额不够）
				if (diversifyCount >= 0) {
					_log.info("[" + accedeOrderId + "]" + "开始分散投资。。。组数：" + groupCount + ", 每组金额：" + groupAmoust
							+ ", 当前组：" + (diversifyCount+1));
					groupAmoust = DigitalUtils.min(groupAmoust, ketouplanAmoust);
				}
				// 分散组数=1 或者 超出分散组数 或者 groupAmoust<100 的投资不再分散
				if (groupCount == 1 || diversifyCount == -1 || diversifyCount+1 > groupCount || groupAmoust.compareTo(new BigDecimal(100)) < 0){
					groupAmoust = ketouplanAmoust;
					diversifyCount = -1;
				}
				// add 汇计划三期 汇计划自动投资(分散投资) liubin 20180515 end
				borrowStr = getBorrowFromQueue(queueName);
				borrowFlag = RedisConstants.HJH_BORROW_INVEST;
			}

			// 没有可投资标的
			if(borrowStr == null || borrowFlag == null){
				return false;
			}

			// 转为borrow对象
			redisBorrow = JSON.parseObject(borrowStr, RedisBorrow.class);

			// 标的编号为空
			if (redisBorrow.getBorrowNid() == null){
				_log.error("[" + accedeOrderId + "]" + "队列的标的编号为空！");
				return false;
			}

			// 标的无可投余额
			if (redisBorrow.getBorrowAccountWait().compareTo(BigDecimal.ZERO) <=0) {
				_log.error("[" + accedeOrderId + "]" + redisBorrow.getBorrowNid()+" 标的可投金额为 "+redisBorrow.getBorrowAccountWait());
				return false;
			}
			/*******************************************/

			boolean result = false;
			boolean isLast = false;
			String borrowNidForCredit = "";
			try {
				//TODO
				if (borrowFlag.equals(RedisConstants.HJH_BORROW_CREDIT)) {
					/** 4. 自动投资债转标的（承接）	 */
					_log.info("==[" + accedeOrderId + "]" + "自动承接债转标的" + redisBorrow.getBorrowNid());
					_log.info("[" + accedeOrderId + "]" + "承前的可投金额：" + ketouplanAmoust + "，"
							+ redisBorrow.getBorrowNid() +"可投余额：" + redisBorrow.getBorrowAccountWait());
					/** 4.1. 债转用金额计算	 */
					// 设置实际投资金额
					// 债转标的： 清算时公允价值-已投本金和已投垫付利息TODO
					BigDecimal yujiAmoust = ketouplanAmoust;
					if (yujiAmoust.compareTo(redisBorrow.getBorrowAccountWait()) >= 0) {
						// 该标的/债转最后一笔投资
						yujiAmoust = redisBorrow.getBorrowAccountWait();
						isLast = true;
					}

					/** 4.2. 获取债转详情	 */
					HjhDebtCredit credit = this.bankAutoTenderService.selectCreditByNid(redisBorrow.getBorrowNid());
					if (credit == null) {
						_log.error("[" + accedeOrderId + "]" + "债转号不存在 "+redisBorrow.getBorrowNid());
						return false;
					}

					if (credit.getCreditStatus().compareTo(3) == 0) {
						//3承接终止
						_log.warn("[" + accedeOrderId + "]" + "债转标的" + redisBorrow.getBorrowNid() + "发生还款（3），被停止债转，不再推回队列。");
						result = true;
						continue;
					}

					// 债转加redis锁，禁止还款
					boolean tranactionSetFlag = RedisUtils.tranactionSet(RedisConstants.HJH_DEBT_SWAPING + borrowNidForCredit, redisBorrow.getBorrowNid(), 300);
					if (!tranactionSetFlag) {//设置失败
						//承接终止
						_log.warn("[" + accedeOrderId + "]" + "债转标的" + redisBorrow.getBorrowNid() + "发生还款（Redis），被停止债转，不再推回队列。");
						result = true;
						continue;
					}

					/** 4.3. 校验是否可以债转	 */
					// 债权的转让人，和计划订单的投资人不能相同
					if (credit.getUserId().compareTo(hjhAccede.getUserId()) == 0) {
//						if (serialFaileCount >= CustomConstants.HJH_TENDER_SERIAL_FAILE_COUNT) {
//							// 连续10次失败后，换个计划订单投
//							_log.error("[" + accedeOrderId + "]" + "借款人/出让人和计划订单的投资人连续相同次数超过"+CustomConstants.HJH_TENDER_SERIAL_FAILE_COUNT+"次,跳过该计划订单");
//							return false;
//						}
						_log.info("[" + accedeOrderId + "]" + "债权的转让人("+credit.getUserId()+")和计划订单的投资人("+hjhAccede.getUserId()+")不能相同");
						String redisStr = JSON.toJSONString(redisBorrow);
						RedisUtils.leftpush(queueName,redisStr);//标的推回队列头，再取标的投资。
						serialFaileCount++;
						result = true;
						continue;
					}
					// credit的CreditStatus = 3 时债转停止，不再推回队列，取下一个队列中的标的
					if (credit.getCreditStatus().compareTo(3) == 0) {
						_log.info("[" + accedeOrderId + "]" + "债转号 "+redisBorrow.getBorrowNid()+"的债权已经停止债转。");
						result = true;
						continue;
					}

					/** 4.4. 调用银行自动购买债权接口	 */
					// 调用银行接口准备参数
					//获取出让用户的江西银行电子账号
					BankOpenAccount sellerBankOpenAccount = this.bankAutoTenderService.getBankOpenAccount(credit.getUserId());
					if (sellerBankOpenAccount == null) {
						_log.info("[" + accedeOrderId + "]" + "转出用户没开户 "+credit.getUserId());
						return false;
					}
					String sellerUsrcustid = sellerBankOpenAccount.getAccount();//出让用户的江西银行电子账号

					// 生成承接日志
					String orderId = GetOrderIdUtils.getOrderId2(hjhAccede.getUserId());
					// 债权承接订单日期
					String orderDate = GetDate.getServerDateTime(1, new Date());
					// 计算实际金额 保存creditTenderLog表
					Map<String, Object> resultMap = this.bankAutoTenderService.saveCreditTenderLog(credit, hjhAccede, orderId, orderDate, yujiAmoust, isLast);
					if (Validator.isNull(resultMap)) {
						throw new Exception("保存creditTenderLog表失败，计划订单号：" + hjhAccede.getAccedeOrderId());
					}
					BigDecimal assignPay = (BigDecimal) resultMap.get("assignPay");//承接支付金额
					BigDecimal assignCapital = (BigDecimal) resultMap.get("assignCapital");//承接本金
					BigDecimal serviceFee = (BigDecimal) resultMap.get("serviceFee");//承接服务费
					_log.info("[" + accedeOrderId + "]" + "承接用计算完成"
							+ "\n,分期数据结果:" + resultMap.get("assignResult")
							+ "\n,承接总额:" + resultMap.get("assignAccount")
							+ "\n,承接本金:" + resultMap.get("assignCapital")
							+ "\n,承接利息:" + resultMap.get("assignInterest")
							+ "\n,承接支付金额:" + resultMap.get("assignPay")
							+ "\n,承接垫付利息:" + resultMap.get("assignAdvanceMentInterest")
							+ "\n,承接延期利息:" + resultMap.get("assignRepayDelayInterest")
							+ "\n,承接逾期利息:" + resultMap.get("assignRepayLateInterest")
							+ "\n,分期本金:" + resultMap.get("assignPeriodCapital")
							+ "\n,分期利息:" + resultMap.get("assignPeriodInterest")
							+ "\n,分期垫付利息:" + resultMap.get("assignPeriodAdvanceMentInterest")
							+ "\n,分期承接延期利息:" + resultMap.get("assignPeriodRepayDelayInterest")
							+ "\n,分期承接延期利息:" + resultMap.get("assignPeriodRepayLateInterest")
							// add 汇计划三期 汇计划自动投资(收债转服务费) liubin 20180515 start
							+ "\n,承接服务率:" + resultMap.get("serviceApr")
							// add 汇计划三期 汇计划自动投资(收债转服务费) liubin 20180515 end
							+ "\n,承接服务费:" + resultMap.get("serviceFee"));

					// add 出让人没有缴费授权临时对应（不收取服务费） liubin 20181113 start
					if(!this.bankAutoTenderService.checkAutoPayment(credit.getCreditNid())){
						serviceFee = BigDecimal.ZERO;//承接服务费
						resultMap.put("serviceFee",BigDecimal.ZERO);
						_log.info("[" + accedeOrderId + "]" + "债权转让人未做缴费授权,该笔债权的承接服务费置为" +resultMap.get("serviceFee"));
					}
					// add 出让人没有缴费授权临时对应（不收取服务费） liubin 20181113 end

					_log.info("[" + accedeOrderId + "]" + " 银行自动购买债权接口调用前  "+credit.getCreditNid());

					//防止钱不够也承接校验
					HjhAccede hjhAccedeCheck = this.bankAutoTenderService.selectHjhAccedeByAccedeOrderId(hjhAccede.getAccedeOrderId());
					if (assignPay.compareTo(hjhAccedeCheck.getAvailableInvestAccount()) == 1) {
						_log.error("[" + accedeOrderId + "]" + " 承接支付金额" + assignPay + ">当前计划订单的剩余可投金额" + hjhAccedeCheck.getAvailableInvestAccount()
								+ "，承接操作不可，承接失败！");
						this.bankAutoTenderService.updateHjhAccede(hjhAccede,ORDER_STATUS_ERR);
						return false;
					}

					BankCallBean bean = this.bankAutoTenderService.autoCreditApi(credit, hjhAccede,hjhUserAuth,
							assignPay, assignCapital, serviceFee,
							tenderUsrcustid, sellerUsrcustid,
							orderId, orderDate, isLast);

					// 投资失败不回滚队列
					if (bean == null ) {
						_log.error("[" + accedeOrderId + "]" + "用户投资失败,银行接口返回空,债转编号："+credit.getBorrowNid() + "银行订单号："+orderId);
						this.bankAutoTenderService.updateHjhAccede(hjhAccede,ORDER_STATUS_FAIL);
						// 不再操作队列
						result = true;
						return false;
					}
					if (!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
						_log.error("[" + accedeOrderId + "]" + "用户投资失败,银行接口返回 " + bean.getRetCode()
								+ " 债转编号："+credit.getBorrowNid()+" 投资订单号："+bean.getLogOrderId());
						this.bankAutoTenderService.updateHjhAccede(hjhAccede,ORDER_STATUS_FAIL);
						// 不再操作队列
						result = true;
						return false;
					}
					_log.info("[" + accedeOrderId + "]" +" 银行自动购买债权接口成功调用后  "+credit.getBorrowNid());

					/** 4.6. 更新同步数据库	 */
					try {
						this.bankAutoTenderService.updateCredit(credit, hjhAccede, hjhPlan, bean, tenderUsrcustid, sellerUsrcustid, resultMap);
					} catch (Exception e) {
						this.bankAutoTenderService.updateHjhAccede(hjhAccede,ORDER_STATUS_FAIL);
						_log.error("[" + accedeOrderId + "]对队列[" + queueName + "]的[" + redisBorrow.getBorrowNid() + "]的投资/承接操作出现 异常 被捕捉，HjhAccede状态更新为"+ORDER_STATUS_FAIL+"，请后台异常处理。");
						e.printStackTrace();
						// 不再操作队列
						result = true;
						return false;
					}

					// add 合规数据上报 埋点 liubin 20181122 start
					Map<String, String> params = new HashMap<String, String>();
					params.put("assignOrderId", bean.getOrderId());
					params.put("flag", "2");//1（散）2（智投）
					params.put("status", "1"); //1承接（每笔）
					// 推送数据到MQ 承接（每笔）
					mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.UNDERTAKE_SINGLE_SUCCESS_DELAY_KEY, JSONObject.toJSONString(params));
					// add 合规数据上报 埋点 liubin 20181122 end

					/** 4.5. 减去被投标的可投金额，部分承接时，余额推回队列	 (20180719改银行和表更新都成功才推回队列)*/
					ketouplanAmoust = setRedisList(ketouplanAmoust, redisBorrow, queueName, assignPay, "R");
					// result = true 后继操作不再操作队列
					_log.info("==投后[" + accedeOrderId + "]" + "自动承接债转标的" + redisBorrow.getBorrowNid() + "(银行承接成功！队列可承金额更新，不可撤销)");
					_log.info("[" + accedeOrderId + "]" + "承后的可承金额：" + ketouplanAmoust + "，"
							+ redisBorrow.getBorrowNid() +"可承余额：" + redisBorrow.getBorrowAccountWait());
					// 不再操作队列
					result = true;

					/** 4.7. 完全承接时，结束债券  */
					if (redisBorrow.getBorrowAccountWait().compareTo(BigDecimal.ZERO) == 0) {
						//获取出让人投标成功的授权号
						String sellerAuthCode = this.bankAutoTenderService.getSellerAuthCode(credit.getSellOrderId(), credit.getSourceType());
						if (sellerAuthCode == null) {
							_log.info("[" + accedeOrderId + "]未取得出让人" + credit.getUserId() + "的债权类型" +
									credit.getSourceType() + "(1原始0原始)的授权码，结束债权失败。");
						}

						// add 合规数据上报 埋点 liubin 20181122 start
						// 推送数据到MQ 承接（完全）
						params = new HashMap<String, String>();
						params.put("creditNid", credit.getCreditNid());
						params.put("flag", "2"); //1（散）2（智投）
						params.put("status", "2"); //2承接（完全）
						this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.UNDERTAKE_ALL_SUCCESS_DELAY_KEY, JSONObject.toJSONString(params));
						// add 合规数据上报 埋点 liubin 20181122 end

						//调用银行结束债权接口
						boolean ret = this.creditService.requestDebtEnd(credit, sellerUsrcustid, sellerAuthCode);
						if (!ret) {
							_log.info("[" + accedeOrderId + "]被承接标的" + redisBorrow.getBorrowNid() +"被完全承接，银行结束债权失败。");
						}
						_log.info("[" + accedeOrderId + "]被承接标的" + redisBorrow.getBorrowNid() +"被完全承接，银行结束债权成功。");
						//银行结束债权后，更新债权表为完全承接
						ret = this.bankAutoTenderService.updateCreditForEnd(credit);
						if (!ret) {
							_log.info("[" + accedeOrderId + "]银行结束债权后，更新债权表为完全承接失败。");
						}
					}
				}else if(borrowFlag.equals(RedisConstants.HJH_BORROW_INVEST)) {
					/** 5. 自动投资原始标的（投资）	 */
					_log.info("==投前[" + accedeOrderId + "]" + "自动投资原始标的" + redisBorrow.getBorrowNid());
					_log.info("[" + accedeOrderId + "]" + "投前的可投金额：" + ketouplanAmoust + "，" + "投前的本组金额：" + groupAmoust + "，"
							+ redisBorrow.getBorrowNid() +"可投余额：" + redisBorrow.getBorrowAccountWait());
					/** 5.1. 投资用金额计算	 */
					// 设置实际投资金额
					// 原始标的： 标的本金-已投本金
					BigDecimal realAmoust = groupAmoust;
					if (realAmoust.compareTo(redisBorrow.getBorrowAccountWait()) >= 0) {
						// 该标的/债转最后一笔投资
						realAmoust = redisBorrow.getBorrowAccountWait();
						isLast = true;
					}

					/** 5.2. 获取标的详情	 */
					Borrow borrow = this.bankAutoTenderService.selectBorrowByNid(redisBorrow.getBorrowNid());
					if (borrow == null) {
						_log.error("[" + accedeOrderId + "]" + "标的号不存在 "+redisBorrow.getBorrowNid());
						return false;
					}

					/** 5.3. 校验是否可以投资	 */
					// 借款人和计划订单的投资人不能相同
					if (borrow.getUserId().compareTo(hjhAccede.getUserId()) == 0) {
//						if (serialFaileCount >= CustomConstants.HJH_TENDER_SERIAL_FAILE_COUNT) {
//							// 连续10次失败后，换个计划订单投
//							_log.error("[" + accedeOrderId + "]" + "借款人/出让人和计划订单的投资人连续相同次数超过"+CustomConstants.HJH_TENDER_SERIAL_FAILE_COUNT+"次,跳过该计划订单");
//							return false;
//						}
						_log.info("[" + accedeOrderId + "]" + "借款人("+borrow.getUserId()+")和计划订单的投资人("+hjhAccede.getUserId()+")不能相同");
						String redisStr = JSON.toJSONString(redisBorrow);
						RedisUtils.leftpush(queueName,redisStr);//标的推回队列头，再取标的投资。
						serialFaileCount++;
						result = true;
						continue;
					}
					//防止钱不够也投资校验
					HjhAccede hjhAccedeCheck = this.bankAutoTenderService.selectHjhAccedeByAccedeOrderId(hjhAccede.getAccedeOrderId());
					if (realAmoust.compareTo(hjhAccedeCheck.getAvailableInvestAccount()) == 1) {
						_log.error("[" + accedeOrderId + "]" + " 投资支付金额" + realAmoust + ">当前计划订单的剩余可投金额" + hjhAccedeCheck.getAvailableInvestAccount()
								+ "，投资操作不可，投资失败！");
						this.bankAutoTenderService.updateHjhAccede(hjhAccede,ORDER_STATUS_ERR);
						return false;
					}

					/** 5.4. 调用银行自动投标申请接口	 */
					_log.info("[" + accedeOrderId + "]" + " 银行自动投标申请接口调用前  "+borrow.getBorrowNid());

					BankCallBean bean = this.bankAutoTenderService.autotenderApi(borrow, hjhAccede,hjhUserAuth,realAmoust, tenderUsrcustid, isLast);

					// 投资失败不回滚队列
					if (bean == null ) {
						_log.error("[" + accedeOrderId + "]" + "用户投资失败,银行接口返回空,标的编号："+borrow.getBorrowNid());
						this.bankAutoTenderService.updateHjhAccede(hjhAccede,ORDER_STATUS_FAIL);
						// 不再操作队列
						result = true;
						return false;
					}
					if (!BankCallConstant.RESPCODE_SUCCESS.equals(bean.getRetCode())) {
						_log.error("[" + accedeOrderId + "]" + "用户投资失败,银行接口返回 " + bean.getRetCode()
								+ " 标的编号："+borrow.getBorrowNid()+" 投资订单号："+bean.getLogOrderId());
						this.bankAutoTenderService.updateHjhAccede(hjhAccede,ORDER_STATUS_FAIL);
						// 不再操作队列
						result = true;
						return false;
					}

					_log.info("[" + accedeOrderId + "]" +" 银行自动投标申请接口成功调用后  "+borrow.getBorrowNid());

					/** 5.6. 更新同步数据库	 */
					// 单笔标的投资
					try {
//						result = this.autoTenderService.updateBorrow(borrow, hjhAccede, realAmoust,bean,redisBorrow);
						this.bankAutoTenderService.updateBorrow(borrow, hjhAccede, realAmoust, bean);
					} catch (Exception e) {
						this.bankAutoTenderService.updateHjhAccede(hjhAccede,ORDER_STATUS_FAIL);
						_log.error("[" + accedeOrderId + "]对队列[" + queueName + "]的[" + redisBorrow.getBorrowNid() + "]的投资/承接操作出现 异常 被捕捉，HjhAccede状态更新为"+ORDER_STATUS_FAIL+"，请后台异常处理。");
						e.printStackTrace();
						// 不再操作队列
						result = true;
						return false;
					}
					// add by liushouyi nifa2 20181204 start
					if(redisBorrow.getBorrowAccountWait().compareTo(realAmoust) == 0){
						// 满标发送满标状态埋点
						// 发送发标成功的消息队列到互金上报数据
						Map<String, String> params = new HashMap<String, String>();
						params.put("borrowNid", borrow.getBorrowNid());
						this.mqService.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.ISSUE_INVESTED_DELAY_KEY, JSONObject.toJSONString(params));
					}
					// add by liushouyi nifa2 20181204 end
					/** 5.5. 减去被投标的可投金额，部分投资时，余额推回队列	  (20180719改银行和表更新都成功才推回队列)*/
					// add 汇计划三期 汇计划自动投资(分散投资) liubin 20180515 start
					if (diversifyCount < 0){
						// 不分散投资（推回投资主队列）
						ketouplanAmoust = setRedisList(ketouplanAmoust, redisBorrow, queueName, realAmoust,"R");
					}else{
						// 分散投资（推回投资临时队列）
						ketouplanAmoust = setRedisList(ketouplanAmoust, redisBorrow, queueName + RedisConstants.HJH_SLASH_TMP, realAmoust
								, "L");
						diversifyCount += 1;
						if (diversifyCount >= groupCount) {
							// 最后一次分散投资，临时队列推回主队列
							// ★临时队列有标的时，先推回主队列（恢复前次分散投资）★
							RedisUtils.lpoprpush(queueName + RedisConstants.HJH_SLASH_TMP, queueName);
						}
					}
					// add 汇计划三期 汇计划自动投资(分散投资) liubin 20180515 end

					// result = true 后继操作不再操作队列
					_log.info("==投后[" + accedeOrderId + "]" + "自动投资原始标的" + redisBorrow.getBorrowNid() + "(银行投资冻结成功！队列可投金额更新，不可撤销)");
					_log.info("[" + accedeOrderId + "]" + "投后的可投金额：" + ketouplanAmoust + "，"
							+ redisBorrow.getBorrowNid() +"可投余额：" + redisBorrow.getBorrowAccountWait());
					// 不再操作队列
					result = true;

				}else {
					_log.error("[" + accedeOrderId + "]" + "该计划没有可投标的！");
					return false;
				}
			} catch (Exception e) {
				this.bankAutoTenderService.updateHjhAccede(hjhAccede,ORDER_STATUS_ERR);
				_log.error("[" + accedeOrderId + "]对队列[" + queueName + "]的[" + redisBorrow.getBorrowNid() + "]的投资/承接操作出现 异常 被捕捉，HjhAccede状态更新为"+ORDER_STATUS_ERR+"，请后台异常处理。");
				e.printStackTrace();
				return false;
			}finally{
				//删除债转中的redis，可以还款
				RedisUtils.del(RedisConstants.HJH_DEBT_SWAPING + borrowNidForCredit);
				if(result){
				}else{
					String redisStr = JSON.toJSONString(redisBorrow);
					RedisUtils.rightpush(queueName,redisStr);//redis相应计划//TODO:可能放两遍
//				    break;
				}
			}
		}

		//投资完成更新计划明细
		// 如果计划成功，则更新计划表为投资完成
		if(ketouplanAmoust.compareTo(minAccountEnable) == -1){
			// 0投资
			if (hjhAccede.getOrderStatus() == 0) {
				this.bankAutoTenderService.updateHjhAccede(hjhAccede,2);
			}
			// 1复投 不更新
		}

		return true;
	}

	// add 汇计划三期 汇计划自动投资(分散投资) liubin 20180515 start
	/**
	 * @param ketouplanAmoust
	 * @param redisBorrow
	 * @param queueName
	 * @param realAmoust
	 * @return
	 */
	private BigDecimal setRedisList(BigDecimal ketouplanAmoust, RedisBorrow redisBorrow, String queueName,
									BigDecimal realAmoust, String pushFlag) {
		// 投资成功后减掉redis 钱 
		redisBorrow.setBorrowAccountWait(redisBorrow.getBorrowAccountWait().subtract(realAmoust));
		ketouplanAmoust = ketouplanAmoust.subtract(realAmoust);

		// 银行成功后，如果标的可投金额非0，推回队列的尾部，标的可投金额为0，不再推回队列
		if (redisBorrow.getBorrowAccountWait().compareTo(BigDecimal.ZERO) != 0) {
			String redisStr = JSON.toJSONString(redisBorrow);
			if (pushFlag.toUpperCase().equals("R")){
				RedisUtils.rightpush(queueName,redisStr);
				_log.info("推回队列的尾部[" + queueName + "]r" + " : " + redisStr);
			}else{
				RedisUtils.leftpush(queueName,redisStr);
				_log.info("推回队列的头部l[" + queueName + "]" + " : " + redisStr);
			}

		}
		return ketouplanAmoust;
	}
	// add 汇计划三期 汇计划自动投资(分散投资) liubin 20180515 end

	/**
	 * 取得最小可投资/复投金额的条件
	 * @param hjhAccede
	 * @return
	 */
	private BigDecimal getMinAccountEnable(HjhAccede hjhAccede) {
		BigDecimal minAccountEnable = null;
		if (hjhAccede.getOrderStatus() == 0) {
			// 0投资
			minAccountEnable = CustomConstants.HJH_TENDER_MIN_ACCOUNT;
		}else {
			// 1复投
			minAccountEnable = CustomConstants.HJH_RETENDER_MIN_ACCOUNT;
		}
		return minAccountEnable;
	}

	/**
	 * 从队列中取得borrow
	 * @param queueName
	 * @return JsonString
	 */
	private String getBorrowFromQueue(String queueName) {
		String borrowStr = null;//标的内容JsonString
		borrowStr = RedisUtils.rpop(queueName);
		if(borrowStr == null) {
			_log.info("队列" + queueName + "中没有标的");
		}else {
			_log.info("队列" + queueName + "中取得标的");
		}
		return borrowStr;
	}

}
