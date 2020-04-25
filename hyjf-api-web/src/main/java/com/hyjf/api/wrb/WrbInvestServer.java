package com.hyjf.api.wrb;

import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.WrbParseParamUtil;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.wrb.WrbBorrowListCustomize;
import com.hyjf.mybatis.model.customize.wrb.WrbBorrowTenderCustomize;
import com.hyjf.mybatis.model.customize.wrb.WrbBorrowTenderSumCustomize;
import com.hyjf.mybatis.model.customize.wrb.WrbInvestRecordCustomize;
import com.hyjf.wrb.WrbResponse;
import com.hyjf.wrb.invest.WrbBorrowReturnBean;
import com.hyjf.wrb.invest.WrbInvestDefine;
import com.hyjf.wrb.invest.WrbInvestServcie;
import com.hyjf.wrb.invest.request.*;
import com.hyjf.wrb.invest.response.*;
import com.hyjf.wrb.invest.response.WrbInvestResponse.InvestDetail;
import com.hyjf.wrb.invest.response.wrbInvestRecoverPlanResponse.WrbRecoverRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.StringUtils.isBlank;

@RestController
@RequestMapping(WrbInvestDefine.REQUEST_MAPPING)
public class WrbInvestServer {
	private Logger logger = LoggerFactory.getLogger(WrbInvestServer.class);

	@Autowired
	private WrbInvestServcie wrbInvestServcie;

	/**
	 * 获取某天出借情况
	 *
	 * @param param
	 * @param sign
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(WrbInvestDefine.DAY_INVEST_LIST)
	public WrbInvestResponse getInvestDetail(@RequestParam String param,
											 @RequestParam(value = "sign", required = false) String sign) {
		logger.info("获取某天出借情况, param is :{}, sign is :{}", param, sign);

		WrbInvestResponse response = new WrbInvestResponse();

        WrbInvestRequest request = null;
        try {
            request = WrbParseParamUtil.mapToBean(WrbParseParamUtil.parseParam(param), WrbInvestRequest.class);
        } catch (Exception e) {
            logger.error("参数解析失败....", e);
            response.setRetcode(WrbResponse.FAIL_RETCODE);
            response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
            return response;
        } finally {
            if (request == null) {
                response.setRetcode(WrbResponse.FAIL_RETCODE);
                response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
                return response;
            }
        }

        Date invest_date = null;
        try {
            invest_date = GetDate.stringToDate2(request.getInvest_date());
        } catch (Exception e) {
            response.setRetcode(WrbResponse.FAIL_RETCODE);
            response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
            return response;
        }
        if(invest_date == null){
            logger.error("日期参数有误");
            response.setRetcode(WrbResponse.FAIL_RETCODE);
            response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
            return response;
        }
		Integer limit = request.getLimit();
		Integer page = request.getPage();

		if (limit == null || page == null) {
            response.setRetcode(WrbResponse.FAIL_RETCODE);
            response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
            return response;
        }

		// 查询出借情况
		List<BorrowTender> borrowTenderList = wrbInvestServcie.getInvestDetail(invest_date, limit, page);

		WrbInvestResponse wrbInvestResponse = new WrbInvestResponse();
		List<InvestDetail> detailList = new ArrayList<>();
		if (!CollectionUtils.isEmpty(borrowTenderList)) {
            for (BorrowTender borrowTender : borrowTenderList) {
                InvestDetail investDetail = new InvestDetail();
                investDetail.setIndex(borrowTender.getNid());// 订单号
                String borrowNid = borrowTender.getBorrowNid();
                Borrow borrow = wrbInvestServcie.selectBorrowByBorrowNid(borrowNid);
                if (borrow != null) {
                    investDetail.setBorrow_id(String.valueOf(borrow.getUserId()));// 借款人ID
                }
                String userName = borrowTender.getTenderUserName();
                if (userName != null) {
                    userName = userName.substring(0, 1).concat("**");
                }
                investDetail.setInvest_user(userName);
                Integer addtime = borrowTender.getAddtime();
                investDetail.setInvest_time(GetDate.timestamptoNUMStrYYYYMMDDHHMMSS(addtime));
                investDetail.setInvest_money(borrowTender.getAccount());
                investDetail.setBid_id(borrowNid);
                detailList.add(investDetail);
            }
            wrbInvestResponse.setInvest_list(detailList);
        }
		return wrbInvestResponse;

	}

	/**
	 * 获取某天汇总数据
	 *
	 * @param param
	 * @param sign
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(WrbInvestDefine.GET_DAY_SUM)
	public WrbInvestSumResponse getSum(@RequestParam String param,
									   @RequestParam(value = "sign", required = false) String sign) {
		logger.info("获取某天汇总数据, param is :{}, sign is :{}", param, sign);
		WrbInvestSumResponse response = new WrbInvestSumResponse();

        WrbInvestSumRequest request = null;
        try {
            request = WrbParseParamUtil.mapToBean(WrbParseParamUtil.parseParam(param), WrbInvestSumRequest.class);
        } catch (Exception e) {
            logger.error("参数解析失败....", e);
            response.setRetcode(WrbResponse.FAIL_RETCODE);
            response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
            return response;
        } finally {
            if (request == null) {
                response.setRetcode(WrbResponse.FAIL_RETCODE);
                response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
                return response;
            }
        }

        Date date = null;
        try {
            date = GetDate.stringToDate2(request.getDate());
        } catch (Exception e) {
            response.setRetcode(WrbResponse.FAIL_RETCODE);
            response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
            return response;
        }
        if(date == null){
            logger.error("参数解析失败....");
            response.setRetcode(WrbResponse.FAIL_RETCODE);
            response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
            return response;
        }
		response = wrbInvestServcie.getDaySum(date);

		return response;
	}

	/**
	 * 查询标的出借情况
	 *
	 * @param param
	 * @param sign
	 */
	@RequestMapping(WrbInvestDefine.BID_INVEST_LIST)
	public WrbBorrowInvestResponse getBorrowInvest(@RequestParam String param,
												   @RequestParam(value = "sign", required = false) String sign) {
		logger.info("查询标的出借情况, param is :{}, sign is :{}", param, sign);
		WrbBorrowInvestResponse response = new WrbBorrowInvestResponse();

		WrbBorrowInvestRequest request = null;
		try {
			request = WrbParseParamUtil.mapToBean(WrbParseParamUtil.parseParam(param), WrbBorrowInvestRequest.class);
		} catch (Exception e) {
			logger.error("参数解析失败....", e);
			response.setRetcode(WrbResponse.FAIL_RETCODE);
			response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
			return response;
		} finally {
			if (request == null) {
				response.setRetcode(WrbResponse.FAIL_RETCODE);
				response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
				return response;
			}
		}

		// 标的id
		String borrowNid;
		// 字符串时间转换为秒数
		int investTime;
		borrowNid = request.getId();
        // 出借开始时间
        String startTime = request.getStart_time();
        if (isBlank(borrowNid)) {
            response.setRetcode(WrbResponse.FAIL_RETCODE);
            response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
            return response;
        }
		try {
            if (startTime == null) {
                Borrow borrow = wrbInvestServcie.selectBorrowByBorrowNid(borrowNid);
                if (borrow == null) {
                    response.setRetcode(WrbResponse.FAIL_RETCODE);
                    response.setRetmsg("标的不存在");
                    return response;
                }

                investTime = Integer.parseInt(borrow.getAddtime());
            } else {
                investTime = (int) ((GetDate.stringToDate(startTime).getTime()) / 1000);
            }
		} catch (Exception e) {
			response.setRetcode(WrbResponse.FAIL_RETCODE);
			response.setRetmsg("参数有误");
			return response;
		}

		// 1. 查询出借明细
		List<WrbBorrowTenderCustomize> investList = wrbInvestServcie.searchBorrowTenderByNidAndTime(borrowNid,
				investTime);
        response.setInvest_list(this.copyBorrowTenderToResponse(investList) == null ? new ArrayList<WrbBorrowInvestResponse.InvestInfo>() : copyBorrowTenderToResponse(investList));

		// 2. 查询出借汇总数据
		WrbBorrowTenderSumCustomize sumCustomize = wrbInvestServcie.searchBorrowTenderSumByNidAndTime(borrowNid,
				investTime);
		if (sumCustomize != null) {
            response.setAll_investors(sumCustomize.getAllInvestors() == null ? "" : sumCustomize.getAllInvestors());
            response.setInvest_all_money(sumCustomize.getInvestAllMoney() == null ? BigDecimal.ZERO : sumCustomize.getInvestAllMoney());
            response.setFirst_invest_time(sumCustomize.getFirstInvestTime() == null ? "" : sumCustomize.getFirstInvestTime());
            response.setLast_invest_time(sumCustomize.getLastInvestTime() == null ? "" : sumCustomize.getFirstInvestTime());
            response.setBorrow_id(sumCustomize.getBorrowId());
		
		}

		return response;
	}

	/**
     * 根据平台用户id获取账户信息
     * @param param
     * @param sign
     * @return
     */
    @RequestMapping(WrbInvestDefine.GET_ACCOUNT_INFO)
    public WrbAccountResponse getAccountInfo(@RequestParam String param,
                                             @RequestParam(value = "sign", required = false) String sign) {
        logger.info("查询账户信息, param is :{}, sign is :{}", param, sign);

        WrbAccountResponse response = new WrbAccountResponse();

        WrbAccountRequest request = null;
        try {
            request = WrbParseParamUtil.mapToBean(WrbParseParamUtil.parseParam(param), WrbAccountRequest.class);
        } catch (Exception e) {
            logger.error("参数解析失败....", e);
            response.setRetcode(WrbResponse.FAIL_RETCODE);
            response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
            return response;
        } finally {
            if (request == null) {
                response.setRetcode(WrbResponse.FAIL_RETCODE);
                response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
                return response;
            }
        }

        // 获取平台用户id
        String userId = request.getPf_user_id();

        if (isBlank(userId)) {
            response.setRetcode(WrbResponse.FAIL_RETCODE);
            response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
            return response;
        }

        // 获取优惠券信息
        List<CouponUser> couponUserList = wrbInvestServcie.getCouponInfo(userId);
        StringBuffer reward = new StringBuffer();
        if (!CollectionUtils.isEmpty(couponUserList)) {
            for (int i = 0; i < couponUserList.size(); i++) {
                CouponUser couponUser = couponUserList.get(i);
                reward.append(convertCouponInfo(couponUser));
                if (i != couponUserList.size() - 1) {
                    reward.append(";");
                }
            }
            response.setReward(reward.toString());
        }

        Account account = wrbInvestServcie.getAccountInfo(userId);
        if (account != null) {
            response.setPf_user_id(String.valueOf(account.getUserId()));
            response.setAll_balance(account.getBankTotal());
            response.setAvailable_balance(account.getBankBalance());
            response.setFrozen_money(account.getBankFrost());
            response.setInvesting_principal(account.getBankAwaitCapital());
            response.setInvesting_interest(account.getBankAwaitInterest());
            response.setEarned_interest(account.getBankInterestSum());
        }
        response.setCurrent_money(BigDecimal.ZERO);
        return response;
    }


    /**
     * 标的查询接口
     * @param param
     * @param sign
     * @return
     */
	@RequestMapping(WrbInvestDefine.BID_BORROW_LIST)
	public WrbBorrowReturnBean getBorrowList(@RequestParam String param,
			@RequestParam(value = "sign", required = false) String sign) {
		logger.info("标的查询接口, param is :{}, sign is :{}", param, sign);
		WrbBorrowReturnBean response = new WrbBorrowReturnBean();
		Map<String, String> request = WrbParseParamUtil.parseParam(param);
		try {
			// 标的id
			String borrowNid = request.get("invest_id");

			// 1. 查询出借明细
			List<WrbBorrowListCustomize> investList = wrbInvestServcie.searchBorrowListByNid(borrowNid);
			response.setInvest_list(investList);
		} catch (Exception e) {
			response.setRetcode(WrbResponse.FAIL_RETCODE);
			response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
		}


		return response;
	}

    /**
     * 查询出借记录
     * @param param
     * @param sign
     * @return
     */
    @RequestMapping(WrbInvestDefine.INVEST_RECORD)
	public WrbInvestRecordResponse getInvestRecord(@RequestParam String param,
                                                   @RequestParam(value = "sign", required = false) String sign) {
        logger.info("查询出借记录, param is :{}, sign is :{}", param, sign);
        WrbInvestRecordResponse response = new WrbInvestRecordResponse();

        WrbInvestRecordRequest request = null;
        try {
            request = WrbParseParamUtil.mapToBean(WrbParseParamUtil.parseParam(param), WrbInvestRecordRequest.class);

        } catch (Exception e) {
            logger.error("参数解析失败....", e);
            response.setRetcode(WrbResponse.FAIL_RETCODE);
            response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
            return response;
        } finally {
            if (request == null) {
                response.setRetcode(WrbResponse.FAIL_RETCODE);
                response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
                return response;
            }
        }

        // 用户id
        String userId = request.getPf_user_id();
        Date startTime = null;
        String timeStart = request.getStart_time();
        String timeEnd = request.getEnd_time();
        Date endTime = null;
        try {
            if (timeStart != null) {
                // 开始时间
                startTime = GetDate.stringToDate(timeStart);
            }

            endTime = null;
            if (timeStart != null) {
                // 结束时间
                endTime = GetDate.stringToDate(timeEnd);
            }
        } catch (Exception e) {
            logger.error("时间格式有误......");
            response.setRetcode(WrbResponse.FAIL_RETCODE);
            response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
            return response;
        }

        // 偏移(页数)
        Integer offset = request.getOffset();
        // 每页查询条数
        Integer limit = request.getLimit();
        // 出借记录id
        String investRecordId = request.getInvest_record_id();
        //出借状态
        Integer investStatus = request.getInvest_status();
        // 必填参数校验
        if (isBlank(userId) || offset == null || limit == null) {
            logger.error("缺少必填参数");
            response.setRetcode(WrbResponse.FAIL_RETCODE);
            response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
            return response;
        }

        // 获取出借记录详情
        try {
            List<WrbInvestRecordCustomize> recordList = wrbInvestServcie.getInvestRecord(request);
            response.setPf_user_id(userId);
            response.setInvest_records(recordList);
        } catch (Exception e) {
            logger.error("获取出借记录失败, param is :{}", param);
            response.setRetcode(WrbResponse.FAIL_RETCODE);
            response.setRetmsg("获取出借记录失败");
            response.setPf_user_id(userId);
        }

        return response;
    }

    /**
     * 出借记录回款计划查询
     * @param param
     * @param sign
     * @return
     */
    @RequestMapping(WrbInvestDefine.RECOVER_PLAN)
    public wrbInvestRecoverPlanResponse getRecoverPlan(@RequestParam String param,
                                                       @RequestParam(value = "sign", required = false) String sign) {

        logger.info("出借记录回款计划, param is :{}, sign is :{}", param, sign);
        wrbInvestRecoverPlanResponse response = new wrbInvestRecoverPlanResponse();

        WrbInvestRecoverPlanRequest request = null;
        try {
            request = WrbParseParamUtil.mapToBean(WrbParseParamUtil.parseParam(param), WrbInvestRecoverPlanRequest.class);

        } catch (Exception e) {
            logger.error("参数解析失败....", e);
            response.setRetcode(WrbResponse.FAIL_RETCODE);
            response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
            return response;
        } finally {
            if (request == null) {
                response.setRetcode(WrbResponse.FAIL_RETCODE);
                response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
                return response;
            }
        }
        // 平台用户id
        String userId = request.getPf_user_id();
        // 出借记录id
        String investRecordId = request.getInvest_record_id();
        // 项目id
        String borrowNid = request.getBid_id();

        response.setInvest_record_id(investRecordId);


        // 必填参数校验
        if (isBlank(userId) || isBlank(investRecordId) || isBlank(borrowNid)) {
            logger.error("缺少必填参数");
            response.setRetcode(WrbResponse.FAIL_RETCODE);
            response.setRetmsg(WrbResponse.FAIL_RETMESSAGE);
            return response;
        }

        List<WrbRecoverRecord> recoverRecordList = wrbInvestServcie.getRecoverPlan(userId, investRecordId, borrowNid);

        response.setBack_records(recoverRecordList);

        return response;
    }


    private List<WrbBorrowInvestResponse.InvestInfo> copyBorrowTenderToResponse(List<WrbBorrowTenderCustomize> list) {
        List<WrbBorrowInvestResponse.InvestInfo> investInfoList = null;
        if (!CollectionUtils.isEmpty(list)) {
            investInfoList = new ArrayList<>();
            WrbBorrowInvestResponse.InvestInfo investInfo = null;
            for (WrbBorrowTenderCustomize customize : list) {
                investInfo = new WrbBorrowInvestResponse.InvestInfo();
                investInfo.setIndex(customize.getNid());
                investInfo.setInvest_money(customize.getAccount());
                investInfo.setInvest_time(customize.getInvestTime());
                investInfo.setInvest_user(customize.getUsername());
                investInfoList.add(investInfo);
            }
        }
        return investInfoList;
    }


    /**
     * 格式化优惠券信息
     * @param couponUser
     * @return
     */
    private StringBuffer convertCouponInfo(CouponUser couponUser) {
        StringBuffer sbf = new StringBuffer();
        // 优惠券编号
        String couponCode = couponUser.getCouponCode();
        CouponConfig couponConfig = wrbInvestServcie.getCouponByCouponCode(couponCode);
        if(couponConfig != null){
            Integer couponType = couponConfig.getCouponType();
            if (1 == couponType) {
                sbf.append("体验金-");
                sbf.append(couponConfig.getCouponQuota()).append("-元-");
            } else if (2 == couponType) {
                sbf.append("加息券-");
                sbf.append(couponConfig.getCouponQuota()).append("%-无-");
            } else {
                sbf.append("代金券-");
                sbf.append(couponConfig.getCouponQuota()).append("-元-");
            }

            // 获取优惠券开始时间
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Integer add = couponConfig.getAddTime();
            String addTime = "无";//开始和生效时间
            if(add != null){
                long addLong = add.longValue() * 1000;
                Date addTimedate = new Date(addLong);
                addTime = format.format(addTimedate);
            }

            // 获取优惠券结束时间
            Integer end = couponConfig.getExpirationDate();
            String endTime = "无";//结束时间
            if (end != null) {
                long endLong = end.longValue() * 1000;
                Date expirationDate = new Date(endLong);
                endTime = format.format(expirationDate);
            }
            sbf.append(addTime).append("-").append(addTime).append("-").append(endTime);//开始时间与生效时间一样
        }
        return sbf;
    }
}
