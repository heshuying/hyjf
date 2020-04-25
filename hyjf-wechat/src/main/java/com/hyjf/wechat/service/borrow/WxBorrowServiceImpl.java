/**
 * Description:汇直投查询service接口实现
 * Copyright: Copyright (HYJF Corporation) 2015
 * Company: HYJF Corporation
 * @author: 王坤
 * @version: 1.0
 * Created at: 2015年11月11日 下午2:17:31
 * Modification History:
 * Modified by : 
 */
package com.hyjf.wechat.service.borrow;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import redis.clients.jedis.JedisPool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.calculate.CalculatesUtil;
import com.hyjf.common.calculate.InterestInfo;
import com.hyjf.common.util.CommonUtils;
import com.hyjf.common.util.CookieUtils;
import com.hyjf.common.util.CustomConstants;
import com.hyjf.common.util.DigitalUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.messageprocesser.MessageProcesser;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.Borrow;
import com.hyjf.mybatis.model.auto.BorrowExample;
import com.hyjf.mybatis.model.auto.BorrowManinfo;
import com.hyjf.mybatis.model.auto.BorrowManinfoExample;
import com.hyjf.mybatis.model.auto.BorrowProjectType;
import com.hyjf.mybatis.model.auto.BorrowProjectTypeExample;
import com.hyjf.mybatis.model.auto.BorrowRepay;
import com.hyjf.mybatis.model.auto.BorrowRepayExample;
import com.hyjf.mybatis.model.auto.BorrowRepayPlan;
import com.hyjf.mybatis.model.auto.BorrowTenderTmp;
import com.hyjf.mybatis.model.auto.BorrowTenderTmpInfo;
import com.hyjf.mybatis.model.auto.BorrowUsers;
import com.hyjf.mybatis.model.auto.BorrowUsersExample;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.mybatis.model.auto.UsersInfo;
import com.hyjf.mybatis.model.auto.UsersInfoExample;
import com.hyjf.mybatis.model.customize.app.AppProjectDetailCustomize;
import com.hyjf.mybatis.model.customize.app.AppProjectInvestListCustomize;
import com.hyjf.mybatis.model.customize.app.AppRiskControlCustomize;
import com.hyjf.mybatis.model.customize.coupon.CouponConfigCustomizeV2;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.wechat.base.BaseServiceImpl;
import com.hyjf.wechat.model.borrow.WxTenderVo;

@Service
public class WxBorrowServiceImpl extends BaseServiceImpl implements WxBorrowService {
	private Logger _log = LoggerFactory.getLogger(WxBorrowServiceImpl.class);
	
	
	@Autowired
    @Qualifier("smsProcesser")
    private MessageProcesser smsProcesser;

    public static JedisPool pool = RedisUtils.getPool();

    /**
     * 根据borrowNid获取借款项目
     * 
     * @param borrowNid
     * @return
     */
    @Override
    public AppProjectDetailCustomize selectProjectDetail(String borrowNid) {
        AppProjectDetailCustomize borrow = appProjectListCustomizeMapper.selectProjectDetail(borrowNid);
        return borrow;
    }
    
    
    /**
     * 执行前每个方法前需要添加BusinessDesc描述
     * @param borrowNid
     * @return
     * @author lm
     */
        
    @Override
    public BorrowUsers getBorrowUsersByNid(String borrowNid) {
        if(StringUtils.isBlank(borrowNid)){
            return null;
        }
        BorrowUsersExample example = new BorrowUsersExample();
        BorrowUsersExample.Criteria cra = example.createCriteria();
        cra.andBorrowNidEqualTo(borrowNid);
        List<BorrowUsers> list = this.borrowUsersMapper.selectByExample(example);
        if(list != null && list.size() > 0){
            // add by liushouyi nifa2 20181206 start
            // 处理企业注册时间（数据库存放格式YYYY-MM-DD）
            if (StringUtils.isNotBlank(list.get(0).getComRegTime()) && list.get(0).getComRegTime().length() >= 4){
                String comRegTime = list.get(0).getComRegTime().substring(0,4).concat("年");
                list.get(0).setComRegTime(comRegTime);
            }
            // add by liushouyi nifa2 20181206 end
            return list.get(0);
        }
        return null;
    }
    
    /**
     * 执行前每个方法前需要添加BusinessDesc描述
     * @param borrowNid
     * @return
     * @author Michael
     */
        
    @Override
    public BorrowManinfo getBorrowManinfoByNid(String borrowNid) {
        if(StringUtils.isBlank(borrowNid)){
            return null;
        }
        BorrowManinfoExample example = new BorrowManinfoExample();
        BorrowManinfoExample.Criteria cra = example.createCriteria();
        cra.andBorrowNidEqualTo(borrowNid);
        // upd by liushouyi nifa 20181206 start
//        List<BorrowManinfo> list = this.borrowManinfoMapper.selectByExample(example);
        List<BorrowManinfo> list = this.borrowManinfoCustomizeMapper.selectBorrowManInfoByBorrowNid(borrowNid);
        // upd by liushouyi nifa 20181206 end
        if(list != null && list.size() > 0){
            return list.get(0);
        }
        return null;
    }
    
    
    
    @Override
    public List<BorrowRepayPlan> findRepayPlanByBorrowNid(String borrowNid) {
        Borrow borrow = this.getBorrowByNid(borrowNid);
        String borrowStyle = borrow.getBorrowStyle();
        Integer projectType = borrow.getProjectType();
        BigDecimal yearRate = borrow.getBorrowApr();
        Integer totalMonth = borrow.getBorrowPeriod();
        BigDecimal account = borrow.getAccount();
        Integer time = borrow.getBorrowSuccessTime();
        if (time == null) {
            time = (int) (System.currentTimeMillis() / 1000);
        }
        List<BorrowRepayPlan> repayPlans = new ArrayList<>();
        // 差异费率
        BigDecimal differentialRate = Validator.isNull(borrow.getDifferentialRate()) ? BigDecimal.ZERO : new BigDecimal(borrow.getDifferentialRate());
        //初审时间
        int borrowVerifyTime =Validator.isNull(borrow.getVerifyTime()) ? 0 : Integer.parseInt(borrow.getVerifyTime());
        // 月利率(算出管理费用[上限])
        BigDecimal borrowMonthRate = Validator.isNull(borrow.getManageFeeRate()) ? BigDecimal.ZERO: new BigDecimal(borrow.getManageFeeRate());
        // 月利率(算出管理费用[下限])
        BigDecimal borrowManagerScaleEnd = Validator.isNull(borrow.getBorrowManagerScaleEnd()) ? BigDecimal.ZERO: new BigDecimal(borrow.getBorrowManagerScaleEnd());
        // 按月计息到期还本还息和按天计息，到期还本还息
        if (borrowStyle.equals(CalculatesUtil.STYLE_END) || borrowStyle.equals(CalculatesUtil.STYLE_ENDDAY)) {
            InterestInfo info = CalculatesUtil.getInterestInfo(account, totalMonth, yearRate, borrowStyle, time,borrowMonthRate, borrowManagerScaleEnd, projectType,differentialRate,borrowVerifyTime);
            if (info != null) {
                String repayTime = "-";
                // by cuigq App和web逻辑不一样，这里是按web的逻辑
                if (borrow.getStatus() >= 3 && borrow.getReverifyStatus() > 0) {
                    repayTime = GetDate.formatDate(GetDate.getDate(info.getRepayTime() * 1000L));
                }
                BorrowRepayPlan planIntrest = new BorrowRepayPlan();
                planIntrest.setRepayTime(repayTime);
                planIntrest.setRepayAccount(info.getRepayAccount());
                repayPlans.add(planIntrest);
            }
        } else {// 等额本息和等额本金和先息后本
            InterestInfo info = CalculatesUtil.getInterestInfo(account, totalMonth, yearRate, borrowStyle, time,
                    borrowMonthRate, borrowManagerScaleEnd, projectType,differentialRate,borrowVerifyTime);
            if (info.getListMonthly() != null) {
                String repayTime = "-";
                for (int i = 0; i < info.getListMonthly().size(); i++) {
                    InterestInfo sub = info.getListMonthly().get(i);
                    // 通过复审
                    if (borrow.getStatus() >= 3 && borrow.getReverifyStatus() > 0) {
                        repayTime = GetDate.formatDate(GetDate.getDate(sub.getRepayTime() * 1000L));
                    }
                    BorrowRepayPlan planIntrest = new BorrowRepayPlan();
                    planIntrest.setRepayTime(repayTime);
                    planIntrest.setRepayAccount(sub.getRepayAccount());
                    repayPlans.add(planIntrest);
                }
            }
        }
        return repayPlans;
    }
    
    
    /**
     * 统计指定项目的项目出借总数
     */
    @Override
    public int countProjectInvestRecordTotal(Map<String, Object> params) {
        int hztInvestTotal = appProjectListCustomizeMapper.countProjectInvestRecordTotal(params);
        return hztInvestTotal;
    }
    
    @Override
    public String countMoneyByBorrowId(Map<String, Object> params) {
        return this.borrowTenderInfoCustomizeMapper.countMoneyByBorrowId(params);
    }
    
    /**
     * 查询指定项目的项目出借详情
     */
    @Override
    public List<AppProjectInvestListCustomize> searchProjectInvestList(Map<String, Object> params) {
        List<AppProjectInvestListCustomize> list = appProjectListCustomizeMapper.selectProjectInvestList(params);
        return list;
    }
    /**
     * 取得用户优惠券信息
     * 
     * @param couponGrantId
     * @return
     */
    @Override
    public CouponConfigCustomizeV2 getCouponUser(String couponGrantId, Integer userId) {
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("couponGrantId", couponGrantId);
        paramMap.put("userId", userId);
        CouponConfigCustomizeV2 ccTemp = this.couponUserCustomizeMapper.selectCouponConfigByGrantId(paramMap);
        return ccTemp;
    }
    
    
    /**
     * 检查参数的正确性(优惠券出借，无本金出借)
     * 
     * @param userId
     * @param transAmt
     *            交易金额
     * @param flag
     *            交易类型，1购买 2赎回
     * @return
     */
    @Override
    public JSONObject checkParamForCoupon(Borrow borrow, WxTenderVo vo, String userId, CouponConfigCustomizeV2 cuc, String couponGrantId) {

        // 判断用户userId是否存在
        if (StringUtils.isEmpty(userId) || !DigitalUtils.isInteger(userId)) {
            return jsonMessage("您未登陆，请先登录", "1");
        }
        Users user = this.getUsers(Integer.parseInt(userId));
        // 判断用户信息是否存在
        if (user == null) {
            return jsonMessage("用户信息不存在", "1");
        }
        // 判断用户是否禁用
        if (user.getStatus() == 1) {// 0启用，1禁用
            return jsonMessage("该用户已被禁用", "1");
        }
        if (borrow == null || borrow.getProjectType() == null) {
            return jsonMessage("未设置该出借项目的项目类型", "1");
        }
        BorrowProjectType borrowProjectType = this.getBorrowProjectType(String.valueOf(borrow.getProjectType()));
        if (borrowProjectType == null) {
            return jsonMessage("未查询到该出借项目的设置信息", "1");
        }
        // 51老用户标
        if (borrowProjectType.getInvestUserType().equals("0")) {
            boolean is51User = this.checkIs51UserCanInvest(Integer.parseInt(userId));
            if (!is51User) {
                return jsonMessage("该项目只能51老用户出借", "1");
            }
        }
        if (borrowProjectType.getInvestUserType().equals("1")) {
            boolean newUser = this.checkIsNewUserCanInvest(Integer.parseInt(userId), borrow.getProjectType());
            if (!newUser) {
                return jsonMessage("该项目只能新手出借", "1");
            }
        }

        if (/*"0".equals(vo.getAccount()) && cuc.getCouponType() != 3 && cuc.getCouponType() != 1*/
                ("0".equals(vo.getAccount()) && cuc == null)
                        || ("0".equals(vo.getAccount()) && cuc != null && cuc.getCouponType() == 2)) {
            return jsonMessage("出借金额不能为0元", "1");
        }

        if (!"0".equals(vo.getAccount()) && StringUtils.isNotEmpty(vo.getAccount()) && cuc.getCouponType() == 1 && cuc.getAddFlg() == 1) {
            return jsonMessage("该优惠券只能单独使用", "1");
        }
        // 用户开户信息
//        System.out.println("==============cwyang 校验用户开户信息=======userID " + userId);
        BankOpenAccount accountChinapnrTender = this.getBankOpenAccount(Integer.parseInt(userId));
//      AccountChinapnr accountChinapnrTender = this.getAccountChinapnr(Integer.parseInt(userId));
//        System.out.println("================cwyang 判断开户信息 userID is " + userId);
        // 用户未在平台开户
        if (accountChinapnrTender == null) {
//            System.out.println("===============cwyang 开户信息不存在!");
            return jsonMessage("用户开户信息不存在", "1");
        }
        // 判断借款人开户信息是否存在
        if (accountChinapnrTender.getAccount() == null) {
            return jsonMessage("用户银行账户号不存在", "1");
        }
        // 判断借款信息是否存在
        if (borrow == null || borrow.getId() == null) {
            return jsonMessage("借款项目不存在", "1");
        }
        if (borrow.getUserId() == null) {
            return jsonMessage("借款人不存在", "1");
        }
        // 借款人开户信息
        BankOpenAccount accountChinapnrBorrower  = this.getBankOpenAccount(borrow.getUserId());
        if (accountChinapnrBorrower == null) {
            return jsonMessage("借款人未开户", "1");
        }
        if (accountChinapnrBorrower.getAccount() == null) {
            return jsonMessage("借款人银行帐户号不存在", "1");
        }
        if (userId.equals(String.valueOf(borrow.getUserId()))) {
            return jsonMessage("借款人不可以自己出借项目", "1");
        }
        // 判断借款是否流标
        if (borrow.getStatus() == 6) { // 流标
            return jsonMessage("此项目已经流标", "1");
        }
        // 已满标
        if (borrow.getBorrowFullStatus() == 1) {
            return jsonMessage("此项目已经满标", "1");
        }
        // 有优惠校验
        JSONObject jsonError = this.validateCoupon(vo.getAccount(), borrow, "1", couponGrantId, userId);
        if (jsonError != null) {
            return jsonError;
        } else {
            // 如果验证没问题，则返回出借人借款人的汇付账号
            Long borrowerUsrcustid = Long.parseLong(accountChinapnrBorrower.getAccount());
            Long tenderUsrcustid = Long.parseLong(accountChinapnrTender.getAccount());
            JSONObject jsonMessage = new JSONObject();
            jsonMessage.put(CustomConstants.APP_STATUS, "0");
            jsonMessage.put("borrowerUsrcustid", borrowerUsrcustid);
            jsonMessage.put("tenderUsrcustid", tenderUsrcustid);
            jsonMessage.put("borrowId", borrow.getId());
//            System.out.println("=================cwyang 返回信息为:　" + jsonMessage);
            return jsonMessage;
        }
    }
    
    /**
     * 有优惠校验
     * @param account
     * @param couponConfig
     * @param borrow
     * @param platform
     * @param cuc
     * @return
     */
    private JSONObject validateCoupon(String account, Borrow borrow, String platform, String couponGrantId, String userId) {

        JSONObject jsonObject = CommonSoaUtils.CheckCoupon(userId, borrow.getBorrowNid(), account, platform, couponGrantId);
        int status = jsonObject.getIntValue("status");
        String statusDesc = jsonObject.getString("statusDesc");
        if (status == 1) {
            return jsonMessage(statusDesc, status + "");
        }
        return null;
    }
    public BorrowProjectType getBorrowProjectType(String projectType) {
        if (StringUtils.isEmpty(projectType)) {
            return null;
        }
        // 查找用户
        BorrowProjectTypeExample borrowProjectTypeExample = new BorrowProjectTypeExample();
        BorrowProjectTypeExample.Criteria criteria2 = borrowProjectTypeExample.createCriteria();
        criteria2.andBorrowCdEqualTo(projectType);
        List<BorrowProjectType> list = borrowProjectTypeMapper.selectByExample(borrowProjectTypeExample);
        BorrowProjectType borrowProjectType = null;
        if (list != null && !list.isEmpty()) {
            borrowProjectType = list.get(0);

        }
        return borrowProjectType;
    }
    
    /**
     * 新用户新手标验证，标如果是新用户标，查看此用户是否有过出借记录，如果有返回true，提示不能投标了
     *
     * @param userId
     * @param projectType
     * @return
     */
    public boolean checkIsNewUserCanInvest(Integer userId, Integer projectType) {

        // 新的判断是否为新用户方法
        int total = webUserInvestListCustomizeMapper.countNewUserTotal(userId + "");
        if (total == 0) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 判断是否51老用户,如果是则返回true，否则返回false
     *
     * @param userId
     * @param projectType
     * @return
     * @author b
     */
    public boolean checkIs51UserCanInvest(Integer userId) {
        UsersInfoExample usersInfoExample = new UsersInfoExample();
        UsersInfoExample.Criteria borrowCriteria = usersInfoExample.createCriteria();
        borrowCriteria.andUserIdEqualTo(userId);
        List<UsersInfo> list = usersInfoMapper.selectByExample(usersInfoExample);
        if (list != null && !list.isEmpty()) {
            UsersInfo usersInfo = list.get(0);
            if (usersInfo != null) {
                Integer is51 = usersInfo.getIs51();// 1是51，0不是
                if (is51 != null && is51 == 1) {
                    return true;
                }
            }
        }
        return false;

    }
    /**
     * 组成返回信息
     * 
     * @param message
     * @param status
     * @return
     */
    public JSONObject jsonMessage(String data, String error) {
        JSONObject jo = null;
        if (Validator.isNotNull(data)) {
            jo = new JSONObject();
            jo.put(CustomConstants.APP_STATUS, CustomConstants.APP_STATUS_FAIL);
            jo.put(CustomConstants.APP_STATUS_DESC, data);
        }
        return jo;
    }
    /**
     * 检查参数的正确性
     * 
     * @param userId
     * @param transAmt
     *            交易金额
     * @param flag
     *            交易类型，1购买 2赎回
     * @return
     */
    public JSONObject checkParam(String borrowNid, String account, String userId, CouponConfigCustomizeV2 cuc) {
        if (account == null || "".equals(account)) {
            account = "0";
        }
        _log.info("=============微官网开始检查参数==================");
        // 判断用户userId是否存在
        if (StringUtils.isEmpty(userId) || !DigitalUtils.isInteger(userId)) {
            return jsonMessage("您未登陆，请先登录", "1");
        }
        Users user = this.getUsers(Integer.parseInt(userId));
        // 判断用户信息是否存在
        if (user == null) {
            return jsonMessage("用户信息不存在", "1");
        }
        UsersInfoExample usersInfoExample = new UsersInfoExample();
        usersInfoExample.createCriteria().andUserIdEqualTo(Integer.parseInt(userId));
        List<UsersInfo> usersInfos = this.usersInfoMapper.selectByExample(usersInfoExample);
        if (null != usersInfos && usersInfos.size() == 1) {
            String roleIsOpen = PropUtils.getSystem(CustomConstants.HYJF_ROLE_ISOPEN);
            if(StringUtils.isNotBlank(roleIsOpen) && roleIsOpen.equals("true")){
                if (usersInfos.get(0).getRoleId() != 1) {// 担保机构用户
                    return jsonMessage("仅限出借人进行出借", "1");
                }
            }


            
        } else {
            return jsonMessage("账户信息异常", "1");
        }
        // 判断用户是否禁用
        if (user.getStatus() == 1) {// 0启用，1禁用
            return jsonMessage("该用户已被禁用", "1");
        }
        // 判断用户是否设置了交易密码
        if (user.getIsSetPassword() == 0) {
            return jsonMessage("该用户未设置交易密码", "1");
        }
        // 判断借款编号是否存在
        if (StringUtils.isEmpty(borrowNid)) {
            return jsonMessage("借款项目不存在", "1");
        }
        Borrow borrow = this.getBorrowByNid(borrowNid);
        // 判断借款信息是否存在
        if (borrow == null || borrow.getId() == null) {
            return jsonMessage("借款项目不存在", "1");
        }
        if (borrow.getUserId() == null) {
            return jsonMessage("借款人不存在", "1");
        }
        Integer projectType = borrow.getProjectType();// 0，51老用户；1，新用户；2，全部用户
        if (projectType == null) {
            return jsonMessage("未设置该出借项目的项目类型", "1");
        }
        BorrowProjectType borrowProjectType = this.getBorrowProjectType(String.valueOf(projectType));
        if (borrowProjectType == null) {
            return jsonMessage("未查询到该出借项目的设置信息", "1");
        }
        // 51老用户标
        if (borrowProjectType.getInvestUserType().equals("0")) {
            boolean is51User = this.checkIs51UserCanInvest(Integer.parseInt(userId));
            if (!is51User) {
                return jsonMessage("该项目只能51老用户出借", "1");
            }
        }
        if (borrowProjectType.getInvestUserType().equals("1")) {
            boolean newUser = this.checkIsNewUserCanInvest(Integer.parseInt(userId), projectType);
            if (!newUser) {
                return jsonMessage("该项目只能新手出借", "1");
            }
        }
        BankOpenAccount accountChinapnrCrediter = this.getBankOpenAccount(Integer.parseInt(userId));
        // 用户未在平台开户
        if (accountChinapnrCrediter == null) {
//            System.out.println("============= 用户开户信息不存在===userid is====" + userId);
            return jsonMessage("用户开户信息不存在", "1");
        }
        // 判断借款人开户信息是否存在
        if (accountChinapnrCrediter.getAccount() == null) {
            return jsonMessage("用户银行账户号不存在", "1");
        }
        
        
        // 出借客户端
        if ( borrow.getCanTransactionWei().equals("0")) {
            String tmpInfo = "";
            if (borrow.getCanTransactionAndroid().equals("1")) {
                tmpInfo += " Android端  ";
            }
            if (borrow.getCanTransactionIos().equals("1")) {
                tmpInfo += " Ios端  ";
            }
            if (borrow.getCanTransactionPc().equals("1")) {
                tmpInfo += " Pc端  ";
            }
            return jsonMessage("此项目只能在" + tmpInfo + "出借", "1");
        }
        // 借款人开户信息
        BankOpenAccount accountChinapnrBorrower = this.getBankOpenAccount(Integer.parseInt(userId));
        if (accountChinapnrBorrower == null) {
            return jsonMessage("借款人未开户", "1");
        }
        if (accountChinapnrBorrower.getAccount() == null) {
            return jsonMessage("借款人银行账户号不存在", "1");
        }
        // 检查服务费授权
        Integer authStatus = CommonUtils.checkPaymentAuthStatus(user.getPaymentAuthStatus());
        if(authStatus==0){
            return jsonMessage("未进行服务费授权", "1");
        }
        if (userId.equals(String.valueOf(borrow.getUserId()))) {
            return jsonMessage("借款人不可以自己出借项目", "1");
        }
        // 判断借款是否流标
        if (borrow.getStatus() == 6) { // 流标
            return jsonMessage("此项目已经流标", "1");
        }
        // 已满标
        if (borrow.getBorrowFullStatus() == 1) {
            return jsonMessage("此项目已经满标", "1");
        }
        // 判断用户出借金额是否为空
        if (!(StringUtils.isNotEmpty(account) || (StringUtils.isEmpty(account) && cuc != null && cuc.getCouponType() == 3))) {
            return jsonMessage("请输入出借金额", "1");
        }
        try {
        	account=new BigDecimal(account).toString();
		} catch (Exception e) {
			return jsonMessage("出借金额格式错误", "1");
		}
        // 还款金额是否数值
        if (!DigitalUtils.isNumber(account)) {
            return jsonMessage("出借金额格式错误", "1");
        }
        if (("0".equals(account) && cuc == null)
                        || ("0".equals(account) && cuc != null && cuc.getCouponType() == 2)) {
            return jsonMessage("出借金额不能为0元", "1");
        }
        try {
            // 出借金额必须是整数
            int accountInt = Integer.parseInt(account);
            if (accountInt != 0 && cuc != null && cuc.getCouponType() == 1 && cuc.getAddFlg() == 1) {
                return jsonMessage("该优惠券只能单独使用", "1");
            }
            if (accountInt < 0) {
                return jsonMessage("出借金额不能为负数", "1");
            }
            // 将出借金额转化为BigDecimal
            BigDecimal accountBigDecimal = new BigDecimal(account);
            String balance = RedisUtils.get(borrowNid);
            if (StringUtils.isEmpty(balance)) {
                return jsonMessage("您来晚了，下次再来抢吧", "1");
            }
            // 起投金额
            Integer min = borrow.getTenderAccountMin();
            // 当剩余可投金额小于最低起投金额，不做最低起投金额的限制
            if (min != null && min != 0 && new BigDecimal(balance).compareTo(new BigDecimal(min)) == -1) {
                if (accountBigDecimal.compareTo(new BigDecimal(balance)) == 1) {
                    return jsonMessage("出借金额不能大于项目剩余", "1");
                }
                if (accountBigDecimal.compareTo(new BigDecimal(balance)) != 0) {
                    return jsonMessage("剩余可投只剩" + balance + "元，须全部购买", "1");
                }
            } else {// 项目的剩余金额大于最低起投金额
                if (min != null && min != 0 && accountBigDecimal.compareTo(new BigDecimal(min)) == -1) {
                    if (accountBigDecimal.compareTo(BigDecimal.ZERO) == 0) {
                        if (cuc != null && cuc.getCouponType() != 3 && cuc.getCouponType() != 1) {
                            return jsonMessage(borrow.getTenderAccountMin() + "元起投", "1");
                        }
                    } else {
                        return jsonMessage(borrow.getTenderAccountMin() + "元起投", "1");
                    }
                } else {
                    Integer max = borrow.getTenderAccountMax();
                    if (max != null && max != 0 && accountBigDecimal.compareTo(new BigDecimal(max)) == 1) {
                        return jsonMessage("项目最大出借额为" + max + "元", "1");
                    }
                }
            }
            if (accountBigDecimal.compareTo(borrow.getAccount()) > 0) {
                return jsonMessage("出借金额不能大于项目总额", "1");
            }
            // 用户账户信息
            Account tenderAccount = this.getAccount(Integer.parseInt(userId));
            if (tenderAccount.getBankBalance().compareTo(accountBigDecimal) < 0) {
                return jsonMessage("可用金额不足", "1");
            }
            // redis剩余金额不足
            if (accountBigDecimal.compareTo(new BigDecimal(balance)) == 1) {
                return jsonMessage("出借金额不能大于项目剩余", "1");
            } else {
                AppProjectDetailCustomize borrowDetail = appProjectListCustomizeMapper.selectProjectDetail(borrowNid);
                // add by 在只使用代金券和体验金,并且没有本金的情况下,不进行出借递增金额的判断,在出借金额等于最大可投金额时也不做递增金额的判断
                if (!(cuc != null && (cuc.getCouponType() == 3||cuc.getCouponType() == 1) && accountInt == 0)) {
                    if (borrowDetail.getIncreaseMoney() != null && (accountInt - min) % Integer.parseInt(borrowDetail.getIncreaseMoney()) != 0
                            && accountBigDecimal.compareTo(new BigDecimal(balance)) == -1 && accountInt < borrow.getTenderAccountMax()) {
                        return jsonMessage("出借递增金额须为" + borrowDetail.getIncreaseMoney() + " 元的整数倍", "1");
                    }
                }
                
                // 如果验证没问题，则返回出借人借款人的银行账号
                Long borrowerUsrcustid = Long.parseLong(accountChinapnrBorrower.getAccount());
                Long tenderUsrcustid = Long.parseLong(accountChinapnrCrediter.getAccount());
                JSONObject jsonMessage = new JSONObject();
                jsonMessage.put(CustomConstants.APP_STATUS, "0");
                jsonMessage.put("borrowerUsrcustid", borrowerUsrcustid);
                jsonMessage.put("tenderUsrcustid", tenderUsrcustid);
                jsonMessage.put("borrowId", borrow.getId());
                jsonMessage.put("bankInputFlag", borrow.getBankInputFlag() + "");
                jsonMessage.put("tenderUserName", user.getUsername());
                _log.info("返回信息为:" + jsonMessage);
                return jsonMessage;
            }

        } catch (Exception e) {
            return jsonMessage("出借金额必须为整数", "1");
        }
    }
    
    /**
     * 调用汇付天下接口前操作,
     * 插入huiyingdai_borrow_tender_tmp和huiyingdai_borrow_tender_tmpinfo表
     *
     * @param borrowNid
     *            借款id
     * @param userId
     *            用户id
     * @param account
     *            出借金额
     * @param ip
     *            出借人ip
     * @return 出借是否成功
     */
    @Override
    public Boolean updateBeforeChinaPnR(HttpServletRequest request, String borrowNid, String orderId, Integer userId, String account, String ip, String couponGrantId, String userName) {

        BorrowTenderTmp temp = new BorrowTenderTmp();
        temp.setUserId(userId);
        temp.setBorrowNid(borrowNid);
        temp.setNid(orderId);
        temp.setAccount(new BigDecimal(account));
        temp.setAddip(ip);
        temp.setChangeStatus(0);
        temp.setChangeUserid(0);
        temp.setChangePeriod(0);
        temp.setTenderStatus(0);
        temp.setTenderNid(borrowNid);
        temp.setTenderAwardAccount(new BigDecimal(0));
        temp.setRecoverFullStatus(0);
        temp.setRecoverFee(new BigDecimal(0));
        temp.setRecoverType("");
        temp.setRecoverAdvanceFee(new BigDecimal(0));
        temp.setRecoverLateFee(new BigDecimal(0));
        temp.setTenderAwardFee(new BigDecimal(0));
        temp.setContents("");
        temp.setAutoStatus(0);
        temp.setWebStatus(0);
        temp.setPeriodStatus(0);
        temp.setWeb(0);
        temp.setIsBankTender(1);
        temp.setAddtime(String.valueOf(GetDate.getNowTime10()));
        temp.setTenderUserName(userName);
        if (StringUtils.isBlank(couponGrantId)) {
            couponGrantId = "0";
        }
        temp.setCouponGrantId(Integer.parseInt(couponGrantId));// add by cwyang 为出借完全掉单优惠券出借时修复做记录
        //如果是风车理财出借就将wrb插入到该表的tender_from中
        Cookie cookie = CookieUtils.getCookieByName(request,CustomConstants.TENDER_FROM_TAG);
        _log.info("测试风车理财出借......");
        if (cookie != null && cookie.getValue() != null){
            String cookieValue = cookie.getValue();
            _log.info("出借来源为 ：{}", cookieValue);
            temp.setTenderFrom(cookieValue);
        }
        boolean tenderTmpFlag = borrowTenderTmpMapper.insertSelective(temp) > 0 ? true : false;
        if (!tenderTmpFlag) {
            throw new RuntimeException("插入borrowTenderTmp表失败，出借订单号：" + orderId);
        }
        BorrowTenderTmpInfo info = new BorrowTenderTmpInfo();
        info.setOrdid(orderId);
        Map<String, String> map = new HashMap<String, String>();
        map.put("borrow_nid", borrowNid);
        map.put("user_id", userId + "");
        map.put("account", account + "");
        map.put("status", "0");
        map.put("nid", orderId);
        map.put("addtime", (new Date().getTime() / 1000) + "");
        map.put("addip", ip);
        String array = JSON.toJSONString(map);
        info.setTmpArray(array);
        info.setAddtime((new Date().getTime() / 1000) + "");
        Boolean tenderTmpInfoFlag = borrowTenderTmpInfoMapper.insertSelective(info) > 0 ? true : false;
        if (!tenderTmpInfoFlag) {
            throw new RuntimeException("插入borrowTenderTmpInfo表失败，出借订单号：" + orderId);
        }
        return tenderTmpInfoFlag;

    }
    
    
    /**
     * 项目可投金额
     * @param borrowNid
     * @return
     */
    @Override
    public String getBorrowAccountWait(String borrowNid) {
        BorrowExample borrowExample = new BorrowExample();
        BorrowExample.Criteria criteria = borrowExample.createCriteria();
        criteria.andBorrowNidEqualTo(borrowNid);
        List<Borrow> borrows = borrowMapper.selectByExample(borrowExample);
        if (borrows!= null && borrows.size()>0){
            return String.valueOf(borrows.get(0).getBorrowAccountWait());
        }
        return null;
    }


	@Override
	public AppRiskControlCustomize selectRiskControl(String borrowNid) {
		AppRiskControlCustomize riskControl = appProjectListCustomizeMapper.selectRiskControl(borrowNid);
		return riskControl;
	}
	
	
	/**
	 * 取得借款信息
	 *
	 * @return
	 */
	@Override
	public BorrowRepay getBorrowRepay(String borrowNid) {
		BorrowRepayExample example = new BorrowRepayExample();
		BorrowRepayExample.Criteria criteria = example.createCriteria();
		criteria.andBorrowNidEqualTo(borrowNid);
		List<BorrowRepay> list = this.borrowRepayMapper.selectByExample(example);

		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}
	
}
