package com.hyjf.api.web.user.synbalance;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.hyjf.api.in.coupon.send.UserCouponServer;
import com.hyjf.api.web.BaseController;
import com.hyjf.bank.service.user.synbalance.SynBalanceBean;
import com.hyjf.bank.service.user.synbalance.SynBalanceService;
import com.hyjf.base.bean.BaseBean;
import com.hyjf.base.bean.BaseDefine;
import com.hyjf.base.bean.BaseResultBean;
import com.hyjf.common.cache.RedisUtils;
import com.hyjf.common.log.LogUtil;
import com.hyjf.common.security.utils.MD5;
import com.hyjf.common.util.*;
import com.hyjf.common.validator.Validator;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.BankOpenAccount;
import com.hyjf.mybatis.model.auto.UnderLineRecharge;
import com.hyjf.mybatis.model.auto.Users;
import com.hyjf.pay.lib.bank.bean.BankCallBean;
import com.hyjf.pay.lib.bank.util.BankCallConstant;
import com.hyjf.soa.apiweb.CommonSoaUtils;
import com.hyjf.user.synbalance.ResultBean;
import com.hyjf.user.synbalance.SynBalanceDefine;
import com.hyjf.user.synbalance.SynBalanceRequestBean;
import com.hyjf.user.synbalance.SynBalanceResultBean;
import com.hyjf.vip.apply.ApplyDefine;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping(value = SynBalanceDefine.REQUEST_MAPPING)
public class SynBalanceServer extends BaseController {
    Logger _log = LoggerFactory.getLogger(UserCouponServer.class);
    @Autowired
    private SynBalanceService synBalanceService;
    
    /**
     * 
     * 同步余额
     * @author hsy
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = SynBalanceDefine.SYNBALANCE_ACTION)
    public SynBalanceResultBean synBalance(@ModelAttribute SynBalanceRequestBean synBalanceRequestBean, HttpServletRequest request, HttpServletResponse response) {
        LogUtil.startLog(this.getClass().getName(), SynBalanceDefine.SYNBALANCE_ACTION);
        SynBalanceResultBean resultBean = new SynBalanceResultBean();
        
        //验证请求参数
        if (Validator.isNull(synBalanceRequestBean.getUserId())) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            _log.info("-------------------请求参数非法--------------------");
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        System.out.println(synBalanceRequestBean.getUserId()+"同步余额开始-----------------------------");
        //验签
        if(!this.checkSign(synBalanceRequestBean, BaseDefine.METHOD_SYNBALANCE)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            _log.info("-------------------验签失败！--------------------");
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        
        DecimalFormat df = CustomConstants.DF_FOR_VIEW;
        Users user = synBalanceService.getUsers(synBalanceRequestBean.getUserId());//用户ID
        if(user == null){
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            _log.info("-------------------用户未登陆--------------------");
            resultBean.setStatusDesc("用户未登陆");
            
            LogUtil.endLog(this.getClass().getName(), SynBalanceDefine.SYNBALANCE_ACTION);
            return resultBean;
        }
        //用户可用余额
        Account accountUser = synBalanceService.getAccount(user.getUserId());
        BigDecimal accountBalance = accountUser.getBankBalance();
        //客户号
        BankOpenAccount bankOpenAccount=synBalanceService.getBankOpenAccount(user.getUserId());
        if(bankOpenAccount==null){
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            _log.info("-------------------用户未开户--------------------");
            resultBean.setStatusDesc("用户未开户");
            
            LogUtil.endLog(this.getClass().getName(), SynBalanceDefine.SYNBALANCE_ACTION);
            return resultBean;
        }
        //查询时间段 (只查当天)
        //上线需放开
        String startDate = getTxDate(user.getUserId());
        String endDate = GetOrderIdUtils.getTxDate();
        
        /*String startDate = "20170105";
        String endDate = "20170105";*/
        String inpDate = "";
        String inpTime = "";
        String relDate = "";
        String traceNo = "";
        //页码定义
        int pageNum = 1;
        int pageSize = 10;
        //调用查询明细接口 查线下充值数据
        BankCallBean retBean = synBalanceService.queryAccountDetails(user.getUserId(), bankOpenAccount.getAccount(), 
                startDate, endDate, "1", "", String.valueOf(pageNum), String.valueOf(pageSize),inpDate,inpTime,relDate,traceNo);

        if(retBean == null){
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            _log.info("-------------------同步余额失败--------------------");
            resultBean.setStatusDesc("同步余额失败");
            
            LogUtil.endLog(this.getClass().getName(), SynBalanceDefine.SYNBALANCE_ACTION);
            return resultBean;
        }
        //返回失败
        if(!BankCallConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())){
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            _log.info("-------------------调用查询接口失败，失败原因：" + synBalanceService.getBankRetMsg(retBean.getRetCode())+"--------------------");
            resultBean.setStatusDesc("调用查询接口失败，失败原因：" + synBalanceService.getBankRetMsg(retBean.getRetCode()));
            
            LogUtil.endLog(this.getClass().getName(), SynBalanceDefine.SYNBALANCE_ACTION);
            return resultBean;
        }
        
        //解析返回数据(记录为空)
        String content = retBean.getSubPacks();
        if(StringUtils.isEmpty(content)){
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
            resultBean.setOriginalBankTotal(accountUser.getBankTotal().toString());
            resultBean.setOriginalBankBalance(accountUser.getBankBalance().toString());
            resultBean.setBankTotal(df.format(accountUser.getBankTotal()));
            resultBean.setBankBalance(df.format(accountBalance));
            
            LogUtil.endLog(this.getClass().getName(), SynBalanceDefine.SYNBALANCE_ACTION);
            return resultBean;
        }
        //返回结果记录数
        //转换结果
        List<ResultBean> recordList = new ArrayList<ResultBean>();
        List<ResultBean> list = new ArrayList<ResultBean>();
        list = JSONArray.parseArray(retBean.getSubPacks(), ResultBean.class);
        if(list==null|| list.size()==0){
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
            resultBean.setOriginalBankTotal(accountUser.getBankTotal().toString());
            resultBean.setOriginalBankBalance(accountUser.getBankBalance().toString());
            resultBean.setBankTotal(df.format(accountUser.getBankTotal()));
            resultBean.setBankBalance(df.format(accountBalance));
            LogUtil.endLog(this.getClass().getName(), SynBalanceDefine.SYNBALANCE_ACTION);
            return resultBean;
        }else{
            ResultBean lastResult = list.get(list.size()-1);
            inpDate = lastResult.getInpDate();
            inpTime = lastResult.getInpTime();
            relDate = lastResult.getRelDate();
            traceNo = String.valueOf(lastResult.getTraceNo());
        }
        recordList.addAll(list);

        
        while (list.size()==pageSize) {
            pageNum ++;
            //调用查询明细接口 查线下充值数据
            BankCallBean bean = synBalanceService.queryAccountDetails(user.getUserId(), bankOpenAccount.getAccount(), 
                    startDate, endDate, "1", "", String.valueOf(pageNum), String.valueOf(pageSize),inpDate,inpTime,relDate,traceNo);
            if(bean == null){
                continue;
            }
            list = JSONArray.parseArray(bean.getSubPacks(), ResultBean.class);
            recordList.addAll(list);
            if(list!=null&&list.size()>0){
                ResultBean lastResult = list.get(list.size()-1);
                inpDate = lastResult.getInpDate();
                inpTime = lastResult.getInpTime();
                relDate = lastResult.getRelDate();
                traceNo = String.valueOf(lastResult.getTraceNo());
            }
        }
        _log.info("-------------------"+recordList.size()+"同步余额总条数--------------------");
        _log.info("-------------------"+pageNum+"同步余额请求次数userid:"+user.getUserId()+"--------------------");
        /**redis 锁 */
        boolean reslut = RedisUtils.tranactionSet("synBalance:"+user.getUserId(),30);
        // 如果没有设置成功，说明有请求来设置过
        if(!reslut){
            //成功???
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
            resultBean.setOriginalBankTotal(accountUser.getBankTotal().toString());
            resultBean.setOriginalBankBalance(accountUser.getBankBalance().toString());
            resultBean.setBankTotal(df.format(accountUser.getBankTotal()));
            resultBean.setBankBalance(df.format(accountBalance));
            LogUtil.endLog(this.getClass().getName(), SynBalanceDefine.SYNBALANCE_ACTION);
            return resultBean;
        }
        for (int i = 0; i < recordList.size(); i++) {
            ResultBean record = recordList.get(i);
            //是否冲正订单
            if(record != null && ("O".equals(record.getOrFlag()) || "0".equals(record.getOrFlag()))){ //原始订单，冲正订单不处理
                //是否是线下充值交易类型
                boolean result1 = isRechargeTransType(record.getTranType());
                if(result1){
                    SynBalanceBean synBalanceBean=new SynBalanceBean();
                    // 如果江西银行不返回电子账户号  就设置本地的电子帐户号
                    if(record.getAccountId()==null){
                        record.setAccountId(bankOpenAccount.getAccount());
                    }
                    BeanUtils.copyProperties(record, synBalanceBean);
                    
                    boolean flag=false;

                    try {
                        flag = synBalanceService.insertAccountDetails(accountUser,synBalanceBean,user.getUsername(),GetCilentIP.getIpAddr(request));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(flag){
                        //更新金额
                    	CommonSoaUtils.listedTwoRecharge(user.getUserId(), record.getTxAmount());
                        accountBalance = accountBalance.add(record.getTxAmount());
                    }  
                    _log.info("-------------------更新线下充值，电子帐户号："+synBalanceBean.getAccountId()+"完毕--------------------");
                }
            }
        }
        _log.info("-------------------"+synBalanceRequestBean.getUserId()+"同步余额结束--------------------");
        accountUser = synBalanceService.getAccount(user.getUserId());
        resultBean.setOriginalBankTotal(accountUser.getBankTotal().toString());
        resultBean.setOriginalBankBalance(accountUser.getBankBalance().toString());
        resultBean.setBankBalance(df.format(accountUser.getBankBalance()));
        resultBean.setBankTotal(df.format(accountUser.getBankTotal()));
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), SynBalanceDefine.SYNBALANCE_ACTION);
        return resultBean;
    }

    /**
     * 判断是否属于线下充值类型.
     * 	优先从Redis中取数据,当Redis中的数据为空时,从数据表中读取数据
     * @param tranType
     * @return
     * @Author : huanghui
     */
    private boolean isRechargeTransType(String tranType) {
        //从Redis获取线下充值类型List
        String codeStringList = RedisUtils.get(RedisConstants.UNDER_LINE_RECHARGE_TYPE);
        JSONArray redisCodeList = JSONArray.parseArray(codeStringList);

        if (StringUtils.isBlank(codeStringList) || redisCodeList.size() <= 0){
            LogUtil.infoLog(this.getClass().getName(), "---------------------------线下充值类型Redis为空!-------------------------");

            List<UnderLineRecharge> codeList = synBalanceService.selectByExample();
            if (codeList.isEmpty()){
                LogUtil.infoLog(this.getClass().getName(), "---------------------------线下充值类型数据库未配置!-------------------------");
                return false;
            }else {
                for (UnderLineRecharge code : codeList){
                    if (code.getCode().equals(tranType)){
                        return true;
                    }else {
                        continue;
                    }
                }
            }
        }else {

            for(Object code : redisCodeList) {
                if (code.equals(tranType)){
                    return true;
                }else {
                    continue;
                }
            }
        }
        return false;
    }

    /**
     * 
     * 同步余额 根据用户江西银行电子帐号
     * @author dxj
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = SynBalanceDefine.SYNBALANCE_BANK_ACTION)
    public SynBalanceResultBean synBankBalance(@ModelAttribute SynBalanceRequestBean synBalanceRequestBean, HttpServletRequest request, HttpServletResponse response) {
    	LogUtil.startLog(this.getClass().getName(), SynBalanceDefine.SYNBALANCE_BANK_ACTION);
    	SynBalanceResultBean resultBean = new SynBalanceResultBean();
        
        //验证请求参数
        if (Validator.isNull(synBalanceRequestBean.getBankAccount())) {
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            _log.info("-------------------请求参数非法--------------------");
            resultBean.setStatusDesc("请求参数非法");
            return resultBean;
        }
        _log.info(synBalanceRequestBean.getBankAccount()+"同步余额开始-----------------------------");
        //验签
        if(!this.chkSysBalanceSign(synBalanceRequestBean)){
            resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            _log.info("-------------------验签失败！--------------------");
            resultBean.setStatusDesc("验签失败！");
            return resultBean;
        }
        
        //根据账号找出用户ID
        BankOpenAccount openAccount = synBalanceService.getBankOpenAccount(synBalanceRequestBean.getBankAccount());
        if(openAccount == null){
        	resultBean.setStatus(BaseResultBean.STATUS_FAIL);
            _log.info("没有根据电子银行卡找到用户 "+synBalanceRequestBean.getBankAccount());
            resultBean.setStatusDesc("没有根据电子银行卡找到用户 ");
            return resultBean;
        }
        
        DecimalFormat df = CustomConstants.DF_FOR_VIEW;
        Users user = synBalanceService.getUsers(openAccount.getUserId());//用户ID
        if(user == null){
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            _log.info("-------------------用户未登陆--------------------");
            resultBean.setStatusDesc("用户未登陆");
            
            LogUtil.endLog(this.getClass().getName(), SynBalanceDefine.SYNBALANCE_BANK_ACTION);
            return resultBean;
        }
        //用户可用余额
        Account accountUser = synBalanceService.getAccount(user.getUserId());
        BigDecimal accountBalance = accountUser.getBankBalance();
        //客户号
        BankOpenAccount bankOpenAccount=synBalanceService.getBankOpenAccount(user.getUserId());
        if(bankOpenAccount==null){
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            _log.info("-------------------用户未开户--------------------");
            resultBean.setStatusDesc("用户未开户");
            
            LogUtil.endLog(this.getClass().getName(), SynBalanceDefine.SYNBALANCE_BANK_ACTION);
            return resultBean;
        }
        //查询时间段 (只查当天)
        //上线需放开
        String startDate = getTxDate(user.getUserId());
        String endDate = GetOrderIdUtils.getTxDate();
        
       /* String startDate = "20171008";
        String endDate = "20171008";*/

        //页码定义
        int pageNum = 1;
        int pageSize = 10;
        // 查询参数定义
        String inpDate = "";
        String inpTime = "";
        String relDate = "";
        String traceNo = "";
        //调用查询明细接口 查线下充值数据
        BankCallBean retBean = synBalanceService.queryAccountDetails(user.getUserId(), bankOpenAccount.getAccount(), 
                startDate, endDate, "1", "", String.valueOf(pageNum), String.valueOf(pageSize),inpDate,inpTime,relDate,traceNo);

        if(retBean == null){
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            _log.info("-------------------同步余额失败--------------------");
            resultBean.setStatusDesc("同步余额失败");
            
            LogUtil.endLog(this.getClass().getName(), SynBalanceDefine.SYNBALANCE_BANK_ACTION);
            return resultBean;
        }
        //返回失败
        if(!BankCallConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())){
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            _log.info("-------------------调用查询接口失败，失败原因：" + synBalanceService.getBankRetMsg(retBean.getRetCode())+"--------------------");
            resultBean.setStatusDesc("调用查询接口失败，失败原因：" + synBalanceService.getBankRetMsg(retBean.getRetCode()));
            
            LogUtil.endLog(this.getClass().getName(), SynBalanceDefine.SYNBALANCE_BANK_ACTION);
            return resultBean;
        }
        //总记录数
        //解析返回数据(记录为空)
        String content = retBean.getSubPacks();
        if(StringUtils.isEmpty(content)){
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
            resultBean.setOriginalBankTotal(accountUser.getBankTotal().toString());
            resultBean.setOriginalBankBalance(accountUser.getBankBalance().toString());
            resultBean.setBankTotal(df.format(accountUser.getBankTotal()));
            resultBean.setBankBalance(df.format(accountBalance));
            LogUtil.endLog(this.getClass().getName(), SynBalanceDefine.SYNBALANCE_BANK_ACTION);
            return resultBean;
        }

        //返回结果记录数
        //转换结果
        List<ResultBean> recordList = new ArrayList<ResultBean>();
        List<ResultBean> list = new ArrayList<ResultBean>();
        list = JSONArray.parseArray(retBean.getSubPacks(), ResultBean.class);
        if(list==null|| list.size()==0){
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
            resultBean.setOriginalBankTotal(accountUser.getBankTotal().toString());
            resultBean.setOriginalBankBalance(accountUser.getBankBalance().toString());
            resultBean.setBankTotal(df.format(accountUser.getBankTotal()));
            resultBean.setBankBalance(df.format(accountBalance));
            LogUtil.endLog(this.getClass().getName(), SynBalanceDefine.SYNBALANCE_BANK_ACTION);
            return resultBean;
        }else{
            ResultBean lastResult = list.get(list.size()-1);
            inpDate = lastResult.getInpDate();
            inpTime = lastResult.getInpTime();
            relDate = lastResult.getRelDate();
            traceNo = String.valueOf(lastResult.getTraceNo());
        }
        recordList.addAll(list);
        
        while (list.size()==pageSize) {
            pageNum ++;
            //调用查询明细接口 查线下充值数据
            BankCallBean bean = synBalanceService.queryAccountDetails(user.getUserId(), bankOpenAccount.getAccount(),
                    startDate, endDate, "1", "", String.valueOf(pageNum), String.valueOf(pageSize),inpDate,inpTime,relDate,traceNo);
            if(bean == null){
                continue;
            }
            list = JSONArray.parseArray(bean.getSubPacks(), ResultBean.class);
            recordList.addAll(list);
        }
        
        _log.info("-------------------"+recordList.size()+"同步余额总条数--------------------");
        _log.info("-------------------"+pageNum+"同步余额请求次数--------------------");

        /**redis 锁 */
        boolean reslut = RedisUtils.tranactionSet("synBalance:"+user.getUserId(),30);
		// 如果没有设置成功，说明有请求来设置过
		if(!reslut){
			//成功???
            resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
            resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
            resultBean.setOriginalBankTotal(accountUser.getBankTotal().toString());
            resultBean.setOriginalBankBalance(accountUser.getBankBalance().toString());
            resultBean.setBankTotal(df.format(accountUser.getBankTotal()));
            resultBean.setBankBalance(df.format(accountBalance));
            LogUtil.endLog(this.getClass().getName(), SynBalanceDefine.SYNBALANCE_BANK_ACTION);
            return resultBean;
		}
        for (int i = 0; i < recordList.size(); i++) {
            ResultBean record = recordList.get(i);
            //是否冲正订单
            if(record != null && ("O".equals(record.getOrFlag()) || "0".equals(record.getOrFlag()))){ //原始订单，冲正订单不处理
                //是否是线下充值交易类型
                boolean result1 = isRechargeTransType(record.getTranType());
                if(result1){
                    SynBalanceBean synBalanceBean=new SynBalanceBean();
                    BeanUtils.copyProperties(record, synBalanceBean);
                    
                    boolean flag=false;
    
                    try {
                        flag = synBalanceService.insertAccountDetails(accountUser,synBalanceBean,user.getUsername(),GetCilentIP.getIpAddr(request));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(flag){
                        //更新金额
                        accountBalance = accountBalance.add(record.getTxAmount());
                    }
                }
            }
        }
        _log.info("-------------------"+synBalanceRequestBean.getUserId()+"同步余额结束--------------------");
        accountUser = synBalanceService.getAccount(user.getUserId());
        resultBean.setOriginalBankTotal(accountUser.getBankTotal().toString());
        resultBean.setOriginalBankBalance(accountUser.getBankBalance().toString());
        resultBean.setBankBalance(df.format(accountUser.getBankBalance()));
        resultBean.setBankTotal(df.format(accountUser.getBankTotal()));
        resultBean.setStatus(BaseResultBean.STATUS_SUCCESS);
        resultBean.setStatusDesc(BaseResultBean.STATUS_DESC_SUCCESS);
        
        LogUtil.endLog(this.getClass().getName(), SynBalanceDefine.SYNBALANCE_BANK_ACTION);
        return resultBean;
    }
    
    /**
     * 
     * 查询用户线下充值明细并同步余额（后台admin用）
     * @author dxj
     * @param request
     * @param response
     * @return
     */
    @ResponseBody
    @RequestMapping(value = SynBalanceDefine.SYNBALANCE_EXCEPTION_ACTION)
    public JSONObject synException(@ModelAttribute SynBalanceRequestBean synBalanceRequestBean, HttpServletRequest request, HttpServletResponse response) {
    	LogUtil.startLog(this.getClass().getName(), SynBalanceDefine.SYNBALANCE_EXCEPTION_ACTION);
        JSONObject result = new JSONObject();

        _log.info(synBalanceRequestBean.getBankAccount()+"同步余额开始-----------------------------");
        //验签
        if(!this.checkSign(synBalanceRequestBean, SynBalanceDefine.METHOD_SYNBALANCE)){
            _log.info("-------------------验签失败！--------------------");
            result.put("status", 1);
            return result;
        }
        //用户可用余额
        Account accountUser = synBalanceService.getAccount(synBalanceRequestBean.getUserId());
        BigDecimal accountBalance = accountUser.getBankBalance();
        //查询时间段
        String startDate = StringUtils.isEmpty(synBalanceRequestBean.getStartTimeSrch())?GetOrderIdUtils.getTxDate():StringUtils.remove(synBalanceRequestBean.getStartTimeSrch(), "-");
        String endDate = StringUtils.isEmpty(synBalanceRequestBean.getEndTimeSrch())?GetOrderIdUtils.getTxDate():StringUtils.remove(synBalanceRequestBean.getEndTimeSrch(), "-");
        
        //页码定义
        int pageNum = 1;
        int pageSize = 10;
        // 查询参数定义
        String inpDate = "";
        String inpTime = "";
        String relDate = "";
        String traceNo = "";
        //调用查询明细接口 查线下充值数据
        BankCallBean retBean = synBalanceService.queryAccountDetails(synBalanceRequestBean.getUserId(), synBalanceRequestBean.getBankAccount(), 
                startDate, endDate, "1", "", String.valueOf(pageNum), String.valueOf(pageSize),inpDate,inpTime,relDate,traceNo);

        if(retBean == null){
            _log.info("-------------------同步余额失败--------------------");
            result.put("status", 1);
            return result;
        }
        //返回失败
        if(!BankCallConstant.RESPCODE_SUCCESS.equals(retBean.getRetCode())){
            _log.info("-------------------调用查询接口失败，失败原因：" + synBalanceService.getBankRetMsg(retBean.getRetCode())+"--------------------");

            result.put("status", 1);
            return result;
        }
        //解析返回数据(记录为空)
        String content = retBean.getSubPacks();
        if(StringUtils.isEmpty(content)){
            result.put("status", 1);
            return result;
        }
        //返回结果记录数
        //转换结果
        List<ResultBean> recordList = new ArrayList<ResultBean>();
        List<ResultBean> list = new ArrayList<ResultBean>();
        list = JSONArray.parseArray(retBean.getSubPacks(), ResultBean.class);
        if(list==null|| list.size()==0){
            result.put("status", 1);
            return result;
        }else{
            ResultBean lastResult = list.get(list.size()-1);
            inpDate = lastResult.getInpDate();
            inpTime = lastResult.getInpTime();
            relDate = lastResult.getRelDate();
            traceNo = String.valueOf(lastResult.getTraceNo());
        }
        recordList.addAll(list);
        
        while (list.size()==pageSize) {
            pageNum ++;
            //调用查询明细接口 查线下充值数据
            BankCallBean bean = synBalanceService.queryAccountDetails(synBalanceRequestBean.getUserId(), synBalanceRequestBean.getBankAccount(),
                    startDate, endDate, "1", "", String.valueOf(pageNum), String.valueOf(pageSize),inpDate,inpTime,relDate,traceNo);
            if(bean == null){
                continue;
            }
            list = JSONArray.parseArray(bean.getSubPacks(), ResultBean.class);
            recordList.addAll(list);
        }
        _log.info("-------------------"+recordList.size()+"同步余额总条数--------------------");
        _log.info("-------------------"+pageNum+"同步余额请求次数--------------------");

        result.put("data", recordList);
        /**redis 锁 */
        boolean validateStatus = RedisUtils.tranactionSet("synBalance:"+synBalanceRequestBean.getUserId(),30);
		// 如果没有设置成功，说明有请求来设置过
		if(!validateStatus){
            return result;
		}
        for (int i = 0; i < recordList.size(); i++) {
            ResultBean record = recordList.get(i);
            //是否冲正订单
            if(record != null && ("O".equals(record.getOrFlag()) || "0".equals(record.getOrFlag()))){ //原始订单，冲正订单不处理
                //是否是线下充值交易类型
                boolean isType = isRechargeTransType(record.getTranType());
                if(isType){
                    SynBalanceBean synBalanceBean=new SynBalanceBean();
                    BeanUtils.copyProperties(record, synBalanceBean);
                    
                    boolean flag=false;
        
                    try {
                        flag = synBalanceService.insertAccountDetails(accountUser,synBalanceBean,synBalanceRequestBean.getUsername(),GetCilentIP.getIpAddr(request));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(flag){
                        //更新金额
                        accountBalance = accountBalance.add(record.getTxAmount());
                    }
                }
            }
        }
        // 返回线下充值明细列表
        return result;
    }
    
    private boolean chkSysBalanceSign(BaseBean paramBean) {
    	String sign = StringUtils.EMPTY;
		String accessKey = PropUtils.getSystem(ApplyDefine.AOP_INTERFACE_ACCESSKEY);
		
    	SynBalanceRequestBean bean=(SynBalanceRequestBean)paramBean;
        sign = StringUtils.lowerCase(MD5.toMD5Code(accessKey + bean.getBankAccount() + bean.getTimestamp() + accessKey));
        
        return StringUtils.equals(sign, paramBean.getChkValue()) ? true : false;
	}
    
    public String getTxDate(Integer userId) {
        SimpleDateFormat dft = new SimpleDateFormat("yyyyMMdd");
        Date beginDate = new Date();
        Calendar date = Calendar.getInstance();
        date.setTime(beginDate);
        date.set(Calendar.DATE, date.get(Calendar.DATE) - 2);
        
        String startDate = dft.format(date.getTime());
        
        BankOpenAccount bankOpenAccount=synBalanceService.getBankOpenAccount(userId);
        if(bankOpenAccount==null){
            return GetOrderIdUtils.getTxDate();
        }
        Date createTime=bankOpenAccount.getCreateTime();
        String startDate1 ="";
        startDate1=dft.format(createTime);

        try {
            if((DateDistance.getDistanceDays2(GetOrderIdUtils.getTxDate(),startDate1))>=2){
                return startDate;
            }else{
                return startDate1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return GetOrderIdUtils.getTxDate();
    }
}
