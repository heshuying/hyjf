/**
 * 计划
 */
package com.hyjf.web.bank.wechat.hjh.invest;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.DuePrincipalAndInterestUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.*;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.mybatis.model.customize.coupon.UserCouponConfigCustomize;
import com.hyjf.mybatis.model.customize.web.htj.DebtPlanDetailCustomize;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.pay.lib.bank.util.BankCallMethodConstant;
import com.hyjf.pay.lib.bank.util.BankCallStatusConstant;
import com.hyjf.pay.lib.bank.util.BankCallUtils;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.web.BaseServiceImpl;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service("wxhjhPlanServiceImpl")
public class PlanServiceImpl extends BaseServiceImpl implements PlanService {

    private Logger logger = LoggerFactory.getLogger(PlanServiceImpl.class);

    /** Jedis  */
    public static JedisPool pool = RedisUtils.getPool();

    @Autowired
    @Qualifier("myAmqpTemplate")
    private RabbitTemplate rabbitTemplate;

    @Override
    public DebtPlanDetailCustomize selectDebtPlanDetail(String planNid) {
        DebtPlanDetailCustomize planDetail = this.hjhPlanCustomizeMapper.selectDebtPlanDetail(planNid);
        return planDetail;
    }

    @Override
    public HjhPlan getPlanByNid(String planNid) {
        HjhPlanExample example = new HjhPlanExample();
        HjhPlanExample.Criteria cri = example.createCriteria();
        cri.andPlanNidEqualTo(planNid);
        List<HjhPlan> debtBorrowList = hjhPlanMapper.selectByExample(example);
        if (debtBorrowList != null && debtBorrowList.size() > 0) {
            return debtBorrowList.get(0);
        } else {
            return null;
        }
    }

    @Override
    public UserCouponConfigCustomize getUserOptimalCoupon(String couponId, String planNid, Integer userId, String money,
        String platform) {
        UserCouponConfigCustomize couponConfig = new UserCouponConfigCustomize();
        if (couponId == null || "".equals(couponId) || couponId.length() == 0) {
            // 获取用户最优优惠券
            couponConfig = getBestCoupon(planNid, userId, money, platform);
        } else {
            couponConfig = couponConfigCustomizeMapper.getBestCouponById(couponId);
        }
        return couponConfig;
    }
    
    private UserCouponConfigCustomize getBestCoupon(String planNid, Integer userId, String money, String platform) {
        if (money == null || "".equals(money) || money.length() == 0) {
            money = "0";
        }
        UserCouponConfigCustomize couponConfig = null;
        JSONObject jsonObject = CommonSoaUtils.getBestCoupon(planNid, userId, money, platform);
        String couponConfigJson = (String) jsonObject.get("couponConfigJson");
        if (couponConfigJson != null && couponConfigJson.length() > 0) {
            couponConfig =JSONObject.toJavaObject(JSONObject.parseObject(couponConfigJson), UserCouponConfigCustomize.class);
        }
        return couponConfig;
    }
    
    @Override
    public void setProspectiveEarnings(InvestInfoResultVo resultVo, UserCouponConfigCustomize couponConfig,
        String planNid, Integer userId, String platform, String money) {
        DecimalFormat df = CustomConstants.DF_FOR_VIEW;
        df.setRoundingMode(RoundingMode.FLOOR);
        HjhPlan plan = getPlanByNid(planNid);
        if (null != plan) {
            BigDecimal earnings = new BigDecimal("0");
            // 如果出借金额不为空
            if (!StringUtils.isBlank(money) && Long.parseLong(money) > 0||!(StringUtils.isEmpty(money) && couponConfig != null && couponConfig.getCouponType() == 1 && couponConfig.getAddFlg() == 1)) {
                // 收益率
                BigDecimal borrowApr = plan.getExpectApr();
                // 周期
                Integer borrowPeriod = plan.getLockPeriod();
                // 还款方式
                String borrowStyle = plan.getBorrowStyle();//endday
                
                if (StringUtils.isNotEmpty(borrowStyle)) {
                    if (StringUtils.equals("endday", borrowStyle)){
                        // 还款方式为”按天计息，到期还本还息“：历史回报=出借金额*年化收益÷365*锁定期；
                        earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(money), borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
                    } else {
                        // 还款方式为”按月计息，到期还本还息“：历史回报=出借金额*年化收益÷12*月数；
                        earnings = DuePrincipalAndInterestUtils.getMonthInterest(new BigDecimal(money), borrowApr.divide(new BigDecimal("100")), borrowPeriod).divide(new BigDecimal("1"), 2, BigDecimal.ROUND_DOWN);
                    }
                    resultVo.setProspectiveEarnings(InvestDefine.PROSPECTIVE_EARNINGS + df.format(earnings) + "元");
                    resultVo.setInterest(df.format(earnings));
                }   
            } else {
                resultVo.setProspectiveEarnings(InvestDefine.PROSPECTIVE_EARNINGS + "0元");
                resultVo.setInterest(df.format(earnings));
            }
        }
    }

    // 汇计划参数校验
    @Override
    public JSONObject checkHJHParam(String planNid, String account, String userId, String platform,
        CouponConfigCustomizeV2 cuc) {
        // 当前用户
        Users user = this.getUsers(Integer.parseInt(userId));

        UsersInfoExample usersInfoExample = new UsersInfoExample();
        usersInfoExample.createCriteria().andUserIdEqualTo(Integer.parseInt(userId));
        List<UsersInfo> usersInfos = this.usersInfoMapper.selectByExample(usersInfoExample);
        // (1.1)判断 用户是不是担保机构 + 用户是不是被禁用
        if (null != usersInfos && usersInfos.size() == 1) {
            String roleIsOpen = PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN);
            if(StringUtils.isNotBlank(roleIsOpen) && roleIsOpen.equals("true")){
                if (usersInfos.get(0).getRoleId() != 1) {// 担保机构用户
                    return hjhResultMess("仅限出借人进行出借");
                }
            }


        } else {
            return hjhResultMess("账户信息异常");
        }
        // 判断用户是否禁用
        if (user.getStatus() == 1) {// 0启用，1禁用
            return hjhResultMess("抱歉，您的账户已被禁用，如有疑问请联系客服");
        } else {
            // 用户存在且用户未被禁用
            /**
             * autoiStatus 自动投标授权状态 0: 未授权 1:已授权',
             * autocStatus 自动债转授权状态 0: 未授权 1:已授权',
             * invesTime 自动投标授权时间
             * iorderId 自动投标订单号
             */
            Map<String, Object> map = hjhPlanCustomizeMapper.selectUserAppointmentInfo(userId);
            // 为空则无授权
            if (map == null || map.isEmpty()) {
                return hjhResultMess("该产品需开通自动投标功能");
            } else {
                if (map.get("autoiStatus") == null || map.get("autoiStatus").toString().equals("0")) {
                    return hjhResultMess("该产品需开通自动投标功能");
                }
                if (map.get("autocStatus") == null || map.get("autocStatus").toString().equals("0")) {
                    return hjhResultMess("该产品需开通自动债转功能");
                }
            }
            HjhPlan plan = this.getPlanByNid(planNid);

            if (plan.getPlanInvestStatus() == 2) {
                return hjhResultMess("此计划项目已经关闭");
            }
            // 查看江西银行账户
            BankOpenAccount bankOpenAccount = this.getBankOpenAccount(Integer.parseInt(userId));
            // 用户未在平台开户 且 未开户
            if (bankOpenAccount == null || user.getBankOpenAccount() == 0) {
                return hjhResultMess("用户开户信息不存在");
            } else {
                // 判断借款人开户信息是否存在
                if (bankOpenAccount.getUserId() == null && bankOpenAccount.getAccount() == null) {
                    return hjhResultMess("用户银行客户号不存在");
                } else {
                    // 判断借款编号是否存在
                    if (StringUtils.isEmpty(planNid)) {
                        return hjhResultMess("计划项目不存在");
                    } else {
                        // 判断借款信息是否存在
                        if (plan == null || plan.getId() == null) {
                            return hjhResultMess("计划项目不存在");
                        } else {
                            // 判断用户出借金额是否为空
                            if (!(StringUtils.isNotEmpty(account)
                                    || (StringUtils.isEmpty(account) && cuc != null && cuc.getCouponType() == 3)
                                    || (StringUtils.isEmpty(account) && cuc != null && cuc.getCouponType() == 1
                                            && cuc.getAddFlg() == 1))) {
                                return hjhResultMess("请输入加入金额");
                            } else {
                                // 还款金额是否数值
                                try {
                                    // 出借金额必须是整数
                                    if (StringUtils.isEmpty(account)) {
                                        account = "0";
                                    }
                                    Long accountInt = Long.parseLong(account);
                                    if (accountInt < 0) {
                                        return hjhResultMess("加入金额不能为负数");
                                    } else {
                                        if ((accountInt == 0 && cuc == null)
                                                || (accountInt == 0 && cuc != null && cuc.getCouponType() == 2)) {
                                            return hjhResultMess("出借金额不能为0元");
                                        }
                                        if (accountInt != 0 && cuc != null && cuc.getCouponType() == 1
                                                && cuc.getAddFlg() == 1) {
                                            return hjhResultMess("该优惠券只能单独使用");
                                        }
                                        // 用户出借额
                                        BigDecimal accountBigDecimal = new BigDecimal(account);

                                        if (plan.getLockPeriod() == null) {
                                            return hjhResultMess("计划锁定期不存在");
                                        }
                                        String lockPeriod = plan.getLockPeriod().toString();
                                        // 从redis取该计划的开放额度
                                        String balance = RedisUtils.get("HJHBAL_" + planNid);
                                        if (StringUtils.isEmpty(balance)) {
                                            return hjhResultMess("您来晚了，下次再来抢吧");
                                        } else {
                                            // DB 该计划可投金额
                                            BigDecimal available = plan.getAvailableInvestAccount();// 该计划的可投金额
                                            BigDecimal minInvest = plan.getMinInvestment();// 该计划的最小出借金额
                                            // 当剩余可投金额小于最低起投金额，不做最低起投金额的限制
                                            if (minInvest != null && minInvest.compareTo(BigDecimal.ZERO) != 0
                                                    && new BigDecimal(balance).compareTo(minInvest) == -1) {
                                                if (accountBigDecimal.compareTo(new BigDecimal(balance)) == 1) {
                                                    return hjhResultMess("剩余可加入金额为" + balance + "元");
                                                }
                                                if (accountBigDecimal.compareTo(new BigDecimal(balance)) != 0) {
                                                    return hjhResultMess("剩余可加入只剩" + balance + "元，须全部购买");
                                                }
                                            } else {
                                                // 项目的剩余金额大于最低起投金额
                                                if (accountBigDecimal.compareTo(plan.getMinInvestment()) == -1) {
                                                    if (accountBigDecimal.compareTo(BigDecimal.ZERO) == 0) {
                                                        if (cuc != null && cuc.getCouponType() != 3
                                                                && cuc.getCouponType() != 1) {
                                                            return hjhResultMess(plan.getMinInvestment() + "元起投");
                                                        }
                                                    } else {
                                                        return hjhResultMess(plan.getMinInvestment() + "元起投");
                                                    }
                                                } else {
                                                    BigDecimal max = plan.getMaxInvestment();
                                                    if (max != null && max.compareTo(BigDecimal.ZERO) != 0
                                                            && accountBigDecimal.compareTo(max) == 1) {
                                                        return hjhResultMess("项目最大加入额为" + max + "元");
                                                    }
                                                }
                                            }
                                            if (accountBigDecimal.compareTo(plan.getAvailableInvestAccount()) > 0) {
                                                return hjhResultMess("加入金额不能大于项目可投金额");
                                            } else {
                                                // 查询用户账户表-出借账户
                                                Account tenderAccount = this.getAccount(Integer.parseInt(userId));
                                                // 获取汇盈平台DB---用户银行账户余额
                                                if (tenderAccount.getBankBalance().compareTo(accountBigDecimal) < 0) {
                                                    return hjhResultMess("余额不足，请充值！");
                                                }
                                                // 获取江西银行---用户银行账户余额
                                                BankOpenAccount accountChinapnr =
                                                        getBankOpenAccount(Integer.parseInt(userId));
                                                BigDecimal userBankBalance = getBankBalance(Integer.parseInt(userId),
                                                        accountChinapnr.getAccount());
                                                if (userBankBalance.compareTo(accountBigDecimal) < 0) {
                                                    return hjhResultMess("余额不足，请充值！");
                                                }
                                                if (StringUtils.isEmpty(balance)) {
                                                    return hjhResultMess("您来晚了，下次再来抢吧");
                                                } else {
                                                    // balance 是 redis 中的该计划可投金额
                                                    if (StringUtils.isNotEmpty(balance)) {
                                                        // redis剩余金额不足
                                                        if (accountBigDecimal.compareTo(new BigDecimal(balance)) == 1) {
                                                            return hjhResultMess("项目太抢手了！剩余可加入金额只有" + balance + "元");
                                                        } else {
                                                            // 应该是： (用户出借额度 -
                                                            // 起投额度)%增量 = 0
                                                            if (plan.getInvestmentIncrement() != null
                                                                    && (accountInt - minInvest.longValue()) % plan
                                                                            .getInvestmentIncrement().longValue() != 0
                                                                    && accountBigDecimal
                                                                            .compareTo(new BigDecimal(balance)) == -1) {
                                                                return hjhResultMess("加入递增金额须为" + plan.getInvestmentIncrement()
                                                                                + " 元的整数倍");
                                                            }
                                                            // 如果验证没问题，则返回出借人借款人的银行账号
                                                            // String accountId
                                                            // =
                                                            // bankOpenAccount.getAccount();
                                                            return successMess("");
                                                        }
                                                    } else {
                                                        return hjhResultMess("您来晚了，下次再来抢吧");
                                                    }
                                                }
                                            }
                                        }
                                    }

                                } catch (Exception e) {
                                    return hjhResultMess("加入金额必须为整数");
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    // 进行出借
    @Override
    public JSONObject investInfo(ModelAndView modelAndView,String planNid, String account, String userId, String platform,
        CouponConfigCustomizeV2 cuc , String ip,String couponGrantId) {
        JSONObject result = new JSONObject();
        modelAndView.addObject("plan", "1");
        modelAndView.addObject("planNid", planNid);
        String accountStr = account;
        if (accountStr == null || "".equals(accountStr)) {
            accountStr = "0";
        }
        
        HjhPlan plan = getPlanByNid(planNid);
        String lockPeriod = plan.getLockPeriod().toString();
        
        System.out.println("joinPlan ShiroUtil.getLoginUserId--------------------" + userId);// 如果没有本金出借且有优惠券出借
        BigDecimal decimalAccount = StringUtils.isNotEmpty(accountStr) ? new BigDecimal(accountStr) : BigDecimal.ZERO;

        // 汇添金无本金出借是使用优惠券出借 start
        modelAndView.addObject("couponInterest", "0");
        // 排他校验时间
        int couponOldTime = Integer.MIN_VALUE;
        // -1:有可用的优惠券，但是出借时不选择优惠券 空或null：用户没有可用优惠券
        if (cuc!=null&&StringUtils.isNotEmpty(couponGrantId) && !StringUtils.equals("-1", couponGrantId)) {
            // 排他check用
            couponOldTime = cuc.getUserUpdateTime();
        }

        // 体验金出借
        if (decimalAccount.compareTo(BigDecimal.ZERO) != 1 && cuc != null && (cuc.getCouponType() == 3 || cuc.getCouponType() == 1)) {
            System.out.println("cuc.getCouponType():" + cuc.getCouponType());
            result = this.couponTender(modelAndView,result, planNid, account,ip,cuc, userId, couponOldTime,platform,couponGrantId);
            return result;
        }
        
        // 汇添金无本金出借是使用优惠券出借 end
        result = checkHJHParam(planNid, accountStr, userId, platform, cuc);
        
        /** redis 锁 */
        boolean reslut = RedisUtils.tranactionSet("HjhInvestUser" + userId, 10);
        if(!reslut){
            return hjhResultMess("您正在出借，请稍后再试...");
        }
        
        if (result == null) {
            LogUtil.infoLog(PlanServiceImpl.class.toString(), "校验异常");
            return hjhResultMess("抱歉，出借失败，请重试！");
        } else if (result.get("error") != null && result.get("error").equals("1")) {
            LogUtil.infoLog(PlanServiceImpl.class.toString(), result.get("data") + "");
            return hjhResultMess("抱歉，出借失败，请重试！");
        }
        String tenderUsrcustid = result.getString("tenderUsrcustid");
        // 生成冻结订单
        String frzzeOrderId = GetOrderIdUtils.getOrderId0(Integer.valueOf(userId));
        String frzzeOrderDate = GetOrderIdUtils.getOrderDate();
        // TODO 冻结 加入 相应金额 明细
        Jedis jedis = pool.getResource();

        // 操作redis
        while ("OK".equals(jedis.watch("HJHBAL_" + planNid))) {
            String balance = RedisUtils.get("HJHBAL_" + planNid);
            if (StringUtils.isNotBlank(balance)) {
                LogUtil.infoLog(PlanServiceImpl.class.toString(), "移动端用户:" + userId + "***********************************加入计划冻结前可投金额：" + balance);
                if (new BigDecimal(balance).compareTo(BigDecimal.ZERO) == 0) {
                    LogUtil.infoLog(PlanServiceImpl.class.toString(), "您来晚了，下次再来抢吧");
                    return hjhResultMess("抱歉，出借失败，请重试！");
                } else {
                    if (new BigDecimal(balance).compareTo(decimalAccount) < 0) {
                        LogUtil.infoLog(PlanServiceImpl.class.toString(), "可加入剩余金额为" + balance + "元");
                        return hjhResultMess("抱歉，出借失败，请重试！");
                    } else {
                        Transaction tx = jedis.multi();
                        // 事务：计划当前可用额度 = 计划未投前可用余额 - 用户出借额度
                        BigDecimal lastAccount = new BigDecimal(balance).subtract(decimalAccount);
                        tx.set("HJHBAL_" + planNid, lastAccount + "");
                        List<Object> result1 = tx.exec();
                        if (result1 == null || result1.isEmpty()) {
                            jedis.unwatch();
                            LogUtil.infoLog(PlanServiceImpl.class.toString(), "redis可加入剩余金额为" + balance + "元");
                            //return hjhResultMess("抱歉，出借失败，请重试！");
                        } else {
                            System.out.println("移动端用户:" + userId + "***********************************计划未投前可用余额redis：" + decimalAccount);
                            // 写队列
                            break;
                        }
                    }
                }
            } else {
                LogUtil.infoLog(PlanServiceImpl.class.toString(), "您来晚了，下次再来抢吧");
                return hjhResultMess("抱歉，出借失败，请重试！");
            }
        }

        boolean afterDealFlag = false;
        String couponInterest="0";
        // 写入加入计划表
        try {
            // 生成加入订单
            result = successMess("恭喜您出借成功!");
            String planOrderId = GetOrderIdUtils.getOrderId0(Integer.valueOf(userId));
            // update by pcc 汇添金有本金出借是使用优惠券出借修改updateAfterPlanRedis接口
        
            // (原汇添金)添加couponGrantId、modelAndView参数 start
            afterDealFlag = updateAfterPlanRedis(modelAndView,planNid, frzzeOrderId, Integer.parseInt(userId), accountStr, tenderUsrcustid, ip, "", frzzeOrderDate, planOrderId,
                    couponGrantId, couponInterest,platform);
            // update by pcc 汇添金有本金出借是使用优惠券出借修改updateAfterPlanRedis接口
            // 添加couponGrantId、modelAndView参数 end
            if (afterDealFlag) {
                
                LogUtil.endLog(PlanServiceImpl.class.toString(), "[交易完成后,回调结束]");
                DecimalFormat df = CustomConstants.DF_FOR_VIEW;
                String interest = null;
                if (StringUtils.isBlank(interest)) {
                    // 根据项目编号获取相应的项目
                    //DebtPlan debtPlan = planService.getPlanByNid(planNid);
                    HjhPlan debtPlan = getPlanByNid(planNid);
                    BigDecimal planApr = debtPlan.getExpectApr();
                    // 周期
                    Integer planPeriod = debtPlan.getLockPeriod();
                    BigDecimal earnings = new BigDecimal("0");
                    df.setRoundingMode(RoundingMode.FLOOR);
                    // 计算历史回报
                    earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(accountStr), planApr.divide(new BigDecimal("100")), planPeriod);
                    // 历史回报
                    result.put("earnings", earnings);
                    modelAndView.addObject("earnings", earnings);
                    interest = df.format(earnings.add(new BigDecimal(couponInterest)));
                }
                if (StringUtils.isNotBlank(interest)) {
                    result.put("interest", interest);
                    modelAndView.addObject("interest", interest);
                }
                result.put("account",  df.format(new BigDecimal(accountStr)));
                modelAndView.addObject("account", df.format(new BigDecimal(accountStr)));
                result.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
                System.out.println("移动端用户:" + userId + "***********************************加入计划成功：" + accountStr);
                LogUtil.endLog(PlanServiceImpl.class.toString(), "investInfo");
                logger.info("移动端加入计划用户:" + userId +",planOrderId:"+ planOrderId+"发送mq");
                //MQ  加入汇计划，出借触发奖励
                sendMQActivity(Integer.valueOf(userId),planOrderId,new BigDecimal(accountStr),3);
                sendRrturnCashActivity(Integer.valueOf(userId),planOrderId,new BigDecimal(accountStr),3);
                return result;
            } else {
                System.out.println("移动端用户:" + userId + "***********************************预约成功后处理失败：" + accountStr);
                // 恢复redis
                recoverRedis(planNid, Integer.parseInt(userId), accountStr, lockPeriod);
                /*planService.recoverRedis(planNid, userId, accountStr, planPeriod);*/
                LogUtil.infoLog(PlanServiceImpl.class.toString(), "系统异常");
                return hjhResultMess("抱歉，出借失败，请重试！");
            }
        } catch (Exception e) {
            // 恢复redis
            recoverRedis(planNid, Integer.parseInt(userId), accountStr, lockPeriod);
            LogUtil.infoLog(PlanServiceImpl.class.toString(), "系统异常");
            return hjhResultMess("抱歉，出借失败，请重试！");

        }
    }

    /**
     * 发放活动奖励
     * @param userId
     * @param order
     * @param investMoney
     * @param projectType 项目类型
     */
    private void sendMQActivity(Integer userId,String order,BigDecimal investMoney,int projectType){
        // 加入到消息队列
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("orderId", order);
        params.put("investMoney", investMoney.toString());
        //来源,1=新手标，2=散标，3=汇计划
        params.put("productType", projectType);
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.MDIAU_ACTIVITY, JSONObject.toJSONString(params));
    }
    /**
     * 纳觅返现活动
     * @param userId
     * @param order
     */
    private void sendRrturnCashActivity(Integer userId,String order,BigDecimal investMoney,int projectType){
        // 加入到消息队列
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", userId);
        params.put("orderId", order);
        params.put("investMoney", investMoney.toString());
        //来源,1=新手标，2=散标，3=汇计划
        params.put("productType", projectType);
        rabbitTemplate.convertAndSend(RabbitMQConstants.EXCHANGES_NAME, RabbitMQConstants.RETURN_CASH_ACTIVITY, JSONObject.toJSONString(params));
    }
    // 体验金出借
    private JSONObject couponTender(ModelAndView modelAndView,JSONObject result, String planNid, String account, String ip, CouponConfigCustomizeV2 cuc, String userId, int couponOldTime, String platform, String couponGrantId) {
        DecimalFormat df = CustomConstants.DF_FOR_VIEW;

        // 优惠券出借校验
        Map<String, String> validateMap = validateCoupon(Integer.parseInt(userId), account, couponGrantId, planNid, platform);
        if (validateMap.containsKey(CustomConstants.APP_STATUS_DESC)) {
            // 出借失败
            return hjhResultMess(validateMap.get(CustomConstants.APP_STATUS_DESC));
        }
        JSONObject jsonObject = CommonSoaUtils.planCouponInvest(userId, planNid, account, platform, couponGrantId, "", ip, couponOldTime + "");

        if (jsonObject.getIntValue("status") == 0) {
            // 出借成功
            result = successMess("出借成功！");
            result.put("account", "0");
            // 优惠券收益
            result.put("couponInterest", df.format(new BigDecimal(jsonObject.getString("couponInterest"))));
            // 历史回报
            result.put("interest", df.format(new BigDecimal(jsonObject.getString("couponInterest"))));
            // 优惠券ID
            result.put("couponGrantId", couponGrantId);
            // 出借类型
            result.put("investType", jsonObject.getString("couponTypeInt"));
            // 优惠券类别
            result.put("couponType", jsonObject.getString("couponType"));
            // 优惠券额度
            result.put("couponQuota", jsonObject.getString("couponQuota"));
            // 出借额度
            result.put("accountDecimal", jsonObject.getString("accountDecimal"));
            /** 修改出借成功页面显示修改开始 */
            modelAndView.addObject("plan", 1);
            modelAndView.addObject("planNid", planNid);

            // 优惠券收益
            modelAndView.addObject("couponInterest", df.format(new BigDecimal(jsonObject.getString("couponInterest"))));
            // 优惠券收益
            modelAndView.addObject("interest", df.format(new BigDecimal(jsonObject.getString("couponInterest"))));
            // 优惠券收益
            modelAndView.addObject("account", "0");
            // 优惠券类别
            modelAndView.addObject("couponType", jsonObject.getString("couponTypeInt"));
            // 优惠券额度
            modelAndView.addObject("couponQuota", jsonObject.getString("couponQuota"));
            return result;
        } else {
            LogUtil.infoLog(this.toString(), "couponTender", "优惠券出借结束。。。。。。");
            result = hjhResultMess(jsonObject.getString("statusDesc"));
            modelAndView.addObject("investDesc", jsonObject.getString("statusDesc"));
            modelAndView.addObject("plan", 1);
            return result;
        }
    }
    
    // 优惠券出借校验
    private Map<String, String> validateCoupon(Integer userId, String account, String couponGrantId, String planNid, String platform) {

        JSONObject jsonObject = CommonSoaUtils.planCheckCoupon(userId + "", planNid, account, platform, couponGrantId);
        int status = jsonObject.getIntValue("status");
        String statusDesc = jsonObject.getString("statusDesc");
        Map<String, String> paramMap = new HashMap<String, String>();
        if (status == 1) {
            paramMap.put(CustomConstants.APP_STATUS_DESC, statusDesc);
        }

        return paramMap;
    }

    @Override
    public boolean updateAfterPlanRedis(ModelAndView modelAndView,String planNid, String frzzeOrderId, Integer userId, String accountStr,
        String tenderUsrcustid, String ipAddr, String freezeTrxId, String frzzeOrderDate,
        String planOrderId, String couponGrantId,String couponInterest,String platform) throws Exception {
        int nowTime = GetDate.getNowTime10();
        Users user = getUsers(userId);
        UsersInfo userInfo = getUsersInfoByUserId(userId);
        HjhPlan debtPlan = getPlanByNid(planNid);
        // 预期年利率
        BigDecimal planApr = debtPlan.getExpectApr();
        // 周期
        Integer planPeriod = debtPlan.getLockPeriod();
        // 计算历史回报(已经换成 天 的计算公式)
        BigDecimal earnings = DuePrincipalAndInterestUtils.getDayInterest(new BigDecimal(accountStr),
                planApr.divide(new BigDecimal("100")), planPeriod);
            // 当大于等于100时 取百位 小于100 时 取十位
            BigDecimal accountDecimal = new BigDecimal(accountStr);//用户出借金额
            BigDecimal maxInvestNumber = debtPlan.getMaxInvestment();//最高加入金额
            BigDecimal minInvestNumber = debtPlan.getMinInvestment();//最低加入金额
            BigDecimal investMaxTmp = accountDecimal.divide(maxInvestNumber, 2, BigDecimal.ROUND_DOWN);
            BigDecimal investMinTmp = accountDecimal.divide(minInvestNumber, 2, BigDecimal.ROUND_UP);
            BigDecimal investMax = BigDecimal.ZERO;
            if (investMaxTmp.compareTo(new BigDecimal("100")) >= 0) {
                investMax = ((investMaxTmp).divide(new BigDecimal("100"), 0, BigDecimal.ROUND_DOWN))
                        .multiply(new BigDecimal("100"));
            } else {
                investMax = ((investMaxTmp).divide(new BigDecimal("10"), 0, BigDecimal.ROUND_DOWN))
                        .multiply(new BigDecimal("10"));
            }
            BigDecimal investMin = ((investMinTmp).divide(new BigDecimal("100"), 0, BigDecimal.ROUND_UP))
                    .multiply(new BigDecimal("100"));
            
                                        /*(1)汇计划加入明细表插表开始*/
            
            //处理汇计划加入明细表(以下涵盖所有字段)
            HjhAccede planAccede = new HjhAccede();
            //DebtPlanAccede debtPlanAccede = new DebtPlanAccede();//   原 计划加入表
            planAccede.setAccedeOrderId(planOrderId);
            planAccede.setPlanNid(planNid);
            planAccede.setUserId(userId);
            planAccede.setUserName(user.getUsername());
            planAccede.setUserAttribute(userInfo.getAttribute());//用户属性 0=>无主单 1=>有主单 2=>线下员工 3=>线上员工
            planAccede.setAccedeAccount(accountDecimal);// 加入金额
            planAccede.setAlreadyInvest(BigDecimal.ZERO);//已出借金额(出借时维护)
            planAccede.setClient(Integer.parseInt(platform));
            planAccede.setOrderStatus(0);//0自动投标中 2自动投标成功 3锁定中 5退出中 7已退出 99 自动出借异常(出借时维护)
            planAccede.setAddTime(nowTime);
            planAccede.setCountInterestTime(0);//计息时间(最后放款时维护)
            planAccede.setSendStatus(0);//协议发送状态0未发送 1已发送
            planAccede.setLockPeriod(debtPlan.getLockPeriod());
            planAccede.setCommissionStatus(0);//提成计算状态:0:未计算,1:已计算
            planAccede.setAvailableInvestAccount(accountDecimal);//可投金额初始与加入金额一致
            planAccede.setWaitTotal(BigDecimal.ZERO);//(出借时维护)
            planAccede.setWaitCaptical(BigDecimal.ZERO);//(出借时维护)
            planAccede.setWaitInterest(BigDecimal.ZERO);//(出借时维护)
            planAccede.setReceivedTotal(BigDecimal.ZERO);//(退出时维护)
            planAccede.setReceivedInterest(BigDecimal.ZERO);//(退出时维护)
            planAccede.setReceivedCapital(BigDecimal.ZERO);//(退出时维护)
            planAccede.setQuitTime(0);//(退出时维护)
            planAccede.setLastPaymentTime(0);//最后回款时间(复审时维护)
            planAccede.setAcctualPaymentTime(0);//实际回款时间(退出时维护)
            planAccede.setShouldPayTotal(accountDecimal.add(earnings));//应还总额 = 应还本金 +应还利息
            planAccede.setShouldPayCapital(accountDecimal);
            planAccede.setShouldPayInterest(earnings);
            planAccede.setCreateUser(userId);
            planAccede.setCreateTime(nowTime);
            //planAccede.setUpdateUser();(更表时维护)
            //planAccede.setUpdateTime();(更表时维护)
            planAccede.setDelFlg(0);//初始未未删除
            
            Integer refferId = null;
            String refferName = null;
            System.out.println("username:"+user.getUsername() + " userid:" + userId);
            
            if (Validator.isNotNull(userInfo)) {
                SpreadsUsers spreadsUsers = this.getRecommendUser(userId);
                System.out.println("推荐人信息：" + spreadsUsers);
                if (spreadsUsers != null) {
                    int refUserId = spreadsUsers.getSpreadsUserid();
                    System.out.println("推荐人用户id：" + spreadsUsers.getSpreadsUserid());
                    // 查找用户推荐人详情信息
                    UserInfoCustomize userInfoCustomize = userInfoCustomizeMapper.queryUserInfoByUserId(refUserId);
                    if (Validator.isNotNull(userInfoCustomize)) {
                        refferId = userInfoCustomize.getUserId();
                        refferName = userInfoCustomize.getUserName();
                        planAccede.setInviteUserId(userInfoCustomize.getUserId());
                        planAccede.setInviteUserName(userInfoCustomize.getUserName());
                        planAccede.setInviteUserAttribute(userInfoCustomize.getAttribute());
                        planAccede.setInviteUserRegionname(userInfoCustomize.getRegionName());
                        planAccede.setInviteUserBranchname(userInfoCustomize.getBranchName());
                        planAccede.setInviteUserDepartmentname(userInfoCustomize.getDepartmentName());
                        System.out.println("InviteUserName: " + userInfoCustomize.getUserName());
                        System.out.println("InviteUserRegionname: " + userInfoCustomize.getRegionName());
                        System.out.println("InviteUserBranchname: " + userInfoCustomize.getBranchName());
                        System.out.println("InviteUserDepartmentname: " + userInfoCustomize.getDepartmentName());
                    }
                    
                    
                }
            }
            System.out.println("计划加入用户:" + userId + "***********************************插入planAccede："
                    + JSON.toJSONString(planAccede));
            //boolean trenderFlag = debtPlanAccedeMapper.insertSelective(planAccede) > 0 ? true : false;// 原 汇添金插表
            boolean trenderFlag =  hjhAccedeMapper.insertSelective(planAccede) > 0 ? true : false;
            
           /*汇计划加入明细表插表结束*/
           /*add by pcc 汇添金使用优惠券出借 start*/
            
            if (StringUtils.isNotEmpty(couponGrantId)) {
                // 优惠券出借校验
                try {
                    Map<String, String> validateMap = this.validateCoupon(userId, accountStr, couponGrantId, planNid,
                            CustomConstants.CLIENT_PC);
                    LogUtil.infoLog(InvestServiceImpl.class.toString(), "updateCouponTender", "优惠券出借校验结果："
                            + validateMap.get("statusDesc"));
                    System.out.println("updateCouponTender" + "优惠券出借校验结果：" + validateMap.get("statusDesc"));
                    if (MapUtils.isEmpty(validateMap)) {
                        // 校验通过 进行出借
                        this.updateCouponTender(modelAndView,couponGrantId, planNid, userId, accountStr, ipAddr, new Integer(
                                frzzeOrderDate), planOrderId, couponInterest,platform);
                    }
                } catch (Exception e) {
                    LogUtil.infoLog(InvestController.class.getName(), "tenderRetUrl", "优惠券出借失败");
                }
            }
            /*add by 汇添金使用优惠券出借 end*/
            
            if (trenderFlag) {//加入明细表插表成功的前提下，继续    
                // 查询开户表获取电子账号
                BankOpenAccount accountChinapnrTender = getBankOpenAccount(userId);
                
                AccountExample example = new AccountExample();
                AccountExample.Criteria criteria = example.createCriteria();
                criteria.andUserIdEqualTo(userId);
                List<Account> list = accountMapper.selectByExample(example);
                if (list != null && list.size() == 1) {
                    // 该用户的江西银行可用余额(查询当前用户)
                    BigDecimal bankBalance = list.get(0).getBankBalance();
                    // 该用户的汇计划账户可用余额(查询当前用户)
                    BigDecimal planBalance = list.get(0).getBankBalance();
                    // 冻结金额(查询当前用户)
                    BigDecimal frost = list.get(0).getBankFrost();
                    // 总金额(查询当前用户)
                    BigDecimal total = list.get(0).getBankTotal();
                    /*该用户的计划可用余额 = 原该用户的计划可用余额 + 该用户加入金额*/
                    /*该用户的江西银行可用余额 = 原该用户的江西银行可用余额 - 该用户加入金额*/
                    Account account = new Account();
                    // 用户id
                    account.setUserId(userId);
                    account.setPlanBalance(accountDecimal);//注意：先set值，加法运算放在SQL中防并发
                    account.setBankBalance(accountDecimal);//注意：先set值，减法运算放在SQL中防并发
                    // 更新用户计划账户
                    boolean accountFlag = this.hjhPlanCustomizeMapper.updateOfPlanJoin(account) > 0 ? true : false;
                    
                    //boolean accountFlag = this.adminAccountCustomizeMapper.updateOfPlanJoin(account) > 0 ? true : false;//原 汇添金更表
                                                  /*更新huiyingdai_account用户账户余额表结束*/
                                                  /*(3)插入account_list表 开始*/
                    if (accountFlag) {//huiyingdai_account更表成功的前提下，继续
                        //先查询一遍Account表
                        Account accedeAccount = this.selectUserAccount(userId);
                        AccountList accountList = new AccountList();
                        accountList.setIsBank(1);
                        accountList.setIsShow(0);
                        accountList.setNid(planOrderId);//插入生成的计划订单号
                        accountList.setAccedeOrderId(planOrderId);//也插入生成的计划订单号
                        accountList.setUserId(userId);
                        accountList.setAmount(accountDecimal);//插入用户加入金额
                        accountList.setType(2);// 收支类型1收入2支出3冻结
                        accountList.setTrade("hjh_invest");// 汇计划出借
                        accountList.setTradeCode("balance");
                        accountList.setIp(ipAddr);
                        accountList.setOperator(userId + "");
                        accountList.setRemark(planNid);
                        accountList.setBankBalance(accedeAccount.getBankBalance());//江西银行账户余额
                        //accountList.setBalance(accedeAccount.getBalance());//原 汇付账户
                        accountList.setPlanBalance(accedeAccount.getPlanBalance());//汇计划账户可用余额
                        accountList.setInterest(new BigDecimal(0));
                        accountList.setAwait(accedeAccount.getAwait());
                        accountList.setPlanFrost(accedeAccount.getPlanFrost());
                        accountList.setBankFrost(frost);
                        accountList.setIsUpdate(0);
                        accountList.setRepay(new BigDecimal(0));
                        accountList.setBankTotal(total);
                        accountList.setWeb(0);
                        accountList.setBaseUpdate(0);
                        accountList.setCreateTime(nowTime);
                        accountList.setAccountId(accountChinapnrTender.getAccount());
                        System.out.println("加入计划用户:" + userId + "***********************************预插入accountList："
                                + JSON.toJSONString(accountList));
                        boolean accountListFlag = accountListMapper.insertSelective(accountList) > 0 ? true : false;
                        /*(3)插入account_list表 结束*/
                        
                        /*(4)更新汇计划列表 开始*/
                        if (accountListFlag) {//account_list表 插表成功的前提下
                            // 更新plan表
                            Map<String, Object> plan = new HashMap<String, Object>();
                            plan.put("planId", debtPlan.getPlanNid());
                            plan.put("accountDecimal", accountDecimal);//用户加入金额
                            plan.put("earnings", earnings);
                            boolean updateBorrowAccountFlag = hjhPlanCustomizeMapper.updateByDebtPlanId(plan) > 0 ? true : false;
                            /*(4)更新汇计划列表 结束*/
                                                    
                            /*(5)更新  渠道统计用户累计出借  和  huiyingdai_utm_reg的首投信息 开始*/
                            if (updateBorrowAccountFlag) {//更新汇计划列表成功的前提下
                                // 更新渠道统计用户累计出借
                                // 出借人信息
                                Users users = getUsers(userId);
                                if (users != null) {
                                    // 更新渠道统计用户累计出借
                                    AppChannelStatisticsDetailExample channelExample = new AppChannelStatisticsDetailExample();
                                    AppChannelStatisticsDetailExample.Criteria crt = channelExample.createCriteria();
                                    crt.andUserIdEqualTo(userId);
                                    List<AppChannelStatisticsDetail> appChannelStatisticsDetails = this.appChannelStatisticsDetailMapper.selectByExample(channelExample);
                                    if (appChannelStatisticsDetails != null && appChannelStatisticsDetails.size() == 1) {
                                        AppChannelStatisticsDetail channelDetail = appChannelStatisticsDetails.get(0);
                                        Map<String, Object> params = new HashMap<String, Object>();
                                        params.put("id", channelDetail.getId());
                                        // 认购本金
                                        params.put("accountDecimal", accountDecimal);
                                        // 出借时间
                                        params.put("investTime", nowTime);
                                        // 项目类型
                                        params.put("projectType", "汇计划");
                                        // 首次投标项目期限
                                        String investProjectPeriod = debtPlan.getLockPeriod() + "天";
                                        params.put("investProjectPeriod", investProjectPeriod);
                                        // 更新渠道统计用户累计出借
                                        if (users.getInvestflag() == 1) {
                                            this.appChannelStatisticsDetailCustomizeMapper.updateAppChannelStatisticsDetail(params);
                                        } else if (users.getInvestflag() == 0) {
                                            // 更新首投出借
                                            this.appChannelStatisticsDetailCustomizeMapper.updateFirstAppChannelStatisticsDetail(params);
                                        }
                                        System.out.println("用户:"+ userId+ "***********************************预更新渠道统计表AppChannelStatisticsDetail，订单号："+ planAccede.getAccedeOrderId());
                                    }else{
                                        // 更新huiyingdai_utm_reg的首投信息
                                        UtmRegExample utmRegExample = new UtmRegExample();
                                        UtmRegExample.Criteria utmRegCra = utmRegExample.createCriteria();
                                        utmRegCra.andUserIdEqualTo(userId);
                                        List<UtmReg> utmRegList = this.utmRegMapper.selectByExample(utmRegExample);
                                        if (utmRegList != null && utmRegList.size() > 0) {
                                            UtmReg utmReg = utmRegList.get(0);
                                            Map<String, Object> params = new HashMap<String, Object>();
                                            params.put("id", utmReg.getId());
                                            params.put("accountDecimal", accountDecimal);
                                            // 出借时间
                                            params.put("investTime", nowTime);
                                            // 项目类型
                                            params.put("projectType", "汇计划");
                                            // 首次投标项目期限
                                            String investProjectPeriod = debtPlan.getLockPeriod() + "天";
                                            // 首次投标项目期限
                                            params.put("investProjectPeriod", investProjectPeriod);
                                            // 更新渠道统计用户累计出借
                                            if (users.getInvestflag() == 0) {
                                                // 更新huiyingdai_utm_reg的首投信息
                                                this.appChannelStatisticsDetailCustomizeMapper.updateFirstUtmReg(params);
                                                System.out.println("用户:"+ userId + "***********************************预更新渠道统计表huiyingdai_utm_reg的首投信息，订单号："+ planAccede.getAccedeOrderId());
                                            }
                                        }
                                    }
                                }
                                /*(5)更新  渠道统计用户累计出借  和  huiyingdai_utm_reg的首投信息 结束*/
                                /*(6)更新  users新手标志位 开始 */
                                // 出借人信息:更新新手标志位
                                users = getUsers(userId);
                                if (users != null) {
                                    if (users.getInvestflag() == 0) {
                                        users.setInvestflag(1);
                                        this.usersMapper.updateByPrimaryKeySelective(users);
                                    }
                                }
                                /*(6)更新  users新手标志位 结束 */
                                return true;
                                
                            } else {
                                throw new RuntimeException("huiyingdai_account表，hyjf_hjh_plan表，account_list表，hyjf_hjh_accede表 操作失败");
                            }
                        } else {
                            throw new RuntimeException("account_list表操作失败");
                        }       
                    } else {
                        throw new RuntimeException("huiyingdai_account表操作失败");
                    }
                } else {
                    throw new RuntimeException("用户账户信息(huiyingdai_account)操作失败！");
                }
            } else {
                throw new RuntimeException("加入明细表操作失败！");
            }
    }
    
    private Account selectUserAccount(Integer userId) {
        AccountExample example = new AccountExample();
        AccountExample.Criteria crt = example.createCriteria();
        crt.andUserIdEqualTo(userId);
        List<Account> accountList = this.accountMapper.selectByExample(example);
        if (accountList != null && accountList.size() == 1) {
            return accountList.get(0);
        }
        return null;
    }
    
    public JSONObject updateCouponTender(ModelAndView modelAndView,String couponGrantId, String planNid, Integer userId, String accountStr,
        String ipAddr, int couponOldTime, String planOrderId, String couponInterest, String plan) {

        LogUtil.infoLog(PlanServiceImpl.class.toString(), "updateCouponTender", "汇添金优惠券出借开始。。。。。。");
        JSONObject jsonObject = CommonSoaUtils.planCouponInvest(userId + "", planNid, accountStr,
                CustomConstants.CLIENT_PC, couponGrantId, planOrderId, ipAddr, couponOldTime + "");

        JSONObject result = successMess("出借成功！");
        if (jsonObject.getIntValue("status") == 0) {
         // 优惠券收益
            result.put("couponInterest", jsonObject.getString("couponInterest"));
            result.put("couponType", jsonObject.getString("couponType"));
            result.put("couponTypeInt", jsonObject.getString("couponTypeInt"));
            result.put("couponQuota", jsonObject.getString("couponQuota"));
            // 优惠券ID
            result.put("couponGrantId", couponGrantId);
            // 优惠券额度
            result.put("couponQuota", jsonObject.getString("couponQuota"));
            // 出借额度
            result.put("accountDecimal", jsonObject.getString("accountDecimal"));
            result.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
            //----------old
            modelAndView.addObject("couponInterest", jsonObject.getString("couponInterest"));
            couponInterest=jsonObject.getString("couponInterest");
            // 优惠券类别
            modelAndView.addObject("couponType", jsonObject.getString("couponTypeInt"));
            // 优惠券额度
            modelAndView.addObject("couponQuota", jsonObject.getString("couponQuota"));
            modelAndView.addObject("investDesc", "出借成功！");
        } else {
            result = hjhResultMess("出借失败！");
        }

        return result;
    }

    public void recoverRedis(String planNid, Integer userId, String account, String lockPeriod) {
        JedisPool pool = RedisUtils.getPool();
        Jedis jedis = pool.getResource();
        BigDecimal accountBigDecimal = new BigDecimal(account);

        while ("OK".equals(jedis.watch("HJHBAL_" + planNid))) {
            String balanceLast = RedisUtils.get("HJHBAL_" + planNid);
            if (StringUtils.isBlank(balanceLast)) {
                return;
            }
            BigDecimal recoverAccount = accountBigDecimal.add(new BigDecimal(balanceLast));
            Transaction tx = jedis.multi();
            tx.set("HJHBAL_" + planNid, recoverAccount + "");
            List<Object> result = tx.exec();
            if (result == null || result.isEmpty()) {
                jedis.unwatch();
            } else {
                System.out.println("加入计划用户:" + userId + "***********************************from redis恢复redis："
                        + account);
                break;
            }
        }
    }
    
    private JSONObject successMess(String resultStr) {
        JSONObject result = new JSONObject();
        result.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_SUCCESS);
        result.put(CustomConstants.APP_STATUS_DESC, resultStr);
        return result;
    }
    
    private JSONObject hjhResultMess(String resultStr) {
        JSONObject result = new JSONObject();
        result.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
        result.put(CustomConstants.APP_STATUS_DESC, resultStr);
        return result;
    }
    
    public BigDecimal getBankBalance(Integer userId, String accountId) {
        // 账户可用余额
        BigDecimal balance = BigDecimal.ZERO;
        BankCallBean bean = new BankCallBean();
        bean.setVersion(BankCallConstant.VERSION_10);// 版本号
        bean.setTxCode(BankCallMethodConstant.TXCODE_BALANCE_QUERY);// 交易代码
        bean.setTxDate(GetOrderIdUtils.getTxDate()); // 交易日期
        bean.setTxTime(GetOrderIdUtils.getTxTime()); // 交易时间
        bean.setSeqNo(GetOrderIdUtils.getSeqNo(6));// 交易流水号
        bean.setChannel(BankCallConstant.CHANNEL_PC); // 交易渠道
        bean.setAccountId(accountId);// 电子账号
        bean.setLogOrderId(GetOrderIdUtils.getOrderId2(userId));// 订单号
        bean.setLogOrderDate(GetOrderIdUtils.getOrderDate());// 订单时间(必须)格式为yyyyMMdd，例如：20130307
        bean.setLogUserId(String.valueOf(userId));
        bean.setLogRemark("电子账户余额查询");
        bean.setLogClient(0);// 平台
        try {
            BankCallBean resultBean = BankCallUtils.callApiBg(bean);
            if (resultBean != null && BankCallStatusConstant.RESPCODE_SUCCESS.equals(resultBean.getRetCode())) {
                balance = new BigDecimal(resultBean.getAvailBal().replace(",", ""));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return balance;
    }
}
