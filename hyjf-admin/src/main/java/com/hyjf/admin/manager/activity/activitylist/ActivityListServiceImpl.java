package com.hyjf.admin.manager.activity.activitylist;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.web.servlet.ShiroHttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

import com.alibaba.fastjson.JSONObject;
import com.hyjf.admin.BaseServiceImpl;
import com.hyjf.admin.manager.borrow.borrowcommon.BorrowCommonImage;
import com.hyjf.admin.shiro.ShiroUtil;
import com.hyjf.common.file.UploadFileUtils;
import com.hyjf.common.util.GetDate;
import com.hyjf.common.util.PropUtils;
import com.hyjf.mybatis.model.auto.Account;
import com.hyjf.mybatis.model.auto.AccountExample;
import com.hyjf.mybatis.model.auto.AccountList;
import com.hyjf.mybatis.model.auto.AccountRecharge;
import com.hyjf.mybatis.model.auto.AccountWebList;
import com.hyjf.mybatis.model.auto.ActivityF1;
import com.hyjf.mybatis.model.auto.ActivityF1Example;
import com.hyjf.mybatis.model.auto.ActivityList;
import com.hyjf.mybatis.model.auto.ActivityListExample;
import com.hyjf.mybatis.model.customize.ActivityListCustomize;
import com.hyjf.mybatis.model.customize.UserInfoCustomize;
import com.hyjf.pay.lib.chinapnr.ChinapnrBean;

@Service
public class ActivityListServiceImpl extends BaseServiceImpl implements ActivityListService {

    /**
     * 获取活动列表列表
     * 
     * @return
     */
    public List<ActivityList> getRecordList(ActivityList ActivityList, int limitStart, int limitEnd) {
        ActivityListExample example = new ActivityListExample();

        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        example.setOrderByClause("`create_time` Desc");
        return activityListMapper.selectByExample(example);
    }

    /**
     * 获取单个权限维护
     * 
     * @return
     */
    public ActivityList getRecord(Integer record) {
        ActivityList list = activityListMapper.selectByPrimaryKey(record);
        return list;
    }

    /**
     * 根据主键判断权限维护中数据是否存在
     * 
     * @return
     */
    public boolean isExistsRecord(ActivityList record) {
        if (record.getId() == null) {
            return false;
        }
        ActivityListExample example = new ActivityListExample();
        ActivityListExample.Criteria cra = example.createCriteria();
        cra.andIdEqualTo(record.getId());
        List<ActivityList> ActivityListList = activityListMapper.selectByExample(example);
        if (ActivityListList != null && ActivityListList.size() > 0) {
            return true;
        }
        return false;
    }

    /**
     * 权限维护插入
     * 
     * @param record
     */
    public void insertRecord(ActivityList record) {
        record.setCreateTime(GetDate.getMyTimeInMillis());
        record.setUpdateTime(GetDate.getMyTimeInMillis());
        activityListMapper.insertSelective(record);
    }

    /**
     * 权限维护更新
     * 
     * @param record
     */
    public void updateRecord(ActivityList record) {
        record.setUpdateTime(GetDate.getMyTimeInMillis());
        activityListMapper.updateByPrimaryKeySelective(record);
    }

    /**
     * 活动删除
     * 
     * @param recordList
     */
    @Override
    public void deleteRecord(List<Integer> recordList) {
        for (Integer id : recordList) {
            activityListMapper.deleteByPrimaryKey(id);
        }

    }

    /**
     * 根据条件查询数据
     */
    public List<ActivityList> selectRecordList(ActivityListBean form, int limitStart, int limitEnd) {
        ActivityListCustomize example = new ActivityListCustomize();
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        // 获取创建时间
        if (StringUtils.isNotEmpty(form.getStartCreate())) {
            example.setStartCreate(Integer.valueOf(GetDate.get10Time(form.getStartCreate())));
        }
        if (StringUtils.isNotEmpty(form.getEndCreate())) {
            example.setEndCreate(Integer.valueOf(GetDate.get10Time(form.getEndCreate())));
        }
        if (StringUtils.isNotEmpty(form.getStartTime())) {
            example.setStartTime(Integer.valueOf(GetDate.get10Time(form.getStartTime())));
        }
        if (StringUtils.isNotEmpty(form.getEndTime())) {
            example.setEndTime(Integer.valueOf(GetDate.get10Time(form.getEndTime())));
        }
        // 获取活动名
        example.setTitle(form.getTitle());
        return activityListCustomizeMapper.selectActivityList(example);
    }

    /**
     * 获取有效的活动列表
     */
    public List<ActivityListCustomize> selectRecordListValid(ActivityListBean form, int limitStart, int limitEnd){
        ActivityListCustomize example = new ActivityListCustomize();
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        example.setNowTime(GetDate.getNowTime10());
        return activityListCustomizeMapper.queryActivityListValid(example);
    }
    
    /**
     * 资料上传
     * 
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public String uploadFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ShiroHttpServletRequest shiroRequest = (ShiroHttpServletRequest) request;
        CommonsMultipartResolver commonsMultipartResolver = new CommonsMultipartResolver();
        MultipartHttpServletRequest multipartRequest = commonsMultipartResolver.resolveMultipart((HttpServletRequest) shiroRequest.getRequest());
        String fileDomainUrl = UploadFileUtils.getDoPath(PropUtils.getSystem("file.domain.url"));
        String filePhysicalPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.physical.path"));
        String fileUploadTempPath = UploadFileUtils.getDoPath(PropUtils.getSystem("file.upload.activity.img.path"));

        String logoRealPathDir = filePhysicalPath + fileUploadTempPath;

        File logoSaveFile = new File(logoRealPathDir);
        if (!logoSaveFile.exists()) {
            logoSaveFile.mkdirs();
        }

        BorrowCommonImage fileMeta = null;
        LinkedList<BorrowCommonImage> files = new LinkedList<BorrowCommonImage>();

        Iterator<String> itr = multipartRequest.getFileNames();
        MultipartFile multipartFile = null;

        while (itr.hasNext()) {
            multipartFile = multipartRequest.getFile(itr.next());
            String fileRealName = String.valueOf(new Date().getTime());
            String originalFilename = multipartFile.getOriginalFilename();
            fileRealName = fileRealName + UploadFileUtils.getSuffix(multipartFile.getOriginalFilename());

            // 文件大小
            String errorMessage = UploadFileUtils.upload4Stream(fileRealName, logoRealPathDir, multipartFile.getInputStream(), 5000000L);

            fileMeta = new BorrowCommonImage();
            int index = originalFilename.lastIndexOf(".");
            if (index != -1) {
                fileMeta.setImageName(originalFilename.substring(0, index));
            } else {
                fileMeta.setImageName(originalFilename);
            }

            fileMeta.setImageRealName(fileRealName);
            fileMeta.setImageSize(multipartFile.getSize() / 1024 + "");// KB
            fileMeta.setImageType(multipartFile.getContentType());
            fileMeta.setErrorMessage(errorMessage);
            // 获取文件路径
            fileMeta.setImagePath(fileUploadTempPath + fileRealName);
            fileMeta.setImageSrc(fileDomainUrl + fileUploadTempPath + fileRealName);
            files.add(fileMeta);
        }
        return JSONObject.toJSONString(files, true);
    }

    
    /**
     * 
     * 根据活动Type获取活动详情列表
     * @author liuyang
     * @param activity_type
     * @return
     */
    @Override
    public List<ActivityF1> selectActivityF1ListByActivityType(String activityType, int limitStart, int limitEnd) {

        ActivityF1Example example = new ActivityF1Example();
        if (limitStart != -1) {
            example.setLimitStart(limitStart);
            example.setLimitEnd(limitEnd);
        }
        ActivityF1Example.Criteria cra = example.createCriteria();
        cra.andSpeedNotEqualTo(0);

        example.setOrderByClause("`speed` Desc,`tender_account_all` Desc,`update_time` Desc");
        return activityF1Mapper.selectByExample(example);
    }

    /**
     * 
     * 根据用户id活动详情
     * @autho liuyang
     * @param user_id
     * @return
     */
    @Override
    public ActivityF1 selectActivityF1ByUserId(Integer user_id) {

        return activityF1Mapper.selectByPrimaryKey(user_id);
    }

    /**
     * 
     * 根据用户id查询活动详情数
     * @author liuyang
     * @param user_id
     * @return
     */
    @Override
    public int getActivityCountByUserId(Integer user_id) {
        ActivityF1Example example = new ActivityF1Example();
        ActivityF1Example.Criteria cra = example.createCriteria();
        cra.andUserIdEqualTo(user_id);
        return activityF1Mapper.countByExample(example);
    }

    /**
     * 
     * 根据用户id查询用户部门 信息
     * @author liuyang
     * @param userId
     * @return
     * @see com.hyjf.admin.manager.activity.activitylist.ActivityListService#queryUserInfoByUserId(java.lang.Integer)
     */
    @Override
    public UserInfoCustomize queryUserInfoByUserId(Integer userId) {
        return userInfoCustomizeMapper.queryUserInfoByUserId(userId);
    }

    /**
     * 
     * 根据检索条件检索活动件数
     * @author liuyang
     * @param form
     * @param limitStart
     * @param limitEnd
     * @return
     * @see com.hyjf.admin.manager.activity.activitylist.ActivityListService#countActivityInfo(com.hyjf.admin.manager.activity.activitylist.ActivityF1Bean,
     *      int, int)
     */
    @Override
    public int countActivityInfo(Map<String, Object> parm) {

        return activityF1CustomizeMapper.queryActivityF1Count(parm);
    }

    /**
     * 
     * 根据检索条件检索活动列表
     * @author liuyang
     * @param form
     * @param limitStart
     * @param limitEnd
     * @return
     * @see com.hyjf.admin.manager.activity.activitylist.ActivityListService#selectActivityInfoList(com.hyjf.admin.manager.activity.activitylist.ActivityF1Bean,
     *      int, int)
     */
    @Override
    public List<ActivityF1> selectActivityInfoList(Map<String, Object> parm, int limitStart, int limitEnd) {
        if (limitStart == 0 || limitStart > 0) {
            parm.put("limitStart", limitStart);
        }
        if (limitEnd > 0) {
            parm.put("limitEnd", limitEnd);
        }
        return activityF1CustomizeMapper.selectActivityF1List(parm);
    }

    /**
     * 
     * 返现操作
     * @author liuyang
     * @param activityF1
     * @param bean
     * @param userInfo
     * @return
     * @see com.hyjf.admin.manager.activity.activitylist.ActivityListService#insertReturncashRecord(com.hyjf.mybatis.model.auto.ActivityF1,
     *      com.hyjf.pay.lib.chinapnr.ChinapnrBean,
     *      com.hyjf.mybatis.model.customize.UserInfoCustomize)
     */
    public int insertReturncashRecord(ActivityF1 activityF1, ChinapnrBean bean, UserInfoCustomize userInfo) {
        int ret = 0;
        // 功能描述
        String note = "五月份活动返现";
        // 增加时间
        Integer time = GetDate.getMyTimeInMillis();
        // 用户ID
        Integer userId = activityF1.getUserId();
        // 操作者用户名
        String operator = ShiroUtil.getLoginUsername();
        ActivityF1 activityF1Info = this.selectActivityF1ByUserId(userId);
        if("1".equals(activityF1Info.getIsReturnFlg())){
             return -1;
        }
        // 更新账户信息
        AccountExample accountExample = new AccountExample();
        AccountExample.Criteria accountCriteria = accountExample.createCriteria();
        accountCriteria.andUserIdEqualTo(userId);
        Account account = accountMapper.selectByExample(accountExample).get(0);
        BigDecimal money = activityF1.getReturnAmount();// 充值金额
        account.setTotal(account.getTotal().add(money)); // 累加到账户总资产
        account.setBalance(account.getBalance().add(money)); // 累加可用余额
        account.setIncome(account.getIncome().add(money));// 累加到总收入
        ret += this.accountMapper.updateByExampleSelective(account, accountExample);

        // 写入收支明细
        AccountList accountList = new AccountList();
        accountList.setNid(bean.getOrdId());
        accountList.setUserId(userId);
        accountList.setAmount(money);
        accountList.setType(1);// 1收入2支出3冻结
        accountList.setTrade("borrowactivity");
        accountList.setTradeCode("balance");
        // accountList.setTotal(account.getTotal().add(money));
        accountList.setTotal(account.getTotal());// 在上一步操作’更新账户信息’时已经加过了，所以不能再加了！
        // accountList.setBalance(account.getBalance().add(money));
        accountList.setBalance(account.getBalance());// 在上一步操作’更新账户信息’时已经加过了，所以不能再加了！
        accountList.setFrost(account.getFrost());
        accountList.setAwait(account.getAwait());
        accountList.setRepay(account.getRepay());
        accountList.setRemark(note);// chinapnrBean.getLogRemark()
        accountList.setCreateTime(time);
        accountList.setOperator(operator);
        accountList.setIp(bean.getLogIp());
        accountList.setIsUpdate(0);
        accountList.setBaseUpdate(0);
        accountList.setInterest(null);
        accountList.setWeb(0);
        ret += this.accountListMapper.insertSelective(accountList);

        // 写入充值表
        AccountRecharge accountRecharge = new AccountRecharge();
        accountRecharge.setNid(bean.getOrdId());
        accountRecharge.setUserId(userId);
        accountRecharge.setStatus(1);
        accountRecharge.setMoney(money);
        accountRecharge.setBalance(money);
        accountRecharge.setFee(new BigDecimal(0));
        accountRecharge.setPayment(bean.getOpenBankId());// 充值银行 暂缺
        accountRecharge.setGateType("ADMIN");
        accountRecharge.setType(0);// 线下充值
        accountRecharge.setRemark(note);
        accountRecharge.setCreateTime(time);
        accountRecharge.setOperator(operator);
        accountRecharge.setAddtime(time.toString());
        accountRecharge.setAddip(bean.getLogIp());
        accountRecharge.setIsok(0);
        accountRecharge.setClient(0);// 0pc 1app
        accountRecharge.setIsok11(0);
        accountRecharge.setFlag(0);
        accountRecharge.setActivityFlag(0);
        ret += this.accountRechargeMapper.insertSelective(accountRecharge);

        // 写入网站收支
        AccountWebList accountWebList = new AccountWebList();
        accountWebList.setOrdid(bean.getOrdId());
        accountWebList.setAmount(money);
        accountWebList.setType(2);// 1收入2支出
        accountWebList.setTrade("recharge");
        accountWebList.setTradeType("平台转账");
        accountWebList.setUserId(userId);
        accountWebList.setUsrcustid(bean.getUsrCustId());
        accountWebList.setTruename(activityF1 == null ? null : activityF1.getRealName());
        accountWebList.setRegionName(userInfo == null ? null : userInfo.getRegionName());
        accountWebList.setBranchName(userInfo == null ? null : userInfo.getBranchName());
        accountWebList.setDepartmentName(userInfo == null ? null : userInfo.getDepartmentName());
        accountWebList.setRemark(bean.getLogRemark());
        accountWebList.setCreateTime(time);
        accountWebList.setOperator(operator);
        accountWebList.setFlag(1);
        ret += this.accountWebListMapper.insertSelective(accountWebList);

        // 活动表更新
        activityF1.setIsReturnFlg("1");
        activityF1.setReturnTime(time);
        ret += this.activityF1Mapper.updateByPrimaryKeySelective(activityF1);

        return ret;
    }

}
